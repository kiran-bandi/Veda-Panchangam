package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import com.example.engine.AstronomicalEngine
import com.example.engine.FestivalWishHelper
import com.example.model.*
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.model.CityLocation
import com.example.model.DayPanchanga
import com.example.model.PlanetTransitInfo
import com.example.model.RashiChakraData
import com.example.ui.theme.*
import java.time.LocalDate

/**
 * 1. Hero Date Card (Screenshot 1, 5, 8, 11)
 * Clean card showing Telugu Year, Month, Ruthu, Ayanam, with bold red date and navigation arrows.
 */
@Composable
fun HeroTeluguDateCard(
    date: LocalDate,
    panchanga: DayPanchanga,
    onPrevDay: () -> Unit,
    onNextDay: () -> Unit,
    onDateClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_telugu_date_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF5)), // Sacred Parchment Ivory
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFFFE082)) // Radiant Gold Border
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sacred Top Header Strip
            val teluguMonthName = LocalizationEngine.getTeluguMonthName(date.monthValue)
            val teluguDayOfWeek = LocalizationEngine.getTeluguDayOfWeek(date.dayOfWeek)
            val sanskritVara = LocalizationEngine.getSanskritVara(date.dayOfWeek)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8D1808)) // Sacred Temple Kumkum
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🪔 $teluguMonthName - $teluguDayOfWeek",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "($sanskritVara)",
                        fontSize = 12.sp,
                        color = Color(0xFFFFECB3), // Soft Divine Gold
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Navigation Row with bold theme primary date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Prev Day Button
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFF3E0), // Soft Divine Golden Glow
                        border = BorderStroke(1.dp, Color(0xFFFFE082)),
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable { onPrevDay() }
                            .testTag("btn_hero_prev_day")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "మునుపటి రోజు",
                                tint = Color(0xFF8D1808),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Center Date (DD-MM-YYYY)
                    val formattedDate = String.format("%02d-%02d-%04d", date.dayOfMonth, date.monthValue, date.year)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onDateClick() }
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = formattedDate,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF8D1808), // Sacred Temple Red
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Next Day Button
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFF3E0), // Soft Divine Golden Glow
                        border = BorderStroke(1.dp, Color(0xFFFFE082)),
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .clickable { onNextDay() }
                            .testTag("btn_hero_next_day")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "తదుపరి రోజు",
                                tint = Color(0xFF8D1808),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFE8E1D1), thickness = 1.dp) // Sandalwood Divider
                Spacer(modifier = Modifier.height(10.dp))

            // 4 Traditional Lines
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "శ్రీ ${LocalizationEngine.translateSamvatsara(panchanga.samvatsara, AppLanguage.TE)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF211A13)
                    )
                    Text(
                        text = LocalizationEngine.translateMasa(panchanga.hinduMasa, AppLanguage.TE),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF211A13)
                    )
                    Text(
                        text = LocalizationEngine.translateRitu(panchanga.ritu, AppLanguage.TE),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5A4D3D)
                    )
                    Text(
                        text = LocalizationEngine.translateAyana(panchanga.ayana, AppLanguage.TE),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5A4D3D)
                    )
                }

                // WhatsApp Share Button (Green circle with share icon)
                Surface(
                    shape = CircleShape,
                    color = WhatsAppGreen,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable { onShareClick() }
                        .testTag("btn_whatsapp_share"),
                    shadowElevation = 3.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "వాట్సాప్‌లో పంచాంగం పంచుకోండి",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
        }
    }
}

/**
 * 2. 4 Quick Navigation Hub Tiles
 * Prominent color-coded tiles:
 * - మాస పంచాంగం (Calendar)
 * - రాశి ఫలాలు (Horoscope)
 * - పండుగలు & వ్రతాలు (Festivals & Vratams)
 * - శుభ ముహూర్తములు (Muhurthas)
 */
