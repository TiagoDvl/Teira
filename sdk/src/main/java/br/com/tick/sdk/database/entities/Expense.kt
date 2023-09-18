package br.com.tick.sdk.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.tick.sdk.database.converters.LatLngConverter
import br.com.tick.sdk.database.converters.LocalDateConverter
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

@Entity
@TypeConverters(LocalDateConverter::class, LatLngConverter::class)
data class Expense(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    val expenseId: Int = 0,

    @ColumnInfo(name = "category_id")
    val categoryId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "value")
    val value: Double,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "location")
    val location: LatLng?,

    @ColumnInfo(name = "photoUri")
    val photoUri: String?

)
