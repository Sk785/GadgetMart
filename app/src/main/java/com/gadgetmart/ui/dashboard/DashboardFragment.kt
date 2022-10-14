package com.gadgetmart.ui.dashboard

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.DashboardFragmentBinding
import com.gadgetmart.ui.category.*
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.ui.product_details.ProductDetailsActivity
import com.gadgetmart.ui.products_of_sub_category.ProductsofSubCategory
import com.gadgetmart.ui.subcategory.AccessoriesActivity
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_home.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : BaseFragment<DashboardFragmentBinding>(),
    DashboardContract,
    DashboardAdapterListener {

    private var binding: DashboardFragmentBinding? = null

    private var categoryAdapter: CategoryAdapter? = null
    private var sectionsAdapter: HomeDataAdapter? = null
    private var popularGadgetsAdapter: PopularGadgetsAdapter? = null
    private var offersAdapter: OffersAdapter? = null
    private var dealsAdapter: DashboardImageAdapter? = null

    private var categories: ArrayList<CategoryItem>? = null
    private var popularGadgets: ArrayList<PopularGadgetOfferItem>? = null
    private var offers: ArrayList<PopularGadgetOfferItem>? = null
    private var dashboardViews: ArrayList<String>? = null
    private var dealsImages: ArrayList<CategoryDeals>? = null
    var timer = Timer()
    lateinit var global:Global

    private var dashboardPresenter: DashboardPresenter? = null
//    private var clearFromRecents: Serdkfd? = null
//    private var clearRecentsBinder: Serdkfd.LocalBinder? = null

    fun newInstance(): DashboardFragment {
        val fragment =
            DashboardFragment()
        val bundle = Bundle()

        fragment.arguments = bundle

        return fragment
    }



    override fun getContentView(): Int {
        return R.layout.dashboard_fragment
    }

    override fun initView(binding: DashboardFragmentBinding) {
        this.binding = binding
        global = activity!!.applicationContext as Global
        dashboardViews = ArrayList()
        dealsImages = ArrayList()
        initRecyclerViewAndAdapter(binding)
    }

    private fun initRecyclerViewAndAdapter(binding: DashboardFragmentBinding) {
        // Categories
        val categoriesLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.layoutManager = categoriesLayoutManager

        categoryAdapter = CategoryAdapter(context!!, ArrayList(), this)
        binding.categoriesRecyclerView.adapter = categoryAdapter

        // Sections
        val sectionsLayoutManager = LinearLayoutManager(context)
        sectionsLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.sectionsRecyclerView.layoutManager = sectionsLayoutManager

        sectionsAdapter = HomeDataAdapter(context!!, ArrayList(), this, timer)
        binding.sectionsRecyclerView.adapter = sectionsAdapter
        checkPermissions()

//        checkPermissions()
//     callDashBoardData(true)

//        addLayoutFromArray(dashboardViews)

    }
 fun callDashBoardData(firstTimeDialog: Boolean){
     dashboardPresenter =
         DashboardPresenter(activity!!, this@DashboardFragment)
     dashboardPresenter!!.getDashboardData(
             ContextUtils.getAuthToken(context),
             firstTimeDialog
     )
 }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callDashBoardData(false)

    }
    override fun initPresenters() {
//        checkPermissions()
    }

    override fun openInternetDialog() {

    }

    override fun onStart() {
        super.onStart()
        if(global.isLoading) {
            callDashBoardData(true)
        }else{
            callDashBoardData(false)

        }

    }

    override fun onResume() {
        super.onResume()
        global.isResume=true
    }

    override fun onPause() {
        super.onPause()
        global.isResume=false

    }

    private fun getDashboardResult(firstTimeDialog: Boolean) {
        ApiClientGenerator
            .getClient()!!
            .getHomeData(ContextUtils.getAuthToken(activity))
            .enqueue(object : Callback<ResponseBody> {

                override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                ) {
                    try {
                        val jsonObject = JSONObject(response.body()?.string()!!)
                        val jsonObjectData = jsonObject.getJSONObject("data")
                        val jsonObjectSections = jsonObjectData.getJSONObject("sections")
                        val keys: Iterator<String> = jsonObjectSections.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            if (jsonObjectSections.getJSONArray(key) is JSONArray) {
                                Log.e("array ::: ", "" + key)
                                dashboardViews?.add(key)
                            }
                        }
                        //addLayoutFromArray(dashboardViews)
                        dashboardPresenter =
                                DashboardPresenter(activity!!, this@DashboardFragment)
                        dashboardPresenter!!.getDashboardData(
                                ContextUtils.getAuthToken(context),
                                firstTimeDialog
                        )

                    } catch (e: Exception) {

                        showToast("Unauthenticated")
                    }
                }

                override fun onFailure(
                        call: Call<ResponseBody>,
                        t: Throwable
                ) {
//                    dismissDialog()

                }
            })
    }

    override fun initListeners(binding: DashboardFragmentBinding) {

//        binding.backToLogin.setOnClickListener { onResendOtpLinkTapped() }

//        binding.continueButton.setOnClickListener { onSendOtpButtonTapped() }

//        binding.otpView?.setOtpCompletionListener { otp -> otpNumber = otp!! }

        binding.swipeToRefresh?.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = true
            categories?.clear()
            popularGadgets?.clear()
            offers?.clear()
            dashboardViews?.clear()
            binding.homeLayout?.visibility = View.GONE
            callDashBoardData(true)

            //getDashboardResult(false)
        }

        binding.swipeToRefresh?.setColorSchemeColors(
                Color.rgb(255, 93, 94),
                Color.GREEN,
                Color.BLUE
        )
    }

    override fun onDashboardDataFound(dashboardResult: DashboardResult?, message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        global.isLoading=false

        if (dashboardResult == null) {
            return
        }
        if (dashboardResult.cartCount != null && dashboardResult.cartCount != "0") {
            (activity as HomeActivity).toolbar.toolbar_cart_item_count.text = dashboardResult.cartCount
            (activity as HomeActivity).toolbar.toolbar_cart_item_count.visibility = View.VISIBLE
           // Log.e("user name", PreferenceManager().getInstance(activity).getUserName()!!)
            global.notificationSchdule(activity!!, PreferenceManager().getInstance(activity).getUserName()!!)
        } else {
            (activity as HomeActivity).toolbar.toolbar_cart_item_count.visibility = View.GONE
            global.cancelNotification(activity!!)
        }

//        Log.d("CategorySize", " " + dashboardResult!!.categories)
//        Log.d("PopularGadgetsSize", " " + dashboardResult.sections?.popularGadgets)
//        Log.d("OffersSize", " " + dashboardResult.sections?.offers)
        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE

        if (dashboardResult.categories == null || dashboardResult.categories.size == 0) {
            binding!!.categoriesRecyclerView.visibility = View.GONE
        } else
            binding!!.categoriesRecyclerView.visibility = View.VISIBLE


        if (dashboardResult.sections == null || dashboardResult.sections.size == 0) {
            binding!!.sectionsRecyclerView.visibility = View.GONE
        }
        else
            binding!!.sectionsRecyclerView.visibility = View.VISIBLE


var p=0;
for(i in 0 until dashboardResult.sections!!.size){
    if(dashboardResult.sections!!.get(i).name=="Recent History"){

p=i;
        break
    }
}
        if(p!=0) {
            if(dashboardResult.sections.get(p).variations!!.size==0) {
                dashboardResult.sections!!.removeAt(p);
            }
        }

        categoryAdapter!!.updateList(dashboardResult.categories)
        sectionsAdapter!!.updateList(dashboardResult.sections, timer)


        if(global.productID.equals("")){

        }else {
            val intent = Intent(activity, ProductDetailsActivity::class.java)
            intent.putExtra("productId", global.productID)
            intent.putExtra("category_name", "Product Details")
            intent.putExtra("position", 0)
            intent.putExtra("type", "home")
            intent.putExtra("id", global.varitionID)
            startActivity(intent)
        }

//        popularGadgets = ArrayList()
//        offers = ArrayList()
//        dealsImages = ArrayList()
//        popularGadgets = dashboardResult.sections?
//        offers = dashboardResult.sections?.offers
//        dealsImages = dashboardResult.sections?.deals!!
//        addLayoutFromArray(dashboardViews)

        /*  popularGadgetsAdapter!!.updateList(dashboardResult.sections?.popularGadgets)


              offersAdapter!!.updateList(dashboardResult.sections?.offers)
              if(dashboardResult.sections?.deals!!.size>0){
                  dealsAdapter!!.updateImages(dashboardResult.sections?.deals!!)

              }*/

    }


    override fun onDashboardDataNotFound(message: String) {
        AppUtil.firebaseEvent(activity!!, "error", "error_events", message)

//        showToast(message)
        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE
        Log.e("DashBoardError ::: ", "" + message)
    }

    override fun onAdapterItemTapped(adapterItem: String?, categoryName: String?, variationId: String?) {
        Log.e("adapter item", adapterItem);
//        showToast(adapterItem.toString())
        val intent = Intent(activity, ProductDetailsActivity::class.java)
        intent.putExtra("productId", adapterItem.toString())

        intent.putExtra("category_name", categoryName)
        intent.putExtra("position", 0)
        intent.putExtra("type", "home")
        intent.putExtra("id", variationId)

        startActivity(intent)

    }

    override fun onAdapterItemCategoryTapped(adapterItem: String?, categoryName: String?) {
        AppUtil.firebaseEvent(activity!!, "name", "category", categoryName!!)

        AccessoriesActivity.start(activity, adapterItem.toString(), categoryName!!)

    }

    private fun addLayoutFromArray(dashboardViews: ArrayList<String>?) {
        if (dashboardViews != null && dashboardViews.size > 0) {
//            arrayFirst = dashboardViews[0]
//            arraySecond = dashboardViews[1]
//            arrayThird = dashboardViews[2]
//            initLayoutFirst(arrayFirst)
//            initLayoutSecond(arraySecond)
//            initLayoutThird(arrayThird)
        }
    }

