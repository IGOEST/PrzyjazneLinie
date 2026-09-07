package com.example.shared.data.entities

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "patterns",
    indices = [Index(value = ["name"], unique = true)]
)
data class PatternEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isExample: Boolean,
    val isComplex: Boolean,
    val createdAt: Long,
    val smoothingEnabled: Boolean,
    val exampleKey: String? = null
)