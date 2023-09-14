package br.com.tick.sdk.database.converters

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class LatLngConverter {

    companion object {
        const val LAT_LNG_SPLITTER = "|"
    }

    @TypeConverter
    fun toString(location: LatLng?): String? {
        if (location == null) return null

        return "${location.latitude}$LAT_LNG_SPLITTER${location.longitude}"
    }

    @TypeConverter
    fun fromString(location: String?): LatLng? {
        if (location == null) return null

        val locationFromString = location.split(LAT_LNG_SPLITTER)
        return LatLng(locationFromString[0].toDouble(), locationFromString[1].toDouble())
    }
}
