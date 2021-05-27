package com.arish1999.olx.ui.details.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.arish1999.olx.R
import com.bumptech.glide.Glide

class DetailImagesAdapter (
       var context: Context,
        val  imageList:ArrayList<String>,
        var itemClick : onItemClick) : PagerAdapter() {

    private val inflater: LayoutInflater
    private var isDatasetChanged=false

    init {
        inflater=LayoutInflater.from(context)
    }


    override fun getCount(): Int {
        if (isDatasetChanged){
            isDatasetChanged=false
            notifyDataSetChanged()
        }
        return imageList.size

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.adapter_detail_images, container, false)!!
        val imageView = view.findViewById<ImageView>(R.id.pagerImage)
        imageView.setOnClickListener(View.OnClickListener {
            itemClick.OnClick(position)
        })
        Glide.with(context).load(imageList.get(position)).placeholder(R.drawable.big_placeholder).into(imageView!!)
        container.addView(view,0)
        return view

    }

    override fun saveState(): Parcelable? {
        return null

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)

    }

    interface onItemClick{
        fun OnClick(position: Int)
    }
}