package com.example.engine

import com.example.model.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

object FestivalRepository {

    private val yearFestivalCache = ConcurrentHashMap<Int, List<FestivalItem>>()

    /**
     * Dynamically calculates all Hindu festivals and auspicious vratams for any year.
     * Uses actual Astronomical Masa, Paksha, and Tithi calculation for each day of the requested year.
     * Fully dynamic: works for 2024, 2025, 2026, 2027, 2028, 2030, etc.
     */
    fun getFestivalsForYear(year: Int): List<FestivalItem> {
        return yearFestivalCache.getOrPut(year) {
            calculateFestivalsForYear(year)
        }
    }

    fun getFestivalsForDate(date: LocalDate): List<FestivalItem> {
        val all = getFestivalsForYear(date.year)
        return all.filter { it.date == date }
    }

    fun getUpcomingFestivals(fromDate: LocalDate, limit: Int = 10): List<FestivalItem> {
        val thisYear = getFestivalsForYear(fromDate.year)
        val upcomingThisYear = thisYear.filter { !it.date.isBefore(fromDate) }
        if (upcomingThisYear.size >= limit) {
            return upcomingThisYear.take(limit)
        }
        val nextYear = getFestivalsForYear(fromDate.year + 1)
        val combined = (upcomingThisYear + nextYear).sortedBy { it.date }
        return combined.take(limit)
    }

