package br.com.tick.sdk.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryColor(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val color: Int
)
