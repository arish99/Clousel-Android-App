package com.arish1999.olx.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.login.LoginActivity
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile,container,false)
        return root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
        listener()

    }

    private fun listener() {
        settingsB.setOnClickListener(this)
        logoutB.setOnClickListener(this)
    }

    private fun setData() {
        tvUserName.text = SharedPref(requireActivity()).getString(Constants.USER_NAME)
        tvUserEmail.text = SharedPref(requireActivity()).getString(Constants.EMAIL)
        Glide.with(requireActivity()).load(SharedPref(requireActivity()).getString(Constants.PHOTO)).placeholder(R.drawable.avatar).into(imageUser)
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.settingsB->{
                findNavController().navigate(R.id.action_profile_to_settings)

            }
            R.id.logoutB->{
                showAlertDialog()
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.logout)
        builder.setMessage(R.string.logout_message)
        builder.setIcon(R.drawable.ic_warning)
        builder.setPositiveButton(getString(R.string.yes)){ dialogInterface, which ->
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut();
            clearSession()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(getString(R.string.no)){ dialogInterface, which ->
            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    private fun clearSession() {
        SharedPref(requireActivity()).setString(Constants.USER_NAME,"")
        SharedPref(requireActivity()).setString(Constants.USER_ID,"")
        SharedPref(requireActivity()).setString(Constants.EMAIL,"")
        SharedPref(requireActivity()).setString(Constants.PHOTO,"")
        SharedPref(requireActivity()).setString(Constants.PHONE!!,"")
        

    }
}