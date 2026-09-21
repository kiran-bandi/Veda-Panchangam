package com.example.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.AstronomicalEngine
import com.example.engine.DevotionalQuote
import com.example.engine.DevotionalQuoteRepository
import com.example.engine.FestivalWishHelper
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.model.CalendarTradition
import com.example.model.IndianDistrict
import com.example.model.IndianDistrictsRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.PanchangaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val date by viewModel.selectedDate.collectAsState()
    val liveDate by viewModel.liveDate.collectAsState()
    val location by viewModel.selectedLocation.collectAsState()
    val tradition by viewModel.selectedTradition.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()
    val panchanga by viewModel.currentPanchanga.collectAsState()

    val resetSubScreensSignal by viewModel.resetSubScreensSignal.collectAsState()
    val isDetectingLocation by viewModel.isDetectingLocation.collectAsState()
    val locationMessage by viewModel.locationMessage.collectAsState()

    var showLocationDialog by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            viewModel.detectAndApplyCurrentLocation(
                context = context,
                onSuccess = { city ->
                    showLocationDialog = false
                    android.widget.Toast.makeText(
                        context,
                        if (language == AppLanguage.TE) "📍 ప్రదేశం గుర్తించబడింది: ${city.name}" else "📍 Location detected: ${city.name}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                },
                onError = { err ->
                    android.widget.Toast.makeText(
                        context,
                        "⚠️ $err",
                        android.widget.Toast.LENGTH_LONG
                    ).show()
                }
            )
        } else {
            android.widget.Toast.makeText(
                context,
                if (language == AppLanguage.TE) "ప్రదేశ అనుమతి తిరస్కరించబడింది" else "Location permission denied",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    var locationSearchQuery by remember { mutableStateOf("") }
    var selectedStateFilter by remember { mutableStateOf<String?>(null) }
    var showTraditionDialog by remember { mutableStateOf(false) }
    var isPanchangExpanded by remember { mutableStateOf(false) }
    var showRashiPhalaluScreen by remember { mutableStateOf(false) }
    var showImportantTithisScreen by remember { mutableStateOf(false) }

    LaunchedEffect(resetSubScreensSignal) {
        if (resetSubScreensSignal > 0L) {
            showRashiPhalaluScreen = false
            showImportantTithisScreen = false
        }
    }

    if (showRashiPhalaluScreen) {
        RashiPhalaluScreen(
            onNavigateBack = { showRashiPhalaluScreen = false },
            modifier = modifier
        )
        return
    }

    if (showImportantTithisScreen) {
        ImportantTithisScreen(
            onNavigateBack = { showImportantTithisScreen = false },
            modifier = modifier
        )
        return
    }

    val scrollState = rememberScrollState()
    val isToday = date == liveDate

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .testTag("home_screen_column")
    ) {
        // 1. Top Bar with Location, Tradition, and Theme Chips (Adaptive & Scrollable for all screen sizes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Chip
            FilterChip(
                selected = true,
                onClick = { showLocationDialog = true },
                label = {
                    Text(
                        text = "${LocalizationEngine.translateCityName(location.id, location.name, language)} 📍",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.testTag("location_chip")
            )

            // Tradition Chip
            FilterChip(
                selected = true,
                onClick = { showTraditionDialog = true },
                label = {
                    Text(
                        text = LocalizationEngine.translateTraditionShort(tradition, language),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.testTag("tradition_chip")
            )

            // Janma Panchangam Shortcut Chip
            FilterChip(
                selected = true,
                onClick = { viewModel.navigateToBirthPanchangam() },
                label = {
                    Text(
                        text = if (language == AppLanguage.TE) "జన్మ పంచాంగం 🔮" else "Birth Panchangam 🔮",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                modifier = Modifier.testTag("janma_panchangam_chip")
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 2. Dynamic Live Date & Time Header Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("live_datetime_banner"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LiveTimeTicker(viewModel = viewModel)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "· ${liveDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("te")))}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!isToday) {
                    Button(
                        onClick = { viewModel.jumpToToday() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_banner_jump_today")
                    ) {
                        Text(
                            text = LocalizationEngine.get("jump_back_to_today", language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AuspiciousGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "● ${LocalizationEngine.get("today", language)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AuspiciousGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 3. Hero Telugu Date Card (తేదీ, వారం, మాసం, సంవత్సరం)
        HeroTeluguDateCard(
            date = date,
            panchanga = panchanga,
            onPrevDay = { viewModel.previousDay() },
            onNextDay = { viewModel.nextDay() },
            onDateClick = { viewModel.jumpToToday() },
            onShareClick = {
                val shareText = "🚩 శ్రీ వేద తెలుగు పంచాంగం (${date.dayOfMonth}-${date.monthValue}-${date.year}):\n" +
                    "✨ తిథి: ${LocalizationEngine.translateTithi(panchanga.tithi.name, AppLanguage.TE)}\n" +
                    "✨ నక్షత్రం: ${LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, AppLanguage.TE)}\n" +
                    "⚠️ రాహుకాలం: ${panchanga.auspiciousTimings.rahuKalam}\n" +
                    "⚠️ యమగండం: ${panchanga.auspiciousTimings.yamaganda}"
                val sendIntent = android.content.Intent().apply {
                    action = android.content.Intent.ACTION_SEND
                    putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                context.startActivity(android.content.Intent.createChooser(sendIntent, "పంచాంగం షేర్ చేయండి"))
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4. నేటి పంచాంగం (తిథి, నక్షత్రం, యోగం, కరణం - 4 Quadrants)
        PanchangaFourQuadrantGrid(
            tithiName = LocalizationEngine.translateTithi(panchanga.tithi.name, AppLanguage.TE),
            tithiEndTime = panchanga.tithi.endTimeStr,
            nextTithi = null,
            nakshatraName = LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, AppLanguage.TE),
            nakshatraEndTime = panchanga.nakshatra.endTimeStr,
            nextNakshatra = null,
            yogaName = LocalizationEngine.translateYoga(panchanga.yoga.name, AppLanguage.TE),
            yogaEndTime = panchanga.yoga.endTimeStr,
            nextYoga = null,
            karanaName = LocalizationEngine.translateKarana(panchanga.karana.name, AppLanguage.TE),
            karanaEndTime = panchanga.karana.endTimeStr,
            nextKarana = null
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 5. శ్రాద్ధ తిథి కార్డ్
        ShraddhaTithiCard(
            tithiText = "${LocalizationEngine.translateTithi(panchanga.tithi.name, AppLanguage.TE)} శ్రాద్ధం"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 6. సూర్యోదయం & చంద్రోదయం
        SunMoonDualCards(
            sunrise = panchanga.sunMoonTimes.sunrise,
            sunset = panchanga.sunMoonTimes.sunset,
            moonrise = panchanga.sunMoonTimes.moonrise,
            moonset = panchanga.sunMoonTimes.moonset
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 7. శుభ సమయములు (అభిజిత్, బ్రహ్మ ముహూర్తం, అమృత కాలం)
        AuspiciousTimingsCard(
            auspiciousTimings = panchanga.auspiciousTimings
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 8. అశుభ కాలాలు (రాహుకాలం, యమగండం, దుర్ముహూర్తం, వర్జ్యం, గుళిక)
        InauspiciousTimingsBlock(
            rahuKalam = panchanga.auspiciousTimings.rahuKalam,
            yamagandam = panchanga.auspiciousTimings.yamaganda,
            durmuhurtham = panchanga.auspiciousTimings.durMuhurtam,
            varjyam = panchanga.auspiciousTimings.varjyam,
            amritaGhadayalu = panchanga.auspiciousTimings.amritaKalam,
            gulikaKalam = panchanga.auspiciousTimings.gulikaKalam
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 9. క్విక్ షార్ట్‌కట్ టైల్స్ (నెల పంచాంగం, రాశి ఫలాలు, పండుగలు, ముహూర్తాలు)
        QuickHubActionTiles(
            onCalendarClick = { viewModel.setActiveTab(1) },
            onRashiClick = { showRashiPhalaluScreen = true },
            onFestivalsClick = { viewModel.setActiveTab(2) },
            onMuhurthasClick = { viewModel.setActiveTab(3) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 10. పండుగలు & శుభాకాంక్షలు (Major Festival Wish Card & Occasion Alert Banner)
        val majorFestival = remember(date, panchanga.festivals) {
            FestivalWishHelper.getMajorFestivalForDate(date, panchanga.festivals)
        }
        majorFestival?.let { fest ->
            FestivalWishCard(
                festival = fest,
                language = language,
                onShareClick = {
                    val wishText = FestivalWishHelper.getDevotionalWishMessage(fest, language)
                    val pujaTimingText = if (!fest.pujaMuhurta.isNullOrBlank()) "\n⏰ పూజా ముహూర్తం: ${fest.pujaMuhurta}" else ""
                    val shareContent = "🎉 ${fest.name} శుభాకాంక్షలు! 🎉\n\n$wishText$pujaTimingText\n\n🚩 శ్రీ వేద తెలుగు పంచాంగం ద్వారా షేర్ చేయబడింది"
                    val sendIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, shareContent)
                        type = "text/plain"
                    }
                    context.startActivity(android.content.Intent.createChooser(sendIntent, "పండుగ శుభాకాంక్షలు షేర్ చేయండి"))
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        val specialFestTitle = remember(date, panchanga) {
            val occ = LocalizationEngine.getFestivalOrOccasionForDay(date, panchanga, AppLanguage.TE)
            if (occ.title.isNotEmpty() && occ.title != "శుభ దినం") occ.title else null
        }
        if (majorFestival == null && specialFestTitle != null) {
            SpecialOccasionBanner(text = specialFestTitle)
            Spacer(modifier = Modifier.height(10.dp))
        }

        // 11. నేటి సుభాషితం / డైలీ కోట్ (Daily Devotional Quote Section)
        DailyDevotionalQuoteSection(
            date = date,
            language = language,
            viewModel = viewModel,
            onOpenDevotionalLibrary = { viewModel.navigateToDevotional() }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 11. Complete Day Panchangam Detailed Table (Expandable / Full view)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_full_day_panchanga"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header with Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { isPanchangExpanded = !isPanchangExpanded }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📜 పూర్తి పంచాంగ వివరాలు",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = if (isPanchangExpanded) "▲ సంక్షిప్తం" else "▼ వివరాలు",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (isPanchangExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        val panchangDetails = listOf(
                            Pair("సంవత్సరం", "శ్రీ ${LocalizationEngine.translateSamvatsara(panchanga.samvatsara, language)}"),
                            Pair("ఆయనం", LocalizationEngine.translateAyana(panchanga.ayana, language)),
                            Pair("ఋతువు", LocalizationEngine.translateRitu(panchanga.ritu, language)),
                            Pair("మాసము", LocalizationEngine.translateMasa(panchanga.hinduMasa, language)),
                            Pair("పక్షము", "${LocalizationEngine.translatePaksha(panchanga.paksha, language)} పక్షం"),
                            Pair("తిథి", "${LocalizationEngine.translateTithi(panchanga.tithi.name, language)} (${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.tithi.endTimeStr, language)})"),
                            Pair("నక్షత్రం", "${LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, language)} ${panchanga.nakshatra.pada}వ పాదం (${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.nakshatra.endTimeStr, language)})"),
                            Pair("యోగం", "${LocalizationEngine.translateYoga(panchanga.yoga.name, language)} (${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.yoga.endTimeStr, language)})"),
                            Pair("కరణం", "${LocalizationEngine.translateKarana(panchanga.karana.name, language)} (${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.karana.endTimeStr, language)})"),
                            Pair("సూర్యోదయం / సూర్యాస్తమయం", "${panchanga.sunMoonTimes.sunrise} / ${panchanga.sunMoonTimes.sunset}"),
                            Pair("చంద్రోదయం / చంద్రాస్తమయం", "${panchanga.sunMoonTimes.moonrise} / ${panchanga.sunMoonTimes.moonset}"),
                            Pair("సూర్య రాశి", LocalizationEngine.translateRashi(panchanga.sunRashi, language)),
                            Pair("చంద్ర రాశి", LocalizationEngine.translateRashi(panchanga.moonRashi, language)),
                            Pair("అభిజిత్ ముహూర్తం", panchanga.auspiciousTimings.abhijitMuhurta),
                            Pair("బ్రహ్మ ముహూర్తం", panchanga.auspiciousTimings.brahmaMuhurta),
                            Pair("అమృత కాలం", panchanga.auspiciousTimings.amritaKalam),
                            Pair("రాహు కాలం", panchanga.auspiciousTimings.rahuKalam),
                            Pair("యమగండం", panchanga.auspiciousTimings.yamaganda),
                            Pair("గుళిక కాలం", panchanga.auspiciousTimings.gulikaKalam),
                            Pair("దుర్ముహూర్తం", panchanga.auspiciousTimings.durMuhurtam),
                            Pair("వర్జ్యం", panchanga.auspiciousTimings.varjyam),
                            Pair("శక సంవత్సరం", "${panchanga.shakaSamvat}"),
                            Pair("విక్రమ సంవత్సరం", "${panchanga.vikramSamvat}")
                        )

                        panchangDetails.forEachIndexed { index, (label, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index % 2 == 1) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface)
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AutoResizedText(
                                    text = label,
                                    fontSize = 11.5.sp,
                                    minFontSize = 8.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.weight(1.2f)
                                )
                                AutoResizedText(
                                    text = value,
                                    fontSize = 11.5.sp,
                                    minFontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1.4f),
                                    textAlign = TextAlign.End
                                )
                            }
                            if (index < panchangDetails.lastIndex) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), thickness = 0.5.dp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val fullPanchangShareText = """
                            📜 శ్రీ వేద తెలుగు పంచాంగం (${date.dayOfMonth}-${date.monthValue}-${date.year})
                            ✨ శ్రీ ${LocalizationEngine.translateSamvatsara(panchanga.samvatsara, language)}
                            ✨ ${LocalizationEngine.translateMasa(panchanga.hinduMasa, language)} | ${LocalizationEngine.translatePaksha(panchanga.paksha, language)} పక్షం
                            ✨ ${LocalizationEngine.translateRitu(panchanga.ritu, language)} | ${LocalizationEngine.translateAyana(panchanga.ayana, language)}
                            
                            🌟 తిథి: ${LocalizationEngine.translateTithi(panchanga.tithi.name, language)} (${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.tithi.endTimeStr, language)})
                            🌟 నక్షత్రం: ${LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, language)} ${panchanga.nakshatra.pada}వ పాదం (${LocalizationEngine.get("ends_at", language)}: ${LocalizationEngine.translateEndTime(panchanga.nakshatra.endTimeStr, language)})
                            🌟 యోగం: ${LocalizationEngine.translateYoga(panchanga.yoga.name, language)}
                            🌟 కరణం: ${LocalizationEngine.translateKarana(panchanga.karana.name, language)}
                            
                            ☀️ సూర్యోదయం: ${panchanga.sunMoonTimes.sunrise} | సూర్యాస్తమయం: ${panchanga.sunMoonTimes.sunset}
                            🌙 చంద్రోదయం: ${panchanga.sunMoonTimes.moonrise} | చంద్రాస్తమయం: ${panchanga.sunMoonTimes.moonset}
                            
                            ✨ శుభ సమయాలు:
                            • అభిజిత్: ${panchanga.auspiciousTimings.abhijitMuhurta}
                            • బ్రహ్మ ముహూర్తం: ${panchanga.auspiciousTimings.brahmaMuhurta}
                            • అమృత కాలం: ${panchanga.auspiciousTimings.amritaKalam}
                            
                            ⚠️ అశుభ కాలాలు:
                            • రాహుకాలం: ${panchanga.auspiciousTimings.rahuKalam}
                            • యమగండం: ${panchanga.auspiciousTimings.yamaganda}
                            • దుర్ముహూర్తం: ${panchanga.auspiciousTimings.durMuhurtam}
                            • వర్జ్యం: ${panchanga.auspiciousTimings.varjyam}
                        """.trimIndent()

                        // Compact Action Bar: Share and Close in same row without large text
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Compact WhatsApp / System Share Icon Button
                            FilledTonalIconButton(
                                onClick = {
                                    val sendIntent = android.content.Intent().apply {
                                        action = android.content.Intent.ACTION_SEND
                                        putExtra(android.content.Intent.EXTRA_TEXT, fullPanchangShareText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(android.content.Intent.createChooser(sendIntent, "పూర్తి పంచాంగ వివరాలు షేర్ చేయండి"))
                                },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = Color(0xFF25D366).copy(alpha = 0.15f),
                                    contentColor = Color(0xFF1B8A44)
                                ),
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("btn_share_full_panchanga")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "షేర్ చేయండి",
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Compact Close / Collapse Icon Button (▲)
                            FilledTonalIconButton(
                                onClick = { isPanchangExpanded = false },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("btn_close_full_panchanga_bottom")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "కుదించు / మూసివేయి",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isPanchangExpanded = true }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "అన్ని తిథి, నక్షత్ర, ముహూర్త వివరాలు చూడటానికి తాకండి",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 13. Choghadiya Table (దివా & రాత్రి చోఘడియా)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "⏱️ ${LocalizationEngine.get("choghadiya_title", language)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "శుభ కార్యాలు మరియు ప్రయాణాలకు అనుకూల సమయాలు",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    panchanga.choghadiyaList.forEach { slot ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (slot.type.isAuspicious) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (slot.type.isAuspicious) TeluguGreen.copy(alpha = 0.5f) else InauspiciousRed.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.width(105.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = LocalizationEngine.translateChoghadiya(slot.name, language).substringBefore(" ("),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (slot.type.isAuspicious) TeluguGreen else InauspiciousRed,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "${slot.startTime} - ${slot.endTime}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF495057),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 14. Notifications & Alerts Card
        val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
        val morningAlertEnabled by viewModel.morningAlertEnabled.collectAsState()
        val rahuKalamAlertEnabled by viewModel.rahuKalamAlertEnabled.collectAsState()
        val festivalsAlertEnabled by viewModel.festivalsAlertEnabled.collectAsState()

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🔔 రోజువారీ హెచ్చరికలు & నోటిఫికేషన్లు",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "ఉదయపు పంచాంగం మరియు రాహుకాలం సమాచారం",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    val notifPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { checked ->
                            if (checked && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                val hasPerm = androidx.core.content.ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                if (!hasPerm) {
                                    notifPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                            viewModel.saveNotificationSettings(
                                context = context,
                                enabled = checked,
                                morning = morningAlertEnabled,
                                rahu = rahuKalamAlertEnabled,
                                festivals = festivalsAlertEnabled
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 15. Mangalam Mahat Footer (Screenshot 1 & 5)
        TeluguCalendarFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Location Selection Dialog (Comprehensive All-India Districts & Global Support)
    if (showLocationDialog) {
        val statesList = remember(language) {
            listOf(
                Pair(null, if (language == AppLanguage.TE) "అన్ని ప్రాంతాలు" else "All Regions"),
                Pair("Telangana", if (language == AppLanguage.TE) "తెలంగాణ" else "Telangana"),
                Pair("Andhra Pradesh", if (language == AppLanguage.TE) "ఆంధ్రప్రదేశ్" else "Andhra Pradesh"),
                Pair("Karnataka", if (language == AppLanguage.TE) "కర్ణాటక" else "Karnataka"),
                Pair("Tamil Nadu", if (language == AppLanguage.TE) "తమిళనాడు" else "Tamil Nadu"),
                Pair("Maharashtra", if (language == AppLanguage.TE) "మహారాష్ట్ర" else "Maharashtra"),
                Pair("Gujarat", if (language == AppLanguage.TE) "గుజరాత్" else "Gujarat"),
                Pair("Kerala", if (language == AppLanguage.TE) "కేరళ" else "Kerala"),
                Pair("Delhi", if (language == AppLanguage.TE) "ఢిల్లీ NCR" else "Delhi NCR"),
                Pair("Uttar Pradesh", if (language == AppLanguage.TE) "ఉత్తర ప్రదేశ్" else "Uttar Pradesh"),
                Pair("Odisha", if (language == AppLanguage.TE) "ఒడిశా" else "Odisha"),
                Pair("Madhya Pradesh", if (language == AppLanguage.TE) "మధ్య ప్రదేశ్" else "Madhya Pradesh"),
                Pair("Rajasthan", if (language == AppLanguage.TE) "రాజస్థాన్" else "Rajasthan"),
                Pair("West Bengal", if (language == AppLanguage.TE) "పశ్చిమ బెంగాల్" else "West Bengal"),
                Pair("Bihar", if (language == AppLanguage.TE) "బీహార్" else "Bihar"),
                Pair("Punjab", if (language == AppLanguage.TE) "పంజాబ్" else "Punjab"),
                Pair("Haryana", if (language == AppLanguage.TE) "హర్యానా" else "Haryana"),
                Pair("Uttarakhand", if (language == AppLanguage.TE) "ఉత్తరాఖండ్" else "Uttarakhand"),
                Pair("Himachal Pradesh", if (language == AppLanguage.TE) "హిమాచల్" else "Himachal"),
                Pair("Jammu and Kashmir", if (language == AppLanguage.TE) "జమ్మూ & కాశ్మీర్" else "Jammu & Kashmir"),
                Pair("Jharkhand", if (language == AppLanguage.TE) "జార్ఖండ్" else "Jharkhand"),
                Pair("Chhattisgarh", if (language == AppLanguage.TE) "ఛత్తీస్‌గఢ్" else "Chhattisgarh"),
                Pair("Assam", if (language == AppLanguage.TE) "ఈశాన్య భారతం (అస్సాం)" else "Northeast (Assam)"),
                Pair("Goa", if (language == AppLanguage.TE) "గోవా" else "Goa"),
                Pair("USA", if (language == AppLanguage.TE) "అమెరికా (NRI)" else "USA (NRI)"),
                Pair("United Kingdom", if (language == AppLanguage.TE) "లండన్ / యూకే" else "UK / Europe"),
                Pair("United Arab Emirates", if (language == AppLanguage.TE) "దుబాయ్ (గల్ఫ్)" else "Dubai / Gulf"),
                Pair("Singapore", if (language == AppLanguage.TE) "సింగపూర్ / ఆసియా" else "Singapore / Asia"),
                Pair("Australia", if (language == AppLanguage.TE) "ఆస్ట్రేలియా" else "Australia")
            )
        }

        val filteredDistricts = remember(selectedStateFilter, locationSearchQuery) {
            IndianDistrictsRepository.filterDistricts(selectedStateFilter, locationSearchQuery)
        }

        AlertDialog(
            onDismissRequest = { 
                showLocationDialog = false 
                locationSearchQuery = ""
            },
            title = null,
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 460.dp)
                ) {
                    // 1. Top: Compact Select Current Location (GPS)
                    Button(
                        onClick = {
                            if (com.example.engine.LocationHelper.hasLocationPermission(context)) {
                                viewModel.detectAndApplyCurrentLocation(
                                    context = context,
                                    onSuccess = { city ->
                                        showLocationDialog = false
                                        android.widget.Toast.makeText(
                                            context,
                                            if (language == AppLanguage.TE) "📍 ప్రదేశం గుర్తించబడింది: ${city.name}" else "📍 Location detected: ${city.name}",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onError = { err ->
                                        android.widget.Toast.makeText(
                                            context,
                                            "⚠️ $err",
                                            android.widget.Toast.LENGTH_LONG
                                        ).show()
                                    }
                                )
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        enabled = !isDetectingLocation,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .testTag("btn_auto_detect_location"),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isDetectingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(13.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.TE) "లొకేషన్ గుర్తిస్తోంది..." else "Detecting...",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "My Location",
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.TE) "📍 ప్రస్తుత ప్రదేశం ఎంచుకోండి (GPS)" else "📍 Select Current Location (GPS)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (!locationMessage.isNullOrEmpty()) {
                        Text(
                            text = locationMessage!!,
                            fontSize = 10.5.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // 2. Sleek Compact Search Bar (32dp height)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TeluguRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (locationSearchQuery.isEmpty()) {
                                    Text(
                                        text = if (language == AppLanguage.TE) "నగరం లేదా జిల్లా శోధించండి..." else "Search district / city...",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                BasicTextField(
                                    value = locationSearchQuery,
                                    onValueChange = { locationSearchQuery = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    singleLine = true,
                                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                                )
                            }
                            if (locationSearchQuery.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { locationSearchQuery = "" },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }

                    // State Filter Chips (Horizontal Scroll)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        statesList.forEach { (stateKey, stateLabel) ->
                            val isSelected = selectedStateFilter == stateKey
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedStateFilter = if (isSelected) null else stateKey
                                },
                                label = {
                                    Text(
                                        text = stateLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    // Count summary
                    Text(
                        text = if (language == AppLanguage.TE) "లభించిన ప్రదేశాలు: ${filteredDistricts.size}" else "Locations found: ${filteredDistricts.size}",
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Districts List
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (filteredDistricts.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (language == AppLanguage.TE) "ఏ జిల్లాలు కనుగొనబడలేదు" else "No matching districts found",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            filteredDistricts.forEach { district ->
                                val isSelected = district.id.equals(location.id, ignoreCase = true)
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clickable {
                                            viewModel.saveLocation(context, district.toCityLocation())
                                            showLocationDialog = false
                                            locationSearchQuery = ""
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (language == AppLanguage.TE) district.nameTe else district.nameEn,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "${district.nameEn} • ${if (language == AppLanguage.TE) district.stateTe else district.stateEn}",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (isSelected) {
                                            Text(
                                                text = "✓",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    showLocationDialog = false 
                    locationSearchQuery = ""
                }) {
                    Text(LocalizationEngine.get("close", language), color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    // Tradition Selection Dialog
    if (showTraditionDialog) {
        AlertDialog(
            onDismissRequest = { showTraditionDialog = false },
            title = { Text("${LocalizationEngine.get("select_tradition", language)} 🕉️", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    CalendarTradition.values().forEach { trad ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable {
                                    viewModel.saveTradition(context, trad)
                                    showTraditionDialog = false
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = if (trad == tradition) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (trad == tradition) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = LocalizationEngine.translateTradition(trad, language),
                                    fontWeight = FontWeight.Bold,
                                    color = if (trad == tradition) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = LocalizationEngine.translateTraditionDesc(trad, language),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTraditionDialog = false }) {
                    Text(LocalizationEngine.get("close", language), color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}

@Composable
private fun LiveTimeTicker(viewModel: PanchangaViewModel) {
    val liveTimeStr by viewModel.liveTimeStr.collectAsState()
    Text(
        text = "🕒 $liveTimeStr",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun DailyDevotionalQuoteSection(
    date: LocalDate,
    language: AppLanguage,
    viewModel: PanchangaViewModel,
    onOpenDevotionalLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var quoteOffset by remember(date) { mutableStateOf(0) }
    var showEnglish by remember { mutableStateOf(false) }

    val totalQuotes = DevotionalQuoteRepository.DAILY_QUOTES.size
    val currentQuote = remember(date, quoteOffset) {
        val baseIndex = ((date.toEpochDay() % totalQuotes + totalQuotes) % totalQuotes).toInt()
        val finalIndex = ((baseIndex + quoteOffset) % totalQuotes + totalQuotes) % totalQuotes
        DevotionalQuoteRepository.DAILY_QUOTES[finalIndex.toInt()]
    }

    val bookmarks by viewModel.bookmarks.collectAsState()
    val isFavorite = bookmarks.any { it.id == "quote_${currentQuote.id}" }

    // Subtle fade-in animation when HomeScreen loads
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 650,
                easing = LinearOutSlowInEasing
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = 550,
                easing = LinearOutSlowInEasing
            ),
            initialOffsetY = { 24 }
        ),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("daily_devotional_quote_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFDF5) // Sacred Parchment Ivory
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082)), // Sacred Gold Border
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFF3E0), // Sacred Saffron Glow
                            border = BorderStroke(1.dp, Color(0xFFFFE082)),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = currentQuote.icon, fontSize = 17.sp)
                            }
                        }
                        Column {
                            Text(
                                text = if (language == AppLanguage.TE) "నిత్య భక్తి సూక్తి" else "Daily Devotional Quote",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = Color(0xFF8D1808) // Sacred Kumkum Red
                            )
                            Text(
                                text = currentQuote.deityOrContext,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Source Chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LightGold.copy(alpha = 0.20f),
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, DeepGold.copy(alpha = 0.45f))
                        ) {
                            Text(
                                text = currentQuote.source,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF8B2500),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        // Heart Button: Save / Favorite
                        IconButton(
                            onClick = {
                                viewModel.toggleBookmarkDevotionalQuote(context, currentQuote, date.toString())
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("btn_heart_quote_${currentQuote.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = if (isFavorite) "Saved in My Favorites" else "Save to My Favorites",
                                tint = if (isFavorite) Color(0xFFE53935) else Color(0xFFB0BEC5),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Sloka / Quote Container
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = androidx.compose.foundation.BorderStroke(0.6.dp, LightGold.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (language == AppLanguage.TE) currentQuote.titleTelugu else currentQuote.titleEnglish,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF880E4F),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "“ ${currentQuote.slokaOrSukti} ”",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Meaning (తాత్పర్యం)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🌺 తాత్పర్యం: ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = currentQuote.meaningTelugu,
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (showEnglish) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "English: ${currentQuote.meaningEnglish}",
                                fontSize = 11.5.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Buttons (WhatsApp Share, Heart/Save, Next Quote, Stotras)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Share Quote on WhatsApp
                        Button(
                            onClick = {
                                val shareText = DevotionalQuoteRepository.formatShareText(currentQuote, date)
                                val sendIntent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                context.startActivity(android.content.Intent.createChooser(sendIntent, "భక్తి సూక్తిని షేర్ చేయండి"))
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WhatsAppGreen,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_share_quote")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.TE) "వాట్సాప్ షేర్" else "Share Quote",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Heart Button (Favorite / Save)
                        IconButton(
                            onClick = {
                                viewModel.toggleBookmarkDevotionalQuote(context, currentQuote, date.toString())
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("btn_action_favorite_quote")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = if (isFavorite) "Saved in My Favorites" else "Save to My Favorites",
                                tint = if (isFavorite) Color(0xFFE53935) else Color(0xFF9E9E9E),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Toggle English meaning
                        TextButton(
                            onClick = { showEnglish = !showEnglish },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (showEnglish) "Hide EN" else "English",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Next / Cycle quote button
                        OutlinedButton(
                            onClick = { quoteOffset++ },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_next_quote")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Next Quote",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.TE) "మరో సూక్తి" else "Next",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Devotional Library shortcut
                        IconButton(
                            onClick = onOpenDevotionalLibrary,
                            modifier = Modifier.size(36.dp).testTag("btn_goto_devotional")
                        ) {
                            Text(text = "🪷", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

