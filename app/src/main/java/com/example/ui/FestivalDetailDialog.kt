package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.model.FestivalItem
import com.example.ui.theme.DeepGold
import com.example.ui.theme.KumkumRed
import com.example.ui.theme.SacredSaffron

@Composable
fun FestivalDetailDialog(
    festival: FestivalItem,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onOpenShareCard: () -> Unit
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${festival.iconEmoji} ${LocalizationEngine.translateFestivalName(festival.name, language)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = LocalizationEngine.get("close", language))
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = LocalizationEngine.formatLocalizedDate(festival.date, language),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${LocalizationEngine.get("deity", language)}: ${LocalizationEngine.translateDeity(festival.deity, language)} · ${LocalizationEngine.translateFestivalCategory(festival.category.label, language)}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Puja Muhurta
                festival.pujaMuhurta?.let { muhurta ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🪔 ${LocalizationEngine.get("puja_muhurta", language)}: $muhurta",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Parana Time
                festival.paranaTime?.let { parana ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🥣 ${LocalizationEngine.get("parana_time", language)}: $parana",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Summary & Significance
                Text(text = LocalizationEngine.get("summary_label", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = LocalizationEngine.translateFestivalSummary(festival.id, festival.summary, language), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = LocalizationEngine.get("significance_label", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = LocalizationEngine.translateFestivalSignificance(festival.id, festival.significance, language), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                // Historical Context & Origin
                festival.historicalContext?.let { history ->
                    if (history.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = LocalizationEngine.get("historical_context_label", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = LocalizationEngine.translateFestivalHistory(festival.id, history, language), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Fasting Rule
                festival.fastingRule?.let { rule ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = LocalizationEngine.get("fasting_rules_label", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = rule, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                // Sacred Story / Katha
                festival.story?.let { story ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = LocalizationEngine.get("sacred_story_label", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = story, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                // Mantra
                festival.mantra?.let { mantra ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = LocalizationEngine.get("mantra_label", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = mantra,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onOpenShareCard) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share Card", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(LocalizationEngine.get("share_card", language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(LocalizationEngine.get("close", language))
            }
        }
    )
}

@Composable
fun FestiveShareCardDialog(
    festival: FestivalItem,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val festTitle = LocalizationEngine.translateFestivalName(festival.name, language)
    val deityTitle = LocalizationEngine.translateDeity(festival.deity, language)
    val mantraLine = festival.mantra?.let { "\"$it\"\n" } ?: ""
    val muhurtaLine = festival.pujaMuhurta?.let { "${LocalizationEngine.get("puja_muhurta", language)}: $it\n" } ?: ""
    val divineWish = LocalizationEngine.translateFestivalWish(festTitle, deityTitle, language)

    val greetingText = """
        🪔 $festTitle 🪔
        
        ${LocalizationEngine.formatLocalizedDate(festival.date, language)}
        $muhurtaLine$mantraLine
        $divineWish
        
        🕉️ Panchanga
    """.trimIndent()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${LocalizationEngine.get("share_card", language)} 🪔", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = LocalizationEngine.get("close", language))
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Card View Visual Presentation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF2C103C), Color(0xFF150824))
                            )
                        )
                        .border(
                            2.dp,
                            Brush.linearGradient(listOf(DeepGold, SacredSaffron, KumkumRed)),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(18.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🕉️", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = festTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepGold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${LocalizationEngine.get("major_festival", language)} · $deityTitle",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = LocalizationEngine.formatLocalizedDate(festival.date, language),
                            fontSize = 12.sp,
                            color = Color(0xFFFFD54F),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        festival.mantra?.let { mantra ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0x33FFB300),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = mantra,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepGold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Text(
                            text = divineWish,
                            fontSize = 11.sp,
                            color = Color(0xFFEDE7F6),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        AdmobBanner()
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, greetingText)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, LocalizationEngine.get("share_card", language))
                    context.startActivity(shareIntent)
                }
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(LocalizationEngine.get("share_card", language))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Festive Greeting", greetingText)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, LocalizationEngine.get("copy_text", language), Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(LocalizationEngine.get("copy_text", language))
            }
        }
    )
}
