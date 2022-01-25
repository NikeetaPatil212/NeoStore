package com.example.neostore.storage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.neostore.R
import com.example.neostore.activities.AddressListActivity
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddAddress :AppCompatActivity(){
    lateinit var addressDb: AddressDb
    lateinit var addressListRecyclerView: RecyclerView
    lateinit var adapter: com.example.neostore.storage.Adapter
    var list = ArrayList<AddressInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
/*
         btnAddToCartToolbarBack.setOnClickListener() {
             onBackPressed()
         }*/
        val btnSave = findViewById<Button>(R.id.btnSaveAddress)
        val edittxtAddress = findViewById<EditText>(R.id.edittxtAddress)
        val edittxtCityName= findViewById<EditText>(R.id.edittxtCityName)
        val edittxtstate= findViewById<EditText>(R.id.edittxtstate)
        val edittxtZipCode=findViewById<EditText>(R.id.edittxtZipCode)
        val edittxtCountry= findViewById<EditText>(R.id.edittxtCountry)
        addressDb= Room.databaseBuilder(
            applicationContext,AddressDb::class.java,"address"
        ).build()
        getData()

        btnSave.setOnClickListener {
            if(!addressEmpty() or !landMarkEmpty() or !cityNameEmpty() or !stateNameEmpty() or !countryNameEmpty() or !zipcodeEmpty()){
                return@setOnClickListener
            }else{
                val finalAdd = edittxtAddress.text.toString().trim()
                val intent = Intent()
                intent.putExtra("finalAddress",finalAdd)
                setResult(101 ,intent)
                finish()
            }

            val intent = Intent(this, AddressListActivity::class.java)
            startActivity(intent)
            list.add(AddressInfo(0,edittxtAddress?.text.toString(),edittxtCityName?.text.toString(),edittxtstate?.text.toString(),
                edittxtZipCode?.text.toString(),edittxtCountry?.text.toString()))
            GlobalScope.launch {
                addressDb.addressDao().addAdreess(
                    Addressentity(0,edittxtAddress?.text.toString(),edittxtCityName?.text.toString(),edittxtstate?.text.toString(),
                        edittxtZipCode?.text.toString(),edittxtCountry?.text.toString())
                )
            }

        }
    }

    fun getData(){
        Thread{
            addressDb.addressDao().getData().forEachIndexed { index, entity ->
                list.add(AddressInfo(entity.id,entity.address,entity.city,entity.state,entity.zipcode,entity.country))

            }
        }.start()

    }
    private fun addressEmpty(): Boolean {
        if(edittxtAddress.text.isEmpty()){
            edittxtAddress.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtAddress.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun zipcodeEmpty(): Boolean {
        if(edittxtZipCode.text.isEmpty()){
            edittxtZipCode.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else if(edittxtZipCode.text.length == 6){
            edittxtZipCode.setBackgroundResource(R.drawable.border_white)
            return true
        }else
            Toast.makeText(this,"Please enter valid zipcode", Toast.LENGTH_LONG).show()
        return false

    }

    private fun countryNameEmpty(): Boolean {
        if(edittxtCountry.text.isEmpty()){
            edittxtCountry.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtCountry.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun stateNameEmpty(): Boolean {
        if(edittxtstate.text.isEmpty()){
            edittxtstate.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtstate.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun cityNameEmpty(): Boolean {
        if(edittxtCityName.text.isEmpty()){
            edittxtCityName.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else
            edittxtCityName.setBackgroundResource(R.drawable.border_white)
        return true
    }

    private fun landMarkEmpty(): Boolean {
        if(edittxtLandMark.text.isEmpty()){
            edittxtLandMark.setBackgroundResource(R.drawable.image_border_red)
            return false
        }else{
            edittxtLandMark.setBackgroundResource(R.drawable.border_white)
            return true
        }
    }
}
