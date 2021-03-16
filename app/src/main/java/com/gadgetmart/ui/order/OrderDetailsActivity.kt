package com.gadgetmart.ui.order

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.OrderDetailsBinding
import com.gadgetmart.ui.my_review.AddReviewActivity
import com.gadgetmart.ui.order.adapter.OrderDetailListAdapter
import com.gadgetmart.ui.order.interfaces.MyOrderInterface
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import kotlinx.android.synthetic.main.order_details.*

class OrderDetailsActivity : BaseActivity<OrderDetailsBinding>(), MyOrderInterface {
    var binding: OrderDetailsBinding? = null
    lateinit var global: Global
    private var orderId = ""
    private var notificationType: String? = null

    companion object {
        fun getPendingStartIntent(
            context: Context?,
            orderId: String,
            notificationType: String
        ) {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra("order_id", orderId)
            intent.putExtra("notification_type", notificationType)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context!!.startActivity(intent)
        }
    }


    override fun getContentView(): Int {

        return R.layout.order_details

    }

    override fun init(binding: OrderDetailsBinding) {
        this.binding = binding
        global = applicationContext as Global
//        if (intent != null) {
//            orderId = intent.getStringExtra("order_id")
//            notificationType = intent.getStringExtra("notification_type")
//        }
        binding.toolbar.toolbar_title_text_view.text = "Order details"
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE
        setListeners(binding)

        Log.e("data for detail", Gson().toJson(global.dataModelValue))
        addValueInView()


    }

    fun addValueInView() {
        if( global.dataModelValue!!.payment_status.equals("Failed")){
          failedError(binding!!);
        }
        order_list_item.layoutManager = LinearLayoutManager(applicationContext)
        order_list_item.isNestedScrollingEnabled = false
        order_list_item.adapter = OrderDetailListAdapter(
            applicationContext,
            global.dataModelValue!!.order_products,
            this@OrderDetailsActivity,
            0, global.dataModelValue!!.is_order_cancel!!, global.dataModelValue!!.payment_status!!
        )
        order_id.setText("Id: #" + global.dataModelValue!!.order_number)
        address_txt.setText(
            global.dataModelValue!!.delivery_address?.address1 + "," +
                    global.dataModelValue!!.delivery_address?.city + "," + global.dataModelValue!!.delivery_address?.state
        )

        bag_total.setText(resources.getString(R.string.us_dollar) + global.dataModelValue!!.bag_amount)
        if(global.dataModelValue!!.discount!=null){
            tax_total.setText("-"+resources.getString(R.string.us_dollar) + global.dataModelValue!!.discount)

        }else{
            tax_total.setText("-"+resources.getString(R.string.us_dollar) + "0")

        }
        if(global.dataModelValue!!.coupon_discount!=null){
            coupon_price.setText("-"+resources.getString(R.string.us_dollar) + global.dataModelValue!!.coupon_discount)

        }else{
            coupon_price.setText("-"+resources.getString(R.string.us_dollar) + "0")

        }
        //coupon_price.setText(resources.getString(R.string.us_dollar) + global.dataModelValue!!.coupon_discount)
        val f =
            global.dataModelValue!!.bag_amount?.toFloat()!! + global.dataModelValue!!.tax?.toFloat()!! + global.dataModelValue!!.coupon_discount?.toFloat()!!
        if(global.dataModelValue!!.delivery_charges!=null) {
            order_total_price.setText(
                resources.getString(R.string.us_dollar) + Math.round(
                    global.dataModelValue!!.total_amount!!.toFloat()
                        .minus(global.dataModelValue!!.delivery_charges!!.toFloat())
                )
            )
        }else{
            order_total_price.setText(
                resources.getString(R.string.us_dollar) + Math.round(
                    global.dataModelValue!!.total_amount!!.toFloat()

                )
            )
        }
        if (global.dataModelValue!!.delivery_charges == null) {
            delivery_price.setText(resources.getString(R.string.us_dollar) + "0")

        } else {
            delivery_price.setText(resources.getString(R.string.us_dollar) + global.dataModelValue!!.delivery_charges)

        }
        total_price.setText(resources.getString(R.string.us_dollar) + Math.round(global.dataModelValue!!.total_amount!!.toFloat()))
Log.e("order cancel",global.dataModelValue!!.is_order_cancel!!.toString())
        binding!!.cancelButton.visibility = View.GONE

//        if (global.dataModelValue!!.is_order_cancel!!) {
//
//            binding!!.cancelButton.visibility = View.VISIBLE
//            binding!!.cancelButton.setBackgroundResource(R.drawable.background_radial)
//
//            binding!!.cancelButton.setOnClickListener {
//
//                val i = Intent(applicationContext, CompleteCancelOrder::class.java)
//                i.putExtra("product_id", global.dataModelValue!!.id)
//                i.putExtra("order_id", global.dataModelValue!!.id)
//                i.putExtra("type", 0)
//                i.putExtra("type_status", 1)
//                i.putExtra("text", "Cancel order")
//
//
//
//                startActivityForResult(i,101);
//            }
//
//        } else {
//
//            binding!!.cancelButton.visibility = View.VISIBLE
//            binding!!.cancelButton.setBackgroundResource(R.drawable.unselected_btn_back)
//            binding!!.cancelButton.setOnClickListener {
//                showToast("You can't cancel complete order")
//            }
//        }

        if( global.dataModelValue!!.payment_status.equals("Failed")){
            binding!!.cancelButton.visibility=View.GONE
            binding!!.downloadTxt.visibility=View.GONE
        }
    }

