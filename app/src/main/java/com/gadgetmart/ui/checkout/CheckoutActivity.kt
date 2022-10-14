package com.gadgetmart.ui.checkout

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.ActivityCheckoutBinding
import com.gadgetmart.databinding.GstinViewLayoutBinding
import com.gadgetmart.databinding.OrderDetailsBinding
import com.gadgetmart.databinding.SortByBottomsheetBinding
import com.gadgetmart.ui.auth._common.OrderConfirmation
import com.gadgetmart.ui.cart_bag.CartInterface
import com.gadgetmart.ui.cart_bag.model.CartModel
import com.gadgetmart.ui.cart_bag.model.MyCart
import com.gadgetmart.ui.checkout.model.CheckOutDataModel
import com.gadgetmart.ui.coupon.CouponActivity
import com.gadgetmart.ui.edit_profile.EditProfileActivity
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.ui.my_address.AddressResult
import com.gadgetmart.ui.my_address.MyAddressesActivity
import com.gadgetmart.ui.order.CancelOrder
import com.gadgetmart.ui.product_details.ProductDetailsActivity
import com.gadgetmart.ui.product_details.productmodel.CartData
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.util.*
import com.gadgetmart.util.custom.CustomProgressDialog
import com.gadgetmart.util.custom.StyledAlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.payumoney.core.PayUmoneySdkInitializer.PaymentParam
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_checkout.view.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat


