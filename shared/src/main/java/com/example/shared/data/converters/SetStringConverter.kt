package com.example.shared.data.converters

import androidx.room3.ColumnTypeConverter

class SetStringConverter {
    @ColumnTypeConverter
    fun fromStringSet(value: Set<String>): String = value.sorted().joinToString(";")

    @ColumnTypeConverter
    fun toStringSet(value: String): Set<String> = value.split(";")
            .filter { it.isNotBlank() }
            .toSet()
}