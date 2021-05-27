package com.arish1999.olx.ui.browseCategories.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arish1999.olx.R

import com.arish1999.olx.model.DataItemModel
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref

import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.adapter_myadds.*


class BrowseCategoryAdapter (var dataitemModel: MutableList<DataItemModel>, var mClickListener: ItemClickListener):
        RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder>()
{
    private lateinit var context: Context
    private val db= FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_myadds,parent,false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.price.setText(Constants.CURRENCY_SYMBOL+dataitemModel.get(position).PRICE)
        holder.brand.setText(dataitemModel.get(position).BRAND)

        holder.address.setText(dataitemModel.get(position).ADDRESS)
        holder.date.setText(dataitemModel.get(position).CREATED_Date.toString())



        holder.date.setText(dataitemModel.get(position).CREATED_Date)

        Glide.with(context).
        load(dataitemModel.
        get(position).images.get(0)).
        into(holder.imageView)


       /* db.collection("favourites")
                .document(SharedPref(context)
                        .getString(Constants.EMAIL))
                .collection("ads")
                .document(dataitemModel.get(position).Id).get().addOnSuccessListener {
                    if(it.exists())
                    {
                        holder.fB.text = context.getString(R.string.remove_favourite)
                    }
                    else
                    {
                        holder.fB.text = context.getString(R.string.add_to_favourites)
                    }
                }*/


        holder.fB.setOnClickListener {
            mClickListener.onFavClick(position)
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            mClickListener.onItemClick(position)
        })

    }

    override fun getItemCount(): Int {
        return dataitemModel.size

    }

    fun updateList(temp: MutableList<DataItemModel>) {
        dataitemModel = temp
        notifyDataSetChanged()

    }


    class ViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
        val imageView= itemView.findViewById<ImageView>(R.id.myAddImageView)!!
        val price=itemView.findViewById<TextView>(R.id.tvPrice)!!
        val brand = itemView.findViewById<TextView>(R.id.tvBrand)!!
        val address = itemView.findViewById<TextView>(R.id.tvAddress)!!
        val date = itemView.findViewById<TextView>(R.id.tvdate)!!
        val fB = itemView.findViewById<Button>(R.id.favButton)



    }

    interface ItemClickListener{
        fun onItemClick(position: Int)
        fun onFavClick(position: Int)


    }

}
