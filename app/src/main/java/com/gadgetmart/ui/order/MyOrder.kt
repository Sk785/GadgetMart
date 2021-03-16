package com.gadgetmart.ui.order

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.MyOrderLayoutBinding
import com.gadgetmart.ui.order.adapter.MyOrderAdapter
import com.gadgetmart.ui.order.interfaces.MyOrderInterface
import com.gadgetmart.ui.order.model.DataModel
import com.gadgetmart.ui.order.model.DataModelArray
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*

class MyOrder : BaseActivity<MyOrderLayoutBinding>(), MyOrderContract, MyOrderInterface {
    private var binding: MyOrderLayoutBinding? = null
    private var orderPresenter: MyOrderPresenter? = null
    private var adapter: MyOrderAdapter? = null

    private var isScrolling = true
    private var isRefreshing = true
    private var currentPage: Int = 1

    lateinit var productsDataItems: ArrayList<DataModelArray>
    lateinit var productsDataItemsAll: ArrayList<DataModelArray>
    lateinit var global: Global


    override fun getContentView(): Int {
        return R.layout.my_order_layout

    }

    override fun init(binding: MyOrderLayoutBinding) {
        this.binding = binding
        global = applicationContext as Global

        binding.toolbar.toolbar_title_text_view.text = "My orders"
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE
        setListeners(binding)
        val subCategoriesLayoutManager = LinearLayoutManager(this)
        binding.myOrderRecyclerview.layoutManager = subCategoriesLayoutManager
        binding.myOrderRecyclerview.isNestedScrollingEnabled = false
        adapter = MyOrderAdapter(applicationContext, ArrayList(), this)
        binding.myOrderRecyclerview.adapter = adapter
        initPresenter()
//        addAdapter(productsDataItemsAll)

    }

    private fun setListeners(binding: MyOrderLayoutBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nes_scrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (nes_scrollView.getChildAt(nes_scrollView.childCount - 1) != null) {
                if (scrollY >= nes_scrollView.getChildAt(nes_scrollView.childCount - 1).measuredHeight - nes_scrollView.measuredHeight &&
                    scrollY > oldScrollY
                ) {

                    if (isScrolling) {
                        isScrolling = false
                        isRefreshing = false
                        binding.pbar.visibility = View.VISIBLE
                        orderPresenter?.getDashboardData(
                            currentPage,
                            ContextUtils.getAuthToken(this),
                            true
                        )

                    }
                }
            }
        })

//        binding.swipeToRefresh.visibility = View.GONE
        binding.swipeToRefresh.setOnRefreshListener {
//            if (isRefreshing) {
            productsDataItemsAll.clear()
            binding.swipeToRefresh.isRefreshing = true
            currentPage = 1
            orderPresenter?.getDashboardData(
                currentPage,
                ContextUtils.getAuthToken(this),
                true
            )

//            }
        }

        binding.swipeToRefresh.setColorSchemeColors(
            Color.rgb(255, 93, 94),
            Color.GREEN,
            Color.BLUE
        )
    }


    private fun initPresenter() {
        productsDataItems = ArrayList<DataModelArray>()
        productsDataItemsAll = ArrayList<DataModelArray>()
        currentPage=0
        orderPresenter = MyOrderPresenter(this, this)
        orderPresenter?.getDashboardData(
            currentPage,
            ContextUtils.getAuthToken(this),
            true
        )
    }

    fun addAdapter(arr: ArrayList<DataModelArray>) {
        adapter = MyOrderAdapter(applicationContext, arr, this)
        binding?.myOrderRecyclerview?.adapter = adapter
    }

    override fun onMyOrderDataFound(orderData: DataModel?, message: String, apiFlag: Int?) {
        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE
        currentPage=currentPage+1
        if (orderData!!.data == null || orderData?.data?.size == 0) {
//            if (subCategoryResult?.categories!![0].subCategories == null)
            binding!!.mainLayout.visibility = View.GONE
            binding!!.noDataFoundLayout.visibility = View.VISIBLE
            return
        } else {
            binding?.pbar?.visibility = View.GONE
            if (orderData?.next_page_url != null) {
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
            if (!orderData.data.isNullOrEmpty()) {
                productsDataItems = orderData.data
                productsDataItemsAll.addAll(productsDataItems)
                adapter?.updateList(productsDataItemsAll)
//                addAdapter(productsDataItemsAll)
            }

        }
    }

    override fun onMyOrderDataNotFound(message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message)

        showToast(message)
    }

    override fun parentClick(pod: Int) {
        global.dataModelValue = productsDataItemsAll.get(pod)
        val i = Intent(applicationContext, OrderDetailsActivity::class.java)
        startActivityForResult(i,101)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101){
            if(resultCode==Activity.RESULT_OK){
                initPresenter()
//                orderPresenter = MyOrderPresenter(this, this)
//                orderPresenter?.getDashboardData(
//                    currentPage,
//                    ContextUtils.getAuthToken(this),
//                    true
//                )
            }
        }
    }
    override fun childClick(parentPos: Int, childPos: Int) {

    }

    override fun onItemClicked(position: Int) {

    }

    override fun openReviewScreen(parentPos: Int, childPos: Int) {

    }

    override fun openCancelScreen(parentPos: Int, childPos: Int,type:Int) {

    }
}