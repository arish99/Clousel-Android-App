package com.arish1999.olx.ui.browseCategories

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.model.DataItemModel
import com.arish1999.olx.ui.browseCategories.adapter.BrowseCategoryAdapter
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.adapter_myadds.*
import kotlinx.android.synthetic.main.fragment_browse_category.*
import kotlinx.android.synthetic.main.fragment_browse_category.edSearch

class BrowseCategoryFragment : BaseFragment(), BrowseCategoryAdapter.ItemClickListener {
    private lateinit var categoriesAdapter: BrowseCategoryAdapter
    private lateinit var dataItemModel: MutableList<DataItemModel>
    private val db= FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_browse_category,container,false)
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       getAdList()
        textListener()




    }








    private fun getAdList() {
        showProgressbar()
        db.collection(arguments?.getString(Constants.KEY)!!).get().addOnSuccessListener {
            hideProgressbar()
            dataItemModel = it.toObjects(DataItemModel::class.java)
            setAdaptor()

        }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                }

    }

    private fun setAdaptor() {
        rv_browse_categories.layoutManager = LinearLayoutManager(context)
        categoriesAdapter = BrowseCategoryAdapter(dataItemModel,this)
        rv_browse_categories.adapter = categoriesAdapter
    }

    override fun onItemClick(position: Int) {
        var bundle = Bundle()
        bundle.putString(Constants.Id, dataItemModel.get(position).Id)
        bundle.putString(Constants.KEY,dataItemModel.get(position).TYPE)
        findNavController().navigate(R.id.action_browse_to_deails,bundle)

    }

    override fun onFavClick(position: Int) {
       // Toast.makeText(requireActivity(),"Fav Button Clicked",Toast.LENGTH_LONG).show()


        if(favButton.text.equals(getString(R.string.add_to_favourites)))
        {



            showProgressbar()

          db.collection("favourites").document(SharedPref(requireActivity())
                    .getString(Constants.EMAIL)).collection("ads").document(dataItemModel.get(position).Id).get().addOnSuccessListener {

                        if(it.exists()) {
                            Toast.makeText(requireActivity(), "Already added in favourites", Toast.LENGTH_LONG).show()
                            hideProgressbar()
                        }

                 else{


                            db.collection(arguments?.getString(Constants.KEY)!!).document(dataItemModel.get(position).Id).get().addOnSuccessListener {

                                val data = it.data
                                val path = dataItemModel.get(position).Id
                                db.collection("favourites")
                                        .document(SharedPref(requireActivity())
                                                .getString(Constants.EMAIL))
                                        .collection("ads")
                                        .document(path).set(data!!)
                                        .addOnSuccessListener {

                                            /* db.collection("favourites").
                                             document(SharedPref(requireActivity())
                                                     .getString(Constants.EMAIL))
                                                     .collection("ads").document(path).delete().addOnSuccessListener {  }*///deletion

                                            //favButton.text = getString(R.string.remove_favourite)

                                            hideProgressbar()

                                            Toast.makeText(requireActivity(),"Added to favourites",Toast.LENGTH_SHORT).show()


                                        }

                                //Toast.makeText(requireActivity(),"Getting Data",Toast.LENGTH_LONG).show()
                            }
                                    .addOnFailureListener{
                                        Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                                    }
                        }
                        }
                  .addOnFailureListener{
                      Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                  }

        }






       /* else if(favButton.text.equals(getString(R.string.remove_favourite)))
        {
            showProgressbar()
            db.collection("favourites")
                    .document(SharedPref(requireActivity())
                            .getString(Constants.EMAIL))
                    .collection("ads")
                    .document(dataItemModel.get(position).Id)
                    .delete().addOnSuccessListener {

                        favButton.text = getString(R.string.add_to_favourites)
                        hideProgressbar()

                        Toast.makeText(requireActivity(),"Removed from favourites",Toast.LENGTH_SHORT).show()
                    }



        }*/



    }




    private fun textListener() {
        edSearch.addTextChangedListener(object : TextWatcher {
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
        val temp : MutableList<DataItemModel> = ArrayList()
        for(data in dataItemModel)
        {
            if(data.BRAND.contains(toString.capitalize()) || data.BRAND.contains(toString) || data.DESCRIPTION.contains(toString) || data.DESCRIPTION.contains(toString.capitalize()))
            {
                temp.add(data)

            }
          categoriesAdapter.updateList(temp)
        }

    }
}