/*

    private fun initLayoutFirst(type: String) {
        try {
            binding?.layoutFirst?.removeAllViews()
        } catch (e: Exception) {
        }

        when (type) {
            "popular" -> {
                if (popularGadgets!!.size > 0) {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.popular_layout, null)
                    binding?.layoutFirst?.addView(v)
                    popularRecyclerView = v.findViewById(R.id.popular_gadgets_recycler_view)
                    popularViewAll = v.findViewById(R.id.view_all_gadgets_layout)
                    popularlayout(popularRecyclerView, popularViewAll)
                    return
                }

            }
            "deals" -> {
                if (dealsImages!!.size > 0) {

                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.deals_layout, null)
                    binding?.layoutFirst?.addView(v)
                    dealsViewPager = v.findViewById(R.id.dashboard_view_pager)
                    dotsIndicator = v.findViewById(R.id.dots_indicator)
                    dealsLayout(dealsViewPager, dotsIndicator)
                    return
                }

            }
            "offers" -> {
                if (offers!!.size > 0) {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.offers_layout, null)
                    binding?.layoutFirst?.addView(v)
                    offersRecyclerView = v.findViewById(R.id.offers_recycler_view)
                    offersViewALl = v.findViewById(R.id.view_all_offers_layout)
                    offersLayout(offersRecyclerView, offersViewALl)
                    return
                }
            }
        }

    }

    private fun initLayoutSecond(type: String) {
        try {
            binding?.layoutSecond?.removeAllViews()
        } catch (e: Exception) {
        }
        when (type) {
            "popular" -> {
                if (popularGadgets!!.size > 0) {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.popular_layout, null)
                    binding?.layoutSecond?.addView(v)
                    popularRecyclerView = v.findViewById(R.id.popular_gadgets_recycler_view)
                    popularViewAll = v.findViewById(R.id.view_all_gadgets_layout)
                    popularlayout(popularRecyclerView, popularViewAll)
                    return
                }

            }
            "deals" -> {
                if (dealsImages!!.size > 0) {

                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.deals_layout, null)
                    binding?.layoutSecond?.addView(v)
                    dealsViewPager = v.findViewById(R.id.dashboard_view_pager)
                    dotsIndicator = v.findViewById(R.id.dots_indicator)
                    dealsLayout(dealsViewPager, dotsIndicator)
                    return
                }


            }
            "offers" -> {
                if (offers!!.size > 0) {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.offers_layout, null)
                    binding?.layoutSecond?.addView(v)
                    offersRecyclerView = v.findViewById(R.id.offers_recycler_view)
                    offersViewALl = v.findViewById(R.id.view_all_offers_layout)
                    offersLayout(offersRecyclerView, offersViewALl)
                    return
                }

            }
        }
    }

    private fun initLayoutThird(type: String) {
        try {
            binding?.layoutThird?.removeAllViews()
        } catch (e: Exception) {
        }
        when (type) {
            "popular" -> {
                if (popularGadgets!!.size > 0) {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.popular_layout, null)
                    binding?.layoutThird?.addView(v)
                    popularRecyclerView = v.findViewById(R.id.popular_gadgets_recycler_view)
                    popularViewAll = v.findViewById(R.id.view_all_gadgets_layout)
                    popularlayout(popularRecyclerView, popularViewAll)
                    return
                }

            }
            "deals" -> {
                if (dealsImages!!.size > 0) {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.deals_layout, null)
                    binding?.layoutThird?.addView(v)
                    dealsViewPager = v.findViewById(R.id.dashboard_view_pager)
                    dotsIndicator = v.findViewById(R.id.dots_indicator)
                    dealsLayout(dealsViewPager, dotsIndicator)
                    return
                }


            }
            "offers" -> {
                if (offers!!.size > 0) {
                    val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
                    val v: View = layoutInflater.inflate(R.layout.offers_layout, null)
                    binding?.layoutThird?.addView(v)
                    offersRecyclerView = v.findViewById(R.id.offers_recycler_view)
                    offersViewALl = v.findViewById(R.id.view_all_offers_layout)
                    offersLayout(offersRecyclerView, offersViewALl)
                    return
                }

            }
        }
    }
*/


