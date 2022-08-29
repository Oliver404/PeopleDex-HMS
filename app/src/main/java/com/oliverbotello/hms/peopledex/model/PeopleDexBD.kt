package com.oliverbotello.hms.peopledex.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PersonEnt::class], version = 1)
abstract class PeopleDexBD : RoomDatabase() {
    abstract fun PersonDao(): PersonDao
}