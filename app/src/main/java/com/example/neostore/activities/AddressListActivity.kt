package com.example.neostore.activities

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.neostore.R
import com.example.neostore.api.RetrofitClient
import com.example.neostore.model.PlaceOrderResponse
import com.example.neostore.storage.*
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddressListActivity : AppCompatActivity(), Adapter.cartInterface{

    var data: MutableList<AddressInfo> = mutableListOf()
    lateinit var UserName: TextView
    lateinit var fullAddress: TextView
    lateinit var recycler_view: RecyclerView
    lateinit var adapter: Adapter
    lateinit var database: AddressDb
    lateinit var addressString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        database = Room.databaseBuilder(
            applicationContext, AddressDb::class.java, "address"
        ).build()
        initAdapter()
        getData()

        imgAddAddress.setOnClickListener() {
            val intent = Intent(this, AddAddress::class.java)
            startActivityForResult(intent, 101)
        }

        btnAddToCartTollbarBack.setOnClickListener() {
            onBackPressed()
        }
        btnPlaceOrder.setOnClickListener(){
            placeOrder()
            startActivity(Intent(this,MyOrdersActivity::class.java))
        }

    }

    private fun placeOrder() {

        val accessToken = SharedPreferenceManager.getInstance(this).data.access_token
        RetrofitClient.getClient.placeOrder(accessToken,addressString).enqueue(object : Callback<PlaceOrderResponse?> {
            override fun onResponse(
                call: Call<PlaceOrderResponse?>,
                response: Response<PlaceOrderResponse?>
            ) {
                if(response.code() == 200){
                    Toast.makeText(this@AddressListActivity,"${response.body()?.user_msg}",Toast.LENGTH_LONG).show()
                }else
                    Toast.makeText(this@AddressListActivity,"${response.errorBody()}",Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<PlaceOrderResponse?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })

    }

    private fun initAdapter() {
        recycler_view = findViewById<RecyclerView>(R.id.addressListRecyclerView)
        adapter = Adapter(data,this)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
    }

    fun getData() {
        Thread {
            database.addressDao().getData().forEachIndexed { index, entity ->
                data.add(
                    AddressInfo(
                        entity.id,
                        entity.address,
                        entity.city,
                        entity.state,
                        entity.country,
                        entity.zipcode
                    )
                )
            }
            initAdapter()
            recycler_view.visibility = android.view.View.VISIBLE
        }.start()
    }

    override fun onClick(position: Int, item: AddressInfo) {
        addressString = item.toString()
    }

    override fun onClose(data: AddressInfo,position: Int) {
        deleteAddress(Addressentity(data.id,data.address,data.city,data.state,data.zipcode,data.country))
    }



    fun deleteAddress(addressEntity: Addressentity) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Are you sure? ")
        builder.setMessage("Do you want to delete?")
        builder.setPositiveButton("Yes", { dialogInterface: DialogInterface, i: Int ->
            /*  adapter.deleteAddress(i)*/
            GlobalScope.launch {
                database.addressDao().deleteHistory(addressEntity)
            }

        })
        builder.setNegativeButton("No",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

}
