package com.example.shared.data.entities

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.ForeignKey.Companion.CASCADE
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "points",
    foreignKeys = [
        ForeignKey(
            entity = StrokeEntity::class,
            parentColumns = ["id"],
            childColumns = ["strokeId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["strokeId"])
    ]
)
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val strokeId: Long,
    val x: Float,
    val y: Float,
    val order: Int
)