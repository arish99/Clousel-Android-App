package com.arish1999.olx.previewImage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ScaleGestureDetector
import com.arish1999.olx.BaseActivity
import com.arish1999.olx.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_preview.*

class PreviewImageActivity : BaseActivity() {
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val imagePath = intent.extras
        if(imagePath?.containsKey("imageUri")!!)
        {
            val imageUri = imagePath.getString("imageUri")
            val imageBitmap = BitmapFactory.decodeFile(imageUri)
            previewImage.setImageBitmap(imageBitmap)
        }

        else
        {
            val imageUrl = imagePath.getString("imageUrl")
            Glide.with(this).load(imageUrl).placeholder(R.drawable.big_placeholder).into(previewImage)
        }

        previewImage!!.scaleX = mScaleFactor
        previewImage!!.scaleY = mScaleFactor
        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        btnClose!!.setOnClickListener { finish() }


    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener (){
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            mScaleFactor *= detector?.scaleFactor!!
            mScaleFactor = Math.max(
                0.1f,
                Math.min(mScaleFactor, 10.0f)
            )

            previewImage!!.scaleX = mScaleFactor
            previewImage!!.scaleY = mScaleFactor
            return true
        }

    }

}

