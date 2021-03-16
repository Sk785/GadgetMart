package com.gadgetmart.ui.my_review

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.EditReviewScreenBinding
import com.gadgetmart.databinding.ReviewListScreenBinding
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.custom.CustomProgressDialog
import kotlinx.android.synthetic.main.no_data_found.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditReviewActivity: BaseActivity<EditReviewScreenBinding>(){
    lateinit var binding: EditReviewScreenBinding
    private var customProgressDialog: CustomProgressDialog? = null
    var title=""
    var description=""
    var id=""


    override fun getContentView(): Int {

        return R.layout.edit_review_screen

    }

    override fun init(binding: EditReviewScreenBinding) {
        this.binding = binding
        binding.toolbarID.backIcon?.setOnClickListener { finish() }
        binding.toolbarID.toolbarTitleTextView?.text="Add Review"
        binding.toolbarID.toolbarOhnikImageView.visibility= View.GONE
        binding.toolbarID.toolbarCartIcon.visibility= View.GONE
        title=intent.extras!!.getString("title").toString()
        //description=intent.extras!!.getString("description").toString()
        id= intent.extras!!.getString("id").toString()


        binding.productName.text=title
        //binding.productDescription.text=description
        binding.productsOfSubcategoryRatingBar.rating=intent.extras!!.getString("rating").toString().toFloat()
        binding.edTitle.setText(intent.extras!!.getString("ratingTitle"))
        binding.edMessage.setText(intent.extras!!.getString("message"))

        if (intent.extras!!.getString("image")!= null && intent.extras!!.getString("image") != "") {
            Glide.with(this)
                .apply {
                    RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.default_icon)
                        .override(256, 140)
                        .fitCenter();
                }
                .load(intent.extras!!.getString("image"))
                .placeholder(R.drawable.default_icon)
                .into(binding.productImage)
        } else {
            Glide.with(this)
                .load(R.drawable.default_icon)
                .into(binding.productImage)
        }

        customProgressDialog = CustomProgressDialog(this)
        binding.btnSubmitReview.setOnClickListener {
            if(binding.edTitle.text.toString().length==0){
                showToast("Please add title")

            }else if(binding.edMessage.text.toString().length==0){
                showToast("Please add message")

            }else if(binding.productsOfSubcategoryRatingBar.rating==0f){
                showToast("Please select rating star")

            }else{
                customProgressDialog!!.dialogShow()
                applyReview(id,binding.edMessage.text.toString(),binding.edTitle.text.toString(),binding.productsOfSubcategoryRatingBar.rating.toString())
            }

        }

    }

    private fun applyReview(variation_id:String,description:String,title:String,rating:String) {
        try {

            ApiClientGenerator
                .getClient()!!
                .addOrUpdateOrderReview(variation_id,description,title,rating,
                    ContextUtils.getAuthToken(this)
                )
                .enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        customProgressDialog!!.dialogDismiss()

                        if (response.code() < 200 || response.code() >= 300) {
                            return
                        }

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("review response",response.toString())
                        if (response.getString("status") == "1") {
                            showToast(response.getString("message"))

var i=Intent()
                            setResult(Activity.RESULT_OK,i)
                            finish()

                        } else {
                            AppUtil.firebaseEvent(applicationContext,"error","error_events",response.getString("message"))

                        }
                    }

                    override fun onFailure(
                        call: Call<ResponseBody?>,
                        t: Throwable
                    ) {
                        customProgressDialog!!.dialogDismiss()

                        //Toast.makeText(ContainerAllActivity.this, "failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}