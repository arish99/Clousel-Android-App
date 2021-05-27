package com.arish1999.olx.ui.uploadPhoto.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.arish1999.olx.R

import com.bumptech.glide.Glide

class UploadImageAdapter (internal var activity: Activity,
                          internal var imagesArrayList: ArrayList<String>,
internal var itemClick : ItemClickListener)
    : RecyclerView.Adapter<UploadImageAdapter.ViewHolder>()
{
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.adapter_image_upload,parent,false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position<imagesArrayList.size)
        {
            val s=imagesArrayList[position]
            Glide.with(context).load(s).into(holder.imageView)
            holder.imageView.setOnClickListener(View.OnClickListener {
                if (position==imagesArrayList.size){
                    itemClick.onItemClick()
                }
            })
        }


    }

    override fun getItemCount(): Int {
        return imagesArrayList.size+1

    }



    fun customNotify(selectedImagesArrayList: ArrayList<String>) {
        this.imagesArrayList=selectedImagesArrayList
        notifyDataSetChanged()

    }


    class ViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
        val imageView=itemView.findViewById<ImageView>(R.id.imageView)




    }

    interface ItemClickListener{
        fun onItemClick()
    }

}