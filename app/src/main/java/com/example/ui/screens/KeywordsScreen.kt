package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.BlockedKeyword
import com.example.model.MatchType
import com.example.model.TargetArea
import com.example.ui.components.AddEditKeywordDialog
import com.example.ui.components.KeywordItemCard
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppStrings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeywordsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val filteredKeywords by viewModel.filteredKeywords.collectAsStateWithLifecycle()
    val allKeywords by viewModel.allKeywords.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val language = settings.language

    var showAddDialog by remember { mutableStateOf(false) }
    var editingKeyword by remember { mutableStateOf<BlockedKeyword?>(null) }
    var deletingKeyword by remember { mutableStateOf<BlockedKeyword?>(null) }
    var showImportExportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }

    val categories = listOf("ALL", "Custom", "Clickbait", "Spoilers", "Pranks & Drama", "Distraction", "Gossip", "Politics")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = AppStrings.get("btn_add_keyword", language)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Screen Header & Action Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = AppStrings.get("keywords", language),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${filteredKeywords.size} ${if (language == "bn") "টি কীওয়ার্ড প্রদর্শিত" else "keywords shown"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { showImportExportDialog = true }) {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = "Import / Export",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text(AppStrings.get("search_keywords", language)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )
            }

            // Category Filter Chips Row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory.equals(cat, ignoreCase = true),
                            onClick = { viewModel.setSelectedCategory(cat) },
                            label = {
                                Text(
                                    if (cat == "ALL") AppStrings.get("filter_all", language) else cat
                                )
                            }
                        )
                    }
                }
            }

            // Empty State
            if (filteredKeywords.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Key,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = AppStrings.get("no_keywords_title", language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = AppStrings.get("no_keywords_desc", language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(AppStrings.get("btn_add_keyword", language))
                            }
                        }
                    }
                }
            } else {
                items(filteredKeywords, key = { it.id }) { kw ->
                    KeywordItemCard(
                        keyword = kw,
                        onToggle = { isEnabled -> viewModel.toggleKeyword(kw.id, isEnabled) },
                        onEdit = { editingKeyword = kw },
                        onDelete = { deletingKeyword = kw }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Add Keyword Dialog
    if (showAddDialog) {
        AddEditKeywordDialog(
            initialKeyword = null,
            language = language,
            onDismiss = { showAddDialog = false },
            onSave = { text, cat, matchType, targetArea, isCaseSensitive ->
                viewModel.addKeyword(text, cat, matchType, targetArea, isCaseSensitive)
                showAddDialog = false
                Toast.makeText(
                    context,
                    if (language == "bn") "কীওয়ার্ড সফলভাবে যুক্ত হয়েছে" else "Keyword added",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    // Edit Keyword Dialog
    editingKeyword?.let { kw ->
        AddEditKeywordDialog(
            initialKeyword = kw,
            language = language,
            onDismiss = { editingKeyword = null },
            onSave = { text, cat, matchType, targetArea, isCaseSensitive ->
                viewModel.updateKeyword(
                    kw.copy(
                        keyword = text,
                        category = cat,
                        matchType = matchType,
                        targetArea = targetArea,
                        isCaseSensitive = isCaseSensitive
                    )
                )
                editingKeyword = null
                Toast.makeText(
                    context,
                    if (language == "bn") "কীওয়ার্ড আপডেট করা হয়েছে" else "Keyword updated",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    // Delete Confirmation Dialog
    deletingKeyword?.let { kw ->
        AlertDialog(
            onDismissRequest = { deletingKeyword = null },
            title = {
                Text(
                    text = if (language == "bn") "কীওয়ার্ড মুছবেন?" else "Delete Keyword?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = if (language == "bn") "'${kw.keyword}' কীওয়ার্ডটি মুছে ফেলতে চান?"
                    else "Are you sure you want to remove '${kw.keyword}'?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteKeyword(kw)
                        deletingKeyword = null
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(AppStrings.get("btn_delete", language))
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingKeyword = null }) {
                    Text(AppStrings.get("btn_cancel", language))
                }
            }
        )
    }

    // Import / Export Dialog
    if (showImportExportDialog) {
        AlertDialog(
            onDismissRequest = { showImportExportDialog = false },
            title = {
                Text(
                    text = if (language == "bn") "কীওয়ার্ড ব্যাকআপ ও ইম্পোর্ট" else "Backup & Import Keywords",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (language == "bn") "আপনার কীওয়ার্ড তালিকা ব্যাকআপ করতে কপি করুন অথবা JSON পেস্ট করে ইম্পোর্ট করুন।"
                        else "Copy your keyword list to clipboard or paste JSON to import rules.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = { importJsonText = it },
                        placeholder = { Text(if (language == "bn") "এখানে JSON পেস্ট করুন…" else "Paste JSON here…") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    val json = viewModel.exportJson()
                                    clipboardManager.setText(AnnotatedString(json))
                                    Toast.makeText(
                                        context,
                                        if (language == "bn") "ক্লিপবোর্ডে কপি করা হয়েছে!" else "Copied to clipboard!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (language == "bn") "এক্সপোর্ট (কপি)" else "Export (Copy)")
                        }

                        Button(
                            onClick = {
                                if (importJsonText.isNotBlank()) {
                                    coroutineScope.launch {
                                        val count = viewModel.importJson(importJsonText)
                                        showImportExportDialog = false
                                        importJsonText = ""
                                        Toast.makeText(
                                            context,
                                            if (language == "bn") "$count টি কীওয়ার্ড ইম্পোর্ট হয়েছে!" else "Imported $count keywords!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            enabled = importJsonText.isNotBlank(),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (language == "bn") "ইম্পোর্ট" else "Import")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showImportExportDialog = false }) {
                    Text(AppStrings.get("btn_cancel", language))
                }
            }
        )
    }
}
