package com.example.engine

import androidx.compose.ui.graphics.Color
import com.example.model.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

data class DaySummaryItem(
    val date: LocalDate,
    val dayOfMonth: Int,
    val dayOfWeek: DayOfWeek,
    val tithi: TithiInfo,
    val nakshatra: NakshatraInfo,
    val yoga: YogaInfo,
    val karana: KaranaInfo,
    val festivals: List<FestivalItem>,
    val isAmavasya: Boolean,
    val isPournami: Boolean,
    val isEkadashi: Boolean,
    val isPradosham: Boolean,
    val isChaturthi: Boolean,
    val isSashti: Boolean,
    val isMasaShivaratri: Boolean,
    val isSankranti: Boolean,
    val isAshtami: Boolean,
    val isNavami: Boolean
)

data class MonthEventOccurrence(
    val date: LocalDate,
    val dayOfMonth: Int,
    val weekdayTelugu: String,
    val title: String,
    val eventCategory: String,
    val color: Color,
    val iconEmoji: String,
    val significance: String,
    val paksha: String
)

data class MonthPanchangaData(
    val yearMonth: YearMonth,
    val teluguMonthHeader: String,
    val days: List<DaySummaryItem>,
    val fastingRows: List<Triple<String, String, Color>>,
    val otherRows: List<Pair<String, String>>,
    val fastingOccurrences: List<MonthEventOccurrence>,
    val otherOccurrences: List<MonthEventOccurrence>,
    val festivals: List<FestivalItem>
)

object MonthDataManager {

    private val monthPanchangaCache = java.util.concurrent.ConcurrentHashMap<String, MonthPanchangaData>()
    private val yearlyEventsCache = java.util.concurrent.ConcurrentHashMap<String, List<YearlyEventItem>>()

