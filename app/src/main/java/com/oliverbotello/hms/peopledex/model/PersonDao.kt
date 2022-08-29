package com.oliverbotello.hms.peopledex.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun getAll(): List<PersonEnt>

    @Query("SELECT * FROM person WHERE id IN (:id)")
    fun loadAllByIds(id: IntArray): List<PersonEnt>

    @Insert
    fun insertAll(vararg person: PersonEnt)

    @Delete
    fun delete(user: PersonEnt)
}