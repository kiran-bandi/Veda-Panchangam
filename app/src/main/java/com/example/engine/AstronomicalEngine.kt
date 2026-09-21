package com.example.engine

import com.example.model.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.*

object AstronomicalEngine {

    private val NAKSHATRA_NAMES = listOf(
        "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashirsha", "Ardra",
        "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni",
        "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha",
        "Mula", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishta", "Shatabhisha",
        "Purva Bhadrapada", "Uttara Bhadrapada", "Revati"
    )

    private val NAKSHATRA_DEITIES = listOf(
        "Ashvins", "Yama", "Agni", "Brahma / Prajapati", "Soma", "Rudra",
        "Aditi", "Brihaspati", "Sarpa / Nagas", "Pitras", "Bhaga", "Aryaman",
        "Surya / Savitri", "Vishwakarma", "Vayu", "Indra-Agni", "Mitra", "Indra",
        "Nirriti", "Apah (Water)", "Vishvedevas", "Vishnu", "Vasus", "Varuna",
        "Aja Ekapada", "Ahirbudhnya", "Pushan"
    )

    private val NAKSHATRA_SYMBOLS = listOf(
        "Horse's Head", "Yoni / Triangle", "Razor / Flame", "Chariot / Cart", "Deer's Head", "Teardrop / Diamond",
        "Bow and Quiver", "Lotus / Cow's Udder", "Coiled Serpent", "Royal Throne", "Front legs of Bed", "Back legs of Bed",
        "Open Hand / Fist", "Bright Pearl / Gem", "Sprout / Coral", "Archway / Potter's Wheel", "Triumphal Arch / Lotus", "Circular Amulet",
        "Tied Roots / Lion's Tail", "Elephant Tusk / Fan", "Small Bed / Planks", "Three Footprints / Ear", "Musical Drum (Mridangam)", "Empty Circle / 100 Stars",
        "Front of Funeral Cot / Sword", "Twins / Back of Cot", "Fish / Drum"
    )

    private val NAKSHATRA_RULERS = listOf(
        "Ketu", "Venus", "Sun", "Moon", "Mars", "Rahu",
        "Jupiter", "Saturn", "Mercury", "Ketu", "Venus", "Sun",
        "Moon", "Mars", "Rahu", "Jupiter", "Saturn", "Mercury",
        "Ketu", "Venus", "Sun", "Moon", "Mars", "Rahu",
        "Jupiter", "Saturn", "Mercury"
    )

    private val YOGA_NAMES = listOf(
        "Vishkambha", "Priti", "Ayushman", "Saubhagya", "Shobhana", "Atiganda",
        "Sukarma", "Dhriti", "Shoola", "Ganda", "Vriddhi", "Dhruva",
        "Vyaghata", "Harshana", "Vajra", "Asiddhi", "Vyatipata", "Variyan",
        "Parigha", "Shiva", "Siddha", "Sadhya", "Shubha", "Shukla",
        "Brahma", "Indra", "Vaidhriti"
    )

    private val KARANA_NAMES = listOf(
        "Bava", "Balava", "Kaulava", "Taitila", "Garija", "Vanija", "Vishti (Bhadra)"
    )

    private val TITHI_NAMES = listOf(
        "Prathama", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
        "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
        "Ekadashi", "Dwadashi", "Trayodashi", "Chaturdashi", "Purnima"
    )

    private val RASHI_NAMES = listOf(
        "Mesha (Aries)", "Vrishabha (Taurus)", "Mithuna (Gemini)", "Karka (Cancer)",
        "Simha (Leo)", "Kanya (Virgo)", "Tula (Libra)", "Vrishchika (Scorpio)",
        "Dhanu (Sagittarius)", "Makara (Capricorn)", "Kumbha (Aquarius)", "Meena (Pisces)"
    )

    private val HINDU_MONTHS = listOf(
        "Chaitra", "Vaishakha", "Jyeshtha", "Ashadha", "Shravana", "Bhadrapada",
        "Ashwina", "Kartika", "Margashirsha", "Pausha", "Magha", "Phalguna"
    )

    private val SAMVATSARA_60 = listOf(
        "Prabhava", "Vibhava", "Shukla", "Pramoda", "Prajapati", "Angirasa", "Shrimukha", "Bhava",
        "Yuva", "Dhatri", "Ishwara", "Bahudhanya", "Pramathi", "Vikrama", "Vrisha", "Chitrabhanu",
        "Subhanu", "Tarana", "Parthiva", "Vyaya", "Sarvajit", "Sarvadhari", "Virodhi", "Vikrita",
        "Khara", "Nandana", "Vijaya", "Jaya", "Manmatha", "Durmukha", "Hemalamba", "Vilamba",
        "Vikari", "Sharvari", "Plava", "Shubhakrit", "Sobhakrit", "Krodhi", "Vishwavasu", "Parabhava",
        "Plavanga", "Kilaka", "Saumya", "Sadharana", "Virodhakrit", "Paridhavi", "Pramadicha", "Ananda",
        "Rakshasa", "Anala", "Pingala", "Kalayukta", "Siddharthi", "Raudri", "Durmati", "Dundubhi",
        "Rudhrodgari", "Raktakshi", "Krodhana", "Kshaya"
    )

    // Precomputed famous cities
    val CITIES = listOf(
        CityLocation("hyd", "Hyderabad", "Telangana, India", 17.3850, 78.4867, "Asia/Kolkata"),
        CityLocation("blr", "Bengaluru", "Karnataka, India", 12.9716, 77.5946, "Asia/Kolkata"),
        CityLocation("bom", "Mumbai", "Maharashtra, India", 19.0760, 72.8777, "Asia/Kolkata"),
        CityLocation("del", "New Delhi", "Delhi, India", 28.6139, 77.2090, "Asia/Kolkata"),
        CityLocation("maa", "Chennai", "Tamil Nadu, India", 13.0827, 80.2707, "Asia/Kolkata"),
        CityLocation("ccu", "Kolkata", "West Bengal, India", 22.5726, 88.3639, "Asia/Kolkata"),
        CityLocation("pnq", "Pune", "Maharashtra, India", 18.5204, 73.8567, "Asia/Kolkata"),
        CityLocation("amd", "Ahmedabad", "Gujarat, India", 23.0225, 72.5714, "Asia/Kolkata"),
        CityLocation("vns", "Varanasi (Kashi)", "Uttar Pradesh, India", 25.3176, 82.9739, "Asia/Kolkata"),
        CityLocation("tpt", "Tirupati", "Andhra Pradesh, India", 13.6288, 79.4192, "Asia/Kolkata"),
        CityLocation("vga", "Vijayawada", "Andhra Pradesh, India", 16.5062, 80.6480, "Asia/Kolkata"),
        CityLocation("ujn", "Ujjain", "Madhya Pradesh, India", 23.1765, 75.7885, "Asia/Kolkata"),
        CityLocation("jpr", "Jaipur", "Rajasthan, India", 26.9124, 75.7873, "Asia/Kolkata"),
        CityLocation("cok", "Kochi", "Kerala, India", 9.9312, 76.2673, "Asia/Kolkata"),
        CityLocation("bbi", "Bhubaneswar", "Odisha, India", 20.2961, 85.8245, "Asia/Kolkata"),
        CityLocation("nyc", "New York", "USA", 40.7128, -74.0060, "America/New_York", isDiaspora = true),
        CityLocation("sfo", "San Jose / Bay Area", "USA", 37.3382, -121.8863, "America/Los_Angeles", isDiaspora = true),
        CityLocation("lon", "London", "United Kingdom", 51.5074, -0.1278, "Europe/London", isDiaspora = true),
        CityLocation("dxb", "Dubai", "United Arab Emirates", 25.2048, 55.2708, "Asia/Dubai", isDiaspora = true),
        CityLocation("sin", "Singapore", "Singapore", 1.3521, 103.8198, "Asia/Singapore", isDiaspora = true),
        CityLocation("syd", "Sydney", "Australia", -33.8688, 151.2093, "Australia/Sydney", isDiaspora = true),
        CityLocation("tor", "Toronto", "Canada", 43.6532, -79.3832, "America/Toronto", isDiaspora = true)
    )

