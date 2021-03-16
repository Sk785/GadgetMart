package com.gadgetmart.ui.order

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.LayoutCancelOrderBinding
import com.gadgetmart.databinding.OrderDetailsBinding
import com.gadgetmart.databinding.TrackProductLayoutBinding
import com.gadgetmart.ui.order.adapter.TrackProductAdapter
import com.gadgetmart.ui.order.model.TrackListModel
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import kotlinx.android.synthetic.main.no_data_found.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackProduct : BaseActivity<TrackProductLayoutBinding>() {
     private var binding: TrackProductLayoutBinding? = null

     override fun getContentView(): Int {

         return R.layout.track_product_layout

     }

     override fun init(binding: TrackProductLayoutBinding) {
         this.binding=binding
         binding.toolbar.toolbar_title_text_view.text = "Track product"
         binding.toolbar.toolbar_ohnik_image_view.visibility = View.GONE
         binding.toolbar.toolbar_cart_item_count.visibility = View.GONE
         toolbar_cart_icon.visibility = View.GONE
         toolbar_ohnik_image_view.visibility = View.GONE
         val trackListLayoutManager = LinearLayoutManager(this)
         binding.trackList.layoutManager = trackListLayoutManager
         var id=intent.getStringExtra("product_id")
        // Log.e("product_id",id)
         callTrackOrderApi(id)
         setListeners(binding)
     }

    private fun setListeners(binding: TrackProductLayoutBinding) {
        binding.toolbar.back_icon.setOnClickListener { finish() }


    }

    fun  callTrackOrderApi(productid:String){
        ApiClientGenerator
            .getClient()!!
            .truckOrder(productid, ContextUtils.getAuthToken(this))
            .enqueue(object : Callback<ApiResponse<TrackListModel?>> {
                override fun onResponse(
                    call: Call<ApiResponse<TrackListModel?>>,
                    response: Response<ApiResponse<TrackListModel?>>
                ) {

                    if (response.code() < 200 || response.code() >= 300) {
                        return
                    }
                    if (response.body() != null) {
                        if(response.body()!!.data!!.orderStatus!!.size>0) {
                            binding!!.noDataFoundLayout.visibility=View.GONE
                            binding!!.trackList.visibility=View.VISIBLE
                            binding!!.trackList.adapter = TrackProductAdapter(
                                this@TrackProduct,
                                response.body()!!.data!!.orderStatus
                            )

                        }else{
                            binding!!.noDataFoundLayout.no_data_found_text.text="No Tracking avaliable"
                            binding!!.noDataFoundLayout.visibility=View.VISIBLE
                            binding!!.trackList.visibility=View.GONE
                        }


                    } else {
                        AppUtil.firebaseEvent(applicationContext,"error","error_events", response.body()!!.message)

                    }

                }

                override fun onFailure(
                    call: Call<ApiResponse<TrackListModel?>>,
                    t: Throwable
                ) {
                    showToast("No data found")

                    //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
    }
 }