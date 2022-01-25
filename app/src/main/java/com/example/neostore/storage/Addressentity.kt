package com.example.neostore.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address" )
data class Addressentity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "ADDRESS")var address:String,
    @ColumnInfo(name = "CITY")var  city:String,
    @ColumnInfo(name = "STATE")var state:String,
    @ColumnInfo(name = "ZIP CODE")var zipcode:String,
    @ColumnInfo(name = "COUNTRY")var country:String
)

