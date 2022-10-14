package com.gadgetmart.ui.product_details

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.ActivityProductDetailsBinding
import com.gadgetmart.databinding.VarientBottomViewBinding
import com.gadgetmart.ui.cart_bag.CartActivity
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.checkout.CheckoutActivity
import com.gadgetmart.ui.checkout.CheckoutActivity_Direct
import com.gadgetmart.ui.product_details.productmodel.CartData
import com.gadgetmart.ui.products_of_sub_category.ProductDataWishlist
import com.gadgetmart.ui.products_of_sub_category.ProductImages
import com.gadgetmart.ui.products_of_sub_category.ProductsAdapterListener
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.gadgetmart.ui.search.SearchActivity
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.ui.support.TermsAndCondtions
import com.gadgetmart.ui.support.WebActivity
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.ui.wishlist.WishListResult
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.gadgetmart.util.custom.StyledAlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.LinkProperties
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.address_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.parceler.Parcels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat


class ProductDetailsActivity : BaseActivity<ActivityProductDetailsBinding>(), ProductDetailContact,
    ProductsAdapterListener, AddressBottomSheet.ItemClickListener, VarientItemClickInterface,
    SubCategoryInterVariant, OfferItemClick {
    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var bindingBottom: VarientBottomViewBinding
    private lateinit var productDetailPresenter: ProductDetailPresenter
    private var variationAdapter: Varientadapter? = null
    private var product_id = ""
    private var productID = ""
    private var vId = ""

    private var categoryName = ""
    private var variationId = ""
    private var wishListVariationId = ""
    private var wishListId = 0
    private var type = ""
    private var productPosition = 0
    private var productsDataItems: ProductsDataItems? = null
    private var productImages: ArrayList<ProductImages>? = ArrayList()
    private var variations: ArrayList<ProductVariation>? = ArrayList()
    private var variantList: ArrayList<SelectedVarient>? = ArrayList()
    private var variantModelList: ArrayList<VarientModel>? = ArrayList()
    private var specificationsList: ArrayList<ProductSpecificationNew>? = ArrayList()

    var savePrecentage = 0.0

    var errorMessage = ""

    var varientDetailResult: ProductDetailResult? = null

    private var currentPage = 0
    private var NUM_PAGES = 0
    private var variation: ProductVariation? = null
    private var isAddedToWishList = false
    private var wishListData: ProductDataWishlist? = null
    var quantityValue = 1
    var quantityInCart = 0
    var productQuantityValue = 1
    var status = "false"
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private var addressBottomSheet: AddressBottomSheet? = null
    private lateinit var myDialog: CustomProgressDialog
    var save_amount = "";
    var save_percent = "";
    var Id = ""
    lateinit var global: Global

    private lateinit var bsd: BottomSheetDialog


    companion object {
        fun start(context: Context, productId: String) {
            context.startActivity(
                Intent(
                    context,
                    ProductDetailsActivity::class.java
                ).putExtra("productId", productId)
            )
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_product_details
    }

    override fun init(binding: ActivityProductDetailsBinding) {
        this.binding = binding
        productImages = ArrayList()
        global = applicationContext as Global
        getintentData()
        initRecyclerView()
        initPresenter()
        initListeners(binding)
        global.varitionID = ""
        global.productID = ""
        myDialog = CustomProgressDialog(this@ProductDetailsActivity)
    }

    private fun getintentData() {
        if (intent.extras != null) {

            product_id = intent.extras?.getString("productId")!!
            categoryName = intent.extras?.getString("category_name")!!
            productPosition = intent.extras?.getInt("position")!!
            type = intent.extras?.getString("type")!!
            productID = intent.extras?.getString("id")!!


        }
    }

    private fun initRecyclerView() {
        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding().variationsRecyclerView.layoutManager = subCategoriesLayoutManager

        variationAdapter = Varientadapter(this, ArrayList(), this)
        binding.variationsRecyclerView.adapter = variationAdapter
        binding.variationsRecyclerView.isNestedScrollingEnabled = false
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)

//        bottomSheetBehavior?.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
//                        toolbar_profile_relative_layout.visibility = View.INVISIBLE
//                    }
//                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                        toolbar_profile_relative_layout.visibility = View.INVISIBLE
//                    }
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//
//                        toolbar_profile_relative_layout.visibility = View.INVISIBLE
//                    }
//                    BottomSheetBehavior.STATE_EXPANDED -> {
//                        binding.toolbar.setBackgroundColor(resources.getColor(R.color.appBackground))
//                        toolbar_profile_relative_layout.visibility = View.VISIBLE
//                    }
//                    BottomSheetBehavior.STATE_DRAGGING -> {
//                        binding.toolbar.setBackgroundColor(resources.getColor(R.color.transparent))
//                        toolbar_profile_relative_layout.visibility = View.INVISIBLE
//
//                    }
//                    BottomSheetBehavior.STATE_SETTLING -> {
//                        toolbar_profile_relative_layout.visibility = View.INVISIBLE
//
//                    }
//                }
//            }
//        })
    }

    private fun initListeners(binding: ActivityProductDetailsBinding) {
        binding.warrentyTxt.setOnClickListener {
            val i = Intent(applicationContext, WebActivity::class.java)
            i.putExtra("type", "Warranty Details")
            i.putExtra("data", "https://gadgetmart.in/public/getGeneralInfo/4")

            startActivity(i)
//            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gadgetmart.in/public/getGeneralInfo/4"))
//            startActivity(browserIntent)
        }
        binding.toolbarID.backIcon.setOnClickListener {
            onBackPressed()
        }
        toolbar_ohnik_image_view.setOnClickListener {
            val intent = Intent(applicationContext, SearchActivity::class.java)

            startActivity(intent)
        }
        binding.toolbarID.toolbarTitleTextView.text = categoryName
        binding.productReadMoreTextView.setOnClickListener {
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                loginUser(binding, "Please login to continue")
            } else {
                ProductDescriptionActivity.start(
                    this,
                    variation,
                    save_amount!!,
                    specificationsList!!


                )
            }
        }
        binding.addToBagBtn.setOnClickListener {
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                loginUser(binding, "To add this product to cart you need to\nlogin first")
            } else {
                if (quantityInCart == 0 && quantityValue >= 1) {
                    callAddedCartApi("bag", quantityValue,true)
                } else {
                    startActivity(Intent(this, CartActivity::class.java))
                }
            }

            /*  val i = Intent(applicationContext, CartActivity::class.java)
              Log.e(
                  "image",
                  productsDataItems!!.image + " " + productsDataItems!!.images!!.get(0).name
              )
              if (productsDataItems!!.image.equals("")) {
                  i.putExtra("image", productsDataItems!!.images!!.get(0).name)

              } else {
                  i.putExtra("image", productsDataItems!!.image)
              }
              i.putExtra("name", productsDataItems!!.name)
              i.putExtra("net price", productsDataItems!!.current_batch!!.net_price!!)
              i.putExtra("base price", productsDataItems!!.current_batch!!.product_mrp!!)



              startActivity(i)*/
        }
        binding.updateCountLayout.decreaseCountImageView.setOnClickListener {
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                loginUser(binding, "To add this product to cart you need to\nlogin first")
            } else {
                if (quantityInCart == 0) {
                    if (quantityValue > 1) {
                        quantityValue--
                        binding.updateCountLayout.quantityTextView.text = "" + quantityValue
                    }
                } else {
                    if (quantityValue != 1) {
                        callAddedCartApi("bag", quantityValue - 1,false)
                    } else {

                        StyledAlertDialog.builder(this)
                            .setCancelable(false)
                            .setTitle(R.string.text_remove)
                            .setMessage(R.string.msg_remove_from_cart)
                            .setPositiveButton(R.string.text_yes) { dialog, _ ->
                                dialog.dismiss()
                                myDialog.dialogShow()
                                removeCart(variation!!.cart!!.id!!)
                            }
                            .setNegativeButton(R.string.text_no) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }
                }
            }
        }

        binding.pinCodeEditText.setOnClickListener { showBottomSheet() }

        binding.updateCountLayout.increaseCountImageView.setOnClickListener {
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                loginUser(binding, "To add this product to cart you need to\nlogin first")
            } else {
                if (quantityValue == productQuantityValue) {
                    showToast("Out of stock product")
                } else {
                    if (quantityInCart == 0) {
                        quantityValue++
                        binding.updateCountLayout.quantityTextView.text = "" + quantityValue
                    } else {
                        callAddedCartApi("bag", quantityValue + 1,false)
                    }
                }
            }
        }

        binding.removeTextView.setOnClickListener {

            StyledAlertDialog.builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_remove)
                .setMessage(R.string.msg_remove_from_cart)
                .setPositiveButton(R.string.text_yes) { dialog, _ ->
                    dialog.dismiss()
                    myDialog.dialogShow()
                    removeCart(variation!!.cart!!.id!!)
                }
                .setNegativeButton(R.string.text_no) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
        toolbar_cart_icon.setOnClickListener {
            val intent = Intent(applicationContext, CartActivity::class.java)

            startActivityForResult(intent, 101)
        }

        binding().shareProductLayout.setOnClickListener {
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                loginUser(binding, "Please login to continue")
            } else {
                onShareButtonTapped()
            }
        }
        binding().addToWishlistLayout.setOnClickListener {
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                loginUser(binding, "To add this product to wishlist you need to\nlogin first")
            } else {
                onWishListTapped()
            }
        }
        buy_btn.setOnClickListener {
            if (PreferenceManager().getInstance(this).getAuthToken().equals("")) {
                loginUser(binding, "To buy this product you need to\nlogin first")
            } else {
                Log.e("cariationreponse", Gson().toJson(variation))
                val intent = Intent(applicationContext, CheckoutActivity_Direct::class.java)
                intent.putExtra("variations", variation)
                intent.putExtra("quantity",binding.updateCountLayout.quantityTextView.text.toString())
                startActivity(intent)
            }


        }