    private fun calculateFestivalsForYear(year: Int): List<FestivalItem> {
        val defaultLoc = AstronomicalEngine.CITIES.first() // Hyderabad / Central Telugu region
        val tradition = CalendarTradition.TELUGU
        val list = mutableListOf<FestivalItem>()

        val startDate = LocalDate.of(year, 1, 1)
        val isLeap = startDate.isLeapYear
        val totalDays = if (isLeap) 366 else 365

        // Track visited major festivals to avoid duplicates within consecutive days if tithi spans midnight
        val addedFestivals = mutableSetOf<String>()

        for (dayOffset in 0 until totalDays) {
            val date = startDate.plusDays(dayOffset.toLong())
            val fast = AstronomicalEngine.calculateFastTithiMasa(date, defaultLoc, tradition)

            val masa = fast.hinduMasa
            val paksha = fast.paksha
            val tithiNum = fast.tithiNumber // 1..30
            val tithiName = fast.tithiName
            val dayOfMonth = date.dayOfMonth
            val monthVal = date.monthValue
            val dow = date.dayOfWeek

            // 1. Fixed Solar / National Celebrations
            if (monthVal == 1 && dayOfMonth == 13) {
                list.add(
                    FestivalItem(
                        id = "lohri_$year",
                        name = "లోహ్రీ పండుగ",
                        date = date,
                        category = FestivalCategory.REGIONAL,
                        deity = "సూర్య & అగ్ని దేవుడు",
                        summary = "శీతాకాల ముగింపును సూచించే పంట కోతల సంబరం మరియు అగ్ని పూజ.",
                        significance = "నూతన పంటల ధాన్యాలు, బెల్లం, నువ్వులను పవిత్ర అగ్నికి సమర్పించి కృతజ్ఞతలు తెలుపుకోవడం.",
                        iconEmoji = "🔥"
                    )
                )
            }

            if (monthVal == 1 && (dayOfMonth == 14 || dayOfMonth == 15)) {
                val key = "makar_sankranti_$year"
                if (!addedFestivals.contains(key) && dayOfMonth == 14) {
                    addedFestivals.add(key)
                    list.add(
                        FestivalItem(
                            id = key,
                            name = "మకర సంక్రాంతి (పెద్ద పండుగ)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "సూర్య భగవానుడు",
                            summary = "సూర్యుని మకరరాశి సంక్రమణం, ఉత్తరాయణ పుణ్యకాల ప్రారంభం.",
                            significance = "రైతుల పంటల సంబరం, భోగి, సంక్రాంతి, కనుమ మూడు రోజుల వైభవం.",
                            pujaMuhurta = "07:15 AM - 12:30 PM (పుణ్య కాలం)",
                            iconEmoji = "🪁"
                        )
                    )
                }
            }

            if (monthVal == 1 && dayOfMonth == 15) {
                list.add(
                    FestivalItem(
                        id = "kanuma_$year",
                        name = "కనుమ పండుగ",
                        date = date,
                        category = FestivalCategory.REGIONAL,
                        deity = "గోమాత & పశువుల పూజ",
                        summary = "గోపూజ మరియు పశువుల కృతజ్ఞతా దినోత్సవం.",
                        significance = "వ్యవసాయంలో సాయపడే పశువులను అలంకరించి కృతజ్ఞతలు తెలపడం.",
                        iconEmoji = "🐄"
                    )
                )
            }

            if (monthVal == 1 && dayOfMonth == 26) {
                list.add(
                    FestivalItem(
                        id = "republic_day_$year",
                        name = "గణతంత్ర దినోత్సవం",
                        date = date,
                        category = FestivalCategory.GOVERNMENT_HOLIDAYS,
                        deity = "భారతమాత",
                        summary = "భారత రాజ్యాంగం అమలులోకి వచ్చిన జాతీయ శుభదినోత్సవం.",
                        significance = "జాతీయ జెండా ఆవిష్కరణ, రాజ్యాంగ స్మరణ మరియు దేశభక్తి సంబరాలు.",
                        iconEmoji = "🇮🇳"
                    )
                )
            }

            if (monthVal == 8 && dayOfMonth == 15) {
                list.add(
                    FestivalItem(
                        id = "independence_day_$year",
                        name = "స్వాతంత్ర్య దినోత్సవం",
                        date = date,
                        category = FestivalCategory.GOVERNMENT_HOLIDAYS,
                        deity = "భారతమాత",
                        summary = "భారతదేశ సార్వభౌమాధికారం మరియు స్వాతంత్ర్య సంబరాల జాతీయ దినోత్సవం.",
                        significance = "మువ్వన్నెల జాతీయ పతాకావిష్కరణ మరియు స్వాతంత్ర్య సమరయోధుల స్మరణ.",
                        iconEmoji = "🇮🇳"
                    )
                )
            }

            if (monthVal == 10 && dayOfMonth == 2) {
                list.add(
                    FestivalItem(
                        id = "gandhi_jayanti_$year",
                        name = "గాంధీ జయంతి",
                        date = date,
                        category = FestivalCategory.GOVERNMENT_HOLIDAYS,
                        deity = "మహాత్మా గాంధీ",
                        summary = "జాతిపిత మహాత్మా గాంధీ జన్మదినం మరియు అంతర్జాతీయ అహింసా దినోత్సవం.",
                        significance = "సత్యం, అహింస సిద్ధాంతాలను స్మరిస్తూ జాతిపితకు ఘన నివాళులు అర్పించే పర్వదినం.",
                        iconEmoji = "🕊️"
                    )
                )
            }

            // 2. Chaitra Masa (March - April)
            if (masa == "Chaitra" && paksha == "Shukla") {
                if (tithiNum == 1 && addedFestivals.add("ugadi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "ugadi_$year",
                            name = "ఉగాది (తెలుగు సంవత్సరాది)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "బ్రహ్మ దేవుడు & వేద పురుషుడు",
                            summary = "తెలుగు సంవత్సరాది, షడ్రుచుల ఉగాది పచ్చడి మరియు పంచాంగ శ్రవణం.",
                            significance = "సృష్టి ప్రారంభ దినం, నూతన సంవత్సర పంచాంగ శ్రవణం, ఆయురారోగ్య ప్రదాత.",
                            pujaMuhurta = "06:30 AM - 10:45 AM",
                            mantra = "శతాయుర్వజ్రదేహాయ సర్వసంపత్కరాయ చ",
                            iconEmoji = "🥭"
                        )
                    )
                }

                if (tithiNum == 9 && addedFestivals.add("sri_rama_navami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "sri_rama_navami_$year",
                            name = "శ్రీరామనవమి (సీతారామ కళ్యాణం)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ సీతారామచంద్ర స్వామి",
                            summary = "శ్రీరామ చంద్రుల జన్మదినం మరియు సీతారాముల దివ్య కళ్యాణ మహోత్సవం.",
                            significance = "భద్రాచలం వంటి పుణ్యక్షేత్రాలలో సీతారామ కళ్యాణం, వడపప్పు-పానకం పంపిణీ.",
                            pujaMuhurta = "11:05 AM - 01:35 PM (మధ్యాహ్న అభిజిత్ లగ్నం)",
                            mantra = "శ్రీరామ రామ రామేతి రమే రామే మనోరమే",
                            iconEmoji = "🏹"
                        )
                    )
                }

                if (tithiNum == 15 && addedFestivals.add("hanuman_jayanti_$year")) {
                    list.add(
                        FestivalItem(
                            id = "hanuman_jayanti_$year",
                            name = "హనుమాన్ జయంతి",
                            date = date,
                            category = FestivalCategory.JAYANTI,
                            deity = "శ్రీ ఆంజనేయ స్వామి",
                            summary = "శ్రీ ఆంజనేయ స్వామి వారి జన్మదినం, సింధూర పూజ మరియు హనుమాన్ చాలీసా పారాయణం.",
                            significance = "ధైర్యం, బలం, బుద్ధి, వివేకం ప్రసాదించే దివ్య జయంతి.",
                            mantra = "మనోజవం మారుతతుల్యవేగం జితేంద్రియం బుద్ధిమతాం వరిష్ఠం",
                            iconEmoji = "🚩"
                        )
                    )
                }
            }

