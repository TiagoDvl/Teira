package br.com.tick.sdk.database.converters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun toLong(date: LocalDate): Long {
        return date.toEpochDay()
    }

    @TypeConverter
    fun fromLong(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }
}
