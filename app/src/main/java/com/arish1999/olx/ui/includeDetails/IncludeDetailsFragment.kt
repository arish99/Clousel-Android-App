package com.arish1999.olx.ui.includeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.util.Constants
import kotlinx.android.synthetic.main.fragment_include_details.*

class IncludeDetailsFragment : BaseFragment(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_include_details, container, false)
        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listener()
    }

    private fun listener() {
        text_view_next.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id)
        {
            R.id.text_view_next->{
                sendData()
            }
        }

    }

    private fun sendData() {
        if(edBrand.text?.isEmpty()!!)
         edBrand.setError(getString(R.string.enter_brand_name))
        else if(edYear.text?.isEmpty()!!)
            edYear.setError(getString(R.string.enter_year))
        else if(edAdTitle.text?.isEmpty()!!)
            edAdTitle.setError(getString(R.string.enter_ad_title))
        else if(edDescribe.text?.isEmpty()!!)
            edDescribe.setError(getString(R.string.enter_desc))
        else if(edPhone.text?.isEmpty()!!)
            edPhone.setError(getString(R.string.enter_phone))
        else if(edAddress.text?.isEmpty()!!)
            edAddress.setError(getString(R.string.enter_phone))
        else if(edPrice.text?.isEmpty()!!)
            edPrice.setError(getString(R.string.enter_price))
        else
        {
            val bundle = Bundle()
            bundle.putString(Constants.BRAND,edBrand.text.toString())
            bundle.putString(Constants.YEAR,edYear.text.toString())
            bundle.putString(Constants.TITLE,edAdTitle.text.toString())
            bundle.putString(Constants.DESCRIPTION,edDescribe.text.toString())
            bundle.putString(Constants.ADDRESS,edAddress.text.toString())
            bundle.putString(Constants.PHONE,edPhone.text.toString())
            bundle.putString(Constants.PRICE,edPrice.text.toString())
            bundle.putString(Constants.KEY,arguments?.getString(Constants.KEY))
            findNavController().navigate(R.id.action_include_details_to_upload_photo,bundle)
        }

    }
}