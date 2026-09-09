package com.example.shared.data.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.shared.data.daos.PatternDao
import com.example.shared.data.daos.LearningStepPatternDao
import com.example.shared.data.daos.LearningStepDao
import com.example.shared.data.entities.PatternEntity
import com.example.shared.data.entities.StrokeEntity
import com.example.shared.data.entities.PointEntity
import com.example.shared.data.entities.LearningStepEntity
import com.example.shared.data.entities.LearningStepPatternEntity

@Database(
    entities = [
        PatternEntity::class,
        StrokeEntity::class,
        PointEntity::class,
        LearningStepEntity::class,
        LearningStepPatternEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class FriendlyLinesDatabase : RoomDatabase() {
    abstract fun patternDao(): PatternDao
    abstract fun learningStepPatternDao(): LearningStepPatternDao
    abstract fun learningStepDao(): LearningStepDao
}