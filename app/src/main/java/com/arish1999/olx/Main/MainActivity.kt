package com.arish1999.olx.Main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arish1999.olx.R
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.OnActivityResultData
import net.alhazmy13.mediapicker.Image.ImagePicker

class MainActivity : AppCompatActivity() {

    lateinit var onActivityResultData: OnActivityResultData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_sell, R.id.navigation_myAds,R.id.navigation_favourites,R.id.navigation_profile))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    fun getActivityResultData(onActivityResultData: OnActivityResultData)
    {
        this.onActivityResultData = onActivityResultData
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK)
        {
            val mPaths=data?.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)
            val bundle=Bundle()
            bundle.putStringArrayList(Constants.IMAGE_PATH,mPaths)
            onActivityResultData.resultData(bundle)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            android.R.id.home->{
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}