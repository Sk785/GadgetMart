package com.gadgetmart.ui.search

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager

import com.gadgetmart.R
import com.gadgetmart.app.Global
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.SearchLayoutBinding
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.ui.product_details.ProductDetailsActivity
import com.gadgetmart.ui.products.ProductsAdapter
import com.gadgetmart.ui.products.ProductsPresenter
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import com.gadgetmart.ui.splash.WelcomeActivity
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.google.gson.Gson

import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*

class SearchActivity : BaseActivity<SearchLayoutBinding>(),SearchContract, DashboardAdapterListener
     {
    private var binding: SearchLayoutBinding? = null
         private var adapter: SearchAdapter? = null
         private var handler: Handler? = null
         private val timeDelay: Long = 2000

    private var isScrolling = true
    private var isRefreshing = true
    private var currentPage: Int = 1
         lateinit var productsDataItems: ArrayList<ProductVariation>

         private var searchPresenter: SearchPresenter? = null


    lateinit var global: Global

    companion object {
        fun start(context: Context) {
            context.startActivity(
                Intent(context, SearchActivity::class.java)

            )
        }
    }

    override fun getContentView(): Int = R.layout.search_layout

    override fun init(binding: SearchLayoutBinding) {
        this.binding = binding
        global = applicationContext as Global
        productsDataItems=ArrayList()
        binding.toolbar.toolbar_title_text_view.text ="Search"
        toolbar_ohnik_image_view.visibility = View.GONE
       toolbar_cart_icon.visibility = View.GONE


        setListeners(binding)

        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.productsRecyclerView.layoutManager = subCategoriesLayoutManager
        adapter = SearchAdapter(applicationContext, productsDataItems, this)
        binding.productsRecyclerView.adapter=adapter


    }

    private fun setListeners(binding: SearchLayoutBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding!!.etSearch.length()!=0){
                    initPresenter(binding!!.etSearch.text.toString())
                }else{
                    productsDataItems.clear();
                    adapter!!.updateList(productsDataItems)
                    binding!!.noDataFoundLayout.visibility=View.VISIBLE
                    binding!!.mainLayout.visibility=View.GONE
                }
            }
        })
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nes_scrollView, _, scrollY, _, oldScrollY ->
            if (nes_scrollView.getChildAt(nes_scrollView.childCount - 1) != null) {
                if (scrollY >= nes_scrollView.getChildAt(nes_scrollView.childCount - 1).measuredHeight - nes_scrollView.measuredHeight
                    && scrollY > oldScrollY
                ) {

                    if (isScrolling) {
                        isScrolling = false
                        isRefreshing = false
                        binding.pbar.visibility = View.VISIBLE

                    }
                }
            }
        })

//        binding.swipeToRefresh.visibility = View.GONE
        binding.swipeToRefresh.setOnRefreshListener {

            binding.swipeToRefresh.isRefreshing = true
            productsDataItems?.clear()
            binding.homeLayout?.visibility = View.GONE
            searchPresenter = SearchPresenter(this, this)
            searchPresenter?.addToSearchList(
                currentPage,
                binding.etSearch.text.toString(),
                ContextUtils.getAuthToken(this)
            )

        }

        binding.swipeToRefresh.setColorSchemeColors(
            Color.rgb(255, 93, 94),
            Color.GREEN,
            Color.BLUE
        )
    }



    override fun onResume() {
        super.onResume()
      //  initPresenter("")
    }

    private fun initPresenter(text:String) {
        searchPresenter = SearchPresenter(this, this)
        searchPresenter?.addToSearchList(
            currentPage,
            text,
            ContextUtils.getAuthToken(this)
        )

    }

         override fun onAddedToSearchList(sectionProductData: SearchProduct) {


             binding?.swipeToRefresh?.isRefreshing = false
             binding?.homeLayout?.visibility = View.VISIBLE

             productsDataItems.clear()


             val sectionProduct = sectionProductData?.data
             productsDataItems = sectionProduct?.data!!
             if (productsDataItems.isNullOrEmpty()) {
//            if (subCategoryResult?.categories!![0].subCategories == null)
                 binding!!.mainLayout.visibility = View.GONE
                 binding!!.noDataFoundLayout.visibility = View.VISIBLE
                 return
             } else {
                 binding?.pbar?.visibility = View.GONE
                 if (sectionProduct.next_page_url != null) {
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
                 if (!sectionProduct?.data!!.isNullOrEmpty()) {
                     productsDataItems = sectionProduct?.data!!
                     val json= Gson()
                     Log.e("reponse######",json.toJson(productsDataItems))
                     adapter?.updateList(productsDataItems)

//                addAdapter(productsDataItemsAll)
                 }
             }

             }

         override fun onNotAddedToSearchList(message: String) {
         }

         override fun onAdapterItemTapped(
             adapterItem: String?,
             categoryName: String?,
             variationId: String?
         ) {
             val intent = Intent(applicationContext, ProductDetailsActivity::class.java)
             intent.putExtra("productId", adapterItem.toString())

             intent.putExtra("category_name", categoryName)
             intent.putExtra("position", 0)
             intent.putExtra("type", "home")
             intent.putExtra("id", variationId)

             startActivity(intent)
         }

         override fun onAdapterItemCategoryTapped(adapterItem: String?, categoryName: String?) {
         }

         private val runnable: Runnable = Runnable {
             if (!isFinishing) {
                 if(binding!!.etSearch.length()!=0){
                     initPresenter(binding!!.etSearch.text.toString())
                 }else{
                     productsDataItems.clear();
                     adapter!!.updateList(productsDataItems)
                     binding!!.noDataFoundLayout.visibility=View.VISIBLE
                     binding!!.mainLayout.visibility=View.GONE
                 }
             }
         }
     }