package com.example.neostore.activities

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.synnapps.carouselview.ImageListener
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import com.bumptech.glide.Glide
import com.example.neostore.R
import com.example.neostore.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() ,View.OnClickListener {

    var imageArray : ArrayList<Int> = ArrayList()
    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolBar)

        imageArray.add(R.drawable.slider_img1)
        imageArray.add(R.drawable.slider_img2)
        imageArray.add(R.drawable.slider_img3)
        imageArray.add(R.drawable.slider_img4)

        carouselView.pageCount = imageArray.size
        carouselView.setImageListener(imageListerner)

        val firstName = SharedPreferenceManager.getInstance(this).data.first_name
        val lastName = SharedPreferenceManager.getInstance(this).data.last_name
        val email = SharedPreferenceManager.getInstance(this).data.email

        val nav = NavigationView.getHeaderView(0)
        val mName:TextView = nav.findViewById(R.id.txtProfileName)
        val mEmail:TextView = nav.findViewById(R.id.txtProfileEmail)
        val mProfile:ImageView = nav.findViewById(R.id.profileImage)

        val ProfilePicUrl = SharedPreferenceManager.getInstance(this).data.profile_pic
        mName.text = "$firstName" + " $lastName"
        mEmail.text = email

        Glide.with(this).load(ProfilePicUrl).placeholder(R.drawable.profileavtar).into(mProfile)

        imgTableIcon.setOnClickListener(this)
        imgChairIcon.setOnClickListener(this)
        imgSofaIcon.setOnClickListener(this)
        imgCupBoardIcon.setOnClickListener(this)

        toggle =
            object : ActionBarDrawerToggle(this, navDrawer, R.string.open, R.string.close) {
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)
                }

                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.menu_icon)
                }
            }
        navDrawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        NavigationView.setNavigationItemSelectedListener(){
            val intent = Intent(this,ProductListingActivity::class.java)
            when(it.itemId){

                R.id.logoutOption -> {SharedPreferenceManager.getInstance(this).clear()
                    onStart()}
                R.id.tablesOption ->{intent.putExtra("CATEGORY_VALUE","1")
                    startActivity(intent)}
                R.id.chairsOption ->{intent.putExtra("CATEGORY_VALUE","2")
                    startActivity(intent)}
                R.id.sofasOption ->{intent.putExtra("CATEGORY_VALUE","3")
                    startActivity(intent)}
                R.id.cupboardOption->{intent.putExtra("CATEGORY_VALUE","4")
                    startActivity(intent)}
                R.id.myAccountOption->startActivity(Intent(this,MyAccountActivity::class.java))
                R.id.myCartOption->startActivity(Intent(this,MyCartActivity::class.java))
                R.id.myOrdersOption->startActivity(Intent(this,MyOrdersActivity::class.java))
                R.id.address->startActivity(Intent(this,AddressListActivity::class.java))
            }
            true
        }
    }

    var imageListerner = ImageListener{position, imageView -> imageView.setImageResource(imageArray[position]) }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return (true)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onStart() {
        super.onStart()
        if(!SharedPreferenceManager.getInstance(this).loggedIn){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = this.getSharedPreferences("my_private_sharedpref", Context.MODE_PRIVATE)
        val cartVal = sharedPreferences.getInt("total_carts",0)

        val gallery = MenuItemCompat.getActionView(
            NavigationView.getMenu().findItem(R.id.myCartOption)
        ) as TextView
        Log.d(TAG, "onResume: val $cartVal")

        if(cartVal == 0)
            gallery.visibility = View.GONE
        gallery.setText("$cartVal")
    }


    override fun onClick(view: View?) {

        val intent = Intent(this,ProductListingActivity::class.java)
        when(view?.id){
            R.id.imgTableIcon -> {intent.putExtra("CATEGORY_VALUE","1")
                startActivity(intent)}
            R.id.imgChairIcon -> {intent.putExtra("CATEGORY_VALUE","2")
                startActivity(intent)}
            R.id.imgSofaIcon->{intent.putExtra("CATEGORY_VALUE","3")
                startActivity(intent)}
            R.id.imgCupBoardIcon->{intent.putExtra("CATEGORY_VALUE","4")
                startActivity(intent)}

            else-> Log.d(TAG, "onClick: not clicked")
        }
    }
}