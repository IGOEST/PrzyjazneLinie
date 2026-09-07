package com.example.shared.data.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.shared.data.daos.PatternDao
import com.example.shared.data.entities.PatternEntity
import com.example.shared.data.entities.StrokeEntity
import com.example.shared.data.entities.PointEntity

@Database(
    entities = [
        PatternEntity::class,
        StrokeEntity::class,
        PointEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class FriendlyLinesDatabase : RoomDatabase() {
    abstract fun patternDao(): PatternDao
}