    private fun setListeners(binding: OrderDetailsBinding) {

        binding.toolbar.back_icon.setOnClickListener { finish() }
        binding.downloadTxt.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(global.dataModelValue!!.invoice_url!!))
            startActivity(browserIntent)
        }

    }

    override fun parentClick(pod: Int) {
    }

    override fun childClick(parentPos: Int, childPos: Int) {
    }

    override fun onItemClicked(position: Int) {

    }

    override fun openReviewScreen(parentPos: Int, childPos: Int) {
        val i = Intent(applicationContext, AddReviewActivity::class.java)
        i.putExtra(
            "id",
            global.dataModelValue!!.order_products!![childPos].product_data!!.variationId
        )
        i.putExtra(
            "title",
            global.dataModelValue!!.order_products!![childPos].product_data?.product_title
        )
        i.putExtra(
            "description",
            global.dataModelValue!!.order_products!![childPos].product_data?.short_description
        )
        if (!global.dataModelValue!!.order_products!![childPos].product_data?.productImages.isNullOrEmpty()) {
            i.putExtra(
                "image",
                global.dataModelValue!!.order_products!![childPos].product_data!!.productImages!![0].name
            )

        } else {
            i.putExtra("image", "")

        }
        startActivity(i)

    }

    override fun openCancelScreen(parentPos: Int, childPos: Int, type: Int) {
        if (type == 0) {
            cancelValid(binding(), childPos)
        } else {
            refundandReplace(binding(), childPos)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            if(resultCode==Activity.RESULT_OK){
                var i=Intent()
                setResult(Activity.RESULT_OK,i)
                finish()

            }
        }
    }
    private fun cancelValid(binding: OrderDetailsBinding, pos: Int) {


        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.login_message_dialog)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btnok = dialog.findViewById(R.id.okbtn) as TextView
        val btncancel = dialog.findViewById(R.id.cancelbtn) as TextView
        val dialog_msg = dialog.findViewById(R.id.dialog_msg) as TextView
        val dialog_title = dialog.findViewById(R.id.dialog_title) as TextView
        dialog_msg.text = "Do you want to cancel order?"
        dialog_title.text = "Warning?"

        btnok.setOnClickListener {
            dialog.dismiss()

            val i = Intent(applicationContext, CancelOrder::class.java)
            i.putExtra("product_id", global.dataModelValue!!.order_products!![pos].id)
            i.putExtra("order_id", global.dataModelValue!!.order_products!![pos].order_id)
            i.putExtra("type", 0)
            i.putExtra("type_status", 1)
            i.putExtra("text", "Cancel order")



            startActivityForResult(i,101);
        }

        btncancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    private fun refundandReplace(binding: OrderDetailsBinding, pos: Int) {


        val dialog = Dialog(
            this
        )





        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.login_message_dialog)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btnok = dialog.findViewById(R.id.okbtn) as TextView
        val btncancel = dialog.findViewById(R.id.cancelbtn) as TextView
        val dialog_msg = dialog.findViewById(R.id.dialog_msg) as TextView
        val dialog_title = dialog.findViewById(R.id.dialog_title) as TextView
        dialog_msg.text = "What you want?"
        dialog_title.text = "Warning?"
        btnok.setText("Replace")
        btncancel.setText("Refund")

        btnok.setOnClickListener {
            dialog.dismiss()

            val i = Intent(applicationContext, RefundAndRetrunOrder::class.java)
            i.putExtra("product_id", global.dataModelValue!!.order_products!![pos].id)
            i.putExtra("order_id", global.dataModelValue!!.order_products!![pos].order_id)
            i.putExtra("type", 1)
            i.putExtra("type_status", 1)
            i.putExtra("text", "Replace order")



            startActivityForResult(i,101);
        }

        btncancel.setOnClickListener {
            dialog.dismiss()
            val i = Intent(applicationContext, RefundAndRetrunOrder::class.java)
            i.putExtra("product_id", global.dataModelValue!!.order_products!![pos].id)
            i.putExtra("order_id", global.dataModelValue!!.order_products!![pos].order_id)
            i.putExtra("type", 1)
            i.putExtra("type_status", 2)
            i.putExtra("text", "Retrun order")



            startActivityForResult(i,101);
        }
        dialog.show()

    }

    private fun failedError(binding: OrderDetailsBinding) {


        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.failed_error)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btnok = dialog.findViewById(R.id.okbtn) as TextView


        btnok.setOnClickListener {
            dialog.dismiss()



        }


        dialog.show()

    }


}