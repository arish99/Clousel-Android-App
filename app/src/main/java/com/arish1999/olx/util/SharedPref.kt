package com.arish1999.olx.util



import android.content.Context
import android.content.SharedPreferences
import com.arish1999.olx.ui.home.HomeFragment

class SharedPref(context: Context) {

    var sharedPref:SharedPreferences
    init {
        sharedPref=context.getSharedPreferences(Constants.SharedPrefName,0)
    }
    fun setString(key: String,value: String){
        sharedPref.edit().putString(key, value).commit()
    }
    fun getString(key:String):String{
        return sharedPref.getString(key,"")!!
    }

}