//        val density = resources.displayMetrics.density

//Set circle indicator radius
        //Set circle indicator radius
//        binding.dotsIndicator.setRadius(5 * density)

        // Pager listener over indicator
        // Pager listener over indicator


    }

    fun showBottomSheet() {
        addressBottomSheet = AddressBottomSheet.newInstance(this)
        addressBottomSheet!!.show(supportFragmentManager, AddressBottomSheet.TAG)
    }

    private fun onWishListTapped() {
        //            productDetailPresenter = ProductDetailPresenter(this, this)
        if (isAddedToWishList) {
            productDetailPresenter.removeFromWishList(
                wishListId,
                ContextUtils.getAuthToken(this)
            )
        } else {
            productDetailPresenter.addToWishList(
                variation?.variationId!!,
                ContextUtils.getAuthToken(this)
            )
        }

    }

    private fun onShareButtonTapped() {
        try {
            if (productImages == null || productImages!!.size == 0) {
                genratelinkType(
                    binding.productNameTextView.text.toString(),
                    binding.productDetailTextView.text.toString(),
                    "",
                    product_id,
                    productID
                );

            } else {
                genratelinkType(
                    binding.productNameTextView.text.toString(),
                    binding.productDetailTextView.text.toString(),
                    productImages!![0].name,
                    product_id,
                    productID
                );

            }
//            val shareIntent = Intent(Intent.ACTION_SEND)
//            shareIntent.type = "text/plain"
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
//            var shareMessage = "\nLet me recommend you this application\n\n"
//            shareMessage =
//                "$shareMessage https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".trimIndent()
//            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
//            startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }

    private fun initPresenter() {
        productDetailPresenter = ProductDetailPresenter(this, this)
        productDetailPresenter.getProductDetail(
            productID,
            ContextUtils.getAuthToken(this)
        )
        updateRecent(productID)

    }

    override fun onResume() {
        super.onResume()
        // initPresenter()
    }

    fun callAddedCartApi(type: String, quantity: Int,isFirstTime: Boolean) {
        myDialog.dialogShow()
        if (quantity == 1 && quantityInCart == 1) {
            StyledAlertDialog.builder(this)
                .setCancelable(false)
                .setTitle(R.string.text_remove)
                .setMessage(R.string.msg_remove_from_cart)
                .setPositiveButton(R.string.text_yes) { dialog, _ ->
                    dialog.dismiss()
                    myDialog.dialogShow()
                    removeCart(variation!!.cart!!.id!!)
                }
                .setNegativeButton(R.string.text_no) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        } else addOrUpdateCart(variationId, quantity, type,isFirstTime)
    }

    override fun onProductDetailDataFound(
        productDetailResult: ProductDetailResult?,
        message: String
    ) {
//        showToast(message)

        if (productDetailResult?.cartCount != null && productDetailResult.cartCount != "0") {
            binding.updateCountLayout.quantityTextView.text = "" + quantityValue
            binding().toolbarID.toolbarCartItemCount.text =
                productDetailResult.cartCount
            binding().toolbarID.toolbarCartItemCount.visibility = View.VISIBLE
        } else {
            binding().toolbarID.toolbarCartItemCount.visibility = View.GONE
        }
        variation = productDetailResult!!.default_variant

        var p =
            (variation!!.save_amount!!.toDouble() / variation!!.currentBatch!!.product_mrp!!.toDouble()) * 100

        binding.saveTxt.text = "You save ₹" + AppUtil.roundTwoDecimalPlaces(
            this,
            variation!!.save_amount!!.toDouble()
        ) + "(" + AppUtil.roundTwoDecimalPlaces(this, p) + "%)"
        save_amount = binding.saveTxt.text.toString();

        variationId = variation!!?.variationId
        variantList = productDetailResult!!.selected_variant
        variantModelList = productDetailResult!!.variants

        updateUI()
        binding.mainLayout.smoothScrollBy(0, 0)
        specificationsList = productDetailResult.product_specifications

//        if(variations!=null){
//        }
    }

    fun updateRecent(variationId: String) {
        productDetailPresenter.addToRecent(
            variationId,
            ContextUtils.getAuthToken(this)
        )
    }

    private fun updateUI() {

        myDialog.dialogShow()


        vId = variation!!.productId.toString()
        if (variation?.product_reviews!!.size > 0) {
            binding.reviewView.visibility = View.VISIBLE

            binding.productRating.rating = variation?.rating!!.toFloat()
            binding.ratingsCountTextView.setText("(" + variation?.rating!!.toString() + ")")
            binding.totalReviewsTextView.setText(variation?.product_reviews!!.size.toString() + " Reviews")
            val subCategoriesLayoutManager = LinearLayoutManager(this)

            subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

            binding().productReviewsRecyclerView.layoutManager = subCategoriesLayoutManager

            binding().productReviewsRecyclerView.adapter =
                ProductDetailReviewAdapter(this, variation?.product_reviews!!)


        } else {
            binding.reviewView.visibility = View.GONE

        }
        if (variation?.offer_available!!) {
            binding.offerAvailable.visibility = View.VISIBLE

            val subCategoriesLayoutManager = LinearLayoutManager(this)

            subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

            binding().offerListing.layoutManager = subCategoriesLayoutManager

            binding().offerListing.adapter =
                OfferListingAdapter(this, variation?.offers, this@ProductDetailsActivity)


        } else {
            binding.offerAvailable.visibility = View.GONE

        }

        if (variation?.wishList != null) {
            wishListVariationId = variation?.wishList?.variation_id!!
            wishListId = variation?.wishList?.wishList_id!!
            updateIconTint(true, binding().addToWishlistTextView, R.color.colorPrimary)
        } else {
            wishListVariationId = ""
            wishListId = 0
            updateIconTint(false, binding().addToWishlistTextView, R.color.colorHeartEmpty)
        }

        if (variation?.rating != null) {
            var avg = variation?.rating

            binding.rateProduct.rating = avg!!.toFloat()


        }
        binding().colorLabelTextView.text = "Select Variant"
        Log.e("itme", Gson().toJson(productsDataItems))
        productImages = variation?.productImages
        variationAdapter!!.updateList(variantList)

        if (productImages == null || productImages!!.size == 0) {
            variation?.productImages?.let { productImages?.addAll(it) }

        }
        binding.productImagesViewPager.adapter =
            variation?.productImages?.let { SlidingImageAdapter(this, it) }

        NUM_PAGES = productImages?.size ?: 0

        binding.dotsIndicator.attachToViewPager(binding.productImagesViewPager)

        binding.productNameTextView.text = variation?.product_title
        AppUtil.firebaseEvent(
            applicationContext,
            "name",
            "product_viewed",
            variation?.product_title!!
        )


        if (variation!!.product?.brand == null) {
            binding.brandNameTextView.visibility = View.GONE

        } else {
            binding.brandNameTextView.text = "Brand: " + variation!!.product?.brand

        }
        if (variation?.product!!.description != null) {
            binding.productDetailTextView.text = variation?.product!!.description

        } else {
            binding.productDetailTextView.text = variation?.product!!.short_description

        }

        val df = DecimalFormat("#.##")
        df.setRoundingMode(RoundingMode.CEILING)
        if (variation?.currentBatch != null) {
            if (variation?.offer_available!!) {
                binding.saveTxt.visibility = View.VISIBLE

                binding.productNetPriceTextView.visibility = View.VISIBLE
                binding.productNetPriceTextView.paintFlags =
                    binding.productNetPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                val str = resources.getText(R.string.us_dollar).toString()
                    .plus(df.format(variation!!.currentBatch?.product_mrp))
                binding.productNetPriceTextView.text = str


                binding.productDiscountedPriceTextView.text =
                    "\u20B9" + df.format(variation!!.offer_discount_price)
                var p =
                    (variation!!.save_amount!!.toDouble() / variation!!.currentBatch!!.product_mrp!!.toDouble()) * 100
                savePrecentage = AppUtil.roundTwoDecimalPlaces(this, p)
                if (savePrecentage < 1) {
                    binding.saveTxt.text = "You save ₹" + df.format(
                        AppUtil.roundTwoDecimalPlaces(
                            this,
                            variation!!.save_amount!!.toDouble()
                        )
                    ) + "(" + savePrecentage + "%)"
                } else {
                    binding.saveTxt.text = "You save ₹" + df.format(
                        AppUtil.roundTwoDecimalPlaces(
                            this,
                            variation!!.save_amount!!.toDouble()
                        )
                    ) + "(" + Math.round(savePrecentage) + "%)"
                }

                save_amount = binding.saveTxt.text.toString()
            } else {
                if (variation!!.currentBatch?.product_mrp == variation!!.currentBatch?.base_rate!!) {
                    binding.productNetPriceTextView.visibility = View.INVISIBLE
                    binding.productDiscountedPriceTextView.visibility = View.VISIBLE







                    binding.productDiscountedPriceTextView.text =
                        "\u20B9" + df.format(variation!!.currentBatch?.product_mrp)

                    binding.saveTxt.visibility = View.GONE
                } else {
                    binding.saveTxt.visibility = View.VISIBLE

                    binding.productNetPriceTextView.visibility = View.VISIBLE
                    binding.productNetPriceTextView.paintFlags =
                        binding.productNetPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    val str = resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(variation!!.currentBatch?.product_mrp))
                    binding.productNetPriceTextView.text = str


                    binding.productDiscountedPriceTextView.text =
                        "\u20B9" + df.format(variation!!.currentBatch?.base_rate)
                    var p =
                        (variation!!.save_amount!!.toDouble() / variation!!.currentBatch!!.product_mrp!!.toDouble()) * 100
                    savePrecentage = AppUtil.roundTwoDecimalPlaces(this, p)
                    if (savePrecentage < 1) {
                        binding.saveTxt.text = "You save ₹" + df.format(
                            AppUtil.roundTwoDecimalPlaces(
                                this,
                                variation!!.save_amount!!.toDouble()
                            )
                        ) + "(" + savePrecentage + "%)"
                    } else {
                        binding.saveTxt.text = "You save ₹" + df.format(
                            AppUtil.roundTwoDecimalPlaces(
                                this,
                                variation!!.save_amount!!.toDouble()
                            )
                        ) + "(" + Math.round(savePrecentage) + "%)"
                    }

                    save_amount = binding.saveTxt.text.toString();
                }
            }
            productQuantityValue = (variation!!.currentBatch!!.balance_quantity!!).toInt()
            if (variation!!.cart != null && variation?.cart?.quantity != 0) {
                quantityInCart = variation!!.cart?.quantity!!
                quantityValue = quantityInCart
                binding.updateCountLayout.quantityTextView.text = "" + quantityInCart
                binding.removeTextView.visibility = View.GONE
                binding.addToBagBtn.text = getString(R.string.action_go_to_cart)
                status = "true"
                AppUtil.firebaseEvent(
                    applicationContext,
                    "name",
                    "product_added_to_cart",
                    variation?.product_title!!
                )

                global.notificationSchdule(
                    applicationContext,
                    PreferenceManager().getInstance(applicationContext).getUserName()!!
                )


            } else {
                quantityInCart = 0
//                    quantityValue = 1
                binding.updateCountLayout.quantityTextView.text = "" + quantityValue
                binding.addToBagBtn.text = getString(R.string.action_add_to_bag)
                binding.removeTextView.visibility = View.GONE
                status = "false"
                global.cancelNotification(applicationContext)

            }

        }

//            if (productsDataItems?.taxable.equals("yes", ignoreCase = true)) {
//                val taxDetails: ProductItemsTax = productsDataItems?.tax!!
//                binding.productTax.visibility = View.VISIBLE
//                if (taxDetails.rate.toString().isNotEmpty()) {
//                    binding.productTax.text = taxDetails.name + " " + taxDetails.rate + "%"
//                }
//            } else {
//                binding.productTax.visibility = View.INVISIBLE
//            }

//            } else {
//                price_calculate_view.visibility = View.GONE
//                binding.bottomLayout.visibility = View.INVISIBLE
////            binding.quantityLayout.visibility = View.INVISIBLE
//
//
////            binding.productTax.text = "Out of stock"
//
//            }


        val favDrawable: Drawable = ContextCompat.getDrawable(this, R.drawable.fav_enable)!!
        val removeFavDrawable: Drawable =
            ContextCompat.getDrawable(this, R.drawable.fav_disable)!!
        if (variation?.wishList != null) {
            isAddedToWishList = true
            productsDataItems?.isAddedToWishList = true
            binding.favProductImageView.setImageDrawable(favDrawable)
        } else {
            isAddedToWishList = false
            productsDataItems?.isAddedToWishList = false
            binding.favProductImageView.setImageDrawable(removeFavDrawable)
        }

        binding.favProductImageView.setOnClickListener {
            val wishlist = variation?.wishList
            //            productDetailPresenter = ProductDetailPresenter(this, this)
            if (wishlist != null) {
                productDetailPresenter.removeFromWishList(
                    wishlist.wishList_id!!,
                    ContextUtils.getAuthToken(this)
                )
            } else {
                AppUtil.firebaseEvent(
                    applicationContext,
                    "name",
                    "wishlist",
                    variation?.product_title!!
                )

                productDetailPresenter.addToWishList(
                    variation?.variationId!!,
                    ContextUtils.getAuthToken(this)
                )
            }

        }

        myDialog.dialogDismiss()
    }

    private fun clearVariationSelection() = variations!!.forEach {
        it.isSelected = false
    }

    override fun onProductDetailDataNotFound(message: String) {
        AppUtil.firebaseEvent(applicationContext, "error", "error_events", message)
        showToast(message)
    }

    override fun onProductAddedToWishList(wishListResult: WishListResult?, message: String) {
        // showToast(message)
        productDetailPresenter = ProductDetailPresenter(
            this@ProductDetailsActivity,
            this@ProductDetailsActivity
        )
        productDetailPresenter.getProductDetail(
            productID,
            ContextUtils.getAuthToken(this@ProductDetailsActivity)
        )
//        updateIconTint(true, binding().addToWishlistTextView, R.color.colorPrimary)
//        productsDataItems?.isAddedToWishList = true
//        isAddedToWishList = true
//        wishListId = wishListResult?.my_wish_list?.wishList_id!!
//        wishListVariationId = wishListResult.my_wish_list.variation_id!!
    }

    override fun onProductRemovedFromWishList(
        wishListRemovedResult: WishListRemovedResult?,
        message: String
    ) {
        showToast(message)
        productDetailPresenter = ProductDetailPresenter(
            this@ProductDetailsActivity,
            this@ProductDetailsActivity
        )
        productDetailPresenter.getProductDetail(
            productID,
            ContextUtils.getAuthToken(this@ProductDetailsActivity)
        )
//        updateIconTint(false, binding().addToWishlistTextView, R.color.colorHeartEmpty)
//        productsDataItems?.isAddedToWishList = false
//        isAddedToWishList = false
//        wishListId = 0
//        wishListVariationId = ""
//        variation?.wishList = null
//        wishListData = null
    }

    override fun productAddToRecent(message: String) {


        //Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }

    override fun onProductRecentNotAdded(message: String) {
    }

    override fun onBackPressed() {
        if (isAddedToWishList && type == "products") {
            val intent = Intent()
            Log.e("WishLisht_detail ::: ", "" + wishListData)
            intent.putExtra("wishListData", Parcels.wrap(wishListData))
            intent.putExtra("position", productPosition)
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun addOrUpdateCart(
        variationId: String,
        quantity: Int,
        type: String,
        isFirstTime: Boolean
    ) {
        try {
            ApiClientGenerator
                .getClient()!!
                .addOrUpdatecart(
                    ContextUtils.getAuthToken(applicationContext),
                    variationId,
                    quantity
                )
                .enqueue(object : Callback<ApiResponse<CartData>?> {
                    override fun onResponse(
                        call: Call<ApiResponse<CartData>?>,
                        response: Response<ApiResponse<CartData>?>
                    ) {
                        myDialog.dialogDismiss()
                        if (response.body()!!.status == 1) {
                            if (type == "bag") {
                                response.body()?.message?.let {
                                    if (isFirstTime) {
                                        showToast("Product has been added to bag")
                                    }
                                }
                                productDetailPresenter = ProductDetailPresenter(
                                    this@ProductDetailsActivity,
                                    this@ProductDetailsActivity
                                )
                                productDetailPresenter.getProductDetail(
                                    productID,
                                    ContextUtils.getAuthToken(this@ProductDetailsActivity)
                                )

                            } else {
                                val intent =
                                    Intent(applicationContext, CheckoutActivity::class.java)
                                intent.putExtra(Constants.type, status)
                                intent.putExtra("id", variationId)
                                startActivity(intent)
                            }

                        } else {
                            AppUtil.firebaseEvent(
                                applicationContext,
                                "error",
                                "error_events",
                                response.body()!!.message
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
                    myDialog.dialogDismiss()

                    if (response.body()?.status == 1) {
                        showToast(response.body()?.message!!)
                        status = "false"
                        quantityInCart = 0
                        quantityValue = 1
                        productDetailPresenter = ProductDetailPresenter(
                            this@ProductDetailsActivity,
                            this@ProductDetailsActivity
                        )
                        productDetailPresenter.getProductDetail(
                            productID,
                            ContextUtils.getAuthToken(this@ProductDetailsActivity)
                        )
                    } else {
                        AppUtil.firebaseEvent(
                            applicationContext,
                            "error",
                            "error_events",
                            response.body()!!.message
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                productDetailPresenter = ProductDetailPresenter(this, this)
                productDetailPresenter.getProductDetail(
                    productID,
                    ContextUtils.getAuthToken(this)
                )
            }
        }
    }

//    override fun onAdapterItemTapped(
//        adapterItem: String?,
//        categoryName: String?,
//        itemPosition: Int?
//    ) {
//
//        if (adapterItem != null) {
//            variationId = adapterItem
//            productDetailPresenter = ProductDetailPresenter(
//                this@ProductDetailsActivity,
//                this@ProductDetailsActivity
//            )
//            productDetailPresenter.getProductDetail(
//                productID,
//                ContextUtils.getAuthToken(this@ProductDetailsActivity)
//            )
//            updateRecent(variationId)
//
////            updateUI()
//
////            if (variationId == wishListVariationId) {
////                updateIconTint(true, binding().addToWishlistTextView, R.color.colorPrimary)
////            } else {
////                updateIconTint(false, binding().addToWishlistTextView, R.color.colorHeartEmpty)
////            }
//        }
//
//    }

    private fun updateIconTint(isTrue: Boolean, textView: TextView, iconTint: Int) {
        productsDataItems?.isAddedToWishList = isTrue
        for (drawable in textView.compoundDrawables) {
            drawable?.colorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(
                    textView.context,
                    iconTint
                ), PorterDuff.Mode.SRC_IN
            )
        }
    }

    override fun onAdapterItemTapped(
        adapterItem: String?,
        categoryName: String?,
        itemPosition: Int?,
        id: String?
    ) {
        if (adapterItem != null) {
            variationId = adapterItem
            productDetailPresenter = ProductDetailPresenter(
                this@ProductDetailsActivity,
                this@ProductDetailsActivity
            )
            productDetailPresenter.getProductDetail(
                productID,
                ContextUtils.getAuthToken(this@ProductDetailsActivity)
            )
            updateRecent(variationId)

//            updateUI()

//            if (variationId == wishListVariationId) {
//                updateIconTint(true, binding().addToWishlistTextView, R.color.colorPrimary)
//            } else {
//                updateIconTint(false, binding().addToWishlistTextView, R.color.colorHeartEmpty)
//            }
        }
    }

    override fun onWish(adapterItem: Int?, categoryName: String?) {
    }

    override fun onAddToBag(adapterItem: Int?, product_id: String?, text: String, title: String) {
    }

    override fun onItemClick(item: String?, code: String?) {

        if (item == "submit") {
            myDialog.dialogShow();
            checkPinCodeAvaliable(code!!);

        } else if (item == "close") {
            addressBottomSheet?.dismiss()
        }

    }

    private fun loginUser(binding: ActivityProductDetailsBinding, message: String) {


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
        dialog_msg.text = message

        btnok.setOnClickListener {
            dialog.dismiss()

            if (PreferenceManager().getInstance(this).getGoogleTOken().equals("")) {

            } else {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

                val googleSignInClient = GoogleSignIn.getClient(this@ProductDetailsActivity, gso)
                googleSignInClient.signOut()
                LoginManager.getInstance().logOut()

            }
            WelcomeActivity.start(this@ProductDetailsActivity)
            // val intent = Intent(applicationContext, WelcomeActivity::class.java)
            /*intent.putExtra(Constants.email, "")
            intent.putExtra(Constants.socialId, "")
            intent.putExtra(Constants.type, "")
            startActivity(intent)*/
            finishAffinity()
        }

        btncancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


    //-------------------------Add prodeuct in cart----------------
    private fun checkPinCodeAvaliable(code: String) {
        try {
            ApiClientGenerator
                .getClient()!!
                .checkPincode(
                    ContextUtils.getAuthToken(applicationContext),
                    code
                )
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        myDialog.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("response", response.toString())
                        if (response.getString("status") == "1") {
                            addressBottomSheet?.dismiss()
                            binding.pinCodeEditText.setText(code)
                            pinNotAvalible(binding, "Delivery is available at your area.")
                            // finish()
                        } else {
                            pinNotAvalible(
                                binding,
                                "Sorry, the product delivery is not available at your area."
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
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


    private fun pinNotAvalible(binding: ActivityProductDetailsBinding, message: String) {


        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.pin_code_not_availabe_view)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btnok = dialog.findViewById(R.id.okbtn) as TextView
        val dialog_msg = dialog.findViewById(R.id.dialog_msg) as TextView
        dialog_msg.text = message

        btnok.setOnClickListener {
            dialog.dismiss()

        }


        dialog.show()

    }

    //----------------Varient bottom sheet------------
    private fun openVarientSheet() {
        val myDrawerView = layoutInflater.inflate(R.layout.varient_bottom_view, null)
        bindingBottom =
            VarientBottomViewBinding.inflate(layoutInflater, myDrawerView as ViewGroup, false)

        bsd = BottomSheetDialog(this)
        bsd.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bsd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bsd.setContentView(bindingBottom.root)
        bsd.setCanceledOnTouchOutside(true)
        bsd.setCancelable(true)
        bsd.show()
        val df = DecimalFormat("#.##")
        df.setRoundingMode(RoundingMode.CEILING)
        if (variation!!.currentBatch != null) {
            if (variation?.offer_available!!) {
                bindingBottom.varientWitoutDisPrice.visibility = View.VISIBLE
                bindingBottom.discountTxt.visibility = View.VISIBLE

                bindingBottom.varientName.text = binding.productNameTextView.text.toString()
                bindingBottom.varientDisPrice.text =
                    binding.productDiscountedPriceTextView.text.toString();
                bindingBottom.varientWitoutDisPrice.paintFlags =
                    bindingBottom.varientWitoutDisPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                bindingBottom.varientWitoutDisPrice.text =
                    binding.productNetPriceTextView.text.toString();
                if (savePrecentage < 1) {
                    bindingBottom.discountTxt.text = savePrecentage.toString() + "% OFF"

                } else {
                    bindingBottom.discountTxt.text = Math.round(savePrecentage).toString() + "% OFF"

                }
            } else {
                if (variation!!.currentBatch?.product_mrp == variation!!.currentBatch?.base_rate!!) {
                    bindingBottom.varientWitoutDisPrice.visibility = View.INVISIBLE
                    bindingBottom.discountTxt.visibility = View.INVISIBLE
                    bindingBottom.varientDisPrice.text =
                        binding.productDiscountedPriceTextView.text.toString();

                } else {
                    bindingBottom.varientWitoutDisPrice.visibility = View.VISIBLE
                    bindingBottom.discountTxt.visibility = View.VISIBLE

                    bindingBottom.varientName.text = binding.productNameTextView.text.toString()
                    bindingBottom.varientDisPrice.text =
                        binding.productDiscountedPriceTextView.text.toString();
                    bindingBottom.varientWitoutDisPrice.paintFlags =
                        bindingBottom.varientWitoutDisPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    bindingBottom.varientWitoutDisPrice.text =
                        binding.productNetPriceTextView.text.toString()
                    if (savePrecentage < 1) {
                        bindingBottom.discountTxt.text = savePrecentage.toString() + "% OFF"

                    } else {
                        bindingBottom.discountTxt.text =
                            Math.round(savePrecentage).toString() + "% OFF"

                    }
                }
            }
        }
        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        bindingBottom.varientListview.layoutManager = subCategoriesLayoutManager

        for (i in 0 until variantModelList!!.size) {
            for (j in 0 until variantList!!.size) {
                Log.e(
                    "equal data",
                    variantModelList!![i].name + "  " + variantList!![j].variation_name
                )
                if (variantModelList!![i].name == variantList!![j].variation_name) {
                    variantModelList!![i].isSelectd = true;
                    variantModelList!![i].selectdText = variantList!![j].variation_value;


                }
            }
        }

        var bottomAdapter = BottomVariationsAdapter(this, variantModelList, this)
        bindingBottom.varientListview.adapter = bottomAdapter
        onclick(bindingBottom)
    }

    private fun onclick(binding: VarientBottomViewBinding) {
        binding.close.setOnClickListener {
            bsd.dismiss()
        }
        binding.cancel.setOnClickListener {
            bsd.dismiss()

        }
        binding.apply.setOnClickListener {
            if (varientDetailResult != null) {
                onProductDetailDataFound(varientDetailResult, "")
                bsd.dismiss()
            }

        }


    }

    override fun onAdapterItemTapped() {
        openVarientSheet()
    }

    override fun onClickSubVarientItem(parentPos: Int, text: String, isSelected: Boolean) {
        try {
            variantList!![parentPos].variation_value = text
            for (i in 0 until variantModelList!!.size) {
                for (j in 0 until variantList!!.size) {
                    if (variantModelList!![i].name == variantList!![j].variation_name) {
                        variantModelList!![i].isSelectd = true;
                        variantModelList!![i].selectdText = variantList!![j].variation_value;


                    }
                }
            }

            var bottomAdapter = BottomVariationsAdapter(this, variantModelList, this)
            bindingBottom.varientListview.adapter = bottomAdapter

            var obj: JSONObject = JSONObject()
            obj.put("product_id", vId)
            val jsonArray = JSONArray()
            for (i in 0 until variantList!!.size) {
                var objArr: JSONObject = JSONObject()
                objArr.put("variation_value", variantList!![i].variation_value)
                objArr.put("variation_name", variantList!![i].variation_name)

                jsonArray.put(objArr)
            }

            obj.put("variant", jsonArray)
            myDialog.dialogShow()

            callApiDetails(obj)
        } catch (e: java.lang.Exception) {

        }

    }


    fun callApiDetails(obj: JSONObject) {
        val parser = JsonParser();

        val json = parser!!.parse(obj.toString()) as JsonObject;
        Log.e("variant print", json.toString());
        ApiClientGenerator
            .getClient()!!
            .getVariants(json, ContextUtils.getAuthToken(applicationContext))
            .enqueue(object : Callback<ApiResponse<ProductDetailResult>?> {

                override fun onResponse(
                    call: Call<ApiResponse<ProductDetailResult>?>,
                    response: Response<ApiResponse<ProductDetailResult>?>
                ) {
//                    dismissDialog()
                    myDialog.dialogDismiss()
                    if (response.code() < 200 || response.code() >= 300) {
                        return
                    }
                    if (response.body() != null) {
                        if (response.body()!!.status == 1) {
                            varientDetailResult = null
                            varientDetailResult = response.body()!!.data
                            if (response.body()!!.data!!.default_variant!!.currentBatch == null) {


                                bindingBottom.errorMessage.text = "Out of stock"
                            } else {
                                val df = DecimalFormat("#.##")
                                df.setRoundingMode(RoundingMode.CEILING)
                                if (response.body()!!.data!!.default_variant!!.currentBatch != null) {
                                    if (response.body()!!.data!!.default_variant!!.offer_available!!) {
                                        bindingBottom.varientWitoutDisPrice.visibility =
                                            View.VISIBLE
                                        bindingBottom.discountTxt.visibility = View.VISIBLE
                                        var p =
                                            (response.body()!!.data!!.default_variant!!.save_amount!!.toDouble() /
                                                    response.body()!!.data!!.default_variant!!.currentBatch!!.product_mrp!!.toDouble()) * 100
                                        bindingBottom.varientName.text =
                                            response.body()!!.data!!.default_variant!!.product_title
                                        bindingBottom.varientDisPrice.text =
                                            resources.getText(R.string.us_dollar)
                                                .toString() + Math.round(response.body()!!.data!!.default_variant!!.offer_discount_price!!)
                                                .toString()
                                        bindingBottom.varientWitoutDisPrice.paintFlags =
                                            bindingBottom.varientWitoutDisPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                        bindingBottom.varientWitoutDisPrice.text =
                                            resources.getText(R.string.us_dollar)
                                                .toString() + Math.round(response.body()!!.data!!.default_variant!!.currentBatch!!.product_mrp!!)
                                                .toString()
                                        savePrecentage =
                                            AppUtil.roundTwoDecimalPlaces(
                                                this@ProductDetailsActivity,
                                                p
                                            )

                                        if (savePrecentage < 1) {
                                            bindingBottom.discountTxt.text =
                                                savePrecentage.toString() + "% OFF"
                                        } else {
                                            bindingBottom.discountTxt.text =
                                                Math.round(savePrecentage).toString() + "% OFF"
                                        }


                                    } else {
                                        if (response.body()!!.data!!.default_variant!!.currentBatch!!.product_mrp == response.body()!!.data!!.default_variant!!.currentBatch?.base_rate!!) {
                                            bindingBottom.varientWitoutDisPrice.visibility =
                                                View.INVISIBLE
                                            bindingBottom.discountTxt.visibility = View.INVISIBLE
                                            bindingBottom.varientDisPrice.text =
                                                resources.getText(R.string.us_dollar)
                                                    .toString() + Math.round(response.body()!!.data!!.default_variant!!.currentBatch!!.base_rate!!)
                                                    .toString()
                                        } else {
                                            bindingBottom.varientWitoutDisPrice.visibility =
                                                View.VISIBLE
                                            bindingBottom.discountTxt.visibility = View.VISIBLE
                                            var p =
                                                (response.body()!!.data!!.default_variant!!.save_amount!!.toDouble() /
                                                        response.body()!!.data!!.default_variant!!.currentBatch!!.product_mrp!!.toDouble()) * 100
                                            bindingBottom.varientName.text =
                                                response.body()!!.data!!.default_variant!!.product_title
                                            bindingBottom.varientDisPrice.text =
                                                resources.getText(R.string.us_dollar)
                                                    .toString() + Math.round(response.body()!!.data!!.default_variant!!.offer_discount_price!!)
                                                    .toString()

                                            bindingBottom.varientWitoutDisPrice.paintFlags =
                                                bindingBottom.varientWitoutDisPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                            bindingBottom.varientWitoutDisPrice.text =
                                                resources.getText(R.string.us_dollar)
                                                    .toString() + Math.round(response.body()!!.data!!.default_variant!!.currentBatch!!.product_mrp!!)
                                                    .toString()

                                            savePrecentage =
                                                AppUtil.roundTwoDecimalPlaces(
                                                    this@ProductDetailsActivity,
                                                    p
                                                )

                                            if (savePrecentage < 1) {
                                                bindingBottom.discountTxt.text =
                                                    savePrecentage.toString() + "% OFF"
                                            } else {
                                                bindingBottom.discountTxt.text =
                                                    Math.round(savePrecentage).toString() + "% OFF"
                                            }

                                        }
                                    }
                                }
                                bindingBottom.errorMessage.text = ""

                            }

                        } else {
                            errorMessage = response.body()!!.message
                            bindingBottom.errorMessage.text = errorMessage
                        }
                    } else {
                        errorMessage = response.body()!!.message
                        bindingBottom.errorMessage.text = errorMessage


                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<ProductDetailResult>?>,
                    t: Throwable
                ) {
                    myDialog.dialogDismiss()
//                    dismissDialog()
                }
            })
    }

    override fun onOfferItemClick(pos: Int) {
        val i = Intent(applicationContext, TermsAndCondtions::class.java)
        i.putExtra("type", variation?.offers!![pos].name)
        i.putExtra("data", variation?.offers!![pos].terms)

        startActivity(i)
    }

    fun genratelinkType(
        productName: String,
        productDescription: String?,

        profileImage: String?,
        productId: String?,
        variationsId: String?
    ) {
        val buo = BranchUniversalObject()
            .setTitle("$productName")
            .setContentImageUrl("$profileImage")
            .setContentDescription("$productDescription") /* .setContentImageUrl(Uri.)*/
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
        val lp = LinkProperties()
            .setFeature("sharing")
            .addControlParameter("desktop_url", "https://worksdelight.com")
            .addControlParameter("productId", productId)
            .addControlParameter("variationId", variationsId)

            .addControlParameter(
                "custom_random",
                java.lang.Long.toString(System.currentTimeMillis())
            )
        buo.generateShortUrl(
            this, lp
        ) { url, error ->
            if (error == null) {
                AppUtil.firebaseEvent(applicationContext, "name", "share", productName)

                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "I loved these gadgets collections on Gadgetmart and hope you too will \uD83D\uDE0A Check it out!"
                )
                i.putExtra(Intent.EXTRA_TEXT, url)
                startActivity(Intent.createChooser(i, "Share URL"))
                Log.i("BRANCH SDK", "got my Branch link to share: $url")
            }
        }
    }

}
