package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.DevotionalQuote
import com.example.engine.DevotionalQuoteRepository
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.model.BookmarkEntity
import com.example.ui.theme.DeepGold
import com.example.ui.theme.LightGold
import com.example.ui.theme.WhatsAppGreen
import com.example.viewmodel.PanchangaViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookmarks by viewModel.bookmarks.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()

    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "QUOTES", "FESTIVALS", "DATES"
    var selectedQuoteDialog by remember { mutableStateOf<DevotionalQuote?>(null) }

    val filteredBookmarks = remember(bookmarks, selectedFilter) {
        when (selectedFilter) {
            "QUOTES" -> bookmarks.filter { it.type == "DEVOTIONAL_QUOTE" }
            "FESTIVALS" -> bookmarks.filter { it.type == "FESTIVAL" }
            "DATES" -> bookmarks.filter { it.type == "DATE" || it.type == "JANMA_PANCHANGA" }
            else -> bookmarks
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("saved_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.TE) "భద్రపరిచిన అంశాలు (My Favorites)" else "My Favorites & Bookmarks",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = if (language == AppLanguage.TE) "మీ ఇష్టమైన భక్తి సూక్తులు, పండుగలు మరియు శుభ తేదీలు" else "Your saved devotional quotes, festivals and auspicious dates",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val quotesCount = bookmarks.count { it.type == "DEVOTIONAL_QUOTE" }
            val festsCount = bookmarks.count { it.type == "FESTIVAL" }
            val datesCount = bookmarks.count { it.type == "DATE" || it.type == "JANMA_PANCHANGA" }

            FilterChip(
                selected = selectedFilter == "ALL",
                onClick = { selectedFilter = "ALL" },
                label = {
                    Text(
                        if (language == AppLanguage.TE) "అన్నీ (${bookmarks.size})" else "All (${bookmarks.size})",
                        fontSize = 12.sp
                    )
                }
            )

            FilterChip(
                selected = selectedFilter == "QUOTES",
                onClick = { selectedFilter = "QUOTES" },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(14.dp)
                    )
                },
                label = {
                    Text(
                        if (language == AppLanguage.TE) "భక్తి సూక్తులు ($quotesCount)" else "Devotional Quotes ($quotesCount)",
                        fontSize = 12.sp
                    )
                }
            )

            FilterChip(
                selected = selectedFilter == "FESTIVALS",
                onClick = { selectedFilter = "FESTIVALS" },
                label = {
                    Text(
                        if (language == AppLanguage.TE) "పండుగలు ($festsCount)" else "Festivals ($festsCount)",
                        fontSize = 12.sp
                    )
                }
            )

            FilterChip(
                selected = selectedFilter == "DATES",
                onClick = { selectedFilter = "DATES" },
                label = {
                    Text(
                        if (language == AppLanguage.TE) "తేదీలు ($datesCount)" else "Dates ($datesCount)",
                        fontSize = 12.sp
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredBookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "No Saved Items",
                        tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == AppLanguage.TE) "ఇంకా ఏమీ సేవ్ చేయలేదు" else "No saved items yet.",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.TE) {
                            "హోమ్ స్క్రీన్‌లోని 'నిత్య భక్తి సూక్తి' పై ఉన్న హార్ట్ (❤️) చిహ్నాన్ని లేదా పంచాంగంలోని తేదీలు, పండుగల బుక్‌మార్క్ చిహ్నాన్ని నొక్కి ఇక్కడ భద్రపరుచుకోవచ్చు."
                        } else {
                            "Tap the Heart (❤️) icon on the Daily Devotional Quote or bookmark dates and festivals to save them to your collection."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredBookmarks, key = { it.id }) { bookmark ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (bookmark.type == "DATE") {
                                    try {
                                        val parsedDate = LocalDate.parse(bookmark.rawDate)
                                        viewModel.setSelectedDate(parsedDate)
                                        viewModel.setActiveTab(0) // Jump to Today tab
                                    } catch (e: Exception) {
                                        // Ignore parse errors
                                    }
                                } else if (bookmark.type == "JANMA_PANCHANGA") {
                                    viewModel.loadBirthPanchangaFromBookmark(bookmark)
                                } else if (bookmark.type == "DEVOTIONAL_QUOTE") {
                                    val quoteId = bookmark.id.removePrefix("quote_").toIntOrNull()
                                    val quote = DevotionalQuoteRepository.DAILY_QUOTES.find { it.id == quoteId }
                                    if (quote != null) {
                                        selectedQuoteDialog = quote
                                    }
                                } else {
                                    // Open festival detail
                                    val festId = bookmark.id.removePrefix("fest_")
                                    val festItem = viewModel.allFestivalsForYear.value.find { it.id == festId }
                                    if (festItem != null) {
                                        viewModel.selectFestivalForDetail(festItem)
                                    }
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                        border = if (bookmark.type == "DEVOTIONAL_QUOTE") {
                            androidx.compose.foundation.BorderStroke(1.dp, LightGold.copy(alpha = 0.45f))
                        } else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (bookmark.type == "DEVOTIONAL_QUOTE") {
                                        Color(0xFF4A0E17).copy(alpha = 0.10f)
                                    } else {
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = bookmark.icon, fontSize = 20.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = bookmark.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = bookmark.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when (bookmark.type) {
                                            "DEVOTIONAL_QUOTE" -> Color(0xFFE53935).copy(alpha = 0.12f)
                                            "DATE" -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                            "JANMA_PANCHANGA" -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                                            else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                        }
                                    ) {
                                        Text(
                                            text = when (bookmark.type) {
                                                "DEVOTIONAL_QUOTE" -> if (language == AppLanguage.TE) "భక్తి సూక్తి ❤️" else "Devotional Quote ❤️"
                                                "DATE" -> if (language == AppLanguage.TE) "తేదీ" else "Specific Date"
                                                "JANMA_PANCHANGA" -> if (language == AppLanguage.TE) "జన్మ పంచాంగం" else "Birth Panchangam"
                                                else -> if (language == AppLanguage.TE) "పండుగ" else "Festival"
                                            },
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (bookmark.type) {
                                                "DEVOTIONAL_QUOTE" -> Color(0xFFC62828)
                                                else -> MaterialTheme.colorScheme.primary
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (bookmark.type == "DATE") {
                                            viewModel.toggleBookmarkDate(context, LocalDate.parse(bookmark.rawDate), "", "")
                                        } else if (bookmark.type == "JANMA_PANCHANGA") {
                                            viewModel.toggleBookmarkJanmaPanchanga(context, bookmark.id, "", "", "")
                                        } else if (bookmark.type == "DEVOTIONAL_QUOTE") {
                                            viewModel.deleteBookmarkById(context, bookmark.id)
                                        } else {
                                            val festId = bookmark.id.removePrefix("fest_")
                                            val festItem = viewModel.allFestivalsForYear.value.find { it.id == festId }
                                            if (festItem != null) {
                                                viewModel.toggleBookmarkFestival(context, festItem)
                                            } else {
                                                viewModel.deleteBookmarkById(context, bookmark.id)
                                            }
                                        }
                                    },
                                    modifier = Modifier.testTag("btn_remove_bookmark_${bookmark.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove Bookmark",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Go",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Dialog for Saved Devotional Quote
    selectedQuoteDialog?.let { quote ->
        AlertDialog(
            onDismissRequest = { selectedQuoteDialog = null },
            shape = RoundedCornerShape(20.dp),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = quote.icon, fontSize = 22.sp)
                    Column {
                        Text(
                            text = if (language == AppLanguage.TE) quote.titleTelugu else quote.titleEnglish,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${quote.source} • ${quote.deityOrContext}",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(0.6.dp, LightGold.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "“ ${quote.slokaOrSukti} ”",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "🌺 తాత్పర్యం:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = quote.meaningTelugu,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "English:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = quote.meaningEnglish,
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareText = DevotionalQuoteRepository.formatShareText(quote, LocalDate.now())
                        val sendIntent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(android.content.Intent.createChooser(sendIntent, "భక్తి సూక్తిని షేర్ చేయండి"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (language == AppLanguage.TE) "షేర్ చేయండి" else "Share", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedQuoteDialog = null }) {
                    Text(if (language == AppLanguage.TE) "ముగించు" else "Close")
                }
            }
        )
    }
}