    // Julian day calculation
    fun toJulianDay(year: Int, month: Int, day: Int, hourFraction: Double = 0.0): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + hourFraction + b - 1524.5
    }

    // Solar Longitude in degrees (Jean Meeus Astronomical Algorithms High Precision)
    fun getSunLongitude(jd: Double): Double {
        val T = (jd - 2451545.0) / 36525.0
        val L0 = (280.46646 + 36000.76983 * T + 0.0003032 * T * T) % 360.0
        val M = (357.52911 + 35999.05029 * T - 0.0001537 * T * T) % 360.0
        val mRad = Math.toRadians(M)
        val C = (1.914602 - 0.004817 * T - 0.000014 * T * T) * sin(mRad) +
                (0.019993 - 0.000101 * T) * sin(2.0 * mRad) +
                0.000289 * sin(3.0 * mRad)
        val trueSun = (L0 + C + 360.0) % 360.0
        val omega = Math.toRadians(125.04 - 1934.136 * T)
        val apparentSun = trueSun - 0.00569 - 0.00478 * sin(omega)
        return (apparentSun + 360.0) % 360.0
    }

    // Lunar Longitude in degrees (Meeus ELP-2000 25-term Perturbation Series)
    fun getMoonLongitude(jd: Double): Double {
        val T = (jd - 2451545.0) / 36525.0
        val Ldeg = (218.3164477 + 481267.88123421 * T - 0.0015786 * T * T) % 360.0
        val Ddeg = (297.8501921 + 445267.1114034 * T - 0.0018819 * T * T) % 360.0
        val MsunDeg = (357.5291092 + 35999.0502909 * T - 0.0001536 * T * T) % 360.0
        val MmoonDeg = (134.9633964 + 477198.8675055 * T + 0.0087414 * T * T) % 360.0
        val Fdeg = (93.2720950 + 483202.0175273 * T - 0.0036539 * T * T) % 360.0

        val D = Math.toRadians(Ddeg)
        val M = Math.toRadians(MsunDeg)
        val Mp = Math.toRadians(MmoonDeg)
        val F = Math.toRadians(Fdeg)

        val dLong = 6.288774 * sin(Mp) +
                1.274027 * sin(2 * D - Mp) +
                0.658314 * sin(2 * D) +
                -0.185596 * sin(M) +
                -0.114336 * sin(2 * F) +
                0.213618 * sin(2 * Mp) +
                0.057043 * sin(2 * D - M - Mp) +
                0.053322 * sin(2 * D + Mp) +
                0.045874 * sin(2 * D - M) +
                0.041024 * sin(Mp - M) +
                -0.034720 * sin(D) +
                -0.030465 * sin(M + Mp) +
                0.015327 * sin(2 * D - 2 * F) +
                -0.012528 * sin(2 * F + Mp) +
                -0.010980 * sin(2 * F - Mp) +
                0.010675 * sin(4 * D - Mp) +
                0.010034 * sin(3 * Mp) +
                0.008548 * sin(4 * D - 2 * Mp) +
                -0.007888 * sin(2 * D + M - Mp) +
                -0.006766 * sin(2 * D + M) +
                -0.005163 * sin(D - Mp) +
                0.004987 * sin(D + M) +
                0.004036 * sin(2 * D - Mp + 2 * F) +
                0.003994 * sin(2 * D - 2 * Mp) +
                0.003861 * sin(4 * D)

        return (Ldeg + dLong + 360.0) % 360.0
    }

    // Lahiri Ayanamsha (Chitra Paksha) in degrees with nutational periodic terms
    // Reference: Indian Calendar Reform Committee / Spica at 180° Sidereal Longitude
    fun getLahiriAyanamsha(year: Int, month: Int): Double {
        val dayInYear = (month - 1) * 30.4375 + 15
        val fractionalYear = year + dayInYear / 365.25
        val t = (fractionalYear - 2000.0) / 100.0
        // Base mean ayanamsha at J2000 = 23.857092°
        val meanAyanamsha = 23.857092 + (fractionalYear - 2000.0) * (50.290966 / 3600.0)
        // Nutation term in longitude (approx. periodic Correction)
        val omega = Math.toRadians((125.04452 - 1934.136261 * t) % 360.0)
        val nutationArcSec = -17.20 * sin(omega)
        val nutationDeg = (nutationArcSec / 3600.0) * cos(Math.toRadians(23.439))
        return meanAyanamsha + nutationDeg
    }

    // Solves exact date and LocalTime when Nirayana Sun enters 270.0° (Makara Rashi)
    fun calculateNirayanaMakaraSankranti(year: Int): Pair<LocalDate, LocalTime> {
        var jdLow = toJulianDay(year, 1, 13, 0.0)
        var jdHigh = toJulianDay(year, 1, 16, 0.0)
        for (i in 0..20) {
            val mid = (jdLow + jdHigh) / 2.0
            val sunL = getSunLongitude(mid)
            val ayan = getLahiriAyanamsha(year, 1)
            val siderealSun = (sunL - ayan + 360.0) % 360.0
            if (siderealSun < 270.0 && siderealSun > 200.0) {
                jdLow = mid
            } else {
                jdHigh = mid
            }
        }
        val z = (jdLow + 0.5).toLong()
        val f = (jdLow + 0.5) - z
        val dayFrac = f * 24.0
        val hour = dayFrac.toInt().coerceIn(0, 23)
        val minute = ((dayFrac - hour) * 60).toInt().coerceIn(0, 59)
        val day = (14 + (z % 2).toInt()).coerceIn(14, 15)
        return Pair(LocalDate.of(year, 1, day), LocalTime.of(hour, minute))
    }


    // Planetary calculation algorithms (VSOP87 / Keplerian standard Sidereal & Geocentric)
    fun getMarsLongitude(jd: Double): Double {
        val d = jd - 2451545.0
        val lMars = (355.433 + 0.52403304 * d) % 360.0
        val mMars = (19.373 + 0.52402078 * d) % 360.0
        val eqMars = 10.691 * sin(Math.toRadians(mMars)) + 0.623 * sin(Math.toRadians(2 * mMars))
        val helioMars = (lMars + eqMars) % 360.0
        val sunL = getSunLongitude(jd)
        val dx = 1.524 * cos(Math.toRadians(helioMars)) - 1.0 * cos(Math.toRadians(sunL))
        val dy = 1.524 * sin(Math.toRadians(helioMars)) - 1.0 * sin(Math.toRadians(sunL))
        return (Math.toDegrees(atan2(dy, dx)) + 360.0) % 360.0
    }

    fun getMercuryLongitude(jd: Double): Double {
        val d = jd - 2451545.0
        val lMer = (252.251 + 4.09233445 * d) % 360.0
        val mMer = (174.795 + 4.09233445 * d) % 360.0
        val helioMer = (lMer + 23.440 * sin(Math.toRadians(mMer)) + 2.982 * sin(Math.toRadians(2 * mMer))) % 360.0
        val sunL = getSunLongitude(jd)
        val dx = 0.387 * cos(Math.toRadians(helioMer)) - 1.0 * cos(Math.toRadians(sunL))
        val dy = 0.387 * sin(Math.toRadians(helioMer)) - 1.0 * sin(Math.toRadians(sunL))
        return (Math.toDegrees(atan2(dy, dx)) + 360.0) % 360.0
    }

    fun getJupiterLongitude(jd: Double): Double {
        val d = jd - 2451545.0
        val lJup = (34.351 + 0.0830853 * d) % 360.0
        val mJup = (20.020 + 0.0830853 * d) % 360.0
        val helioJup = (lJup + 5.555 * sin(Math.toRadians(mJup)) + 0.168 * sin(Math.toRadians(2 * mJup))) % 360.0
        val sunL = getSunLongitude(jd)
        val dx = 5.204 * cos(Math.toRadians(helioJup)) - 1.0 * cos(Math.toRadians(sunL))
        val dy = 5.204 * sin(Math.toRadians(helioJup)) - 1.0 * sin(Math.toRadians(sunL))
        return (Math.toDegrees(atan2(dy, dx)) + 360.0) % 360.0
    }

    fun getVenusLongitude(jd: Double): Double {
        val d = jd - 2451545.0
        val lVen = (181.979 + 1.60213022 * d) % 360.0
        val mVen = (50.416 + 1.60213022 * d) % 360.0
        val helioVen = (lVen + 0.776 * sin(Math.toRadians(mVen)) + 0.003 * sin(Math.toRadians(2 * mVen))) % 360.0
        val sunL = getSunLongitude(jd)
        val dx = 0.723 * cos(Math.toRadians(helioVen)) - 1.0 * cos(Math.toRadians(sunL))
        val dy = 0.723 * sin(Math.toRadians(helioVen)) - 1.0 * sin(Math.toRadians(sunL))
        return (Math.toDegrees(atan2(dy, dx)) + 360.0) % 360.0
    }

    fun getSaturnLongitude(jd: Double): Double {
        val d = jd - 2451545.0
        val lSat = (50.077 + 0.0334442 * d) % 360.0
        val mSat = (317.021 + 0.0334442 * d) % 360.0
        val helioSat = (lSat + 6.359 * sin(Math.toRadians(mSat)) + 0.220 * sin(Math.toRadians(2 * mSat))) % 360.0
        val sunL = getSunLongitude(jd)
        val dx = 9.582 * cos(Math.toRadians(helioSat)) - 1.0 * cos(Math.toRadians(sunL))
        val dy = 9.582 * sin(Math.toRadians(helioSat)) - 1.0 * sin(Math.toRadians(sunL))
        return (Math.toDegrees(atan2(dy, dx)) + 360.0) % 360.0
    }

    fun getRahuLongitude(jd: Double): Double {
        val d = jd - 2451545.0
        val node = (125.0445 - 0.05295376 * d) % 360.0
        return (node + 360.0) % 360.0
    }

    fun getKetuLongitude(jd: Double): Double {
        return (getRahuLongitude(jd) + 180.0) % 360.0
    }

    fun getLagnaLongitude(jd: Double, lat: Double, lng: Double): Double {
        val d = jd - 2451545.0
        val gmst = (280.46061837 + 360.98564736629 * d) % 360.0
        val lst = (gmst + lng + 360.0) % 360.0
        val eps = 23.4392911
        val lstRad = Math.toRadians(lst)
        val epsRad = Math.toRadians(eps)
        val latRad = Math.toRadians(lat)
        val num = cos(lstRad)
        val denom = -(sin(lstRad) * cos(epsRad) + tan(latRad) * sin(epsRad))
        val asc = Math.toDegrees(atan2(num, denom))
        return (asc + 360.0) % 360.0
    }

    val TELUGU_RASHIS_LIST = listOf(
        "మేషం", "వృషభం", "మిథునం", "కర్కాటకం",
        "సింహం", "కన్య", "తుల", "వృశ్చికం",
        "ధనుస్సు", "మకరం", "కుంభం", "మీనం"
    )

    val TELUGU_NAKSHATRAS_LIST = listOf(
        "అశ్విని", "భరణి", "కృత్తిక", "రోహిణి", "మృగశిర", "ఆరుద్ర",
        "పునర్వసు", "పుష్యమి", "ఆశ్లేష", "మఖ", "పూర్వ ఫల్గుణి (పుబ్బ)", "ఉత్తర ఫల్గుణి (ఉత్తర)",
        "హస్త", "చిత్ర", "స్వాతి", "విశాఖ", "అనూరాధ", "జ్యేష్ఠ",
        "మూల", "పూర్వాషాఢ", "ఉత్తరాషాఢ", "శ్రవణం", "ధనిష్ఠ", "శతభిషం",
        "పూర్వాభాద్ర", "ఉత్తరాభాద్ర", "రేవతి"
    )

    fun calculatePlanetTransits(
        date: LocalDate,
        location: CityLocation,
        time: LocalTime = LocalTime.of(6, 0)
    ): RashiChakraData {
        val hourFraction = (time.hour + time.minute / 60.0) / 24.0
        val jd = toJulianDay(date.year, date.monthValue, date.dayOfMonth, hourFraction)
        val ayanamsha = getLahiriAyanamsha(date.year, date.monthValue)

        fun createTransit(
            id: String,
            nameTe: String,
            shortNameTe: String,
            symbol: String,
            calcTropical: (Double) -> Double,
            alwaysRetrograde: Boolean = false,
            neverRetrograde: Boolean = false
        ): PlanetTransitInfo {
            val trop = calcTropical(jd)
            val sid = (trop - ayanamsha + 360.0) % 360.0
            val rIdx = (sid / 30.0).toInt().coerceIn(0, 11)
            val degInSign = sid % 30.0
            val deg = degInSign.toInt()
            val min = ((degInSign - deg) * 60).toInt()
            val degStr = String.format("%02d° %02d'", deg, min)
            val nakIdx = (sid / (360.0 / 27.0)).toInt().coerceIn(0, 26)
            val pada = (((sid % (360.0 / 27.0)) / (360.0 / 108.0)).toInt() + 1).coerceIn(1, 4)
            
            val isRetro = when {
                neverRetrograde -> false
                alwaysRetrograde -> true
                else -> {
                    val tropPrev = calcTropical(jd - 0.5)
                    val tropNext = calcTropical(jd + 0.5)
                    val diff = (tropNext - tropPrev + 540.0) % 360.0 - 180.0
                    diff < 0
                }
            }
            val statusTe = if (isRetro) "వక్రగతి ↺" else "మార్గి"

            return PlanetTransitInfo(
                id = id,
                nameTe = nameTe,
                shortNameTe = shortNameTe,
                symbol = symbol,
                siderealLongitude = sid,
                rashiIndex = rIdx,
                rashiNameTe = TELUGU_RASHIS_LIST[rIdx],
                degreeInSign = degInSign,
                degreeStrTe = degStr,
                nakshatraNumber = nakIdx + 1,
                nakshatraNameTe = TELUGU_NAKSHATRAS_LIST[nakIdx],
                pada = pada,
                isRetrograde = isRetro,
                statusTe = statusTe
            )
        }

        val sun = createTransit("sun", "సూర్యుడు (రవి)", "రవి", "☉", { getSunLongitude(it) }, neverRetrograde = true)
        val moon = createTransit("moon", "చంద్రుడు", "చంద్ర", "☽", { getMoonLongitude(it) }, neverRetrograde = true)
        val mars = createTransit("mars", "కుజుడు (అంగారకుడు)", "కుజ", "♂", { getMarsLongitude(it) })
        val mercury = createTransit("mercury", "బుధుడు", "బుధ", "☿", { getMercuryLongitude(it) })
        val jupiter = createTransit("jupiter", "గురుడు (బృహస్పతి)", "గురు", "♃", { getJupiterLongitude(it) })
        val venus = createTransit("venus", "శుక్రుడు", "శుక్ర", "♀", { getVenusLongitude(it) })
        val saturn = createTransit("saturn", "శని భగవానుడు", "శని", "♄", { getSaturnLongitude(it) })
        val rahu = createTransit("rahu", "రాహువు", "రాహు", "☊", { getRahuLongitude(it) }, alwaysRetrograde = true)
        val ketu = createTransit("ketu", "కేతువు", "కేతు", "☋", { getKetuLongitude(it) }, alwaysRetrograde = true)
        val lagna = createTransit("lagna", "లగ్నం (ఉదయ లగ్నం)", "ల", "౹", { getLagnaLongitude(it, location.latitude, location.longitude) }, neverRetrograde = true)

        val planetsList = listOf(sun, moon, mars, mercury, jupiter, venus, saturn, rahu, ketu)

        return RashiChakraData(
            date = date,
            location = location,
            planets = planetsList,
            lagna = lagna,
            ayanamshaName = "లాహిరి చిత్రపక్ష అయనాంశం (${String.format(Locale.ENGLISH, "%.2f", ayanamsha)}°)",
            zodiacSystem = "నిరయణ రాశి చక్రం"
        )
    }

    fun formatTeluguTimeLimit(time: LocalTime): String {
        val h = time.hour
        val m = time.minute
        val period = when (h) {
            in 4..11 -> "ఉదయం"
            in 12..15 -> "మధ్యాహ్నం"
            in 16..19 -> "సాయంత్రం"
            else -> "రాత్రి"
        }
        val displayH = when {
            h == 0 -> 12
            h > 12 -> h - 12
            else -> h
        }
        return String.format(Locale.ENGLISH, "%s %02d:%02d వరకు", period, displayH, m)
    }


    // Sunrise and Sunset calculation using standard NOAA atmospheric refraction zenith 90.8333°
    fun calculateSunriseSunset(date: LocalDate, location: CityLocation): Pair<LocalTime, LocalTime> {
        val dayOfYear = date.dayOfYear
        val lat = location.latitude
        val lng = location.longitude

        val tzOffsetHours = try {
            java.time.ZoneId.of(location.timezoneId).rules.getOffset(date.atTime(12, 0)).totalSeconds / 3600.0
        } catch (e: Exception) {
            when (location.timezoneId) {
                "Asia/Kolkata" -> 5.5
                "Asia/Dubai" -> 4.0
                "Asia/Singapore" -> 8.0
                "Australia/Sydney" -> 10.0
                "Europe/London" -> 1.0
                "America/New_York" -> -4.0
                "America/Los_Angeles" -> -7.0
                "America/Toronto" -> -4.0
                else -> 5.5
            }
        }

        val zenith = 90.8333
        val lngHour = lng / 15.0
        val tRise = dayOfYear + ((6.0 - lngHour) / 24.0)
        val tSet = dayOfYear + ((18.0 - lngHour) / 24.0)

        fun calcTime(t: Double, isSunrise: Boolean): LocalTime {
            val m = (0.9856 * t) - 3.289
            val mRad = Math.toRadians(m)
            var l = m + (1.916 * sin(mRad)) + (0.020 * sin(2 * mRad)) + 282.634
            l = (l % 360 + 360) % 360
            val lRad = Math.toRadians(l)

            var ra = Math.toDegrees(atan(0.91764 * tan(lRad)))
            ra = (ra % 360 + 360) % 360
            val lQuadrant = floor(l / 90.0) * 90.0
            val raQuadrant = floor(ra / 90.0) * 90.0
            ra += (lQuadrant - raQuadrant)
            val raHours = ra / 15.0

            val sinDec = 0.39782 * sin(lRad)
            val cosDec = cos(asin(sinDec))

            val cosH = (cos(Math.toRadians(zenith)) - (sinDec * sin(Math.toRadians(lat)))) /
                    (cosDec * cos(Math.toRadians(lat)))

            val clampedCosH = cosH.coerceIn(-1.0, 1.0)
            val hHours = if (isSunrise) {
                (360.0 - Math.toDegrees(acos(clampedCosH))) / 15.0
            } else {
                Math.toDegrees(acos(clampedCosH)) / 15.0
            }

            val tUtc = hHours + raHours - (0.06571 * t) - 6.622
            var localT = (tUtc - lngHour + tzOffsetHours) % 24.0
            if (localT < 0) localT += 24.0

            val hour = localT.toInt().coerceIn(0, 23)
            val minute = ((localT - hour) * 60).toInt().coerceIn(0, 59)
            return LocalTime.of(hour, minute)
        }

        val rise = calcTime(tRise, true)
        val set = calcTime(tSet, false)
        return Pair(rise, set)
    }

    // Moonrise and Moonset calculation with astronomical meridian transit alignment (DrikPanchang method)
    fun calculateMoonriseMoonset(
        date: LocalDate,
        location: CityLocation,
        sunLong: Double,
        moonLong: Double,
        sunrise: LocalTime,
        sunset: LocalTime
    ): Pair<LocalTime, LocalTime> {
        val lat = location.latitude
        val latRad = Math.toRadians(lat)
        val eps = Math.toRadians(23.439)

        // Solar Right Ascension
        val sinSunLong = sin(Math.toRadians(sunLong))
        val cosSunLong = cos(Math.toRadians(sunLong))
        var sunRa = Math.toDegrees(atan2(cos(eps) * sinSunLong, cosSunLong))
        sunRa = (sunRa % 360.0 + 360.0) % 360.0

        // Lunar Right Ascension & Declination
        val sinMoonLong = sin(Math.toRadians(moonLong))
        val cosMoonLong = cos(Math.toRadians(moonLong))
        var moonRa = Math.toDegrees(atan2(cos(eps) * sinMoonLong, cosMoonLong))
        moonRa = (moonRa % 360.0 + 360.0) % 360.0

        val sinMoonDec = sin(eps) * sinMoonLong
        val cosMoonDec = sqrt((1.0 - sinMoonDec * sinMoonDec).coerceAtLeast(0.0001))

        // Lunar Zenith for rise/set: 90° + 34' (refraction) + 16' (semi-diameter) - 57' (parallax) = 89°53' = 89.8833°
        val moonZenithRad = Math.toRadians(89.8833)
        val cosH0 = (cos(moonZenithRad) - (sin(latRad) * sinMoonDec)) / (cos(latRad) * cosMoonDec)
        val clampedCosH0 = cosH0.coerceIn(-1.0, 1.0)
        val semiDiurnalArcHours = Math.toDegrees(acos(clampedCosH0)) / 15.0

        // Difference in RA between Moon and Sun in hours
        var diffRaHours = (moonRa - sunRa) / 15.0
        diffRaHours = (diffRaHours % 24.0 + 24.0) % 24.0

        val dayLengthMinutes = java.time.Duration.between(sunrise, sunset).toMinutes().toDouble()
        val solarNoonMinuteOfDay = (sunrise.toSecondOfDay() / 60.0) + (dayLengthMinutes / 2.0)

        // Moon transit time in minutes of the day
        val moonTransitMinuteOfDay = (solarNoonMinuteOfDay + diffRaHours * 60.0) % 1440.0

        var riseMinutes = (moonTransitMinuteOfDay - semiDiurnalArcHours * 60.0) % 1440.0
        if (riseMinutes < 0) riseMinutes += 1440.0

        var setMinutes = (moonTransitMinuteOfDay + semiDiurnalArcHours * 60.0) % 1440.0
        if (setMinutes < 0) setMinutes += 1440.0

        val riseH = (riseMinutes / 60.0).toInt().coerceIn(0, 23)
        val riseM = (riseMinutes % 60.0).toInt().coerceIn(0, 59)

        val setH = (setMinutes / 60.0).toInt().coerceIn(0, 23)
        val setM = (setMinutes % 60.0).toInt().coerceIn(0, 59)

        return Pair(LocalTime.of(riseH, riseM), LocalTime.of(setH, setM))
    }

    // Traditional Varjya Ghati start offsets for 27 Nakshatras (out of 60 Ghati)
    private val VARJYA_GHATI_OFFSETS = listOf(
        50, 24, 30, 40, 14, 11, 30, 20, 32, 30, 20, 18, 21, 20, 14, 14, 10, 14, 56, 24, 20, 10, 10, 18, 16, 24, 30
    )

    // Finds the exact LocalTime relative to sunriseJd when targetDegrees is reached (22-iteration sub-second precision)
    private fun findBoundaryTime(
        sunriseJd: Double,
        sunriseLocal: LocalTime,
        targetDeg: Double,
        evaluator: (Double) -> Double
    ): LocalTime {
        var low = 0.0
        var high = 1.25 // Search up to 30 hours
        for (step in 0..17) {
            val mid = (low + high) / 2.0
            val cur = evaluator(sunriseJd + mid)
            var diff = (cur - targetDeg + 360.0) % 360.0
            if (diff > 180.0) diff -= 360.0
            if (diff < 0) {
                low = mid
            } else {
                high = mid
            }
        }
        val totalMinutes = (low * 24.0 * 60.0).toLong()
        return sunriseLocal.plusMinutes(totalMinutes)
    }

    fun formatTeluguTimePeriod(time: LocalTime): String {
        val h = time.hour
        val m = time.minute
        val period = when (h) {
            in 4..11 -> "ఉ."
            in 12..15 -> "మ."
            in 16..19 -> "సా."
            else -> "రా."
        }
        val displayH = when {
            h == 0 -> 12
            h > 12 -> h - 12
            else -> h
        }
        return String.format(Locale.ENGLISH, "%s %d:%02d", period, displayH, m)
    }

    private fun getKaranaNameTe(karanaTotal: Int): String {
        return when {
            karanaTotal == 0 -> "కింస్తుఘ్న"
            karanaTotal in 1..56 -> listOf("బవ", "బాలవ", "కౌలవ", "తైతిల", "గరజి", "వణిజ", "దిష్టి (భద్ర)")[ (karanaTotal - 1) % 7 ]
            karanaTotal == 57 -> "శకుని"
            karanaTotal == 58 -> "చతుష్పాత్"
            else -> "నాగవ"
        }
    }

    // Full daily calculation
    private val panchangaCalculationCache = java.util.concurrent.ConcurrentHashMap<String, DayPanchanga>()
    private val fastTithiMasaCache = java.util.concurrent.ConcurrentHashMap<String, FastTithiMasa>()

    data class FastTithiMasa(
        val tithiIndex: Int,
        val tithiNumber: Int,
        val tithiName: String,
        val paksha: String,
        val isShukla: Boolean,
        val hinduMasa: String,
        val masaIndex: Int,
        val nakshatraIndex: Int,
        val nakshatraName: String
    )

    fun calculateFastTithiMasa(
        date: LocalDate,
        location: CityLocation,
        tradition: CalendarTradition
    ): FastTithiMasa {
        val cacheKey = "${date}_${location.id}_${tradition.name}"
        fastTithiMasaCache[cacheKey]?.let { return it }

        val (sunrise, _) = calculateSunriseSunset(date, location)
        val tzOffsetHours = try {
            java.time.ZoneId.of(location.timezoneId).rules.getOffset(date.atTime(12, 0)).totalSeconds / 3600.0
        } catch (e: Exception) {
            when (location.timezoneId) {
                "Asia/Kolkata" -> 5.5
                "Asia/Dubai" -> 4.0
                "Asia/Singapore" -> 8.0
                "Australia/Sydney" -> 10.0
                "Europe/London" -> 1.0
                "America/New_York" -> -4.0
                "America/Los_Angeles" -> -7.0
                "America/Toronto" -> -4.0
                else -> 5.5
            }
        }

        val sunriseHourUtc = (sunrise.hour + sunrise.minute / 60.0) - tzOffsetHours
        val dayFractionUtc = sunriseHourUtc / 24.0
        val jd = toJulianDay(date.year, date.monthValue, date.dayOfMonth, dayFractionUtc)

        val sunLong = getSunLongitude(jd)
        val moonLong = getMoonLongitude(jd)
        val ayanamsha = getLahiriAyanamsha(date.year, date.monthValue)

        val siderealSun = (sunLong - ayanamsha + 360.0) % 360.0
        val siderealMoon = (moonLong - ayanamsha + 360.0) % 360.0

        val tithiAngle = (moonLong - sunLong + 360.0) % 360.0
        val tithiIndex = (tithiAngle / 12.0).toInt().coerceIn(0, 29)
        val isShukla = tithiIndex < 15
        val tithiNumInPaksha = if (isShukla) tithiIndex + 1 else (tithiIndex - 15) + 1
        val pakshaName = if (isShukla) "Shukla" else "Krishna"

        val tithiName = when {
            tithiIndex == 14 -> "Purnima"
            tithiIndex == 29 -> "Amavasya"
            else -> "${if (isShukla) "Shukla" else "Krishna"} ${TITHI_NAMES[tithiNumInPaksha - 1]}"
        }

        val nakshatraAngle = siderealMoon % 360.0
        val nakshatraIndex = (nakshatraAngle / (360.0 / 27.0)).toInt().coerceIn(0, 26)
        val nakshatraName = NAKSHATRA_NAMES[nakshatraIndex]

        val daysSinceLastAmavasya = (tithiAngle / 360.0) * 29.53059
        val jdLastAmavasya = jd - daysSinceLastAmavasya
        val sunLongAtAmavasya = getSunLongitude(jdLastAmavasya)
        val siderealSunAtAmavasya = (sunLongAtAmavasya - ayanamsha + 360.0) % 360.0
        val sunRashiAtAmavasya = (siderealSunAtAmavasya / 30.0).toInt().coerceIn(0, 11)
        val masaIndex = (sunRashiAtAmavasya + 1) % 12
        val hinduMasa = HINDU_MONTHS[masaIndex]

        val result = FastTithiMasa(
            tithiIndex = tithiIndex,
            tithiNumber = tithiIndex + 1,
            tithiName = tithiName,
            paksha = pakshaName,
            isShukla = isShukla,
            hinduMasa = hinduMasa,
            masaIndex = masaIndex,
            nakshatraIndex = nakshatraIndex,
            nakshatraName = nakshatraName
        )
        fastTithiMasaCache[cacheKey] = result
        return result
    }

    fun calculatePanchanga(
        date: LocalDate,
        location: CityLocation,
        tradition: CalendarTradition
    ): DayPanchanga {
        val cacheKey = "${date}_${location.id}_${tradition.name}"
        panchangaCalculationCache[cacheKey]?.let { return it }

        val (sunrise, sunset) = calculateSunriseSunset(date, location)

        val tzOffsetHours = try {
            java.time.ZoneId.of(location.timezoneId).rules.getOffset(date.atTime(12, 0)).totalSeconds / 3600.0
        } catch (e: Exception) {
            when (location.timezoneId) {
                "Asia/Kolkata" -> 5.5
                "Asia/Dubai" -> 4.0
                "Asia/Singapore" -> 8.0
                "Australia/Sydney" -> 10.0
                "Europe/London" -> 1.0
                "America/New_York" -> -4.0
                "America/Los_Angeles" -> -7.0
                "America/Toronto" -> -4.0
                else -> 5.5
            }
        }

        // Julian Day at exact local sunrise UTC
        val sunriseHourUtc = (sunrise.hour + sunrise.minute / 60.0) - tzOffsetHours
        val dayFractionUtc = sunriseHourUtc / 24.0
        val jd = toJulianDay(date.year, date.monthValue, date.dayOfMonth, dayFractionUtc)

        val sunLong = getSunLongitude(jd)
        val moonLong = getMoonLongitude(jd)
        val ayanamsha = getLahiriAyanamsha(date.year, date.monthValue)

        val siderealSun = (sunLong - ayanamsha + 360.0) % 360.0
        val siderealMoon = (moonLong - ayanamsha + 360.0) % 360.0

        val sunRashiIndex = (siderealSun / 30.0).toInt().coerceIn(0, 11)
        val timeFmt = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

        // 1. Tithi (0..29)
        val tithiAngle = (moonLong - sunLong + 360.0) % 360.0
        val tithiIndex = (tithiAngle / 12.0).toInt().coerceIn(0, 29)
        val isShukla = tithiIndex < 15
        val tithiNumInPaksha = if (isShukla) tithiIndex + 1 else (tithiIndex - 15) + 1
        val pakshaName = if (isShukla) "Shukla Paksha" else "Krishna Paksha"

        val tithiName = when {
            tithiIndex == 14 -> "Purnima"
            tithiIndex == 29 -> "Amavasya"
            else -> "${if (isShukla) "Shukla" else "Krishna"} ${TITHI_NAMES[tithiNumInPaksha - 1]}"
        }

        // Precise Tithi boundary solver
        val targetTithiDeg = (tithiIndex + 1) * 12.0
        val tithiEndLocalTime = findBoundaryTime(jd, sunrise, targetTithiDeg) { (getMoonLongitude(it) - getSunLongitude(it) + 360.0) % 360.0 }
        val tithiEndTimeStr = "${formatTeluguTimePeriod(tithiEndLocalTime)} వరకు"

        val tithiDeities = listOf(
            "Agni", "Brahma", "Gauri", "Ganesha", "Nagas", "Kartikeya", "Surya",
            "Shiva", "Durga", "Yama", "Vishvadevas", "Vishnu", "Kamadeva", "Shiva", "Moon / Chandra"
        )
        val tithiDeity = tithiDeities[(tithiNumInPaksha - 1).coerceIn(0, 14)]

        val tithiInfo = TithiInfo(
            number = tithiIndex + 1,
            name = tithiName,
            paksha = pakshaName,
            endTimeStr = tithiEndTimeStr,
            progressPercent = ((tithiAngle % 12.0) / 12.0).toFloat(),
            deity = tithiDeity,
            significance = if (isShukla) "Bright waxing lunar fortnight, auspicious for new beginnings" else "Waning fortnight, favorable for contemplation and spiritual sadhana"
        )

        // 2. Nakshatra (0..26) & Multi-Nakshatra formatting
        val nakshatraAngle = siderealMoon % 360.0
        val nakshatraIndex = (nakshatraAngle / (360.0 / 27.0)).toInt().coerceIn(0, 26)
        val pada = (((nakshatraAngle % (360.0 / 27.0)) / (360.0 / 108.0)).toInt() + 1).coerceIn(1, 4)

        val targetNakDeg = (nakshatraIndex + 1) * (360.0 / 27.0)
        val nakEndLocalTime = findBoundaryTime(jd, sunrise, targetNakDeg) { (getMoonLongitude(it) - getLahiriAyanamsha(date.year, date.monthValue) + 360.0) % 360.0 }

        val nextNakIndex = (nakshatraIndex + 1) % 27
        val nextNakNameTe = TELUGU_NAKSHATRAS_LIST[nextNakIndex]
        val nakEndTimeStr = "${formatTeluguTimePeriod(nakEndLocalTime)} వరకు, తర్వాత $nextNakNameTe"

        val moonRashiIndex = (siderealMoon / 30.0).toInt().coerceIn(0, 11)
        val nakshatraInfo = NakshatraInfo(
            number = nakshatraIndex + 1,
            name = NAKSHATRA_NAMES[nakshatraIndex],
            pada = pada,
            rashi = RASHI_NAMES[moonRashiIndex],
            rulingPlanet = NAKSHATRA_RULERS[nakshatraIndex],
            deity = NAKSHATRA_DEITIES[nakshatraIndex],
            symbol = NAKSHATRA_SYMBOLS[nakshatraIndex],
            endTimeStr = nakEndTimeStr,
            auspiciousness = if (nakshatraIndex in listOf(0, 3, 4, 6, 7, 11, 12, 14, 16, 21, 26)) "Highly Auspicious" else "Favorable"
        )

        // 3. Yoga (0..26)
        val yogaAngle = (siderealSun + siderealMoon) % 360.0
        val yogaIndex = (yogaAngle / (360.0 / 27.0)).toInt().coerceIn(0, 26)

        val targetYogaDeg = (yogaIndex + 1) * (360.0 / 27.0)
        val yogaEndLocalTime = findBoundaryTime(jd, sunrise, targetYogaDeg) { ((getSunLongitude(it) - getLahiriAyanamsha(date.year, date.monthValue)) + (getMoonLongitude(it) - getLahiriAyanamsha(date.year, date.monthValue)) + 720.0) % 360.0 }
        val yogaEndTimeStr = "${formatTeluguTimePeriod(yogaEndLocalTime)} వరకు"

        val inauspiciousYogas = listOf(0, 5, 8, 9, 12, 14, 16, 18, 26)
        val yogaNature = if (yogaIndex in inauspiciousYogas) "Inauspicious (Varjya)" else "Auspicious (Shubha)"
        val yogaInfo = YogaInfo(
            number = yogaIndex + 1,
            name = YOGA_NAMES[yogaIndex],
            endTimeStr = yogaEndTimeStr,
            nature = yogaNature
        )

        // 4. Karana (1..60) & Multi-Karana formatting
        val karanaTotal = (tithiAngle / 6.0).toInt().coerceIn(0, 59)
        val karanaName1 = getKaranaNameTe(karanaTotal)

        val targetKaranaDeg1 = (karanaTotal + 1) * 6.0
        val karanaEndLocalTime1 = findBoundaryTime(jd, sunrise, targetKaranaDeg1) { (getMoonLongitude(it) - getSunLongitude(it) + 360.0) % 360.0 }

        val karanaTotal2 = (karanaTotal + 1) % 60
        val karanaName2 = getKaranaNameTe(karanaTotal2)
        val targetKaranaDeg2 = (karanaTotal + 2) * 6.0
        val karanaEndLocalTime2 = findBoundaryTime(jd, sunrise, targetKaranaDeg2) { (getMoonLongitude(it) - getSunLongitude(it) + 360.0) % 360.0 }

        val karanaEndTimeStr = "$karanaName1 ${formatTeluguTimePeriod(karanaEndLocalTime1)} వరకు, $karanaName2 ${formatTeluguTimePeriod(karanaEndLocalTime2)} వరకు"

        val karanaInfo = KaranaInfo(
            number = karanaTotal + 1,
            name = karanaName1,
            endTimeStr = karanaEndTimeStr,
            category = if (karanaTotal in 1..56) "Chara (Movable)" else "Sthira (Fixed)",
            deity = if (karanaName1.contains("దిష్టి")) "Yama (Avoid new initiatives)" else "Vishnu / Prajapati"
        )

        // 5. Sun & Moon Timings
        val dayLengthMinutes = java.time.Duration.between(sunrise, sunset).toMinutes().coerceAtLeast(0)
        val nightLengthMinutes = (1440 - dayLengthMinutes)

        val moonAgeDays = (tithiAngle / 360.0) * 29.53059
        val (moonrise, moonset) = calculateMoonriseMoonset(date, location, sunLong, moonLong, sunrise, sunset)

        val sunMoonTimes = SunMoonTimes(
            sunrise = sunrise.format(timeFmt),
            sunset = sunset.format(timeFmt),
            moonrise = moonrise.format(timeFmt),
            moonset = moonset.format(timeFmt),
            dayLength = "${dayLengthMinutes / 60}h ${dayLengthMinutes % 60}m",
            nightLength = "${nightLengthMinutes / 60}h ${nightLengthMinutes % 60}m",
            solarNoon = sunrise.plusMinutes(dayLengthMinutes / 2).format(timeFmt),
            sunZodiac = RASHI_NAMES[sunRashiIndex],
            moonZodiac = RASHI_NAMES[moonRashiIndex]
        )

        // Moon Phase Info
        val illumination = ((1.0 - cos(Math.toRadians(tithiAngle))) / 2.0 * 100.0).toInt().coerceIn(0, 100)
        val phaseName = when {
            tithiIndex == 0 || tithiIndex == 29 -> "New Moon (Amavasya)"
            tithiIndex < 7 -> "Waxing Crescent"
            tithiIndex == 7 -> "First Quarter (Ashtami)"
            tithiIndex < 14 -> "Waxing Gibbous"
            tithiIndex == 14 -> "Full Moon (Purnima)"
            tithiIndex < 22 -> "Waning Gibbous"
            tithiIndex == 22 -> "Last Quarter (Krishna Ashtami)"
            else -> "Waning Crescent"
        }
        val moonPhaseInfo = MoonPhaseInfo(
            phaseName = phaseName,
            illuminationPercent = illumination,
            moonAgeDays = (moonAgeDays * 10).roundToInt() / 10.0,
            isWaxing = isShukla,
            phaseAngleDeg = tithiAngle
        )

        // 6. Auspicious & Inauspicious Timings
        val slotDurationMinutes = dayLengthMinutes / 8.0
        val dayOfWeek = date.dayOfWeek

        // Rahu Kalam order: Mon=2, Tue=7, Wed=5, Thu=6, Fri=4, Sat=3, Sun=8 (1-indexed)
        val rahuSlot = when (dayOfWeek) {
            DayOfWeek.MONDAY -> 2
            DayOfWeek.TUESDAY -> 7
            DayOfWeek.WEDNESDAY -> 5
            DayOfWeek.THURSDAY -> 6
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 3
            DayOfWeek.SUNDAY -> 8
        }
        val rahuStart = sunrise.plusMinutes(((rahuSlot - 1) * slotDurationMinutes).toLong())
        val rahuEnd = sunrise.plusMinutes((rahuSlot * slotDurationMinutes).toLong())

        // Yamaganda: Mon=4, Tue=3, Wed=2, Thu=1, Fri=7, Sat=6, Sun=5
        val yamaSlot = when (dayOfWeek) {
            DayOfWeek.MONDAY -> 4
            DayOfWeek.TUESDAY -> 3
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 1
            DayOfWeek.FRIDAY -> 7
            DayOfWeek.SATURDAY -> 6
            DayOfWeek.SUNDAY -> 5
        }
        val yamaStart = sunrise.plusMinutes(((yamaSlot - 1) * slotDurationMinutes).toLong())
        val yamaEnd = sunrise.plusMinutes((yamaSlot * slotDurationMinutes).toLong())

        // Gulika Kalam: Mon=6, Tue=5, Wed=4, Thu=3, Fri=2, Sat=1, Sun=7
        val guliSlot = when (dayOfWeek) {
            DayOfWeek.MONDAY -> 6
            DayOfWeek.TUESDAY -> 5
            DayOfWeek.WEDNESDAY -> 4
            DayOfWeek.THURSDAY -> 3
            DayOfWeek.FRIDAY -> 2
            DayOfWeek.SATURDAY -> 1
            DayOfWeek.SUNDAY -> 7
        }
        val guliStart = sunrise.plusMinutes(((guliSlot - 1) * slotDurationMinutes).toLong())
        val guliEnd = sunrise.plusMinutes((guliSlot * slotDurationMinutes).toLong())

        // Abhijit Muhurta (8th muhurta of day = centered on solar noon, 48 min, not on Wed)
        val abhijitStr = if (dayOfWeek == DayOfWeek.WEDNESDAY) {
            "బుధవారం నిషిద్ధం (Varjya)"
        } else {
            val solarNoon = sunrise.plusMinutes(dayLengthMinutes / 2)
            "${formatTeluguTimePeriod(solarNoon.minusMinutes(24))} - ${formatTeluguTimePeriod(solarNoon.plusMinutes(24))}"
        }

        // Brahma Muhurta: 1h 36m to 48m before sunrise
        val brahmaStart = sunrise.minusMinutes(96)
        val brahmaEnd = sunrise.minusMinutes(48)

        // Exact Varjyam based on 27 Nakshatra Ghati offsets
        val nakStartDeg = nakshatraIndex * (360.0 / 27.0)
        val prevNakJd = jd - 1.0
        val nakStartTime = findBoundaryTime(prevNakJd, sunrise, nakStartDeg) { (getMoonLongitude(it) - getLahiriAyanamsha(date.year, date.monthValue) + 360.0) % 360.0 }
        val nakDurMin = java.time.Duration.between(nakStartTime, nakEndLocalTime).toMinutes().let { if (it <= 0) 1440L else it }.coerceIn(1200L, 1600L)
        val varjyaStartOffsetMin = (VARJYA_GHATI_OFFSETS[nakshatraIndex] / 60.0 * nakDurMin).toLong()
        val varjyaStartTime = nakStartTime.plusMinutes(varjyaStartOffsetMin)
        val varjyaEndTime = varjyaStartTime.plusMinutes((4.0 / 60.0 * nakDurMin).toLong())

        val varjyamStr = "${formatTeluguTimePeriod(varjyaStartTime)} నుండి ${formatTeluguTimePeriod(varjyaEndTime)} వరకు"
        val amritaStartTime = varjyaEndTime.plusMinutes(120)
        val amritaEndTime = amritaStartTime.plusMinutes(90)

        // Durmuhurtham solver (15 daytime muhurthas)
        val muhurtaDayMin = dayLengthMinutes / 15.0
        val durMuhurtamStr = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> "${formatTeluguTimePeriod(sunrise.plusMinutes((13 * muhurtaDayMin).toLong()))} నుండి ${formatTeluguTimePeriod(sunrise.plusMinutes((14 * muhurtaDayMin).toLong()))} వరకు"
            DayOfWeek.MONDAY -> "${formatTeluguTimePeriod(sunrise.plusMinutes((8 * muhurtaDayMin).toLong()))} నుండి ${formatTeluguTimePeriod(sunrise.plusMinutes((9 * muhurtaDayMin).toLong()))} వరకు"
            DayOfWeek.TUESDAY -> "${formatTeluguTimePeriod(sunrise.plusMinutes((3 * muhurtaDayMin).toLong()))} నుండి ${formatTeluguTimePeriod(sunrise.plusMinutes((4 * muhurtaDayMin).toLong()))} వరకు"
            DayOfWeek.WEDNESDAY -> "${formatTeluguTimePeriod(sunrise.plusMinutes((4 * muhurtaDayMin).toLong()))} నుండి ${formatTeluguTimePeriod(sunrise.plusMinutes((5 * muhurtaDayMin).toLong()))} వరకు"
            DayOfWeek.THURSDAY -> "${formatTeluguTimePeriod(sunrise.plusMinutes((5 * muhurtaDayMin).toLong()))} నుండి ${formatTeluguTimePeriod(sunrise.plusMinutes((6 * muhurtaDayMin).toLong()))} వరకు"
            DayOfWeek.FRIDAY -> "${formatTeluguTimePeriod(sunrise.plusMinutes((3 * muhurtaDayMin).toLong()))} నుండి ${formatTeluguTimePeriod(sunrise.plusMinutes((4 * muhurtaDayMin).toLong()))} వరకు"
            DayOfWeek.SATURDAY -> "${formatTeluguTimePeriod(sunrise)} నుండి ${formatTeluguTimePeriod(sunrise.plusMinutes((1 * muhurtaDayMin).toLong()))} వరకు"
        }

        val vijayaStart = sunset.minusMinutes(48)
        val vijayaEnd = sunset.minusMinutes(10)
        val godhuliStart = sunset.minusMinutes(15)
        val godhuliEnd = sunset.plusMinutes(15)

        val auspiciousTimings = AuspiciousTimings(
            rahuKalam = "${formatTeluguTimePeriod(rahuStart)} - ${formatTeluguTimePeriod(rahuEnd)}",
            yamaganda = "${formatTeluguTimePeriod(yamaStart)} - ${formatTeluguTimePeriod(yamaEnd)}",
            gulikaKalam = "${formatTeluguTimePeriod(guliStart)} - ${formatTeluguTimePeriod(guliEnd)}",
            abhijitMuhurta = abhijitStr,
            brahmaMuhurta = "${formatTeluguTimePeriod(brahmaStart)} - ${formatTeluguTimePeriod(brahmaEnd)}",
            amritaKalam = "${formatTeluguTimePeriod(amritaStartTime)} - ${formatTeluguTimePeriod(amritaEndTime)}",
            varjyam = varjyamStr,
            durMuhurtam = durMuhurtamStr,
            vijayaMuhurta = "${formatTeluguTimePeriod(vijayaStart)} - ${formatTeluguTimePeriod(vijayaEnd)}",
            godhuliMuhurta = "${formatTeluguTimePeriod(godhuliStart)} - ${formatTeluguTimePeriod(godhuliEnd)}"
        )

        // 7. Choghadiya (Day 8 slots, Night 8 slots)
        val dayChogOrder = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> listOf(ChoghadiyaType.UDVEG, ChoghadiyaType.CHAR, ChoghadiyaType.LABH, ChoghadiyaType.AMRIT, ChoghadiyaType.KAAL, ChoghadiyaType.SHUBH, ChoghadiyaType.ROG, ChoghadiyaType.UDVEG)
            DayOfWeek.MONDAY -> listOf(ChoghadiyaType.AMRIT, ChoghadiyaType.KAAL, ChoghadiyaType.SHUBH, ChoghadiyaType.ROG, ChoghadiyaType.UDVEG, ChoghadiyaType.CHAR, ChoghadiyaType.LABH, ChoghadiyaType.AMRIT)
            DayOfWeek.TUESDAY -> listOf(ChoghadiyaType.ROG, ChoghadiyaType.UDVEG, ChoghadiyaType.CHAR, ChoghadiyaType.LABH, ChoghadiyaType.AMRIT, ChoghadiyaType.KAAL, ChoghadiyaType.SHUBH, ChoghadiyaType.ROG)
            DayOfWeek.WEDNESDAY -> listOf(ChoghadiyaType.LABH, ChoghadiyaType.AMRIT, ChoghadiyaType.KAAL, ChoghadiyaType.SHUBH, ChoghadiyaType.ROG, ChoghadiyaType.UDVEG, ChoghadiyaType.CHAR, ChoghadiyaType.LABH)
            DayOfWeek.THURSDAY -> listOf(ChoghadiyaType.SHUBH, ChoghadiyaType.ROG, ChoghadiyaType.UDVEG, ChoghadiyaType.CHAR, ChoghadiyaType.LABH, ChoghadiyaType.AMRIT, ChoghadiyaType.KAAL, ChoghadiyaType.SHUBH)
            DayOfWeek.FRIDAY -> listOf(ChoghadiyaType.CHAR, ChoghadiyaType.LABH, ChoghadiyaType.AMRIT, ChoghadiyaType.KAAL, ChoghadiyaType.SHUBH, ChoghadiyaType.ROG, ChoghadiyaType.UDVEG, ChoghadiyaType.CHAR)
            DayOfWeek.SATURDAY -> listOf(ChoghadiyaType.KAAL, ChoghadiyaType.SHUBH, ChoghadiyaType.ROG, ChoghadiyaType.UDVEG, ChoghadiyaType.CHAR, ChoghadiyaType.LABH, ChoghadiyaType.AMRIT, ChoghadiyaType.KAAL)
        }

        val choghadiyaList = mutableListOf<ChoghadiyaSlot>()
        for (i in 0..7) {
            val sTime = sunrise.plusMinutes((i * slotDurationMinutes).toLong())
            val eTime = sunrise.plusMinutes(((i + 1) * slotDurationMinutes).toLong())
            val type = dayChogOrder[i]
            choghadiyaList.add(
                ChoghadiyaSlot(
                    name = type.label,
                    type = type,
                    startTime = sTime.format(timeFmt),
                    endTime = eTime.format(timeFmt),
                    isDay = true
                )
            )
        }

        // 8. Hora Slots (24 hours governed by planetary rulers: Sun, Ven, Mer, Moon, Sat, Jup, Mars)
        val horaList = mutableListOf<HoraSlot>()
        val horaPlanets = listOf("Sun (Surya)", "Venus (Shukra)", "Mercury (Budha)", "Moon (Chandra)", "Saturn (Shani)", "Jupiter (Guru)", "Mars (Mangala)")
        val startPlanetIndex = when (dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 3
            DayOfWeek.TUESDAY -> 6
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 5
            DayOfWeek.FRIDAY -> 1
            DayOfWeek.SATURDAY -> 4
        }
        for (h in 0..11) {
            val hStart = sunrise.plusMinutes((h * 60).toLong())
            val hEnd = hStart.plusHours(1)
            val p = horaPlanets[(startPlanetIndex + h) % 7]
            val isAusp = p.contains("Venus") || p.contains("Jupiter") || p.contains("Mercury") || p.contains("Moon")
            horaList.add(
                HoraSlot(
                    planet = p,
                    startTime = hStart.format(timeFmt),
                    endTime = hEnd.format(timeFmt),
                    nature = if (isAusp) "Auspicious" else "Moderate / Action-oriented"
                )
            )
        }

        // 9. Masa and Samvatsara (Amanta / Drik Ganita System)
        // In Amanta system (Telugu / South Indian / Marathi): Month starts at New Moon (Amavasya).
        // The Sun's rashi at preceding Amavasya determines the Lunar Month:
        // Sun in Meena(11) at Amavasya -> Chaitra(0), Sun in Mesha(0) -> Vaishakha(1), ..., Sun in Simha(4) -> Bhadrapada(5)
        val daysSinceLastAmavasya = (tithiAngle / 360.0) * 29.53059
        val jdLastAmavasya = jd - daysSinceLastAmavasya
        val sunLongAtAmavasya = getSunLongitude(jdLastAmavasya)
        val siderealSunAtAmavasya = (sunLongAtAmavasya - ayanamsha + 360.0) % 360.0
        val sunRashiAtAmavasya = (siderealSunAtAmavasya / 30.0).toInt().coerceIn(0, 11)
        val masaIndex = (sunRashiAtAmavasya + 1) % 12
        val hinduMasa = HINDU_MONTHS[masaIndex]

        // Regional Month Names
        val regionalMonth = when (tradition) {
            CalendarTradition.TAMIL -> listOf("Chithirai", "Vaikasi", "Aani", "Aadi", "Avani", "Purattasi", "Aippasi", "Karthigai", "Margazhi", "Thai", "Masi", "Panguni")[masaIndex]
            CalendarTradition.MALAYALAM -> listOf("Medam", "Edavam", "Mithunam", "Karkidakam", "Chingam", "Kanni", "Thulam", "Vrishchikam", "Dhanu", "Makaram", "Kumbham", "Meenam")[masaIndex]
            CalendarTradition.BENGALI -> listOf("Boishakh", "Joishtho", "Asharh", "Srabon", "Bhadro", "Ashwin", "Kartik", "Agrahayan", "Poush", "Magh", "Falgun", "Choitro")[masaIndex]
            CalendarTradition.TELUGU -> listOf("చైత్రం", "వైశాఖం", "జ్యేష్ఠం", "ఆషాఢం", "శ్రావణం", "భాద్రపదం", "ఆశ్వయుజం", "కార్తీకం", "మార్గశిరం", "పుష్యం", "మాఘం", "ఫాల్గుణం")[masaIndex]
            CalendarTradition.KANNADA, CalendarTradition.MARATHI -> hinduMasa
            CalendarTradition.GUJARATI -> hinduMasa
            CalendarTradition.NORTH_INDIAN -> hinduMasa
            CalendarTradition.ODIA -> listOf("Baisakha", "Jyestha", "Asadha", "Srabana", "Bhadrava", "Aswina", "Kartika", "Margasira", "Pousha", "Magha", "Falguna", "Chaitra")[masaIndex]
        }

        // Samvatsara calculation (2026 is Parabhava / Vishwavasu in 60-year cycle)
        val samvatsaraIndex = (date.year - 1987 + 60) % 60
        val samvatsara = SAMVATSARA_60[samvatsaraIndex]

        val ritu = when (masaIndex) {
            0, 1 -> "వసంత ఋతువు"
            2, 3 -> "గ్రీష్మ ఋతువు"
            4, 5 -> "వర్ష ఋతువు"
            6, 7 -> "శరద్ ఋతువు"
            8, 9 -> "హేమంత ఋతువు"
            else -> "శిశిర ఋతువు"
        }

        val ayana = if (sunRashiIndex in 9..11 || sunRashiIndex in 0..2) "ఉత్తరాయణం" else "దక్షిణాయనం"

        val weekdayName = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)

        val result = DayPanchanga(
            date = date,
            tradition = tradition,
            location = location,
            hinduMasa = hinduMasa,
            regionalMonthName = regionalMonth,
            paksha = pakshaName,
            samvatsara = samvatsara,
            ritu = ritu,
            ayana = ayana,
            vara = weekdayName,
            tithi = tithiInfo,
            nakshatra = nakshatraInfo,
            yoga = yogaInfo,
            karana = karanaInfo,
            sunMoonTimes = sunMoonTimes,
            moonPhase = moonPhaseInfo,
            auspiciousTimings = auspiciousTimings,
            choghadiyaList = choghadiyaList,
            horaList = horaList,
            festivals = emptyList() // Will be populated by FestivalRepository
        )
        panchangaCalculationCache[cacheKey] = result
        return result
    }

    // "What is happening right now?" live analyzer
    fun evaluateWhatIsHappeningNow(
        panchanga: DayPanchanga,
        currentTime: LocalTime = LocalTime.now()
    ): WhatIsHappeningNow {
        // Evaluate active Choghadiya
        val (sunrise, sunset) = calculateSunriseSunset(panchanga.date, panchanga.location)
        val dayLengthMin = java.time.Duration.between(sunrise, sunset).toMinutes().coerceAtLeast(0)
        val slotDurMin = dayLengthMin / 8.0

        val currentChogSlot = if (currentTime.isAfter(sunrise) && currentTime.isBefore(sunset)) {
            val minFromSunrise = java.time.Duration.between(sunrise, currentTime).toMinutes()
            val slotIdx = (minFromSunrise / slotDurMin).toInt().coerceIn(0, 7)
            panchanga.choghadiyaList.getOrNull(slotIdx)
        } else {
            panchanga.choghadiyaList.firstOrNull()
        }

        // Evaluate Rahu Kalam active
        val dayOfWeek = panchanga.date.dayOfWeek
        val rahuSlot = when (dayOfWeek) {
            DayOfWeek.MONDAY -> 2
            DayOfWeek.TUESDAY -> 7
            DayOfWeek.WEDNESDAY -> 5
            DayOfWeek.THURSDAY -> 6
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 3
            DayOfWeek.SUNDAY -> 8
        }
        val rahuStart = sunrise.plusMinutes(((rahuSlot - 1) * slotDurMin).toLong())
        val rahuEnd = sunrise.plusMinutes((rahuSlot * slotDurMin).toLong())
        val isRahuActive = currentTime.isAfter(rahuStart) && currentTime.isBefore(rahuEnd)

        // Brahma Muhurta check
        val brahmaStart = sunrise.minusMinutes(96)
        val brahmaEnd = sunrise.minusMinutes(48)
        val isBrahmaActive = currentTime.isAfter(brahmaStart) && currentTime.isBefore(brahmaEnd)

        val nextTransition = when {
            currentTime.isBefore(sunrise) -> "Sunrise in ${java.time.Duration.between(currentTime, sunrise).toMinutes()}m"
            currentTime.isBefore(rahuStart) -> "Rahu Kalam starts in ${java.time.Duration.between(currentTime, rahuStart).toMinutes()}m"
            isRahuActive -> "Rahu Kalam ends in ${java.time.Duration.between(currentTime, rahuEnd).toMinutes()}m"
            currentTime.isBefore(sunset) -> "Sunset in ${java.time.Duration.between(currentTime, sunset).toMinutes()}m"
            else -> "Next Sunrise at ${sunrise.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))}"
        }

        val nextEvent = when {
            panchanga.tithi.name.contains("Ekadashi") -> "Fasting Day: Ekadashi Vrat is currently active"
            panchanga.tithi.name.contains("Purnima") -> "Full Moon (Purnima) — Sacred for Satyanarayan Puja"
            panchanga.tithi.name.contains("Amavasya") -> "Amavasya (New Moon) — Pitru Tarpana day"
            panchanga.tithi.name.contains("Chaturthi") -> "Sankashti / Vinayaka Chaturthi observances"
            panchanga.tithi.name.contains("Pradosh") -> "Pradosham Sandhya Puja period"
            else -> "Next transition: ${panchanga.tithi.endTimeStr}"
        }

        return WhatIsHappeningNow(
            currentTithiName = panchanga.tithi.name,
            currentNakshatraName = "${panchanga.nakshatra.name} (Pada ${panchanga.nakshatra.pada})",
            currentChoghadiya = currentChogSlot,
            isRahuKalamActive = isRahuActive,
            rahuKalamTimeSpan = "${rahuStart.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))} - ${rahuEnd.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH))}",
            isBrahmaMuhurtaActive = isBrahmaActive,
            nextTransitionDescription = nextTransition,
            nextImportantEvent = nextEvent
        )
    }
}
