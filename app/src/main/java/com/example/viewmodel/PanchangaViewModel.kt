package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.db.*
import com.example.engine.*
import com.example.model.*
import com.example.network.PanchangNetworkRepository
import com.example.repository.*
import com.example.worker.PanchangaDailyWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class PanchangaViewModel : ViewModel() {

    private val panchangNetworkRepository = PanchangNetworkRepository()
    private var panchangaRepository: PanchangaRepository? = null

    private fun getPanchangaRepository(context: android.content.Context): PanchangaRepository {
        return panchangaRepository ?: synchronized(this) {
            val repo = PanchangaRepository(context)
            panchangaRepository = repo
            repo
        }
    }

    private val _datasetAuditStatus = MutableStateFlow(
        DatasetAuditStatus(
            year = 2026,
            totalDatesExpected = 365,
            totalDatesLoaded = 285,
            coverageStatus = DatasetCoverageStatus.PARTIAL_DATA_REQUIRED,
            missingRangeDescription = "2026-01-01 through 2026-03-21",
            requiredFileName = "rashtriya_panchang_1947_saka.json",
            duplicateCount = 0
        )
    )
    val datasetAuditStatus: StateFlow<DatasetAuditStatus> = _datasetAuditStatus.asStateFlow()

    fun initializeRepository(context: android.content.Context) {
        viewModelScope.launch {
            val repo = getPanchangaRepository(context)
            repo.initializeAndImportIfNeeded()
            val audit = repo.getDatasetAudit(2026)
            _datasetAuditStatus.value = audit
        }
    }

    // Room Bookmark database / repository helpers
    private var bookmarkRepository: BookmarkRepository? = null

    private fun getBookmarkRepository(context: android.content.Context): BookmarkRepository {
        return bookmarkRepository ?: synchronized(this) {
            val repo = BookmarkRepository(
                AppDatabase.getDatabase(context).bookmarkDao()
            )
            bookmarkRepository = repo
            repo
        }
    }

    private val _bookmarks = MutableStateFlow<List<BookmarkEntity>>(emptyList())
    val bookmarks: StateFlow<List<BookmarkEntity>> = _bookmarks.asStateFlow()

    private val _selectedLocation = MutableStateFlow(AstronomicalEngine.CITIES.first()) // Default resolved dynamically
    val selectedLocation: StateFlow<CityLocation> = _selectedLocation.asStateFlow()

    private val _selectedTradition = MutableStateFlow(CalendarTradition.TELUGU)
    val selectedTradition: StateFlow<CalendarTradition> = _selectedTradition.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(AppLanguage.TE) // Default to Telugu as requested
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _showLanguageDialog = MutableStateFlow(false)
    val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

    private val _selectedThemeMode = MutableStateFlow(AppThemeMode.SACRED_SAFFRON)
    val selectedThemeMode: StateFlow<AppThemeMode> = _selectedThemeMode.asStateFlow()

    private val _showThemeDialog = MutableStateFlow(false)
    val showThemeDialog: StateFlow<Boolean> = _showThemeDialog.asStateFlow()

    private val _activeTab = MutableStateFlow(0) // 0: Today, 1: Calendar, 2: Festivals, 3: Muhurtha, 4: Explore
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _resetSubScreensSignal = MutableStateFlow(0L)
    val resetSubScreensSignal: StateFlow<Long> = _resetSubScreensSignal.asStateFlow()

    fun resetSubScreens() {
        _resetSubScreensSignal.value = System.currentTimeMillis()
        _selectedFestivalForDetail.value = null
        _shareCardFestival.value = null
        _selectedSpiritualItem.value = null
    }

    private val _exploreSubTab = MutableStateFlow(0) // 0: Sky/Astronomy, 1: Rashis, 2: Birth Panchangam, 3: Devotional, 4: Family
    val exploreSubTab: StateFlow<Int> = _exploreSubTab.asStateFlow()

    data class BirthInputState(
        val day: Int,
        val month: Int,
        val year: Int,
        val hour: Int,
        val minute: Int,
        val districtId: String = "hyd",
        val personName: String = "",
        val autoCalculateToken: Long = System.currentTimeMillis()
    )

    private val _selectedBirthInput = MutableStateFlow<BirthInputState?>(null)
    val selectedBirthInput: StateFlow<BirthInputState?> = _selectedBirthInput.asStateFlow()

    fun setExploreSubTab(subTabIndex: Int) {
        _exploreSubTab.value = subTabIndex
    }

    fun navigateToBirthPanchangam() {
        _exploreSubTab.value = 2
        _activeTab.value = 4
    }

    fun loadBirthPanchangaFromBookmark(bookmark: BookmarkEntity) {
        try {
            var year = 1995
            var month = 9
            var day = 15
            var hour = 10
            var minute = 30
            var districtId = "hyd"
            var personName = ""

            if (bookmark.rawDate.contains("|")) {
                val parts = bookmark.rawDate.split("|")
                val dateParts = parts[0].split("-")
                if (dateParts.size == 3) {
                    year = dateParts[0].toIntOrNull() ?: 1995
                    month = dateParts[1].toIntOrNull() ?: 9
                    day = dateParts[2].toIntOrNull() ?: 15
                }
                if (parts.size >= 2 && parts[1].contains(":")) {
                    val timeParts = parts[1].split(":")
                    hour = timeParts[0].toIntOrNull() ?: 10
                    minute = timeParts[1].toIntOrNull() ?: 30
                }
                if (parts.size >= 3 && parts[2].isNotBlank()) {
                    districtId = parts[2]
                }
                if (parts.size >= 4) {
                    personName = parts[3]
                }
            } else {
                val parts = bookmark.id.removePrefix("janma_").split("_")
                if (parts.size >= 5) {
                    year = parts[0].toIntOrNull() ?: 1995
                    month = parts[1].toIntOrNull() ?: 9
                    day = parts[2].toIntOrNull() ?: 15
                    hour = parts[3].toIntOrNull() ?: 10
                    minute = parts[4].toIntOrNull() ?: 30
                    if (parts.size >= 6) districtId = parts[5]
                }
                if (bookmark.title.contains(" - ")) {
                    personName = bookmark.title.substringBefore(" - ").trim()
                }
            }

            _selectedBirthInput.value = BirthInputState(
                day = day,
                month = month,
                year = year,
                hour = hour,
                minute = minute,
                districtId = districtId,
                personName = personName,
                autoCalculateToken = System.currentTimeMillis()
            )
            navigateToBirthPanchangam()
        } catch (e: Exception) {
            navigateToBirthPanchangam()
        }
    }

    fun navigateToDevotional() {
        _exploreSubTab.value = 3
        _activeTab.value = 4
    }

    // Muhurtha selection
    private val _selectedMuhurthaCategory = MutableStateFlow(MuhurthaCategory.MARRIAGE)
    val selectedMuhurthaCategory: StateFlow<MuhurthaCategory> = _selectedMuhurthaCategory.asStateFlow()

    private val _selectedMuhurthaMonths = MutableStateFlow(3)
    val selectedMuhurthaMonths: StateFlow<Int> = _selectedMuhurthaMonths.asStateFlow()

    // Search and filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _festivalCategoryFilter = MutableStateFlow(FestivalCategory.ALL)
    val festivalCategoryFilter: StateFlow<FestivalCategory> = _festivalCategoryFilter.asStateFlow()

    // Modals
    private val _selectedFestivalForDetail = MutableStateFlow<FestivalItem?>(null)
    val selectedFestivalForDetail: StateFlow<FestivalItem?> = _selectedFestivalForDetail.asStateFlow()

    private val _shareCardFestival = MutableStateFlow<FestivalItem?>(null)
    val shareCardFestival: StateFlow<FestivalItem?> = _shareCardFestival.asStateFlow()

    private val _selectedSpiritualItem = MutableStateFlow<SpiritualItem?>(null)
    val selectedSpiritualItem: StateFlow<SpiritualItem?> = _selectedSpiritualItem.asStateFlow()

    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()

    private val _isDetectingLocation = MutableStateFlow(false)
    val isDetectingLocation: StateFlow<Boolean> = _isDetectingLocation.asStateFlow()

    private val _locationMessage = MutableStateFlow<String?>(null)
    val locationMessage: StateFlow<String?> = _locationMessage.asStateFlow()

    /**
     * Detects user's current device timezone/location by default on launch.
     */
    fun initializeDefaultLocation(context: android.content.Context) {
        val defaultLoc = LocationHelper.getDefaultLocation(context)
        _selectedLocation.value = defaultLoc
    }

    /**
     * Auto-detects user's current city/coordinates using Google Location Services API.
     */
    fun detectAndApplyCurrentLocation(
        context: android.content.Context,
        onSuccess: (CityLocation) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isDetectingLocation.value = true
            _locationMessage.value = null
            try {
                val result = LocationHelper.detectCurrentLocation(context)
                result.fold(
                    onSuccess = { city ->
                        saveLocation(context, city)
                        _isDetectingLocation.value = false
                        val msg = if (_selectedLanguage.value == AppLanguage.TE) 
                            "📍 ప్రదేశం గుర్తించబడింది: ${city.name} (${city.stateOrCountry})" 
                        else 
                            "📍 Location detected: ${city.name} (${city.stateOrCountry})"
                        _locationMessage.value = msg
                        onSuccess(city)
                    },
                    onFailure = { err ->
                        _isDetectingLocation.value = false
                        val errMsg = err.message ?: "Failed to detect location"
                        _locationMessage.value = "⚠️ $errMsg"
                        onError(errMsg)
                    }
                )
            } catch (e: Exception) {
                _isDetectingLocation.value = false
                val errMsg = e.message ?: "Error detecting location"
                _locationMessage.value = "⚠️ $errMsg"
                onError(errMsg)
            }
        }
    }

    fun clearLocationMessage() {
        _locationMessage.value = null
    }

    /**
     * Schedules daily background push notification at 6:30 AM using WorkManager.
     */
    fun scheduleDailyPanchangaNotification(context: android.content.Context) {
        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean("notifications_enabled", true)
        val festivalsEnabled = prefs.getBoolean("festivals_alert", true)
        if (enabled) {
            PanchangaDailyWorker.scheduleDaily630AMWorker(context)
        }
        if (enabled && festivalsEnabled) {
            com.example.worker.FestivalAlertWorker.scheduleFestivalAlertWorker(context)
        }
    }

    fun isIdBookmarked(id: String): Boolean {
        return _bookmarks.value.any { it.id == id }
    }

    fun toggleBookmarkJanmaPanchanga(
        context: android.content.Context,
        id: String,
        title: String,
        subtitle: String,
        rawDate: String
    ) {
        val isAlreadyBookmarked = isIdBookmarked(id)

        // Optimistic UI update for 0ms instantaneous feedback
        if (isAlreadyBookmarked) {
            _bookmarks.value = _bookmarks.value.filterNot { it.id == id }
        } else {
            val newEntity = BookmarkEntity(
                id = id,
                type = "JANMA_PANCHANGA",
                title = title,
                subtitle = subtitle,
                rawDate = rawDate,
                icon = "🔮"
            )
            _bookmarks.value = listOf(newEntity) + _bookmarks.value
        }

        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val repo = getBookmarkRepository(context)
            if (isAlreadyBookmarked) {
                repo.deleteById(id)
            } else {
                repo.insert(
                    BookmarkEntity(
                        id = id,
                        type = "JANMA_PANCHANGA",
                        title = title,
                        subtitle = subtitle,
                        rawDate = rawDate,
                        icon = "🔮"
                    )
                )
            }
        }
    }

    private fun getCityZoneId(): java.time.ZoneId {
        return try {
            java.time.ZoneId.of(_selectedLocation.value.timezoneId)
        } catch (e: Exception) {
            java.time.ZoneId.of("Asia/Kolkata")
        }
    }

    // Default reference date: dynamically initializes to current date in India/city timezone
    private val _selectedDate = MutableStateFlow(java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Kolkata")).toLocalDate())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    // Real-time ticking live clock synchronized with selected location timezone (IST by default)
    private val _currentLiveTime = MutableStateFlow(java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Kolkata")).toLocalTime())
    val currentLiveTime: StateFlow<LocalTime> = _currentLiveTime.asStateFlow()

    private val _currentLiveDate = MutableStateFlow(java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Kolkata")).toLocalDate())
    val currentLiveDate: StateFlow<LocalDate> = _currentLiveDate.asStateFlow()

    val liveDate: StateFlow<LocalDate> get() = _currentLiveDate.asStateFlow()

    val liveTimeStr: StateFlow<String> = _currentLiveTime.map {
        it.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm:ss a", java.util.Locale.ENGLISH))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val _notificationsEnabled = MutableStateFlow(false)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _morningAlertEnabled = MutableStateFlow(true)
    val morningAlertEnabled: StateFlow<Boolean> = _morningAlertEnabled.asStateFlow()

    private val _rahuKalamAlertEnabled = MutableStateFlow(true)
    val rahuKalamAlertEnabled: StateFlow<Boolean> = _rahuKalamAlertEnabled.asStateFlow()

    private val _festivalsAlertEnabled = MutableStateFlow(true)
    val festivalsAlertEnabled: StateFlow<Boolean> = _festivalsAlertEnabled.asStateFlow()

    private val _selectedRashi = MutableStateFlow(Rashi.MESHA)
    val selectedRashi: StateFlow<Rashi> = _selectedRashi.asStateFlow()

    private val _dailyHoroscope = MutableStateFlow("")
    val dailyHoroscope: StateFlow<String> = _dailyHoroscope.asStateFlow()

    private val _isHoroscopeLoading = MutableStateFlow(false)
    val isHoroscopeLoading: StateFlow<Boolean> = _isHoroscopeLoading.asStateFlow()

    init {
        // Dynamic second-by-second live ticker in selected location timezone
        viewModelScope.launch {
            while (true) {
                val zdt = java.time.ZonedDateTime.now(getCityZoneId())
                _currentLiveTime.value = zdt.toLocalTime()
                val today = zdt.toLocalDate()
                if (_currentLiveDate.value != today) {
                    _currentLiveDate.value = today
                }
                delay(1000)
            }
        }

        // Collect combination of rashi, date, and language to update horoscope
        viewModelScope.launch {
            combine(
                _selectedRashi,
                _selectedDate,
                _selectedLanguage
            ) { rashi, date, lang ->
                Triple(rashi, date, lang)
            }.collectLatest { (rashi, date, lang) ->
                _isHoroscopeLoading.value = true
                val forecast = HoroscopeService.fetchDailyHoroscope(rashi, date, lang)
                _dailyHoroscope.value = forecast
                _isHoroscopeLoading.value = false
            }
        }
    }

    fun setThemeMode(mode: AppThemeMode) {
        _selectedThemeMode.value = mode
    }

    fun setShowThemeDialog(show: Boolean) {
        _showThemeDialog.value = show
    }

    // Family events
    private val _familyEvents = MutableStateFlow(
        listOf(
            FamilyEvent(
                id = "fe_1",
                title = "Father's Birthday",
                personName = "Father",
                eventType = "Birthday",
                date = LocalDate.of(2026, 9, 21),
                tithiNotes = "Shukla Dashami"
            ),
            FamilyEvent(
                id = "fe_2",
                title = "Grandfather's Annual Shraddha",
                personName = "Grandfather",
                eventType = "Shraddha",
                date = LocalDate.of(2026, 10, 10),
                tithiNotes = "Mahalaya Sarva Pitru Amavasya"
            )
        )
    )
    val familyEvents: StateFlow<List<FamilyEvent>> = _familyEvents.asStateFlow()

    // Computed Day Panchanga
    val currentPanchanga: StateFlow<DayPanchanga> = combine(
        _selectedDate,
        _selectedLocation,
        _selectedTradition
    ) { date, loc, trad ->
        val base = AstronomicalEngine.calculatePanchanga(date, loc, trad)
        val dayFestivals = FestivalRepository.getFestivalsForDate(date)
        base.copy(festivals = dayFestivals)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AstronomicalEngine.calculatePanchanga(
            _selectedDate.value,
            _selectedLocation.value,
            _selectedTradition.value
        ).copy(festivals = FestivalRepository.getFestivalsForDate(_selectedDate.value))
    )

    // "What's Happening Now"
    val whatIsHappeningNow: StateFlow<WhatIsHappeningNow> = currentPanchanga.map { panchanga ->
        AstronomicalEngine.evaluateWhatIsHappeningNow(panchanga)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AstronomicalEngine.evaluateWhatIsHappeningNow(currentPanchanga.value)
    )

    // Muhurtha results
    val muhurthaResults: StateFlow<List<MuhurthaResult>> = combine(
        combine(_selectedMuhurthaCategory, _selectedMuhurthaMonths, _selectedDate, ::Triple),
        combine(_selectedLocation, _selectedTradition, _selectedLanguage, ::Triple)
    ) { (cat, months, date), (loc, trad, lang) ->
        MuhurthaEngine.findMuhurthas(cat, date, months, loc, trad, lang)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered festivals
    val allFestivalsForYear: StateFlow<List<FestivalItem>> = _selectedDate.map { date ->
        FestivalRepository.getFestivalsForYear(date.year)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val upcomingFestivals: StateFlow<List<FestivalItem>> = _selectedDate.map { date ->
        FestivalRepository.getUpcomingFestivals(date, 8)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun nextDay() {
        _selectedDate.value = _selectedDate.value.plusDays(1)
    }

    fun previousDay() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    fun jumpToToday() {
        val zdt = java.time.ZonedDateTime.now(getCityZoneId())
        _selectedDate.value = zdt.toLocalDate()
        _currentLiveDate.value = zdt.toLocalDate()
        _currentLiveTime.value = zdt.toLocalTime()
        _activeTab.value = 0
        resetSubScreens()
    }

    fun setLocation(city: CityLocation) {
        _selectedLocation.value = city
        val zoneId = try {
            java.time.ZoneId.of(city.timezoneId)
        } catch (e: Exception) {
            java.time.ZoneId.of("Asia/Kolkata")
        }
        val zdt = java.time.ZonedDateTime.now(zoneId)
        _currentLiveDate.value = zdt.toLocalDate()
        _currentLiveTime.value = zdt.toLocalTime()
    }

    fun setTradition(tradition: CalendarTradition) {
        _selectedTradition.value = tradition
    }

    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
        if (language == AppLanguage.TE) {
            _selectedTradition.value = CalendarTradition.TELUGU
        }
    }

    fun loadPreferences(context: android.content.Context) {
        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        
        val savedCityId = prefs.getString("selected_city_id", null)
        if (savedCityId != null) {
            val city = AstronomicalEngine.CITIES.find { it.id.equals(savedCityId, ignoreCase = true) }
                ?: IndianDistrictsRepository.ALL_DISTRICTS.find { it.id.equals(savedCityId, ignoreCase = true) }?.toCityLocation()
            if (city != null) {
                setLocation(city)
            }
        }
        
        val savedLangStr = prefs.getString("selected_language", null)
        if (savedLangStr != null) {
            val lang = try { AppLanguage.valueOf(savedLangStr) } catch (e: Exception) { null }
            if (lang != null) {
                _selectedLanguage.value = lang
            }
        }
        
        val savedTraditionStr = prefs.getString("selected_tradition", null)
        if (savedTraditionStr != null) {
            val tradition = try { CalendarTradition.valueOf(savedTraditionStr) } catch (e: Exception) { null }
            if (tradition != null) {
                _selectedTradition.value = tradition
            }
        }

        val savedThemeStr = prefs.getString("selected_theme_mode", null)
        if (savedThemeStr != null) {
            val theme = try { AppThemeMode.valueOf(savedThemeStr) } catch (e: Exception) { null }
            if (theme != null) {
                _selectedThemeMode.value = theme
            }
        }

        val savedRashiStr = prefs.getString("selected_rashi", null)
        if (savedRashiStr != null) {
            val rashi = try { Rashi.valueOf(savedRashiStr) } catch (e: Exception) { null }
            if (rashi != null) {
                _selectedRashi.value = rashi
            }
        }

        _notificationsEnabled.value = prefs.getBoolean("notifications_enabled", true)
        _morningAlertEnabled.value = prefs.getBoolean("morning_alert", true)
        _rahuKalamAlertEnabled.value = prefs.getBoolean("rahu_alert", true)
        _festivalsAlertEnabled.value = prefs.getBoolean("festivals_alert", true)

        // Enforce Telugu strictly as the only active language to satisfy user requirement
        _selectedLanguage.value = AppLanguage.TE
        _selectedTradition.value = CalendarTradition.TELUGU
        _showLanguageDialog.value = false

        // Initialize and stream bookmarks from database Flow on IO dispatcher
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            getBookmarkRepository(context).allBookmarks.collect { list ->
                _bookmarks.value = list
            }
        }
    }

    fun toggleBookmarkDate(context: android.content.Context, date: LocalDate, title: String, subtitle: String) {
        val id = "date_${date}"
        val isAlreadyBookmarked = isIdBookmarked(id)
        
        // Optimistic UI update for 0ms instantaneous feedback
        if (isAlreadyBookmarked) {
            _bookmarks.value = _bookmarks.value.filterNot { it.id == id }
        } else {
            val newEntity = BookmarkEntity(
                id = id,
                type = "DATE",
                title = title,
                subtitle = subtitle,
                rawDate = date.toString(),
                icon = "🚩"
            )
            _bookmarks.value = listOf(newEntity) + _bookmarks.value
        }

        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val repo = getBookmarkRepository(context)
            if (isAlreadyBookmarked) {
                repo.deleteById(id)
            } else {
                repo.insert(
                    BookmarkEntity(
                        id = id,
                        type = "DATE",
                        title = title,
                        subtitle = subtitle,
                        rawDate = date.toString(),
                        icon = "🚩"
                    )
                )
            }
        }
    }

    fun toggleBookmarkFestival(context: android.content.Context, festival: FestivalItem) {
        val id = "fest_${festival.id}"
        val isAlreadyBookmarked = isIdBookmarked(id)

        // Optimistic UI update for 0ms instantaneous feedback
        if (isAlreadyBookmarked) {
            _bookmarks.value = _bookmarks.value.filterNot { it.id == id }
        } else {
            val newEntity = BookmarkEntity(
                id = id,
                type = "FESTIVAL",
                title = festival.name,
                subtitle = festival.summary,
                rawDate = festival.date.toString(),
                icon = festival.iconEmoji
            )
            _bookmarks.value = listOf(newEntity) + _bookmarks.value
        }

        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val repo = getBookmarkRepository(context)
            if (isAlreadyBookmarked) {
                repo.deleteById(id)
            } else {
                repo.insert(
                    BookmarkEntity(
                        id = id,
                        type = "FESTIVAL",
                        title = festival.name,
                        subtitle = festival.summary,
                        rawDate = festival.date.toString(),
                        icon = festival.iconEmoji
                    )
                )
            }
        }
    }

    fun toggleBookmarkDevotionalQuote(
        context: android.content.Context,
        quote: com.example.engine.DevotionalQuote,
        rawDate: String = ""
    ) {
        val id = "quote_${quote.id}"
        val isAlreadyBookmarked = isIdBookmarked(id)

        // Optimistic UI update for 0ms instantaneous feedback
        if (isAlreadyBookmarked) {
            _bookmarks.value = _bookmarks.value.filterNot { it.id == id }
        } else {
            val newEntity = BookmarkEntity(
                id = id,
                type = "DEVOTIONAL_QUOTE",
                title = quote.titleTelugu,
                subtitle = "${quote.source} • ${quote.deityOrContext}",
                rawDate = rawDate,
                icon = quote.icon
            )
            _bookmarks.value = listOf(newEntity) + _bookmarks.value
        }

        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val repo = getBookmarkRepository(context)
            if (isAlreadyBookmarked) {
                repo.deleteById(id)
            } else {
                repo.insert(
                    BookmarkEntity(
                        id = id,
                        type = "DEVOTIONAL_QUOTE",
                        title = quote.titleTelugu,
                        subtitle = "${quote.source} • ${quote.deityOrContext}",
                        rawDate = rawDate,
                        icon = quote.icon
                    )
                )
            }
        }
    }

    fun deleteBookmarkById(context: android.content.Context, id: String) {
        _bookmarks.value = _bookmarks.value.filterNot { it.id == id }
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            getBookmarkRepository(context).deleteById(id)
        }
    }

    fun saveLanguage(context: android.content.Context, language: AppLanguage) {
        setLanguage(language)
        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit()
            .putString("selected_language", language.name)
            .putBoolean("has_selected_language", true)
            .apply()
    }
    
    fun saveLocation(context: android.content.Context, city: CityLocation) {
        setLocation(city)
        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("selected_city_id", city.id).apply()
    }
    
    fun saveTradition(context: android.content.Context, tradition: CalendarTradition) {
        setTradition(tradition)
        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("selected_tradition", tradition.name).apply()
    }

    fun saveThemeMode(context: android.content.Context, mode: AppThemeMode) {
        setThemeMode(mode)
        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("selected_theme_mode", mode.name).apply()
    }

    fun saveRashi(context: android.content.Context, rashi: Rashi) {
        _selectedRashi.value = rashi
        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().putString("selected_rashi", rashi.name).apply()
    }

    fun setShowLanguageDialog(show: Boolean) {
        _showLanguageDialog.value = show
    }

    fun setActiveTab(tab: Int) {
        _activeTab.value = tab
        resetSubScreens()
    }

    fun setMuhurthaCategory(category: MuhurthaCategory) {
        _selectedMuhurthaCategory.value = category
    }

    fun setMuhurthaMonths(months: Int) {
        _selectedMuhurthaMonths.value = months
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFestivalFilter(category: FestivalCategory) {
        _festivalCategoryFilter.value = category
    }

    fun selectFestivalForDetail(item: FestivalItem?) {
        _selectedFestivalForDetail.value = item
    }

    fun openShareCard(item: FestivalItem?) {
        _shareCardFestival.value = item
    }

    fun selectSpiritualItem(item: SpiritualItem?) {
        _selectedSpiritualItem.value = item
    }

    fun toggleDevotionalAudio(durationSec: Int = 30) {
        _isAudioPlaying.value = !_isAudioPlaying.value
    }

    fun addFamilyEvent(title: String, person: String, type: String, date: LocalDate, tithi: String) {
        val newEvent = FamilyEvent(
            id = "fe_${System.currentTimeMillis()}",
            title = title,
            personName = person,
            eventType = type,
            date = date,
            tithiNotes = tithi
        )
        _familyEvents.value = _familyEvents.value + newEvent
    }

    fun deleteFamilyEvent(id: String) {
        _familyEvents.value = _familyEvents.value.filter { it.id != id }
    }

    fun saveNotificationSettings(
        context: android.content.Context,
        enabled: Boolean,
        morning: Boolean,
        rahu: Boolean,
        festivals: Boolean
    ) {
        _notificationsEnabled.value = enabled
        _morningAlertEnabled.value = morning
        _rahuKalamAlertEnabled.value = rahu
        _festivalsAlertEnabled.value = festivals

        val prefs = context.getSharedPreferences("PanchangamPrefs", android.content.Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("notifications_enabled", enabled)
            .putBoolean("morning_alert", morning)
            .putBoolean("rahu_alert", rahu)
            .putBoolean("festivals_alert", festivals)
            .apply()

        if (enabled) {
            PanchangaDailyWorker.scheduleDaily630AMWorker(context)
            if (festivals) {
                com.example.worker.FestivalAlertWorker.scheduleFestivalAlertWorker(context)
            } else {
                com.example.worker.FestivalAlertWorker.cancelWorker(context)
            }
        } else {
            PanchangaDailyWorker.cancelWorker(context)
            com.example.worker.FestivalAlertWorker.cancelWorker(context)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
