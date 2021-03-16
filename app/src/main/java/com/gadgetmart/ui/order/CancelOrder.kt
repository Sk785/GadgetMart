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
import com.gadgetmart.databinding.CancelOrdersBinding
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

class CancelOrder: BaseActivity<CancelOrdersBinding>(), CancelOrderContract{
    private var binding: CancelOrdersBinding? = null
    lateinit var global: Global

    private var reason: String? = null
    var order_id = ""
    var product_id = ""
    lateinit var myDialog: CustomProgressDialog
    lateinit var cancelOrderPresenter:CancelOrderPresenter

    var imageList:ArrayList<String>?=null


    private lateinit var bsd: BottomSheetDialog


    override fun getContentView(): Int {
        return R.layout.cancel_orders
    }


    override fun init(binding: CancelOrdersBinding) {
        this.binding = binding
        myDialog= CustomProgressDialog(this);
        imageList= ArrayList()
        product_id = intent.extras!!.getString("product_id").toString()
        order_id = intent.extras!!.getString("order_id").toString()
        global = applicationContext as Global
        binding.toolbar.toolbar_title_text_view.text = intent.extras!!.getString("text").toString()
        binding.toolbar.toolbar_ohnik_image_view.visibility = View.GONE
        binding.toolbar.toolbar_cart_item_count.visibility = View.GONE
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE

        setListeners(binding)

        initPresenter()
    }

    private fun initPresenter() {
        cancelOrderPresenter = CancelOrderPresenter(this, this)

    }

    private fun setListeners(binding: CancelOrdersBinding) {

        binding.toolbar.back_icon.setOnClickListener {
            finish()
        }
        binding.btnProceed.setOnClickListener {
                myDialog.dialogShow()
                cancelOrderPresenter?.cancelOrder(product_id, order_id, binding.edReason.text.toString(),imageList)

        }
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
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message)

        showToast(message)
    }

    override fun noNetworkFound(message: String) {
        myDialog.dialogDismiss()

        showToast(message)
    }





}