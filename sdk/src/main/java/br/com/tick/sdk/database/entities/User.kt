package br.com.tick.sdk.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import br.com.tick.sdk.database.converters.LocalDateConverter
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.domain.AccountingDate

@Entity
@TypeConverters(LocalDateConverter::class)
data class User(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val userId: Int = 0,

    @ColumnInfo(name = "monthly_income")
    val monthlyIncome: Double,

    @ColumnInfo(name = "notification_periodicity")
    val notificationPeriodicity: NotificationPeriodicity,

    @ColumnInfo(name = "currency")
    val currency: CurrencyFormat,

    @ColumnInfo(name = "accounting_date")
    val accountingDate: AccountingDate
)
