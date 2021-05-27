package com.arish1999.olx.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_settings,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edFullNameSettings.setText(SharedPref(requireActivity()).getString(Constants.USER_NAME))
        edemailAddressSettings.setText(SharedPref(requireActivity()).getString(Constants.EMAIL))
        edPhoneSettings.setText(SharedPref(requireActivity()).getString(Constants.PHONE!!))
        edPostalAddressSettings.setText(SharedPref(requireActivity()).getString(Constants.ADDRESS!!))
        listener()

    }

    private fun listener() {
        tvSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.tvSave->{
                saveData()
            }
        }
    }

    private fun saveData() {
        if (edFullNameSettings.text?.isEmpty()!!) {
            edFullNameSettings.setError(getString(R.string.enter_full_name))
        } else if (edemailAddressSettings.text.toString().isEmpty()) {
            edemailAddressSettings.setError(getString(R.string.enter_email))
        } else if (edPhoneSettings.text.toString().isEmpty()) {
            edPhoneSettings.setError(getString(R.string.enter_phone_number))
        }
            else if(edPostalAddressSettings.text.toString().isEmpty())
        {
                edPostalAddressSettings.setError(getString(R.string.enter_postal_address))
        }

        else {
            SharedPref(requireActivity()).setString(Constants.USER_NAME, edFullNameSettings.text.toString())
            SharedPref(requireActivity()).setString(Constants.EMAIL, edemailAddressSettings.text.toString())
            SharedPref(requireActivity()).setString(Constants.PHONE!!, edPhoneSettings.text.toString())
            SharedPref(requireActivity()).setString(Constants.ADDRESS!!, edPostalAddressSettings.text.toString())
            Toast.makeText(requireActivity(), getString(R.string.saved_success), Toast.LENGTH_LONG).show()
            fragmentManager?.popBackStack()
        }

    }
}