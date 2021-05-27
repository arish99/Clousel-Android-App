package com.arish1999.olx.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.R
import com.arish1999.olx.model.DataItemModel
import com.arish1999.olx.ui.favourites.adapter.FavouritesAdapter
import com.arish1999.olx.ui.myAdds.adapter.MyAdsAdapter
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.adapter_myadds.*
import kotlinx.android.synthetic.main.fragment_favourites.*

class FavouritesFragment : BaseFragment(),FavouritesAdapter.ClickListener {
    private lateinit var dataItemModel: MutableList<DataItemModel>
    private lateinit var favouritesAdapter: FavouritesAdapter
    private val db= FirebaseFirestore.getInstance()
    private var documentIdList: MutableList<DataItemModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favourites,container,false)
        return root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getFavouritesList()
        rv_favourites.layoutManager = LinearLayoutManager(context)
    }

    private fun getFavouritesList() {
        showProgressbar()
        db.collection("favourites")
                .document(SharedPref(requireActivity())
                        .getString(Constants.EMAIL))
                .collection("ads")
                .get().addOnSuccessListener {
                    hideProgressbar()
                    dataItemModel = it.toObjects(DataItemModel::class.java)
                    documentIdList.addAll(dataItemModel)

                    setAdapter()
                }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                }
    }

    private fun setAdapter() {
        favouritesAdapter = FavouritesAdapter(dataItemModel,this)
        if(rv_favourites!=null)
        {
            rv_favourites.adapter = favouritesAdapter


        }

    }

    override fun onItemClick(position: Int) {
        var bundle = Bundle()
        bundle.putString(Constants.KEY,documentIdList[position].TYPE)
        bundle.putString(Constants.Id,documentIdList.get(position).Id)
        findNavController().navigate(R.id.action_favourites_to_details,bundle)

    }

    override fun delClick(position: Int) {

        showProgressbar()
        db.collection("favourites")
                .document(SharedPref(requireActivity())
                        .getString(Constants.EMAIL))
                .collection("ads")
                .document(dataItemModel.get(position).Id)
                .delete().addOnSuccessListener {


                    getFavouritesList()


                   //favouritesAdapter.updateList(documentIdList)

              //  favouritesAdapter = FavouritesAdapter(dataItemModel,this)



                    //favButton.text = getString(R.string.add_to_favourites)

                    hideProgressbar()

                    Toast.makeText(requireActivity(),"Removed from favourites",Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                }

        hideProgressbar()





    }



}


