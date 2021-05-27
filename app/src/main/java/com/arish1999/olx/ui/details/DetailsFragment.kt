package com.arish1999.olx.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.model.DataItemModel
import com.arish1999.olx.previewImage.PreviewImageActivity
import com.arish1999.olx.ui.details.adapter.DetailImagesAdapter
import com.arish1999.olx.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : BaseFragment(), DetailImagesAdapter.onItemClick {
    private lateinit var dataItemModel: DataItemModel
    val db= FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_details,container,false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getItemDetails()
        clickListener()

    }

    private fun clickListener() {
        tvCall.setOnClickListener(View.OnClickListener {
            val intent= Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${dataItemModel.PHONE}")
            startActivity(intent)
        })
    }

    private fun getItemDetails() {
        showProgressbar()
        db.collection(arguments?.getString(Constants.KEY)!!)
                .document(arguments?.getString(Constants.Id)!!)
                .get()
                .addOnSuccessListener {
                    hideProgressbar()
                    dataItemModel=it.toObject(DataItemModel::class.java)!!
                    setData()
                    setPagerAdapter()
                }


    }

    private fun setPagerAdapter() {
        val detailimagesAdapter = DetailImagesAdapter(requireContext(),dataItemModel.images,this)
        viewPager.adapter =  detailimagesAdapter
        viewPager.offscreenPageLimit=1


    }

    private fun setData() {
        tvPrice.text = Constants.CURRENCY_SYMBOL+ dataItemModel.PRICE
        tvBrand.text = dataItemModel.BRAND
        tvYear.text = dataItemModel.YEAR.toString()
        tvDesc.text = dataItemModel.DESCRIPTION
        tvAddress.text = dataItemModel.ADDRESS
        tvdate.text = dataItemModel.CREATED_Date
        tvPhone.text = dataItemModel.PHONE.toString()

    }

    override fun OnClick(position: Int) {
        startActivity(Intent(activity,PreviewImageActivity::class.java).putExtra("imageUrl",dataItemModel.images.get(position)
        )
        )

    }
}