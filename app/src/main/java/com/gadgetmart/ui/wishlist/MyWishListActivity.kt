package com.gadgetmart.ui.wishlist

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.ActivityMyWishListBinding
import com.gadgetmart.ui.product_details.ProductDetailsActivity
import com.gadgetmart.ui.product_details.productmodel.CartData
import com.gadgetmart.ui.products_of_sub_category.ProductDataWishlist
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.custom.CustomProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWishListActivity : BaseActivity<ActivityMyWishListBinding>() , WishListContract , WishListAdapterListener{
    lateinit var binding: ActivityMyWishListBinding
    private var myWishListAdapter : MyWishListAdapter ?= null
    private var myWishList : ArrayList<ProductDataWishlist> ?= null
    lateinit var wishListPresenter: WishListPresenter
    lateinit var myDialog: CustomProgressDialog

    private var position : Int ?= null
    override fun getContentView(): Int {
        return R.layout.activity_my_wish_list
    }

    override fun init(binding: ActivityMyWishListBinding) {
        this.binding = binding
        binding.toolbarID.toolbarTitleTextView?.text = "Wishlist"
        myDialog = CustomProgressDialog(this)

        initListeners(binding)
        initRecyclerView(binding)
        initPresenter()
    }

    private fun initListeners(binding: ActivityMyWishListBinding){
        binding.toolbarID.toolbarBackIcon?.setOnClickListener { finish() }
        binding.btnfinish.setOnClickListener { finish() }

        binding.swipeToRefresh?.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = true
            myWishList?.clear()
            binding.homeLayout?.visibility = View.GONE
            wishListPresenter.getMyWishList(ContextUtils.getAuthToken(this), false)
        }

        binding.swipeToRefresh?.setColorSchemeColors(Color.rgb(255,93,94) , Color.GREEN , Color.BLUE)

    }

    private fun initRecyclerView(binding: ActivityMyWishListBinding){
        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.wishlistRecyclerView.layoutManager = subCategoriesLayoutManager

        myWishListAdapter = MyWishListAdapter(this, myWishList, this)
        binding.wishlistRecyclerView.adapter = myWishListAdapter
    }

    private fun initPresenter(){
        wishListPresenter = WishListPresenter(this, this)
        wishListPresenter.getMyWishList(ContextUtils.getAuthToken(this), true)
    }

    override fun onWishListDataFound(
        wishListRemovedResult: WishListRemovedResult?,
        message: String,
        apiFlag : Int,
        status:Int?
    ) {
            when (apiFlag) {
                AppUtil.GetWishListFlag -> {
                    binding.swipeToRefresh?.isRefreshing = false
                    binding.homeLayout?.visibility = View.VISIBLE
                    if (wishListRemovedResult?.my_wish_list == null || wishListRemovedResult.my_wish_list.size == 0) {
                        binding.mainLayout?.visibility = View.GONE
                        binding.mainLayoutEmpty?.visibility = View.VISIBLE
                        return
                    } else {
                        binding.mainLayout?.visibility = View.VISIBLE
                        binding.mainLayoutEmpty?.visibility = View.GONE
                        myWishList = wishListRemovedResult.my_wish_list
                        myWishListAdapter?.updateList(wishListRemovedResult.my_wish_list)
                    }
                }
                AppUtil.RemoveWishListFlag -> {
                    myWishList?.removeAt(position!!)
                    myWishListAdapter?.notifyDataSetChanged()
                    if (myWishList?.size == 0){
                        binding.mainLayout?.visibility = View.GONE
                        binding.mainLayoutEmpty?.visibility = View.VISIBLE
                    }
                }
            }

    }

    override fun onWisListDataNotFound(message: String) {
        binding.swipeToRefresh?.isRefreshing = false
        binding.homeLayout?.visibility = View.VISIBLE
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message
        )

    }

    override fun onAdapterItemTapped(adapterItem: Int?, categoryName: String?, itemPosition : Int?) {
        when(categoryName){
            "removedFromWishList" ->{
                position = itemPosition
                wishListPresenter.removeFromWishList(adapterItem!!, ContextUtils.getAuthToken(this))
            }
            "Product Details" ->{
                Log.e("ids", adapterItem.toString()+" "+itemPosition.toString())
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("productId", adapterItem.toString())

                intent.putExtra("category_name", categoryName)
                intent.putExtra("position", 0)
                intent.putExtra("type", "home")
                intent.putExtra("id", itemPosition.toString())

                startActivity(intent)

            }
            else -> {

            }
        }
    }

    override fun onAddToBag(adapterItem: Int?, product_id: String?,title:String) {
        myDialog.dialogShow()
        AppUtil.firebaseEvent(applicationContext,"name","product_added_to_cart ",title)

        addOrUpdatecart(product_id!!, 1)

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
                            showToast(response.body()!!.message)
                            initPresenter()
                        } else {
                            AppUtil.firebaseEvent(applicationContext,"error","error_events",response.body()!!.message
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


    companion object{

        fun start(context: Context?) {
            val intent = Intent(context, MyWishListActivity::class.java)
            context!!.startActivity(intent)
        }
    }


}
