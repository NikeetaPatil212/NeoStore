package com.example.neostore.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(Addressentity::class)],version = 2)
abstract class AddressDb: RoomDatabase() {

    abstract fun addressDao():AddressDao
}
