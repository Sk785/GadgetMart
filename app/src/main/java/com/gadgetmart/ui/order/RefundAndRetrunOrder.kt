package com.gadgetmart.ui.order

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.domain.AwsBucketApi
import com.gadgetmart.databinding.LayoutCancelOrderBinding
import com.gadgetmart.ui.order.adapter.CancelOrderListAdapter
import com.gadgetmart.ui.order.interfaces.MyOrderInterface
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.AppValidationUtil
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.layout_cancel_order.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import java.io.File

public class RefundAndRetrunOrder : BaseActivity<LayoutCancelOrderBinding>(), CancelOrderContract,
    MyOrderInterface, AwsBucketApi.AwsBucketApiListener {
    private var binding: LayoutCancelOrderBinding? = null
    private var cancelOrderPresenter: CancelOrderPresenter? = null
    lateinit var global: Global
    lateinit var reasonList: ArrayList<String>
    lateinit var reasonListHM: ArrayList<HashMap<String, String>>
    private var adapter: CancelOrderListAdapter? = null
    private var reason: String? = null
    var order_id = ""
    var product_id = ""
    lateinit var myDialog: CustomProgressDialog

    var imageCount = 0
    var image1 = ""
    var image2 = ""
    var image3 = ""
    private lateinit var bsd: BottomSheetDialog

    private var CAMERA = 101
    private var GALLERY = 102
    private var CROP_PIC = 103
var imageList:ArrayList<String>?=null

    private var realPath: String? = ""
    private var bucketImagePath: String? = ""
    override fun getContentView(): Int {
        return R.layout.layout_cancel_order
    }


    override fun init(binding: LayoutCancelOrderBinding) {
        this.binding = binding
        reasonList = ArrayList()
        myDialog=CustomProgressDialog(this);

        reasonListHM = ArrayList<HashMap<String, String>>()
        imageList= ArrayList();
        product_id = intent.extras!!.getString("product_id").toString()
        order_id = intent.extras!!.getString("order_id").toString()
        global = applicationContext as Global
        binding.toolbar.toolbar_title_text_view.text = intent.extras!!.getString("text").toString()
        binding.toolbar.toolbar_ohnik_image_view.visibility = View.GONE
        binding.toolbar.toolbar_cart_item_count.visibility = View.GONE
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE

        setListeners(binding)
        reasonList.add("Not the same product what i booked")
        reasonList.add("Damaged/defective")
        reasonList.add("Color Changed")
        reasonList.add("Not the quantity what i expected")
        reasonList.add("Wrongly booked")
        reasonList.add("Other")

        for (i in 0 until reasonList.size) {
            var hashMap = HashMap<String, String>()
            hashMap.put("reason", reasonList.get(i))
            hashMap.put("isSelected", "false")
            reasonListHM.add(hashMap)

        }
        val subCategoriesLayoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = subCategoriesLayoutManager
        adapter = CancelOrderListAdapter(this, reasonListHM, this)
        binding.rvList.adapter = adapter
        initPresenter()
    }

    private fun initPresenter() {
        cancelOrderPresenter = CancelOrderPresenter(this, this)

    }

    private fun setListeners(binding: LayoutCancelOrderBinding) {
//        binding.close1.setOnClickListener {
//
//        }
//        binding.close1.setOnClickListener {
//
//        }
//        binding.close1.setOnClickListener {
//
//        }
        binding.addMore.setOnClickListener {
            if (imageCount < 3) {

                checkPermissions()
            }else{
                showToast("Maximum 3 images seleced")
            }

        }
        binding.toolbar.back_icon.setOnClickListener {
            finish()
        }
        binding.btnProceed.setOnClickListener {
            if (reason != null) {
                if (reason.equals("Other")) {
                    if (edReason.text.length == 0) {
                        edReason.setError("This field is required")
                    } else {
                        order_id = intent.extras!!.getString("order_id").toString()
//                        var product_image=""
//                        for (i in 0 until imageList!!.size) {
//                            if (product_image.equals("")) {
//                                product_image = imageList!![i]
//
//                            } else {
//                                product_image = product_image + "," + imageList!![i]
//
//                            }
//                        }
                        myDialog.dialogShow()
                        //Log.e("product_image",product_image)
                        if (intent.extras!!.getInt("type") == 1) {
                            cancelOrderPresenter?.replaceRefund(
                                product_id,
                                order_id,
                                reason!!,
                                intent.extras!!.getInt("type_status").toString(),
                                imageList
                            )
                            edReason.setError(null)
                        } else {

                            cancelOrderPresenter?.cancelOrder(product_id, order_id, reason!!,imageList)
                            edReason.setError(null)
                        }

                    }
                } else {
                    var product_image=""
//                    for (i in 0 until imageList!!.size) {
//                        if (product_image.equals("")) {
//                            product_image = imageList!![i]
//
//                        } else {
//                            product_image = product_image + "," + imageList!![i]
//
//                        }
//                    }
//                    Log.e("product_image",product_image)
                    myDialog.dialogShow()
                    if (intent.extras!!.getInt("type") == 1) {
                        cancelOrderPresenter?.replaceRefund(
                            product_id,
                            order_id,
                            reason!!,
                            intent.extras!!.getInt("type_status").toString(),imageList
                        )

                    } else {
                        cancelOrderPresenter?.cancelOrder(product_id, order_id, reason!!,imageList)
                    }
                }
            } else {
                showToast("Please select atleast one reason !")
            }
        }
    }

    override fun onItemClicked(position: Int) {
        if ((reasonListHM.size - 1) == position) {
            binding?.edReason?.visibility = View.VISIBLE

        } else {
            binding?.edReason?.visibility = View.GONE
        }
        for (i in 0 until reasonListHM.size) {
            reasonListHM.get(i).set("isSelected", "false")
        }
        reason = reasonListHM.get(position).get("reason")
        reasonListHM.get(position).set("isSelected", "true")
        adapter!!.notifyData(reasonListHM)

    }

    override fun parentClick(pod: Int) {
        TODO("Not yet implemented")
    }

    override fun childClick(parentPos: Int, childPos: Int) {
        TODO("Not yet implemented")
    }

    override fun openReviewScreen(parentPos: Int, childPos: Int) {
        TODO("Not yet implemented")
    }

    override fun openCancelScreen(parentPos: Int, childPos: Int, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onCancelOrderSuccess(message: String) {
        myDialog.dialogDismiss()
        showToast(message)
        var i = Intent()
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    override fun onCancelOrderFailure(message: String) {
        myDialog.dialogDismiss()
        AppUtil.firebaseEvent(applicationContext,"error","error_events", message)

        showToast(message)
    }

    override fun noNetworkFound(message: String) {
        myDialog.dialogDismiss()

        showToast(message)
    }


    private fun checkPermissions() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    bsd = BottomSheetDialog(this@RefundAndRetrunOrder)
                    bsd.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    bsd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    bsd.setContentView(R.layout.choose_image_from_layout)
                    bsd.setCanceledOnTouchOutside(false)
                    bsd.setCancelable(false)
                    bsd.show()
                    onclick()
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {

                }
            })
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }


    private fun onclick() {

        val camera: LinearLayout? = bsd.findViewById(R.id.camera_layout) as LinearLayout?
        val gallery: LinearLayout? = bsd.findViewById(R.id.gallery_layout) as LinearLayout?
        val cancelLayout: LinearLayout? = bsd.findViewById(R.id.cancel_layout) as LinearLayout?

        camera?.setOnClickListener {
            bsd.dismiss()
            openCamera()
        }
        gallery?.setOnClickListener {
            bsd.dismiss()
            openGallery()
        }
        cancelLayout?.setOnClickListener { bsd.dismiss() }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    private fun openGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA -> if (resultCode == Activity.RESULT_OK) {
                val photo: Bitmap = data!!.extras!!.get("data") as Bitmap
                val selectedImage = AppValidationUtil.getImageUri(this, photo)
                CropImage.activity(selectedImage)
                    .start(this);

            }
            GALLERY -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri? = data!!.data
                CropImage.activity(selectedImage)
                    .start(this);
//                realPath = selectedImage?.let { AppValidationUtil.getRealPathFromURI(it, this) }
//
////                realPath = selectedImage.toString()
//                Log.e("ImageRealPath : ", "" + realPath)
//
//                if (realPath == null) {
//                    showToast("Invalid image")
//                }
//                val timeInMillis = System.currentTimeMillis().toString()
//                val file = File(realPath!!)
//                AwsBucketApi.uploadImageToBucket(
//                    this,
//                    file,
//                    timeInMillis + "_" + file.name,
//                    this
//                )


            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->


                if (resultCode == Activity.RESULT_OK) {
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.e("########uri", resultUri.toString())
                    //  val photo: Bitmap = data!!.extras!!.get("data") as Bitmap
                    // val selectedImage = AppValidationUtil.getImageUri(this, photo)
                    realPath = AppValidationUtil.getFilePath(this, resultUri)

                    Log.e("ImageRealPath : ", "" + realPath)
                    if (realPath == null) {
                        showToast("Invalid image")
                    }
                    val timeInMillis = System.currentTimeMillis().toString()
                    val file = File(realPath!!)
                    bucketImagePath = realPath
                    PreferenceManager().getInstance(this).setProfileImage(bucketImagePath)
                    if (imageCount == 0) {
                        binding!!.image1.visibility=View.VISIBLE
//                        Glide.with(this)
//                            .load(bucketImagePath)
//                            .centerCrop()
//
//                            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
//                            .into(binding!!.image1View)
                        binding!!.image1View.setImageURI(resultUri)
                        imageCount = imageCount + 1
                        imageList!!.add(bucketImagePath!!)
                    } else if (imageCount == 1) {
                        binding!!.image2.visibility=View.VISIBLE

//                        Glide.with(this)
//                            .load(bucketImagePath)
//
//                            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
//
//                            .into(binding!!.image2View)
                        binding!!.image2View.setImageURI(resultUri)

                        imageCount = imageCount + 1
                        imageList!!.add(bucketImagePath!!)

                    } else if (imageCount == 2) {
                        binding!!.image3.visibility=View.VISIBLE
                        binding!!.image3View.setImageURI(resultUri)

//
//                        Glide.with(this)
//                            .load(bucketImagePath)
//                            .centerCrop()
//                            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
//                            .into(binding!!.image3View)
                        imageCount = imageCount + 1
                        imageList!!.add(bucketImagePath!!)

                    }

//                    AwsBucketApi.uploadProductImageToBucket(
//                        this,
//                        file,
//                        timeInMillis + "_" + file.name,
//                        this@CancelOrder
//                    )


                }
        }
    }

    override fun onPicUploadedOnBucket(imagePath: String?) {
        bucketImagePath = imagePath
        PreferenceManager().getInstance(this).setProfileImage(bucketImagePath)
        if (imageCount == 0) {
            binding!!.image1.visibility=View.VISIBLE
            Glide.with(this)
                .load(bucketImagePath)
                .centerCrop()

                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(binding!!.image1View)
            imageCount = imageCount + 1
imageList!!.add(bucketImagePath!!)
        } else if (imageCount == 1) {
            binding!!.image2.visibility=View.VISIBLE

            Glide.with(this)
                .load(bucketImagePath)

                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))

                .into(binding!!.image2View)
            imageCount = imageCount + 1
            imageList!!.add(bucketImagePath!!)

        } else if (imageCount == 2) {
            binding!!.image3.visibility=View.VISIBLE

            Glide.with(this)
                .load(bucketImagePath)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(binding!!.image3View)
            imageCount = imageCount + 1
            imageList!!.add(bucketImagePath!!)

        }

        Log.e("Bucket Path ::: ", "" + bucketImagePath)
    }

    override fun onPicNotUploadedOnBucket(message: String?) {
        AppUtil.showToast(this, message!!)
        Log.e("Bucket Error ::: ", "" + message)
    }

    override fun onPicUpdationProgress(percent: Int?) {

    }

}