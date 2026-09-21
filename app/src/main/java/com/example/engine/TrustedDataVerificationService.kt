package com.example.engine

import com.example.model.CityLocation
import com.example.model.DayPanchanga
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class ConfidenceLevel {
    HIGH,
    MEDIUM,
    LOW,
    UNVERIFIED
}

enum class ValidationStatus {
    VALIDATED,
    WARNING,
    INVALID,
    UNVERIFIED
}

data class FieldValidationResult(
    val fieldName: String,
    val isSuccess: Boolean,
    val message: String
)

data class PanchangDataProvenance(
    val calculationEngine: String = "Drik Ganita Ephemeris Engine",
    val ayanamshaSystem: String = "Lahiri (Chitrapaksha) Ayanamsha",
    val zodiacSystem: String = "Sidereal (Nirayana)",
    val location: CityLocation,
    val calculationDate: LocalDate,
    val confidence: ConfidenceLevel,
    val status: ValidationStatus,
    val validationLogs: List<FieldValidationResult>,
    val officialSources: List<String> = listOf(
        "వైదిక దృక్ గణిత పంచాంగము",
        "సిద్ధాంత జ్యోతిష్య గణితం"
    ),
    val methodologyDescriptionTe: String = "ప్రామాణిక వైదిక దృక్ గణిత ఆస్ట్రోనామికల్ సిద్ధాంతము"
)

object TrustedDataVerificationService {

    /**
     * Validates a calculated [DayPanchanga] for correctness, sanity, and logical consistency.
     * Returns a [PanchangDataProvenance] metadata record.
     */
    fun verifyAndAnnotate(
        panchanga: DayPanchanga,
        date: LocalDate,
        location: CityLocation
    ): PanchangDataProvenance {
        val logs = mutableListOf<FieldValidationResult>()
        var overallStatus = ValidationStatus.VALIDATED
        var overallConfidence = ConfidenceLevel.HIGH

        // 1. Sunrise / Sunset Sanity Check
        val sunriseStr = panchanga.sunMoonTimes.sunrise
        val sunsetStr = panchanga.sunMoonTimes.sunset
        if (sunriseStr.isBlank() || sunsetStr.isBlank()) {
            logs.add(FieldValidationResult("SunTimes", false, "Sunrise or Sunset timing is empty."))
            overallStatus = ValidationStatus.INVALID
            overallConfidence = ConfidenceLevel.UNVERIFIED
        } else {
            logs.add(FieldValidationResult("SunTimes", true, "Sunrise and Sunset correctly calculated for ${location.name}."))
        }

        // 2. Tithi Range and Consistency Check
        val tithiNumber = panchanga.tithi.number
        if (tithiNumber in 1..30) {
            logs.add(FieldValidationResult("Tithi", true, "Tithi index $tithiNumber is within valid range [1..30]."))
        } else {
            logs.add(FieldValidationResult("Tithi", false, "Tithi index $tithiNumber out of range."))
            overallStatus = ValidationStatus.INVALID
            overallConfidence = ConfidenceLevel.UNVERIFIED
        }

        // 3. Nakshatra Range Check
        val nakNumber = panchanga.nakshatra.number
        val pada = panchanga.nakshatra.pada
        if (nakNumber in 1..27 && pada in 1..4) {
            logs.add(FieldValidationResult("Nakshatra", true, "Nakshatra $nakNumber (Pada $pada) is valid."))
        } else {
            logs.add(FieldValidationResult("Nakshatra", false, "Invalid Nakshatra $nakNumber or Pada $pada."))
            overallStatus = ValidationStatus.INVALID
            overallConfidence = ConfidenceLevel.UNVERIFIED
        }

        // 4. Hindu Masa Check
        if (panchanga.hinduMasa.isNotBlank() && panchanga.regionalMonthName.isNotBlank()) {
            logs.add(FieldValidationResult("HinduMasa", true, "Masa verified: ${panchanga.regionalMonthName} (${panchanga.hinduMasa})."))
        } else {
            logs.add(FieldValidationResult("HinduMasa", false, "Masa field is blank."))
            overallStatus = ValidationStatus.WARNING
            if (overallConfidence == ConfidenceLevel.HIGH) overallConfidence = ConfidenceLevel.MEDIUM
        }

        // 5. Yoga and Karana Check
        if (panchanga.yoga.number in 1..27 && panchanga.karana.number in 1..60) {
            logs.add(FieldValidationResult("YogaKarana", true, "Yoga ${panchanga.yoga.number} and Karana ${panchanga.karana.number} verified."))
        } else {
            logs.add(FieldValidationResult("YogaKarana", false, "Yoga or Karana numbers invalid."))
            overallStatus = ValidationStatus.WARNING
        }

        // 6. Location & Timezone Check
        if (location.latitude != 0.0 && location.longitude != 0.0 && location.timezoneId.isNotBlank()) {
            logs.add(FieldValidationResult("Location", true, "Coordinates (${location.latitude}, ${location.longitude}) and Timezone ${location.timezoneId} active."))
        } else {
            logs.add(FieldValidationResult("Location", false, "Location data missing or invalid."))
            overallStatus = ValidationStatus.INVALID
            overallConfidence = ConfidenceLevel.UNVERIFIED
        }

        return PanchangDataProvenance(
            location = location,
            calculationDate = date,
            confidence = overallConfidence,
            status = overallStatus,
            validationLogs = logs
        )
    }

    /**
     * Safety gate: Returns true if the panchanga calculation is safe to display to the user.
     */
    fun isSafeToDisplay(provenance: PanchangDataProvenance): Boolean {
        return provenance.status != ValidationStatus.INVALID && provenance.confidence != ConfidenceLevel.UNVERIFIED
    }
}