            // 3. Vaishakha Masa (April - May)
            if (masa == "Vaishakha" && paksha == "Shukla") {
                if (tithiNum == 3 && addedFestivals.add("akshaya_tritiya_$year")) {
                    list.add(
                        FestivalItem(
                            id = "akshaya_tritiya_$year",
                            name = "అక్షయ తృతీయ",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ మహాలక్ష్మి & కుబేరుడు",
                            summary = "అనంతమైన పుణ్యఫలాలు మరియు సమృద్ధిని ప్రసాదించే సర్వశుభ దినం.",
                            significance = "స్వర్ణ కొనుగోలు, దానధర్మాలు, నూతన వ్యాపార ఆరంభానికి అత్యంత శ్రేష్ఠమైన తిథి.",
                            pujaMuhurta = "06:15 AM - 12:20 PM",
                            iconEmoji = "🪙"
                        )
                    )
                }

                if (tithiNum == 14 && addedFestivals.add("narasimha_jayanti_$year")) {
                    list.add(
                        FestivalItem(
                            id = "narasimha_jayanti_$year",
                            name = "నృసింహ జయంతి",
                            date = date,
                            category = FestivalCategory.JAYANTI,
                            deity = "శ్రీ లక్ష్మీ నరసింహ స్వామి",
                            summary = "భక్త ప్రహ్లాదుని రక్షించడానికి శ్రీ మహావిష్ణువు నరసింహునిగా అవతరించిన పవిత్ర దినం.",
                            significance = "సూర్యాస్తమయ సంధ్యాకాలంలో నరసింహ ఆరాధన శత్రుబాధలు నివారిస్తుంది.",
                            pujaMuhurta = "04:30 PM - 07:15 PM (ప్రదోష వేళ)",
                            iconEmoji = "🦁"
                        )
                    )
                }

                if (tithiNum == 15 && addedFestivals.add("buddha_purnima_$year")) {
                    list.add(
                        FestivalItem(
                            id = "buddha_purnima_$year",
                            name = "బుద్ధ పూర్ణిమ & కూర్మ జయంతి",
                            date = date,
                            category = FestivalCategory.POURNAMI,
                            deity = "గౌతమ బుద్ధుడు & శ్రీకూర్మ స్వామి",
                            summary = "గౌతమ బుద్ధుని నిర్వాణ దినం మరియు శ్రీకూర్మ జయంతి.",
                            significance = "శాంతి, అహింస మరియు సత్య ధర్మాల దివ్య ప్రబోధ దినం.",
                            iconEmoji = "🌸"
                        )
                    )
                }
            }

            // 4. Jyeshtha Masa (May - June)
            if (masa == "Jyeshtha" && paksha == "Shukla") {
                if (tithiNum == 10 && addedFestivals.add("ganga_dussehra_$year")) {
                    list.add(
                        FestivalItem(
                            id = "ganga_dussehra_$year",
                            name = "గంగా దసరా",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "గంగా భవాని",
                            summary = "స్వర్గం నుండి గంగా నది భూమిపైకి దిగివచ్చిన పుణ్య దినం.",
                            significance = "పది రకాల పాపాలను హరించే పవిత్ర గంగాస్నానం మరియు దీపదానం.",
                            iconEmoji = "🌊"
                        )
                    )
                }

                if (tithiNum == 15 && addedFestivals.add("vata_savitri_$year")) {
                    list.add(
                        FestivalItem(
                            id = "vata_savitri_$year",
                            name = "వట సావిత్రి వ్రతం & ఏరువాక పౌర్ణమి",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "సావిత్రి దేవి & భూమాత",
                            summary = "వ్యవసాయ తొలి దుక్కి పండుగ, నాగళ్ళు-ఎడ్ల పూజ మరియు మర్రి చెట్టు పూజ.",
                            significance = "రైతులకు వర్ష సమృద్ధి, పంటల సమృద్ధి మరియు స్త్రీలకు దీర్ఘ సుమంగళీ ప్రాప్తి.",
                            iconEmoji = "🌾"
                        )
                    )
                }
            }

            // 5. Ashadha Masa (June - July)
            if (masa == "Ashadha" && paksha == "Shukla") {
                if (tithiNum == 2 && addedFestivals.add("jagannath_ratha_yatra_$year")) {
                    list.add(
                        FestivalItem(
                            id = "jagannath_ratha_yatra_$year",
                            name = "పూరీ జగన్నాథ రథయాత్ర",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ జగన్నాథుడు, బలభద్రుడు & సుభద్ర",
                            summary = "పురీ జగన్నాథుని దివ్య రథయాత్ర మహోత్సవం.",
                            significance = "గుండిచా మందిరానికి వేలాది భక్తుల మధ్య స్వామి వారి రథ ప్రయాణం.",
                            iconEmoji = "🎪"
                        )
                    )
                }

                if (tithiNum == 11 && addedFestivals.add("tholi_ekadashi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "tholi_ekadashi_$year",
                            name = "తొలి ఏకాదశి (శయన ఏకాదశి)",
                            date = date,
                            category = FestivalCategory.EKADASHI,
                            deity = "శ్రీ మహావిష్ణువు",
                            summary = "చాతుర్మాస వ్రత ప్రారంభం, శ్రీమహావిష్ణువు శయన ఏకాదశి.",
                            significance = "ఉపవాసం, జాగరణ మరియు పేలాల పిండి ప్రసాద స్వీకరణతో పాపనాశనం.",
                            paranaTime = "మరుసటి రోజు ఉదయం 06:15 - 08:45",
                            iconEmoji = "🙏"
                        )
                    )
                }

