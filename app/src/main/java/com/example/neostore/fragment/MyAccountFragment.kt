package com.example.neostore.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.neostore.R
import com.example.neostore.activities.MyAccountActivity
import com.example.neostore.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyAccountFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myAccount = activity as MyAccountActivity
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)

        val AccFirstName = SharedPreferenceManager.getInstance(myAccount).data.first_name
        val AccLastName = SharedPreferenceManager.getInstance(myAccount).data.last_name
        val AccEmail = SharedPreferenceManager.getInstance(myAccount).data.email
        val AccPhoneNumber = SharedPreferenceManager.getInstance(myAccount).data.phone_no
        val AccDob = SharedPreferenceManager.getInstance(myAccount).data.dob
        val AccProfilePic = SharedPreferenceManager.getInstance(myAccount).data.profile_pic

        view.txtAccFirstName.text = AccFirstName
        view.txtAccLastName.text = AccLastName
        view.txtAccEmail.text = AccEmail
        view.txtAccPhonenumber.text = AccPhoneNumber.toString()
        view.txtAccDOB.text = AccDob
        Log.d(TAG, "onCreateView: pic $AccProfilePic")
        Glide.with(activity as MyAccountActivity).load(AccProfilePic).placeholder(R.drawable.profileavtar)
            .into(view.profileImage)

          view.btnEditProfile.setOnClickListener(){
            val editProfileFragment :Fragment = EditProfileFragment()
            myAccount.supportFragmentManager.beginTransaction().replace(R.id.frameLayout,editProfileFragment,"myAccoutnfrgament")
                .addToBackStack("Edit_Profile")
                .commit()
        }

        view.myAccountTollbarBack.setOnClickListener(){
            (activity as MyAccountActivity).onBackPressed()
        }

        view.btnResetPassword.setOnClickListener(){
            val resetPassword :Fragment = ResetPasswordFragment()
            myAccount.supportFragmentManager.beginTransaction().replace(R.id.frameLayout,resetPassword,"resetPasswordFragment")
                .addToBackStack("Reset_Password")
                .commit()
        }


        return view
    }


    }


