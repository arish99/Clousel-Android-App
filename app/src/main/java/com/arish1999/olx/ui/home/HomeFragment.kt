package com.arish1999.olx.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.model.CategoriesModel
import com.arish1999.olx.ui.home.adapter.CategoriesAdapter
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), CategoriesAdapter.ItemClickListener {

    private lateinit var adapter: CategoriesAdapter
    val db = FirebaseFirestore.getInstance()
    private lateinit var categoriesModel: MutableList<CategoriesModel>



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvCityName.text = SharedPref(requireActivity()).getString(Constants.CITY_NAME)
        getCategoryList()
        textListener()
    }

    private fun textListener() {
        edSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())

            }

        })
    }

    private fun filterList(toString: String) {
        val temp : MutableList<CategoriesModel> = ArrayList()
        for(data in categoriesModel)
        {
            if(data.key.contains(toString.capitalize()) || data.key.contains(toString))
            {
                temp.add(data)

            }
            adapter.updateList(temp)
        }

    }

    private fun getCategoryList() {
        showProgressbar()
        db.collection("Categories").get().addOnSuccessListener {Result->
            hideProgressbar()
            categoriesModel = Result.toObjects(CategoriesModel::class.java)
            setAdaptor()

        }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                }
    }

    private fun setAdaptor() {
        rv_categories.layoutManager = GridLayoutManager(context,3)
        adapter = CategoriesAdapter(categoriesModel,this)
        rv_categories.adapter = adapter
    }

    override fun onItemClick(position: Int) {

        var bundle = Bundle()
        bundle.putString(Constants.KEY,categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_home_to_browse,bundle)
    }
}
