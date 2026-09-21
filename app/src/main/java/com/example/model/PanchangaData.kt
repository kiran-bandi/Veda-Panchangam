package com.example.model

import java.time.LocalDate
import java.time.LocalTime

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    EN("en", "English", "English"),
    HI("hi", "Hindi", "हिन्दी"),
    TE("te", "Telugu", "తెలుగు"),
    TA("ta", "Tamil", "தமிழ்"),
    KN("kn", "Kannada", "ಕನ್ನಡ"),
    MR("mr", "Marathi", "मराठी"),
    GU("gu", "Gujarati", "ગુજરાતી"),
    BN("bn", "Bengali", "বাংলা"),
    ML("ml", "Malayalam", "മലയാളം"),
    OR_LANG("or", "Odia", "ଓଡ଼ିଆ")
}

enum class AppThemeMode(val titleEn: String, val titleTe: String, val icon: String) {
    SACRED_SAFFRON("Sacred Saffron", "సాఫ్రాన్ (స్వర్ణ శోభ)", "🟧"),
    COSMIC_TWILIGHT("Cosmic Twilight (Dark)", "కాల భైరవ రాత్రి (డార్క్)", "🌌"),
    ROYAL_GOLD("Royal Temple Gold", "రాజ దేవాలయ మార్గం", "👑"),
    DIVINE_BLUE("Divine Ocean Blue", "దివ్య విష్ణు నీలం", "💙"),
    SYSTEM("System Default", "సిస్టమ్ మోడ్", "📱")
}

enum class CalendarTradition(val id: String, val title: String, val regionDescription: String) {
    TELUGU("te_panchang", "Telugu Panchangam", "Amanta Chandramana (Andhra & Telangana)"),
    TAMIL("ta_panchang", "Tamil Panchangam", "Sauramana / Tirukanitha (Tamil Nadu)"),
    KANNADA("kn_panchang", "Kannada Panchanga", "Amanta Chandramana (Karnataka)"),
    NORTH_INDIAN("north_panchang", "North Indian Calendar", "Purnimanta (Varanasi / Delhi / UP / Bihar)"),
    MARATHI("mr_panchang", "Marathi Panchang", "Amanta Chandramana (Maharashtra)"),
    GUJARATI("gu_panchang", "Gujarati Panchang", "Amanta (Kartika New Year / Gujarat)"),
    MALAYALAM("ml_panchang", "Malayalam Panchangam", "Kollavarsham Solar (Kerala)"),
    BENGALI("bn_panchang", "Bengali Panjika", "Surya Siddhanta Solar (Bengal & Tripura)"),
    ODIA("or_panchang", "Odia Panji", "Sauramana / Chandramana (Odisha)")
}

data class CityLocation(
    val id: String,
    val name: String,
    val stateOrCountry: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String = "Asia/Kolkata",
    val isDiaspora: Boolean = false
)

data class TithiInfo(
    val number: Int,
    val name: String,
    val paksha: String, // Shukla or Krishna
    val endTimeStr: String,
    val progressPercent: Float,
    val deity: String,
    val significance: String
)

data class NakshatraInfo(
    val number: Int,
    val name: String,
    val pada: Int,
    val rashi: String,
    val rulingPlanet: String,
    val deity: String,
    val symbol: String,
    val endTimeStr: String,
    val auspiciousness: String
)

data class YogaInfo(
    val number: Int,
    val name: String,
    val endTimeStr: String,
    val nature: String // Auspicious, Inauspicious, Neutral
)

data class KaranaInfo(
    val number: Int,
    val name: String,
    val endTimeStr: String,
    val category: String, // Chara or Sthira
    val deity: String
)

data class SunMoonTimes(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val dayLength: String,
    val nightLength: String,
    val solarNoon: String,
    val sunZodiac: String,
    val moonZodiac: String
)

data class MoonPhaseInfo(
    val phaseName: String,
    val illuminationPercent: Int,
    val moonAgeDays: Double,
    val isWaxing: Boolean,
    val phaseAngleDeg: Double
)

data class AuspiciousTimings(
    val rahuKalam: String,
    val yamaganda: String,
    val gulikaKalam: String,
    val abhijitMuhurta: String,
    val brahmaMuhurta: String,
    val amritaKalam: String,
    val varjyam: String,
    val durMuhurtam: String,
    val vijayaMuhurta: String = "",
    val godhuliMuhurta: String = ""
)

enum class ChoghadiyaType(val label: String, val isAuspicious: Boolean, val nature: String) {
    AMRIT("Amrit", true, "Best for all auspicious tasks"),
    SHUBH("Shubh", true, "Good for ceremonies & religious works"),
    LABH("Labh", true, "Favorable for commerce & education"),
    CHAR("Char", true, "Beneficial for travel & motion"),
    ROG("Rog", false, "Inauspicious, avoid medical start"),
    KAAL("Kaal", false, "Inauspicious, governed by Saturn"),
    UDVEG("Udveg", false, "Restless period, governed by Sun")
}

