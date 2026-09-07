package com.example.shared.data.entities

import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import androidx.room3.Entity
import androidx.room3.ForeignKey.Companion.CASCADE
import androidx.room3.Index

@Entity(
    tableName = "strokes",
    foreignKeys = [
        ForeignKey(
            entity = PatternEntity::class,
            parentColumns = ["id"],
            childColumns = ["patternId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["patternId"])
    ]
)
data class StrokeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patternId: Long,
    val order: Int,
)