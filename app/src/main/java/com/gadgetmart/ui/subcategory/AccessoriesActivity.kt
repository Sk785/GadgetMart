package com.gadgetmart.ui.subcategory

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.ActivityAccessoriesBinding
import com.gadgetmart.ui.category.SubCategoryItem
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.products_of_sub_category.ProductsofSubCategory
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*

class AccessoriesActivity : BaseActivity<ActivityAccessoriesBinding>(), SubCategoryContract,
    DashboardAdapterListener {
    private var binding: ActivityAccessoriesBinding? = null
    private var subCategoryAdapter: SubCategoryAdapter? = null
    private var dashboardPresenter: SubCategoryPresenter? = null
    private var subcategoryItemsList: ArrayList<SubCategoryItem>? = null
    private var categoryId = ""
    private var categoryName = ""
    override fun getContentView(): Int {
        return R.layout.activity_accessories
    }

    override fun init(binding: ActivityAccessoriesBinding) {
        this.binding = binding
        setSupportActionBar(binding.toolbar)
        getIntentData()
        binding.toolbarID.toolbarTitleTextView.text = categoryName
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

    private fun setListeners(binding: ActivityAccessoriesBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
        binding.toolbarID.toolbarCartIcon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE
        binding.swipeToRefresh?.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = true
            subcategoryItemsList?.clear()
            binding.homeLayout?.visibility = View.GONE
            dashboardPresenter?.getSubcategories(
                categoryId,
                ContextUtils.getAuthToken(this),
                isDialogShows = false
            )
        }

        binding.swipeToRefresh?.setColorSchemeColors(
            Color.rgb(255, 93, 94),
            Color.GREEN,
            Color.BLUE
        )

    }

    private fun initRecyclerViewAndAdapter(binding: ActivityAccessoriesBinding) {
        // Categories
        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.subcategoryRecyclerview.layoutManager = subCategoriesLayoutManager

        subCategoryAdapter = SubCategoryAdapter(this, subcategoryItemsList, this)
        binding.subcategoryRecyclerview.adapter = subCategoryAdapter
    }

    private fun initPresenter() {
        dashboardPresenter =
            SubCategoryPresenter(this, this)
        dashboardPresenter!!.getSubcategories(categoryId, ContextUtils.getAuthToken(this), true)
    }

    companion object {
        fun start(context: Context?, categoryId: String, categoryName: String) {
            val intent =
                Intent(context, AccessoriesActivity::class.java).putExtra("categoryId", categoryId)
                    .putExtra("categoryName", categoryName)
            context!!.startActivity(intent)
        }

    }

    override fun onSubCategoryDataFound(subCategoryResult: SubCategoryResult?, message: String) {

        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE
        if (subCategoryResult?.categories!![0].subCategories == null || subCategoryResult.categories[0].subCategories?.size == 0) {
//            if (subCategoryResult?.categories!![0].subCategories == null)
            binding!!.mainLayout.visibility = View.GONE
            binding?.noDataFoundLayout?.visibility = View.VISIBLE
            return
        } else {
            binding!!.mainLayout.visibility = View.VISIBLE
            binding?.noDataFoundLayout?.visibility = View.GONE
            subcategoryItemsList = subCategoryResult.categories[0].subCategories
            subCategoryAdapter!!.updateList(subCategoryResult.categories[0].subCategories)
        }
    }

    override fun onSubCategoryDataNotFound(message: String) {
        binding?.swipeToRefresh?.isRefreshing = false
        binding?.homeLayout?.visibility = View.VISIBLE
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message
        )
    }

    override fun onAdapterItemTapped(adapterItem: String?, categoryName: String?,variationId:String?) {
        AppUtil.firebaseEvent(applicationContext,"name","subcategory",categoryName!!)

        ProductsofSubCategory.start(this, adapterItem.toString(), categoryName!!)
    }

    override fun onAdapterItemCategoryTapped(adapterItem: String?, categoryName: String?) {
    }


}
