package com.arish1999.olx.ui.sell.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arish1999.olx.R
import com.arish1999.olx.model.CategoriesModel
import com.bumptech.glide.Glide

class SellAdapter (var categoriesList: MutableList<CategoriesModel>, var mClickListener: ItemClickListener) :
RecyclerView.Adapter<SellAdapter.ViewHolder>()
{
   private lateinit var context:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_sell,parent,false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = categoriesList.get(position).key
        Glide.with(context).
        load(categoriesList.
        get(position).image_bw).
        into(holder.imageView)

        holder.itemView.setOnClickListener(View.OnClickListener {
            mClickListener.onItemClick(position)
        })

    }

    override fun getItemCount(): Int {
        return categoriesList.size

    }

    fun updateList(temp: MutableList<CategoriesModel>) {
        categoriesList = temp
        notifyDataSetChanged()

    }


    class ViewHolder(itemView:View)  : RecyclerView.ViewHolder(itemView) {
        val imageView=itemView.findViewById<ImageView>(R.id.ivIcon)
        val textView=itemView.findViewById<TextView>(R.id.tvTitle)


    }

    interface ItemClickListener{
        fun onItemClick(position: Int)
    }

}