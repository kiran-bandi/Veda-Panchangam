package com.example.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "panchanga_days",
    indices = [
        Index(value = ["gregorianDate"], unique = true),
        Index(value = ["sakaYear", "sakaMonth"]),
        Index(value = ["sourceVolume"])
    ]
)
data class PanchangaDayEntity(
    @PrimaryKey val id: String,
    val gregorianDate: String, // YYYY-MM-DD
    val weekday: String,
    val sakaYear: Int,
    val sakaMonth: String,
    val sakaDay: Int,
    val kaliYear: Int,
    val lunarMonth: String,
    val paksha: String,
    val tithi: String,
    val tithiStart: String? = null,
    val tithiEnd: String? = null,
    val nakshatra: String,
    val nakshatraStart: String? = null,
    val nakshatraEnd: String? = null,
    val yoga: String,
    val yogaStart: String? = null,
    val yogaEnd: String? = null,
    val karana: String,
    val karanaStart: String? = null,
    val karanaEnd: String? = null,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moonAge: Double = 0.0,
    val sunDeclination: Double = 0.0,
    val siderealTime: String = "",
    val equationOfTime: String = "",
    val lagna: String = "",
    val midday: String = "",
    val sourceVolume: String,
    val sourceFile: String,
    val sourcePage: Int,
    val calculationMethod: String,
    val ayanamshaMethod: String,
    val referenceLocation: String = "Central Station (82°30'E, 23°11'N)",
    val verified: Boolean = true
)

@Entity(
    tableName = "planetary_longitudes",
    indices = [
        Index(value = ["date", "planet"]),
        Index(value = ["date"])
    ]
)
data class PlanetaryLongitudeEntity(
    @PrimaryKey val id: String,
    val date: String,
    val planet: String, // సూర్యుడు, చంద్రుడు, కుజుడు, బుధుడు, గురుడు, శుక్రుడు, శని, రాహువు, కేతువు
    val nirayanaLongitude: Double,
    val rashi: String,
    val degree: Int,
    val minute: Int,
    val second: Int,
    val nakshatra: String,
    val pada: Int,
    val retrograde: Boolean,
    val sourceVolume: String,
    val sourcePage: Int,
    val calculationMethod: String,
    val ayanamshaMethod: String,
    val verified: Boolean = true
)

@Entity(
    tableName = "rashi_transits",
    indices = [Index(value = ["date"]), Index(value = ["planet"])]
)
data class RashiTransitEntity(
    @PrimaryKey val id: String,
    val planet: String,
    val fromRashi: String,
    val toRashi: String,
    val date: String,
    val time: String,
    val source: String,
    val sourceVolume: String,
    val sourcePage: Int,
    val methodology: String
)

@Entity(
    tableName = "nakshatra_transits",
    indices = [Index(value = ["date"]), Index(value = ["planet"])]
)
data class NakshatraTransitEntity(
    @PrimaryKey val id: String,
    val planet: String,
    val nakshatra: String,
    val date: String,
    val time: String,
    val source: String,
    val sourceVolume: String,
    val sourcePage: Int,
    val methodology: String
)

@Entity(
    tableName = "retrograde_events",
    indices = [Index(value = ["date"]), Index(value = ["planet"])]
)
data class RetrogradeEventEntity(
    @PrimaryKey val id: String,
    val planet: String,
    val eventType: String, // వక్రగతి, మార్గి
    val date: String,
    val time: String,
    val source: String,
    val sourceVolume: String,
    val sourcePage: Int,
    val methodology: String
)

@Entity(
    tableName = "eclipses",
    indices = [Index(value = ["date"])]
)
data class EclipseEntity(
    @PrimaryKey val id: String,
    val date: String,
    val type: String, // సూర్యగ్రహణం, చంద్రగ్రహణం
    val startTime: String,
    val maximumTime: String,
    val endTime: String,
    val magnitude: String,
    val visibility: String,
    val regions: String,
    val source: String,
    val sourceVolume: String,
    val sourcePage: Int,
    val methodology: String
)

@Entity(
    tableName = "festivals",
    indices = [
        Index(value = ["date"]),
        Index(value = ["category"]),
        Index(value = ["sakaYear", "sakaMonth"])
    ]
)
data class FestivalEntity(
    @PrimaryKey val id: String,
    val date: String,
    val name: String,
    val category: String, // పండుగ, వ్రతం, ఏకాదశి, పౌర్ణమి, అమావాస్య, సంక్రాంతి, ప్రదోషం, సంకష్టి చతుర్థి, జయంతి, సెలవు, ఇతరాలు
    val type: String,
    val startTime: String? = null,
    val endTime: String? = null,
    val description: String,
    val fasting: String? = null,
    val parana: String? = null,
    val locationScope: String = "All Andhra & Telangana",
    val tradition: String = "Amanta Chandramana",
    val sakaYear: Int = 1948,
    val sakaMonth: String = "",
    val source: String = "Rashtriya Panchang",
    val sourceVolume: String = "Saka 1948",
    val sourcePage: Int = 1,
    val calculationMethod: String = "Government Ephemeris",
    val verified: Boolean = true
)

@Entity(tableName = "source_documents")
data class SourceDocumentEntity(
    @PrimaryKey val id: String,
    val sourceName: String,
    val publisher: String,
    val governmentSource: String,
    val fileName: String,
    val volume: String,
    val sakaYear: Int,
    val coverageStart: String,
    val coverageEnd: String,
    val methodology: String,
    val notes: String,
    val importedAt: Long,
    val version: String
)

@Entity(tableName = "dataset_audits")
data class DatasetAuditEntity(
    @PrimaryKey val id: String,
    val datasetName: String,
    val year: Int,
    val expectedRecords: Int,
    val loadedRecords: Int,
    val missingRecords: Int,
    val duplicateRecords: Int,
    val coverageStart: String,
    val coverageEnd: String,
    val status: String, // COMPLETE, PARTIAL, DATA_REQUIRED, INVALID
    val lastValidatedAt: Long
)

@Entity(
    tableName = "location_calculations",
    indices = [Index(value = ["date", "latitude", "longitude", "timezone"], unique = true)]
)
data class LocationCalculationEntity(
    @PrimaryKey val id: String,
    val date: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val locationName: String,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val rahuKalamStart: String,
    val rahuKalamEnd: String,
    val yamagandamStart: String,
    val yamagandamEnd: String,
    val gulikaStart: String,
    val gulikaEnd: String,
    val abhijitStart: String,
    val abhijitEnd: String,
    val calculationMethod: String,
    val ayanamshaMethod: String,
    val engineVersion: String
)

@Entity(
    tableName = "horoscope_contents",
    indices = [Index(value = ["periodType", "dateOrPeriod", "rashi"])]
)
data class HoroscopeContentEntity(
    @PrimaryKey val id: String,
    val periodType: String, // DAILY, WEEKLY, MONTHLY, YEARLY
    val dateOrPeriod: String,
    val rashi: String,
    val content: String,
    val source: String,
    val language: String = "te",
    val verified: Boolean = true,
    val copyrightStatus: String = "Public Traditional / Sourced"
)