    /**
     * Calculates complete dynamic month Panchangam for any YearMonth, Location, and Tradition.
     * Guaranteed 100% dynamic: no hardcoded dates or stale data.
     */
    fun getMonthPanchanga(
        yearMonth: YearMonth,
        location: CityLocation,
        tradition: CalendarTradition,
        language: AppLanguage = AppLanguage.TE
    ): MonthPanchangaData {
        val cacheKey = "${yearMonth}_${location.id}_${tradition.name}_${language.name}"
        monthPanchangaCache[cacheKey]?.let { return it }

        val daysInMonth = yearMonth.lengthOfMonth()
        val daysList = mutableListOf<DaySummaryItem>()

        val amavasyaDays = mutableListOf<LocalDate>()
        val pournamiDays = mutableListOf<LocalDate>()
        val ekadashiDays = mutableListOf<LocalDate>()
        val pradoshamDays = mutableListOf<LocalDate>()
        val sashtiDays = mutableListOf<LocalDate>()
        val chaturthiDays = mutableListOf<LocalDate>()
        val shivaratriDays = mutableListOf<LocalDate>()
        val sankashtiDays = mutableListOf<LocalDate>()
        val ashtamiDays = mutableListOf<LocalDate>()
        val navamiDays = mutableListOf<LocalDate>()

        val fastingOccurrences = mutableListOf<MonthEventOccurrence>()
        val otherOccurrences = mutableListOf<MonthEventOccurrence>()

        for (d in 1..daysInMonth) {
            val date = yearMonth.atDay(d)
            val panch = AstronomicalEngine.calculatePanchanga(date, location, tradition)
            val dayFestivals = FestivalRepository.getFestivalsForDate(date)

            val tithiNum = panch.tithi.number
            val tithiName = panch.tithi.name
            val paksha = panch.paksha
            val isShukla = paksha.equals("Shukla", ignoreCase = true)
            val pakshaStr = if (isShukla) "శుక్ల పక్షం" else "కృష్ణ పక్షం"
            val weekdayStr = LocalizationEngine.translateVara(date.dayOfWeek, language)

            val isAmav = tithiNum == 30 || tithiName.contains("Amavasya", ignoreCase = true)
            val isPour = tithiNum == 15 || tithiName.contains("Purnima", ignoreCase = true) || tithiName.contains("Pournami", ignoreCase = true)
            val isEka = tithiNum == 11 || tithiNum == 26 || tithiName.contains("Ekadashi", ignoreCase = true)
            val isPrad = tithiNum == 13 || tithiNum == 28 || tithiName.contains("Trayodashi", ignoreCase = true)
            val isSash = tithiNum == 6 || tithiNum == 21 || tithiName.contains("Shashti", ignoreCase = true) || tithiName.contains("Sashti", ignoreCase = true)
            val isChat = tithiNum == 4 || tithiNum == 19 || tithiName.contains("Chaturthi", ignoreCase = true)
            val isShiva = (!isShukla && (tithiNum == 29 || tithiNum % 15 == 14)) || tithiName.contains("Krishna Chaturdashi", ignoreCase = true)
            val isSankashti = (!isShukla && (tithiNum == 19 || tithiNum % 15 == 4))
            val isAsh = tithiNum == 8 || tithiNum == 23 || tithiName.contains("Ashtami", ignoreCase = true)
            val isNav = tithiNum == 9 || tithiNum == 24 || tithiName.contains("Navami", ignoreCase = true)
            val isSank = dayFestivals.any { it.name.contains("Sankranti", ignoreCase = true) }

            if (isAmav) {
                amavasyaDays.add(date)
                fastingOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = "${LocalizationEngine.translateMasa(panch.hinduMasa, language)} అమావాస్య",
                        eventCategory = "అమావాస్య",
                        color = Color(0xFF424242),
                        iconEmoji = "🌑",
                        significance = "పితృ తర్పణం & పూర్వీకుల ఆరాధన శుభ దినం",
                        paksha = pakshaStr
                    )
                )
            }
            if (isPour) {
                pournamiDays.add(date)
                fastingOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = "${LocalizationEngine.translateMasa(panch.hinduMasa, language)} పౌర్ణమి",
                        eventCategory = "పౌర్ణమి",
                        color = Color(0xFFF57F17),
                        iconEmoji = "🌕",
                        significance = "శ్రీ సత్యనారాయణ స్వామి వ్రతం & చంద్ర పూజ",
                        paksha = pakshaStr
                    )
                )
            }
            if (isEka) {
                ekadashiDays.add(date)
                val ekaName = if (isShukla) "శుక్ల ఏకాదశి" else "కృష్ణ ఏకాదశి"
                fastingOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = "$ekaName (ఉపవాస దినం)",
                        eventCategory = "ఏకాదశి",
                        color = Color(0xFF2E7D32),
                        iconEmoji = "🙏",
                        significance = "శ్రీ మహావిష్ణువు పూజ & హరివాసర ఉపవాసం",
                        paksha = pakshaStr
                    )
                )
            }
            if (isPrad) {
                pradoshamDays.add(date)
                val pradName = if (isShukla) "శుక్ల ప్రదోషం" else "కృష్ణ ప్రదోషం"
                fastingOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = "$pradName (సాయంకాల పూజ)",
                        eventCategory = "ప్రదోషం",
                        color = Color(0xFFC2185B),
                        iconEmoji = "🐂",
                        significance = "పరమశివుడు & పార్వతీదేవి ప్రదోషకాల ఆరాధన",
                        paksha = pakshaStr
                    )
                )
            }
            if (isSash) {
                sashtiDays.add(date)
                val sashName = if (isShukla) "శుక్ల షష్ఠి" else "కృష్ణ షష్ఠి"
                fastingOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = "$sashName (స్కంద ఆరాధన)",
                        eventCategory = "షష్ఠి",
                        color = Color(0xFF7B1FA2),
                        iconEmoji = "🦚",
                        significance = "శ్రీ సుబ్రహ్మణ్యేశ్వర స్వామి పూజ",
                        paksha = pakshaStr
                    )
                )
            }
            if (isChat) {
                chaturthiDays.add(date)
                if (isShukla) {
                    fastingOccurrences.add(
                        MonthEventOccurrence(
                            date = date,
                            dayOfMonth = d,
                            weekdayTelugu = weekdayStr,
                            title = "వినాయక చవితి (శుక్ల చతుర్థి)",
                            eventCategory = "చవితి",
                            color = Color(0xFFE65100),
                            iconEmoji = "🐘",
                            significance = "శ్రీ మహాగణపతి పూజ",
                            paksha = pakshaStr
                        )
                    )
                }
            }
            if (isShiva) {
                shivaratriDays.add(date)
                fastingOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = "మాస శివరాత్రి (లింగోద్భవం)",
                        eventCategory = "మాస శివరాత్రి",
                        color = Color(0xFF00838F),
                        iconEmoji = "🔱",
                        significance = "ఈశ్వరార్చన, అభిషేకం & రాత్రి జాగరణ",
                        paksha = pakshaStr
                    )
                )
            }
            if (isSankashti) {
                sankashtiDays.add(date)
                fastingOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = "సంకటహర చతుర్థి (కృష్ణ చవితి)",
                        eventCategory = "సంకటహర చతుర్థి",
                        color = Color(0xFFD84315),
                        iconEmoji = "🐘",
                        significance = "సంకట నివారణ గణపతి పూజ & చంద్ర దర్శనం",
                        paksha = pakshaStr
                    )
                )
            }
            if (isAsh) {
                ashtamiDays.add(date)
                val ashName = if (isShukla) {
                    if (panch.hinduMasa.contains("Ashwina", ignoreCase = true)) "దుర్గాష్టమి (మహాష్టమి)"
                    else "శుక్ల అష్టమి (దుర్గాష్టమి)"
                } else {
                    if (panch.hinduMasa.contains("Shravana", ignoreCase = true)) "శ్రీ కృష్ణాష్టమి (గోకులాష్టమి)"
                    else "కృష్ణ అష్టమి (కాలాష్టమి)"
                }
                otherOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = ashName,
                        eventCategory = "అష్టమి",
                        color = Color(0xFFD32F2F),
                        iconEmoji = "🌺",
                        significance = if (isShukla) "దుర్గాదేవి ఆరాధన & నవరాత్రి పూజ" else "కాలభైరవ పూజ / శ్రీకృష్ణ జన్మోత్సవం",
                        paksha = pakshaStr
                    )
                )
            }
            if (isNav) {
                navamiDays.add(date)
                val navName = if (isShukla) {
                    if (panch.hinduMasa.contains("Chaitra", ignoreCase = true)) "శ్రీరామనవమి (శుక్ల నవమి)"
                    else if (panch.hinduMasa.contains("Ashwina", ignoreCase = true)) "మహార్నవమి (శుక్ల నవమి)"
                    else "శుక్ల నవమి"
                } else "కృష్ణ నవమి"
                otherOccurrences.add(
                    MonthEventOccurrence(
                        date = date,
                        dayOfMonth = d,
                        weekdayTelugu = weekdayStr,
                        title = navName,
                        eventCategory = "నవమి",
                        color = Color(0xFF1976D2),
                        iconEmoji = "🏹",
                        significance = if (isShukla) "శ్రీ సీతారాముల పూజ, కళ్యాణోత్సవం & దేవి ఆరాధన" else "శ్రీరామ స్మరణ & మాతృశక్తి ఆరాధన",
                        paksha = pakshaStr
                    )
                )
            }

            daysList.add(
                DaySummaryItem(
                    date = date,
                    dayOfMonth = d,
                    dayOfWeek = date.dayOfWeek,
                    tithi = panch.tithi,
                    nakshatra = panch.nakshatra,
                    yoga = panch.yoga,
                    karana = panch.karana,
                    festivals = dayFestivals,
                    isAmavasya = isAmav,
                    isPournami = isPour,
                    isEkadashi = isEka,
                    isPradosham = isPrad,
                    isChaturthi = isChat,
                    isSashti = isSash,
                    isMasaShivaratri = isShiva,
                    isSankranti = isSank,
                    isAshtami = isAsh,
                    isNavami = isNav
                )
            )
        }

        // Format dates into readable localized strings e.g. "10 గురువారము"
        fun formatDayList(dates: List<LocalDate>): String {
            if (dates.isEmpty()) return "ఈ నెలలో లేదు"
            return dates.joinToString(", ") { d ->
                "${d.dayOfMonth} ${LocalizationEngine.translateVara(d.dayOfWeek, language)}"
            }
        }

        val fastingRows = listOf(
            Triple("● అమావాస్య", formatDayList(amavasyaDays), Color(0xFF424242)),
            Triple("○ పౌర్ణమి", formatDayList(pournamiDays), Color(0xFFF57F17)),
            Triple("🙏 ఏకాదశి", formatDayList(ekadashiDays), Color(0xFF2E7D32)),
            Triple("🐂 ప్రదోషం", formatDayList(pradoshamDays), Color(0xFFC2185B)),
            Triple("షష్ఠి", formatDayList(sashtiDays), Color(0xFF7B1FA2)),
            Triple("చవితి", formatDayList(chaturthiDays), Color(0xFFE65100)),
            Triple("మాస శివరాత్రి", formatDayList(shivaratriDays), Color(0xFF00838F)),
            Triple("సంకటహర చతుర్థి", formatDayList(sankashtiDays), Color(0xFFD84315))
        )

        val otherRows = listOf(
            Pair("అష్టమి", formatDayList(ashtamiDays)),
            Pair("నవమి", formatDayList(navamiDays))
        )

        // Month festivals strictly filtered for the requested year and month
        val allYearFestivals = FestivalRepository.getFestivalsForYear(yearMonth.year)
        val monthFestivals = allYearFestivals.filter {
            it.date.year == yearMonth.year && it.date.monthValue == yearMonth.monthValue
        }.sortedBy { it.date }

        // Telugu month header (e.g. "శ్రావణ మాసము - భాద్రపద మాసము")
        val startPanch = AstronomicalEngine.calculatePanchanga(yearMonth.atDay(1), location, tradition)
        val endPanch = AstronomicalEngine.calculatePanchanga(yearMonth.atDay(daysInMonth), location, tradition)
        val startMasaStr = LocalizationEngine.translateMasa(startPanch.hinduMasa, language)
        val endMasaStr = LocalizationEngine.translateMasa(endPanch.hinduMasa, language)
        val teluguMonthHeader = if (startMasaStr == endMasaStr) {
            startMasaStr
        } else {
            "$startMasaStr - $endMasaStr"
        }

        val result = MonthPanchangaData(
            yearMonth = yearMonth,
            teluguMonthHeader = teluguMonthHeader,
            days = daysList,
            fastingRows = fastingRows,
            otherRows = otherRows,
            fastingOccurrences = fastingOccurrences.sortedBy { it.date },
            otherOccurrences = otherOccurrences.sortedBy { it.date },
            festivals = monthFestivals
        )
        monthPanchangaCache[cacheKey] = result
        return result
    }

    /**
     * Gets all yearly events for the entire 12 months with category classification
     */
    fun getYearlyEvents(
        year: Int,
        location: CityLocation,
        tradition: CalendarTradition,
        language: AppLanguage = AppLanguage.TE
    ): List<YearlyEventItem> {
        val cacheKey = "${year}_${location.id}_${tradition.name}_${language.name}"
        yearlyEventsCache[cacheKey]?.let { return it }

        val festivals = FestivalRepository.getFestivalsForYear(year)
        val list = mutableListOf<YearlyEventItem>()

        festivals.forEach { fest ->
            list.add(
                YearlyEventItem(
                    id = fest.id,
                    date = fest.date,
                    title = fest.name,
                    category = when (fest.category) {
                        FestivalCategory.EKADASHI -> YearlyCategory.EKADASHI
                        FestivalCategory.VRAT_FASTING -> YearlyCategory.VRATHAM
                        FestivalCategory.REGIONAL -> YearlyCategory.HOLIDAY
                        else -> YearlyCategory.FESTIVAL
                    },
                    icon = fest.iconEmoji,
                    summary = fest.summary
                )
            )
        }

        // Add Pournamis, Amavasyas, Sankrantis throughout all 12 months
        for (m in 1..12) {
            val ym = YearMonth.of(year, m)
            for (d in 1..ym.lengthOfMonth()) {
                val dt = ym.atDay(d)
                val fast = AstronomicalEngine.calculateFastTithiMasa(dt, location, tradition)
                if (fast.tithiNumber == 15 || fast.tithiName.contains("Purnima", ignoreCase = true)) {
                    if (list.none { it.date == dt && it.category == YearlyCategory.POURNAMI }) {
                        list.add(
                            YearlyEventItem(
                                id = "pournami_${dt}",
                                date = dt,
                                title = "${LocalizationEngine.translateMasa(fast.hinduMasa, language)} పౌర్ణమి",
                                category = YearlyCategory.POURNAMI,
                                icon = "🌕",
                                summary = "శుక్ల పక్ష పూర్ణిమ - సత్యనారాయణ వ్రత శుభ దినం"
                            )
                        )
                    }
                }
                if (fast.tithiNumber == 30 || fast.tithiName.contains("Amavasya", ignoreCase = true)) {
                    if (list.none { it.date == dt && it.category == YearlyCategory.AMAVASYA }) {
                        list.add(
                            YearlyEventItem(
                                id = "amavasya_${dt}",
                                date = dt,
                                title = "${LocalizationEngine.translateMasa(fast.hinduMasa, language)} అమావాస్య",
                                category = YearlyCategory.AMAVASYA,
                                icon = "🌑",
                                summary = "కృష్ణ పక్ష అమావాస్య - పితృ తర్పణ శుభ దినం"
                            )
                        )
                    }
                }
            }
        }

        val sorted = list.sortedBy { it.date }
        yearlyEventsCache[cacheKey] = sorted
        return sorted
    }
}

enum class YearlyCategory(val label: String) {
    ALL("అన్నీ"),
    FESTIVAL("పండుగలు"),
    VRATHAM("వ్రతాలు"),
    EKADASHI("ఏకాదశి"),
    POURNAMI("పౌర్ణమి"),
    AMAVASYA("అమావాస్య"),
    HOLIDAY("సెలవులు"),
    MUHURTHAM("ముహూర్తాలు")
}

data class YearlyEventItem(
    val id: String,
    val date: LocalDate,
    val title: String,
    val category: YearlyCategory,
    val icon: String,
    val summary: String
)
