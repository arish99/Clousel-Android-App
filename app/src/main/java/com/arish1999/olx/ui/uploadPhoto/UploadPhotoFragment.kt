package com.arish1999.olx.ui.uploadPhoto

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.arish1999.olx.BaseFragment
import com.arish1999.olx.Main.MainActivity
import com.arish1999.olx.R
import com.arish1999.olx.previewImage.PreviewImageActivity
import com.arish1999.olx.ui.uploadPhoto.adapter.UploadImageAdapter
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.OnActivityResultData
import com.arish1999.olx.util.SharedPref
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_upload_photo.*
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class UploadPhotoFragment : BaseFragment(), View.OnClickListener,
    UploadImageAdapter.ItemClickListener {

    private lateinit var uploadTask: UploadTask
    internal var dialog : BottomSheetDialog? = null
    internal var selectedImage : File? = null
    internal var TAG=UploadPhotoFragment::class.java.simpleName
    val db = FirebaseFirestore.getInstance()
    internal lateinit var storageRef: StorageReference
    internal lateinit var imageRef:StorageReference
    private var count=0
    internal lateinit var storage:FirebaseStorage
    private var outputFileUri: String? = null
    private val selectedImagesArrayList= ArrayList<String>()
    private  var imageAdapter : UploadImageAdapter? = null
    private val imageUriList=ArrayList<String>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {




        val root = inflater.inflate(R.layout.fragment_upload_photo, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = GridLayoutManager(activity,3)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        listener()

        registerCallbackPhoto()




    }


    private fun registerCallbackPhoto() {
        (activity as MainActivity).getActivityResultData(object : OnActivityResultData{
            override fun resultData(bundle: Bundle) {
                linearLayoutChoosePhoto.visibility=View.GONE
                recyclerView.visibility=View.VISIBLE
                val mPaths= bundle.getStringArrayList(Constants.IMAGE_PATH)

                selectedImage= File(mPaths!![0])
                outputFileUri=mPaths[0]
                selectedImagesArrayList.add(mPaths[0])
                setAdapter()
                uploadPhoto.setOnClickListener{
                    showBottomSheetDialog()
                }




            }
        })
    }

    private fun setAdapter() {
        if(imageAdapter!=null)
        {
            imageAdapter!!.customNotify(selectedImagesArrayList)
        }
        else
        {
            imageAdapter = UploadImageAdapter(requireActivity(),selectedImagesArrayList,this)
            recyclerView.adapter = imageAdapter

        }

    }

    private fun listener() {
        imageViewchooserPhoto.setOnClickListener(this)
        buttonUpload.setOnClickListener(this)
        buttonPreview.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
          R.id.imageViewchooserPhoto->{
                showBottomSheetDialog()

            }

            R.id.buttonPreview->{
                if(selectedImage!=null)
                {
                    startActivity(Intent(activity,PreviewImageActivity::class.java).putExtra("imageUri",outputFileUri))

                }

            }
            R.id.buttonUpload->{
                if(selectedImage == null || !selectedImage!!.exists())
                {
                    Toast.makeText(requireActivity(),"Please set photo",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    saveFileinFirebaseStorage()
                }

            }

        }
    }

    private fun saveFileinFirebaseStorage() {
        showProgressbar()

        for(i in 0..selectedImagesArrayList.size-1)
        {
            val file = File(selectedImagesArrayList[i])
            uploadImage(file,file.name,i)
        }
    }

    private fun uploadImage(file: File, name: String, i: Int) {
        imageRef = storageRef.child("images/$name")
        uploadTask=imageRef.putFile(Uri.fromFile(file))
        uploadTask.addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                imageRef.downloadUrl.addOnSuccessListener {
                    count++
                    val url=it.toString()
                    imageUriList.add(url)
                    if (count==selectedImagesArrayList.size){
                        postAd()
                    }
                }

            }

        })

    }

    private fun postAd() {
        val date=currentDate()
        var docId = db.collection(arguments?.getString(Constants.KEY)!!).document().id
        val docData= hashMapOf(
                Constants.BRAND to arguments?.getString(Constants.BRAND),
                Constants.DESCRIPTION to arguments?.getString(Constants.DESCRIPTION),
                Constants.YEAR to arguments?.getString(Constants.YEAR),
                Constants.PRICE to arguments?.getString(Constants.PRICE),
                Constants.ADDRESS to arguments?.getString(Constants.ADDRESS),
                Constants.PHONE to arguments?.getString(Constants.PHONE),
                Constants.Id to docId,
                Constants.TYPE to arguments?.getString(Constants.KEY),
                Constants.USER_ID to SharedPref(requireActivity()).getString(Constants.USER_ID),
                Constants.CREATED_Date to date,
                "images" to imageUriList)



        db.collection(arguments?.getString(Constants.KEY)!!)
                .add(docData)
                .addOnSuccessListener{
                    updateDocId(it.id)

                }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                }


    }

    private fun updateDocId(id: String) {
        val docData = mapOf(
                Constants.Id to id
        )
        db.collection(arguments?.getString(Constants.KEY)!!).
        document(id).
        update(docData).addOnSuccessListener {

            hideProgressbar()
            Toast.makeText(activity, "Ad Posted Successfully", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_upload_photo_to_my_ads)
        }
                .addOnFailureListener{
                    Toast.makeText(requireActivity(),"Error:"+it.toString(),Toast.LENGTH_LONG).show()
                }


    }
     private fun currentDate(): String {
         val date= Date()
         val format=SimpleDateFormat("dd,MMM,yyyy")
         return format.format(date)
     }

    private fun showBottomSheetDialog() {
        val layoutInflater=requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= layoutInflater.inflate(R.layout.bottom_sheet_dialog,null)
        dialog= BottomSheetDialog(requireActivity())
        dialog!!.setContentView(view)
        dialog!!.window!!.findViewById<View>(R.id.design_bottom_sheet)
                .setBackgroundColor(resources.getColor(android.R.color.transparent))
        val tvGallery=dialog!!.findViewById<TextView>(R.id.textViewPhoto)
        val tvCamera=dialog!!.findViewById<TextView>(R.id.textViewCamera)
        val tvCancel=dialog!!.findViewById<TextView>(R.id.textViewCancel)
        tvCamera!!.setOnClickListener(View.OnClickListener {
            //dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.CAMERA)
        })

        tvGallery!!.setOnClickListener(View.OnClickListener {
            //dialog!!.dismiss()
            chooseImage(ImagePicker.Mode.GALLERY)
        })
        tvCancel!!.setOnClickListener(View.OnClickListener {
            dialog!!.dismiss()
        })

        dialog!!.show()
        val lp = WindowManager.LayoutParams()
        val window = dialog!!.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp

    }

    private fun chooseImage(mode: ImagePicker.Mode){
        ImagePicker.Builder(requireActivity())
                .mode(mode)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build()
    }

    override fun onItemClick() {
        if(selectedImagesArrayList.size<5)
        showBottomSheetDialog()
        else
            Toast.makeText(activity,"You can not add more images", Toast.LENGTH_SHORT).show()

    }
}