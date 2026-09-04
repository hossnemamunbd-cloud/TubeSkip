package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.BlockedKeyword
import com.example.model.MatchType
import com.example.model.TargetArea
import com.example.util.AppStrings

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditKeywordDialog(
    initialKeyword: BlockedKeyword? = null,
    language: String,
    onDismiss: () -> Unit,
    onSave: (String, String, MatchType, TargetArea, Boolean) -> Unit
) {
    var keywordText by remember { mutableStateOf(initialKeyword?.keyword ?: "") }
    var category by remember { mutableStateOf(initialKeyword?.category ?: "Custom") }
    var matchType by remember { mutableStateOf(initialKeyword?.matchType ?: MatchType.CONTAINS) }
    var targetArea by remember { mutableStateOf(initialKeyword?.targetArea ?: TargetArea.ALL) }
    var isCaseSensitive by remember { mutableStateOf(initialKeyword?.isCaseSensitive ?: false) }

    val presetCategories = listOf(
        "Custom", "Clickbait", "Spoilers", "Pranks & Drama", "Distraction", "Gossip", "Politics"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = if (initialKeyword == null) Icons.Rounded.Add else Icons.Rounded.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = if (initialKeyword == null) AppStrings.get("dialog_add_title", language)
                else AppStrings.get("dialog_edit_title", language),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = keywordText,
                    onValueChange = { keywordText = it },
                    label = { Text(AppStrings.get("label_keyword_text", language)) },
                    placeholder = { Text(AppStrings.get("hint_keyword_text", language)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Category Selection
                Text(
                    text = AppStrings.get("label_category", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetCategories.forEach { cat ->
                        FilterChip(
                            selected = category.equals(cat, ignoreCase = true),
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                // Match Type Selection
                Text(
                    text = AppStrings.get("label_match_type", language),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MatchType.values().forEach { type ->
                        FilterChip(
                            selected = matchType == type,
                            onClick = { matchType = type },
                            label = {
                                Text(
                                    when (type) {
                                        MatchType.CONTAINS -> AppStrings.get("match_contains", language)
                                        MatchType.EXACT -> AppStrings.get("match_exact", language)
                                        MatchType.STARTS_WITH -> AppStrings.get("match_starts", language)
                                        MatchType.REGEX -> AppStrings.get("match_regex", language)
                                    }
                                )
                            }
                        )
                    }
                }

                // Case Sensitive toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isCaseSensitive,
                        onCheckedChange = { isCaseSensitive = it }
                    )
                    Text(
                        text = AppStrings.get("label_case_sensitive", language),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (keywordText.isNotBlank()) {
                        onSave(keywordText, category, matchType, targetArea, isCaseSensitive)
                    }
                },
                enabled = keywordText.isNotBlank()
            ) {
                Text(AppStrings.get("btn_save", language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.get("btn_cancel", language))
            }
        }
    )
}