data class ChoghadiyaSlot(
    val name: String,
    val type: ChoghadiyaType,
    val startTime: String,
    val endTime: String,
    val isDay: Boolean,
    val isCurrentlyActive: Boolean = false
)

data class HoraSlot(
    val planet: String,
    val startTime: String,
    val endTime: String,
    val nature: String,
    val isCurrentlyActive: Boolean = false
)

enum class FestivalCategory(val label: String) {
    ALL("All"),
    MAJOR_FESTIVALS("Major Festivals"),
    VRAT_FASTING("Vrats & Fasting"),
    VRATHAMS("Vrats & Fasting"),
    EKADASHI("Ekadashi"),
    POURNAMI("Pournami"),
    AMAVASYA("Amavasya"),
    JAYANTI("Jayanti"),
    REGIONAL("Regional"),
    GOVERNMENT_HOLIDAYS("Government Holidays")
}

data class FestivalItem(
    val id: String,
    val name: String,
    val date: LocalDate,
    val category: FestivalCategory,
    val tradition: CalendarTradition? = null,
    val deity: String,
    val summary: String,
    val significance: String,
    val historicalContext: String? = null,
    val pujaMuhurta: String? = null,
    val paranaTime: String? = null,
    val fastingRule: String? = null,
    val mantra: String? = null,
    val story: String? = null,
    val iconEmoji: String = "🪔"
)

enum class MuhurthaCategory(val title: String, val icon: String, val description: String) {
    MARRIAGE("Vivah (Marriage)", "💍", "Most auspicious conjunctions for wedding nuptials"),
    GRIHA_PRAVESH("Griha Pravesh (House Warming)", "🏡", "Entering a new home under planetary protection"),
    VEHICLE("Vahan Kharidi (Vehicle Purchase)", "🚗", "Auspicious planetary alignment for vehicle purchase"),
    PROPERTY("Bhoomi Puja & Property", "🏛️", "Land purchase, foundation laying, and property deals"),
    BUSINESS("Vyapar Aarambh (Business Opening)", "💼", "Inauguration, opening shops, and new ventures"),
    NAMAKARAN("Namakaran (Naming Ceremony)", "👶", "Sacred naming for newborn infants"),
    ANNAPRASHANA("Annaprashana (First Solid Food)", "🥣", "Child's initial consecrated food intake"),
    UPANAYANA("Upanayana (Sacred Thread)", "🕉️", "Vedic initiation and Gayatri Upadesha"),
    AKSHARABHYASAM("Aksharabhyasam (First Learning)", "📚", "Initiation into alphabets, Saraswati blessings"),
    TRAVEL("Yatra (Auspicious Travel)", "✈️", "Long distance journeys free of Disha Shoola")
}

data class MuhurthaResult(
    val date: LocalDate,
    val dayOfWeek: String,
    val purpose: MuhurthaCategory,
    val timeWindow: String,
    val rating: String, // "Highly Auspicious", "Auspicious", "Good"
    val tithi: String,
    val nakshatra: String,
    val favorablePoints: List<String>,
    val cautions: List<String> = emptyList()
)

data class SpiritualItem(
    val id: String,
    val title: String,
    val category: String, // Mantra, Stotra, Aarti, Chalisa, Puja Vidhi
    val deity: String,
    val verses: List<String>,
    val englishMeaning: String,
    val benefits: String,
    val audioDurationSeconds: Int = 180
)

data class FamilyEvent(
    val id: String,
    val title: String,
    val personName: String,
    val eventType: String, // Birthday, Anniversary, Shraddha, Vrat Reminder
    val date: LocalDate,
    val tithiNotes: String = "",
    val reminderHour: Int = 8
)

data class DayPanchanga(
    val date: LocalDate,
    val tradition: CalendarTradition,
    val location: CityLocation,
    val hinduMasa: String,
    val regionalMonthName: String,
    val paksha: String,
    val samvatsara: String,
    val ritu: String,
    val ayana: String,
    val vara: String,
    val tithi: TithiInfo,
    val nakshatra: NakshatraInfo,
    val yoga: YogaInfo,
    val karana: KaranaInfo,
    val sunMoonTimes: SunMoonTimes,
    val moonPhase: MoonPhaseInfo,
    val auspiciousTimings: AuspiciousTimings,
    val choghadiyaList: List<ChoghadiyaSlot>,
    val horaList: List<HoraSlot>,
    val festivals: List<FestivalItem>
) {
    val sunRashi: String get() = sunMoonTimes.sunZodiac
    val moonRashi: String get() = sunMoonTimes.moonZodiac
    val shakaSamvat: Int get() = date.year - 78
    val vikramSamvat: Int get() = date.year + 57
}

