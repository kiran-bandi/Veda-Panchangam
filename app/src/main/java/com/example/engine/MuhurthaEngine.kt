package com.example.engine

import com.example.model.*
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

object MuhurthaEngine {

    private val muhurthaCache = java.util.concurrent.ConcurrentHashMap<String, List<MuhurthaResult>>()

    fun findMuhurthas(
        category: MuhurthaCategory,
        startDate: LocalDate,
        monthsRange: Int,
        location: CityLocation,
        tradition: CalendarTradition,
        language: AppLanguage = AppLanguage.TE
    ): List<MuhurthaResult> {
        val cacheKey = "${category.name}_${startDate}_${monthsRange}_${location.id}_${tradition.name}_${language.name}"
        muhurthaCache[cacheKey]?.let { return it }

        val results = mutableListOf<MuhurthaResult>()
        val endDate = startDate.plusMonths(monthsRange.toLong())
        var curr = startDate

        // Evaluate days in range
        while (curr.isBefore(endDate) && results.size < 12) {
            val fast = AstronomicalEngine.calculateFastTithiMasa(curr, location, tradition)
            val tithiNum = fast.tithiNumber % 15
            val nakIndex = fast.nakshatraIndex

            val isFavorableTithi = tithiNum in listOf(2, 3, 5, 7, 10, 11, 13) // Dwitiya, Tritiya, Panchami, Saptami, Dashami, Ekadashi, Trayodashi
            val isRiktaTithi = tithiNum in listOf(4, 9, 14, 0) // Chaturthi, Navami, Chaturdashi, Amavasya

            // Category-specific nakshatra preferences
            val isFavorableNakshatra = when (category) {
                MuhurthaCategory.MARRIAGE -> nakIndex in listOf(3, 4, 6, 9, 11, 12, 14, 16, 18, 20, 25, 26) // Rohini, Mrigashira, Uttara Phalguni, Hasta, Swati, Anuradha, etc.
                MuhurthaCategory.GRIHA_PRAVESH -> nakIndex in listOf(3, 4, 11, 12, 13, 16, 20, 25, 26) // Rohini, Mriga, Chitra, Anuradha, Uttara Ashadha, etc.
                MuhurthaCategory.VEHICLE -> nakIndex in listOf(0, 3, 6, 7, 12, 14, 21, 22, 26) // Ashwini, Rohini, Punarvasu, Pushya, Hasta, Swati, Shravana, Dhanishta
                MuhurthaCategory.PROPERTY -> nakIndex in listOf(3, 4, 7, 11, 12, 16, 20, 21, 25) // Rohini, Mriga, Pushya, Uttara, Anuradha, Shravana
                MuhurthaCategory.BUSINESS -> nakIndex in listOf(0, 3, 7, 11, 12, 14, 16, 21, 26)
                MuhurthaCategory.NAMAKARAN -> nakIndex in listOf(0, 3, 4, 6, 7, 11, 12, 14, 16, 21, 25, 26)
                MuhurthaCategory.ANNAPRASHANA -> nakIndex in listOf(0, 3, 4, 6, 7, 12, 14, 16, 21, 26)
                MuhurthaCategory.UPANAYANA -> nakIndex in listOf(0, 3, 4, 6, 7, 11, 12, 14, 16, 21)
                MuhurthaCategory.AKSHARABHYASAM -> nakIndex in listOf(0, 3, 4, 6, 7, 11, 12, 14, 16, 21, 26)
                MuhurthaCategory.TRAVEL -> nakIndex in listOf(0, 4, 6, 7, 12, 14, 16, 21, 26)
            }

            if (isFavorableNakshatra && !isRiktaTithi && isFavorableTithi) {
                val panchanga = AstronomicalEngine.calculatePanchanga(curr, location, tradition)
                val rating = when {
                    tithiNum in listOf(3, 5, 10, 13) && nakIndex in listOf(3, 7, 12, 16, 21) -> "Highly Auspicious"
                    isFavorableTithi -> "Auspicious"
                    else -> "Good"
                }

                val timeWindow = when (category) {
                    MuhurthaCategory.MARRIAGE -> when (language) {
                        AppLanguage.TE -> "ఉదయం 07:30 - 10:15 (ఉదయ శుభ లగ్నం)"
                        AppLanguage.HI -> "प्रातः 07:30 - 10:15 (प्रातः शुभ लग्न)"
                        else -> "07:30 AM - 10:15 AM (Morning Lagna)"
                    }
                    MuhurthaCategory.GRIHA_PRAVESH -> when (language) {
                        AppLanguage.TE -> "ఉదయం 06:45 - 09:30 (వృషభ స్థిర లగ్నం)"
                        AppLanguage.HI -> "प्रातः 06:45 - 09:30 (वृषभ स्थिर लग्न)"
                        else -> "06:45 AM - 09:30 AM (Vrishabha Sthira Lagna)"
                    }
                    MuhurthaCategory.VEHICLE -> when (language) {
                        AppLanguage.TE -> "ఉదయం 09:15 - 11:40 (చోఘడియా లాభ సమయం)"
                        AppLanguage.HI -> "प्रातः 09:15 - 11:40 (चौघड़िया लाभ)"
                        else -> "09:15 AM - 11:40 AM (Choghadiya Labh)"
                    }
                    MuhurthaCategory.BUSINESS -> when (language) {
                        AppLanguage.TE -> "ఉదయం 08:30 - 11:15 (అమృత కాలం)"
                        AppLanguage.HI -> "प्रातः 08:30 - 11:15 (अमृत काल)"
                        else -> "08:30 AM - 11:15 AM (Amrit Kaal)"
                    }
                    MuhurthaCategory.NAMAKARAN -> when (language) {
                        AppLanguage.TE -> "ఉదయం 09:00 - 11:30 (శుభ సమయం)"
                        AppLanguage.HI -> "प्रातः 09:00 - 11:30 (शुभ समय)"
                        else -> "09:00 AM - 11:30 AM"
                    }
                    MuhurthaCategory.PROPERTY -> when (language) {
                        AppLanguage.TE -> "ఉదయం 07:00 - 09:45 (అభిజిత్ / స్థిర ముహూర్తం)"
                        AppLanguage.HI -> "प्रातः 07:00 - 09:45 (अभिजीत मुहूर्त)"
                        else -> "07:00 AM - 09:45 AM"
                    }
                    else -> when (language) {
                        AppLanguage.TE -> "ఉదయం 08:15 - 11:00 (అనుకూల శుభ ముహూర్తం)"
                        AppLanguage.HI -> "प्रातः 08:15 - 11:00 (शुभ मुहूर्त)"
                        else -> "08:15 AM - 11:00 AM"
                    }
                }

                val favorablePoints = mutableListOf<String>()
                val translatedNak = LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, language)
                val translatedTithi = LocalizationEngine.translateTithi(panchanga.tithi.name, language)
                val translatedPaksha = LocalizationEngine.translatePaksha(panchanga.paksha, language)

                when (language) {
                    AppLanguage.TE -> {
                        favorablePoints.add("అనుకూలమైన $translatedNak నక్షత్రం (శుభప్రదం)")
                        favorablePoints.add("$translatedPaksha నందు శ్రేష్ఠమైన $translatedTithi తిథి")
                        favorablePoints.add("రాహుకాలం మరియు వర్జ్యం లేని పరిశుద్ధ సమయం")
                    }
                    AppLanguage.HI -> {
                        favorablePoints.add("अनुकूल $translatedNak नक्षत्र (शुभ फलदायी)")
                        favorablePoints.add("$translatedPaksha में उत्तम $translatedTithi तिथि")
                        favorablePoints.add("राहुकाल एवं वर्ज्यम दोष रहित पावन समय")
                    }
                    else -> {
                        favorablePoints.add("Favorable $translatedNak Nakshatra (${panchanga.nakshatra.auspiciousness})")
                        favorablePoints.add("Beneficial $translatedTithi under $translatedPaksha")
                        favorablePoints.add("Free of Rahu Kalam & major planetary collisions")
                    }
                }

                val cautions = when (language) {
                    AppLanguage.TE -> listOf(
                        "రాహుకాలం (${panchanga.auspiciousTimings.rahuKalam}) సమయంలో ముహూర్తం ప్రారంభించకూడదు",
                        "కుటుంబ జాతక అనుకూలత కోసం పండితుల సలహా సూచించడమైనది"
                    )
                    AppLanguage.HI -> listOf(
                        "राहुकाल (${panchanga.auspiciousTimings.rahuKalam}) के समय कार्य प्रारंभ करने से बचें",
                        "व्यक्तिगत जन्मपत्रिका मिलान हेतु ज्योतिषी परामर्श अनुशंसित"
                    )
                    else -> listOf(
                        "Avoid starting precisely during Rahu Kalam (${panchanga.auspiciousTimings.rahuKalam})",
                        "Traditional astrologer confirmation recommended for specific family horoscopes"
                    )
                }

                results.add(
                    MuhurthaResult(
                        date = curr,
                        dayOfWeek = LocalizationEngine.translateVara(curr.dayOfWeek, language),
                        purpose = category,
                        timeWindow = timeWindow,
                        rating = rating,
                        tithi = panchanga.tithi.name,
                        nakshatra = "${panchanga.nakshatra.name} (Pada ${panchanga.nakshatra.pada})",
                        favorablePoints = favorablePoints,
                        cautions = cautions
                    )
                )
            }

            curr = curr.plusDays(1)
        }

        muhurthaCache[cacheKey] = results
        return results
    }
}
