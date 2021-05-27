package com.arish1999.olx.ui.myAdds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.model.DataItemModel
import com.arish1999.olx.ui.myAdds.adapter.MyAdsAdapter
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.adapter_myadds.*
import kotlinx.android.synthetic.main.fragment_myadds.*

class MyAdsFragment : BaseFragment(), MyAdsAdapter.ItemClickListener {
    private lateinit var dataItemModel: MutableList<DataItemModel>
    private lateinit var myAdsAdapter: MyAdsAdapter
    private val db= FirebaseFirestore.getInstance()
    private var documentIdList: MutableList<DataItemModel> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_myadds, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //myAdsAdapter.updateList(dataItemModel)

        getMyAds()
        rv_ads.layoutManager= LinearLayoutManager(context)

    }

    private fun getMyAds() {

        showProgressbar()
        db.collection(Constants.CATEGORIES!!)
                .get()
                .addOnSuccessListener {
                    for (i in it.documents){
                        getDataFromKey(i.getString(Constants.KEY)!!)
                        //print(i)

                    }
                }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                }


    }

    private fun getDataFromKey(key: String) {
        db.collection(key)
                .whereEqualTo("USER_ID", SharedPref(requireActivity()).getString(Constants.USER_ID))
                .get()
                .addOnSuccessListener {
                    hideProgressbar()
                    dataItemModel=it.toObjects(DataItemModel::class.java)
                    documentIdList.addAll(dataItemModel)

                    setAdapter()
                }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(), Toast.LENGTH_LONG).show()
                }

    }

    private fun setAdapter() {
        myAdsAdapter =
                MyAdsAdapter(documentIdList, this)
        if (rv_ads != null)
            rv_ads.adapter = myAdsAdapter

    }

    override fun onItemClick(position: Int) {
        var bundle = Bundle()
        bundle.putString(Constants.KEY,documentIdList[position].TYPE)
        bundle.putString(Constants.Id,documentIdList.get(position).Id)
        findNavController().navigate(R.id.action_myads_to_details,bundle)


    }

}