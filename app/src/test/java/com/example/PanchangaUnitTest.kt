package com.example

import com.example.engine.AstronomicalEngine
import com.example.engine.FestivalRepository
import com.example.engine.LocalizationEngine
import com.example.engine.MuhurthaEngine
import com.example.model.AppLanguage
import com.example.model.CalendarTradition
import com.example.model.MuhurthaCategory
import org.junit.Assert.*
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class PanchangaUnitTest {

    @Test
    fun testPanchangaCalculation() {
        val date = LocalDate.of(2026, 9, 14)
        val city = AstronomicalEngine.CITIES.first()
        val tradition = CalendarTradition.TELUGU

        val panchanga = AstronomicalEngine.calculatePanchanga(date, city, tradition)

        assertNotNull(panchanga)
        assertNotNull(panchanga.tithi)
        assertNotNull(panchanga.nakshatra)
        assertNotNull(panchanga.yoga)
        assertNotNull(panchanga.karana)
        assertEquals("Monday", panchanga.vara)
        assertTrue(panchanga.choghadiyaList.size == 8)
        assertNotNull(panchanga.auspiciousTimings.rahuKalam)
        assertNotNull(panchanga.auspiciousTimings.abhijitMuhurta)
    }

    @Test
    fun testFestivalLookup() {
        val year2026Festivals = FestivalRepository.getFestivalsForYear(2026)
        assertTrue("Expected rich collection of 2026 festivals", year2026Festivals.isNotEmpty())

        val diwali = year2026Festivals.find { it.id.contains("deepavali") || it.name.contains("దీపావళి") }
        assertNotNull(diwali)
        assertTrue(diwali?.deity?.contains("మహాలక్ష్మి") == true)
    }

    @Test
    fun testMuhurthaFinder() {
        val date = LocalDate.of(2026, 9, 14)
        val city = AstronomicalEngine.CITIES.first()
        val results = MuhurthaEngine.findMuhurthas(
            category = MuhurthaCategory.MARRIAGE,
            startDate = date,
            monthsRange = 3,
            location = city,
            tradition = CalendarTradition.TELUGU
        )

        assertNotNull(results)
        results.forEach { res ->
            assertTrue(res.favorablePoints.isNotEmpty())
            assertTrue(res.cautions.isNotEmpty())
            assertTrue(res.timeWindow.isNotBlank())
        }
    }

    @Test
    fun testTeluguLocalization() {
        assertEquals("వేద పంచాంగం", LocalizationEngine.get("app_title", AppLanguage.TE))
        assertEquals("తిథి", LocalizationEngine.get("tithi", AppLanguage.TE))
        assertEquals("నక్షత్రం", LocalizationEngine.get("nakshatra", AppLanguage.TE))
        assertEquals("రాహు కాలం", LocalizationEngine.get("rahu_kalam", AppLanguage.TE))

        val localizedTithi = LocalizationEngine.translateTithi("Shukla Tritiya", AppLanguage.TE)
        assertTrue(localizedTithi.contains("శుక్ల"))
        assertTrue(localizedTithi.contains("తదియ") || localizedTithi.contains("త్రితీయ"))

        val localizedVara = LocalizationEngine.translateVara(DayOfWeek.MONDAY, AppLanguage.TE)
        assertEquals("సోమవారం", localizedVara)

        val localizedDate = LocalizationEngine.formatLocalizedDate(LocalDate.of(2026, 9, 14), AppLanguage.TE)
        assertTrue(localizedDate.contains("సెప్టెంబరు") || localizedDate.contains("14"))
    }

    @Test
    fun testHindiLocalization() {
        assertEquals("वैदिक पंचांग", LocalizationEngine.get("app_title", AppLanguage.HI))
        assertEquals("तिथि", LocalizationEngine.get("tithi", AppLanguage.HI))
        assertEquals("नक्षत्र", LocalizationEngine.get("nakshatra", AppLanguage.HI))
        assertEquals("राहु काल", LocalizationEngine.get("rahu_kalam", AppLanguage.HI))

        val localizedNakshatra = LocalizationEngine.translateNakshatra("Rohini", AppLanguage.HI)
        assertEquals("रोहिणी", localizedNakshatra)

        val localizedVara = LocalizationEngine.translateVara(DayOfWeek.SUNDAY, AppLanguage.HI)
        assertEquals("रविवार", localizedVara)
    }

    @Test
    fun testTamilLocalization() {
        assertEquals("வேத பஞ்சாங்கம்", LocalizationEngine.get("app_title", AppLanguage.TA))
        assertEquals("திதி", LocalizationEngine.get("tithi", AppLanguage.TA))
        assertEquals("நட்சத்திரம்", LocalizationEngine.get("nakshatra", AppLanguage.TA))
        assertEquals("இராகு காலம்", LocalizationEngine.get("rahu_kalam", AppLanguage.TA))

        val localizedVara = LocalizationEngine.translateVara(DayOfWeek.FRIDAY, AppLanguage.TA)
        assertEquals("வெள்ளிக்கிழமை", localizedVara)
    }

    @Test
    fun testLiveClockFormatting() {
        val time = LocalTime.of(15, 30, 45)
        val formattedTime = LocalizationEngine.formatLiveTime(time, AppLanguage.EN)
        assertTrue(formattedTime.contains("03:30:45") || formattedTime.contains("15:30:45"))
    }
}
