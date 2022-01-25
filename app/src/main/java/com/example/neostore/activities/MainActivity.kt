package com.example.neostore.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.neostore.R
import com.example.neostore.api.RetrofitClient
import com.example.neostore.model.DefaultResponse
import com.example.neostore.model.FetchAccountDetail
import com.example.neostore.model.LoginErrorResponse
import com.example.neostore.storage.SharedPreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        btnLoginButton.setOnClickListener{
            validateForm()
        }
        imgplus.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        txtforgotPassword.setOnClickListener(){
              startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }



    private fun validateForm(){
        if(!userNameValidate() or !passWordValidate()){
            return
        }else{
            userLogin()
        }
    }

    private fun userLogin() {
        val emailAdd = txtInputUserName.editText?.text.toString()
        val loginPassword = txtInputPassword.editText?.text.toString()

        RetrofitClient.getClient.loginUser(emailAdd,loginPassword).enqueue(object :
            Callback<DefaultResponse?> {
            override fun onResponse(
                call: Call<DefaultResponse?>,
                response: Response<DefaultResponse?>
            ) {
                if(response.code() == 401 || response.code() == 500){
                    val gson: Gson = GsonBuilder().create()
                    val error : LoginErrorResponse
                    try {
                        error = gson.fromJson(response.errorBody()?.string(), LoginErrorResponse::class.java)
                        Toast.makeText(this@MainActivity, "${error.user_msg}", Toast.LENGTH_LONG).show()
                        Log.d(ContentValues.TAG, "on404 response: ${error.message} and ${error.status}")
                    }
                    catch (e: Exception){
                        Log.d(ContentValues.TAG, "onResponse: $e")
                    }
                }else{
                    Toast.makeText(this@MainActivity , "${response.body()?.user_msg}", Toast.LENGTH_LONG).show()
                    SharedPreferenceManager.getInstance(applicationContext).saveUser(response.body()?.data!!)
                    updateCartValue()
                    val intent = Intent(this@MainActivity,HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<DefaultResponse?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun updateCartValue() {
        val acceessToken = SharedPreferenceManager.getInstance(this).data.access_token
        RetrofitClient.getClient.fetchAccountDetail(acceessToken).enqueue(object : Callback<FetchAccountDetail?> {
            override fun onResponse(
                call: Call<FetchAccountDetail?>,
                response: Response<FetchAccountDetail?>
            ) {
                if(response.body() != null){
                    val totalCarts = response.body()!!.data.total_carts
                    val sharedPreferences = this@MainActivity.getSharedPreferences("my_private_sharedpref",
                        Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("total_carts",totalCarts)
                    editor.apply()
                }
                else{
                    Toast.makeText(this@MainActivity,"${response.errorBody()}",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FetchAccountDetail?>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }


    override fun onStart() {
        super.onStart()
        if(SharedPreferenceManager.getInstance(this).loggedIn){
            val intent = Intent(applicationContext,HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

    private fun userNameValidate():Boolean{
        if(txtInputUserName.editText?.text.toString().isEmpty()){
            txtInputUserName.apply{

                error= "Filed Can't be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
        else{
            txtInputUserName.apply{
                error = null
                isExpandedHintEnabled = true
            }
            txtInputUserName.requestFocus()
            return true
        }

    }

    private fun passWordValidate():Boolean{
        if(txtInputPassword.editText?.text.toString().isEmpty()){
            txtInputPassword.apply {
                error = "field can't be blank"
                isExpandedHintEnabled = false
                requestFocus()
            }
            return false
        }
        else{
            txtInputPassword.apply {
                error = null
                isExpandedHintEnabled = true
            }
            txtInputUserName.requestFocus()
            return true

        }
    }
}
