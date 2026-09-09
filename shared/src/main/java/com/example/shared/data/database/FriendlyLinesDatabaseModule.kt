package com.example.shared.data.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.shared.data.daos.PatternDao
import com.example.shared.data.daos.LearningStepPatternDao
import com.example.shared.data.daos.LearningStepDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFriendlyLinesDatabase(
        @ApplicationContext context: Context
    ): FriendlyLinesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FriendlyLinesDatabase::class.java,
            "friendly_lines_database"
        )
            .fallbackToDestructiveMigration(true)
            .addCallback(object : RoomDatabase.Callback() {
                override suspend fun onOpen(db: SQLiteConnection) {
                    super.onOpen(db)
                    db.execSQL("PRAGMA foreign_keys=ON;") // to umozliwia dzialanie foreign keys
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun providePatternDao(
        database: FriendlyLinesDatabase
    ): PatternDao {
        return database.patternDao()
    }

    @Provides
    @Singleton
    fun provideLearningStepPatternDao(
        database: FriendlyLinesDatabase
    ): LearningStepPatternDao {
        return database.learningStepPatternDao()
    }

    @Provides
    @Singleton
    fun provideLearningStepDao(
        database: FriendlyLinesDatabase
    ): LearningStepDao {
        return database.learningStepDao()
    }
}