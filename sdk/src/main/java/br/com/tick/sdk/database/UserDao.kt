package br.com.tick.sdk.database

import androidx.room.Dao
import androidx.room.Query
import br.com.tick.sdk.database.entities.User
import br.com.tick.sdk.domain.CurrencyFormat
import br.com.tick.sdk.domain.NotificationPeriodicity
import br.com.tick.sdk.domain.AccountingDate
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE user_id = 1")
    fun getUniqueUser(): Flow<User>

    @Query("UPDATE User SET monthly_income = :newValue WHERE user_id = 1")
    suspend fun setMonthlyIncome(newValue: Double)

    @Query("UPDATE User SET notification_periodicity = :newPeriodicity WHERE user_id = 1")
    suspend fun setNotificationPeriodicity(newPeriodicity: NotificationPeriodicity)

    @Query("UPDATE User SET currency = :currency WHERE user_id = 1")
    suspend fun setCurrency(currency: CurrencyFormat)

    @Query("UPDATE User SET accounting_date = :accountingDate WHERE user_id = 1")
    suspend fun setAccountingDate(accountingDate: AccountingDate)
}
