package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.TeluguGreen
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.model.FestivalCategory
import com.example.model.FestivalItem
import com.example.viewmodel.PanchangaViewModel
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

enum class FestivalScopeMode(val labelTe: String, val labelEn: String) {
    CURRENT_MONTH("ఈ నెల పండుగలు", "Current Month"),
    UPCOMING("రాబోయేవి", "Upcoming"),
    FULL_YEAR("సంవత్సరం మొత్తం", "Full Year")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalsScreen(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val allFestivals by viewModel.allFestivalsForYear.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentFilter by viewModel.festivalCategoryFilter.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val bookmarks by viewModel.bookmarks.collectAsState()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var activeMonthDate by remember { mutableStateOf(selectedDate) }
    var scopeMode by remember { mutableStateOf(FestivalScopeMode.CURRENT_MONTH) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var monthDropdownExpanded by remember { mutableStateOf(false) }

    // Keep activeMonthDate in sync when user picks a date from calendar
    LaunchedEffect(selectedDate) {
        if (activeMonthDate.year != selectedDate.year || activeMonthDate.month != selectedDate.month) {
            activeMonthDate = selectedDate
        }
    }

    val filteredList = remember(allFestivals, searchQuery, currentFilter, scopeMode, activeMonthDate) {
        val today = LocalDate.now()
        allFestivals.filter { item ->
            val matchesCategory = when (currentFilter) {
                FestivalCategory.ALL -> true
                FestivalCategory.AMAVASYA -> item.category == FestivalCategory.AMAVASYA || item.name.contains("అమావాస్య", ignoreCase = true) || item.id.contains("amavasya", ignoreCase = true)
                FestivalCategory.POURNAMI -> item.category == FestivalCategory.POURNAMI || item.name.contains("పౌర్ణమి", ignoreCase = true) || item.id.contains("pournami", ignoreCase = true)
                FestivalCategory.EKADASHI -> item.category == FestivalCategory.EKADASHI || item.name.contains("ఏకాదశి", ignoreCase = true) || item.id.contains("ekadashi", ignoreCase = true)
                else -> item.category == currentFilter
            }

            val matchesScope = when (scopeMode) {
                FestivalScopeMode.CURRENT_MONTH -> item.date.month == activeMonthDate.month && item.date.year == activeMonthDate.year
                FestivalScopeMode.UPCOMING -> !item.date.isBefore(today)
                FestivalScopeMode.FULL_YEAR -> item.date.year == activeMonthDate.year
            }

            val matchesQuery = searchQuery.isBlank() ||
                    item.name.contains(searchQuery, ignoreCase = true) ||
                    item.deity.contains(searchQuery, ignoreCase = true) ||
                    item.summary.contains(searchQuery, ignoreCase = true)

            matchesCategory && matchesScope && matchesQuery
        }.sortedBy { it.date }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("festivals_screen")
    ) {
        // Month Selector Bar with Nav Buttons
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        activeMonthDate = activeMonthDate.minusMonths(1)
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "మునుపటి నెల",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                monthDropdownExpanded = true
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        val displayMonthText = if (language == AppLanguage.TE) {
                            "${LocalizationEngine.getTeluguMonthName(activeMonthDate.monthValue)} ${activeMonthDate.year}"
                        } else {
                            "${activeMonthDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${activeMonthDate.year}"
                        }

                        Text(
                            text = displayMonthText,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "నెల ఎంపిక",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    DropdownMenu(
                        expanded = monthDropdownExpanded,
                        onDismissRequest = { monthDropdownExpanded = false }
                    ) {
                        Month.values().forEach { month ->
                            DropdownMenuItem(
                                text = {
                                    val name = if (language == AppLanguage.TE) LocalizationEngine.getTeluguMonthName(month.value) else month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                                    Text("$name ${activeMonthDate.year}", fontWeight = if (month == activeMonthDate.month) FontWeight.Bold else FontWeight.Normal)
                                },
                                onClick = {
                                    activeMonthDate = LocalDate.of(activeMonthDate.year, month, 1)
                                    monthDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        activeMonthDate = activeMonthDate.plusMonths(1)
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "తరువాతి నెల",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Compact Sleek Search Bar (Reduced height, clean borders, and responsive clear/dismiss)
        BasicTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 12.5.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            if (searchQuery.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "శోధన",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = if (language == AppLanguage.TE) "పండుగ లేదా వ్రతం శోధించండి..." else "Search festival or vrat...",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.setSearchQuery("")
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            },
                            modifier = Modifier.size(22.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "క్లియర్",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("festival_search_bar")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Unified Filter Buttons Row (All buttons have matching, uniform height and style)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Dropdown Filter Chip ("అన్ని పండుగలు" / Selected Category)
            Box {
                val categoryLabel = when (currentFilter) {
                    FestivalCategory.ALL -> if (language == AppLanguage.TE) "🌸 అన్ని పండుగలు" else "🌸 All Festivals"
                    FestivalCategory.AMAVASYA -> if (language == AppLanguage.TE) "🌑 అమావాస్యలు" else "🌑 Amavasya"
                    FestivalCategory.POURNAMI -> if (language == AppLanguage.TE) "🌕 పౌర్ణమిలు" else "🌕 Pournami"
                    FestivalCategory.EKADASHI -> if (language == AppLanguage.TE) "🙏 ఏకాదశులు" else "🙏 Ekadashis"
                    FestivalCategory.MAJOR_FESTIVALS -> if (language == AppLanguage.TE) "🪔 ప్రధాన పండుగలు" else "🪔 Major Festivals"
                    FestivalCategory.VRATHAMS -> if (language == AppLanguage.TE) "🌺 వ్రతాలు" else "🌺 Vrats"
                    FestivalCategory.JAYANTI -> if (language == AppLanguage.TE) "🕉️ జయంతులు" else "🕉️ Jayantis"
                    else -> LocalizationEngine.translateFestivalCategory(currentFilter.label, language)
                }

                FilterChip(
                    selected = currentFilter != FestivalCategory.ALL,
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        categoryDropdownExpanded = true
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = categoryLabel,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "వర్గం ఎంపిక",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )

                DropdownMenu(
                    expanded = categoryDropdownExpanded,
                    onDismissRequest = { categoryDropdownExpanded = false }
                ) {
                    val filterOptions = listOf(
                        Pair(FestivalCategory.ALL, if (language == AppLanguage.TE) "🌸 అన్ని పండుగలు & ఉపవాసాలు" else "🌸 All Festivals & Vrats"),
                        Pair(FestivalCategory.AMAVASYA, if (language == AppLanguage.TE) "🌑 అమావాస్యలు" else "🌑 Amavasya"),
                        Pair(FestivalCategory.POURNAMI, if (language == AppLanguage.TE) "🌕 పౌర్ణమిలు" else "🌕 Pournami"),
                        Pair(FestivalCategory.EKADASHI, if (language == AppLanguage.TE) "🙏 ఏకాదశి ఉపవాసాలు" else "🙏 Ekadashi Fasting"),
                        Pair(FestivalCategory.MAJOR_FESTIVALS, if (language == AppLanguage.TE) "🪔 ప్రధాన పండుగలు" else "🪔 Major Festivals"),
                        Pair(FestivalCategory.VRATHAMS, if (language == AppLanguage.TE) "🌺 వ్రతాలు & నోములు" else "🌺 Vrathams"),
                        Pair(FestivalCategory.JAYANTI, if (language == AppLanguage.TE) "🕉️ జయంతిలు & ఉత్సవాలు" else "🕉️ Jayantis")
                    )

                    filterOptions.forEach { (cat, label) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = label,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (cat == currentFilter) FontWeight.Bold else FontWeight.Normal,
                                    color = if (cat == currentFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                viewModel.setFestivalFilter(cat)
                                categoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Scope Filter Chips: "ఈ నెల పండుగలు", "రాబోయేవి", "సంవత్సరం మొత్తం"
            FestivalScopeMode.values().forEach { mode ->
                FilterChip(
                    selected = mode == scopeMode,
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        scopeMode = mode
                    },
                    label = {
                        Text(
                            text = if (language == AppLanguage.TE) mode.labelTe else mode.labelEn,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Counter text
        val scopeText = when (scopeMode) {
            FestivalScopeMode.CURRENT_MONTH -> if (language == AppLanguage.TE) "${LocalizationEngine.getTeluguMonthName(activeMonthDate.monthValue)} నెల పండుగలు" else "${activeMonthDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} Festivals"
            FestivalScopeMode.UPCOMING -> if (language == AppLanguage.TE) "రాబోయే పండుగలు" else "Upcoming Festivals"
            FestivalScopeMode.FULL_YEAR -> if (language == AppLanguage.TE) "${activeMonthDate.year} మొత్తం పండుగలు" else "${activeMonthDate.year} Full Year Festivals"
        }

        Text(
            text = "$scopeText (${filteredList.size})",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Festival List with Modern, Clean, User-Friendly Cards
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🪔", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == AppLanguage.TE) "ఈ విభాగంలో ఎలాంటి పండుగలు లభించలేదు" else "No festivals found in this section",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { fest ->
                    val today = LocalDate.now()
                    val daysDiff = ChronoUnit.DAYS.between(today, fest.date)
                    val daysPillText = when {
                        daysDiff == 0L -> LocalizationEngine.get("today", language)
                        daysDiff == 1L -> if (language == AppLanguage.TE) "రేపు" else if (language == AppLanguage.HI) "कल" else "Tomorrow"
                        daysDiff > 0L -> if (language == AppLanguage.TE) "$daysDiff రోజుల్లో" else if (language == AppLanguage.HI) "$daysDiff दिनों में" else "In $daysDiff days"
                        else -> if (language == AppLanguage.TE) "${-daysDiff} రోజుల క్రితం" else "Past"
                    }

                    // Modern Festive Card with temple devotional styling
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_festival_${fest.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        border = BorderStroke(1.dp, Color(0xFFFFE082)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Top Header: Date badge + Festival Name + Days pill
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFF3E0)) // Warm Devotional Saffron Fill
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Saffron Date Container
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF8D1808), // Sacred Temple Kumkum Red
                                        border = BorderStroke(1.dp, Color(0xFFFFE082)),
                                        modifier = Modifier.size(width = 46.dp, height = 48.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "${fest.date.dayOfMonth}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = fest.date.month.name.take(3),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFFFFECB3)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = fest.iconEmoji, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = LocalizationEngine.translateFestivalName(fest.name, language),
                                                fontSize = 14.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = LocalizationEngine.formatLocalizedDate(fest.date, language),
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (daysDiff in 0..7) TeluguGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = daysPillText,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (daysDiff in 0..7) TeluguGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 1.dp)

                            // Card Body
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                // Full Summary
                                Text(
                                    text = LocalizationEngine.translateFestivalSummary(fest.id, fest.summary, language),
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                // Significance if available
                                val significance = LocalizationEngine.translateFestivalSignificance(fest.id, fest.significance, language)
                                if (significance.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = if (language == AppLanguage.TE) "✨ ప్రాముఖ్యత" else "✨ Significance",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = significance,
                                                fontSize = 11.5.sp,
                                                lineHeight = 17.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }

                                // Puja Muhurta or Parana Timing
                                if (fest.pujaMuhurta != null || fest.paranaTime != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        fest.pujaMuhurta?.let { muhurta ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(0xFFE6FCF5),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "✨ పూజా ముహూర్తం: $muhurta",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF2B8A3E),
                                                    modifier = Modifier.padding(6.dp)
                                                )
                                            }
                                        }
                                        fest.paranaTime?.let { parana ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(0xFFFFF4E6),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "🍽️ పారణ సమయం: $parana",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFFD9480F),
                                                    modifier = Modifier.padding(6.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.5.dp)
                                Spacer(modifier = Modifier.height(8.dp))

                                // Bottom Action Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${LocalizationEngine.get("deity", language)}: ${LocalizationEngine.translateDeity(fest.deity, language)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF868E96)
                                    )

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val isBookmarked = bookmarks.any { it.id == "fest_${fest.id}" }
                                        IconButton(
                                            onClick = { viewModel.toggleBookmarkFestival(context, fest) },
                                            modifier = Modifier
                                                .size(32.dp)
                                                .testTag("btn_bookmark_festival_${fest.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = if (isBookmarked) "Saved in My Favorites" else "Save to My Favorites",
                                                tint = if (isBookmarked) Color(0xFFE53935) else Color(0xFFB0BEC5),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Button(
                                            onClick = {
                                                viewModel.setSelectedDate(fest.date)
                                                viewModel.setActiveTab(0)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                        ) {
                                            Text("పంచాంగం ›", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Spacer(modifier = Modifier.width(4.dp))

                                        FilledTonalIconButton(
                                            onClick = { viewModel.openShareCard(fest) },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .size(32.dp)
                                                .testTag("btn_share_festival_${fest.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = "షేర్ చేయండి",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