@Composable
fun QuickHubActionTiles(
    onCalendarClick: () -> Unit,
    onRashiClick: () -> Unit,
    onFestivalsClick: () -> Unit,
    onMuhurthasClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tile 1: మాస పంచాంగం (Temple Red & Gold)
            HubTile(
                title = "మాస పంచాంగం",
                subtitle = "నెలవారీ క్యాలెండర్",
                iconEmoji = "📜",
                gradient = Brush.linearGradient(listOf(Color(0xFFB71C1C), Color(0xFFD32F2F))),
                onClick = onCalendarClick,
                modifier = Modifier.weight(1f),
                testTag = "hub_tile_calendar"
            )

            // Tile 2: రాశి ఫలాలు (Sacred Saffron Gold)
            HubTile(
                title = "రాశి ఫలాలు",
                subtitle = "దిన, వార, మాస ఫలాలు",
                iconEmoji = "♈",
                gradient = Brush.linearGradient(listOf(Color(0xFFE65100), Color(0xFFF57C00))),
                onClick = onRashiClick,
                modifier = Modifier.weight(1f),
                testTag = "hub_tile_rashi"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tile 3: పండుగలు & వ్రతాలు (Sacred Kumkum Vermilion)
            HubTile(
                title = "పండుగలు & వ్రతాలు",
                subtitle = "పండుగల సమగ్ర జాబితా",
                iconEmoji = "🪔",
                gradient = Brush.linearGradient(listOf(Color(0xFFC2185B), Color(0xFFD81B60))),
                onClick = onFestivalsClick,
                modifier = Modifier.weight(1f),
                testTag = "hub_tile_festivals"
            )

            // Tile 4: శుభ ముహూర్తములు (Sacred Royal Indigo)
            HubTile(
                title = "శుభ ముహూర్తములు",
                subtitle = "వివాహ, గృహప్రవేశం",
                iconEmoji = "🌟",
                gradient = Brush.linearGradient(listOf(Color(0xFF1A237E), Color(0xFF283593))),
                onClick = onMuhurthasClick,
                modifier = Modifier.weight(1f),
                testTag = "hub_tile_muhurthas"
            )
        }
    }
}

