package com.example.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.example.TubeSkipApp
import com.example.model.BlockedKeyword
import com.example.model.MatchType
import com.example.model.VideoType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayDeque

class YouTubeBlockerAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mainHandler = Handler(Looper.getMainLooper())

    @Volatile
    private var activeKeywords: List<BlockedKeyword> = emptyList()

    @Volatile
    private var lastSkippedTitle: String = ""

    @Volatile
    private var lastSkipTimestamp: Long = 0L

    @Volatile
    private var masterActive: Boolean = true

    @Volatile
    private var skipFullVideos: Boolean = true

    @Volatile
    private var skipShorts: Boolean = true

    @Volatile
    private var showSkipToast: Boolean = true

    @Volatile
    private var cooldownMs: Long = 700L

    private var screenWidth: Int = 1080
    private var screenHeight: Int = 2400

    override fun onServiceConnected() {
        super.onServiceConnected()
        isRunning = true
        updateScreenDimensions()
        observeKeywordsAndSettings()
    }

    private fun updateScreenDimensions() {
        val dm: DisplayMetrics = resources.displayMetrics
        if (dm.widthPixels > 0) screenWidth = dm.widthPixels
        if (dm.heightPixels > 0) screenHeight = dm.heightPixels
    }

    private fun observeKeywordsAndSettings() {
        val app = application as? TubeSkipApp ?: return

        // Observe active keywords instantly
        serviceScope.launch {
            app.keywordRepository.activeKeywords.collectLatest { keywords ->
                activeKeywords = keywords
            }
        }

        // Observe settings
        serviceScope.launch {
            app.settingsRepository.settings.collectLatest { settings ->
                masterActive = settings.isMasterActive
                skipFullVideos = settings.skipFullVideos
                skipShorts = settings.skipShorts
                showSkipToast = settings.showSkipToast
                cooldownMs = (settings.skipCooldownSeconds * 500L).coerceIn(400L, 2000L)
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || !masterActive) return
        val packageName = event.packageName?.toString() ?: return

        if (!isYouTubePackage(packageName)) return

        val now = System.currentTimeMillis()
        if (now - lastSkipTimestamp < cooldownMs) return

        val rootNode = try {
            rootInActiveWindow
        } catch (e: Exception) {
            null
        } ?: return

        try {
            inspectAndSkipIfNeeded(rootNode)
        } catch (e: Exception) {
            // Safe execution
        }
    }

    private fun isYouTubePackage(pkg: String): Boolean {
        return pkg.contains("youtube", ignoreCase = true) ||
                pkg == "com.google.android.youtube" ||
                pkg == "app.revanced.android.youtube" ||
                pkg == "app.rvx.android.youtube" ||
                pkg == "com.vanced.android.youtube" ||
                pkg == "com.google.android.apps.youtube.mango"
    }

    private fun inspectAndSkipIfNeeded(rootNode: AccessibilityNodeInfo) {
        val keywords = activeKeywords
        if (keywords.isEmpty()) return

        val (extractedTexts, nextButtonNode, isLikelyShorts) = scanNodeHierarchy(rootNode)

        if (extractedTexts.isEmpty()) return

        val match = findMatchedKeyword(extractedTexts, keywords) ?: return
        val (matchedKeyword, matchedText) = match

        val now = System.currentTimeMillis()
        if (matchedText == lastSkippedTitle && now - lastSkipTimestamp < (cooldownMs * 2)) {
            return
        }

        lastSkippedTitle = matchedText
        lastSkipTimestamp = now

        // Check whether this is a Short or Full Video
        if (isLikelyShorts || nextButtonNode == null) {
            if (skipShorts) {
                performShortsSwipeSkip(matchedKeyword, matchedText)
            }
        } else {
            if (skipFullVideos) {
                performFullVideoSkip(nextButtonNode, matchedKeyword, matchedText)
            }
        }
    }

    private data class ScanResult(
        val texts: List<String>,
        val nextButtonNode: AccessibilityNodeInfo?,
        val isLikelyShorts: Boolean
    )

    private fun scanNodeHierarchy(root: AccessibilityNodeInfo): ScanResult {
        val texts = ArrayList<String>(40)
        var nextButton: AccessibilityNodeInfo? = null
        var shortsDetected = false

        val queue = ArrayDeque<AccessibilityNodeInfo>()
        queue.add(root)

        var nodesInspected = 0
        val maxNodes = 120

        while (queue.isNotEmpty() && nodesInspected < maxNodes) {
            val node = queue.poll() ?: continue
            nodesInspected++

            // Check view ID
            val viewId = node.viewIdResourceName
            if (viewId != null) {
                if (viewId.contains("reel", ignoreCase = true) ||
                    viewId.contains("shorts", ignoreCase = true)
                ) {
                    shortsDetected = true
                }

                if (viewId.endsWith("next_button") || viewId.contains("next_button", ignoreCase = true)) {
                    if (node.isClickable) {
                        nextButton = node
                    }
                }
            }

            // Collect text
            val text = node.text?.toString()
            if (!text.isNullOrBlank()) {
                val clean = text.trim()
                if (clean.length >= 2) {
                    texts.add(clean)
                }
            }

            // Collect content description
            val contentDesc = node.contentDescription?.toString()
            if (!contentDesc.isNullOrBlank()) {
                val clean = contentDesc.trim()
                if (clean.length >= 2) {
                    texts.add(clean)
                    if (clean.contains("Shorts", ignoreCase = true) ||
                        clean.contains("Remix", ignoreCase = true) ||
                        clean.contains("Sound", ignoreCase = true)
                    ) {
                        shortsDetected = true
                    }
                    if (clean.contains("Next video", ignoreCase = true) ||
                        clean.contains("পরবর্তী ভিডিও", ignoreCase = true)
                    ) {
                        if (node.isClickable) {
                            nextButton = node
                        }
                    }
                }
            }

            val childCount = node.childCount
            for (i in 0 until childCount) {
                val child = node.getChild(i)
                if (child != null) {
                    queue.add(child)
                }
            }
        }

        return ScanResult(texts, nextButton, shortsDetected)
    }

    private fun findMatchedKeyword(
        texts: List<String>,
        keywords: List<BlockedKeyword>
    ): Pair<BlockedKeyword, String>? {
        for (text in texts) {
            val trimmed = text.trim()
            if (trimmed.length < 2) continue

            for (kw in keywords) {
                if (!kw.isEnabled) continue
                if (isKeywordMatch(trimmed, kw)) {
                    return Pair(kw, trimmed)
                }
            }
        }

        // Also check combined string of top texts for phrase matches
        val combined = texts.take(15).joinToString(" ")
        for (kw in keywords) {
            if (!kw.isEnabled) continue
            if (isKeywordMatch(combined, kw)) {
                return Pair(kw, combined.take(60))
            }
        }

        return null
    }

    private fun isKeywordMatch(text: String, keyword: BlockedKeyword): Boolean {
        val pattern = keyword.keyword.trim()
        if (pattern.isEmpty()) return false

        val sourceText = if (keyword.isCaseSensitive) text else text.lowercase()
        val matchPattern = if (keyword.isCaseSensitive) pattern else pattern.lowercase()

        return when (keyword.matchType) {
            MatchType.CONTAINS -> sourceText.contains(matchPattern)
            MatchType.EXACT -> sourceText.equals(matchPattern, ignoreCase = !keyword.isCaseSensitive)
            MatchType.STARTS_WITH -> sourceText.startsWith(matchPattern)
            MatchType.REGEX -> try {
                val regex = if (keyword.isCaseSensitive) Regex(pattern) else Regex(pattern, RegexOption.IGNORE_CASE)
                regex.containsMatchIn(text)
            } catch (e: Exception) {
                sourceText.contains(matchPattern)
            }
        }
    }

    private fun performShortsSwipeSkip(keyword: BlockedKeyword, title: String) {
        updateScreenDimensions()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val swipePath = Path().apply {
                val startX = screenWidth * 0.5f
                val startY = screenHeight * 0.78f
                val endX = screenWidth * 0.5f
                val endY = screenHeight * 0.18f
                moveTo(startX, startY)
                lineTo(endX, endY)
            }

            // Snappy 120ms vertical swipe for instant short skip
            val stroke = GestureDescription.StrokeDescription(swipePath, 0, 120)
            val gesture = GestureDescription.Builder().addStroke(stroke).build()

            dispatchGesture(gesture, object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    onSkipExecuted(keyword, title, VideoType.SHORTS)
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                    onSkipExecuted(keyword, title, VideoType.SHORTS)
                }
            }, null)
        } else {
            onSkipExecuted(keyword, title, VideoType.SHORTS)
        }
    }

    private fun performFullVideoSkip(
        nextButton: AccessibilityNodeInfo,
        keyword: BlockedKeyword,
        title: String
    ) {
        var clicked = false
        try {
            var target: AccessibilityNodeInfo? = nextButton
            while (target != null && !target.isClickable) {
                target = target.parent
            }
            if (target != null && target.isClickable) {
                clicked = target.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } catch (e: Exception) {
            clicked = false
        }

        if (clicked) {
            onSkipExecuted(keyword, title, VideoType.VIDEO)
        } else {
            // Fallback to swipe gesture
            performShortsSwipeSkip(keyword, title)
        }
    }

    private fun onSkipExecuted(keyword: BlockedKeyword, title: String, videoType: VideoType) {
        val app = application as? TubeSkipApp ?: return

        serviceScope.launch {
            app.keywordRepository.recordSkip(
                title = title.take(120),
                matchedKeyword = keyword.keyword,
                videoType = videoType,
                keywordId = keyword.id
            )
        }

        if (showSkipToast) {
            mainHandler.post {
                val isBn = (application as? TubeSkipApp)?.settingsRepository?.settings?.value?.language == "bn"
                val message = if (isBn) {
                    "স্বয়ংক্রিয় স্কিপ: '${keyword.keyword}'"
                } else {
                    "Auto-skipped: '${keyword.keyword}'"
                }
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    companion object {
        var isRunning: Boolean = false
            private set

        fun isAccessibilityServiceEnabled(context: Context): Boolean {
            val expectedServiceName = "${context.packageName}/${YouTubeBlockerAccessibilityService::class.java.name}"
            val enabledServicesSetting = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

            val colonSplitter = TextUtils.SimpleStringSplitter(':')
            colonSplitter.setString(enabledServicesSetting)

            while (colonSplitter.hasNext()) {
                val componentName = colonSplitter.next()
                if (componentName.equals(expectedServiceName, ignoreCase = true) ||
                    componentName.contains(YouTubeBlockerAccessibilityService::class.java.simpleName)
                ) {
                    return true
                }
            }
            return false
        }
    }
}

