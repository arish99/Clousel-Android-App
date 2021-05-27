package com.arish1999.olx.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.arish1999.olx.BaseActivity
import com.arish1999.olx.Main.MainActivity

import com.arish1999.olx.R
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref

import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : BaseActivity(), View.OnClickListener {
    companion object{
        private const val RC_SIGN_IN = 120
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val EMAIL = "email"
    private var callbackManager : CallbackManager ?=null
    private lateinit var GButton : Button
    private lateinit var FButton : Button
    private lateinit var LBUTTON : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FacebookSdk.sdkInitialize(getApplicationContext())
        //AppEventsLogger.activateApp(this);
        mAuth = FirebaseAuth.getInstance()
        //clicklisteners()
        configureGoogleSignIn()
        registerFbcallBack()

        GButton = findViewById(R.id.ll_google)
        FButton = findViewById(R.id.ll_facebook)
        LBUTTON = findViewById(R.id.login_button)

        GButton.setOnClickListener(this)
        FButton.setOnClickListener(this)


        Log.d("Gooddddddddddd","sfdfsdfvsvsxvxv")


      /*  try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }*/


    }

   /*private fun registerFbcallButtonBack() {
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {

                handleFacebookAccess(loginResult?.accessToken)
            }

            override fun onCancel() {
                Log.d("LoginActivity", "facebook:onCancel")
            }

            override fun onError(exception: FacebookException) {
                Log.d("LoginActivity", "facebook:onError", exception)
            }
        })
    }*/



     private fun registerFbcallBack() {
         callbackManager = CallbackManager.Factory.create()
          login_button.setReadPermissions(Arrays.asList(EMAIL,"public_profile"));
         LoginManager.getInstance().registerCallback(callbackManager,
             object : FacebookCallback<LoginResult?> {
                 override fun onSuccess(loginResult: LoginResult?) {
                     // App code
                     Log.d("LoginActivity", "facebook:onSuccess:" + loginResult);
                     handleFacebookAccess(loginResult?.accessToken)
                 }

                 override fun onCancel() {
                     Log.d("LoginActivity", "facebook:onCancel")
                 }

                 override fun onError(exception: FacebookException) {
                     Log.d("LoginActivity", "facebook:onError", exception)
                 }
             })
     }

    private fun handleFacebookAccess(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken?.token!!)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d("LoginActivity", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    //Toast.makeText(this, user?.displayName, Toast.LENGTH_LONG).show()
                    if (user!=null) {
                        sendUserToHomeScreen(user!!)
                    }


                    //finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed:"+task.exception,
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

    }


    private fun configureGoogleSignIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

  /*  private fun clicklisteners() {
       ll_google.setOnClickListener(this)
        ll_facebook.setOnClickListener(this)

    }*/
    override fun onClick(v: View?) {
        when(v?.id)
        {
           R.id.ll_facebook -> {

               login_button.performClick()

               Log.d("facebook","BUTTONclicked")
            }
            R.id.ll_google -> {
                signIn()

            }
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful)
            {
                try {

                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("LoginActivity", "Google sign in failed", e)
                }
            }


        }
        else
        {
            callbackManager?.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information


                    //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                    val user = mAuth.currentUser
                    if (user!=null) {
                        sendUserToHomeScreen(user!!)
                    }


                    //Toast.makeText(this, "user:" + user?.email, Toast.LENGTH_LONG).show()

                    //finish()


                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Google SignIn Failed", Toast.LENGTH_LONG).show()

                }
            }
    }


    private fun sendUserToHomeScreen(user: FirebaseUser?) {
        if(user?.email!=null)
            SharedPref(this).setString(Constants.EMAIL,user.email!!)
        if(user?.uid!=null)
            SharedPref(this).setString(Constants.USER_ID,user.uid!!)
        if(user?.displayName!=null)
            SharedPref(this).setString(Constants.USER_NAME,user.displayName!!)
        if(user?.photoUrl!=null)
            SharedPref(this).setString(Constants.PHOTO, user.photoUrl.toString()!!)
        val intent =Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }




}