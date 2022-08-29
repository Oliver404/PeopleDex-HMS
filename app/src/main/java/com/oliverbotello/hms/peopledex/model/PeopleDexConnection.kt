package com.oliverbotello.hms.peopledex.model

import android.content.Context
import androidx.room.Room
import com.oliverbotello.hms.peopledex.utils.DATABASE_NAME

class PeopleDexConnection {
    companion object {
        private var CONNECTION: PersonDao? = null

        fun initConnection(context: Context) {
            CONNECTION = Room.databaseBuilder(
                context,
                PeopleDexBD::class.java,
                DATABASE_NAME
            ).build().PersonDao()
        }
    }

    fun insertPerson(newPerson: PersonEnt) =
        CONNECTION?.insertAll(newPerson) ?: throw Exception("Connection not init")

    fun getAll(): List<PersonEnt> = CONNECTION?.getAll() ?: throw Exception("Connection not init")
}