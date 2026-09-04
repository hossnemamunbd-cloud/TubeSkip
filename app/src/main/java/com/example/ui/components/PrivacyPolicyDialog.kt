package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdsClick
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContactMail
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.SettingsAccessibility
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.SoftAmberGold
import com.example.ui.theme.SoftMintGreen
import com.example.util.AdMobConfig
import com.example.util.AppStrings

/**
 * Full-fidelity Privacy Policy & Legal Disclaimer Dialog
 *
 * Implements full disclosures required for Google Play Store compliance:
 * - App Purpose & Functionality
 * - Android AccessibilityService specific usage
 * - Strict Personal Data Protections (No screen recording, no credential access, no keyword exfiltration)
 * - Google AdMob third-party data processing disclosure with official Google Privacy link
 * - Data Security & Storage (100% on-device SQLite)
 * - Data Retention and Complete User Deletion
 * - Children's Privacy
 * - Third-Party Services
 * - Policy Updates & Developer Contact Information
 * - Formal Third-Party Disclaimer (Non-affiliation with YouTube/Google LLC)
 */
@Composable
fun PrivacyPolicyDialog(
    language: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isBn = language == "bn"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Policy,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = AppStrings.get("privacy_policy_title", language),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isBn) "সর্বশেষ আপডেট: সেপ্টেম্বর ২০২৬" else "Last updated: September 2026",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) {
                    // 1. App Purpose
                    PolicySection(
                        icon = Icons.Rounded.Info,
                        title = if (isBn) "১. অ্যাপের মূল উদ্দেশ্য ও কার্যক্রম" else "1. App Purpose & Functionality",
                        body = if (isBn)
                            "TubeSkip (YT-Blocker) ব্যবহারকারীদের নিজস্ব নির্ধারিত কিওয়ার্ডের ভিত্তিতে ইউটিউব ভিডিও ও শর্টস অটোমেটিকভাবে স্কিপ/ব্লক করতে সহায়তা করে।"
                        else
                            "TubeSkip (YT-Blocker) helps users automatically skip and filter YouTube videos and YouTube Shorts based on their own custom, user-defined keyword rules."
                    )

                    // 2. Accessibility Service Usage
                    PolicySection(
                        icon = Icons.Rounded.SettingsAccessibility,
                        title = if (isBn) "২. এক্সেসিবিলিটি সার্ভিসের সুনির্দিষ্ট ব্যবহার" else "2. AccessibilityService Usage",
                        body = if (isBn)
                            "এই অ্যাপটি শুধুমাত্র স্ক্রিনের দৃশ্যমান ইউটিউব ভিডিও টাইটেল ও শর্টস ক্যাপশন শনাক্ত করতে এবং কিওয়ার্ড ম্যাচ হলে ব্যবহারকারীর পক্ষ থেকে স্কিপ সোয়াইপ করতে Android AccessibilityService ব্যবহার করে।\n\n• স্ক্রিন রেকর্ড করা হয় না (No screen recording)\n• ইউটিউব একাউন্ট ক্রেডেনশিয়াল বা পাসওয়ার্ড এক্সেস করা হয় না\n• এক্সেসিবিলিটি ডাটা শুধুমাত্র এই নির্দিষ্ট স্কিপিং কাজের জন্যই ব্যবহৃত হয়।"
                        else
                            "This app uses Android AccessibilityService solely to inspect visible YouTube interface text (video titles, descriptions, and Shorts metadata) and execute the user's requested skip gesture when a blocked keyword is detected.\n\n• The app does NOT record the screen.\n• The app does NOT access YouTube account credentials or login details.\n• Accessibility data is used only for this automated skip functionality."
                    )

                    // 3. Personal Data Protection
                    PolicySection(
                        icon = Icons.Rounded.Lock,
                        title = if (isBn) "৩. ব্যক্তিগত তথ্যের সুরক্ষা ও গোপনীয়তা" else "3. Personal Data & Privacy",
                        body = if (isBn)
                            "আমরা কোনো ব্যক্তিগত তথ্য সংগ্রহ, বিক্রয় বা শেয়ার করি না। ব্যবহারকারীর ইনপুট করা কিওয়ার্ড বা ইউটিউবের কোনো কনটেন্ট আমাদের কোনো সার্ভারে পাঠানো হয় না। সমস্ত কিওয়ার্ড শুধুমাত্র অ্যাপের নিজস্ব ফিল্টারিং কাজে ব্যবহৃত হয়।"
                        else
                            "We do NOT collect, sell, or share personal data. The app does not send YouTube content, viewing history, or user-entered keywords to external servers. User keywords are stored locally and used exclusively for the app's blocking functionality."
                    )

                    // 4. Google AdMob Disclosure
                    PolicySection(
                        icon = Icons.Rounded.AdsClick,
                        title = if (isBn) "৪. বিজ্ঞাপন পার্টনার (Google AdMob)" else "4. Third-Party Advertising (Google AdMob)",
                        body = if (isBn)
                            "অ্যাপটির কার্যক্রম ও ফ্রি সেবা বজায় রাখতে Google AdMob SDK সংহত রয়েছে। গুগল অ্যাডমব তাদের নিজস্ব প্রাইভেসি নীতিমালা ও শর্ত অনুযায়ী বিজ্ঞাপন পরিবেশন ও প্রতারণা রোধে ডিভাইস আইডেন্টিফায়ার এবং প্রাসঙ্গিক ডায়াগনস্টিক ডাটা সংগ্রহ বা প্রক্রিয়াজাত করতে পারে।"
                        else
                            "This app integrates Google AdMob to display banner advertisements. Google AdMob may collect and process device identifiers, general location, and advertising telemetry in accordance with Google's Privacy Policy to serve ads, prevent fraud, and analyze ad traffic."
                    )

                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AdMobConfig.GOOGLE_PRIVACY_POLICY_URL)).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "Google প্রাইভেসি পলিসি দেখুন" else "View Google's Privacy Policy",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5. Data Security
                    PolicySection(
                        icon = Icons.Rounded.Security,
                        title = if (isBn) "৫. ডাটা সিকিউরিটি ও স্টোরেজ" else "5. Data Security",
                        body = if (isBn)
                            "আপনার সমস্ত সংরক্ষিত কিওয়ার্ড এবং স্কিপ পরিসংখ্যান আপনার ডিভাইসের অভ্যন্তরীণ সুরক্ষিত SQLite/Room ডাটাবেসে স্থানীয়ভাবে এনক্রিপ্ট/সংরক্ষিত থাকে।"
                        else
                            "All blocked keywords, settings, and skip statistics are stored 100% locally on your device in a sandboxed Room/SQLite database. No user data is transmitted to or stored on remote servers."
                    )

                    // 6. Data Retention & Deletion
                    PolicySection(
                        icon = Icons.Rounded.DeleteOutline,
                        title = if (isBn) "৬. ডাটা মুছে ফেলা (Retention & Deletion)" else "6. Data Retention & Deletion",
                        body = if (isBn)
                            "আপনি যেকোনো সময় ড্যাশবোর্ড বা সেটিংস থেকে আপনার সংরক্ষিত কিওয়ার্ড ও স্কিপ হিস্ট্রি মুছে ফেলতে পারেন। অ্যাপটি আনইনস্টল করলে ডিভাইসের সমস্ত ডাটা তাৎক্ষণিকভাবে স্থায়ীভাবে মুছে যায়।"
                        else
                            "Users have complete control over their data. You can edit, export, or delete any blocked keyword or clear the skip logs at any time from within the app. Uninstalling the app permanently erases all local data from your device."
                    )

                    // 7. Children's Privacy
                    PolicySection(
                        icon = Icons.Rounded.ChildCare,
                        title = if (isBn) "৭. শিশুদের গোপনীয়তা (Children's Privacy)" else "7. Children's Privacy",
                        body = if (isBn)
                            "TubeSkip ১৩ বছরের কম বয়সী শিশুদের কাছ থেকে জেনেশুনে কোনো ব্যক্তিগত তথ্য সংগ্রহ করে না।"
                        else
                            "TubeSkip is not directed to children under the age of 13 and does not knowingly collect personal identifiable information from children."
                    )

                    // 8. Third-Party Services
                    PolicySection(
                        icon = Icons.Rounded.Sync,
                        title = if (isBn) "৮. তৃতীয় পক্ষের সেবা (Third-Party Services)" else "8. Third-Party Services",
                        body = if (isBn)
                            "অ্যাপটিতে Google Play Services এবং Google Mobile Ads (AdMob) ব্যবহার করা হয়েছে। এই পরিষেবাগুলো তাদের নিজস্ব নীতি দ্বারা পরিচালিত হয়।"
                        else
                            "The app utilizes standard Android platform services, Google Play Services, and Google Mobile Ads (AdMob) which operate under their respective privacy policies."
                    )

                    // 9. Policy Updates
                    PolicySection(
                        icon = Icons.Rounded.Policy,
                        title = if (isBn) "৯. পলিসি আপডেট (Policy Updates)" else "9. Policy Updates",
                        body = if (isBn)
                            "আমরা প্রয়োজনে এই নীতিমালা হালনাগাদ করতে পারি। যেকোনো পরিবর্তন অ্যাপ আপডেট এবং অফিসিয়াল প্রাইভেসি পলিসি পেজে প্রকাশ করা হবে।"
                        else
                            "We may update this Privacy Policy periodically. Continued use of the application after changes constitutes acceptance of the updated policy."
                    )

                    // 10. Contact Information
                    PolicySection(
                        icon = Icons.Rounded.ContactMail,
                        title = if (isBn) "১০. যোগাযোগ (Contact Information)" else "10. Contact Information",
                        body = if (isBn)
                            "প্রাইভেসি পলিসি বা অ্যাপ সংক্রান্ত যেকোনো তথ্যের জন্য যোগাযোগ করুন:\nEmail: hossnemamunbd@gmail.com\nDeveloper: Angle_Moni"
                        else
                            "For privacy inquiries, support, or questions regarding our data practices:\nEmail: hossnemamunbd@gmail.com\nDeveloper: Angle_Moni"
                    )

                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AdMobConfig.CONTACT_URL)).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.get("btn_open_contact", language),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 11. Separate Disclaimer Section (Requirement 3)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SoftAmberGold.copy(alpha = 0.12f))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.WarningAmber,
                                    contentDescription = null,
                                    tint = SoftAmberGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) "আইনি ডিসক্লেইমার (Legal Disclaimer)" else "Legal Disclaimer",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isBn)
                                    "• YT-Blocker (TubeSkip) একটি সম্পূর্ণ স্বাধীন থার্ড-পার্টি অ্যাপ্লিকেশন।\n" +
                                    "• এটি YouTube বা Google LLC-এর সাথে কোনোভাবেই সম্বন্ধযুক্ত, অনুমোদিত বা স্পনসরকৃত নয়।\n" +
                                    "• YouTube হলো Google LLC-এর একটি রেজিস্টার্ড ট্রেডমার্ক।\n" +
                                    "• অ্যাপটি শুধুমাত্র ব্যবহারকারীর নিজস্ব নির্দেশিত অ্যাকশন অটোমেট করে এবং ইউটিউবের কোনো ফাইল, কোড বা কার্যপ্রণালী পরিবর্তন করে না।\n" +
                                    "• YouTube-এর Terms of Service এবং প্রযোজ্য আইন মেনে চলা ব্যবহারকারীর নিজস্ব দায়িত্ব।"
                                else
                                    "• YT-Blocker (TubeSkip) is an independent third-party application.\n" +
                                    "• It is not affiliated with, endorsed by, or sponsored by YouTube or Google.\n" +
                                    "• YouTube is a registered trademark of Google LLC.\n" +
                                    "• The app only automates user-defined actions and does not modify YouTube.\n" +
                                    "• Users are responsible for complying with YouTube's Terms of Service and applicable laws.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AdMobConfig.DISCLAIMER_URL)).apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.get("btn_open_disclaimer", language),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Buttons: Open External URL + Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(AppStrings.get("btn_close", language))
                    }

                    Button(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(AdMobConfig.PRIVACY_POLICY_URL)).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    if (isBn) "ব্রাউজার খোলা সম্ভব হয়নি" else "Could not open browser",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(imageVector = Icons.Rounded.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "ওয়েবসাইটে দেখুন" else "Open in Browser",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PolicySection(
    icon: ImageVector,
    title: String,
    body: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp
        )
    }
}
