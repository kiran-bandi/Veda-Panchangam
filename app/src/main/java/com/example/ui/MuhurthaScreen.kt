package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.model.MuhurthaCategory
import com.example.ui.theme.TeluguGreen
import com.example.ui.theme.TeluguRed
import com.example.ui.theme.TeluguRedLight
import com.example.viewmodel.PanchangaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuhurthaScreen(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedMuhurthaCategory.collectAsState()
    val selectedMonths by viewModel.selectedMuhurthaMonths.collectAsState()
    val location by viewModel.selectedLocation.collectAsState()
    val tradition by viewModel.selectedTradition.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()
    val results by viewModel.muhurthaResults.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("muhurtha_screen")
    ) {
        // Top Header Bar (Compact)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "శుభ ముహూర్తములు",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "${LocalizationEngine.translateCityName(location.id, location.name, AppLanguage.TE)} · ${LocalizationEngine.translateTraditionTitle(tradition.title, AppLanguage.TE)}",
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            }
        }

        // Compact Horizontal Category Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MuhurthaCategory.values().forEach { cat ->
                val isSelected = cat == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setMuhurthaCategory(cat) },
                    label = {
                        Text(
                            text = "${cat.icon} ${LocalizationEngine.translateMuhurthaCategory(cat.title, AppLanguage.TE)}",
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        // Range Selector Pill (1 Month, 3 Months, 6 Months)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "వ్యవధి:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6C757D)
            )

            listOf(1, 3, 6).forEach { months ->
                val isSelected = months == selectedMonths
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) TeluguRedLight else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) TeluguRed else Color(0xFFE2E4E8)),
                    modifier = Modifier.clickable { viewModel.setMuhurthaMonths(months) }
                ) {
                    Text(
                        text = "$months ${if (months == 1) "నెల" else "నెలలు"}",
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${results.size} ముహూర్తాలు",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Muhurtha Cards List (Clean, readable, responsive)
        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ఎంచుకున్న వ్యవధిలో నిర్దిష్ట ప్రమాణాలకు సరిపోయే ముహూర్తాలు లేవు. దయచేసి వ్యవధిని పెంచండి.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(results) { res ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
                        border = BorderStroke(1.dp, Color(0xFFFFE082)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Top Row: Date & Auspicious Rating Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = LocalizationEngine.formatLocalizedDate(res.date, AppLanguage.TE),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${LocalizationEngine.translateTithi(res.tithi, AppLanguage.TE)} · ${LocalizationEngine.translateNakshatra(res.nakshatra, AppLanguage.TE)}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                val isHigh = res.rating.contains("Highly") || res.rating.contains("అతి")
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isHigh) TeluguGreen.copy(alpha = 0.15f) else Color(0xFFFFB300).copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, if (isHigh) TeluguGreen.copy(alpha = 0.5f) else Color(0xFFFFB300).copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = LocalizationEngine.translateRating(res.rating, AppLanguage.TE),
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isHigh) TeluguGreen else Color(0xFFE65100),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Golden Auspicious Time Window Banner
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFF8E1), // Temple Sandalwood Gold
                                border = BorderStroke(1.dp, Color(0xFFFFCC80)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "⏰", fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "శుభ సమయం: ${res.timeWindow}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF8D1808) // Sacred Temple Kumkum
                                    )
                                }
                            }

                            // Favorable points
                            if (res.favorablePoints.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                res.favorablePoints.forEach { pt ->
                                    Text(
                                        text = "• $pt",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            // Cautions if any
                            if (res.cautions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                res.cautions.forEach { c ->
                                    Text(
                                        text = "⚠️ $c",
                                        fontSize = 10.5.sp,
                                        color = Color(0xFFC62828),
                                        lineHeight = 14.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.setSelectedDate(res.date)
                                        viewModel.setActiveTab(0)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TeluguRed),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "ఈ రోజు పంచాంగం చూడండి ›",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
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
