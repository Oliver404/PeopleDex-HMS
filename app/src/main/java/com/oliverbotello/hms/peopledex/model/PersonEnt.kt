package com.oliverbotello.hms.peopledex.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class PersonEnt(
    @PrimaryKey val id: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val gender: Boolean,
    @ColumnInfo val type: String,
    @ColumnInfo val description: String
)