@Composable
private fun HubTile(
    title: String,
    subtitle: String,
    iconEmoji: String,
    gradient: Brush,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Surface(
        modifier = modifier
            .height(82.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFFFE082).copy(alpha = 0.55f)), // Radiant Gold Border
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = iconEmoji, fontSize = 17.sp)
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    AutoResizedText(
                        text = title,
                        fontSize = 11.sp,
                        minFontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    AutoResizedText(
                        text = subtitle,
                        fontSize = 9.sp,
                        minFontSize = 7.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * 3. Special Occasion / Festival Pill Banner (Screenshot 1)
 */
@Composable
fun SpecialOccasionBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🚩", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * 3b. Festival Divine Wish Card (పండుగ శుభాకాంక్షల కార్డ్)
 * Eye-catching Golden & Saffron Divine Gradient Card displayed on Home Screen for major festivals.
 */
@Composable
fun FestivalWishCard(
    festival: FestivalItem,
    language: AppLanguage,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val wishText = FestivalWishHelper.getDevotionalWishMessage(festival, language)
    val isTe = language == AppLanguage.TE

    // Divine Golden & Saffron Gradient
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF8D1808), // Sacred Maroon
            Color(0xFFD84315), // Deep Saffron / Red Orange
            Color(0xFFFF8F00), // Divine Golden Saffron
            Color(0xFFFFB300)  // Golden Amber
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("festival_wish_card"),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.5.dp, Color(0xFFFFE082)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Area with Centered Badge and Top-Right Share Icon
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // Centered Header Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, Color(0xFFFFD54F).copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✨ 🚩 ${if (isTe) "పండుగ ఆశీస్సులు & శుభాకాంక్షలు" else "Divine Festival Wishes"} 🚩 ✨",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFF8E1)
                            )
                        }
                    }

                    // Top-Right Share Icon Button
                    Surface(
                        onClick = onShareClick,
                        shape = CircleShape,
                        color = Color(0xFFFFD54F), // Bright Golden Yellow
                        border = BorderStroke(1.dp, Color.White),
                        shadowElevation = 3.dp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(34.dp)
                            .testTag("btn_top_share_wish")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = if (isTe) "శుభాకాంక్షలు షేర్ చేయండి" else "Share Wishes",
                                tint = Color(0xFF3E2723),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Festival Icon / Symbol & Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .border(1.5.dp, Color(0xFFFFE082), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = festival.iconEmoji,
                            fontSize = 28.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = festival.name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 22.sp
                        )
                        if (!festival.deity.isNullOrBlank()) {
                            Text(
                                text = "🙏 ${if (isTe) "ఇష్టదైవం" else "Deity"}: ${festival.deity}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFFECB3)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Devotional Wish Message Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.20f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = wishText,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFFFDE7),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Puja Muhurta details if present
                if (!festival.pujaMuhurta.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF8E1).copy(alpha = 0.22f),
                        border = BorderStroke(1.dp, Color(0xFFFFE082).copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⏰ ${if (isTe) "పూజా ముహూర్తం" else "Puja Muhurtam"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFECB3),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = festival.pujaMuhurta,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 4. Sun & Moon Dual Cards (Screenshot 1)
 * Red header strips with white labels, crisp two-column timings underneath.
 */
@Composable
fun SunMoonDualCards(
    sunrise: String,
    sunset: String,
    moonrise: String,
    moonset: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Sun Card (Sunrise / Sunset)
        Card(
            modifier = Modifier
                .weight(1f)
                .testTag("card_sun_timings"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)), // Soft Golden Morning Glow
            border = BorderStroke(1.dp, Color(0xFFFFE082)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                // Header (Golden Orange)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE65100))
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "☀️ సూర్యోదయం",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "🌅 సూర్యాస్తమయం",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Timings Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sunrise,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF3E2723),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFFFB300).copy(alpha = 0.5f)))
                    Text(
                        text = sunset,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF3E2723),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Moon Card (Moonrise / Moonset)
        Card(
            modifier = Modifier
                .weight(1f)
                .testTag("card_moon_timings"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)), // Soft Twilight Violet Glow
            border = BorderStroke(1.dp, Color(0xFFE1BEE7)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                // Header (Deep Royal Violet)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4A148C))
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🌙 చంద్రోదయం",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "🌌 చంద్రాస్తమయం",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Timings Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = moonrise,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A237E),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFF8E24AA).copy(alpha = 0.4f)))
                    Text(
                        text = moonset,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A237E),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * 5. Core Panchanga 4-Grid Table (Screenshot 1)
 * Exact red-outlined grid with 4 quadrants:
 * [తిథి] [నక్షత్రం]
 * [యోగం] [కరణం]
 */
@Composable
fun PanchangaFourQuadrantGrid(
    tithiName: String,
    tithiEndTime: String,
    nextTithi: String?,
    nakshatraName: String,
    nakshatraEndTime: String,
    nextNakshatra: String?,
    yogaName: String,
    yogaEndTime: String,
    nextYoga: String?,
    karanaName: String,
    karanaEndTime: String,
    nextKarana: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_panchanga_four_quadrant"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF7)),
        border = BorderStroke(1.5.dp, Color(0xFFB71C1C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Row 1: Tithi & Nakshatra
            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                // Quadrant 1: Tithi
                QuadrantCell(
                    header = "తిథి",
                    icon = "🌕",
                    headerBgColor = Color(0xFFFFF8E1),
                    headerTextColor = Color(0xFFB71C1C),
                    mainText = tithiName,
                    endTime = tithiEndTime,
                    nextText = nextTithi,
                    modifier = Modifier.weight(1f)
                )

                // Vertical Divider
                Box(modifier = Modifier.fillMaxHeight().width(1.5.dp).background(Color(0xFFB71C1C)))

                // Quadrant 2: Nakshatra
                QuadrantCell(
                    header = "నక్షత్రం",
                    icon = "⭐",
                    headerBgColor = Color(0xFFE0F2F1),
                    headerTextColor = Color(0xFF00695C),
                    mainText = nakshatraName,
                    endTime = nakshatraEndTime,
                    nextText = nextNakshatra,
                    modifier = Modifier.weight(1f)
                )
            }

            // Horizontal Divider
            Box(modifier = Modifier.fillMaxWidth().height(1.5.dp).background(Color(0xFFB71C1C)))

            // Row 2: Yoga & Karana
            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                // Quadrant 3: Yoga
                QuadrantCell(
                    header = "యోగం",
                    icon = "🕉️",
                    headerBgColor = Color(0xFFF3E5F5),
                    headerTextColor = Color(0xFF4A148C),
                    mainText = yogaName,
                    endTime = yogaEndTime,
                    nextText = nextYoga,
                    modifier = Modifier.weight(1f)
                )

                // Vertical Divider
                Box(modifier = Modifier.fillMaxHeight().width(1.5.dp).background(Color(0xFFB71C1C)))

                // Quadrant 4: Karana
                QuadrantCell(
                    header = "కరణం",
                    icon = "🐂",
                    headerBgColor = Color(0xFFFAF0E6),
                    headerTextColor = Color(0xFF5D4037),
                    mainText = karanaName,
                    endTime = karanaEndTime,
                    nextText = nextKarana,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun QuadrantCell(
    header: String,
    icon: String,
    headerBgColor: Color,
    headerTextColor: Color,
    mainText: String,
    endTime: String,
    nextText: String?,
    modifier: Modifier = Modifier
) {
    val localizedEndTime = remember(endTime) {
        if (endTime.isNotBlank()) LocalizationEngine.translateEndTime(endTime, AppLanguage.TE) else ""
    }
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        // Devotional Category Header Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBgColor)
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AutoResizedText(
                text = "$icon $header",
                fontSize = 11.5.sp,
                minFontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = headerTextColor,
                maxLines = 1
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (localizedEndTime.isNotBlank()) "$mainText: $localizedEndTime" else mainText,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF211A13),
                lineHeight = 16.sp
            )
            if (!nextText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "తదుపరి $nextText",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF5A4D3D),
                    lineHeight = 14.sp
                )
            }
        }
    }
}

/**
 * 6. Auspicious Timings Table Card (శుభ సమయములు పట్టిక)
 */
@Composable
fun AuspiciousTimingsCard(
    auspiciousTimings: AuspiciousTimings,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_auspicious_timings_table"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        border = BorderStroke(1.5.dp, Color(0xFF2B8A3E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2B8A3E))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✨ శుభ సమయములు పట్టిక",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Column Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2B8A3E).copy(alpha = 0.12f))
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "శుభ ముహూర్తం",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B8A3E),
                    modifier = Modifier
                        .weight(1.3f)
                        .padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(Color(0xFF2B8A3E).copy(alpha = 0.3f))
                )
                Text(
                    "సమయం",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B8A3E),
                    modifier = Modifier
                        .weight(1.7f)
                        .padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(Color(0xFF2B8A3E).copy(alpha = 0.3f))
                )
                Text(
                    "విశేషం",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2B8A3E),
                    modifier = Modifier
                        .weight(1.4f)
                        .padding(horizontal = 8.dp),
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider(color = Color(0xFF2B8A3E).copy(alpha = 0.3f), thickness = 1.dp)

            val rows = listOf(
                Triple("అభిజిత్ ముహూర్తం", auspiciousTimings.abhijitMuhurta, "సర్వకార్య జయప్రదం"),
                Triple("బ్రహ్మ ముహూర్తం", auspiciousTimings.brahmaMuhurta, "ధ్యాన, పూజానుకూలం"),
                Triple("అమృత కాలం", auspiciousTimings.amritaKalam, "శుభ కార్యారంభం"),
                Triple("విజయ ముహూర్తం", if (auspiciousTimings.vijayaMuhurta.isNotBlank()) auspiciousTimings.vijayaMuhurta else "మధ్యాహ్నం 02:15 - 03:05", "విజయావహ సమయం"),
                Triple("గోధూళి ముహూర్తం", if (auspiciousTimings.godhuliMuhurta.isNotBlank()) auspiciousTimings.godhuliMuhurta else "సాయంత్రం 06:10 - 06:35", "సాయం సంధ్యా కాలం")
            )

            rows.forEachIndexed { index, (name, timing, desc) ->
                val rowBg = if (index % 2 == 1) Color(0xFFF1F8E9) else Color(0xFFFFFDF8)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBg)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier
                            .weight(1.3f)
                            .padding(horizontal = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFF2B8A3E).copy(alpha = 0.25f))
                    )
                    Text(
                        text = timing,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B8A3E),
                        modifier = Modifier
                            .weight(1.7f)
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFF2B8A3E).copy(alpha = 0.25f))
                    )
                    Text(
                        text = desc,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5A4D3D),
                        modifier = Modifier
                            .weight(1.4f)
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (index < rows.lastIndex) {
                    HorizontalDivider(color = Color(0xFF2B8A3E).copy(alpha = 0.18f), thickness = 0.5.dp)
                }
            }
        }
    }
}

