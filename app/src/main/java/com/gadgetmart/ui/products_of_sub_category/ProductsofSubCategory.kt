package com.gadgetmart.ui.products_of_sub_category

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.ActivityProductsofSubCategoryBinding
import com.gadgetmart.databinding.FilterByBottomsheetBinding
import com.gadgetmart.databinding.SortByBottomsheetBinding
import com.gadgetmart.ui.cart_bag.CartActivity
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.product_details.ProductDetailsActivity
import com.gadgetmart.ui.product_details.productmodel.CartData
import com.gadgetmart.ui.search.SearchActivity
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.ui.wishlist.WishListRemovedResult
import com.gadgetmart.ui.wishlist.WishListResult
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.parceler.Parcels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductsofSubCategory : BaseActivity<ActivityProductsofSubCategoryBinding>(),
    ProductsAdapterListener, ProductsOfSubContract ,FilterInterface{
    private var binding: ActivityProductsofSubCategoryBinding? = null
    private var bindingFilter: FilterByBottomsheetBinding? = null
    private var productsofSubCategoryAdapter: ProductsofSubCategoryAdapter? = null
    private var productsDataItems: ArrayList<ProductVariation>? = null
    private var productsDataItemsAll: ArrayList<ProductVariation>? = null
    private var productsFilter: ArrayList<ProductsFilterOptions>? = null
    private var filterArray: ArrayList<FiltersArray>? = null
    private var productsSubFilter: ArrayList<String>? = null
    private lateinit var bsd: BottomSheetDialog
    private lateinit var bsd_filter: Dialog
    private var categoryId = ""
    private var categoryName = ""
    private var Position: Int = 0
    private var currentPage: Int = 1
    private var productsOfSubPresenter: ProductsofSubPresenter? = null
    private val WishListResultIntent = 101
    private var filterAdapter: FilterAdapter? = null
    private var subFilterAdapter: SubFilterAdapter? = null
    private var isScrolling = true
    private var isRefreshing = true
    lateinit var myDialog: CustomProgressDialog
    private var filterSelectedArr = JSONArray()
    private var minValue: Int = 0
    private var maxValue: Int = 0
    private var cartCount: Int = 0

    lateinit var global: Global



    private var sort_type = ""

    override fun getContentView(): Int {
        return R.layout.activity_productsof_sub_category
    }

    override fun init(binding: ActivityProductsofSubCategoryBinding) {
        this.binding = binding
        getIntentData()
        filterArray=ArrayList()
        global=applicationContext as Global
        myDialog = CustomProgressDialog(this)

        productsDataItems = ArrayList<ProductVariation>()
        productsDataItemsAll = ArrayList<ProductVariation>()
        binding.toolbar.toolbar_title_text_view.text = categoryName
        setListeners(binding)

        initPresenter()
        initRecyclerViewAndAdapter(binding)

    }

    private fun getIntentData() {
        if (intent.extras != null) {
            categoryId = intent.extras!!.getString("categoryId").toString()
            categoryName = intent.extras!!.getString("categoryName").toString()
        }
    }

    private fun setListeners(binding: ActivityProductsofSubCategoryBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
        binding.sortbtTextView.setOnClickListener { openSortBySheet() }
        binding.filterbyTextView.setOnClickListener { openFilterBySheet() }
        toolbar_cart_icon.setOnClickListener {
            val intent = Intent(applicationContext, CartActivity::class.java)

            startActivity(intent)
        }
        toolbar_ohnik_image_view.setOnClickListener {
            val intent = Intent(applicationContext, SearchActivity::class.java)

            startActivity(intent)
        }
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nes_scrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (nes_scrollView.getChildAt(nes_scrollView.childCount - 1) != null) {
                if (scrollY >= nes_scrollView.getChildAt(nes_scrollView.childCount - 1).measuredHeight - nes_scrollView.measuredHeight &&
                    scrollY > oldScrollY
                ) {

                    if (isScrolling) {
                        isScrolling = false
                        isRefreshing = false
                        binding.pbar.visibility = View.VISIBLE
                        val handler = Handler()
                        handler.postDelayed(Runnable {
                            currentPage += 1
                            when (categoryName) {
                                "Popular Gadgets" -> {
                                    productsOfSubPresenter?.getPopularProductsData(
                                        currentPage,
                                        ContextUtils.getAuthToken(
                                            this
                                        ), false
                                    )
                                }
                                "Offers" -> {
                                    productsOfSubPresenter?.getOfferProductsdata(
                                        currentPage,
                                        ContextUtils.getAuthToken(this),
                                        false
                                    )
                                }
                                else -> {
                                    productsOfSubPresenter?.getSubcategoriesProducts(
                                        categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                                         false
                                    )
                                }
                            }
                        }, 1200)
                    }
                }
            }
        })


        binding.swipeToRefresh.setOnRefreshListener {
            if (isRefreshing) {
                binding.swipeToRefresh.isRefreshing = true
                currentPage = 1
                productsDataItems?.clear()
                productsFilter?.clear()
                productsSubFilter?.clear()
                productsDataItemsAll?.clear()
                binding.homeLayout.visibility = View.GONE
                when (categoryName) {
                    "Popular Gadgets" -> {
                        myDialog.dialogShow()
                        productsOfSubPresenter?.getPopularProductsData(
                            currentPage,
                            ContextUtils.getAuthToken(
                                this
                            ), false
                        )
                    }
                    "Offers" -> {
                        myDialog.dialogShow()

                        productsOfSubPresenter?.getOfferProductsdata(
                            currentPage,
                            ContextUtils.getAuthToken(this),
                            false
                        )
                    }
                    else -> {
                        myDialog.dialogShow()
                        productsDataItemsAll!!.clear()

                        productsOfSubPresenter?.getSubcategoriesProducts(
                            categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                            false
                        )
                    }
                }
            }
        }

        binding.swipeToRefresh.setColorSchemeColors(
            Color.rgb(255, 93, 94),
            Color.GREEN,
            Color.BLUE
        )
    }

    private fun initPresenter() {
        getFilter()
        productsOfSubPresenter = ProductsofSubPresenter(this, this)
        when (categoryName) {
            "Popular Gadgets" -> {
                myDialog.dialogShow()

                productsOfSubPresenter?.getPopularProductsData(
                    currentPage,
                    ContextUtils.getAuthToken(this),
                    true
                )
            }
            "Offers" -> {
                myDialog.dialogShow()

                productsOfSubPresenter?.getOfferProductsdata(
                    currentPage,
                    ContextUtils.getAuthToken(this),
                    true
                )
            }
            else -> {
                myDialog.dialogShow()
                productsDataItemsAll!!.clear()

                productsOfSubPresenter?.getSubcategoriesProducts(
                    categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                    false
                )
            }
        }
    }

    private fun initRecyclerViewAndAdapter(binding: ActivityProductsofSubCategoryBinding) {
        // Categories
        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.subcategoryProductsRecyclerview.layoutManager = subCategoriesLayoutManager
        binding.subcategoryProductsRecyclerview.isNestedScrollingEnabled = false

        productsofSubCategoryAdapter =
            ProductsofSubCategoryAdapter(this, productsDataItemsAll, this)
        binding.subcategoryProductsRecyclerview.adapter = productsofSubCategoryAdapter
    }

    private fun openSortBySheet() {
        val myDrawerView = layoutInflater.inflate(R.layout.sort_by_bottomsheet, null)
        val binding =
            SortByBottomsheetBinding.inflate(layoutInflater, myDrawerView as ViewGroup, false)

        bsd = BottomSheetDialog(this)
        bsd.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bsd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bsd.setContentView(binding.root)
        bsd.setCanceledOnTouchOutside(true)
        bsd.setCancelable(true)
        bsd.show()
        if(sort_type=="popular"){
            displayTick(
                binding.popularsImageView,
                binding.newArrivalImageView,
                binding.highPriceImageView,
                binding.lowPriceImageView
            )

        }else if(sort_type=="latest"){
            displayTick(
                binding.newArrivalImageView,
                binding.popularsImageView,
                binding.highPriceImageView,
                binding.lowPriceImageView
            )
        }
        else if(sort_type=="price_low"){
            displayTick(
                binding.lowPriceImageView,
                binding.newArrivalImageView,
                binding.highPriceImageView,
                binding.popularsImageView
            )
        }else if(sort_type=="price_high"){
            displayTick(
                binding.highPriceImageView,
                binding.newArrivalImageView,
                binding.popularsImageView,
                binding.lowPriceImageView
            )
        }
        onclick(binding)
    }

    private fun openFilterBySheet() {

        bsd_filter = Dialog(this, R.style.AppTheme_NoActionBar_FullScreenDialog)
        val binding: FilterByBottomsheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.filter_by_bottomsheet,
            null,
            false
        )
        bsd_filter.setContentView(binding.root)
        this.bindingFilter = binding
        bsd_filter.show()

        onFilterclick(binding)
    }

    private fun onclick(binding: SortByBottomsheetBinding) {

        binding.sortbyPopulars.setOnClickListener {
            displayTick(
                binding.popularsImageView,
                binding.newArrivalImageView,
                binding.highPriceImageView,
                binding.lowPriceImageView
            )
            sort_type="popular";
            myDialog.dialogShow()
            productsDataItemsAll!!.clear()

            productsOfSubPresenter?.getSubcategoriesProducts(
                categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                false
            )
            bsd.dismiss()

        }
        binding.sortbyArrival.setOnClickListener {
            displayTick(
                binding.newArrivalImageView,
                binding.popularsImageView,
                binding.highPriceImageView,
                binding.lowPriceImageView
            )
            sort_type="latest";
            myDialog.dialogShow()
            productsDataItemsAll!!.clear()

            productsOfSubPresenter?.getSubcategoriesProducts(
                categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                false
            )
            bsd.dismiss()

        }
        binding.sortbyHighToLow.setOnClickListener {
            displayTick(
                binding.highPriceImageView,
                binding.newArrivalImageView,
                binding.popularsImageView,
                binding.lowPriceImageView
            )
            sort_type="price_high";
            myDialog.dialogShow()
            productsDataItemsAll!!.clear()

            productsOfSubPresenter?.getSubcategoriesProducts(
                categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                false
            )
            bsd.dismiss()

        }
        binding.sortbyLowToHigh.setOnClickListener {
            displayTick(
                binding.lowPriceImageView,
                binding.newArrivalImageView,
                binding.highPriceImageView,
                binding.popularsImageView
            )
            sort_type="price_low";
            myDialog.dialogShow()

            productsDataItemsAll!!.clear()

            productsOfSubPresenter?.getSubcategoriesProducts(
                categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                false
            )
            bsd.dismiss()

        }
    }

    private fun displayTick(
        viewShow: ImageView,
        viewHide1: ImageView,
        viewHide2: ImageView,
        viewHide3: ImageView
    ) {
        viewShow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.tick))
        viewHide1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.round_grey_bg))
        viewHide2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.round_grey_bg))
        viewHide3.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.round_grey_bg))
    }

    private fun onFilterclick(binding: FilterByBottomsheetBinding) {
        binding.toolbarClearAllTextView.setOnClickListener {
            for(i in 0 until filterArray!!.size){
                filterArray!![i].isSelected=false;
                for(j in 0 until filterArray!![i].filter_options.size){
                    filterArray!![i].filter_options[j].isSelected=false;
                }

            }
            filterSelectedArr=JSONArray()
            myDialog.dialogShow()
            productsDataItemsAll!!.clear()

            productsOfSubPresenter?.getSubcategoriesProducts(
                categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                false
            )
            Log.e("selected arr",filterSelectedArr.toString())
            bsd_filter.dismiss()

        }
        binding.applyBtn.setOnClickListener {
            minValue=bindingFilter?.priceRangeSlider!!.selectedMinValue.toInt()
            maxValue=bindingFilter?.priceRangeSlider!!.selectedMaxValue.toInt()
            filterSelectedArr= JSONArray()
            for(i in 0 until filterArray!!.size){
                if(filterArray!![i].isSelected==true) {
                    var obj = JSONObject()
                    var optionArr = JSONArray()

                    if (filterArray!![i].name.equals("Price")) {
                        obj.put("filter_name", filterArray!![i].name)
                        optionArr.put(minValue)
                        optionArr.put(maxValue)

                        obj.put("filter_options", optionArr)
                        filterSelectedArr.put(obj)
                    } else {
                        obj.put("filter_name", filterArray!![i].name)

                        for (j in 0 until filterArray!![i].filter_options.size) {

                            if (filterArray!![i].filter_options[j].isSelected == true) {
                                optionArr.put(filterArray!![i].filter_options[j].name)
                                obj.put("filter_options", optionArr)
                            }

                        }
                        filterSelectedArr.put(obj)

                    }
                }


            }
            myDialog.dialogShow()
            productsDataItemsAll!!.clear()
            productsOfSubPresenter?.getSubcategoriesProducts(
                categoryId, currentPage,ContextUtils.getAuthToken(this),sort_type,filterSelectedArr,
                false
            )
            Log.e("selected arr",filterSelectedArr.toString())
            bsd_filter.dismiss()

        }

        binding.backIcon.setOnClickListener { bsd_filter.dismiss() }
        val filterLLM = LinearLayoutManager(this)

        filterLLM.orientation = LinearLayoutManager.VERTICAL

        binding.filterRecyclerView.layoutManager = filterLLM

        filterAdapter = FilterAdapter(this, filterArray, this)
        binding.filterRecyclerView.adapter = filterAdapter

        val subFilterLLM = LinearLayoutManager(this)

        subFilterLLM.orientation = LinearLayoutManager.VERTICAL

        bindingFilter?.subFilterRecyclerView?.layoutManager = subFilterLLM

        subFilterAdapter = SubFilterAdapter(this, filterArray!![0].filter_options, this,0)
        bindingFilter?.subFilterRecyclerView?.adapter = subFilterAdapter
    }

    companion object {
        fun start(context: Context?, categoryId: String, categoryName: String?) {
            val intent = Intent(context, ProductsofSubCategory::class.java).putExtra(
                "categoryId",
                categoryId
            ).putExtra("categoryName", categoryName)
            context!!.startActivity(intent)
        }

    }


    override fun onProductsOfSubDataFound(
        productsOfSubResult: ProductsOfSubResult?,
        message: String,
        apiFlag: Int?
    ) {
        myDialog.dialogDismiss()
        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE

        if (productsOfSubResult?.cartCount != null && productsOfSubResult.cartCount != "0") {
            cartCount=productsOfSubResult.cartCount.toInt()
            binding().toolbarID.toolbarCartItemCount.text =
                productsOfSubResult.cartCount
            binding().toolbarID.toolbarCartItemCount.visibility = View.VISIBLE
        } else {
            binding().toolbarID.toolbarCartItemCount.visibility = View.GONE
        }

        if (productsOfSubResult?.products?.data == null || productsOfSubResult.products.data.size == 0) {
//            if (subCategoryResult?.categories!![0].subCategories == null)
            binding!!.mainLayout.visibility = View.GONE
            binding!!.noDataFoundLayout.visibility = View.VISIBLE
            return
        } else {
            binding?.pbar?.visibility = View.GONE
            if (productsOfSubResult.products.next_page_url != null) {
                isScrolling = true
                isRefreshing = true
            } else {
                isScrolling = false
                isRefreshing = true
            }
            binding!!.mainLayout.visibility = View.VISIBLE
            binding!!.noDataFoundLayout.visibility = View.GONE
            /*  if (categoryName != "PopularProducts" && categoryName != "OfferProducts")
              {
                  productsFilter = productsOfSubResult.filterOptions
                  filterAdapter?.updateList(productsFilter)
                  for(i in 0 until productsFilter?.size!!-1){
                      productsSubFilter = productsFilter!![i].filter_options
                  }
                  subFilterAdapter?.updateList(productsSubFilter)
              }*/
            productsDataItems = productsOfSubResult.products.data
            productsDataItemsAll?.addAll(productsDataItems!!)

            productsofSubCategoryAdapter!!.updateList(productsDataItemsAll)

        }
    }

    override fun onProductAddedToWishList(wishListResult: WishListResult?, message: String) {
        if(PreferenceManager().getInstance(this).getAuthToken().equals("")){
            loginUser(binding!!,"To add this product to wishlist you need to\nlogin first")
        }else {
            Log.e("WishList ::: ", "" + message)
            val productDataWishlist = wishListResult?.my_wish_list

            productsDataItemsAll!![Position]?.wishList = productDataWishlist
            productsofSubCategoryAdapter?.notifyDataSetChanged()
        }

    }

    override fun onProductRemovedFromWishList(
        wishListRemovedResult: WishListRemovedResult?,
        message: String
    ) {
        Log.e("WishList ::: ", "" + message)
//        val productDataWishlist = wishListRemovedResult?.my_wish_list
//
        productsDataItemsAll!![Position]?.wishList = null
        productsofSubCategoryAdapter?.notifyDataSetChanged()
    }

    override fun onProductsOfSubDataNotFound(message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE
        Log.e("Products Error ::: ", "" + message)
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message)

    }

    override fun onAdapterItemTapped(adapterItem: String?, categoryName: String?, itemPosition: Int?,variationid:String?) {

        Position = itemPosition!!
        when (categoryName) {
            "addedToWishList" -> {
                for(i in 0 until productsDataItemsAll!!.size){
                    if(productsDataItemsAll!![i].variationId==variationid){
                        AppUtil.firebaseEvent(applicationContext,"name","wishlist",productsDataItemsAll!![i].product_title!!)
                        break
                    }
                }
                productsOfSubPresenter?.addToWishList(
                    variationid!!,
                    ContextUtils.getAuthToken(this)
                )
            }
            "removedFromWishList" -> {
                productsOfSubPresenter?.removeFromWishList(
                    adapterItem!!,
                    ContextUtils.getAuthToken(this)
                )
            }
            "FilterOptions" -> {
                setSubFilter(itemPosition)
            }
            else -> {
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("productId", adapterItem.toString())
                intent.putExtra("category_name", this.categoryName)
                intent.putExtra("position", Position)
                intent.putExtra("type", "products")
                intent.putExtra("id", variationid)

                startActivityForResult(intent, WishListResultIntent)
            }

        }
    }

    override fun onWish(adapterItem: Int?, categoryName: String?) {

    }

    override fun onAddToBag(adapterItem: Int?, product_id: String?,text:String,title:String) {
        if(PreferenceManager().getInstance(this).getAuthToken().equals("")){
            loginUser(binding!!,"To add this product to bag you need to\nlogin first")
        }else {
            if(text.equals("Go to bag ")){
                val intent = Intent(applicationContext, CartActivity::class.java)

                startActivity(intent)
            }else {
                AppUtil.firebaseEvent(applicationContext,"name","product_added_to_cart ",title)

                myDialog.dialogShow()
                addOrUpdatecart(product_id!!, 1)
            }
        }
    }

    private fun setSubFilter(filterPosition: Int) {
        filterAdapter = FilterAdapter(this, filterArray, this)
        bindingFilter?.filterRecyclerView?.adapter = filterAdapter


        if (filterPosition >= 0) {
            productsSubFilter = productsFilter!![filterPosition].filter_options
            bindingFilter?.subFilterRecyclerView?.visibility = View.VISIBLE
            subFilterAdapter = SubFilterAdapter(this, filterArray!![0].filter_options, this,0)
            bindingFilter?.subFilterRecyclerView?.adapter = subFilterAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WishListResultIntent && resultCode == Activity.RESULT_OK) {
            if (data?.extras != null) {
                val pos = data.extras?.getInt("position")!!
                Log.e("WishLisht :::::: ", "" + data.extras?.getParcelable("wishListData"))
                if (Parcels.unwrap(data.extras?.getParcelable("wishListData")) as ProductDataWishlist == null) {
                    productsDataItemsAll!![pos]?.wishList = null
                } else {
                    productsDataItemsAll!![pos]?.wishList =
                        Parcels.unwrap(data.extras?.getParcelable("wishListData"))
                }

               // productsofSubCategoryAdapter?.notifyDataSetChanged()
            }
        }
    }

    //-------------------------Add prodeuct in cart----------------
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
                        myDialog.dialogDismiss()
                        if (response.body()!!.status == 1) {
                            showToast("Product has been added to bag")
                                cartCount=cartCount+1
                                binding().toolbarID.toolbarCartItemCount.text =cartCount.toString()
                                binding().toolbarID.toolbarCartItemCount.visibility = View.VISIBLE
                                global.notificationSchdule(applicationContext, PreferenceManager().getInstance(applicationContext).getUserName()!!)

                             
                            //initPresenter()
                        } else {
                            AppUtil.firebaseEvent(applicationContext,"error","error_events",response.body()!!.message)

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

    private fun loginUser(binding: ActivityProductsofSubCategoryBinding, message:String) {


        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.login_message_dialog)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        }
        val btnok = dialog.findViewById(R.id.okbtn) as TextView
        val btncancel = dialog.findViewById(R.id.cancelbtn) as TextView
        val dialog_msg=dialog.findViewById(R.id.dialog_msg) as TextView
        dialog_msg.text=message

        btnok.setOnClickListener {
            dialog.dismiss()

            if (PreferenceManager().getInstance(this).getGoogleTOken().equals("")) {

            } else {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

                val googleSignInClient = GoogleSignIn.getClient(this@ProductsofSubCategory, gso)
                googleSignInClient.signOut()
                LoginManager.getInstance().logOut()

            }
            WelcomeActivity.start(this@ProductsofSubCategory)
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


    //-------------------------filter list----------------
    private fun getFilter() {
        try {

            ApiClientGenerator
                .getClient()!!
                .getFilters(
                    categoryId,
                    ContextUtils.getAuthToken(this)
                )
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("filter response",response.toString())
                        if (response.getString("status") == "1") {

var data=response.getJSONObject("data")
                            var arr=data.getJSONArray("filters")
                            for(i in 0 until arr.length()){
                                var arrObj=arr.getJSONObject(i)
                                var optionArr=arrObj.getJSONArray("filter_options")
                                var filterString:ArrayList<FilterOptionsArray>?= ArrayList()
                                for(j in 0 until optionArr.length()){
                                    filterString!!.add(FilterOptionsArray(optionArr.getString(j),false))
                                }

                                var map=FiltersArray(arrObj.getString("name"),filterString!!,false)
                                filterArray!!.add(map)
                            }

                        } else {
                            AppUtil.firebaseEvent(applicationContext,"error","error_events",response.getString("message"))

                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClickParent(pos: Int) {
        if(filterArray!![pos].name.equals("Price")){
            filterArray!![pos].isSelected=true
            bindingFilter?.priceRangeSlider!!.visibility=View.VISIBLE
            bindingFilter?.subFilterRecyclerView!!.visibility=View.GONE
            bindingFilter?.rlValueText!!.visibility = View.VISIBLE
            bindingFilter?.tvMinValue!!.text =resources.getString(R.string.us_dollar)+" "+ filterArray!![pos].filter_options!![0].name.toString()
            bindingFilter?.tvMaxValue!!.text = resources.getString(R.string.us_dollar)+" "+filterArray!![pos].filter_options!![1].name.toString()
            if(filterArray!![pos].filter_options!![0].name!="")
            {

                bindingFilter?.priceRangeSlider!!.setRangeValues(filterArray!![pos].filter_options!![0].name.toString().toInt(),filterArray!![pos].filter_options[1].name.toString().toInt())
            }
            bindingFilter?.priceRangeSlider!!.setOnRangeSeekBarChangeListener { bar, minValue, maxValue ->
                val minValueNum = minValue as Number
                val maxValueNum = maxValue as Number

                bindingFilter?.tvMinValue!!.text =resources.getString(R.string.us_dollar)+" "+(minValueNum.toString())
                bindingFilter?.tvMaxValue!!.text =resources.getString(R.string.us_dollar)+" "+maxValueNum.toString()

            }

        }else {

            bindingFilter?.priceRangeSlider!!.visibility=View.GONE
            bindingFilter?.subFilterRecyclerView!!.visibility=View.VISIBLE
            bindingFilter?.rlValueText!!.visibility = View.GONE
            subFilterAdapter = SubFilterAdapter(this, filterArray!![pos].filter_options, this, pos)
            bindingFilter?.subFilterRecyclerView?.adapter = subFilterAdapter
        }
    }

    override fun onClickChild(parentPos:Int,pos: Int,isSelected:Boolean) {

        filterArray!![parentPos].isSelected=isSelected
        filterArray!![parentPos].filter_options!![pos].isSelected=isSelected;



    }


}