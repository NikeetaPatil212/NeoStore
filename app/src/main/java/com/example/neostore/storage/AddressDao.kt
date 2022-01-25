package com.example.neostore.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface AddressDao {

    @Insert
    fun addAdreess(add: Addressentity)

    @Query("select * From  address")
    abstract fun getData(): List<Addressentity>

    @Delete
    fun deleteHistory(data:Addressentity)

}