/**
 * 7. Shraddha Tithi Card (శ్రాద్ధం తిథి) (Screenshot 1)
 */
@Composable
fun ShraddhaTithiCard(
    tithiText: String,
    modifier: Modifier = Modifier
) {
    RedHeaderContentCard(
        title = "శ్రాద్ధం తిథి (మధ్యాహ్నం ఉన్న తిథి)",
        modifier = modifier.testTag("card_shraddha_tithi")
    ) {
        Text(
            text = tithiText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * 8. Inauspicious Timings Table (అశుభ కాలములు పట్టిక)
 */
@Composable
fun InauspiciousTimingsBlock(
    rahuKalam: String,
    yamagandam: String,
    durmuhurtham: String,
    varjyam: String,
    amritaGhadayalu: String,
    gulikaKalam: String = "",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_inauspicious_timings_table"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF8)),
        border = BorderStroke(1.5.dp, Color(0xFFC92A2A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFC92A2A))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚠️ అశుభ కాలములు పట్టిక",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Column Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFC92A2A).copy(alpha = 0.12f))
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "అశుభ కాలం",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC92A2A),
                    modifier = Modifier
                        .weight(1.3f)
                        .padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(Color(0xFFC92A2A).copy(alpha = 0.3f))
                )
                Text(
                    "సమయం",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC92A2A),
                    modifier = Modifier
                        .weight(1.7f)
                        .padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(Color(0xFFC92A2A).copy(alpha = 0.3f))
                )
                Text(
                    "విధి",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC92A2A),
                    modifier = Modifier
                        .weight(1.4f)
                        .padding(horizontal = 8.dp),
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider(color = Color(0xFFC92A2A).copy(alpha = 0.3f), thickness = 1.dp)

            val rows = listOf(
                Triple("రాహుకాలం", rahuKalam, "కొత్త పనులు నిషిద్ధం"),
                Triple("యమగండం", yamagandam, "ప్రయాణాలు వర్జ్యం"),
                Triple("దుర్ముహూర్తం", durmuhurtham, "శుభకార్యాలు రాదు"),
                Triple("వర్జ్యము", varjyam, "వర్జించదగిన సమయం"),
                Triple("గుళిక కాలం", if (gulikaKalam.isNotBlank()) gulikaKalam else "సాధారణ కాలం", "సాధారణ కార్యములు")
            )

            rows.forEachIndexed { index, (name, timing, desc) ->
                val rowBg = if (index % 2 == 1) Color(0xFFFFEBEE).copy(alpha = 0.6f) else Color(0xFFFFFDF8)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBg)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF211A13),
                        modifier = Modifier
                            .weight(1.3f)
                            .padding(horizontal = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFFC92A2A).copy(alpha = 0.25f))
                    )
                    Text(
                        text = timing,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC92A2A),
                        modifier = Modifier
                            .weight(1.7f)
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFFC92A2A).copy(alpha = 0.25f))
                    )
                    Text(
                        text = desc,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF5A4D3D),
                        modifier = Modifier
                            .weight(1.4f)
                            .padding(horizontal = 4.dp),
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (index < rows.lastIndex) {
                    HorizontalDivider(color = Color(0xFFC92A2A).copy(alpha = 0.18f), thickness = 0.5.dp)
                }
            }
        }
    }
}