                if (tithiNum == 15 && addedFestivals.add("guru_purnima_$year")) {
                    list.add(
                        FestivalItem(
                            id = "guru_purnima_$year",
                            name = "గురు పూర్ణిమ (వ్యాస పూజ)",
                            date = date,
                            category = FestivalCategory.POURNAMI,
                            deity = "మహర్షి వేదవ్యాసుడు & సద్గురువు",
                            summary = "వేద వ్యాసుల జయంతి, గురుదేవులకు పాదాభివందన సమర్పణ దినం.",
                            significance = "గురుకృప ద్వారా జ్ఞానోదయం, సద్బుద్ధి సిద్ధింపజేసే పరమ పవిత్ర దినం.",
                            mantra = "గురుర్బ్రహ్మా గురుర్విష్ణుః గురుర్దేవో మహేశ్వరః",
                            iconEmoji = "🪷"
                        )
                    )
                }
            }

            // 6. Shravana Masa (July - August)
            if (masa == "Shravana") {
                if (paksha == "Shukla" && tithiNum == 5 && addedFestivals.add("naga_panchami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "naga_panchami_$year",
                            name = "నాగ పంచమి",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "నాగేంద్ర స్వామి & సుబ్రహ్మణ్యేశ్వరుడు",
                            summary = "పుట్టలో పాలు పోసి నాగేంద్రుని పూజించే పవిత్ర పండుగ.",
                            significance = "సర్పదోష నివారణ, సంతాన ప్రాప్తి మరియు కుటుంబ రక్షణ.",
                            iconEmoji = "🐍"
                        )
                    )
                }

                // Varalakshmi Vratam: Friday before Shravana Purnima
                if (paksha == "Shukla" && dow == DayOfWeek.FRIDAY && tithiNum in 8..14 && addedFestivals.add("varalakshmi_vratam_$year")) {
                    list.add(
                        FestivalItem(
                            id = "varalakshmi_vratam_$year",
                            name = "వరలక్ష్మీ వ్రతం",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "శ్రీ వరలక్ష్మీ దేవి",
                            summary = "శ్రావణ మాస శుక్రవార వరలక్ష్మీ వ్రతం, కలశ స్థాపన మరియు తోరం పూజ.",
                            significance = "సకల సౌభాగ్యాలు, ఐశ్వర్యం, అష్టలక్ష్మీ కటాక్షం ప్రసాదించే పరమ శ్రేష్ఠ వ్రతం.",
                            pujaMuhurta = "ఉదయం 07:30 - 10:30 లేదా సాయంత్రం 05:30 - 07:00",
                            mantra = "పద్మాసనే పద్మకరే సర్వలోకైక పూజితే",
                            iconEmoji = "🪷"
                        )
                    )
                }

                if (paksha == "Shukla" && tithiNum == 15 && addedFestivals.add("rakhi_purnima_$year")) {
                    list.add(
                        FestivalItem(
                            id = "rakhi_purnima_$year",
                            name = "రాఖీ పౌర్ణమి (రక్షాబంధన్ & హయగ్రీవ జయంతి)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ హయగ్రీవ స్వామి",
                            summary = "సోదర సోదరీమణుల అనురాగ బంధం రాఖీ పండుగ, యజ్ఞోపవీత ధారణ.",
                            significance = "రక్షా సూత్ర బంధనం, శ్రావణ పౌర్ణమి ఉపాకర్మ మరియు విద్యా ప్రదాత హయగ్రీవ పూజ.",
                            pujaMuhurta = "ఉదయం 08:30 - మధ్యాహ్నం 01:15",
                            iconEmoji = "🧶"
                        )
                    )
                }

                // Sri Krishna Janmashtami (Ashtami of Krishna Paksha)
                if (paksha == "Krishna" && (tithiNum == 8 || tithiNum == 23) && addedFestivals.add("sri_krishna_janmashtami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "sri_krishna_janmashtami_$year",
                            name = "శ్రీకృష్ణాష్టమి (గోకులాష్టమి)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "భగవాన్ శ్రీకృష్ణుడు",
                            summary = "రోహిణీ నక్షత్ర యుక్త శ్రీకృష్ణావతార దినం, ఉట్లోత్సవం మరియు బాలగోపాల పూజ.",
                            significance = "అర్ధరాత్రి నిశీధ వేళ కృష్ణ జననోత్సవం, వెన్న-అటుకుల నైవేద్యం.",
                            pujaMuhurta = "11:58 PM - 12:45 AM (నిశీధ కాలం)",
                            mantra = "ఓం నమో భగవతే వాసుదేవాయ",
                            iconEmoji = "🦚"
                        )
                    )
                }
            }

            // 7. Bhadrapada Masa (August - September)
            if (masa == "Bhadrapada" || (monthVal in 8..9 && paksha == "Shukla")) {
                if (paksha == "Shukla" && (tithiNum == 4 || tithiNum == 3 || tithiNum == 5) && addedFestivals.add("vinayaka_chavithi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "vinayaka_chavithi_$year",
                            name = "వినాయక చవితి (గణేష్ చతుర్థి)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ విఘ్నేశ్వరుడు (గణపతి)",
                            summary = "విఘ్ననాశక గణనాథుని మట్టి ప్రతిమ స్థాపన, ఏకవింశతి పత్ర పూజ.",
                            significance = "సమస్త విఘ్నాలు తొలగి సర్వకార్య విజయాలు సిద్ధించే మహా పర్వదినం.",
                            pujaMuhurta = "11:05 AM - 01:35 PM (మధ్యాహ్న గణేశ లగ్నం)",
                            mantra = "శుక్లాంబరధరం విష్ణుం శశివర్ణం చతుర్భుజం",
                            iconEmoji = "🐘"
                        )
                    )
                }

                if (paksha == "Shukla" && (tithiNum == 5 || tithiNum == 6) && addedFestivals.add("rishi_panchami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "rishi_panchami_$year",
                            name = "ఋషి పంచమి",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "సప్తర్షులు",
                            summary = "సప్తఋషుల పూజ, వ్రతకథ పఠనం మరియు సప్తర్షి ఆశీర్వాదం.",
                            significance = "తెలిసీ తెలియక చేసిన సమస్త పాపముల నివారణకు ప్రసిద్ధ వ్రతం.",
                            iconEmoji = "🧘‍♂️"
                        )
                    )
                }

                if (paksha == "Shukla" && (tithiNum == 14 || tithiNum == 13) && addedFestivals.add("anantha_chaturdashi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "anantha_chaturdashi_$year",
                            name = "అనంత పద్మనాభ చతుర్దశి వ్రతం",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "శ్రీ అనంత పద్మనాభ స్వామి",
                            summary = "14 ముడుల అనంత దారంతో శ్రీమహావిష్ణువును ఆరాధించే దివ్య వ్రతం.",
                            significance = "కష్టాల నుండి విముక్తి, సర్వతోముఖ అభివృద్ధి మరియు సంపూర్ణ రక్షణ.",
                            iconEmoji = "🪷"
                        )
                    )
                }

                if (paksha == "Krishna" && (tithiNum == 15 || tithiNum == 30 || tithiNum == 29) && addedFestivals.add("mahalaya_amavasya_$year")) {
                    list.add(
                        FestivalItem(
                            id = "mahalaya_amavasya_$year",
                            name = "మహాలయ అమావాస్య (పితృ పక్షం)",
                            date = date,
                            category = FestivalCategory.AMAVASYA,
                            deity = "పితృదేవతలు",
                            summary = "పితృ పక్ష ముగింపు, పితృదేవతల కృతజ్ఞతా తర్పణ దినం.",
                            significance = "తిల తర్పణాలు, అన్నదానం ద్వారా పితృదేవతల అనుగ్రహం మరియు వంశాభివృద్ధి.",
                            iconEmoji = "🙏"
                        )
                    )
                }
            }

            // 8. Ashwina Masa (September - October)
            if (masa == "Ashwina") {
                if (paksha == "Shukla" && tithiNum == 1 && addedFestivals.add("devi_navaratri_arambham_$year")) {
                    list.add(
                        FestivalItem(
                            id = "devi_navaratri_arambham_$year",
                            name = "శరన్నవరాత్రులు ప్రారంభం (కలశ స్థాపన)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ దుర్గాదేవి & కనకదుర్గమ్మ",
                            summary = "కలశ స్థాపనతో తొమ్మిది రోజుల పాటు సాగే దేవి శరన్నవరాత్రుల ఆరంభం.",
                            significance = "ఇంద్రకీలాద్రిపై శ్రీ కనకదుర్గమ్మ అలంకార పూజలు, కుంకుమార్చనలు.",
                            pujaMuhurta = "06:20 AM - 10:15 AM (ఘటస్థాపన)",
                            mantra = "సర్వమంగళ మాంగల్యే శివే సర్వార్థ సాధికే",
                            iconEmoji = "🔱"
                        )
                    )
                }

                if (paksha == "Shukla" && tithiNum == 8 && addedFestivals.add("durgashtami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "durgashtami_$year",
                            name = "దుర్గాష్టమి & సద్దుల బతుకమ్మ",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ దుర్గాదేవి & గౌరమ్మ",
                            summary = "మహాష్టమి దుర్గా పూజ మరియు తెలంగాణ పూల పండుగ సద్దుల బతుకమ్మ.",
                            significance = "సకల విజయాలు, శక్తి ప్రదాత దుర్గాష్టమి ఆరాధన.",
                            iconEmoji = "🌺"
                        )
                    )
                }

                if (paksha == "Shukla" && tithiNum == 9 && addedFestivals.add("ayudha_puja_$year")) {
                    list.add(
                        FestivalItem(
                            id = "ayudha_puja_$year",
                            name = "మహానవమి / ఆయుధ పూజ",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ సరస్వతీ దేవి & విశ్వకర్మ",
                            summary = "యంత్రాలు, వాహనాలు, పుస్తకాలు మరియు పనిముట్లను పూజించే మహానవమి.",
                            significance = "వృత్తి, వ్యాపారాలలో విజయాల కోసం సాధనాల పవిత్రార్చన.",
                            iconEmoji = "🛠️"
                        )
                    )
                }

                if (paksha == "Shukla" && tithiNum == 10 && addedFestivals.add("vijayadashami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "vijayadashami_$year",
                            name = "విజయదశమి (దసరా పండుగ)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ చాముండేశ్వరి & శ్రీరాముడు",
                            summary = "మహిషాసుర మర్దనం, శమీ పూజ మరియు జమ్మి ఆకుల మార్పిడి.",
                            significance = "చెడుపై మంచి సాధించిన విజయం, ఏ పని తలపెట్టినా విజయం చేకూరే విజయదశమి.",
                            pujaMuhurta = "02:05 PM - 02:50 PM (విజయ ముహూర్తం)",
                            mantra = "శమీ శమయతే పాపం శమీ శత్రువినాశినీ",
                            iconEmoji = "🌿"
                        )
                    )
                }

                if (paksha == "Krishna" && (tithiNum == 4 || tithiNum == 19) && addedFestivals.add("atla_tadde_$year")) {
                    list.add(
                        FestivalItem(
                            id = "atla_tadde_$year",
                            name = "అట్ల తద్దె",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "శ్రీ గౌరీదేవి & చంద్ర దేవుడు",
                            summary = "ఆంధ్రుల సంప్రదాయ అట్ల తద్దె వ్రతం, ఉయ్యాలలూగడం, గోరింటాకు ధారణ.",
                            significance = "కన్యలకు మంచి భర్త లభించడం మరియు సుమంగళి సౌభాగ్య రక్షణ.",
                            iconEmoji = "🌕"
                        )
                    )
                }
            }

            // 9. Kartika Masa (October - November)
            if (masa == "Kartika" || (masa == "Ashwina" && paksha == "Krishna" && tithiNum >= 13)) {
                if (paksha == "Krishna" && (tithiNum == 13 || tithiNum == 28) && addedFestivals.add("dhanteras_$year")) {
                    list.add(
                        FestivalItem(
                            id = "dhanteras_$year",
                            name = "ధన త్రయోదశి (ధన్తేరస్)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "ధన్వంతరి భగవానుడు & మహాలక్ష్మి",
                            summary = "ఆయుర్వేద అవతార దినం, లక్ష్మీ-కుబేర పూజ మరియు పాత్రల కొనుగోలు.",
                            significance = "ఆరోగ్యం, సంపద మరియు అకాల మృత్యు నివారణకు యమదీపం.",
                            pujaMuhurta = "06:15 PM - 08:25 PM (ప్రదోష కాలం)",
                            iconEmoji = "🏺"
                        )
                    )
                }

                if (paksha == "Krishna" && (tithiNum == 14 || tithiNum == 29) && addedFestivals.add("naraka_chaturdashi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "naraka_chaturdashi_$year",
                            name = "నరక చతుర్దశి",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "సత్యభామా సమేత శ్రీకృష్ణుడు",
                            summary = "నరకాసుర వధ సంబరం, తెల్లవారుజామున అభ్యంగన స్నానం.",
                            significance = "యమలోక భయాలు తొలగించి సుఖశాంతులు ప్రసాదించే ఉషఃకాల స్నానం.",
                            iconEmoji = "🪔"
                        )
                    )
                }

                if (paksha == "Krishna" && (tithiNum == 15 || tithiNum == 30) && addedFestivals.add("deepavali_$year")) {
                    list.add(
                        FestivalItem(
                            id = "deepavali_$year",
                            name = "దీపావళి పండుగ (లక్ష్మీ పూజ)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ మహాలక్ష్మి & గణపతి",
                            summary = "దివ్య దీప కాంతుల మహా పండుగ, లక్ష్మీ కుబేర పూజ మరియు బాణసంచా.",
                            significance = "చీకటిపై వెలుగుల విజయం, అష్టైశ్వర్యాలు మరియు నిరంతర ఆనందం.",
                            pujaMuhurta = "06:30 PM - 08:35 PM (స్థిర వృషభ లగ్నం)",
                            mantra = "ఓం శ్రీం హ్రీం క్లీం మహాలక్ష్మ్యై నమః",
                            iconEmoji = "🪔"
                        )
                    )
                }
            }

            if (masa == "Kartika" && paksha == "Shukla") {
                if ((tithiNum == 4 || tithiNum == 3 || tithiNum == 5) && addedFestivals.add("nagula_chavithi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "nagula_chavithi_$year",
                            name = "నాగుల చవితి",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "నాగేంద్ర స్వామి",
                            summary = "కార్తీక శుద్ధ చవితి నాగుల చవితి పండుగ, పుట్టలో పాలు పోసి పూజించడం.",
                            significance = "సర్పభయం తొలగి, కుటుంబ ఆరోగ్యం, పిల్లల రక్షణ ప్రసాదించే శ్రేష్ఠ వ్రతం.",
                            pujaMuhurta = "ఉదయం 08:00 - 11:30",
                            iconEmoji = "🐍"
                        )
                    )
                }

                if ((tithiNum == 11 || tithiNum == 12) && addedFestivals.add("kartika_ekadashi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "kartika_ekadashi_$year",
                            name = "క్షీరాబ్ధి ద్వాదశి & తులసీ వివాహం",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "తులసీ మాత & శ్రీ దామోదర స్వామి",
                            summary = "తులసి కోటలో దామోదర స్వామి కళ్యాణం, ఉసిరి కొమ్మల తోరణాలు.",
                            significance = "తులసి వివాహంతో సమస్త వివాహ ముహూర్తాల ప్రారంభం, వైకుంఠ ప్రాప్తి.",
                            pujaMuhurta = "06:15 PM - 08:30 PM",
                            iconEmoji = "🌿"
                        )
                    )
                }

                if (tithiNum == 15 && addedFestivals.add("kartika_purnima_$year")) {
                    list.add(
                        FestivalItem(
                            id = "kartika_purnima_$year",
                            name = "కార్తీక పౌర్ణమి (జ్వాలాతోరణం)",
                            date = date,
                            category = FestivalCategory.POURNAMI,
                            deity = "పరమేశ్వరుడు & సుబ్రహ్మణ్యేశ్వరుడు",
                            summary = "365 వత్తుల దీపారాధన, శివాలయాలలో జ్వాలాతోరణ దర్శనం.",
                            significance = "త్రిపురాసుర సంహార దినం, గంగా స్నానం, అనంతమైన శివానుగ్రహం.",
                            mantra = "ఓం నమః శివాయ",
                            iconEmoji = "🔥"
                        )
                    )
                }
            }

            // 10. Margashira Masa (November - December)
            if (masa == "Margashira" && paksha == "Shukla") {
                if (tithiNum == 6 && addedFestivals.add("subramanya_sashti_$year")) {
                    list.add(
                        FestivalItem(
                            id = "subramanya_sashti_$year",
                            name = "సుబ్రహ్మణ్య షష్ఠి (స్కంద షష్ఠి)",
                            date = date,
                            category = FestivalCategory.VRATHAMS,
                            deity = "శ్రీ సుబ్రహ్మణ్య స్వామి",
                            summary = "తారకాసుర వధ చేసిన షణ్ముఖుని ఆరాధన, కావడి ఉత్సవాలు.",
                            significance = "కుజదోష నివారణ, సంతాన ప్రాప్తి మరియు శత్రుజయం.",
                            iconEmoji = "🪶"
                        )
                    )
                }

                if (tithiNum == 11 && addedFestivals.add("gita_jayanti_$year")) {
                    list.add(
                        FestivalItem(
                            id = "gita_jayanti_$year",
                            name = "గీతా జయంతి & మోక్షద ఏకాదశి",
                            date = date,
                            category = FestivalCategory.EKADASHI,
                            deity = "భగవాన్ శ్రీకృష్ణుడు",
                            summary = "కురుక్షేత్ర రణరంగంలో శ్రీకృష్ణుడు అర్జునునికి భగవద్గీతను ఉపదేశించిన దినం.",
                            significance = "18 అధ్యాయాల గీతా పారాయణం సకల సందేహాలను నివారించి మోక్షాన్నిస్తుంది.",
                            mantra = "కర్మణ్యేవాధికారస్తే మా ఫలేషు కదాచన",
                            iconEmoji = "📖"
                        )
                    )
                }
            }

            // 11. Pushya Masa (December - January)
            if ((masa == "Margashira" || masa == "Pushya") && paksha == "Shukla" && tithiNum == 11 && addedFestivals.add("vaikuntha_ekadashi_$year")) {
                list.add(
                    FestivalItem(
                        id = "vaikuntha_ekadashi_$year",
                        name = "ముక్కోటి ఏకాదశి (వైకుంఠ ఏకాదశి)",
                        date = date,
                        category = FestivalCategory.EKADASHI,
                        deity = "శ్రీ వేంకటేశ్వర స్వామి",
                        summary = "తిరుమల మరియు వైష్ణవ ఆలయాలలో ఉత్తర ద్వార దర్శనం ప్రారంభం.",
                        significance = "వైకుంఠ ద్వార ప్రవేశం పునర్జన్మ రాహిత్యాన్ని మరియు పరమపదాన్ని ప్రసాదిస్తుంది.",
                        iconEmoji = "🚪"
                    )
                )
            }

            // 12. Magha Masa (January - February)
            if (masa == "Magha" && paksha == "Shukla") {
                if (tithiNum == 5 && addedFestivals.add("vasant_panchami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "vasant_panchami_$year",
                            name = "వసంత పంచమి (శ్రీ సరస్వతీ పూజ)",
                            date = date,
                            category = FestivalCategory.MAJOR_FESTIVALS,
                            deity = "శ్రీ సరస్వతీ దేవి",
                            summary = "విద్యాదేవి సరస్వతీ అమ్మవారి జన్మదినం, అక్షరాభ్యాసాలకు అత్యంత శుభదినం.",
                            significance = "బాసర వంటి పుణ్యక్షేత్రాలలో అక్షరాభ్యాసాలు, పసుపు వస్త్ర ధారణ.",
                            pujaMuhurta = "07:15 AM - 12:30 PM",
                            mantra = "ఓం ఐం సరస్వత్యై నమః",
                            iconEmoji = "🪕"
                        )
                    )
                }

                if (tithiNum == 7 && addedFestivals.add("ratha_saptami_$year")) {
                    list.add(
                        FestivalItem(
                            id = "ratha_saptami_$year",
                            name = "రథసప్తమి (సూర్య జయంతి)",
                            date = date,
                            category = FestivalCategory.JAYANTI,
                            deity = "సూర్య భగవానుడు",
                            summary = "సూర్య భగవానుని రథం ఉత్తర-తూర్పు దిశకు తిరిగే పవిత్ర దినం.",
                            significance = "జిల్లేడు ఆకులు తలపై ఉంచి స్నానం చేయడం ద్వారా దీర్ఘాయుష్షు, ఆరోగ్యం.",
                            pujaMuhurta = "05:25 AM - 07:15 AM (స్నాన ముహూర్తం)",
                            iconEmoji = "☀️"
                        )
                    )
                }

                if (tithiNum == 11 && addedFestivals.add("bhishma_ekadashi_$year")) {
                    list.add(
                        FestivalItem(
                            id = "bhishma_ekadashi_$year",
                            name = "భీష్మ ఏకాదశి",
                            date = date,
                            category = FestivalCategory.EKADASHI,
                            deity = "భీష్మ పితామహుడు & శ్రీ మహావిష్ణువు",
                            summary = "విష్ణు సహస్రనామ స్తోత్ర అవతరణ దినం, భీష్ముని ముక్తి దివసం.",
                            significance = "విష్ణు సహస్రనామ పారాయణం సకల పాపహరం మరియు రోగ నివారకం.",
                            iconEmoji = "📜"
                        )
                    )
                }
            }

            // 13. Phalguna Masa (February - March)
            if ((masa == "Magha" || masa == "Phalguna") && paksha == "Krishna" && (tithiNum == 14 || tithiNum == 29) && addedFestivals.add("maha_shivaratri_$year")) {
                list.add(
                    FestivalItem(
                        id = "maha_shivaratri_$year",
                        name = "మహా శివరాత్రి",
                        date = date,
                        category = FestivalCategory.MAJOR_FESTIVALS,
                        deity = "పరమశివుడు & పార్వతీ దేవి",
                        summary = "శివ-పార్వతుల దివ్య కళ్యాణం, లింగోద్భవ కాలం, ఉపవాసం మరియు జాగరణ.",
                        significance = "నిశీధ కాలంలో మహాలింగార్చన, బిల్వార్చనతో జన్మజన్మల పాప విముక్తి.",
                        pujaMuhurta = "11:58 PM - 12:48 AM (నిశీధ కాలం)",
                        mantra = "ఓం నమః శివాయ",
                        iconEmoji = "🔱"
                    )
                )
            }

            if (masa == "Phalguna" && paksha == "Shukla" && tithiNum == 15 && addedFestivals.add("holi_$year")) {
                list.add(
                    FestivalItem(
                        id = "holi_$year",
                        name = "కామదహనం & హోలీ పండుగ",
                        date = date,
                        category = FestivalCategory.MAJOR_FESTIVALS,
                        deity = "రాధాకృష్ణులు & మన్మథుడు",
                        summary = "వసంత ఋతువు ఆహ్వానం, రంగుల పండుగ మరియు కామదహన మహోత్సవం.",
                        significance = "వసంతోత్సవం, పరస్పరం రంగులు చల్లుకుంటూ ఆనందాన్ని పంచుకోవడం.",
                        iconEmoji = "🎨"
                    )
                )
            }

            // Generic Monthly Fasting Days (Ekadashi, Pournami, Amavasya)
            val isEkadashi = tithiName.contains("Ekadashi", ignoreCase = true) || tithiNum == 11 || tithiNum == 26
            val isPournami = tithiName.contains("Pournami", ignoreCase = true) || tithiName.contains("Purnima", ignoreCase = true) || (paksha == "Shukla" && tithiNum == 15)
            val isAmavasya = tithiName.contains("Amavasya", ignoreCase = true) || (paksha == "Krishna" && (tithiNum == 15 || tithiNum == 30))

            val masaTe = LocalizationEngine.translateMasa(masa, AppLanguage.TE)

            if (isEkadashi && addedFestivals.add("ekadashi_${date}")) {
                val pakText = if (paksha == "Shukla" || tithiNum == 11) "శుక్ల" else "కృష్ణ"
                list.add(
                    FestivalItem(
                        id = "ekadashi_${date}",
                        name = "$masaTe $pakText ఏకాదశి",
                        date = date,
                        category = FestivalCategory.EKADASHI,
                        deity = "శ్రీ మహావిష్ణువు",
                        summary = "శ్రీమహావిష్ణువు అనుగ్రహం కోసం అత్యంత పవిత్రమైన ఏకాదశి ఉపవాస దినం.",
                        significance = "హరివాసరంలో ఉపవాసం చేసి విష్ణు సహస్రనామ పారాయణం చేయడం సర్వపాపహరం.",
                        pujaMuhurta = "సూర్యోదయం నుండి ద్వాదశి ఘడియల వరకు",
                        iconEmoji = "🙏"
                    )
                )
            }

            if (isPournami && addedFestivals.add("pournami_${date}")) {
                list.add(
                    FestivalItem(
                        id = "pournami_${date}",
                        name = "$masaTe పౌర్ణమి",
                        date = date,
                        category = FestivalCategory.POURNAMI,
                        deity = "సత్యనారాయణ స్వామి",
                        summary = "చంద్రుని సంపూర్ణ ప్రశాంత కాంతి ప్రసరించే శుభ పౌర్ణమి.",
                        significance = "శ్రీ సత్యనారాయణ వ్రతం మరియు చంద్ర దర్శనానికి అత్యంత శ్రేష్ఠమైన తిథి.",
                        iconEmoji = "🌕"
                    )
                )
            }

            if (isAmavasya && addedFestivals.add("amavasya_${date}")) {
                list.add(
                    FestivalItem(
                        id = "amavasya_${date}",
                        name = "$masaTe అమావాస్య",
                        date = date,
                        category = FestivalCategory.AMAVASYA,
                        deity = "పితృదేవతలు & పరమేశ్వరుడు",
                        summary = "పితృదేవతల ఆరాధనకు మరియు తర్పణాలకు అనుకూలమైన పుణ్య దినం.",
                        significance = "పితృ తర్పణాలు, దానధర్మాలు మరియు శివారాధన ద్వారా కుటుంబ శాంతి, సమృద్ధి.",
                        iconEmoji = "🌑"
                    )
                )
            }
        }

        return list.sortedBy { it.date }
    }
}
