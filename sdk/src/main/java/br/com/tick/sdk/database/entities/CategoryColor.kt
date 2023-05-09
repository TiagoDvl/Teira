package br.com.tick.sdk.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class CategoryColor(

    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val color: Long
)