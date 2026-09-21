package com.example.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.model.BookmarkEntity

@Database(
    entities = [
        BookmarkEntity::class,
        PanchangaDayEntity::class,
        PlanetaryLongitudeEntity::class,
        RashiTransitEntity::class,
        NakshatraTransitEntity::class,
        RetrogradeEventEntity::class,
        EclipseEntity::class,
        FestivalEntity::class,
        SourceDocumentEntity::class,
        DatasetAuditEntity::class,
        LocationCalculationEntity::class,
        HoroscopeContentEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun panchangaDao(): PanchangaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "panchanga_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

