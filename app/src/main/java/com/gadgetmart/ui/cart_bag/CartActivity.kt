package com.gadgetmart.ui.cart_bag

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.ui.cart_bag.model.CartModel
import com.gadgetmart.ui.cart_bag.model.MyCart
import com.gadgetmart.ui.checkout.CheckoutActivity
import com.gadgetmart.ui.product_details.ProductDetailsActivity
import com.gadgetmart.ui.product_details.productmodel.CartData
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.custom.CustomProgressDialog
import com.gadgetmart.util.custom.StyledAlertDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat


class CartActivity : Activity(), CartInterface {
    lateinit var main_layout_empty: LinearLayout

    lateinit var total_amount: TextView
    lateinit var btnBuy: Button
    lateinit var toolbar_title_text_view: TextView
    lateinit var toolbar_back_icon: ImageView

    lateinit var cart_listing: RecyclerView
    private lateinit var myDialog: CustomProgressDialog
    lateinit var adapter: CartAdapter

    lateinit var bottom_view: RelativeLayout

    lateinit var my_cart: ArrayList<MyCart>
    var pos = 0
    var previousPos = 0
    lateinit var btnfinish: TextView
    var total = 0.0f
    var isOutOfStock=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_layout)
        init()
        setListners()
        // addData()
    }

    fun init() {
        bottom_view = findViewById(R.id.bottom_view)
        main_layout_empty = findViewById(R.id.main_layout_empty)
        cart_listing = findViewById(R.id.cart_listing)
        total_amount = findViewById(R.id.total_amount)
        btnBuy = findViewById(R.id.btnBuy)
        btnfinish = findViewById(R.id.btnfinish)
        toolbar_back_icon = findViewById(R.id.toolbar_back_icon)
        toolbar_title_text_view = findViewById(R.id.toolbar_title_text_view)
        cart_listing.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
                cart_listing.context,
                LinearLayoutManager.VERTICAL
        )
        cart_listing.addItemDecoration(dividerItemDecoration)
        myDialog = CustomProgressDialog(this@CartActivity)
        myDialog.dialogShow()
        toolbar_title_text_view.text = "Bag"
        my_cart = ArrayList()
        myCart()


    }

    fun setListners() {

        toolbar_back_icon.setOnClickListener {
            onBackPressed()
        }
        btnfinish.setOnClickListener {
            onBackPressed()
        }
        btnBuy.setOnClickListener {
            if(isOutOfStock==true){
Toast.makeText(applicationContext,"Some product out of stock please remove first.",Toast.LENGTH_SHORT).show()
            }else {
                val intent = Intent(applicationContext, CheckoutActivity::class.java)
                intent.putExtra(Constants.type, "true")
                intent.putExtra("id", "")

                startActivityForResult(intent, 101)
            }
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                myDialog.dialogShow()
                my_cart = ArrayList()
                total = 0.0f
                myCart()
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent()
        setResult(Activity.RESULT_OK, i)
        finish()

    }

    fun addAdapter(list: ArrayList<MyCart>) {
        Log.e("cart data", Gson().toJson(list))
        adapter = CartAdapter(this@CartActivity, list, this)
        cart_listing.adapter = adapter

    }

    private fun myCart() {
        isOutOfStock=false

        try {

            ApiClientGenerator
                    .getClient()!!
                    .getMyCart(
                            ContextUtils.getAuthToken(applicationContext)
                    ).enqueue(object : Callback<CartModel?> {
                        override fun onResponse(
                                call: Call<CartModel?>,
                                response: Response<CartModel?>
                        ) {
                            myDialog.dialogDismiss()
                            if (response.body()!!.status == 1) {
                                isOutOfStock=false
                                for(l in 0 until response.body()!!.data?.my_cart?.size!!){
                                    if(response.body()!!.data?.my_cart!!.get(l).variation!!.currentBatch==null){
                                        isOutOfStock=true
                                        break
                                    }
                                }
                                if (response.body()!!.data?.my_cart?.size!! > 0) {
                                    my_cart = response.body()!!.data?.my_cart!!
//for(i in 0 until my_cart.size){
//    if(my_cart[i].variation!!.offer_available!!){
//        if(my_cart[i].variation!!.offers!![0].discount_option.equals("Discount")){
//            var rate=my_cart[i].variation!!.currentBatch!!.base_rate!! * my_cart[i].quantity!!
//
//            if(my_cart[i].variation!!.offers!![0].discount_type.equals("Percentage")) {
//                var d =
//                    (rate * my_cart[i].variation!!.offers!![0].discount_amount!!) / 100
//
//                my_cart[i].variation!!.currentBatch!!.discount_rate = d
//                Log.e("discount amount", "discount" + d)
//            }else{
//                var d =
//                    rate.minus(my_cart[i].variation!!.offers!![0].discount_amount!!)
//
//                my_cart[i].variation!!.currentBatch!!.discount_rate = d
//                Log.e("discount amount", "discount" + d)
//            }
//
//
//        }else{
//            var rate=my_cart[i].variation!!.currentBatch!!.base_rate!! * my_cart[i].quantity!!
//
//            if(rate>my_cart[i].variation!!.offers!![0].min_order_amount!!){
//
//                       var discountAmount = (rate * my_cart[i].variation!!.offers!![0].discount_amount!!) / 100
//
//                    if (discountAmount >= my_cart[i].variation!!.offers!![0].discount_upto!!.toFloat()) {
//                        var d=rate.minus(my_cart[i].variation!!.offers!![0].discount_upto!!)
//                        my_cart[i].variation!!.currentBatch!!.discount_rate=d
//
//                    }else{
//                        var d=rate!!.minus(discountAmount)
//                        my_cart[i].variation!!.currentBatch!!.discount_rate=d
//                    }
//
//            }else{
//                my_cart[i].variation!!.currentBatch!!.discount_rate=my_cart[i].variation!!.currentBatch!!.base_rate
//
//            }
//
//        }
//    }
//}
                                    addAdapter(my_cart)
                                    main_layout_empty.visibility = View.GONE
                                    cart_listing.visibility = View.VISIBLE
                                    bottom_view.visibility = View.VISIBLE
                                    //Deepak Changes
                                    for (j in 0 until my_cart.size) {
                                        if(my_cart[j].variation?.currentBatch!=null) {
                                            if (my_cart[j].variation?.offer_available!!) {
                                                my_cart[j].variation?.currentBatch!!.base_rate =
                                                    my_cart[j].variation?.offer_discount_price
                                            }

                                            if (my_cart[j].variation?.balanceQuantity!! > 0) {
                                                if (total == 0.0f) {
                                                    total =
                                                        (my_cart[j].variation?.currentBatch!!.base_rate!!.toFloat())
                                                    total *= my_cart[j].quantity!!


                                                } else {
                                                    total += (my_cart[j].quantity?.let {
                                                        (my_cart[j].variation?.currentBatch!!.base_rate!!.toFloat())?.times(
                                                            it
                                                        )
                                                    })!!
                                                }
                                            }
                                        }
                                    }
                                    val df = DecimalFormat("#.##")
                                    df.setRoundingMode(RoundingMode.CEILING)
                                    total_amount.text = "\u20B9" + df.format(total)
                                } else {
                                    main_layout_empty.visibility = View.VISIBLE
                                    cart_listing.visibility = View.GONE
                                    bottom_view.visibility = View.GONE

                                }
                            } else {
                                AppUtil.firebaseEvent(applicationContext,"error","error_events",response.body()?.message!!
                                )
                            }
                        }

                        override fun onFailure(
                                call: Call<CartModel?>,
                                t: Throwable
                        ) {
                            Log.e("message", t.message)
                            myDialog.dialogDismiss()

                            //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
        } catch (e: Exception) {
            myDialog.dialogDismiss()
            e.printStackTrace()
        }
    }

    override fun addMoreProduct(p: Int, variationId: String, price: String, count: Int) {
        myDialog.dialogShow()

        addOrUpdateCart(variationId, count)
    }

    override fun addLessProduct(p: Int, variationId: String, price: String, count: Int) {
        myDialog.dialogShow()

        addOrUpdateCart(variationId, count)
    }


    override fun clickOnItem(p: Int) {

        StyledAlertDialog.builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_remove)
                .setMessage(R.string.msg_remove_from_cart)
                .setPositiveButton(R.string.text_yes) { dialog, _ ->

                    Log.e("############id", my_cart[p].id?.toString())
                    dialog.dismiss()
                    myDialog.dialogShow()
                    my_cart[p].id?.let { removeCart(it) }
                }
                .setNegativeButton(R.string.text_no) { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    override fun clickOnView(p: Int) {
        if(isOutOfStock==true){
          //  Toast.makeText(applicationContext,"product out of stock please remove it",Toast.LENGTH_SHORT).show()
        }else {
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("productId", my_cart[p].variation!!.productId)

            intent.putExtra("category_name", "Product Details")
            intent.putExtra("position", 0)
            intent.putExtra("type", "home")
            intent.putExtra("id", my_cart[p].variation!!.variationId)

            startActivity(intent)
        }
    }


    //---------------------------Add or remove product--------------------
    private fun addOrUpdateCart(product_id: String, quantity: Int) {
        try {

            ApiClientGenerator
                    .getClient()!!
                    .addOrUpdatecart(
                            ContextUtils.getAuthToken(applicationContext),
                            product_id, quantity
                    ).enqueue(object : Callback<ApiResponse<CartData>?> {
                        override fun onResponse(
                                call: Call<ApiResponse<CartData>?>,
                                response: Response<ApiResponse<CartData>?>
                        ) {
                            if (response.body()!!.status == 1) {
                                // total_amount.text = "\u20B9" + total
                                my_cart = ArrayList()
                                total = 0.0f
                                myCart()
                            } else {
                                AppUtil.firebaseEvent(applicationContext,"error","error_events",response.body()?.message!!
                                )
                                myDialog.dialogDismiss()

                            }
                        }

                        override fun onFailure(
                                call: Call<ApiResponse<CartData>?>,
                                t: Throwable
                        ) {
                           // myDialog.dialogDismiss()

                            //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeCart(cart_id: Int) {
        try {

            ApiClientGenerator.getClient()!!.removeFromMyCart(
                    ContextUtils.getAuthToken(
                            applicationContext
                    ), cart_id
            ).enqueue(object : Callback<ApiResponse<CartData>?> {
                override fun onResponse(
                        call: Call<ApiResponse<CartData>?>,
                        response: Response<ApiResponse<CartData>?>
                ) {

                    if (response.body()?.status == 1) {
                        my_cart = ArrayList()
                        total = 0.0f
                        myCart()
//                        my_cart.removeAt(pos)
//                        if (my_cart.size == 0) {
//                            main_layout_empty.visibility = View.VISIBLE
//                            cart_listing.visibility = View.GONE
//                            bottom_view.visibility = View.GONE
//                        } else {
//                            addAdapter(my_cart)
//                            total = 0.0f
//                            for (j in 0 until my_cart.size) {
//                                if (total == 0.0f) {
//                                    total = (my_cart[j].variation?.currentBatch!!.discount_rate!!.toFloat())
//                                    total *= my_cart[j].quantity!!
//                                } else {
//                                    total += (my_cart[j].quantity?.let {
//                                        (my_cart[j].variation?.currentBatch!!.discount_rate!!.toFloat())?.times(
//                                            it
//                                        )
//                                    })!!
//                                }
//                            }
//                            total_amount.text = "\u20B9" + total
//
//
//                        }
                    } else {
                        myDialog.dialogDismiss()
                        AppUtil.firebaseEvent(applicationContext,"error","error_events",response.body()?.message!!
                        )
                    }
                }

                override fun onFailure(
                        call: Call<ApiResponse<CartData>?>,
                        t: Throwable
                ) {
                    myDialog.dialogDismiss()

                    //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}