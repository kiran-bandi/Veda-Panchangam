package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.FestivalRepository
import com.example.engine.LocalizationEngine
import com.example.engine.MonthDataManager
import com.example.engine.YearlyCategory
import com.example.engine.YearlyPanchangaManager
import com.example.engine.YearlyPanchangaOverview
import com.example.model.AppLanguage
import com.example.ui.components.AutoResizedText
import com.example.ui.theme.*
import com.example.viewmodel.PanchangaViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

enum class CalendarViewMode(val title: String) {
    GRID("క్యాలెండర్ గ్రిడ్"),
    TABLE("నెలవారీ పట్టిక"),
    YEARLY("వార్షిక పంచాంగం")
}

@Composable
fun CalendarScreen(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val location by viewModel.selectedLocation.collectAsState()
    val tradition by viewModel.selectedTradition.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()
    val panchanga by viewModel.currentPanchanga.collectAsState()

    var displayedYearMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    var viewMode by remember { mutableStateOf(CalendarViewMode.GRID) }
    var yearlyFilter by remember { mutableStateOf(YearlyCategory.ALL) }

    // Keep displayed year-month in sync if user selects date from another month
    LaunchedEffect(selectedDate) {
        if (YearMonth.from(selectedDate) != displayedYearMonth) {
            displayedYearMonth = YearMonth.from(selectedDate)
        }
    }

    // Completely dynamic month data calculation for the displayed year and month
    val monthData = remember(displayedYearMonth, location, tradition, language) {
        MonthDataManager.getMonthPanchanga(displayedYearMonth, location, tradition, language)
    }

    // Dynamic yearly events for the entire 12 months (computed on-demand for Yearly view)
    val yearlyEvents = remember(displayedYearMonth.year, location, tradition, language, viewMode) {
        if (viewMode == CalendarViewMode.YEARLY) {
            MonthDataManager.getYearlyEvents(displayedYearMonth.year, location, tradition, language)
        } else {
            emptyList()
        }
    }

    val daysInMonth = displayedYearMonth.lengthOfMonth()
    val firstDayOfMonth = displayedYearMonth.atDay(1)
    // Sunday is 7 in java.time.DayOfWeek, we want Sunday=0 for traditional Telugu calendar grid
    val dayOfWeekOffset = if (firstDayOfMonth.dayOfWeek == DayOfWeek.SUNDAY) 0 else firstDayOfMonth.dayOfWeek.value

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp)
            .testTag("calendar_screen_column")
    ) {
        // 1. Navigation Header (YEAR -> MONTH -> DAY Hierarchy + Today Button)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)), // Sacred Temple Parchment
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082)), // Radiant Gold
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Year Navigation & Today Button Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { displayedYearMonth = displayedYearMonth.minusYears(1) },
                            modifier = Modifier.size(32.dp).testTag("btn_cal_prev_year")
                        ) {
                            Text("«", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8D1808))
                        }
                        Text(
                            text = "${displayedYearMonth.year}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF8D1808),
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        IconButton(
                            onClick = { displayedYearMonth = displayedYearMonth.plusYears(1) },
                            modifier = Modifier.size(32.dp).testTag("btn_cal_next_year")
                        ) {
                            Text("»", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8D1808))
                        }
                    }

                    // Today Button
                    Button(
                        onClick = {
                            val today = LocalDate.now()
                            displayedYearMonth = YearMonth.from(today)
                            viewModel.jumpToToday()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8D1808),
                            contentColor = Color(0xFFFFD54F)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F).copy(alpha = 0.6f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("btn_cal_today")
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventNote,
                            contentDescription = "నేడు",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "నేడు",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFFFE082).copy(alpha = 0.6f))

                // Month Navigation Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { displayedYearMonth = displayedYearMonth.minusMonths(1) },
                        modifier = Modifier.testTag("btn_cal_prev_month")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "మునుపటి నెల",
                            tint = Color(0xFF8D1808)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${LocalizationEngine.getTeluguMonthName(displayedYearMonth.monthValue)} ${displayedYearMonth.year}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = Color(0xFF8D1808)
                        )
                        Text(
                            text = monthData.teluguMonthHeader,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5A4D3D)
                        )
                    }

                    IconButton(
                        onClick = { displayedYearMonth = displayedYearMonth.plusMonths(1) },
                        modifier = Modifier.testTag("btn_cal_next_month")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "తదుపరి నెల",
                            tint = Color(0xFF8D1808)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. View Mode Toggle (Grid vs Table vs Yearly)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalendarViewMode.values().forEach { mode ->
                val isSelected = mode == viewMode
                Button(
                    onClick = { viewMode = mode },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF8D1808) else Color(0xFFFFFDF5),
                        contentColor = if (isSelected) Color.White else Color(0xFF5A4D3D)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFFFFD54F) else Color(0xFFFFE082)),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    val icon = when (mode) {
                        CalendarViewMode.GRID -> Icons.Default.CalendarViewMonth
                        CalendarViewMode.TABLE -> Icons.Default.TableChart
                        CalendarViewMode.YEARLY -> Icons.Default.EventNote
                    }
                    Icon(imageVector = icon, contentDescription = mode.title, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = mode.title,
                        fontSize = 10.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (viewMode) {
            CalendarViewMode.GRID -> {
                // Traditional Calendar Grid with Sunday in Red
                val weekDays = listOf(
                    DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
                )

                // Weekday Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weekDays.forEach { dow ->
                        val isSun = dow == DayOfWeek.SUNDAY
                        Text(
                            text = LocalizationEngine.getShortDayName(dow, language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSun) TeluguRed else Color(0xFF495057),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 11.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Calendar Days Grid
                val totalCells = ((dayOfWeekOffset + daysInMonth + 6) / 7) * 7
                val weeks = totalCells / 7

                for (w in 0 until weeks) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (d in 0..6) {
                            val dayIndex = w * 7 + d
                            val dayNumber = dayIndex - dayOfWeekOffset + 1

                            if (dayNumber in 1..daysInMonth) {
                                val cellDate = displayedYearMonth.atDay(dayNumber)
                                val isSelected = cellDate == selectedDate
                                val isToday = cellDate == LocalDate.now()
                                val isSunday = cellDate.dayOfWeek == DayOfWeek.SUNDAY

                                val dayItem = monthData.days.getOrNull(dayNumber - 1)
                                val hasFest = dayItem?.festivals?.isNotEmpty() == true
                                val isPournami = dayItem?.isPournami == true
                                val isAmavasya = dayItem?.isAmavasya == true
                                val isEkadashi = dayItem?.isEkadashi == true
                                val isNavami = dayItem?.isNavami == true

                                val shortTithi = dayItem?.tithi?.let {
                                    LocalizationEngine.getShortTithiTelugu(it.number)
                                } ?: ""

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(0.68f)
                                        .padding(1.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when {
                                                isSelected -> Color(0xFF8D1808) // Sacred Temple Crimson
                                                isToday -> Color(0xFFFFECB3) // Sacred Morning Gold Glow
                                                isPournami -> Color(0xFFFFF8E1) // Radiant Full-Moon Gold
                                                isEkadashi -> Color(0xFFE8F5E9) // Sacred Tulsi Green
                                                isAmavasya -> Color(0xFFECEFF1) // Soft Ash Moonlight
                                                hasFest -> Color(0xFFFFF3E0) // Festive Saffron Warmth
                                                else -> Color(0xFFFFFDF8) // Sacred Temple Canvas
                                            }
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else if (isToday) 1.5.dp else 0.8.dp,
                                            color = when {
                                                isSelected -> Color(0xFFFFD54F) // Radiant Gold border for selected
                                                isToday -> Color(0xFFE65100)
                                                isPournami -> Color(0xFFFFD54F)
                                                isEkadashi -> Color(0xFFA5D6A7)
                                                isAmavasya -> Color(0xFFB0BEC5)
                                                hasFest -> Color(0xFFFFCC80)
                                                else -> Color(0xFFE8E1D1) // Sandalwood border
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            viewModel.setSelectedDate(cellDate)
                                        }
                                        .testTag("cal_day_$dayNumber"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize().padding(horizontal = 0.5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "$dayNumber",
                                            fontSize = 13.5.sp,
                                            fontWeight = if (isSelected || isToday) FontWeight.Black else FontWeight.Bold,
                                            color = when {
                                                isSelected -> Color.White
                                                isToday -> Color(0xFF8D1808)
                                                isSunday -> Color(0xFF8D1808)
                                                else -> Color(0xFF211A13)
                                            }
                                        )

                                        if (shortTithi.isNotEmpty()) {
                                            Text(
                                                text = shortTithi,
                                                fontSize = 8.sp,
                                                lineHeight = 9.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isSelected) Color.White.copy(alpha = 0.95f) else Color(0xFF5A4D3D),
                                                maxLines = 1
                                            )
                                        }

                                        // Sacred Symbol Badge indicators (crisp, clearly visible, unclipped)
                                        val hasSymbols = isPournami || isAmavasya || isEkadashi || hasFest || isNavami
                                        if (hasSymbols) {
                                            Spacer(modifier = Modifier.height(1.dp))
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (isPournami) {
                                                    Text("🌕", fontSize = 11.5.sp)
                                                } else if (isAmavasya) {
                                                    Text("🌑", fontSize = 11.5.sp)
                                                } else if (isEkadashi) {
                                                    Text("🙏", fontSize = 10.5.sp)
                                                }

                                                if (hasFest) {
                                                    if (isPournami || isAmavasya || isEkadashi) Spacer(modifier = Modifier.width(1.dp))
                                                    Text("🪔", fontSize = 10.5.sp)
                                                } else if (isNavami && !isPournami && !isAmavasya && !isEkadashi) {
                                                    Text("🏹", fontSize = 10.5.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(0.68f)
                                        .padding(1.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }

                // Sacred Calendar Symbol Legend
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌕", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("పౌర్ణమి", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8D1808))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌑", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("అమావాస్య", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF37474F))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🙏", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("ఏకాదశి", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🪔", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("పండుగ", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Selected Day Panchangam Card with button to view full day details
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cal_selected_day_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f, fill = false)) {
                                Text(
                                    text = LocalizationEngine.formatLocalizedDate(selectedDate, language),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                    color = Color(0xFF8D1808),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${LocalizationEngine.translatePaksha(panchanga.paksha, language)} · ${LocalizationEngine.translateMasa(panchanga.hinduMasa, language)}",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF5A4D3D),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { viewModel.setActiveTab(0) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D1808)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F).copy(alpha = 0.6f)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text("పూర్తి పంచాంగం ›", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD54F))
                            }
                        }

                        // Devotional Status Badges for Selected Day
                        val selDayNum = selectedDate.dayOfMonth
                        val selDayItem = monthData.days.getOrNull(selDayNum - 1)
                        val isSelPournami = selDayItem?.isPournami == true
                        val isSelAmavasya = selDayItem?.isAmavasya == true
                        val isSelEkadashi = selDayItem?.isEkadashi == true

                        if (isSelPournami || isSelAmavasya || isSelEkadashi) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSelPournami) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFFFF8E1),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD54F))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("🌕", fontSize = 13.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("పౌర్ణమి పుణ్యదినం", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF8D1808))
                                        }
                                    }
                                }
                                if (isSelAmavasya) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFECEFF1),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF90A4AE))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("🌑", fontSize = 13.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("అమావాస్య పుణ్యదినం", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF263238))
                                        }
                                    }
                                }
                                if (isSelEkadashi) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFE8F5E9),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA5D6A7))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("🙏", fontSize = 13.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("ఏకాదశి వ్రతం", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Row 1: Tithi & Nakshatra (Spacious 2-column layout)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Column 1: Tithi
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color(0xFFFFF3E0), shape = RoundedCornerShape(10.dp))
                                    .border(0.8.dp, Color(0xFFFFCC80), shape = RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = LocalizationEngine.get("tithi", language),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = LocalizationEngine.translateTithi(panchanga.tithi.name, language),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF3E2723),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.tithi.endTimeStr, language)}",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF6D4C41),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            // Column 2: Nakshatra
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(10.dp))
                                    .border(0.8.dp, Color(0xFFA5D6A7), shape = RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = LocalizationEngine.get("nakshatra", language),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, language),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1B5E20),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.nakshatra.endTimeStr, language)}",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF33691E),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Row 2: Rahu Kalam (Full-width clean bar)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(10.dp))
                                .border(0.8.dp, Color(0xFFFFCDD2), shape = RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "⚠️",
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = LocalizationEngine.get("rahu_kalam", language),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InauspiciousRed
                                )
                            }
                            Text(
                                text = panchanga.auspiciousTimings.rahuKalam,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = InauspiciousRed
                            )
                        }

                        // Day Festivals if any
                        if (panchanga.festivals.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            panchanga.festivals.forEach { fest ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFFF9C4),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082)),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${fest.iconEmoji} ${LocalizationEngine.translateFestivalName(fest.name, language)}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF795548)
                                        )
                                        TextButton(
                                            onClick = { viewModel.selectFestivalForDetail(fest) },
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Text("వివరాలు ›", fontSize = 11.sp, color = TeluguRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fasting Days Table (ఉపవాసం రోజులు) - 100% Dynamic Table Layout
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF8D1808))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "ఉపవాసం & పుణ్య తిథులు (${LocalizationEngine.getTeluguMonthName(displayedYearMonth.monthValue)})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD54F)
                            )
                        }

                        if (monthData.fastingOccurrences.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(14.dp), contentAlignment = Alignment.Center) {
                                Text("ఈ నెలలో ఉపవాస దినాలు లేవు", fontSize = 12.sp, color = Color(0xFF6C757D))
                            }
                        } else {
                            // Table Subheader
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFF3E0))
                                    .height(IntrinsicSize.Min),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "తేదీ / వారం",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8D1808),
                                    modifier = Modifier.width(62.dp).padding(vertical = 7.dp),
                                    textAlign = TextAlign.Center
                                )
                                VerticalDivider(color = Color(0xFFFFE082), thickness = 1.dp)
                                Text(
                                    text = "తిథి & విశేషం",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8D1808),
                                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 7.dp),
                                    textAlign = TextAlign.Start
                                )
                                VerticalDivider(color = Color(0xFFFFE082), thickness = 1.dp)
                                Text(
                                    text = "వివరాలు",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8D1808),
                                    modifier = Modifier.width(58.dp).padding(vertical = 7.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            HorizontalDivider(color = Color(0xFFFFE082), thickness = 1.dp)

                            // Table Rows
                            monthData.fastingOccurrences.forEachIndexed { index, occ ->
                                val rowBg = if (index % 2 == 1) Color(0xFFFFF8E1) else Color(0xFFFFFDF8)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(rowBg)
                                        .height(IntrinsicSize.Min)
                                        .clickable {
                                            viewModel.setSelectedDate(occ.date)
                                            viewModel.setActiveTab(0)
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Column 1: Date & Weekday
                                    Column(
                                        modifier = Modifier
                                            .width(62.dp)
                                            .padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = occ.color.copy(alpha = 0.14f),
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "${occ.dayOfMonth}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = occ.color
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = occ.weekdayTelugu,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF5A4D3D)
                                        )
                                    }

                                    VerticalDivider(color = Color(0xFFFFE082).copy(alpha = 0.5f), thickness = 1.dp)

                                    // Column 2: Tithi & Significance
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp, vertical = 7.dp)
                                     ) {
                                        Text(
                                            text = "${occ.iconEmoji} ${occ.title}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF211A13)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = occ.significance,
                                            fontSize = 10.sp,
                                            color = Color(0xFF5A4D3D),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    VerticalDivider(color = Color(0xFFFFE082).copy(alpha = 0.5f), thickness = 1.dp)

                                    // Column 3: Action Link
                                    Box(
                                        modifier = Modifier
                                            .width(58.dp)
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "చూడండి ›",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF8D1808)
                                        )
                                    }
                                }
                                if (index < monthData.fastingOccurrences.lastIndex) {
                                    HorizontalDivider(color = Color(0xFFFFE082).copy(alpha = 0.5f), thickness = 0.8.dp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Other Days Table (ఇతర విశేష రోజులు - అష్టమి / నవమి / చవితి) - 100% Dynamic Table Layout
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF8D1808))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "ఇతర విశేష తిథులు (${LocalizationEngine.getTeluguMonthName(displayedYearMonth.monthValue)})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD54F)
                            )
                        }

                        if (monthData.otherOccurrences.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(14.dp), contentAlignment = Alignment.Center) {
                                Text("ఈ నెలలో ఇతర విశేష తిథులు లేవు", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        } else {
                            // Table Subheader
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFF3E0))
                                    .height(IntrinsicSize.Min),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "తేదీ / వారం",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8D1808),
                                    modifier = Modifier.width(62.dp).padding(vertical = 7.dp),
                                    textAlign = TextAlign.Center
                                )
                                VerticalDivider(color = Color(0xFFFFE082), thickness = 1.dp)
                                Text(
                                    text = "విశేష తిథి & పూజ",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8D1808),
                                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 7.dp),
                                    textAlign = TextAlign.Start
                                )
                                VerticalDivider(color = Color(0xFFFFE082), thickness = 1.dp)
                                Text(
                                    text = "వివరాలు",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8D1808),
                                    modifier = Modifier.width(58.dp).padding(vertical = 7.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            HorizontalDivider(color = Color(0xFFFFE082), thickness = 1.dp)

                            // Table Rows
                            monthData.otherOccurrences.forEachIndexed { index, occ ->
                                val rowBg = if (index % 2 == 1) Color(0xFFFFF8E1) else Color(0xFFFFFDF8)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(rowBg)
                                        .height(IntrinsicSize.Min)
                                        .clickable {
                                            viewModel.setSelectedDate(occ.date)
                                            viewModel.setActiveTab(0)
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Column 1: Date & Weekday
                                    Column(
                                        modifier = Modifier
                                            .width(62.dp)
                                            .padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = occ.color.copy(alpha = 0.14f),
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "${occ.dayOfMonth}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = occ.color
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = occ.weekdayTelugu,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF5A4D3D)
                                        )
                                    }

                                    VerticalDivider(color = Color(0xFFFFE082).copy(alpha = 0.5f), thickness = 1.dp)

                                    // Column 2: Tithi & Significance
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp, vertical = 7.dp)
                                    ) {
                                        Text(
                                            text = "${occ.iconEmoji} ${occ.title}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF211A13)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = occ.significance,
                                            fontSize = 10.sp,
                                            color = Color(0xFF5A4D3D),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    VerticalDivider(color = Color(0xFFFFE082).copy(alpha = 0.5f), thickness = 1.dp)

                                    // Column 3: Action Link
                                    Box(
                                        modifier = Modifier
                                            .width(58.dp)
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "చూడండి ›",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF8D1808)
                                        )
                                    }
                                }
                                if (index < monthData.otherOccurrences.lastIndex) {
                                    HorizontalDivider(color = Color(0xFFFFE082).copy(alpha = 0.5f), thickness = 0.8.dp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Month Festivals (ఈ నెల పండుగలు) - 100% Dynamic Table Layout
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "ఈ నెల పండుగలు (${monthData.festivals.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        if (monthData.festivals.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                Text("ఈ నెలలో ప్రధాన పండుగలు లేవు", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        } else {
                            // Table Subheader
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                    .height(IntrinsicSize.Min),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "తేదీ / వారం",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.width(62.dp).padding(vertical = 7.dp),
                                    textAlign = TextAlign.Center
                                )
                                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 1.dp)
                                Text(
                                    text = "పండుగ / వ్రతం విశేషం",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp, vertical = 7.dp),
                                    textAlign = TextAlign.Start
                                )
                                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 1.dp)
                                Text(
                                    text = "వివరాలు",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.width(58.dp).padding(vertical = 7.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 1.dp)

                            // Table Rows
                            monthData.festivals.forEachIndexed { index, fest ->
                                val rowBg = if (index % 2 == 1) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
                                val festWeekday = when (fest.date.dayOfWeek) {
                                    DayOfWeek.MONDAY -> "సోమవారం"
                                    DayOfWeek.TUESDAY -> "మంగళవారం"
                                    DayOfWeek.WEDNESDAY -> "బుధవారం"
                                    DayOfWeek.THURSDAY -> "గురువారం"
                                    DayOfWeek.FRIDAY -> "శుక్రవారం"
                                    DayOfWeek.SATURDAY -> "శనివారం"
                                    DayOfWeek.SUNDAY -> "ఆదివారం"
                                    else -> ""
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(rowBg)
                                        .height(IntrinsicSize.Min)
                                        .clickable {
                                            viewModel.setSelectedDate(fest.date)
                                            viewModel.selectFestivalForDetail(fest)
                                        },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Column 1: Date & Weekday
                                    Column(
                                        modifier = Modifier
                                            .width(62.dp)
                                            .padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "${fest.date.dayOfMonth}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = festWeekday,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)

                                    // Column 2: Festival & Summary
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp, vertical = 7.dp)
                                    ) {
                                        Text(
                                            text = "${fest.iconEmoji} ${LocalizationEngine.translateFestivalName(fest.name, language)}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = LocalizationEngine.translateFestivalSummary(fest.id, fest.summary, language),
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)

                                    // Column 3: Action Link
                                    Box(
                                        modifier = Modifier
                                            .width(58.dp)
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "చూడండి ›",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                if (index < monthData.festivals.lastIndex) {
                                    HorizontalDivider(color = Color(0xFFF1F3F5), thickness = 0.8.dp)
                                }
                            }
                        }
                    }
                }
            }

            CalendarViewMode.TABLE -> {
                // Table View: | తేదీ | వారం | తిథి | నక్షత్రం | పండుగ / వ్రతం | with distinct borders & columns
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "తేదీ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.width(38.dp).padding(vertical = 9.dp),
                                textAlign = TextAlign.Center
                            )
                            VerticalDivider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.35f), thickness = 1.dp)
                            Text(
                                text = "వారం",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.width(48.dp).padding(vertical = 9.dp),
                                textAlign = TextAlign.Center
                            )
                            VerticalDivider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.35f), thickness = 1.dp)
                            Text(
                                text = "తిథి",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.weight(1.1f).padding(horizontal = 6.dp, vertical = 9.dp),
                                textAlign = TextAlign.Start
                            )
                            VerticalDivider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.35f), thickness = 1.dp)
                            Text(
                                text = "నక్షత్రం",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.weight(1.2f).padding(horizontal = 6.dp, vertical = 9.dp),
                                textAlign = TextAlign.Start
                            )
                            VerticalDivider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.35f), thickness = 1.dp)
                            Text(
                                text = "పండుగ / వ్రతం",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.weight(1.1f).padding(horizontal = 6.dp, vertical = 9.dp),
                                textAlign = TextAlign.Start
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)

                        // Table Rows
                        monthData.days.forEachIndexed { index, dayItem ->
                            val isSun = dayItem.dayOfWeek == DayOfWeek.SUNDAY
                            val isSelected = dayItem.date == selectedDate
                            val isToday = dayItem.date == LocalDate.now()

                            val rowBg = when {
                                isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                isToday -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                                isSun -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)
                                index % 2 == 1 -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                else -> MaterialTheme.colorScheme.surface
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(rowBg)
                                    .height(IntrinsicSize.Min)
                                    .clickable {
                                        viewModel.setSelectedDate(dayItem.date)
                                        viewModel.setActiveTab(0) // Jump to Day Panchangam
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 1. Date Column
                                Box(
                                    modifier = Modifier
                                        .width(38.dp)
                                        .fillMaxHeight()
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${dayItem.dayOfMonth}",
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSun) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)

                                // 2. Varam (Weekday) Column
                                Box(
                                    modifier = Modifier
                                        .width(48.dp)
                                        .fillMaxHeight()
                                        .padding(vertical = 4.dp, horizontal = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (isSun) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.padding(2.dp)
                                    ) {
                                        Text(
                                            text = LocalizationEngine.getShortDayName(dayItem.dayOfWeek, language),
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSun) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)

                                // 3. Tithi Column
                                Column(
                                    modifier = Modifier
                                        .weight(1.1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = 6.dp, vertical = 6.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = LocalizationEngine.translateTithi(dayItem.tithi.name, language),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = LocalizationEngine.translateEndTime(dayItem.tithi.endTimeStr, language),
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 11.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)

                                // 4. Nakshatra Column
                                Column(
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .fillMaxHeight()
                                        .padding(horizontal = 6.dp, vertical = 6.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = LocalizationEngine.translateNakshatra(dayItem.nakshatra.name, language),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = LocalizationEngine.translateEndTime(dayItem.nakshatra.endTimeStr, language),
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 11.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp)

                                // 5. Festival / Vratham Column
                                Column(
                                    modifier = Modifier
                                        .weight(1.1f)
                                        .fillMaxHeight()
                                        .padding(horizontal = 5.dp, vertical = 6.dp),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    when {
                                        dayItem.festivals.isNotEmpty() -> {
                                            val fest = dayItem.festivals.first()
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                                            ) {
                                                Text(
                                                    text = "${fest.iconEmoji} ${LocalizationEngine.translateFestivalName(fest.name, language)}",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        dayItem.isPournami -> {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFFFFF8E1),
                                                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFFFECB3))
                                            ) {
                                                Text(
                                                    text = "🌕 పౌర్ణమి",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFE65100),
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        dayItem.isAmavasya -> {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFFECEFF1),
                                                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFCFD8DC))
                                            ) {
                                                Text(
                                                    text = "🌑 అమావాస్య",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF37474F),
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        dayItem.isEkadashi -> {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFFE8F5E9),
                                                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFC8E6C9))
                                            ) {
                                                Text(
                                                    text = "🙏 ఏకాదశి",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF2B8A3E),
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        else -> {
                                            Text(
                                                text = "-",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 0.8.dp)
                        }
                    }
                }
            }

            CalendarViewMode.YEARLY -> {
                val yearlyData = remember(displayedYearMonth.year) {
                    YearlyPanchangaManager.getYearlyPanchanga(displayedYearMonth.year, language)
                }

                // 1. Samvatsara Peethika & Annual Jagallagna Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "🚩 ${yearlyData.samvatsaraNameTelugu}",
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "ప్రభవాది 60 సంవత్సరాలలో ${yearlyData.samvatsaraNumber}వ సంవత్సరం",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(14.dp)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "📖 ${yearlyData.samvatsaraMeaning}",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "జగల్లగ్న ఫలితాలు & ప్రజా జీవనం:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = yearlyData.generalForecast,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "వర్షాలు & వ్యవసాయ పరిశీలన:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = yearlyData.rainfallAgricultureForecast,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "ఆర్థిక లావాదేవీలు & వాణిజ్యం:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = yearlyData.economicForecast,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Navanayaka Phala Card (నవనాయక ఫలాలు)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "👑 నవనాయక ఫలాలు (సంవత్సరాధిపతులు)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Column(modifier = Modifier.padding(12.dp)) {
                            yearlyData.navanayakas.forEachIndexed { index, item ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = item.planetEmoji,
                                                    fontSize = 14.sp
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "${item.title}: ",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = item.planet,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.primaryContainer
                                            ) {
                                                Text(
                                                    text = item.nature,
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.result,
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. 12 Rashis Adaya-Vyaya & Rajapoojya-Avamana Table (ద్వాదశ రాశుల ఆదాయ - వ్యయాలు)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "📊 ద్వాదశ రాశుల ఆదాయ - వ్యయాలు & రాజపూజ్యం",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("రాశి", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1.3f))
                            Text("ఆదాయం", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("వ్యయం", fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            Text("రాజపూజ్యం", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, modifier = Modifier.weight(1.1f))
                            Text("అవమానం", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 1.dp)

                        yearlyData.adayaVyayas.forEachIndexed { index, rashi ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                                    .padding(horizontal = 8.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1.3f)) {
                                    Text(rashi.rashiEmoji, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = rashi.rashiName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text("${rashi.adayam}", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                                Text("${rashi.vyayam}", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                                Text("${rashi.rajapoojyam}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center, modifier = Modifier.weight(1.1f))
                                Text("${rashi.avamanam}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
                            }
                            if (index < yearlyData.adayaVyayas.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 0.6.dp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. Yearly Eclipses Card (సంవత్సర గ్రహణాలు)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "🌘 వార్షిక గ్రహణాల నిర్ణయం",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Column(modifier = Modifier.padding(12.dp)) {
                            yearlyData.eclipses.forEachIndexed { index, ecl ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        // Header: Title with weight + Single-line pill badge
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(ecl.typeEmoji, fontSize = 14.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = ecl.title,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }

                                            // Single-line pill badge (never wraps into two lines)
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = if (ecl.isVisibleInIndia) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                                border = androidx.compose.foundation.BorderStroke(
                                                    0.8.dp,
                                                    if (ecl.isVisibleInIndia) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                                )
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                ) {
                                                    Surface(
                                                        shape = RoundedCornerShape(50),
                                                        color = if (ecl.isVisibleInIndia) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                        modifier = Modifier.size(6.dp)
                                                    ) {}
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = if (ecl.isVisibleInIndia) "భారత్‌లో దృశ్యం" else "అదృశ్యం",
                                                        fontSize = 9.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (ecl.isVisibleInIndia) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                                        maxLines = 1,
                                                        softWrap = false
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 0.8.dp)
                                        Spacer(modifier = Modifier.height(6.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Today,
                                                contentDescription = "తేదీ",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(13.5.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "తేదీ & సమయం: ${ecl.dateStr} · ${ecl.timing}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "⚠️ ${ecl.affectedRashis}",
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "🙏 పరిహారం: ${ecl.pariharam}",
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. Planetary Transits Card (గురు & శని గోచార సంచారాలు)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "🪐 బృహస్పతి & శని గోచార సంచార ఫలితాలు",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Column(modifier = Modifier.padding(12.dp)) {
                            yearlyData.majorTransits.forEachIndexed { index, tr ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(tr.iconEmoji, fontSize = 14.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = tr.title,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = tr.transitDetails,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = tr.impacts,
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 14.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "✅ అనుకూల రాశులు: ${tr.auspiciousRashis}",
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "⚠️ శాంతి రాశులు: ${tr.pariharaRashis}",
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.error,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "🙏 సూచించిన పరిహారం: ${tr.recommendedParihara}",
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 6. Yearly Festivals & Vratas List (వార్షిక పండుగలు & వ్రతాల సూచిక)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "🗓️ ${displayedYearMonth.year} వార్షిక పండుగలు & వ్రతాల క్యాలెండర్",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "మొత్తం 12 నెలల పండుగలు, వ్రతాలు, విశేష తిథుల జాబితా",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Filter Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            YearlyCategory.values().forEach { cat ->
                                val isSelected = cat == yearlyFilter
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { yearlyFilter = cat },
                                    label = { Text(cat.label, fontSize = 11.5.sp) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val filteredEvents = remember(yearlyEvents, yearlyFilter) {
                            if (yearlyFilter == YearlyCategory.ALL) yearlyEvents
                            else yearlyEvents.filter { it.category == yearlyFilter }
                        }

                        Text(
                            text = "${filteredEvents.size} విశేషాలు కనుగొనబడ్డాయి",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        filteredEvents.forEachIndexed { index, ev ->
                            val isSun = ev.date.dayOfWeek == DayOfWeek.SUNDAY
                            val monthName = LocalizationEngine.getTeluguMonthName(ev.date.monthValue)
                            val dayVara = LocalizationEngine.getTeluguDayOfWeek(ev.date.dayOfWeek)

                            val categoryColor = when (ev.category) {
                                YearlyCategory.FESTIVAL -> MaterialTheme.colorScheme.primary
                                YearlyCategory.VRATHAM -> MaterialTheme.colorScheme.secondary
                                YearlyCategory.EKADASHI -> MaterialTheme.colorScheme.primary
                                YearlyCategory.POURNAMI -> MaterialTheme.colorScheme.tertiary
                                YearlyCategory.AMAVASYA -> MaterialTheme.colorScheme.onSurfaceVariant
                                YearlyCategory.HOLIDAY -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.primary
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.5.dp)
                                    .clickable {
                                        viewModel.setSelectedDate(ev.date)
                                        viewModel.setActiveTab(0) // Navigate straight to Day Panchangam
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // 1. Prominent Calendar Date & Month Badge (Fixed ample width so Month is never cut off)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        border = androidx.compose.foundation.BorderStroke(1.2.dp, if (isSun) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                        modifier = Modifier.width(58.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            // Month Header (Top Red/Maroon Bar)
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(if (isSun) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                                                    .padding(vertical = 2.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = monthName,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSun) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                                                    maxLines = 1,
                                                    softWrap = false,
                                                    textAlign = TextAlign.Center
                                                )
                                            }

                                            // Day Number & Weekday
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 3.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = "${ev.date.dayOfMonth}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (isSun) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                                    lineHeight = 15.sp
                                                )
                                                Text(
                                                    text = LocalizationEngine.getShortDayName(ev.date.dayOfWeek, language),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSun) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))
                                    VerticalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), thickness = 1.dp, modifier = Modifier.fillMaxHeight().padding(vertical = 2.dp))
                                    Spacer(modifier = Modifier.width(8.dp))

                                    // 2. Festival Details Column
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${ev.icon} ${LocalizationEngine.translateFestivalName(ev.title, language)}",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "$dayVara · ${ev.summary}",
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 14.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    // 3. Category Tag Badge
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = categoryColor.copy(alpha = 0.12f),
                                        border = androidx.compose.foundation.BorderStroke(0.6.dp, categoryColor.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = ev.category.label,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = categoryColor,
                                            maxLines = 1,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