class CheckoutActivity : BaseActivity<ActivityCheckoutBinding>(), CartInterface,
        PaymentResultListener {
    private lateinit var binding: ActivityCheckoutBinding
    lateinit var adapter: CheckoutAdapter
    lateinit var my_cart: ArrayList<MyCart>
    lateinit var myDialog: CustomProgressDialog
    var total = 0.0f

    var pos = 0
    var deliveryCharges = 0.0f
    var taxCharges = 0.0f
    var couponDiscount = 0.0f
    var orderTotal = 0.0f
    var status = ""
    var id = ""
    var addressId = ""
    var orderNumber = ""
    val rcCheckout = 23
    var discountAmount1 = 0.0f
    var couponID = ""
    var couponCode = ""

    var gstinNumber = ""
    var businessname = ""
    var orderId = ""
    var order_number = ""

    var weight = 0.0f

    var pincode = ""
    var count = 0;

    private lateinit var bsd: BottomSheetDialog
    var isOutOfStock = false


    override fun getContentView(): Int {
        return R.layout.activity_checkout
    }

    override fun init(binding: ActivityCheckoutBinding) {
        this.binding = binding
        binding.toolbar.toolbar_ohnik_image_view.visibility = View.GONE
        binding.toolbar.toolbar_cart_icon.visibility = View.GONE
        binding.toolbar.toolbar_title_text_view.text = "Checkout"
        Log.d("asdkjsakdas","ehi ni he");
        getIntentData()
        addData()
        setListeners(binding)
        myDialog = CustomProgressDialog(this)
        myDialog.dialogShow()
        myCart()
        Checkout.preload(applicationContext)

    }

    private fun getIntentData() {
        if (intent.extras != null) {
            status = intent.getStringExtra(Constants.type)
            id = intent.getStringExtra("id")
        }
    }

    private fun addData() {
        cart_products_recycler_view.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
                cart_products_recycler_view.context,
                LinearLayoutManager.VERTICAL
        )
        cart_products_recycler_view.addItemDecoration(dividerItemDecoration)
    }

    private fun setListeners(binding: ActivityCheckoutBinding) {
        binding.editView.setOnClickListener {
            openGSTINBySheet()

        }
        binding.gstinView.setOnClickListener {
            openGSTINBySheet()

        }
        binding.applyCouponLayout.setOnClickListener {
            val intent = Intent(applicationContext, CouponActivity::class.java)


            startActivityForResult(intent, rcCheckout)
        }
        binding.tvRemove.setOnClickListener {
            couponID = ""
            couponCode = ""
            couponApply.visibility = View.GONE
            couponApply.visibility = View.GONE

            coupon_image_view_arrow.visibility = View.VISIBLE
            tvRemove.visibility = View.GONE
            tvCouponApplied.visibility = View.GONE
            binding?.taxAmount?.text = "-₹0"

            order_total.text = "\u20B9" + (Math.round(orderTotal))
            total_amount.text = "\u20B9" + (Math.round(orderTotal - deliveryCharges))
            discountAmount1 = 0.0f


        }
        binding.toolbar.back_icon.setOnClickListener {
            onBackPressed()
        }
        btnfinish.setOnClickListener {
            onBackPressed()
        }
        checkout_button.setOnClickListener {
            if (isOutOfStock == true) {
                Toast.makeText(applicationContext, "Some product out of stock please remove first.", Toast.LENGTH_SHORT).show()
            } else {
                if (PreferenceManager().getInstance(this).getUserEmail().equals("")) {
                    editValid("Please edit email in profile section")
                    // showToast("Please add your email in edit profile section")
                } else if (PreferenceManager().getInstance(this).getUserPhone().equals("")) {
                    editValid("Please edit phone number in profile section")
                } else {
                    if (addressId == "") {
                        showToast("No address selected")
                    } else {
                        AppUtil.firebaseEvent(applicationContext, "", "go_to_checkout", "")

                        myDialog.dialogShow()
                        placeOrderApi("PG")
                    }
                }
            }
        }

        cash_on_delivery.setOnClickListener {
            if (isOutOfStock == true) {
                Toast.makeText(applicationContext, "Some product out of stock please remove first.", Toast.LENGTH_SHORT).show()
            } else {
                if (PreferenceManager().getInstance(this).getUserEmail().equals("")) {
                    editValid("Please edit email in profile section")
                    // showToast("Please add your email in edit profile section")
                } else if (PreferenceManager().getInstance(this).getUserPhone().equals("")) {
                    editValid("Please edit phone number in profile section")
                } else {
                    if (addressId == "") {
                        showToast("No address selected")
                    } else {
                        AppUtil.firebaseEvent(applicationContext, "", "go_to_checkout", "")

                        myDialog.dialogShow()
                        placeOrderApi("COD")
                    }
                }
            }
        }
        changeaddress_txt.setOnClickListener {
            val intent = Intent(applicationContext, MyAddressesActivity::class.java)
            intent.putExtra("type", "change address")
            startActivityForResult(intent, 101)
        }

//        binding.checkBilling.setOnCheckedChangeListener { _, isChecked ->
//            Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
//            if (isChecked){
//                binding.llBillingAddress.visibility=View.VISIBLE
//            }else{
//                binding.llBillingAddress.visibility=View.GONE
//            }
//        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("onActivityResult  ", "-- " + requestCode)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.extras!!.getString("name").equals("")) {
                    addressId = ""
                    changeaddress_txt.text = "Add address"
                    address.text = "No Address"
                } else {
                    address.text = data.extras!!.getString("name")
                    addressId = data.extras!!.getInt("id").toString()
                    pincode = data.extras!!.getString("pincode").toString()

                    if (!pincode.equals("")) {
                        val df = DecimalFormat("#.##")
                        df.setRoundingMode(RoundingMode.CEILING)
                        if (count != my_cart.size) {
                            myDialog.dialogShow()

                            getDeliveryPrice(pincode, df.format(weight))
                        }
                    }
                }
            }
        } else if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT) {
            Log.e("data  ", "-- " + data)

            if (resultCode == RESULT_OK && data != null) {
                val transactionResponse: TransactionResponse =
                        data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)

                if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                    if (transactionResponse.getTransactionStatus()
                                    .equals(TransactionResponse.TransactionStatus.SUCCESSFUL)
                    ) {
                        //Success Transaction
                        showToast("Payment Success");
                        val payuResponse: String = transactionResponse.getPayuResponse()
                        val response = JSONObject(payuResponse)
                        val result = response.getJSONObject("result")
                        // Response from SURl and FURL
                        myDialog.dialogShow()
                        //afterPaymentSuccessApi(result)
                        Log.e("payment Tag", "tran $result")
                    } else {
                        //Failure Transaction
                        showToast("Payment Failed");
                        afterPaymentFailedApi(orderId)
                    }

                    // Response from Payumoney

                }
            } else {
                showToast("Payment cancel");
                afterPaymentFailedApi(orderId)
            }

        } else if (requestCode == rcCheckout && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                val dataExtras = data.extras

                val discount_amount = dataExtras!!.getString("amount")
                val min_order_amount = dataExtras.getString("minmum_amount")

                discountAmount1 = 0.0f
                Log.e("total", "" + orderTotal.minus(Math.round(taxCharges)))
                if (orderTotal >= min_order_amount!!.toFloat()) {
                    couponApply.visibility = View.VISIBLE

                    val name = dataExtras!!.getString("name")
                    couponID = dataExtras.getString("id").toString()
                    couponCode = dataExtras.getString("coupon_code").toString()
//                    if (taxCharges == 0.0f) {
//                        taxCharges = discount_amount!!.toFloat()
//                    } else {
//                        taxCharges = taxCharges + discount_amount!!.toFloat()
//                    }
                    discountAmount1 = discount_amount!!.toFloat()
                    order_total.text = "\u20B9" + Math.round(
                            order_total.text.toString().replace("\u20B9", "")
                                    .toFloat() - discountAmount1
                    )
                    total_amount.text = "\u20B9" + Math.round(
                            total_amount.text.toString().replace("\u20B9", "")
                                    .toFloat() - discountAmount1
                    )
                    binding?.taxAmount?.text = "- ₹" + discountAmount1
                    coupon_image_view_arrow.visibility = View.GONE
                    tvRemove.visibility = View.VISIBLE
                    tvCouponApplied.visibility = View.VISIBLE
                    tvCouponApplied.text = name

                } else {
                    tvCouponApplied.visibility = View.VISIBLE
                    tvCouponApplied.text =
                            "coupon will applied on minimum \u20B9$min_order_amount order"
                }

            }

        }


    }

    override fun onBackPressed() {

        if (status == "true") {
            finish()
        } else {
            for (j in 0 until my_cart.size) {
                if ((my_cart[j].variation!!.variationId).toString() == id) {
                    myDialog.dialogShow()
                    my_cart[j].id?.let { removeCart(it) }
                    break

                }
            }
        }
    }

    fun addAdapter(list: ArrayList<MyCart>) {
        Log.e("cart data", Gson().toJson(list))
        adapter = CheckoutAdapter(this@CheckoutActivity, list, this)
        cart_products_recycler_view.adapter = adapter

    }

    private fun myCart() {
        try {
            taxCharges = 0.0f
            couponDiscount = 0.0f
            orderTotal = 0.0f
            total = 0.0f
            count = 0
            isOutOfStock = false
            ApiClientGenerator
                    .getClient()!!
                    .getMyCart(ContextUtils.getAuthToken(applicationContext))
                    .enqueue(object : Callback<CartModel?> {
                        override fun onResponse(
                                call: Call<CartModel?>,
                                response: Response<CartModel?>
                        ) {
                            myDialog.dialogDismiss()
                            if (response.body()!!.status == 1) {
                                for (l in 0 until response.body()!!.data?.my_cart?.size!!) {
                                    if (response.body()!!.data?.my_cart!!.get(l).variation!!.currentBatch == null) {
                                        isOutOfStock = true
                                        break
                                    }
                                }
                                if (response.body()!!.data?.my_cart?.size!! > 0) {
                                    my_cart = response.body()!!.data?.my_cart!!
                                    var isOffer = false
                                    for (i in 0 until my_cart.size) {
                                        if (my_cart[i].variation?.currentBatch != null) {
                                            if (my_cart[i].variation!!.offer_available!!) {
                                                isOffer = true

                                                my_cart[i].variation?.currentBatch!!.base_rate =
                                                        my_cart[i].variation?.offer_discount_price


//
                                            }
                                            val df = DecimalFormat("#.##")
                                            df.setRoundingMode(RoundingMode.CEILING)
                                            if (my_cart[i].variation?.currentBatch != null) {
                                                var d =
                                                        my_cart[i].variation?.currentBatch!!.product_mrp!!.minus(
                                                                my_cart[i].variation?.currentBatch!!.base_rate!!
                                                        )
                                                d *= my_cart[i].quantity!!

                                                if (taxCharges == 0.0f) {
                                                    taxCharges =
                                                            d
                                                } else {
                                                    taxCharges =
                                                            taxCharges + d
                                                }
                                                binding?.gstAmount?.text =
                                                        "-₹" + df.format(taxCharges)
                                            }
                                        }
                                    }
                                    calculationForCheckOut(my_cart)
                                    if (isOffer == true) {
                                        binding.applyCouponLayout.visibility = View.GONE
                                    }

                                } else {
                                    main_layout_empty.visibility = View.VISIBLE
                                    main_constraint.visibility = View.GONE

                                }
                            } else {

                            }
                        }

                        override fun onFailure(
                                call: Call<CartModel?>,
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

    override fun addMoreProduct(p: Int, variationId: String, price: String, count: Int) {
        myDialog.dialogShow()
        //my_cart[p].quantity = count
        addOrUpdatecart((my_cart[p].variation?.variationId).toString(), count)
        couponID = ""
        couponCode = ""
        couponApply.visibility = View.GONE
        couponApply.visibility = View.GONE

        coupon_image_view_arrow.visibility = View.VISIBLE
        tvRemove.visibility = View.GONE
        tvCouponApplied.visibility = View.GONE
        binding?.taxAmount?.text = "-₹0"

        order_total.text = "\u20B9" + (Math.round(orderTotal))
        total_amount.text = "\u20B9" + (Math.round(orderTotal - deliveryCharges))
        discountAmount1 = 0.0f
//        total += (price).toFloat()
//        if (my_cart[p].variation?.product?.tax != null)
//            taxCharges += ((price.toFloat() / 100) * my_cart[p].variation?.product?.tax?.rate?.toFloat()!!)
    }

    override fun addLessProduct(p: Int, variationId: String, price: String, count: Int) {
        myDialog.dialogShow()
        //my_cart[p].quantity = count

        addOrUpdatecart((my_cart[p].variation?.variationId).toString(), count)
        couponID = ""
        couponCode = ""
        couponApply.visibility = View.GONE
        couponApply.visibility = View.GONE

        coupon_image_view_arrow.visibility = View.VISIBLE
        tvRemove.visibility = View.GONE
        tvCouponApplied.visibility = View.GONE
        binding?.taxAmount?.text = "-₹0"

        order_total.text = "\u20B9" + (Math.round(orderTotal))
        total_amount.text = "\u20B9" + (Math.round(orderTotal - deliveryCharges))
        discountAmount1 = 0.0f
//        total -= (price).toFloat()
//        if (my_cart[p].variation?.product?.tax != null)
//            taxCharges -= ((price.toFloat() / 100) * my_cart[p].variation?.product?.tax?.rate?.toFloat()!!)
    }


    override fun clickOnItem(p: Int) {

        StyledAlertDialog.builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_remove)
                .setMessage(R.string.msg_remove_from_cart)
                .setPositiveButton(R.string.text_yes) { dialog, _ ->
                    dialog.dismiss()
                    pos = p
                    myDialog.dialogShow()
                    my_cart[p].id?.let { removeCart(it) }
                }
                .setNegativeButton(R.string.text_no) { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    override fun clickOnView(p: Int) {
        if (isOutOfStock == true) {
            //Toast.makeText(applicationContext,"product out of stock please remove it",Toast.LENGTH_SHORT).show()
        } else {
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
    private fun addOrUpdatecart(product_id: String, quantity: Int) {
        try {

            ApiClientGenerator
                    .getClient()!!
                    .addOrUpdatecart(
                            ContextUtils.getAuthToken(applicationContext),
                            product_id,
                            quantity
                    )
                    .enqueue(object : Callback<ApiResponse<CartData>?> {
                        override fun onResponse(
                                call: Call<ApiResponse<CartData>?>,
                                response: Response<ApiResponse<CartData>?>
                        ) {
                            if (response.body()!!.status == 1) {
                                myCart()

                            } else {
                                AppUtil.firebaseEvent(applicationContext, "error", "error_events", response.body()?.message!!
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
                        if (status == "false") {
                            finish()
                        } else {

                            myCart()

                        }
                    } else {
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

    fun calculationForCheckOut(my_cart: ArrayList<MyCart>) {
        if (my_cart.size == 0) {
            main_layout_empty.visibility = View.VISIBLE
            main_constraint.visibility = View.GONE
        } else {
            main_layout_empty.visibility = View.GONE
            main_constraint.visibility = View.VISIBLE
            addAdapter(my_cart)
            total = 0.0f

            for (j in 0 until my_cart.size) {
                if (my_cart[j].variation?.currentBatch != null) {
                    if (total == 0.0f) {
                        total =
                                (my_cart[j].variation?.currentBatch?.product_mrp!!)
                        total *= my_cart[j].quantity!!
                    } else {
                        total += (my_cart[j].quantity?.let {
                            (my_cart[j].variation?.currentBatch?.product_mrp)?.times(
                                    it
                            )
                        })!!
                    }
                }
            }

            weight = 0.0f

            for (j in 0 until my_cart.size) {
                if (my_cart[j].variation?.currentBatch != null) {
                    if (my_cart[j].variation!!.offer_available!!) {
                        if (my_cart[j].variation!!.offers[0].discount_option!!.contains("Free Shipping")) {
                            if (my_cart[j].variation?.currentBatch!!.base_rate!! >= my_cart[j].variation!!.offers[0].min_order_amount!!) {
                                count = count + 1
                            } else {

                                if (my_cart[j].variation?.currentBatch?.weight != null) {
                                    if (weight == 0.0f) {
                                        weight =
                                                (my_cart[j].variation?.currentBatch?.weight!!.toFloat())
                                        weight *= my_cart[j].quantity!!
                                    } else {
                                        weight += (my_cart[j].quantity?.let {
                                            (my_cart[j].variation?.currentBatch?.weight!!.toFloat())?.times(
                                                    it
                                            )
                                        })!!
                                    }
                                }
                            }
                        } else {
                            if (my_cart[j].variation?.currentBatch?.weight != null) {
                                if (weight == 0.0f) {
                                    weight =
                                            (my_cart[j].variation?.currentBatch?.weight!!.toFloat())
                                    weight *= my_cart[j].quantity!!
                                } else {
                                    weight += (my_cart[j].quantity?.let {
                                        (my_cart[j].variation?.currentBatch?.weight!!.toFloat())?.times(
                                                it
                                        )
                                    })!!
                                }
                            }
                        }
                    } else {
                        if (my_cart[j].variation?.currentBatch?.weight != null) {
                            Log.e("weight", weight.toString())

                            if (weight == 0.0f) {
                                weight =
                                        (my_cart[j].variation?.currentBatch?.weight!!.toFloat())
                                weight *= my_cart[j].quantity!!
                            } else {
                                weight += (my_cart[j].quantity?.let {
                                    (my_cart[j].variation?.currentBatch?.weight!!.toFloat())?.times(
                                            it
                                    )
                                })!!
                            }
                        }
                    }
                }
            }

            orderTotal = 0.0f
            for (j in 0 until my_cart.size) {
                if (my_cart[j].variation?.currentBatch != null) {
                    if (orderTotal == 0.0f) {
                        orderTotal =
                                (my_cart[j].variation?.currentBatch!!.base_rate!!.toFloat())
                        orderTotal *= my_cart[j].quantity!!
                    } else {
                        orderTotal += (my_cart[j].quantity?.let {
                            (my_cart[j].variation?.currentBatch!!.base_rate!!.toFloat())?.times(
                                    it
                            )
                        })!!
                    }
                }
            }
            //bag_total.text = "\u20B9" + total
//            taxCharges = 0.0f
//            for (j in 0 until my_cart.size) {
//                if (my_cart[j].variation?.currentBatch != null)
//                    if (taxCharges == 0.0f) {
//                        if (my_cart.get(j).variation?.product?.taxable.equals("Yes")) {
//                            taxCharges =
//                                    (((my_cart.get(j).variation?.currentBatch?.base_rate!!) * my_cart.get(
//                                            j
//                                    ).quantity!!) - ((my_cart.get(j).variation?.currentBatch?.base_rate!!) * my_cart.get(
//                                            j
//                                    ).quantity!!)
//                                            * (100 / (100 + my_cart.get(j).variation?.product?.tax?.rate?.toFloat()!!)))
//                            Log.e("tax cahnrges", "" + taxCharges)
//
//                        }
//
//                    } else {
//                        if (my_cart[j].variation?.product?.taxable == "Yes") {
//                            taxCharges += (
//                                    (((my_cart.get(j).variation?.currentBatch?.base_rate!!) * my_cart.get(
//                                            j
//                                    ).quantity!!) - ((my_cart.get(j).variation?.currentBatch?.base_rate!!) * my_cart.get(
//                                            j
//                                    ).quantity!!)
//                                            * (100 / (100 + my_cart.get(j).variation?.product?.tax?.rate?.toFloat()!!)))
//                                    )
//                            Log.e("tax charges", "" + taxCharges)
//
//                        }
//                    }
//            }
        }
        getAddress()


        val df = DecimalFormat("#.##")
        df.setRoundingMode(RoundingMode.CEILING)

        delivery_amount.text = "\u20B9" + 0.0
        var bGTotal = (orderTotal - taxCharges)
        if (discountAmount1 == 0.0f) {
            binding?.couponAmount?.text =
                    "₹0.0"
        }

        if (orderTotal > 0) {
            order_total.text = "\u20B9" + Math.round(orderTotal - discountAmount1)
            total_amount.text = "\u20B9" + Math.round(orderTotal - discountAmount1)

        } else {
            order_total.text = "\u20B90"
            total_amount.text = "\u20B90"

        }

        bag_total.text = "\u20B9" + df.format(total)
        Log.e("weight", weight.toString())
//        if (!pincode.equals("")) {
//if(count!=my_cart.size) {
//    myDialog.dialogShow()
//
//    getDeliveryPrice(pincode, df.format(weight))
//}
//        }


    }

    fun getAddress() {
        ApiClientGenerator
                .getClient()!!
                .getAddresses(ContextUtils.getAuthToken(this))
                .enqueue(object : Callback<ApiResponse<AddressResult>?> {

                    override fun onResponse(
                            call: Call<ApiResponse<AddressResult>?>,
                            response: Response<ApiResponse<AddressResult>?>
                    ) {
//                    dismissDialog()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }
                        if (response.body()!!.status == 1) {
                            if (response.body()!!.data.addresses!!.size > 0) {
                                changeaddress_txt.text = "Change address"
                                // if (response.body()!!.data.addresses!!.get(0).address2!!.isEmpty()) {
                                address.text =
                                        response.body()!!.data.addresses!!.get(0).address1 + ", " + response.body()!!.data.addresses!!.get(
                                                0
                                        ).address2

                                pincode = response.body()!!.data.addresses!!.get(0).zip.toString()
                                if (!pincode.equals("")) {
                                    val df = DecimalFormat("#.##")
                                    df.setRoundingMode(RoundingMode.CEILING)
                                    if (count != my_cart.size) {
                                        myDialog.dialogShow()
                                        getDeliveryPrice(pincode, df.format(weight))
                                    }
                                }

//                            } else {
//                                address.text = response.body()!!.data.addresses!!.get(0).address1
//
//                            }
                                addressId = response.body()!!.data.addresses!!.get(0).id.toString()
                            } else {
                                changeaddress_txt.text = "Add address"
                            }
                        } else {
                            AppUtil.firebaseEvent(applicationContext, "error", "error_events", response.body()?.message!!
                            )
                        }
                    }

                    override fun onFailure(
                            call: Call<ApiResponse<AddressResult>?>,
                            t: Throwable
                    ) {
//                    dismissDialog()
                    }
                })
    }

    fun placeOrderApi(paymentType:String) {
        val obj = JSONObject()
        val arr = JSONArray()
        obj.put(Constants.address_id, addressId)
        obj.put(Constants.payment_type, paymentType)
        obj.put(Constants.platform_type, "Android")
        obj.put(Constants.bag_amount, "" + total)
        obj.put(Constants.discount, taxCharges)
        obj.put(Constants.coupon_code, "" + couponCode)

//        obj.put(Constants.coupon_code, "" + couponID)
        obj.put(Constants.coupon_discount, "" + discountAmount1)
        obj.put(
                Constants.delivery_charges,
                binding.deliveryAmount.text.toString().replace("\u20B9", "")
        )

        if (gstinNumber.toString().length > 0) {
            obj.put("gst_no", gstinNumber)
            obj.put("gst_name", businessname)


        }

        obj.put(Constants.total_amount, total_amount.text.toString().replace("\u20B9", ""))
        for (j in 0 until my_cart.size) {
            val obj = JSONObject()
            obj.put(Constants.product_id, my_cart[j].variation?.variationId.toString())
            obj.put(Constants.quantity, my_cart[j].quantity)
            if (my_cart[j].variation?.offer_available!!) {
                obj.put(Constants.offer_id, my_cart[j].variation?.offers!![0].id)
            } else {
                obj.put(Constants.offer_id, "")

            }


            arr.put(obj)
        }
        obj.put(Constants.products, arr)
        Log.e("obj", obj.toString())

        val model = Gson().fromJson(obj.toString(), CheckOutDataModel::class.java)
        ApiClientGenerator
                .getClient()!!
                .placeOrder(model, ContextUtils.getAuthToken(this))
                .enqueue(object : Callback<ApiResponse<OrderConfirmation>?> {

                    override fun onResponse(
                            call: Call<ApiResponse<OrderConfirmation>?>,
                            response: Response<ApiResponse<OrderConfirmation>?>
                    ) {

//                    dismissDialog()

                        myDialog.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }
                        if (response.body()!!.status == 1) {
                            // showToast(response.body()!!.message)
                            val order = response.body()?.data
                            orderId = response.body()?.data!!.id
                            order_number = response.body()?.data!!.order_number

                            Log.e("order_id", orderId)
                            if(paymentType=="COD"){
                                OrderConfirmationActivity.start(
                                    this@CheckoutActivity,
                                    order_number
                                )
                            }else{
                                startPayment(order)
                            }

                            // finish()
                        } else {
                            AppUtil.firebaseEvent(applicationContext, "error", "error_events", response.body()?.message!!
                            )
                            showToast(response.body()?.message!!)
                        }
                    }

                    override fun onFailure(
                            call: Call<ApiResponse<OrderConfirmation>?>,
                            t: Throwable
                    ) {

                        myDialog.dialogDismiss()

//                    dismissDialog()
                    }
                })
    }

    fun setUpPayUMoney(
            order: OrderConfirmation?,
            model: CheckOutDataModel
    ) {

        var amount = order?.payment_config?.amount
        try {
            amount = amount?.toDouble()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        startpay(order);

    }

    var builder = PaymentParam.Builder()

    //declare paymentParam object
    var paymentParam: PaymentParam? = null
    fun startpay(order: OrderConfirmation?) {
        orderNumber = order?.order_number.toString()
        builder.setAmount(order?.payment_config?.amount!!) // Payment amount
                .setTxnId(order?.payment_config?.txnid) // Transaction ID
                .setPhone(order?.delivery_address?.phone) // User Phone number
                .setProductName(order?.payment_config?.productInfo) // Product Name or description
                .setFirstName(order?.payment_config?.firstname) // User First name
                .setEmail(order?.payment_config?.email) // User Email ID
                .setsUrl(Constants.pay_u_success_url) // Success URL (surl)
                .setfUrl(Constants.pay_u_failure_url) //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5(order?.payment_config?.udf5)

                .setIsDebug(false) // Integration environment - true (Debug)/ false(Production)
                .setKey(Constants.pay_u_merchant_key) // Merchant key

                .setMerchantId(Constants.pay_u_merchant_id)

        try {
            paymentParam = builder.build()
            paymentParam!!.setMerchantHash(order?.payment_config?.hash)


            Log.e("hash", order?.payment_config?.hash)

            PayUmoneyFlowManager.startPayUMoneyFlow(
                    paymentParam,
                    this@CheckoutActivity,
                    R.style.AppTheme,
                    true
            )
        } catch (e: Exception) {
            //Failure Transaction
            showToast(e.message.toString());
            afterPaymentFailedApi(orderId)
        }

//        val serverCalculatedHash = hashCal(
//            "SHA-512",
//            Constants.pay_u_merchant_key + "|" + order?.payment_config?.txnid + "|" +order?.payment_config?.amount!!.toDouble() + "|" + order?.payment_config?.productInfo + "|"
//                    + order?.payment_config?.firstname + "|" + order?.payment_config?.email + "|" + order?.payment_config?.udf5 + "|" + order?.payment_config?.udf5 + "|" + order?.payment_config?.udf5 + "|" + order?.payment_config?.udf5 + "|" + order?.payment_config?.udf5 + "||||||" + Constants.pay_u_merchant_salt
//        )


    }

    fun hashCal(type: String?, str: String): String? {
        val hashseq = str.toByteArray()
        val hexString = StringBuffer()
        try {
            val algorithm: MessageDigest = MessageDigest.getInstance(type)
            algorithm.reset()
            algorithm.update(hashseq)
            val messageDigest: ByteArray = algorithm.digest()
            for (i in messageDigest.indices) {
                val hex =
                        Integer.toHexString(0xFF and messageDigest[i].toInt())
                if (hex.length == 1) {
                    hexString.append("0")
                }
                hexString.append(hex)
            }
        } catch (nsae: NoSuchAlgorithmException) {
        }
        return hexString.toString()
    }

//    fun afterPaymentSuccessApi(model: JSONObject) {
//
//        val parser = JsonParser();
//        val json = parser!!.parse(model.toString()) as JsonObject;
//
//
//
//
//        ApiClientGenerator
//            .getClient()!!
//            .updatePaymentStatus(json, ContextUtils.getAuthToken(this))
//            .enqueue(object : Callback<ResponseBody?> {
//
//                override fun onResponse(
//                    call: Call<ResponseBody?>,
//                    response: Response<ResponseBody?>
//                ) {
//
////                    dismissDialog()
//
//                    myDialog.dialogDismiss()
//
//                    if (response.code() < 200 || response.code() >= 300) {
//                        return
//                    }
//
//                    val response = JSONObject(response.body()?.string()!!)
//                    Log.e("response", response.toString())
//                    if (response.getString("status") == "1") {
//                        OrderConfirmationActivity.start(
//                            this@CheckoutActivity,
//                            orderNumber
//                        )
//                        // finish()
//                    } else {
//
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<ResponseBody?>,
//                    t: Throwable
//                ) {
//
//                    myDialog.dialogDismiss()
//
////                    dismissDialog()
//                }
//            })
//    }

    fun afterPaymentSuccessApi(razorpay_payment_id: String) {


        ApiClientGenerator
                .getClient()!!
                .updatePaymentStatus(razorpay_payment_id, orderId, ContextUtils.getAuthToken(this))
                .enqueue(object : Callback<ResponseBody?> {

                    override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                    ) {

//                    dismissDialog()

                        myDialog.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("response", response.toString())
                        if (response.getString("status") == "1") {
                            OrderConfirmationActivity.start(
                                    this@CheckoutActivity,
                                    order_number
                            )
                            // finish()
                        } else {
                            showToast(response.getString("message"))
                            afterPaymentFailedApi(orderId)
                            AppUtil.firebaseEvent(applicationContext, "error", "error_events", response.getString("message")
                            )
                        }
                    }

                    override fun onFailure(
                            call: Call<ResponseBody?>,
                            t: Throwable
                    ) {

                        myDialog.dialogDismiss()

//                    dismissDialog()
                    }
                })
    }

    fun getDeliveryPrice(pincode: String, weight: String) {

        ApiClientGenerator
                .getClient()!!
                .getDeliveryPrice(ContextUtils.getAuthToken(this), pincode, weight)
                .enqueue(object : Callback<ResponseBody?> {

                    override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                    ) {

//                    dismissDialog()

                        myDialog.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("response", response.toString())
                        if (response.getString("status") == "1") {
                            binding.deliveryAmount.setText("₹" + response.getString("amount"))
                            val df = DecimalFormat("#.##")
                            df.setRoundingMode(RoundingMode.CEILING)
                            total_amount.setText(
                                    "₹" + Math.round(
                                            order_total.text.toString().replace("\u20B9", "")
                                                    .toFloat() + response.getString("amount").toFloat()
                                    )
                            )

                            // finish()
                        } else {
                            binding.deliveryAmount.setText("₹0")
                            total_amount.setText("₹" + Math.round(orderTotal))
                            AppUtil.firebaseEvent(applicationContext, "error", "error_events", response.getString("message")
                            )

                        }
                    }

                    override fun onFailure(
                            call: Call<ResponseBody?>,
                            t: Throwable
                    ) {

                        myDialog.dialogDismiss()

//                    dismissDialog()
                    }
                })
    }


    fun afterPaymentFailedApi(orderId: String) {


        ApiClientGenerator
                .getClient()!!
                .fetchAllSectionProducts(ContextUtils.getAuthToken(this), orderId, "false")
                .enqueue(object : Callback<ResponseBody?> {

                    override fun onResponse(
                            call: Call<ResponseBody?>,
                            response: Response<ResponseBody?>
                    ) {

//                    dismissDialog()

                        myDialog.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("response", response.toString())
                        if (response.getString("status") == "1") {

                            // finish()
                        } else {
                            AppUtil.firebaseEvent(applicationContext, "error", "error_events", response.getString("message")
                            )
                        }
                    }

                    override fun onFailure(
                            call: Call<ResponseBody?>,
                            t: Throwable
                    ) {

                        myDialog.dialogDismiss()

//                    dismissDialog()
                    }
                })
    }


    private fun openGSTINBySheet() {
        val myDrawerView = layoutInflater.inflate(R.layout.gstin_view_layout, null)
        val gstinbinding =
                GstinViewLayoutBinding.inflate(layoutInflater, myDrawerView as ViewGroup, false)

        bsd = BottomSheetDialog(this)
        bsd.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bsd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bsd.setContentView(gstinbinding.root)
        bsd.setCanceledOnTouchOutside(true)
        bsd.setCancelable(true)
        bsd.show()

        gstinbinding.gstinEd.setText(gstinNumber)
        gstinbinding.businessEd.setText(businessname)
        gstinbinding.closeView.setOnClickListener {
            bsd.dismiss()

        }
        gstinbinding.btnConfirm.setOnClickListener {
            if (gstinbinding.gstinEd.text.toString().length == 0) {
                Toast.makeText(applicationContext, "Please enter GSTIN Number", Toast.LENGTH_SHORT)
                        .show()
            } else if (gstinbinding.businessEd.text.toString().length == 0) {
                Toast.makeText(applicationContext, "Please enter Business Name", Toast.LENGTH_SHORT)
                        .show()

            } else if (AppValidationUtil.gstinAddress(gstinbinding.gstinEd.text.toString()) == false) {
                Toast.makeText(applicationContext, "Please enter Valid GSTIN", Toast.LENGTH_SHORT)
                        .show()

            } else {
                gstinNumber = gstinbinding.gstinEd.text.toString()
                businessname = gstinbinding.businessEd.text.toString()
                binding.radioImage.setImageResource(R.drawable.on_button)
                binding.gstinNumberView.setText(gstinNumber)
                binding.editView.visibility = View.VISIBLE
                bsd.dismiss()

            }

        }

    }

    //------------Razorpay payment method---------------
    private fun startPayment(order: OrderConfirmation?) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        var amount = Math.round(order!!.payment_config.amount.toFloat())

        try {
            val options = JSONObject()



            options.put("name", "GadgetMart")
            //options.put("key","rzp_test_jwCi9xQ9IIgUEK")


            //You can omit the image option to fetch the image from dashboard
            options.put("currency", order!!.payment_config.currancy)
            options.put("amount", amount.toInt())
            options.put("theme.color", "#F55758");
            options.put("order_id", order!!.payment_config.payment_order_id);//from response of step 3.
            val prefill = JSONObject()
            prefill.put("email", order!!.payment_config.email)
            prefill.put("contact", order!!.payment_config.phone)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Log.e("Error in payment: ", e.message)
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            afterPaymentFailedApi(orderId)

            e.printStackTrace()
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        try {
            showToast(response!!);
            afterPaymentFailedApi(orderId)
            AppUtil.firebaseEvent(applicationContext, "", "payment_failed ", "")


        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()

        }


    }

    override fun onPaymentSuccess(rzpPaymentId: String?) {
        try {
            AppUtil.firebaseEvent(applicationContext, "", "payment_success", "")

            orderNumber = rzpPaymentId!!
            myDialog.dialogShow()

//            handler = Handler()
//            handler!!.postDelayed(runnable, timeDelay)

//
            afterPaymentSuccessApi(rzpPaymentId!!)

        } catch (e: Exception) {
            AppUtil.firebaseEvent(applicationContext, "", "payment_failed ", "")

            Log.e("error", e.message)
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()

        }

    }

    private var handler: Handler? = null
    private val timeDelay: Long = 3000
    lateinit var global: Global

    private val runnable: Runnable = Runnable {
        myDialog.dialogDismiss()
        OrderConfirmationActivity.start(
                this@CheckoutActivity,
                orderNumber
        )
    }

    private fun editValid(message: String) {


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
        dialog_msg.text = message
        dialog_title.text = "Warning?"

        btnok.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, 101)

        }

        btncancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


}
