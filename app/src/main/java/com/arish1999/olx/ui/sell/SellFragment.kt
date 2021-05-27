package com.arish1999.olx.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.model.CategoriesModel
import com.arish1999.olx.ui.home.adapter.CategoriesAdapter
import com.arish1999.olx.ui.sell.adapter.SellAdapter
import com.arish1999.olx.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : BaseFragment(), SellAdapter.ItemClickListener {

    private lateinit var sellAdapter: SellAdapter
    val db = FirebaseFirestore.getInstance()
    private lateinit var categoriesModel: MutableList<CategoriesModel>



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_sell, container, false)

        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCategoryList()
    }

    private fun getCategoryList() {
        showProgressbar()
        db.collection("Categories").get().addOnSuccessListener {Result->
            hideProgressbar()
            categoriesModel = Result.toObjects(CategoriesModel::class.java)
            setAdaptor()

        }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(), Toast.LENGTH_LONG).show()
                }
    }

    private fun setAdaptor() {
        rv_offerings.layoutManager = GridLayoutManager(context,3)
        sellAdapter = SellAdapter(categoriesModel,this)
        rv_offerings.adapter = sellAdapter

    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putString(Constants.KEY,categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_sell_to_include_details,bundle)

    }

}



