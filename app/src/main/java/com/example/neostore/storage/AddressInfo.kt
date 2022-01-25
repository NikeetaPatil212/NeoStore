package com.example.neostore.storage

import androidx.room.ColumnInfo

data class AddressInfo(
    var id:Int,
    var address:String,
    var  city:String,
   var state:String,
    var zipcode:String,
    var country:String
)

