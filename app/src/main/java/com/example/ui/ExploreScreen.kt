package com.example.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepGold
import com.example.ui.theme.AuspiciousGreen
import com.example.engine.DevotionalRepository
import com.example.engine.LocalizationEngine
import com.example.engine.ZodiacRepository
import com.example.model.AppLanguage
import com.example.model.DayPanchanga
import com.example.model.FamilyEvent
import com.example.model.SpiritualItem
import com.example.model.IndianDistrict
import com.example.model.IndianDistrictsRepository
import com.example.engine.AstronomicalEngine
import com.example.viewmodel.PanchangaViewModel
import java.time.LocalDate

@Composable
fun ExploreScreen(
    viewModel: PanchangaViewModel,
    modifier: Modifier = Modifier
) {
    val panchanga by viewModel.currentPanchanga.collectAsState()
    val familyEvents by viewModel.familyEvents.collectAsState()
    val isAudioPlaying by viewModel.isAudioPlaying.collectAsState()
    val selectedSpiritualItem by viewModel.selectedSpiritualItem.collectAsState()
    val language by viewModel.selectedLanguage.collectAsState()

    val exploreSubTab by viewModel.exploreSubTab.collectAsState()
    var selectedSubTab by remember { mutableIntStateOf(0) }
    var showAddEventDialog by remember { mutableStateOf(false) }

    LaunchedEffect(exploreSubTab) {
        selectedSubTab = exploreSubTab
    }

    val subTabTitlesBase = when (language) {
        AppLanguage.TE -> listOf("ఖగోళం & ఆకాశం", "రాశులు & నక్షత్రాలు", "భక్తి & స్తోత్రాలు", "కుటుంబ వ్రతాలు")
        AppLanguage.HI -> listOf("खगोल एवं आकाश", "राशि एवं नक्षत्र", "भक्ति एवं स्तोत्र", "परिवार एवं व्रत")
        AppLanguage.TA -> listOf("வானியல் & கோள்கள்", "ராசி & நட்சத்திரங்கள்", "பக்தி & ஸ்தோத்திரங்கள்", "குடும்ப விரதங்கள்")
        AppLanguage.KN -> listOf("ಖಗೋಳ & ಆಕಾಶ", "ರಾಶಿ & ನಕ್ಷತ್ರಗಳು", "ಭಕ್ತಿ & ಸ್ತೋತ್ರಗಳು", "ಕುಟುಂಬ ವ್ರತಗಳು")
        AppLanguage.ML -> listOf("ജ്യോതിശാസ്ത്രം", "രാശി & നക്ഷത്രങ്ങൾ", "ഭക്തി & സ്തോത്രങ്ങൾ", "കുടുംബ വ്രതങ്ങൾ")
        AppLanguage.MR -> listOf("खगोल आणि आकाश", "राशी आणि नक्षत्र", "भक्ती आणि स्तोत्रे", "कौटुंबिक व्रत")
        AppLanguage.GU -> listOf("ખગોળ અને આકાશ", "રાશિ અને નક્ષત્ર", "ભક્તિ અને સ્તોત્ર", "પારિવારિક વ્રત")
        AppLanguage.BN -> listOf("জ্যোতির্বিজ্ঞান ও আকাশ", "রাশি ও নক্ষত্র", "ভক্তি ও স্তোত্র", "পারিবারিক ব্রত")
        AppLanguage.OR_LANG -> listOf("ଜ୍ୟୋତିର୍ବିଜ୍ଞାନ ଓ ଆକାଶ", "ରାଶି ଓ ନକ୍ଷତ୍ର", "ଭକ୍ତି ଓ ସ୍ତୋତ୍ର", "ପାରିବାରିକ ବ୍ରତ")
        else -> listOf("Sky & Astronomy", "Zodiac & Stars", "Devotional", "Family & Vrat")
    }

    val subTabTitles = remember(subTabTitlesBase, language) {
        val list = subTabTitlesBase.toMutableList()
        val insertText = if (language == AppLanguage.TE) "జన్మ పంచాంగం 🕉️" else "Birth Panchangam 🕉️"
        if (list.size >= 2) {
            list.add(2, insertText)
        } else {
            list.add(insertText)
        }
        val auditText = if (language == AppLanguage.TE) "డేటా ధృవీకరణ & ఆడిట్ 📋" else "Data Audit 📋"
        list.add(auditText)
        list
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("explore_screen")
    ) {
        // Sub-tabs row
        ScrollableTabRow(
            selectedTabIndex = selectedSubTab,
            edgePadding = 0.dp,
            containerColor = Color(0xFFFFFDF5),
            contentColor = Color(0xFF8D1808),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = Color(0xFF8D1808)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            subTabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSubTab == index,
                    onClick = { 
                        selectedSubTab = index
                        viewModel.setExploreSubTab(index)
                    },
                    selectedContentColor = Color(0xFF8D1808),
                    unselectedContentColor = Color(0xFF5A4D3D),
                    text = { Text(title, fontSize = 12.sp, fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedSubTab) {
            0 -> SkyAndAstronomyTab(panchanga, language)
            1 -> ZodiacAndNakshatraTab(language)
            2 -> BirthPanchangamTab(viewModel, language)
            3 -> DevotionalLibraryTab(
                viewModel = viewModel,
                isAudioPlaying = isAudioPlaying,
                language = language,
                onSelectItem = { viewModel.selectSpiritualItem(it) }
            )
            4 -> FamilyCalendarTab(
                events = familyEvents,
                language = language,
                onAddEventClick = { showAddEventDialog = true },
                onDeleteEvent = { viewModel.deleteFamilyEvent(it) }
            )
            5 -> AuditScreen(viewModel = viewModel)
        }
    }

    // Add Family Event Dialog
    if (showAddEventDialog) {
        var title by remember { mutableStateOf("") }
        var person by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("Birthday") }
        var tithi by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddEventDialog = false },
            title = {
                Text(
                    when (language) {
                        AppLanguage.TE -> "కుటుంబ వ్రతం / వేడుక జోడించండి"
                        AppLanguage.HI -> "परिवार का धार्मिक प्रसंग जोड़ें"
                        AppLanguage.TA -> "குடும்ப நிகழ்வு சேர்க்க"
                        else -> "Add Family Event / Observance"
                    }
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(LocalizationEngine.get("event_title", language)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = person,
                        onValueChange = { person = it },
                        label = { Text(LocalizationEngine.get("family_member_name", language)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tithi,
                        onValueChange = { tithi = it },
                        label = { Text(LocalizationEngine.get("traditional_tithi_vrat", language)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addFamilyEvent(title, person, type, LocalDate.now().plusDays(5), tithi)
                            showAddEventDialog = false
                        }
                    }
                ) {
                    Text(
                        when (language) {
                            AppLanguage.TE -> "భద్రపరచండి"
                            AppLanguage.HI -> "सहेजें"
                            AppLanguage.TA -> "சேமிக்கவும்"
                            else -> "Save Event"
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddEventDialog = false }) {
                    Text(LocalizationEngine.get("close", language))
                }
            }
        )
    }

    // Spiritual Item Detail Dialog
    selectedSpiritualItem?.let { item ->
        AlertDialog(
            onDismissRequest = { viewModel.selectSpiritualItem(null) },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = item.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    IconButton(onClick = { viewModel.toggleDevotionalAudio() }) {
                        Icon(
                            imageVector = if (isAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Audio Chime",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "${LocalizationEngine.get("deity", language)}: ${LocalizationEngine.translateDeity(item.deity, language)} · ${item.category}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    item.verses.forEach { verse ->
                        Text(
                            text = verse,
                            fontSize = 13.sp,
                            fontWeight = if (verse.contains("॥") || verse.startsWith("ॐ")) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = LocalizationEngine.get("translation_meaning", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = item.englishMeaning, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = LocalizationEngine.get("spiritual_significance_benefits", language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = item.benefits, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.selectSpiritualItem(null) }) {
                    Text(LocalizationEngine.get("close", language))
                }
            }
        )
    }
}

@Composable
fun SkyAndAstronomyTab(panchanga: DayPanchanga, language: AppLanguage) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Moon Phase Visual Card with Canvas drawing
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LocalizationEngine.get("astronomy_sky", language),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Canvas rendering of Moon disk
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E1035)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val radius = size.minDimension / 2.0f
                        val center = Offset(size.width / 2.0f, size.height / 2.0f)

                        drawCircle(color = Color(0xFF261942), radius = radius, center = center)

                        val illumPercent = panchanga.moonPhase.illuminationPercent / 100.0f
                        val glowColor = Color(0xFFFFE082)

                        drawCircle(
                            color = glowColor.copy(alpha = illumPercent.coerceIn(0.2f, 1f)),
                            radius = radius * (0.4f + 0.6f * illumPercent),
                            center = center
                        )
                    }

                    Text(
                        text = "${panchanga.moonPhase.illuminationPercent}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = LocalizationEngine.translateMoonPhase(panchanga.moonPhase.phaseName, language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = if (language == AppLanguage.TE) "${LocalizationEngine.get("chandra_moon", language)}: ${panchanga.moonPhase.moonAgeDays} రోజులు / 29.53 రోజుల చక్రం" else "${LocalizationEngine.get("chandra_moon", language)}: ${panchanga.moonPhase.moonAgeDays} d / 29.53d cycle",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Solar Positions Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${LocalizationEngine.get("surya_sun", language)} & ${LocalizationEngine.get("chandra_moon", language)}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "${LocalizationEngine.get("sunrise", language)}:", fontSize = 11.sp, maxLines = 1)
                    Text(text = panchanga.sunMoonTimes.sunrise, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "${LocalizationEngine.get("sunset", language)}:", fontSize = 11.sp, maxLines = 1)
                    Text(text = panchanga.sunMoonTimes.sunset, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "${LocalizationEngine.get("day_length", language)}:", fontSize = 11.sp, maxLines = 1)
                    Text(text = panchanga.sunMoonTimes.dayLength, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "${LocalizationEngine.get("zodiac", language)}:", fontSize = 11.sp, maxLines = 1)
                    Text(text = LocalizationEngine.translateRashi(panchanga.sunMoonTimes.sunZodiac, language), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, maxLines = 1)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "${LocalizationEngine.get("ayanam", language)}:", fontSize = 11.sp, maxLines = 1)
                    Text(text = LocalizationEngine.translateAyana(panchanga.ayana, language), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun ZodiacAndNakshatraTab(language: AppLanguage) {
    val context = LocalContext.current
    var subTab by remember { mutableStateOf(0) } // 0: రాశులు (Rashis), 1: నక్షత్రాలు (Nakshatras), 2: పాదాలు (Padas)
    
    // Search query for Padas
    var padaSearchQuery by remember { mutableStateOf("") }
    
    // Selected states for detail dialogs
    var selectedRashiForDetail by remember { mutableStateOf<com.example.engine.RashiDetail?>(null) }
    var selectedNakshatraForDetail by remember { mutableStateOf<com.example.engine.NakshatraDetail?>(null) }
    var selectedPadaForDetail by remember { mutableStateOf<com.example.engine.PadaDetail?>(null) }
    
    val padasList = remember { com.example.engine.ZodiacRepository.get108Padas() }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Sub Tab Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val chips = listOf("రాశులు (12)", "నక్షత్రాలు (27)", "పాదాలు (108)")
            chips.forEachIndexed { index, title ->
                val isSelected = subTab == index
                FilterChip(
                    selected = isSelected,
                    onClick = { subTab = index },
                    label = { 
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ) 
                    },
                    modifier = Modifier.weight(1f).testTag("zodiac_sub_tab_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (subTab) {
            0 -> {
                // 12 Rashis List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(com.example.engine.ZodiacRepository.RASHIS) { rashi ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedRashiForDetail = rashi }
                                .testTag("card_rashi_${rashi.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = rashi.symbol,
                                            fontSize = 24.sp,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Column {
                                            Text(
                                                text = rashi.teluguName,
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            )
                                            Text(
                                                text = rashi.westernName,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                                        contentDescription = "వివరాలు",
                                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = rashi.teluguDescription,
                                    fontSize = 11.5.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "అధిపతి: ${rashi.teluguRuler}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "రత్నం: ${rashi.teluguGemstone}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // 27 Nakshatras List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(com.example.engine.ZodiacRepository.NAKSHATRAS) { nak ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedNakshatraForDetail = nak }
                                .testTag("card_nakshatra_${nak.number}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = nak.teluguName,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        )
                                        Text(
                                            text = "${nak.name} (${nak.sanskritName})",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                                        contentDescription = "వివరాలు",
                                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = nak.teluguTraits,
                                    fontSize = 11.5.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "అధిపతి: ${nak.teluguRuler}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "దేవత: ${nak.teluguDeity}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // 108 Padas List with Search
                Column(modifier = Modifier.fillMaxSize()) {
                    OutlinedTextField(
                        value = padaSearchQuery,
                        onValueChange = { padaSearchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .testTag("search_padas"),
                        placeholder = { Text("నక్షత్రం, రాశి లేదా అక్షరధ్వనితో శోధించండి...", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                                contentDescription = "శోధన",
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    val filteredPadas = remember(padaSearchQuery, padasList) {
                        if (padaSearchQuery.isBlank()) {
                            padasList
                        } else {
                            val q = padaSearchQuery.lowercase().trim()
                            padasList.filter {
                                it.nakshatraTeluguName.lowercase().contains(q) ||
                                it.nakshatraName.lowercase().contains(q) ||
                                it.rashiTeluguName.lowercase().contains(q) ||
                                it.rashiName.lowercase().contains(q) ||
                                it.namingSyllable.lowercase().contains(q) ||
                                it.navamsaRashiTeluguName.lowercase().contains(q)
                            }
                        }
                    }

                    if (filteredPadas.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("ఫలితాలు ఏవీ లభించలేదు", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(filteredPadas) { pada ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedPadaForDetail = pada }
                                        .testTag("card_pada_${pada.absoluteIndex}"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f)),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "${pada.nakshatraTeluguName} - ${pada.padaNumber}వ పాదం",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.5.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "${pada.rashiTeluguName} (${pada.startDegreeStr} - ${pada.endDegreeStr})",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "నవాంశ: ${pada.navamsaRashiTeluguName} · అక్షరధ్వని: '${pada.namingSyllable}'",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = pada.namingSyllable,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                fontWeight = FontWeight.Black,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
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

    // ==========================================
    // DETAIL DIALOGS
    // ==========================================

    // 1. Rashi Detail Dialog
    if (selectedRashiForDetail != null) {
        val rashi = selectedRashiForDetail!!
        androidx.compose.ui.window.Dialog(onDismissRequest = { selectedRashiForDetail = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .testTag("dialog_rashi_detail"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(rashi.symbol, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                                Column {
                                    Text(
                                        text = rashi.teluguName,
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                    )
                                    Text(
                                        text = rashi.westernName,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        val shareText = "${rashi.symbol} *${rashi.teluguName} (${rashi.westernName}) రాశి వివరాలు* ${rashi.symbol}\n\n" +
                                            "🪐 రాశ్యాధిపతి: ${rashi.teluguRuler}\n" +
                                            "🔥 తత్త్వం: ${rashi.teluguElement}\n" +
                                            "💎 అదృష్ట రత్నం: ${rashi.teluguGemstone}\n" +
                                            "🔤 నామాక్షరాలు: ${rashi.teluguNamingLetters}\n" +
                                            "🎨 శుభ వర్ణం: ${rashi.teluguColor} | సంఖ్య: ${rashi.luckyNumber}\n" +
                                            "✨ దిన ఫలితం: ${rashi.teluguDailyForecast}\n\n" +
                                            "— శ్రీ వేద తెలుగు పంచాంగం"
                                        val sendIntent = android.content.Intent().apply {
                                            action = android.content.Intent.ACTION_SEND
                                            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                            type = "text/plain"
                                        }
                                        context.startActivity(android.content.Intent.createChooser(sendIntent, "${rashi.teluguName} వివరాలు షేర్ చేయండి"))
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "రాశి వివరాలు షేర్ చేయండి",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { selectedRashiForDetail = null },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        val swabhavaData = remember(rashi.id) {
                            com.example.engine.RashiNakshatra108Data.JANMA_RASHI_SWABHAVALU_LIST.firstOrNull { it.rashiId == rashi.id }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            TableCellRow(label = "రాశ్యాధిపతి", value = rashi.teluguRuler, isEven = true)
                            TableCellRow(label = "పంచభూత తత్త్వం", value = rashi.teluguElement, isEven = false)
                            if (swabhavaData != null) {
                                TableCellRow(label = "స్వభావం & లింగం", value = "${swabhavaData.svabhavam} | ${swabhavaData.lingam}", isEven = true)
                                TableCellRow(label = "ప్రతీకం & దిశ", value = "${swabhavaData.pratikam} | దిశ: ${swabhavaData.disha}", isEven = false)
                                TableCellRow(label = "కాళపురుష భాగం", value = swabhavaData.kalapurushaBhagam, isEven = true)
                                TableCellRow(label = "నక్షత్ర పాదాలు", value = "${swabhavaData.nakshatraPadalu} (${swabhavaData.totalPadalu} పాదాలు)", isEven = false)
                            }
                            TableCellRow(label = "అదృష్ట రత్నం", value = rashi.teluguGemstone, isEven = true)
                            TableCellRow(label = "నామాక్షరాలు", value = rashi.teluguNamingLetters, isEven = false)
                            TableCellRow(label = "శుభ వర్ణం & సంఖ్య", value = "${rashi.teluguColor} | సంఖ్య: ${rashi.luckyNumber}", isEven = true)
                            if (swabhavaData != null) {
                                TableCellRow(label = "ప్రధాన స్వభావం", value = swabhavaData.pradhanaSvabhavam, isEven = false)
                                TableCellRow(label = "సాధారణ లక్షణాలు", value = swabhavaData.sadharanaLakshanalu, isEven = true)
                                if (swabhavaData.manchiVaipu.isNotBlank()) {
                                    TableCellRow(label = "మంచి వైపు", value = swabhavaData.manchiVaipu, isEven = false)
                                }
                                TableCellRow(label = "సవాళ్లు & జాగ్రత్తలు", value = swabhavaData.savallu, isEven = true)
                                TableCellRow(label = "సంప్రదాయ వృత్తి రంగాలు", value = swabhavaData.vruttiRangalu, isEven = false)
                            } else {
                                TableCellRow(label = "రాశి స్వభావము", value = rashi.teluguDescription, isEven = false)
                            }
                            TableCellRow(label = "దిన ఫలితము", value = rashi.teluguDailyForecast, isEven = true, highlight = true)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "ఈ రాశికి సంబంధించిన నక్షత్రాలు & పాదాలు:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        val connectedNaks = remember(rashi) {
                            com.example.engine.ZodiacRepository.NAKSHATRAS.filter { nak ->
                                rashi.teluguNakshatras.contains(nak.teluguName.replace(Regex("^\\d+\\.\\s*"), "").take(3)) ||
                                nak.teluguRashi.contains(rashi.teluguName.take(3))
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            connectedNaks.forEach { nak ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedRashiForDetail = null
                                            subTab = 1
                                            selectedNakshatraForDetail = nak
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = nak.teluguName,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "అధిపతి: ${nak.teluguRuler} · దేవత: ${nak.teluguDeity}",
                                                fontSize = 10.5.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                                            contentDescription = "వెళ్ళు",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
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

    // 2. Nakshatra Detail Dialog
    if (selectedNakshatraForDetail != null) {
        val nak = selectedNakshatraForDetail!!
        androidx.compose.ui.window.Dialog(onDismissRequest = { selectedNakshatraForDetail = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .testTag("dialog_nakshatra_detail"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = nak.teluguName,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                )
                                Text(
                                    text = "${nak.name} (${nak.sanskritName})",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f))
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        val shareText = "⭐ *${nak.teluguName} (${nak.name}) నక్షత్ర వివరాలు* ⭐\n\n" +
                                            "🙏 అధిదేవత: ${nak.teluguDeity}\n" +
                                            "🪐 నక్షత్రాధిపతి: ${nak.teluguRuler}\n" +
                                            "🌌 సంబంధిత రాశి: ${nak.teluguRashi}\n" +
                                            "🔱 గణము: ${nak.teluguGana} | యోని: ${nak.teluguYoni}\n" +
                                            "🔤 నామాక్షరాలు: ${nak.teluguNamingLetters}\n" +
                                            "💫 గుణగణాలు: ${nak.teluguTraits}\n\n" +
                                            "— శ్రీ వేద తెలుగు పంచాంగం"
                                        val sendIntent = android.content.Intent().apply {
                                            action = android.content.Intent.ACTION_SEND
                                            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                            type = "text/plain"
                                        }
                                        context.startActivity(android.content.Intent.createChooser(sendIntent, "${nak.teluguName} వివరాలు షేర్ చేయండి"))
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "నక్షత్రం వివరాలు షేర్ చేయండి",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { selectedNakshatraForDetail = null },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            TableCellRow(label = "అధిదేవత", value = nak.teluguDeity, isEven = true)
                            TableCellRow(label = "నక్షత్రాధిపతి", value = nak.teluguRuler, isEven = false)
                            TableCellRow(label = "సంబంధిత రాశి", value = nak.teluguRashi, isEven = true)
                            TableCellRow(label = "గణము", value = nak.teluguGana, isEven = false)
                            TableCellRow(label = "యోని", value = nak.teluguYoni, isEven = true)
                            TableCellRow(label = "నామాక్షరాలు", value = nak.teluguNamingLetters, isEven = false)
                            TableCellRow(label = "చిహ్నం", value = nak.symbol, isEven = true)
                            TableCellRow(label = "గుణగణాలు", value = nak.teluguTraits, isEven = false, highlight = true)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "నక్షత్ర చతుష్పాదాలు (4 పాదాలు):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        val connectedPadas = remember(nak) {
                            padasList.filter { it.nakshatraNumber == nak.number }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            connectedPadas.forEach { pada ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedNakshatraForDetail = null
                                            subTab = 2
                                            selectedPadaForDetail = pada
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "${pada.padaNumber}వ పాదం (అక్షరధ్వని: '${pada.namingSyllable}')",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = "రాశి: ${pada.rashiTeluguName} · నవాంశ: ${pada.navamsaRashiTeluguName} నవాంశం · విభాగం: ${pada.startDegreeStr} - ${pada.endDegreeStr}",
                                                fontSize = 10.5.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Icon(
                                            imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                                            contentDescription = "వెళ్ళు",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
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

    // 3. Pada Detail Dialog
    if (selectedPadaForDetail != null) {
        val pada = selectedPadaForDetail!!
        androidx.compose.ui.window.Dialog(onDismissRequest = { selectedPadaForDetail = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .testTag("dialog_pada_detail"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${pada.nakshatraTeluguName} - ${pada.padaNumber}వ పాదం",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = "క్రమ సంఖ్య: ${pada.absoluteIndex} / 108",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val shareText = "✨ *${pada.nakshatraTeluguName} - ${pada.padaNumber}వ పాదం (నామాక్షరం / వివరాలు)* ✨\n\n" +
                                        "🔤 నామాక్షరధ్వని: '${pada.namingSyllable}'\n" +
                                        "🌌 సంచార రాశి: ${pada.rashiTeluguName}\n" +
                                        "🧭 నవాంశ రాశి: ${pada.navamsaRashiTeluguName} నవాంశం\n" +
                                        "🎯 పురుషార్థం: ${pada.purusharthaTe}\n" +
                                        "🌟 సాంప్రదాయ గుణం: ${pada.traitsTe}\n" +
                                        "💫 వ్యక్తిత్వ విశ్లేషణ: ${pada.personalityTe}\n\n" +
                                        "— శ్రీ వేద తెలుగు పంచాంగం"
                                    val sendIntent = android.content.Intent().apply {
                                        action = android.content.Intent.ACTION_SEND
                                        putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(android.content.Intent.createChooser(sendIntent, "పాదం వివరాలు షేర్ చేయండి"))
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "షేర్ చేయండి",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = { selectedPadaForDetail = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        TableCellRow(label = "ఖగోళ విభాగం", value = "${pada.startDegreeStr} నుండి ${pada.endDegreeStr} వరకు", isEven = true)
                        TableCellRow(label = "సంచార రాశి", value = pada.rashiTeluguName, isEven = false)
                        TableCellRow(label = "నవాంశ రాశి", value = "${pada.navamsaRashiTeluguName} నవాంశం", isEven = true)
                        TableCellRow(label = "పురుషార్థం", value = pada.purusharthaTe, isEven = false)
                        TableCellRow(label = "అక్షరధ్వని", value = "'${pada.namingSyllable}'", isEven = true)
                        TableCellRow(label = "సాంప్రదాయ గుణం", value = pada.traitsTe, isEven = false)
                        TableCellRow(label = "వ్యక్తిత్వ విశ్లేషణ", value = pada.personalityTe, isEven = true, highlight = true)
                    }
                }
            }
        }
    }
}

@Composable
fun TableCellRow(
    label: String,
    value: String,
    isEven: Boolean,
    highlight: Boolean = false
) {
    val bgColor = if (highlight) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
    } else if (isEven) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(115.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 11.5.sp,
            fontWeight = if (highlight) FontWeight.SemiBold else FontWeight.Normal,
            color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), thickness = 1.dp)
}

data class BirthPanchangaResult(
    val rashi: String,
    val rashiEnglish: String,
    val rashiSymbol: String,
    val nakshatra: String,
    val pada: Int,
    val tithi: String,
    val paksha: String,
    val rulingPlanet: String,
    val deity: String,
    val luckyNumber: Int,
    val luckyColor: String,
    val luckyGemstone: String,
    val characterTraits: String,
    val luckyHours: String,
    val physicalAppearance: String,
    val professionTips: String,
    val lagna: String = "",
    val lagnaIndex: Int = 0,
    val lagnaLord: String = "",
    val nakshatraLord: String = "",
    val yoga: String = "",
    val karana: String = "",
    val birthPlaceName: String = "",
    val planetPlacements: Map<Int, List<String>> = emptyMap()
)

fun getRashiIndex(rashi: String): Int {
    val rashiNames = listOf(
        "మేషం", "వృషభం", "మిథునం", "కర్కాటకం", "సింహం", "కన్య", "తులా", "వృశ్చికం", "ధనుస్సు", "మకరం", "కుంభం", "మీనం"
    )
    val idx = rashiNames.indexOf(rashi.trim())
    return if (idx >= 0) idx else 0
}

fun getNakshatraTraits(nakshatra: String): String {
    return when (nakshatra) {
        "అశ్విని" -> "అశ్విని నక్షత్రంలో జన్మించిన వారు చురుకైన బుద్ధి, ఉత్సాహం కలిగి ఉంటారు. పనులను వేగంగా పూర్తి చేయగలరు. నాయకత్వ లక్షణాలు మెండుగా ఉంటాయి."
        "భరణి" -> "భరణి నక్షత్ర జాతకులు స్థిరమైన మనస్తత్వం, పట్టుదల కలిగి ఉంటారు. సత్యాన్ని ఇష్టపడతారు. కళల పట్ల ఆసక్తి మరియు ఆకర్షణీయమైన వ్యక్తిత్వం వీరి సొంతం."
        "కృత్తిక" -> "కృత్తిక నక్షత్రంలో జన్మించిన వారు తేజస్సు, పదునైన బుద్ధి కలిగి ఉంటారు. స్వతంత్ర భావాలు ఎక్కువ. సమాజంలో మంచి గౌరవ ప్రతిష్టలు సాధిస్తారు."
        "రోహిణి", "రోహిణి" -> "రోహిణి నక్షత్ర జాతకులు అత్యంత ఆకర్షణీయులు, మృదు స్వభావులు. సృజనాత్మకత, కళాత్మక నైపుణ్యాలు మెండుగా ఉంటాయి. ఇతరులను సులభంగా ఆకట్టుకుంటారు."
        "మృగశిర" -> "మృగశిర నక్షత్ర జాతకులు పరిశోధనాత్మక మనస్తత్వం, సత్య అన్వేషణ కలిగి ఉంటారు. స్నేహపూర్వక ప్రవర్తన మరియు ప్రయాణాల పట్ల ఆసక్తి కలిగి ఉంటారు."
        "ఆరుద్ర" -> "ఆరుద్ర నక్షత్రంలో జన్మించిన వారు తీక్షణమైన బుద్ధి, కష్టపడే తత్వం కలిగి ఉంటారు. భావోద్వేగాలు ఎక్కువ. సమస్యలను ధైర్యంగా ఎదుర్కొంటారు."
        "పునర్వసు" -> "పునర్వసు నక్షత్ర జాతకులు దయాగుణం, శాంత స్వభావం కలిగి ఉంటారు. ఆధ్యాత్మిక చింతన ఎక్కువ. సమాజంలో మంచి పేరు ప్రతిష్టలు సంపాదిస్తారు."
        "పుష్యమి" -> "పుష్యమి నక్షత్ర జాతకులు ధర్మచింతన, క్రమశిక్షణ కలిగి ఉంటారు. పోషించే గుణం మరియు సేవా భావం వీరి ప్రత్యేకత. అత్యంత అదృష్టవంతులుగా ఉంటారు."
        "ఆశ్లేష" -> "ఆశ్లేష నక్షత్రంలో జన్మించిన వారు చతురత, నాయకత్వ లక్షణాలు కలిగి ఉంటారు. స్వతంత్ర నిర్ణయాలు తీసుకుంటారు. వీరికి గూఢచర్య బుద్ధి మరియు వ్యాపార కౌశలం ఎక్కువ."
        "మఖ" -> "మఖ నక్షత్ర జాతకులు రాజసమైన వ్యక్తిత్వం, ఆత్మగౌరవం కలిగి ఉంటారు. పితృభక్తి ఎక్కువ. సమాజంలో ఉన్నత స్థానాన్ని మరియు కీర్తిని పొందుతారు."
        "పూర్వ ఫల్గుణి" -> "పూర్వ ఫల్గుణి నక్షత్ర జాతకులు విలాసవంతమైన జీవితాన్ని ఇష్టపడతారు. సంగీతం, కళల పట్ల ఆసక్తి ఉంటుంది. సామాజిక సంబంధాలలో చురుకుగా ఉంటారు."
        "ఉత్తర ఫల్గుణి" -> "ఉత్తర ఫల్గుణి నక్షత్రంలో జన్మించిన వారు సహాయ గుణం, ధైర్యం కలిగి ఉంటారు. నమ్మకమైన స్నేహితులుగా ఉంటారు. ఉద్యోగ, వ్యాపార రంగాలలో రాణిస్తారు."
        "హస్త" -> "హస్త నక్షత్ర జాతకులు చతురత, హస్తకళల నైపుణ్యం కలిగి ఉంటారు. హాస్య ప్రియులు. ప్రణాళికాబద్ధంగా వ్యవహరించి విజయం సాధిస్తారు."
        "చిత్ర" -> "చిత్ర నక్షత్ర జాతకులు కళాత్మక దృష్టి, సృజనాత్మకత కలిగి ఉంటారు. ఆకర్షణీయమైన రూపం ఉంటుంది. వృత్తిపరంగా అద్భుతమైన విజయాలు అందుకుంటారు."
        "స్వాతి" -> "స్వాతి నక్షత్రంలో జన్మించిన వారు స్వతంత్ర భావాలు, అపారమైన మేధస్సు కలిగి ఉంటారు. న్యాయబద్ధంగా నడుచుకుంటారు. ఆర్థికంగా మంచి అభివృద్ధి సాధిస్తారు."
        "విశాఖ" -> "విశాఖ నక్షత్ర జాతకులు పట్టుదల, ఆశయ సిద్ధి కలిగి ఉంటారు. కష్టపడి విజయాన్ని అందుకుంటారు. ఆధ్యాత్మిక వృద్ధి మరియు మంచి వాక్చాతుర్యం వీరి సొంతం."
        "అనూరాధ" -> "అనూరాధ నక్షత్ర జాతకులు స్నేహశీలురు, నిరాడంబరులు. విదేశీ ప్రయాణాల పట్ల ఆసక్తి ఉంటుంది. ఎంతటి కష్టమైనా తట్టుకుని నిలబడే శక్తి వీరి స్వభావం."
        "జ్యేష్ఠ" -> "జ్యేష్ఠ నక్షత్రంలో జన్మించిన వారు ఆత్మవిశ్వాసం, రక్షించే గుణం కలిగి ఉంటారు. నాయకత్వ స్థానాలలో రాణిస్తారు. సమాజంలో ప్రతిష్టాత్మకమైన గౌరవం పొందుతారు."
        "మూల" -> "మూల నక్షత్ర జాతకులు లోతైన జ్ఞానం, దార్శనికత కలిగి ఉంటారు. పరిశోధనలు చేయడం వీరి సహజ గుణం. జీవితంలో అనేక మలుపులను దాటి విజయం సాధిస్తారు."
        "పూర్వాషాఢ" -> "పూర్వాషాఢ నక్షత్ర జాతకులు సానుకూల దృక్పథం, ఆత్మవిశ్వాసం కలిగి ఉంటారు. అపజయాలకు కుంగిపోరు. సంఘంలో కీర్తి మరియు స్నేహ సంపద కలిగి ఉంటారు."
        "ఉత్తరాషాఢ" -> "ఉత్తరాషాఢ నక్షత్రంలో జన్మించిన వారు సత్యసంధులు, శాంతమూర్తులు. శత్రువులపై విజయం సాధిస్తారు. సమాజ హితం కోసం పాటుపడతారు."
        "శ్రవణం" -> "శ్రవణ నక్షత్ర జాతకులు విజ్ఞాన దాహం, దయాగుణం కలిగి ఉంటారు. మంచి శ్రోతలు. ఇతరులకు సహాయం చేయడం మరియు కీర్తిని ఆర్జించడం వీరి ప్రత్యేకత."
        "ధనిష్ఠ" -> "ధనిష్ఠ నక్షత్ర జాతకులు సంగీతం, కళల పట్ల ఆసక్తి కలిగి ఉంటారు. సాహసోపేతమైన పనులు చేస్తారు. నాయకత్వ నైపుణ్యాలు మరియు అపారమైన కీర్తి పొందుతారు."
        "శతభిషం" -> "శతభిష నక్షత్రంలో జన్మించిన వారు రహస్యాలను కాపాడగలరు. తత్వశాస్త్రం మరియు వైద్య రంగాల పట్ల ఆసక్తి ఉంటుంది. స్వతంత్ర ఆలోచనలు కలిగి ఉంటారు."
        "పూర్వాభాద్ర" -> "పూర్వాభాద్ర నక్షత్ర జాతకులు నిస్వార్థమైన స్వభావం, ఆధ్యాత్మిక ఉన్నతి కలిగి ఉంటారు. ఇతరులకు మార్గదర్శిగా నిలుస్తారు. ఉన్నతమైన ఆశయాలు కలిగి ఉంటారు."
        "ఉత్తరాభాద్ర" -> "ఉత్తరాభాద్ర నక్షత్ర జాతకులు వివేకం, శాంతం, క్రమశిక్షణ కలిగి ఉంటారు. కుటుంబం పట్ల ప్రేమ ఎక్కువ. సమాజంలో గౌరవనీయమైన స్థానాన్ని పొందుతారు."
        "రేవతి" -> "రేవతి నక్షత్ర జాతకులు మృదుస్వభావులు, పరోపకారులు. కళల పట్ల, ప్రయాణాల పట్ల మక్కువ కలిగి ఉంటారు. జీవితంలో పరిపూర్ణమైన ఆనందం మరియు విజయాన్ని అందుకుంటారు."
        else -> "అద్భుతమైన మేధస్సు, పట్టుదల మరియు సద్గుణాలు కలిగిన వ్యక్తిత్వం వీరి సొంతం."
    }
}

fun calculateBirthPanchanga(
    day: Int,
    month: Int,
    year: Int,
    hour: Int,
    minute: Int,
    district: IndianDistrict,
    language: AppLanguage
): BirthPanchangaResult {
    // Local birth time converted to UTC day fraction (India IST is UTC + 5:30)
    val localHourDecimal = hour.toDouble() + minute.toDouble() / 60.0
    val utcHourDecimal = localHourDecimal - 5.5
    val dayFractionUtc = utcHourDecimal / 24.0

    val jd = AstronomicalEngine.toJulianDay(year, month, day, dayFractionUtc)

    // Tropical Solar & Lunar Longitudes
    val sunTropical = AstronomicalEngine.getSunLongitude(jd)
    val moonTropical = AstronomicalEngine.getMoonLongitude(jd)

    // Lahiri Ayanamsha for the given birth year and month
    val ayanamsha = AstronomicalEngine.getLahiriAyanamsha(year, month)

    // Sidereal Nirayana Longitudes
    val sunSidereal = (sunTropical - ayanamsha + 360.0) % 360.0
    val moonSidereal = (moonTropical - ayanamsha + 360.0) % 360.0

    // Sidereal Lagna (Ascendant)
    val lagnaTropical = AstronomicalEngine.getLagnaLongitude(jd, district.latitude, district.longitude)
    val lagnaSidereal = (lagnaTropical - ayanamsha + 360.0) % 360.0
    val lagnaIndex = (lagnaSidereal / 30.0).toInt().coerceIn(0, 11)

    // Other Planets Sidereal Longitudes
    val marsSidereal = (AstronomicalEngine.getMarsLongitude(jd) - ayanamsha + 360.0) % 360.0
    val mercurySidereal = (AstronomicalEngine.getMercuryLongitude(jd) - ayanamsha + 360.0) % 360.0
    val jupiterSidereal = (AstronomicalEngine.getJupiterLongitude(jd) - ayanamsha + 360.0) % 360.0
    val venusSidereal = (AstronomicalEngine.getVenusLongitude(jd) - ayanamsha + 360.0) % 360.0
    val saturnSidereal = (AstronomicalEngine.getSaturnLongitude(jd) - ayanamsha + 360.0) % 360.0
    val rahuSidereal = (AstronomicalEngine.getRahuLongitude(jd) - ayanamsha + 360.0) % 360.0
    val ketuSidereal = (AstronomicalEngine.getKetuLongitude(jd) - ayanamsha + 360.0) % 360.0

    // Nakshatra and Pada (from Moon Sidereal)
    val nakshatraSpan = 360.0 / 27.0 // 13.333333333333334 degrees
    val nakshatraIndex = (moonSidereal / nakshatraSpan).toInt().coerceIn(0, 26)
    val pada = ((moonSidereal % nakshatraSpan) / (nakshatraSpan / 4.0)).toInt() + 1

    // Rashi (from Moon Sidereal)
    val rashiIndex = (moonSidereal / 30.0).toInt().coerceIn(0, 11)

    // Tithi (from Moon - Sun Elongation)
    val elongation = (moonTropical - sunTropical + 360.0) % 360.0
    val tithiNumber = (elongation / 12.0).toInt() + 1 // 1..30
    val isShukla = tithiNumber <= 15
    val paksha = if (language == AppLanguage.TE) {
        if (isShukla) "శుక్ల పక్షం" else "కృష్ణ పక్షం"
    } else {
        if (isShukla) "Shukla Paksha" else "Krishna Paksha"
    }
    val tithiIndexInPaksha = if (isShukla) tithiNumber else (tithiNumber - 15)

    val tithiNamesTe = listOf(
        "పాడ్యమి", "విదియ", "తదియ", "చవితి", "పంచమి",
        "షష్ఠి", "సప్తమి", "అష్టమి", "నవమి", "దశమి",
        "ఏకాదశి", "ద్వాదశి", "త్రయోదశి", "చతుర్దశి"
    )
    val selectedTithi = if (tithiIndexInPaksha == 15) {
        if (isShukla) "పౌర్ణమి" else "అమావాస్య"
    } else {
        tithiNamesTe.getOrElse(tithiIndexInPaksha - 1) { "పాడ్యమి" }
    }

    // Yoga
    val yogaIndex = (((sunSidereal + moonSidereal) % 360.0) / nakshatraSpan).toInt().coerceIn(0, 26)
    val yogaNamesTe = listOf(
        "విష్కంభం", "ప్రీతి", "ఆయుష్మాన్", "సౌభాగ్య", "శోభన", "అతిగండ", "సుకర్మ",
        "ధృతి", "శూల", "గండ", "వృద్ధి", "ధ్రువ", "వ్యాఘాత", "హర్షణ",
        "వజ్ర", "సిద్ధి", "వ్యతీపాత", "వరీయాన్", "పరిఘ", "శివ", "సిద్ధ",
        "సాధ్య", "శుభ", "శుక్ల", "బ్రహ్మ", "ఐంద్ర", "వైధృతి"
    )
    val selectedYoga = yogaNamesTe.getOrElse(yogaIndex) { "శుభ" }

    // Karana
    val karanaTotal = (elongation / 6.0).toInt() + 1 // 1..60
    val selectedKarana = when (karanaTotal) {
        1 -> "కింస్తుఘ్న"
        in 2..57 -> {
            val charaIndex = (karanaTotal - 2) % 7
            val charaNames = listOf("బవ", "బాలవ", "కౌలవ", "తైతుల", "గరజ", "వణిజ", "విష్టి (భద్ర)")
            charaNames[charaIndex]
        }
        58 -> "శకుని"
        59 -> "చతుష్పాత్"
        60 -> "నాగవ"
        else -> "బవ"
    }

    val nakshatrasFixed = listOf(
        "అశ్విని", "భరణి", "కృత్తిక", "రోహిణి", "మృగశిర", "ఆరుద్ర", "పునర్వసు", "పుష్యమి", "ఆశ్లేష",
        "మఖ", "పూర్వ ఫల్గుణి", "ఉత్తర ఫల్గుణి", "హస్త", "చిత్ర", "స్వాతి", "విశాఖ", "అనూరాధ", "జ్యేష్ఠ",
        "మూల", "పూర్వాషాఢ", "ఉత్తరాషాఢ", "శ్రవణం", "ధనిష్ఠ", "శతభిషం", "పూర్వాభాద్ర", "ఉత్తరాభాద్ర", "రేవతి"
    )
    val selectedNakshatra = nakshatrasFixed.getOrElse(nakshatraIndex) { "అశ్విని" }

    val rashis = listOf(
        "మేషం", "వృషభం", "మిథునం", "కర్కాటకం", "సింహం", "కన్య", "తులా", "వృశ్చికం", "ధనుస్సు", "మకరం", "కుంభం", "మీనం"
    )
    val rashisEnglish = listOf(
        "Mesha (Aries)", "Vrishabha (Taurus)", "Mithuna (Gemini)", "Karkataka (Cancer)",
        "Simha (Leo)", "Kanya (Virgo)", "Tula (Libra)", "Vrishchika (Scorpio)",
        "Dhanus (Sagittarius)", "Makara (Capricorn)", "Kumbha (Aquarius)", "Meena (Pisces)"
    )
    val selectedRashi = rashis.getOrElse(rashiIndex) { "మేషం" }
    val selectedRashiEnglish = rashisEnglish.getOrElse(rashiIndex) { "Aries" }
    val rashiSymbols = listOf("♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓")
    val selectedRashiSymbol = rashiSymbols.getOrElse(rashiIndex) { "♈" }

    val lagnaName = "${rashis[lagnaIndex]} లగ్నం (${rashisEnglish[lagnaIndex]})"

    val rulersFixed = listOf(
        "కుజుడు (అంగారకుడు)", "శుక్రుడు", "బుధుడు", "చంద్రుడు", "సూర్యుడు", "బుధుడు",
        "శుక్రుడు", "కుజుడు", "గురుడు (బృహస్పతి)", "శని", "శని", "గురుడు (బృహస్పతి)"
    )
    val rulingPlanet = rulersFixed[rashiIndex]
    val lagnaLord = rulersFixed[lagnaIndex]

    val nakshatraLords = listOf(
        "కేతువు", "శుక్రుడు", "సూర్యుడు", "చంద్రుడు", "కుజుడు", "రాహువు", "గురుడు", "శని", "బుధుడు",
        "కేతువు", "శుక్రుడు", "సూర్యుడు", "చంద్రుడు", "కుజుడు", "రాహువు", "గురుడు", "శని", "బుధుడు",
        "కేతువు", "శుక్రుడు", "సూర్యుడు", "చంద్రుడు", "కుజుడు", "రాహువు", "గురుడు", "శని", "బుధుడు"
    )
    val nakshatraLord = nakshatraLords.getOrElse(nakshatraIndex) { "సూర్యుడు" }

    val deities = listOf(
        "సుబ్రమణ్యేశ్వర స్వామి", "శ్రీ మహాలక్ష్మి", "శ్రీమహావిష్ణువు", "పార్వతీ దేవి", "పరమశివుడు",
        "శ్రీ విఘ్నేశ్వరుడు", "శ్రీ మహాలక్ష్మి", "హనుమంతుడు", "దక్షిణామూర్తి", "వెంకటేశ్వర స్వామి",
        "ఆంజనేయ స్వామి", "శ్రీమహావిష్ణువు"
    )
    val luckyColors = listOf("ఎరుపు", "తెలుపు", "ఆకుపచ్చ", "వెండి రంగు / తెలుపు", "బంగారు రంగు / నారింజ", "పసుపు", "గులాబీ రంగు", "ముదురు ఎరుపు", "పీతాంబరం / పసుపు", "నీలం", "నలుపు / నీలం", "పసుపు / బంగారు వర్ణం")
    val luckyGemstones = listOf("పగడం", "వజ్రం", "పచ్చ (మరకతం)", "ముత్యం", "మాణిక్యం (కెంపు)", "పచ్చ (మరకతం)", "వజ్రం", "పగడం", "కనక పుష్యరాగం", "ఇంద్రనీలం", "నీలం", "కనక పుష్యరాగం")
    val luckyNums = listOf(9, 6, 5, 2, 1, 5, 6, 9, 3, 8, 8, 3)

    // Build planet placements for chart
    val placements = mutableMapOf<Int, MutableList<String>>()
    for (i in 0..11) {
        placements[i] = mutableListOf()
    }
    placements[lagnaIndex]?.add("ల")
    placements[(sunSidereal / 30.0).toInt().coerceIn(0, 11)]?.add("రవి")
    placements[rashiIndex]?.add("చం")
    placements[(marsSidereal / 30.0).toInt().coerceIn(0, 11)]?.add("కు")
    placements[(mercurySidereal / 30.0).toInt().coerceIn(0, 11)]?.add("బు")
    placements[(jupiterSidereal / 30.0).toInt().coerceIn(0, 11)]?.add("గు")
    placements[(venusSidereal / 30.0).toInt().coerceIn(0, 11)]?.add("శు")
    placements[(saturnSidereal / 30.0).toInt().coerceIn(0, 11)]?.add("శని")
    placements[(rahuSidereal / 30.0).toInt().coerceIn(0, 11)]?.add("రా")
    placements[(ketuSidereal / 30.0).toInt().coerceIn(0, 11)]?.add("కే")

    val characterTraits = getNakshatraTraits(selectedNakshatra)

    val professionTips = when (rashiIndex) {
        0 -> "మేష రాశి వారికి వ్యాపారం, భద్రత, రక్షణ రంగాలు, లోహ పరిశ్రమలు మరియు ఇంజనీరింగ్ రంగాలలో విజయం లభిస్తుంది."
        1 -> "వృషభ రాశి వారికి లలిత కళలు, డిజైనింగ్, బ్యాంకింగ్, అలంకరణ వస్తువుల వ్యాపారం మరియు వ్యవసాయ రంగాలలో పురోగతి ఉంటుంది."
        2 -> "మిథున రాశి వారికి ఐటి రంగాలు, జర్నలిజం, బోధనా రంగం, అకౌంట్స్ మరియు అనువాద రంగాలలో రాణిస్తారు."
        3 -> "కర్కాటక రాశి వారికి జల సంబంధిత వ్యాపారాలు, నర్సింగ్, మానసిక సలహాదారులు మరియు ఆహార పరిశ్రమలలో మంచి లాభాలు వస్తాయి."
        4 -> "సింహ రాశి వారికి ప్రభుత్వ ఉద్యోగాలు, రాజకీయాలు, నాయకత్వ బాధ్యతలు మరియు మేనేజ్మెంట్ రంగాలలో ఉన్నత స్థాయి లభిస్తుంది."
        5 -> "కన్య రాశి వారికి పరిశోధనలు, గణాంకాలు, వైద్యం, బోధన మరియు విశ్లేషణాత్మక రంగాలు అత్యంత అనుకూలమైనవి."
        6 -> "తులా రాశి వారికి న్యాయ రంగం, ప్రజా సంబంధాలు, వినోద పరిశ్రమ, వస్త్ర వ్యాపారం మరియు ఫ్యాషన్ రంగాలలో విజయం లభిస్తుంది."
        7 -> "వృశ్చిక రాశి వారికి పరిశోధనలు, మైనింగ్, ఆర్మీ, సర్జికల్ వైద్యం మరియు గూఢచర్య రంగాలలో గొప్ప విజయాలు సాధిస్తారు."
        8 -> "ధనుస్సు రాశి వారికి బ్యాంకింగ్, దైవచింతన, ప్రబోధకులు, ఉన్నత విద్య మరియు చట్టపరమైన సలహాదారుల రూపంలో కీర్తి లభిస్తుంది."
        9 -> "మకర రాశి వారికి రియల్ ఎస్టేట్, నిర్మాణ రంగం, పరిశ్రమలు, శ్రమతో కూడిన వృత్తులు మరియు యంత్ర పరికరాల వ్యాపారంలో రాణిస్తారు."
        10 -> "కుంభ రాశి వారికి నూతన సాంకేతిక రంగాలు, శాస్త్రవేత్తలు, విమానయానం మరియు స్వచ్ఛంద సేవా సంస్థలలో మంచి గుర్తింపు లభిస్తుంది."
        11 -> "మీన రాశి వారికి బోధన, సముద్ర పరిశోధనలు, రచనలు, మతపరమైన సంస్థలు మరియు కౌన్సిలింగ్ రంగాలలో ఉన్నతమైన కీర్తి లభిస్తుంది."
        else -> "వ్యాపార కౌశల్యం, నాయకత్వ లక్షణాలతో ఏ రంగంలోనైనా వీరు అద్భుతమైన విజయాన్ని సాధించగలరు."
    }

    return BirthPanchangaResult(
        rashi = selectedRashi,
        rashiEnglish = selectedRashiEnglish,
        rashiSymbol = selectedRashiSymbol,
        nakshatra = selectedNakshatra,
        pada = pada,
        tithi = selectedTithi,
        paksha = paksha,
        rulingPlanet = rulingPlanet,
        deity = deities[rashiIndex],
        luckyNumber = luckyNums[rashiIndex],
        luckyColor = luckyColors[rashiIndex],
        luckyGemstone = luckyGemstones[rashiIndex],
        characterTraits = characterTraits,
        luckyHours = "",
        physicalAppearance = "",
        professionTips = professionTips,
        lagna = lagnaName,
        lagnaIndex = lagnaIndex,
        lagnaLord = lagnaLord,
        nakshatraLord = nakshatraLord,
        yoga = selectedYoga,
        karana = selectedKarana,
        birthPlaceName = "${district.nameTe} (${district.stateTe})",
        planetPlacements = placements
    )
}

@Composable
fun BirthPanchangamTab(
    viewModel: PanchangaViewModel,
    language: AppLanguage
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val bookmarks by viewModel.bookmarks.collectAsState()
    val selectedBirthInput by viewModel.selectedBirthInput.collectAsState()

    var personName by remember { mutableStateOf("") }
    var dobDay by remember { mutableStateOf(15) }
    var dobMonth by remember { mutableStateOf(6) }
    var dobYear by remember { mutableStateOf(1995) }
    var tobHour12 by remember { mutableStateOf(10) }
    var tobMinute by remember { mutableStateOf(30) }
    var tobAmPm by remember { mutableStateOf("AM") }

    val defaultDistrict = remember {
        IndianDistrictsRepository.ALL_DISTRICTS.firstOrNull { it.id == "tg_hyderabad" }
            ?: IndianDistrictsRepository.ALL_DISTRICTS.first()
    }
    var selectedDistrict by remember { mutableStateOf(defaultDistrict) }
    var showDistrictDialog by remember { mutableStateOf(false) }
    var districtSearchQuery by remember { mutableStateOf("") }
    var selectedStateFilter by remember { mutableStateOf("అన్నీ") }

    val isDetectingLocation by viewModel.isDetectingLocation.collectAsState()
    val birthLocationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            viewModel.detectAndApplyCurrentLocation(
                context = context,
                onSuccess = { city ->
                    val dist = IndianDistrictsRepository.ALL_DISTRICTS.find { it.id.equals(city.id, ignoreCase = true) }
                        ?: IndianDistrictsRepository.findNearestDistrict(city.latitude, city.longitude)
                    selectedDistrict = dist
                    showDistrictDialog = false
                    android.widget.Toast.makeText(
                        context,
                        if (language == AppLanguage.TE) "📍 జన్మ స్థలం గుర్తించబడింది: ${dist.nameTe}" else "📍 Birth place detected: ${dist.nameEn}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    var expandedDay by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }
    var expandedHour by remember { mutableStateOf(false) }
    var expandedMin by remember { mutableStateOf(false) }
    var expandedAmPm by remember { mutableStateOf(false) }

    var result by remember { mutableStateOf<BirthPanchangaResult?>(null) }

    val monthNames = if (language == AppLanguage.TE) {
        listOf("జనవరి", "ఫిబ్రవరి", "మార్చి", "ఏప్రిల్", "మే", "జూన్", "జూలై", "ఆగస్టు", "సెప్టెంబరు", "అక్టోబరు", "నవంబరు", "డిసెంబరు")
    } else {
        listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    }

    // Auto-calculate on initial load or when a saved bookmark is loaded
    LaunchedEffect(selectedBirthInput?.autoCalculateToken) {
        val input = selectedBirthInput
        if (input != null) {
            dobDay = input.day
            dobMonth = input.month
            dobYear = input.year
            personName = input.personName

            val h24 = input.hour
            tobMinute = input.minute
            if (h24 == 0) {
                tobHour12 = 12
                tobAmPm = "AM"
            } else if (h24 < 12) {
                tobHour12 = h24
                tobAmPm = "AM"
            } else if (h24 == 12) {
                tobHour12 = 12
                tobAmPm = "PM"
            } else {
                tobHour12 = h24 - 12
                tobAmPm = "PM"
            }

            val matchedDist = IndianDistrictsRepository.ALL_DISTRICTS.find { it.id.equals(input.districtId, ignoreCase = true) }
                ?: IndianDistrictsRepository.ALL_DISTRICTS.find { it.nameEn.equals(input.districtId, ignoreCase = true) }
            if (matchedDist != null) {
                selectedDistrict = matchedDist
            }

            val calculated24Hour = when {
                tobAmPm == "PM" && tobHour12 < 12 -> tobHour12 + 12
                tobAmPm == "AM" && tobHour12 == 12 -> 0
                else -> tobHour12
            }

            result = calculateBirthPanchanga(dobDay, dobMonth, dobYear, calculated24Hour, tobMinute, selectedDistrict, language)
        } else if (result == null) {
            val calculated24Hour = when {
                tobAmPm == "PM" && tobHour12 < 12 -> tobHour12 + 12
                tobAmPm == "AM" && tobHour12 == 12 -> 0
                else -> tobHour12
            }
            result = calculateBirthPanchanga(dobDay, dobMonth, dobYear, calculated24Hour, tobMinute, selectedDistrict, language)
        }
    }

    // District Selector Dialog
    if (showDistrictDialog) {
        AlertDialog(
            onDismissRequest = { showDistrictDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.TE) "జన్మ స్థలం ఎంచుకోండి 📍" else "Select Birth Place 📍",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    )
                    IconButton(
                        onClick = { showDistrictDialog = false },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            if (com.example.engine.LocationHelper.hasLocationPermission(context)) {
                                viewModel.detectAndApplyCurrentLocation(
                                    context = context,
                                    onSuccess = { city ->
                                        val dist = IndianDistrictsRepository.ALL_DISTRICTS.find { it.id.equals(city.id, ignoreCase = true) }
                                            ?: IndianDistrictsRepository.findNearestDistrict(city.latitude, city.longitude)
                                        selectedDistrict = dist
                                        showDistrictDialog = false
                                        android.widget.Toast.makeText(
                                            context,
                                            if (language == AppLanguage.TE) "📍 జన్మ స్థలం గుర్తించబడింది: ${dist.nameTe}" else "📍 Birth place detected: ${dist.nameEn}",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            } else {
                                birthLocationPermissionLauncher.launch(
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
                            .padding(bottom = 8.dp)
                            .testTag("btn_auto_detect_birth_location"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isDetectingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.TE) "స్థానాన్ని గుర్తిస్తోంది..." else "Detecting location...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = if (language == AppLanguage.TE) "ప్రస్తుత ప్రదేశాన్ని గుర్తించండి (GPS) 🎯" else "Auto-Detect Current Location (GPS) 🎯",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    OutlinedTextField(
                        value = districtSearchQuery,
                        onValueChange = { districtSearchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                if (language == AppLanguage.TE) "జిల్లా / నగరం వెతకండి..." else "Search district / city...",
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        trailingIcon = {
                            if (districtSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { districtSearchQuery = "" }) {
                                    Text("✕", fontSize = 14.sp)
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val stateFilters = listOf("అన్నీ", "తెలంగాణ", "ఆంధ్రప్రదేశ్", "కర్ణాటక", "తమిళనాడు", "మహారాష్ట్ర", "ఇతర రాష్ట్రాలు")
                        stateFilters.forEach { filter ->
                            val isSelected = selectedStateFilter == filter
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedStateFilter = filter },
                                label = { Text(filter, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val filteredList = remember(districtSearchQuery, selectedStateFilter) {
                        var list = if (districtSearchQuery.isBlank()) {
                            IndianDistrictsRepository.ALL_DISTRICTS
                        } else {
                            IndianDistrictsRepository.searchDistricts(districtSearchQuery)
                        }
                        if (selectedStateFilter != "అన్నీ") {
                            list = when (selectedStateFilter) {
                                "తెలంగాణ" -> list.filter { it.stateEn == "Telangana" }
                                "ఆంధ్రప్రదేశ్" -> list.filter { it.stateEn == "Andhra Pradesh" }
                                "కర్ణాటక" -> list.filter { it.stateEn == "Karnataka" }
                                "తమిళనాడు" -> list.filter { it.stateEn == "Tamil Nadu" }
                                "మహారాష్ట్ర" -> list.filter { it.stateEn == "Maharashtra" }
                                "ఇతర రాష్ట్రాలు" -> list.filter { it.stateEn !in listOf("Telangana", "Andhra Pradesh", "Karnataka", "Tamil Nadu", "Maharashtra") }
                                else -> list
                            }
                        }
                        list
                    }

                    Text(
                        text = "${filteredList.size} జిల్లాలు / నగరాలు లభ్యమవుతున్నాయి",
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        items(filteredList, key = { it.id }) { dist ->
                            val isCurrent = dist.id == selectedDistrict.id
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedDistrict = dist
                                        showDistrictDialog = false
                                    },
                                color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = dist.nameTe,
                                            fontSize = 13.sp,
                                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${dist.nameEn} • ${dist.stateTe}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (isCurrent) {
                                        Text("✓", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDistrictDialog = false }) {
                    Text(if (language == AppLanguage.TE) "ముగించు" else "Close", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.TE) "🕉️ మీ జన్మ వివరాలను నమోదు చేయండి" else "🕉️ Enter Your Birth Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = if (language == AppLanguage.TE) "ఖచ్చితమైన లగ్నం, నక్షత్ర పాదం, రాశి మరియు గ్రహ స్థితులను కనుగొనండి" else "Accurately calculate Lagna, Nakshatra Pada, Rashi & Graha Kundali",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Optional Name Field
                    OutlinedTextField(
                        value = personName,
                        onValueChange = { personName = it },
                        label = { Text(if (language == AppLanguage.TE) "పేరు (ఐచ్ఛికం - సేవ్ & షేర్ కొరకు)" else "Name (Optional - for Save & Share)", fontSize = 12.sp) },
                        placeholder = { Text(if (language == AppLanguage.TE) "ఉదా: కిరణ్ / రమేష్" else "e.g. Kiran / Ramesh", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_birth_person_name")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Date Selection Row
                    Text(text = if (language == AppLanguage.TE) "జన్మ తేదీ:" else "Date of Birth:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Day
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandedDay = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                Text(text = "$dobDay", fontSize = 13.sp)
                            }
                            DropdownMenu(expanded = expandedDay, onDismissRequest = { expandedDay = false }) {
                                (1..31).forEach { d ->
                                    DropdownMenuItem(text = { Text("$d") }, onClick = { dobDay = d; expandedDay = false })
                                }
                            }
                        }

                        // Month
                        Box(modifier = Modifier.weight(1.8f)) {
                            OutlinedButton(
                                onClick = { expandedMonth = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                Text(text = monthNames[dobMonth - 1], fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            DropdownMenu(expanded = expandedMonth, onDismissRequest = { expandedMonth = false }) {
                                monthNames.forEachIndexed { idx, mName ->
                                    DropdownMenuItem(text = { Text(mName) }, onClick = { dobMonth = idx + 1; expandedMonth = false })
                                }
                            }
                        }

                        // Year
                        Box(modifier = Modifier.weight(1.3f)) {
                            OutlinedButton(
                                onClick = { expandedYear = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                Text(text = "$dobYear", fontSize = 13.sp)
                            }
                            DropdownMenu(expanded = expandedYear, onDismissRequest = { expandedYear = false }) {
                                (1940..2026).reversed().forEach { y ->
                                    DropdownMenuItem(text = { Text("$y") }, onClick = { dobYear = y; expandedYear = false })
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Time Selection Row
                    Text(text = if (language == AppLanguage.TE) "జన్మ సమయం (12 గంటల విధానం - AM/PM):" else "Time of Birth (12h - AM/PM):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Hour (1..12)
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandedHour = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                Text(text = String.format("%02d గం", tobHour12), fontSize = 12.sp)
                            }
                            DropdownMenu(expanded = expandedHour, onDismissRequest = { expandedHour = false }) {
                                (1..12).forEach { h ->
                                    DropdownMenuItem(text = { Text(String.format("%02d", h)) }, onClick = { tobHour12 = h; expandedHour = false })
                                }
                            }
                        }

                        // Minute (0..59)
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandedMin = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                Text(text = String.format("%02d ని", tobMinute), fontSize = 12.sp)
                            }
                            DropdownMenu(expanded = expandedMin, onDismissRequest = { expandedMin = false }) {
                                (0..59).forEach { m ->
                                    DropdownMenuItem(text = { Text(String.format("%02d", m)) }, onClick = { tobMinute = m; expandedMin = false })
                                }
                            }
                        }

                        // AM / PM
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandedAmPm = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                Text(text = tobAmPm, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            DropdownMenu(expanded = expandedAmPm, onDismissRequest = { expandedAmPm = false }) {
                                listOf("AM", "PM").forEach { period ->
                                    DropdownMenuItem(
                                        text = { Text(period, fontWeight = FontWeight.Bold) },
                                        onClick = { tobAmPm = period; expandedAmPm = false }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // District Selection Row
                    Text(text = if (language == AppLanguage.TE) "జన్మ స్థలం (భారతదేశ జిల్లాలు):" else "Place of Birth (India Districts):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .clickable { showDistrictDialog = true }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "📍 ${selectedDistrict.nameTe} (${selectedDistrict.stateTe})",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${selectedDistrict.nameEn} • ${selectedDistrict.stateEn}",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = if (language == AppLanguage.TE) "మార్చండి ▾" else "Change ▾",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val calculated24Hour = when {
                                tobAmPm == "PM" && tobHour12 < 12 -> tobHour12 + 12
                                tobAmPm == "AM" && tobHour12 == 12 -> 0
                                else -> tobHour12
                            }
                            result = calculateBirthPanchanga(dobDay, dobMonth, dobYear, calculated24Hour, tobMinute, selectedDistrict, language)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.TE) "జన్మ పంచాంగం కనుగొనండి 🕉️" else "Calculate Birth Panchangam 🕉️",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (result != null) {
            val res = result!!
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val calculated24Hour = when {
                            tobAmPm == "PM" && tobHour12 < 12 -> tobHour12 + 12
                            tobAmPm == "AM" && tobHour12 == 12 -> 0
                            else -> tobHour12
                        }
                        val birthBookmarkId = "janma_${dobYear}_${dobMonth}_${dobDay}_${calculated24Hour}_${tobMinute}_${selectedDistrict.id}"
                        val isSaved = viewModel.isIdBookmarked(birthBookmarkId)
                        val formatted12hTime = String.format("%02d:%02d %s", tobHour12, tobMinute, tobAmPm)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (personName.isNotBlank()) "📊 $personName గారి జన్మ పంచాంగం" else if (language == AppLanguage.TE) "📊 మీ వ్యక్తిగత జన్మ పంచాంగం" else "📊 Your Personal Birth Panchangam",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    text = "${String.format("%02d-%02d-%04d", dobDay, dobMonth, dobYear)} • $formatted12hTime • ${selectedDistrict.nameTe}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                // Save / Bookmark Button
                                IconButton(
                                    onClick = {
                                        val title = if (personName.isNotBlank()) "$personName - జన్మ పంచాంగం" else "జన్మ పంచాంగం (${String.format("%02d-%02d-%04d", dobDay, dobMonth, dobYear)})"
                                        val subtitle = "${res.rashi} రాశి • ${res.nakshatra} (${res.pada}వ పాదం) • ${selectedDistrict.nameTe}"
                                        viewModel.toggleBookmarkJanmaPanchanga(
                                            context = context,
                                            id = birthBookmarkId,
                                            title = title,
                                            subtitle = subtitle,
                                            rawDate = "${dobYear}-${String.format("%02d", dobMonth)}-${String.format("%02d", dobDay)}|${calculated24Hour}:${tobMinute}|${selectedDistrict.id}|${personName}"
                                        )
                                    },
                                    modifier = Modifier.testTag("btn_save_janma_panchangam")
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = if (isSaved) "సేవ్ చేయబడింది" else "సేవ్ చేయండి",
                                        tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                // Share Button
                                IconButton(
                                    onClick = {
                                        val headerName = if (personName.isNotBlank()) "$personName గారి " else ""
                                        val shareText = "🔮 *శ్రీ వేద తెలుగు జన్మ పంచాంగం (${headerName}వివరాలు)* 🔮\n\n" +
                                            "🚩 జన్మ తేదీ: ${String.format("%02d-%02d-%04d", dobDay, dobMonth, dobYear)}\n" +
                                            "⏰ జన్మ సమయం: $formatted12hTime\n" +
                                            "📍 జన్మ స్థలం: ${res.birthPlaceName}\n\n" +
                                            "✨ జన్మ రాశి: ${res.rashiSymbol} ${res.rashi} (${res.rashiEnglish})\n" +
                                            "✨ జన్మ నక్షత్రం: ${res.nakshatra} (${res.pada}వ పాదం)\n" +
                                            "✨ జన్మ లగ్నం: ${res.lagna}\n" +
                                            "✨ జన్మ తిథి: ${res.paksha} ${res.tithi}\n" +
                                            "✨ జన్మ యోగం: ${res.yoga}\n" +
                                            "✨ జన్మ కరణం: ${res.karana}\n" +
                                            "🪐 రాశ్యాధిపతి: ${res.rulingPlanet}\n" +
                                            "🪐 లగ్నాధిపతి: ${res.lagnaLord}\n" +
                                            "🪐 నక్షత్రాధిపతి: ${res.nakshatraLord}\n" +
                                            "🙏 ఆరాధ్య దైవం: ${res.deity}\n\n" +
                                            "🌟 *అదృష్ట అంశాలు:*\n" +
                                            "• అదృష్ట సంఖ్య: ${res.luckyNumber}\n" +
                                            "• అదృష్ట రంగు: ${res.luckyColor}\n" +
                                            "• అదృష్ట రత్నం: ${res.luckyGemstone}\n\n" +
                                            "💫 *నక్షత్ర స్వభావం:* ${res.characterTraits}\n\n" +
                                            "💼 *వృత్తి/వ్యాపారం:* ${res.professionTips}\n\n" +
                                            "— శ్రీ వేద తెలుగు పంచాంగం ద్వారా షేర్ చేయబడింది"

                                        val sendIntent = android.content.Intent().apply {
                                            action = android.content.Intent.ACTION_SEND
                                            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                            type = "text/plain"
                                        }
                                        context.startActivity(android.content.Intent.createChooser(sendIntent, "జన్మ పంచాంగం షేర్ చేయండి"))
                                    },
                                    modifier = Modifier.testTag("btn_share_janma_panchangam")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "జన్మ పంచాంగం షేర్ చేయండి",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            TableCellRow(label = if (language == AppLanguage.TE) "జన్మ స్థలం" else "Birth Place", value = res.birthPlaceName, isEven = true)
                            TableCellRow(label = if (language == AppLanguage.TE) "జన్మ నక్షత్రం" else "Birth Star", value = "${res.nakshatra} (${res.pada}వ పాదం)", isEven = false)
                            TableCellRow(label = if (language == AppLanguage.TE) "జన్మ రాశి" else "Moon Sign (Rashi)", value = "${res.rashiSymbol} ${res.rashi} (${res.rashiEnglish})", isEven = true)
                            TableCellRow(label = if (language == AppLanguage.TE) "జన్మ లగ్నం" else "Birth Lagna", value = res.lagna, isEven = false)
                            TableCellRow(label = if (language == AppLanguage.TE) "జన్మ తిథి" else "Birth Tithi", value = "${res.paksha} ${res.tithi}", isEven = true)
                            TableCellRow(label = if (language == AppLanguage.TE) "జన్మ యోగం" else "Birth Yoga", value = res.yoga, isEven = false)
                            TableCellRow(label = if (language == AppLanguage.TE) "జన్మ కరణం" else "Birth Karana", value = res.karana, isEven = true)
                            TableCellRow(label = if (language == AppLanguage.TE) "రాశ్యాధిపతి" else "Rashi Lord", value = res.rulingPlanet, isEven = false)
                            TableCellRow(label = if (language == AppLanguage.TE) "లగ్నాధిపతి" else "Lagna Lord", value = res.lagnaLord, isEven = true)
                            TableCellRow(label = if (language == AppLanguage.TE) "నక్షత్రాధిపతి" else "Star Lord", value = res.nakshatraLord, isEven = false)
                            TableCellRow(label = if (language == AppLanguage.TE) "ఆరాధ్య దైవం" else "Ruling Deity", value = res.deity, isEven = true)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.TE) "✨ మీ నక్షత్ర స్వభావం & అదృష్టాలు" else "✨ Star Qualities & Lucky Attributes",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LuckyBadge(label = if (language == AppLanguage.TE) "అదృష్ట సంఖ్య" else "Lucky Num", value = "${res.luckyNumber}", modifier = Modifier.weight(1f))
                            LuckyBadge(label = if (language == AppLanguage.TE) "అదృష్ట రంగు" else "Lucky Color", value = res.luckyColor, modifier = Modifier.weight(1.3f))
                            LuckyBadge(label = if (language == AppLanguage.TE) "అదృష్ట రత్నం" else "Lucky Gem", value = res.luckyGemstone, modifier = Modifier.weight(1.5f))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = if (language == AppLanguage.TE) "నక్షత్ర గుణాలు & వ్యక్తిత్వం:" else "Star Character & Personality:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = res.characterTraits, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 16.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = if (language == AppLanguage.TE) "వృత్తి & వ్యాపార సూచనలు:" else "Career & Business Advice:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = res.professionTips, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 16.sp)
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (language == AppLanguage.TE) "🕉️ మీ జన్మ జాతక చక్రం (కుండలి)" else "🕉️ Your Janma Kundali (Birth Chart)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = if (language == AppLanguage.TE) "దక్షిణాది రాశి చక్ర పద్ధతి (గ్రహ స్థితులు)" else "South Indian Style (Planetary Positions)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        BirthChartGrid(
                            rashiIndex = getRashiIndex(res.rashi),
                            lagnaIndex = res.lagnaIndex,
                            planetPlacements = res.planetPlacements,
                            language = language
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ల = లగ్నం · రవి = సూర్యుడు · చం = చంద్రుడు · కు = కుజుడు · బు = బుధుడు · గు = గురుడు · శు = శుక్రుడు · శని = శని · రా = రాహువు · కే = కేతువు",
                            fontSize = 9.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LuckyBadge(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), RoundedCornerShape(8.dp)),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 9.sp, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun BirthChartGrid(
    rashiIndex: Int,
    lagnaIndex: Int,
    planetPlacements: Map<Int, List<String>>,
    language: AppLanguage
) {
    val rashiNames = listOf(
        "మేషం", "వృషభం", "మిథునం", "కర్కాటకం", "సింహం", "కన్య", "తులా", "వృశ్చికం", "ధనుస్సు", "మకరం", "కుంభం", "మీనం"
    )
    val rashiSymbols = listOf("♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(2.dp, DeepGold, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Row 0 (Top) - Meena (11), Mesha (0), Vrishabha (1), Mithuna (2)
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            ChartCell(index = 11, name = rashiNames[11], symbol = rashiSymbols[11], planets = planetPlacements[11] ?: emptyList(), modifier = Modifier.weight(1f))
            ChartCell(index = 0, name = rashiNames[0], symbol = rashiSymbols[0], planets = planetPlacements[0] ?: emptyList(), modifier = Modifier.weight(1f))
            ChartCell(index = 1, name = rashiNames[1], symbol = rashiSymbols[1], planets = planetPlacements[1] ?: emptyList(), modifier = Modifier.weight(1f))
            ChartCell(index = 2, name = rashiNames[2], symbol = rashiSymbols[2], planets = planetPlacements[2] ?: emptyList(), modifier = Modifier.weight(1f))
        }

        // Row 1 - Kumbha (10), Middle, Karkataka (3)
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            ChartCell(index = 10, name = rashiNames[10], symbol = rashiSymbols[10], planets = planetPlacements[10] ?: emptyList(), modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))
                    .border(0.5.dp, DeepGold.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🕉️", fontSize = 28.sp, color = DeepGold)
            }
            ChartCell(index = 3, name = rashiNames[3], symbol = rashiSymbols[3], planets = planetPlacements[3] ?: emptyList(), modifier = Modifier.weight(1f))
        }

        // Row 2 - Makara (9), Middle, Simha (4)
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            ChartCell(index = 9, name = rashiNames[9], symbol = rashiSymbols[9], planets = planetPlacements[9] ?: emptyList(), modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))
                    .border(0.5.dp, DeepGold.copy(alpha = 0.5f))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (language == AppLanguage.TE) "జన్మ కుండలి (రాశి చక్రం)" else "Janma Kundali",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (language == AppLanguage.TE) "దక్షిణాది పద్ధతి" else "South Indian Style",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            ChartCell(index = 4, name = rashiNames[4], symbol = rashiSymbols[4], planets = planetPlacements[4] ?: emptyList(), modifier = Modifier.weight(1f))
        }

        // Row 3 (Bottom) - Left-to-Right: Dhanus (8), Makara is on left col so: Dhanus (8) is bottom-left, then Vrishchika (7), Tula (6), Kanya (5) on bottom-right
        // Wait, clockwise flow around perimeter:
        // Top row L->R: Meena (11) -> Mesha (0) -> Vrishabha (1) -> Mithuna (2)
        // Right col Top->Bottom: Mithuna (2) -> Karkataka (3) -> Simha (4) -> Kanya (5)
        // Bottom row Right->Left: Kanya (5) -> Tula (6) -> Vrishchika (7) -> Dhanus (8)
        // Left col Bottom->Top: Dhanus (8) -> Makara (9) -> Kumbha (10) -> Meena (11)
        // Therefore, when rendering Row 3 (Bottom) from Left-to-Right on screen:
        // Leftmost cell is Dhanus (8), next is Vrishchika (7), next is Tula (6), rightmost cell is Kanya (5).
        // Let's verify: In South Indian chart:
        // Row 0: Meena (11) | Mesha (0) | Vrishabha (1) | Mithuna (2)
        // Row 1: Kumbha (10) |        Center        | Karkataka (3)
        // Row 2: Makara (9)  |        Center        | Simha (4)
        // Row 3: Dhanus (8)  | Vrishchika (7) | Tula (6) | Kanya (5)
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            ChartCell(index = 8, name = rashiNames[8], symbol = rashiSymbols[8], planets = planetPlacements[8] ?: emptyList(), modifier = Modifier.weight(1f))
            ChartCell(index = 7, name = rashiNames[7], symbol = rashiSymbols[7], planets = planetPlacements[7] ?: emptyList(), modifier = Modifier.weight(1f))
            ChartCell(index = 6, name = rashiNames[6], symbol = rashiSymbols[6], planets = planetPlacements[6] ?: emptyList(), modifier = Modifier.weight(1f))
            ChartCell(index = 5, name = rashiNames[5], symbol = rashiSymbols[5], planets = planetPlacements[5] ?: emptyList(), modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChartCell(
    index: Int,
    name: String,
    symbol: String,
    planets: List<String>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(0.5.dp, DeepGold.copy(alpha = 0.5f))
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$symbol $name",
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            if (planets.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    maxItemsInEachRow = 2,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    planets.forEach { p ->
                        val badgeColor = when (p) {
                            "ల" -> MaterialTheme.colorScheme.primary
                            "రవి" -> Color(0xFFD97706)
                            "చం" -> AuspiciousGreen
                            "కు" -> Color(0xFFDC2626)
                            "బు" -> Color(0xFF0D9488)
                            "గు" -> DeepGold
                            "శు" -> Color(0xFFDB2777)
                            "శని" -> Color(0xFF4338CA)
                            "రా" -> Color(0xFF7C3AED)
                            "కే" -> Color(0xFF78716C)
                            else -> MaterialTheme.colorScheme.secondary
                        }
                        Surface(
                            color = badgeColor,
                            shape = RoundedCornerShape(3.dp),
                            modifier = Modifier.padding(1.dp)
                        ) {
                            Text(
                                text = p,
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 3.dp, vertical = 0.5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DevotionalLibraryTab(
    viewModel: PanchangaViewModel,
    isAudioPlaying: Boolean,
    language: AppLanguage,
    onSelectItem: (SpiritualItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.TE) "భక్తి & స్తోత్ర మాలిక" else "SPIRITUAL & DEVOTIONAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp
            )

            Button(
                onClick = { viewModel.toggleDevotionalAudio() },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = if (isAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = "గంట నాదం",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (isAudioPlaying) "ఆపండి ⏹️" else "గంట నాదం 🔔", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(DevotionalRepository.SPIRITUAL_ITEMS) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectItem(item) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = item.category,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${LocalizationEngine.get("deity", language)}: ${LocalizationEngine.translateDeity(item.deity, language)}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = item.verses.firstOrNull { it.isNotBlank() } ?: "",
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(text = "${LocalizationEngine.get("view_details", language)} ›", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun FamilyCalendarTab(
    events: List<FamilyEvent>,
    language: AppLanguage,
    onAddEventClick: () -> Unit,
    onDeleteEvent: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (language) {
                        AppLanguage.TE -> "కుటుంబ వ్రత క్యాలెండర్"
                        AppLanguage.HI -> "पारिवारिक धार्मिक कैलेंडर"
                        AppLanguage.TA -> "குடும்ப விரத நாள்காட்டி"
                        else -> "FAMILY RELIGIOUS CALENDAR"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.5.sp
                )
            }

            Button(
                onClick = onAddEventClick,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    when (language) {
                        AppLanguage.TE -> "జోడించండి"
                        AppLanguage.HI -> "जोड़ें"
                        AppLanguage.TA -> "சேர்க்க"
                        else -> "Add Event"
                    },
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (language == AppLanguage.TE) "కుటుంబ వ్రతాలు ఏవీ నమోదు కాలేదు. పైన ఉన్న 'జోడించండి' బటన్‌పై నొక్కండి." else "No family events added yet.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(events) { ev ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ev.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (language == AppLanguage.TE) "సభ్యులు: ${ev.personName} · ${ev.eventType}" else "Member: ${ev.personName} · ${ev.eventType}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (ev.tithiNotes.isNotBlank()) {
                                    Text(
                                        text = "${LocalizationEngine.get("tithi", language)}: ${ev.tithiNotes}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            IconButton(onClick = { onDeleteEvent(ev.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "తొలగించండి",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
