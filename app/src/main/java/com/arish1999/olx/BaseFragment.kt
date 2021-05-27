package com.arish1999.olx

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    lateinit var mDialog : Dialog
    open fun showProgressbar(){
        mDialog= Dialog(requireActivity())
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.layout_progress_bar)
        mDialog.setCancelable(true)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }
    open fun hideProgressbar(){
        mDialog.dismiss()
    }


}