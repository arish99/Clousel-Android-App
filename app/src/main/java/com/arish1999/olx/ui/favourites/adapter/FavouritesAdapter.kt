package com.arish1999.olx.ui.favourites.adapter

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
import com.bumptech.glide.Glide

class FavouritesAdapter (var dataitemModel: MutableList<DataItemModel>, var mClickListener: FavouritesAdapter.ClickListener):
        RecyclerView.Adapter<FavouritesAdapter.ViewHolder>()


{
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_favourites,parent,false)
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

       holder.delBut.setOnClickListener {
           mClickListener.delClick(position)
       }


        holder.itemView.setOnClickListener(View.OnClickListener {
            mClickListener.onItemClick(position)
        })

    }

    override fun getItemCount(): Int {
        return dataitemModel.size

    }

    fun updateList(temp: MutableList<DataItemModel>) {
        dataitemModel.clear()
        dataitemModel.addAll(temp)
        this.notifyDataSetChanged()

    }


    class ViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
        val imageView= itemView.findViewById<ImageView>(R.id.favImageView)!!
        val price=itemView.findViewById<TextView>(R.id.favPrice)!!
        val brand = itemView.findViewById<TextView>(R.id.favBrand)!!
        val address = itemView.findViewById<TextView>(R.id.favAddress)!!
        val date = itemView.findViewById<TextView>(R.id.favDate)!!
        val delBut = itemView.findViewById<Button>(R.id.favDelete)



    }

    interface ClickListener{
        fun onItemClick(position: Int)
        fun delClick(position: Int)


    }

}