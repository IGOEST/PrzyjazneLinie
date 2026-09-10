package com.example.shared.data.daos

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import com.example.shared.data.entities.PatternEntity
import com.example.shared.data.entities.PatternWithStrokes
import com.example.shared.data.entities.PointEntity
import com.example.shared.data.entities.StrokeEntity

@Dao
interface PatternDao {
    @Query("SELECT COUNT(*) FROM patterns WHERE isExample = 1")
    suspend fun getExamplePatternsCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM patterns WHERE name = :name)")
    suspend fun patternExistsByName(name: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM patterns WHERE exampleKey = :exampleKey)")
    suspend fun examplePatternExists(exampleKey: String): Boolean

    @Transaction
    @Query("SELECT * FROM patterns ORDER BY isExample DESC, createdAt ASC")
    suspend fun getAllPatterns(): List<PatternEntity>

    @Query("SELECT * FROM strokes")
    suspend fun getAllStrokes(): List<StrokeEntity>

    @Query("SELECT * FROM points")
    suspend fun getAllPoints(): List<PointEntity>

    @Transaction
    @Query("SELECT * FROM patterns WHERE id = :patternId")
    suspend fun getPatternWithStrokes(
        patternId: Long
    ): PatternWithStrokes?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPattern(
        pattern: PatternEntity
    ): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStroke(
        stroke: StrokeEntity
    ): Long

    @Insert
    suspend fun insertStrokes(
        strokes: List<StrokeEntity>
    ): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoint(
        point: PointEntity
    )

    @Insert
    suspend fun insertPoints(
        points: List<PointEntity>
    ): List<Long>

    @Delete
    suspend fun deletePattern(
        pattern: PatternEntity
    )

    @Query("DELETE FROM patterns WHERE id = :patternId")
    suspend fun deletePatternById(patternId: Long)

    @Transaction
    suspend fun insertPatternWithStrokes(
        pattern: PatternEntity,
        strokes: List<StrokeEntity>,
        pointsByStroke: List<List<PointEntity>>
    ): Long {
        val patternId = insertPattern(pattern)

        val strokesWithPatternId = strokes.map {
            it.copy(
                patternId = patternId
            )
        }

        val strokeIds = insertStrokes(strokesWithPatternId)

        val pointsWithStrokeIds = pointsByStroke.mapIndexed { strokeIndex, points ->
            val strokeId = strokeIds[strokeIndex]

            points.map { point ->
                point.copy(
                    strokeId = strokeId
                )
            }
        }.flatten()

        insertPoints(pointsWithStrokeIds)

        return patternId
    }
}