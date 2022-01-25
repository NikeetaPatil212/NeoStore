package com.example.neostore.fragment

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import java.lang.Exception
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.fragment.app.FragmentManager
import com.example.neostore.R
import com.example.neostore.activities.MyAccountActivity
import com.example.neostore.api.RetrofitClient
import com.example.neostore.model.FetchAccountDetail
import com.example.neostore.model.UpdateProfileClass
import com.example.neostore.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.registratio_actionbar_custom_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EditProfileFragment : Fragment() {

    private val IMAGE = 100
    lateinit var mbitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        mbitmap = createBitmap(1000, 1000)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val activity = activity as MyAccountActivity
        val firstName = SharedPreferenceManager.getInstance(activity).data.first_name
        val lastName = SharedPreferenceManager.getInstance(activity).data.last_name
        val email = SharedPreferenceManager.getInstance(activity).data.email
        val phoneNumber = SharedPreferenceManager.getInstance(activity).data.phone_no
        val accessToken = SharedPreferenceManager.getInstance(activity).data.access_token
        view.txtEditProfileFirstName.setText(firstName)
        view.txtEditProfileLastName.setText(lastName)
        view.txtEditProfileEmail.setText(email)
        view.txtEditProfilePhoneNumber.setText(phoneNumber.toString())

        view.editAccProfileImage.setOnClickListener(){
            selectImage()
        }

        view.editAccountBackPress.setOnClickListener(){
            activity.supportFragmentManager.popBackStack("Edit_Profile",
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        view.btnEditProfileSubmit.setOnClickListener(){
            uploadEditProfileData(accessToken)
        }

        view.txtEditProfileDob.setOnClickListener(){
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                txtEditProfileDob.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year)

            }, year, month, day)

            dpd.show()
        }

        return view
    }

    private fun uploadEditProfileData(accessToken: String) {
        val profilePic = "data:image/jpg;base64,"+convertToString()!!
        val firstName = txtEditProfileLastName.text.toString()
        val lastName = txtEditProfileFirstName.text.toString()
        val dob = txtEditProfileDob.text.toString()
        val editPhoneNumber = txtEditProfilePhoneNumber.text.toString()
        val editEmail = txtEditProfileEmail.text.toString()


        RetrofitClient.getClient.updateProfile(accessToken,firstName,lastName,editEmail,dob,editPhoneNumber,profilePic).enqueue(object : Callback<UpdateProfileClass?> {
            override fun onResponse(
                call: Call<UpdateProfileClass?>,
                response: Response<UpdateProfileClass?>
            ) {
                if(response.body() != null){

                    Toast.makeText((activity as MyAccountActivity).applicationContext, "${response.body()?.user_msg}", Toast.LENGTH_LONG).show()
                    fetchUserData(accessToken)
                }else{
                    Log.d(TAG, "onResponse: Something went wrong")

                }
            }

            override fun onFailure(call: Call<UpdateProfileClass?>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
        
        
        

    }

    private fun fetchUserData(accessToken: String) {
            RetrofitClient.getClient.fetchAccountDetail(accessToken).enqueue(object : Callback<FetchAccountDetail?> {
                override fun onResponse(
                    call: Call<FetchAccountDetail?>,
                    response: Response<FetchAccountDetail?>
                ) {
                    if(response.body() != null){
                        SharedPreferenceManager.getInstance((activity as MyAccountActivity).applicationContext)
                            .saveUser(response.body()?.data?.user_data!!)
                        Log.d(TAG, "onResponse: picprofile : ${response.body()?.data?.user_data?.profile_pic}")
                        (activity as MyAccountActivity).backPress
                    }else{
                        Log.d(TAG, "onResponse: User Detail not fetch")
                    }
                    
                    
                }

                override fun onFailure(call: Call<FetchAccountDetail?>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
    }

    private fun convertToString(): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imgByte: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgByte, Base64.DEFAULT)
    }
    private fun selectImage() {
        val imageIntent = Intent()
        imageIntent.setType("image/*")
        imageIntent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(imageIntent,IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE && resultCode == RESULT_OK && data!=null){
            val path: Uri? = data.data
            val mactivty = activity as MyAccountActivity
            try{
                mbitmap = MediaStore.Images.Media.getBitmap(mactivty.contentResolver,path)
                editAccProfileImage.setImageBitmap(mbitmap)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }

}