//    private fun popularlayout(recyclerView: RecyclerView?, viewall: LinearLayout?) {
//        // Popular Gadgets
//
//
//        popularGadgetsAdapter = PopularGadgetsAdapter(context!!, ArrayList(), this)
//        recyclerView?.adapter = popularGadgetsAdapter
//
//        val horizontalDivider = ContextCompat.getDrawable(activity!!, R.drawable.line_divider)
//        val verticalDivider =
//            ContextCompat.getDrawable(activity!!, R.drawable.line_divider_vertical)
//        val popularGadgetsLayoutManager = GridLayoutManager(context, 2)
//
//        popularGadgetsLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int = 1
//        }
//
//        recyclerView?.layoutManager = popularGadgetsLayoutManager
//        if (popularGadgets!!.size == 1) {
//
//        } else {
//            recyclerView?.addItemDecoration(
//                GridDividerItemDecoration(
//                    horizontalDivider,
//                    verticalDivider,
//                    2
//                )
//            )
//        }
//
//        viewall?.setOnClickListener {
//            ProductsofSubCategory.start(activity!!, "0", "Popular Gadgets")
//        }
//    }

    private fun offersLayout(recyclerView: RecyclerView?, viewall: LinearLayout?) {
        // Offers
        val offersLayoutManager = GridLayoutManager(context, 3)

        offersLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = 1
        }

        recyclerView?.layoutManager = offersLayoutManager

//        offersAdapter = OffersAdapter(context!!, offers, this)
//        recyclerView?.adapter = offersAdapter

        viewall?.setOnClickListener {
            ProductsofSubCategory.start(activity!!, "0", "Offers")
        }
    }

    private fun dealsLayout(viewPager: ViewPager?, dots: IndefinitePagerIndicator?) {
        dealsAdapter = DashboardImageAdapter(activity!!, dealsImages!!, "sectionName")
        viewPager?.adapter = dealsAdapter
        dots?.attachToViewPager(viewPager)
    }


    private fun checkPermissions() {
        TedPermission.with(activity)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    //getDashboardResult(true)

                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {

                }
            })
            .setDeniedMessage("If you reject permission,you can not use this service\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }
}