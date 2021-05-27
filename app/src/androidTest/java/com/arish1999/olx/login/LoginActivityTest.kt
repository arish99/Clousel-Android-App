package com.arish1999.olx.login

import android.widget.Button
import androidx.test.rule.ActivityTestRule
import com.arish1999.olx.R
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @Rule
    @JvmField
    var mLoginActivityTestRule:ActivityTestRule<LoginActivity> =
        ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    private var mLoginActivity:LoginActivity?=null

    @Before
    fun setUp() {
        mLoginActivity = mLoginActivityTestRule.activity
    }

    @Test
    fun testLaunch() {
        var view = mLoginActivity?.findViewById<Button>(R.id.ll_google)
        assertNotNull(view)


    }

    @After
    fun tearDown() {
        mLoginActivity = null
    }
}