package br.com.tick.sdk.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
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
    val date: Long
)
