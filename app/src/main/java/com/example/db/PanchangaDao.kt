package com.example.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PanchangaDao {

    // PanchangaDay Queries
    @Query("SELECT * FROM panchanga_days WHERE gregorianDate = :date LIMIT 1")
    suspend fun getPanchangaDay(date: String): PanchangaDayEntity?

    @Query("SELECT * FROM panchanga_days WHERE gregorianDate = :date LIMIT 1")
    fun getPanchangaDayFlow(date: String): Flow<PanchangaDayEntity?>

    @Query("SELECT * FROM panchanga_days WHERE gregorianDate >= :startDate AND gregorianDate <= :endDate ORDER BY gregorianDate ASC")
    suspend fun getPanchangaDaysForRange(startDate: String, endDate: String): List<PanchangaDayEntity>

    @Query("SELECT COUNT(*) FROM panchanga_days WHERE gregorianDate LIKE :yearPattern")
    suspend fun getLoadedDayCountForYear(yearPattern: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPanchangaDays(days: List<PanchangaDayEntity>)

    // Planetary Longitudes
    @Query("SELECT * FROM planetary_longitudes WHERE date = :date")
    suspend fun getPlanetaryPositionsForDate(date: String): List<PlanetaryLongitudeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanetaryLongitudes(planets: List<PlanetaryLongitudeEntity>)

    // Festivals & Important Tithis
    @Query("SELECT * FROM festivals WHERE date = :date ORDER BY name ASC")
    suspend fun getFestivalsForDate(date: String): List<FestivalEntity>

    @Query("SELECT * FROM festivals WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getFestivalsForRange(startDate: String, endDate: String): List<FestivalEntity>

    @Query("SELECT * FROM festivals WHERE category = :category ORDER BY date ASC")
    suspend fun getFestivalsByCategory(category: String): List<FestivalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFestivals(festivals: List<FestivalEntity>)

    // Transits & Retrogrades
    @Query("SELECT * FROM rashi_transits WHERE date LIKE :yearPattern ORDER BY date ASC")
    suspend fun getRashiTransitsForYear(yearPattern: String): List<RashiTransitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRashiTransits(transits: List<RashiTransitEntity>)

    @Query("SELECT * FROM retrograde_events WHERE date LIKE :yearPattern ORDER BY date ASC")
    suspend fun getRetrogradeEventsForYear(yearPattern: String): List<RetrogradeEventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetrogradeEvents(events: List<RetrogradeEventEntity>)

    // Eclipses
    @Query("SELECT * FROM eclipses WHERE date LIKE :yearPattern ORDER BY date ASC")
    suspend fun getEclipsesForYear(yearPattern: String): List<EclipseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEclipses(eclipses: List<EclipseEntity>)

    // Source Documents & Audits
    @Query("SELECT * FROM dataset_audits WHERE id = :id LIMIT 1")
    suspend fun getDatasetAudit(id: String): DatasetAuditEntity?

    @Query("SELECT * FROM dataset_audits")
    suspend fun getAllDatasetAudits(): List<DatasetAuditEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDatasetAudit(audit: DatasetAuditEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSourceDocument(doc: SourceDocumentEntity)

    // Location Calculation Cache
    @Query("SELECT * FROM location_calculations WHERE date = :date AND ABS(latitude - :lat) < 0.01 AND ABS(longitude - :lon) < 0.01 LIMIT 1")
    suspend fun getLocationCalculation(date: String, lat: Double, lon: Double): LocationCalculationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationCalculation(calc: LocationCalculationEntity)

    // Horoscope Content
    @Query("SELECT * FROM horoscope_contents WHERE periodType = :periodType AND dateOrPeriod = :dateOrPeriod AND rashi = :rashi LIMIT 1")
    suspend fun getHoroscopeContent(periodType: String, dateOrPeriod: String, rashi: String): HoroscopeContentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoroscopeContents(contents: List<HoroscopeContentEntity>)
}