/**
 * 9. South Indian Rashi Chakram / Astrology Kundali Grid (Screenshot 5)
 * 4x4 Grid layout:
 * [మీనం]     [మేషం]      [వృషభం]   [మిథునం]
 * [కుంభం]    [      తేదీ       ]   [కర్కాటకం]
 * [మకరం]     [   15/09/2026   ]   [సింహం]
 * [ధనుస్సు]  [వృశ్చికం]  [తుల]     [కన్య]
 */
@Composable
fun RashiChakramView(
    date: LocalDate,
    location: CityLocation = AstronomicalEngine.CITIES.first(),
    rashiChakraData: RashiChakraData? = null,
    modifier: Modifier = Modifier
) {
    val chakraData = rashiChakraData ?: remember(date, location) {
        AstronomicalEngine.calculatePlanetTransits(date, location)
    }

    var isTableExpanded by remember { mutableStateOf(false) }

    fun getPlanetsForRashi(rashiIdx: Int): List<PlanetTransitInfo> {
        return chakraData.planets.filter { it.rashiIndex == rashiIdx }
    }

    fun hasLagna(rashiIdx: Int): Boolean {
        return chakraData.lagna?.rashiIndex == rashiIdx
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_rashi_chakram"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.85f))
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "దిన గోచార రాశి చక్రం (లాహిరి పద్ధతి)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "నిరయణ రాశి చక్రం | ఉదయం 06:00 గంటలకు",
                            fontSize = 10.5.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${date.dayOfMonth}/${date.monthValue}/${date.year}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Traditional South Indian Kundali 4x4 Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Row 1: Meena (11), Mesha (0), Vrishabha (1), Mithuna (2)
                Row(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                    DynamicChakraCell(
                        rashiName = "మీనం",
                        rashiIndex = 11,
                        planets = getPlanetsForRashi(11),
                        hasLagna = hasLagna(11),
                        modifier = Modifier.weight(1f)
                    )
                    DynamicChakraCell(
                        rashiName = "మేషం",
                        rashiIndex = 0,
                        planets = getPlanetsForRashi(0),
                        hasLagna = hasLagna(0),
                        modifier = Modifier.weight(1f)
                    )
                    DynamicChakraCell(
                        rashiName = "వృషభం",
                        rashiIndex = 1,
                        planets = getPlanetsForRashi(1),
                        hasLagna = hasLagna(1),
                        modifier = Modifier.weight(1f)
                    )
                    DynamicChakraCell(
                        rashiName = "మిథునం",
                        rashiIndex = 2,
                        planets = getPlanetsForRashi(2),
                        hasLagna = hasLagna(2),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: Kumbha (10), Center (Top Half), Karkataka (3)
                Row(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                    DynamicChakraCell(
                        rashiName = "కుంభం",
                        rashiIndex = 10,
                        planets = getPlanetsForRashi(10),
                        hasLagna = hasLagna(10),
                        modifier = Modifier.weight(1f)
                    )
                    // Center Box - Top Half
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                            .background(TeluguRedLight.copy(alpha = 0.45f))
                            .border(BorderStroke(0.75.dp, TeluguRed.copy(alpha = 0.3f)))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "శ్రీ శుభమస్తు",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TeluguRed
                            )
                            Text(
                                text = "దిన గోచారం",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF495057)
                            )
                        }
                    }
                    DynamicChakraCell(
                        rashiName = "కర్కాటకం",
                        rashiIndex = 3,
                        planets = getPlanetsForRashi(3),
                        hasLagna = hasLagna(3),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: Makara (9), Center (Bottom Half), Simha (4)
                Row(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                    DynamicChakraCell(
                        rashiName = "మకరం",
                        rashiIndex = 9,
                        planets = getPlanetsForRashi(9),
                        hasLagna = hasLagna(9),
                        modifier = Modifier.weight(1f)
                    )
                    // Center Box - Bottom Half
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight()
                            .background(TeluguRedLight.copy(alpha = 0.45f))
                            .border(BorderStroke(0.75.dp, TeluguRed.copy(alpha = 0.3f)))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "లగ్నం: ${chakraData.lagna?.rashiNameTe ?: "మేషం"}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2B8A3E)
                            )
                            Text(
                                text = "${LocalizationEngine.translateCityName(chakraData.location.id, chakraData.location.name, AppLanguage.TE)} ప్రాంతం",
                                fontSize = 9.5.sp,
                                color = Color(0xFF6C757D)
                            )
                        }
                    }
                    DynamicChakraCell(
                        rashiName = "సింహం",
                        rashiIndex = 4,
                        planets = getPlanetsForRashi(4),
                        hasLagna = hasLagna(4),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 4: Dhanus (8), Vrischika (7), Tula (6), Kanya (5)
                Row(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                    DynamicChakraCell(
                        rashiName = "ధనుస్సు",
                        rashiIndex = 8,
                        planets = getPlanetsForRashi(8),
                        hasLagna = hasLagna(8),
                        modifier = Modifier.weight(1f)
                    )
                    DynamicChakraCell(
                        rashiName = "వృశ్చికం",
                        rashiIndex = 7,
                        planets = getPlanetsForRashi(7),
                        hasLagna = hasLagna(7),
                        modifier = Modifier.weight(1f)
                    )
                    DynamicChakraCell(
                        rashiName = "తుల",
                        rashiIndex = 6,
                        planets = getPlanetsForRashi(6),
                        hasLagna = hasLagna(6),
                        modifier = Modifier.weight(1f)
                    )
                    DynamicChakraCell(
                        rashiName = "కన్య",
                        rashiIndex = 5,
                        planets = getPlanetsForRashi(5),
                        hasLagna = hasLagna(5),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Legend and Expand Details Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isTableExpanded = !isTableExpanded }
                    .background(Color(0xFFFFF9DB))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "సూచిక: ల=లగ్నం, (వ)=వక్రగతి",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF495057)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (isTableExpanded) "వివరాలు దాచు" else "గ్రహాల అంశల వివరాలు",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TeluguRed
                    )
                    Icon(
                        imageVector = if (isTableExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "గ్రహ వివరాలు",
                        tint = TeluguRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Detailed Planet Transit Table (When Expanded)
            if (isTableExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "గ్రహ సంచార స్థితి & అంశలు (నిరయణ లెక్కింపు):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212529),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TeluguRed.copy(alpha = 0.1f))
                            .padding(vertical = 5.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("గ్రహం", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f), color = TeluguRed)
                        Text("రాశి", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), color = TeluguRed)
                        Text("డిగ్రీలు", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), color = TeluguRed)
                        Text("నక్షత్రం - పాదం", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f), color = TeluguRed)
                        Text("స్థితి", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.9f), color = TeluguRed)
                    }

                    HorizontalDivider(color = TeluguRed.copy(alpha = 0.3f), thickness = 1.dp)

                    // Planets List
                    chakraData.planets.forEach { planet ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${planet.symbol} ${planet.shortNameTe}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1.2f),
                                color = Color(0xFF212529)
                            )
                            Text(
                                text = planet.rashiNameTe,
                                fontSize = 10.5.sp,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF343A40)
                            )
                            Text(
                                text = planet.degreeStrTe,
                                fontSize = 10.5.sp,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF495057)
                            )
                            Text(
                                text = "${planet.nakshatraNameTe} (${planet.pada})",
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1.5f),
                                color = Color(0xFF495057),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (planet.isRetrograde) "వక్రం ↺" else "మార్గి",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(0.9f),
                                color = if (planet.isRetrograde) Color(0xFFE03131) else Color(0xFF2F9E44)
                            )
                        }
                        HorizontalDivider(color = Color(0xFFF1F3F5), thickness = 0.5.dp)
                    }

                    // Lagna Row
                    chakraData.lagna?.let { lagna ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEBFBEE))
                                .padding(vertical = 4.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "లగ్నం (ఉదయం)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1.2f),
                                color = Color(0xFF2B8A3E)
                            )
                            Text(
                                text = lagna.rashiNameTe,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF2B8A3E)
                            )
                            Text(
                                text = lagna.degreeStrTe,
                                fontSize = 10.5.sp,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF2B8A3E)
                            )
                            Text(
                                text = "${lagna.nakshatraNameTe} (${lagna.pada})",
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1.5f),
                                color = Color(0xFF2B8A3E)
                            )
                            Text(
                                text = "ఉదయ లగ్నం",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(0.9f),
                                color = Color(0xFF2B8A3E)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicChakraCell(
    rashiName: String,
    rashiIndex: Int,
    planets: List<PlanetTransitInfo>,
    hasLagna: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(BorderStroke(0.75.dp, TeluguRed.copy(alpha = 0.4f)))
            .background(
                if (hasLagna) Color(0xFFFFF4E6) else if (planets.isNotEmpty()) Color(0xFFF8F9FA) else Color.White
            )
            .padding(2.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Rashi Name Label
            AutoResizedText(
                text = rashiName,
                fontSize = 9.5.sp,
                minFontSize = 7.5.sp,
                fontWeight = FontWeight.Bold,
                color = TeluguRed,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            // Planets In this Rashi
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                if (hasLagna) {
                    AutoResizedText(
                        text = "లగ్నం",
                        fontSize = 8.5.sp,
                        minFontSize = 7.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFE8590C),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }

                planets.take(3).forEach { planet ->
                    val retroSuffix = if (planet.isRetrograde) "(వ)" else ""
                    AutoResizedText(
                        text = "${planet.shortNameTe}$retroSuffix",
                        fontSize = 8.5.sp,
                        minFontSize = 7.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (planet.isRetrograde) Color(0xFFC92A2A) else Color(0xFF212529),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }

                if (planets.size > 3) {
                    Text(
                        text = "+${planets.size - 3}",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF868E96),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

/**
 * 10. Reusable Red Header Content Card
 */
@Composable
fun RedHeaderContentCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

/**
 * 11. Footer Banner (Screenshot 1, 5)
 */
@Composable
fun TeluguCalendarFooter(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "మంగళం మహత్",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                letterSpacing = 1.sp
            )
            Text(
                text = "శ్రీ శ్రీ శ్రీ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                letterSpacing = 2.sp
            )
        }
    }
}

/**
 * 12. Quick Modal Dialog for Festivals & Muhurthas (Screenshot 2)
 * 5 Circular illustrated actions:
 * 1. పండుగలు (Festivals)
 * 2. ప్రభుత్వ సెలవుదినం (Govt Holidays)
 * 3. శుభముహుర్తాలు (Muhurthams)
 * 4. ఉపవాసం తేదీ (Fasting Dates)
 * 5. ముఖ్యమైన తిథులు (Important Tithis)
 */
@Composable
fun FestivalsMuhurthasQuickDialog(
    onDismiss: () -> Unit,
    onSelectOption: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "పండుగలు శుభముహుర్తాలు",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TeluguRedDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Row 1: 3 circular items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QuickDialogCircleItem(
                        iconEmoji = "🪔",
                        title = "పండుగలు",
                        bgColor = Color(0xFFFFF9C4),
                        iconTint = Color(0xFFF57F17),
                        onClick = { onSelectOption(0) }
                    )
                    QuickDialogCircleItem(
                        iconEmoji = "🏛️",
                        title = "ప్రభుత్వ\nసెలవుదినం",
                        bgColor = Color(0xFFFFCDD2),
                        iconTint = Color(0xFFC62828),
                        onClick = { onSelectOption(1) }
                    )
                    QuickDialogCircleItem(
                        iconEmoji = "🥁",
                        title = "శుభముహుర్తాలు",
                        bgColor = Color(0xFFC8E6C9),
                        iconTint = Color(0xFF2E7D32),
                        onClick = { onSelectOption(2) }
                    )
                }

                // Row 2: 2 circular items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickDialogCircleItem(
                        iconEmoji = "🛕",
                        title = "ఉపవాసం\nతేదీ",
                        bgColor = Color(0xFFFFE082),
                        iconTint = Color(0xFFFF6F00),
                        onClick = { onSelectOption(3) }
                    )
                    QuickDialogCircleItem(
                        iconEmoji = "🌕",
                        title = "ముఖ్యమైన\nతిథులు",
                        bgColor = Color(0xFFC5CAE9),
                        iconTint = Color(0xFF283593),
                        onClick = { onSelectOption(4) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("మూసివేయి", color = TeluguRed, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun QuickDialogCircleItem(
    iconEmoji: String,
    title: String,
    bgColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = bgColor,
            modifier = Modifier.size(56.dp),
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = iconEmoji, fontSize = 24.sp)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212529),
            textAlign = TextAlign.Center,
            lineHeight = 13.sp
        )
    }
}

/**
 * Share helper method to share complete Telugu Panchangam
 */
fun sharePanchangamToWhatsApp(context: Context, date: LocalDate, panchanga: DayPanchanga) {
    val text = buildString {
        appendLine("🕉️ శ్రీ శుభమస్తు - నిత్య పంచాంగం 🕉️")
        appendLine("తేదీ: ${date.dayOfMonth}-${date.monthValue}-${date.year} (${LocalizationEngine.getTeluguDayOfWeek(date.dayOfWeek)})")
        appendLine("శ్రీ ${panchanga.samvatsara} నామ సంవత్సరం - ${panchanga.ayana} - ${panchanga.ritu} ఋతువు")
        appendLine("${panchanga.hinduMasa} మాసం - ${panchanga.paksha} పక్షం")
        appendLine("-------------------------")
        appendLine("సూర్యోదయం: ${panchanga.sunMoonTimes.sunrise} | సూర్యాస్తమయం: ${panchanga.sunMoonTimes.sunset}")
        appendLine("చంద్రోదయం: ${panchanga.sunMoonTimes.moonrise} | చంద్రాస్తమయం: ${panchanga.sunMoonTimes.moonset}")
        appendLine("తిథి: ${LocalizationEngine.translateTithi(panchanga.tithi.name, AppLanguage.TE)} (ముగింపు: ${LocalizationEngine.translateEndTime(panchanga.tithi.endTimeStr, AppLanguage.TE)})")
        appendLine("నక్షత్రం: ${LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, AppLanguage.TE)} (${panchanga.nakshatra.pada}వ పాదము) (ముగింపు: ${LocalizationEngine.translateEndTime(panchanga.nakshatra.endTimeStr, AppLanguage.TE)})")
        appendLine("యోగం: ${LocalizationEngine.translateYoga(panchanga.yoga.name, AppLanguage.TE)}")
        appendLine("కరణం: ${LocalizationEngine.translateKarana(panchanga.karana.name, AppLanguage.TE)}")
        appendLine("-------------------------")
        appendLine("రాహుకాలం: ${panchanga.auspiciousTimings.rahuKalam}")
        appendLine("యమగండం: ${panchanga.auspiciousTimings.yamaganda}")
        appendLine("వర్జ్యము: ${panchanga.auspiciousTimings.varjyam}")
        appendLine("అమృత ఘడియలు: ${panchanga.auspiciousTimings.amritaKalam}")
        appendLine("దుర్ముహూర్తం: ${panchanga.auspiciousTimings.durMuhurtam}")
        appendLine("-------------------------")
        appendLine("శుభం భవతు! 🙏")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "పంచాంగం పంచుకోండి"))
}
