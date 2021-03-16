package com.gadgetmart.ui.products

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.facebook.login.LoginManager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.ActivityProductsofSubCategoryBinding
import com.gadgetmart.databinding.ProductListActivityBinding
import com.gadgetmart.ui.cart_bag.CartActivity
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.order.model.DataModelArray
import com.gadgetmart.ui.product_details.ProductDetailsActivity
import com.gadgetmart.ui.products.popular.PopulatorAdapter
import com.gadgetmart.ui.search.SearchActivity
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.toolbar_cart_item_count
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.toolbar_ohnik_image_view
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import kotlinx.android.synthetic.main.layout_toolbar_home.*

class PopularActivity : BaseActivity<ProductListActivityBinding>(), DashboardAdapterListener,
    ProductsContract, AddToWishListContract, RemoveFromWishListContract {
    private var binding: ProductListActivityBinding? = null
    private var productsPresenter: ProductsPresenter? = null
    private var addToWishListPresenter: AddToWishListPresenter? = null
    private var removeFromWishListPresenter: RemoveFromWishListPresenter? = null
    private var adapter: PopulatorAdapter? = null

    private var isScrolling = true
    private var isRefreshing = true
    private var currentPage: Int = 1
    private var sectionId: String? = null
    private var sectionName: String? = null
    private var isBanner: Boolean = false

    lateinit var productsDataItems: ArrayList<ProductVariation>
    lateinit var productsDataItemsAll: ArrayList<ProductVariation>
    lateinit var global: Global

    companion object {
        fun start(context: Context, sectionId: String, sectionName: String, isBanner: Boolean) {
            context.startActivity(
                Intent(context, PopularActivity::class.java)
                    .putExtra("sectionId", sectionId)
                    .putExtra("sectionName", sectionName)
                    .putExtra("sectionType", isBanner)
            )
        }
    }

    override fun getContentView(): Int = R.layout.product_list_activity

    override fun init(binding: ProductListActivityBinding) {
        this.binding = binding
        global = applicationContext as Global
        productsDataItems = ArrayList()
        productsDataItemsAll = ArrayList()
        if (intent != null) {
            sectionId = intent.getStringExtra("sectionId")
            sectionName = intent.getStringExtra("sectionName")
            isBanner = intent.getBooleanExtra("sectionType", false)
        }
        binding.toolbar.toolbar_title_text_view.text = sectionName ?: "Products"
       // toolbar_ohnik_image_view.visibility = View.GONE
        setListeners(binding)

        val subCategoriesLayoutManager = GridLayoutManager(this, 2)
        subCategoriesLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = 1
        }

        binding.productsRecyclerView.layoutManager = subCategoriesLayoutManager

        binding.productsRecyclerView.isNestedScrollingEnabled = false
        adapter = PopulatorAdapter(applicationContext, ArrayList(), this)
        binding.productsRecyclerView.adapter = adapter
        initPresenter()
//        addAdapter(productsDataItemsAll)

    }

    private fun setListeners(binding: ProductListActivityBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
        toolbar_ohnik_image_view.setOnClickListener {
            val intent = Intent(applicationContext, SearchActivity::class.java)

            startActivity(intent)
        }
        binding.toolbar.toolbar_cart_icon.setOnClickListener {
            val intent = Intent(applicationContext, CartActivity::class.java)

            startActivity(intent)
        }
//        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nes_scrollView, _, scrollY, _, oldScrollY ->
//            if (nes_scrollView.getChildAt(nes_scrollView.childCount - 1) != null) {
//                if (scrollY >= nes_scrollView.getChildAt(nes_scrollView.childCount - 1).measuredHeight - nes_scrollView.measuredHeight
//                    && scrollY > oldScrollY
//                ) {
//
//                    if (isScrolling) {
//                        isScrolling = false
//                        isRefreshing = false
//                        binding.pbar.visibility = View.VISIBLE
//                        if (isBanner) fetchBannerProducts()
//                        else fetchSectionProducts()
//                    }
//                }
//            }
//        })

//        binding.swipeToRefresh.visibility = View.GONE
        binding.swipeToRefresh.setOnRefreshListener {
            if(productsDataItemsAll.size>0){
                productsDataItemsAll.clear()
                productsDataItems.clear()
            }
//            if (isRefreshing) {
            binding.swipeToRefresh.isRefreshing = true
//                currentPage = 1
            fetchSectionProducts()
//
        }

        binding.swipeToRefresh.setColorSchemeColors(
            Color.rgb(255, 93, 94),
            Color.GREEN,
            Color.BLUE
        )
    }

    private fun fetchBannerProducts() {
        sectionId?.let {
            productsPresenter?.getBannerProducts(
                currentPage,
                ContextUtils.getAuthToken(this),
                it,
                true
            )
        }
    }

    private fun fetchSectionProducts() {
        sectionId?.let {
            productsPresenter?.getPopularSectionProducts(
                currentPage,
                ContextUtils.getAuthToken(this),
                it,
                true
            )
        }
    }

    override fun onResume() {
        super.onResume()
        //initPresenter()
    }

    private fun initPresenter() {
        productsPresenter = ProductsPresenter(this, this)
        addToWishListPresenter = AddToWishListPresenter(this, this)
        removeFromWishListPresenter = RemoveFromWishListPresenter(this, this)
        fetchSectionProducts()
    }

    fun addAdapter(arr: ArrayList<ProductVariation>) {
        adapter = PopulatorAdapter(applicationContext, arr, this)
        binding?.productsRecyclerView?.adapter = adapter
    }

    override fun onProductsDataFound(sectionProductData: SectionProductData?, message: String) {


    }

    override fun onPopularProductsDataFound(
        sectionProduct: PopularSectionProductData?,
        message: String
    ) {
        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE

        productsDataItems.clear()

        if (sectionProduct?.cartCount != null && sectionProduct?.cartCount != "0") {
            toolbar_cart_item_count.text = sectionProduct.cartCount
            toolbar_cart_item_count.visibility = View.VISIBLE
        } else {
            toolbar_cart_item_count.visibility = View.GONE
        }
        productsDataItems = sectionProduct!!.products!!
        Log.e("############,prod",""+productsDataItems.size);
        if (productsDataItems.isNullOrEmpty()) {
//            if (subCategoryResult?.categories!![0].subCategories == null)
            binding!!.mainLayout.visibility = View.GONE
            binding!!.noDataFoundLayout.visibility = View.VISIBLE
            return
        } else {
//            binding?.pbar?.visibility = View.GONE
//            if (sectionProduct?.next_page_url != null) {
//                isScrolling = true
//                isRefreshing = true
//            } else {
//                isScrolling = false
//                isRefreshing = true
//            }
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
            if (! sectionProduct?.products!!.isNullOrEmpty()) {
                productsDataItems =  sectionProduct?.products!!
                productsDataItemsAll?.addAll(productsDataItems!!)

                adapter?.updateList(productsDataItems)
//                addAdapter(productsDataItemsAll)
            }

        }
    }


    override fun onProductsDataNotFound(message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        AppUtil.firebaseEvent(applicationContext,"error","error_events", message)

        showToast(message)
    }

    override fun onAdapterItemTapped(adapterItem: String?, categoryName: String?,variationId:String?) {
        if (categoryName == "addedToWishList") {
            for(i in 0 until productsDataItemsAll.size){
                if(productsDataItemsAll[i].variationId==variationId){
                    AppUtil.firebaseEvent(applicationContext,"name","wishlist",productsDataItemsAll[i].product_title!!)
                    break
                }
            }
            adapterItem?.let {
                addToWishListPresenter?.addToWishList(
                    it,
                    ContextUtils.getAuthToken(this)
                )
            }
        } else if (categoryName == "removedFromWishList") {
            adapterItem?.let {
                removeFromWishListPresenter?.removeFromWishList(
                    it,
                    ContextUtils.getAuthToken(this)
                )
            }

        } else {
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("productId", adapterItem.toString())
            intent.putExtra("category_name", categoryName)
            intent.putExtra("position", 0)
            intent.putExtra("type", "home")
            intent.putExtra("id", variationId)

            startActivity(intent)
        }
//        global.dataModelValue = productsDataItemsAll.get(pod)
//        val i = Intent(applicationContext, OrderDetailsActivity::class.java)
//        startActivity(i)
    }

    override fun onAdapterItemCategoryTapped(adapterItem: String?, categoryName: String?) {
    }

    override fun onAddedToWishList(message: String) {
        if(PreferenceManager().getInstance(this).getAuthToken().equals("")){
            loginUser(binding!!,"To add this product to wishlist you need to\nlogin first")
        }else {
            binding?.swipeToRefresh?.isRefreshing = false
            showToast(message)
            if (isBanner) fetchBannerProducts()
            else fetchSectionProducts()
        }
    }

    override fun onNotAddedToWishList(message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        AppUtil.firebaseEvent(applicationContext,"error","error_events", message)

        showToast(message)
    }

    override fun onRemovedFromWishList(message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        showToast(message)
        if (isBanner) fetchBannerProducts()
        else fetchSectionProducts()

    }

    override fun onNotRemovedFromWishList(message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        AppUtil.firebaseEvent(applicationContext,"error","error_events", message)

        showToast(message)
    }

    private fun loginUser(binding: ProductListActivityBinding, message:String) {


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

                val googleSignInClient = GoogleSignIn.getClient(this@PopularActivity, gso)
                googleSignInClient.signOut()
                LoginManager.getInstance().logOut()

            }
            WelcomeActivity.start(this@PopularActivity)
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


}