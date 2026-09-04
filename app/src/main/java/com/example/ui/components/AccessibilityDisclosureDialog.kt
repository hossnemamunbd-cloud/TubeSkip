package com.example.ui.components

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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SettingsAccessibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SoftMintGreen
import com.example.util.AppStrings

/**
 * In-App Accessibility Disclosure Dialog
 *
 * Fully compliant with Google Play Prominent Disclosure & Consent requirements:
 * - Shown to user BEFORE requesting the Accessibility permission.
 * - Explicitly explains what data is accessed, how it is used, and what is NOT accessed.
 */
@Composable
fun AccessibilityDisclosureDialog(
    language: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val isBn = language == "bn"

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Rounded.SettingsAccessibility,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = AppStrings.get("accessibility_disclosure_title", language),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Mandatory explicit statement
                Text(
                    text = AppStrings.get("accessibility_disclosure_message", language),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                DisclosureBullet(
                    icon = Icons.Rounded.CheckCircle,
                    iconTint = SoftMintGreen,
                    title = if (isBn) "যা করা হয়:" else "How it works:",
                    description = if (isBn)
                        "শুধুমাত্র ইউটিউব অ্যাপে দৃশ্যমান ভিডিও ও শর্টসের টাইটেল স্ক্যান করে আপনার কিওয়ার্ডের সাথে মেলায় এবং অটো-স্কিপ সোয়াইপ করে।"
                    else
                        "Reads visible video/Short titles and descriptions on-screen solely to detect matches with your blocked keywords and trigger the skip gesture."
                )

                Spacer(modifier = Modifier.height(10.dp))

                DisclosureBullet(
                    icon = Icons.Rounded.VisibilityOff,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = if (isBn) "যা করা হয় না:" else "What we NEVER do:",
                    description = if (isBn)
                        "স্ক্রিন রেকর্ড করা হয় না, ইউটিউব একাউন্ট ক্রেডেনশিয়াল এক্সেস করা হয় না এবং কোনো ভিডিও বা কিওয়ার্ড ডাটা সার্ভারে পাঠানো হয় না।"
                    else
                        "No screen recording, no account credential access, and zero data transmission. Your keywords and videos are never sent to external servers."
                )

                Spacer(modifier = Modifier.height(10.dp))

                DisclosureBullet(
                    icon = Icons.Rounded.Lock,
                    iconTint = SoftMintGreen,
                    title = if (isBn) "১০০% লোকাল প্রসেসিং:" else "100% On-Device:",
                    description = if (isBn)
                        "সমস্ত ফিল্টারিং এবং স্কিপ হিস্ট্রি সম্পূর্ণ আপনার ফোনেই সুরক্ষিতভাবে সংরক্ষিত থাকে।"
                    else
                        "All title matching and skip logging occurs strictly on your device using local SQLite storage."
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = AppStrings.get("btn_continue_to_settings", language),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.get("action_cancel", language))
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun DisclosureBullet(
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}