data class WhatIsHappeningNow(
    val currentTithiName: String,
    val currentNakshatraName: String,
    val currentChoghadiya: ChoghadiyaSlot?,
    val isRahuKalamActive: Boolean,
    val rahuKalamTimeSpan: String,
    val isBrahmaMuhurtaActive: Boolean,
    val nextTransitionDescription: String,
    val nextImportantEvent: String
)

enum class Rashi(
    val englishName: String,
    val teluguName: String,
    val emoji: String,
    val planet: String,
    val element: String,
    val syllables: String
) {
    MESHA("Mesha (Aries)", "మేష రాశి", "♈", "Mars", "Fire", "A, L, I"),
    VRISHABHA("Vrishabha (Taurus)", "వృషభ రాశి", "♉", "Venus", "Earth", "U, O, Va, Vi"),
    MITHUNA("Mithuna (Gemini)", "మిథున రాశి", "♊", "Mercury", "Air", "Ka, Ki, Ku, Gha"),
    KARKA("Karka (Cancer)", "కర్కాటక రాశి", "♋", "Moon", "Water", "Hi, Hu, He, Ho"),
    SIMHA("Simha (Leo)", "సింహ రాశి", "♌", "Sun", "Fire", "Ma, Mi, Mu, Me"),
    KANYA("Kanya (Virgo)", "కన్యా రాశి", "♍", "Mercury", "Earth", "To, Pa, Pi, Pu"),
    TULA("Tula (Libra)", "తులా రాశి", "♎", "Venus", "Air", "Ra, Ri, Ru, Re"),
    VRISHCHIKA("Vrishchika (Scorpio)", "వృశ్చిక రాశి", "♏", "Mars", "Water", "To, Na, Ni, Nu"),
    DHANUS("Dhanus (Sagittarius)", "ధనుస్సు రాశి", "♐", "Jupiter", "Fire", "Ye, Yo, Bha, Bhi"),
    MAKARA("Makara (Capricorn)", "మకర రాశి", "♑", "Saturn", "Earth", "Bho, Ja, Ji, Khi"),
    KUMBHA("Kumbha (Aquarius)", "కుంభ రాశి", "♒", "Saturn", "Air", "Go, Ge, Go, Sa"),
    MEENA("Meena (Pisces)", "మీన రాశి", "♓", "Jupiter", "Water", "Di, Du, Tha, Jha")
}

data class PlanetTransitInfo(
    val id: String,
    val nameTe: String,
    val shortNameTe: String,
    val symbol: String,
    val siderealLongitude: Double,
    val rashiIndex: Int,
    val rashiNameTe: String,
    val degreeInSign: Double,
    val degreeStrTe: String,
    val nakshatraNumber: Int,
    val nakshatraNameTe: String,
    val pada: Int,
    val isRetrograde: Boolean,
    val statusTe: String
)

data class RashiChakraData(
    val date: LocalDate,
    val location: CityLocation,
    val planets: List<PlanetTransitInfo>,
    val lagna: PlanetTransitInfo,
    val ayanamshaName: String = "లాహిరి అయనాంశం (Chitrapaksha)",
    val zodiacSystem: String = "నిరయణ విధానం (Sidereal Zodiac)"
)

data class DataProvenance(
    val sourceName: String,
    val sourceVolume: String?,
    val sourcePage: Int?,
    val calculationMethod: String,
    val ayanamshaMethod: String?,
    val referenceLocation: String?,
    val verified: Boolean
)

enum class DatasetCoverageStatus(val labelTe: String) {
    COMPLETE_365_DAYS("సంపూర్ణ నమోదు (365 రోజులు)"),
    PARTIAL_DATA_REQUIRED("పాక్షిక నమోదు - 1947 శక సంపుటి డేటా అవసరం"),
    UNAVAILABLE("డేటా లభ్యం కాలేదు")
}

data class DatasetAuditStatus(
    val year: Int,
    val totalDatesExpected: Int,
    val totalDatesLoaded: Int,
    val coverageStatus: DatasetCoverageStatus,
    val missingRangeDescription: String?,
    val requiredFileName: String?,
    val duplicateCount: Int = 0
)

data class PanchangValidationDiff(
    val date: LocalDate,
    val fieldName: String,
    val calculatedValue: String,
    val referenceValue: String,
    val differenceMinutes: Long?,
    val explanation: String,
    val referenceLocation: String = "కేంద్ర వేధశాల (IST 82°30'E, 23°11'N)",
    val calculationLocation: String = "హైదరాబాద్ (17°22'N, 78°28'E)",
    val referenceMethod: String = "రాష్ట్రీయ పంచాంగ్ (IMD / Positional Astronomy Centre)",
    val calculationMethod: String = "దృక్ గణిత లాహిరి పద్ధతి",
    val referenceAyanamsha: String = "రాష్ట్రీయ పంచాంగ్ అయనాంశం",
    val calculationAyanamsha: String = "లాహిరి (చిత్రపక్ష) అయనాంశం"
)

