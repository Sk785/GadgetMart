package com.gadgetmart.ui.my_review

import android.app.Activity
import com.gadgetmart.ui.my_account.ProfileContract
import com.gadgetmart.ui.my_account.ProfilePresenter


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.base.ApiResponse
import com.gadgetmart.data.domain.ApiClientGenerator
import com.gadgetmart.databinding.MyAccountActivityBinding
import com.gadgetmart.databinding.ReviewListScreenBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.auth.otp_verification.OtpVerificationFragment
import com.gadgetmart.ui.dashboard.DashboardAdapterListener
import com.gadgetmart.ui.edit_profile.EditProfileActivity
import com.gadgetmart.ui.my_address.MyAddressesActivity
import com.gadgetmart.ui.search.SearchPresenter
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.PreferenceManager
import com.gadgetmart.util.custom.CustomProgressDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.no_data_found.view.*
import kotlinx.android.synthetic.main.welcome_activity.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyReviewActivity : BaseActivity<ReviewListScreenBinding>(), ReviewContract,
    DashboardAdapterListener, ReviewInterface {

    private var reviewPresenter: ReviewPresenter? = null
    lateinit var binding: ReviewListScreenBinding
    private var customProgressDialog: CustomProgressDialog? = null
    var reviewData: ReviewData? = null

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, MyReviewActivity::class.java)
            context!!.startActivity(intent)
        }

    }

    override fun getContentView(): Int {
        window.statusBarColor = ContextCompat.getColor(this, R.color.red_light)
        return R.layout.review_list_screen
    }

    override fun init(binding: ReviewListScreenBinding) {
        this.binding = binding
        binding.toolbarID.backIcon?.setOnClickListener { finish() }
        binding.toolbarID.toolbarTitleTextView?.text = "My Review"
        binding.toolbarID.toolbarOhnikImageView.visibility = View.GONE
        binding.toolbarID.toolbarCartIcon.visibility = View.GONE

        customProgressDialog = CustomProgressDialog(this)
        binding.noDataFoundLayout.no_data_found_text.text = "No Review found"
        binding.noDataFoundLayout.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE
        binding.swipeToRefresh.isEnabled = false

        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.reviewRecyclerView.layoutManager = subCategoriesLayoutManager
        initListeners(binding)
        reviewPresenter = ReviewPresenter(this, this)
        customProgressDialog!!.dialogShow()
        reviewPresenter?.reviewList(
            ContextUtils.getAuthToken(this)
        )


    }

    fun initListeners(binding: ReviewListScreenBinding) {


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                reviewPresenter?.reviewList(
                    ContextUtils.getAuthToken(this)
                )
            }

        }
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onAdapterItemTapped(
        adapterItem: String?,
        categoryName: String?,
        variationId: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun onAdapterItemCategoryTapped(adapterItem: String?, categoryName: String?) {
    }

    override fun onReviewFound(result: ReviewData) {
        customProgressDialog!!.dialogDismiss()

        if (result.data?.size != 0) {
            binding.homeLayout.visibility = View.VISIBLE
            reviewData = result
            binding.reviewRecyclerView.adapter =
                ReviewAdapter(applicationContext, this, result.data!!);
        } else {
            binding.homeLayout.visibility = View.GONE
            binding.noDataFoundLayout.no_data_found_text.text = "No Reviews Found"
            binding.noDataFoundLayout.visibility = View.VISIBLE
        }
    }

    override fun onReviewNotFound(message: String) {
        customProgressDialog!!.dialogDismiss()

        binding.homeLayout.visibility = View.GONE
        binding.noDataFoundLayout.no_data_found_text.text = "No Reviews Found"
        binding.noDataFoundLayout.visibility = View.VISIBLE
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message)

    }

    override fun onDeleteClick(pos: Int) {
        customProgressDialog!!.dialogShow()
        deleteReview(reviewData!!.data?.get(pos)!!.id.toString())
        reviewData?.data?.removeAt(pos)
        binding.reviewRecyclerView.adapter = ReviewAdapter(applicationContext, this, reviewData?.data!!);
    }

    override fun onEditClick(pos: Int) {
        val i = Intent(applicationContext, EditReviewActivity::class.java)
        i.putExtra("id", reviewData!!.data!![pos].variation.variationId.toString())
        i.putExtra("title", reviewData!!.data!![pos].variation.product_title)
        i.putExtra("description", reviewData!!.data!![pos].variation.short_description)
        if (!reviewData!!.data!![pos].variation.productImages!![0].name.isNullOrEmpty()) {
            i.putExtra("image", reviewData!!.data!![pos].variation.productImages!![0].name)

        } else {
            i.putExtra("image", "")

        }
        i.putExtra("rating", reviewData!!.data!![pos].rating.toString())
        i.putExtra("ratingTitle", reviewData!!.data!![pos].title.toString())
        i.putExtra("message", reviewData!!.data!![pos].description.toString())




        startActivityForResult(i, 101)
    }

    private fun deleteReview(id: String) {
        try {

            ApiClientGenerator
                .getClient()!!
                .removeReview(
                    id,
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
                        customProgressDialog!!.dialogDismiss()

                        val response = JSONObject(response.body()?.string()!!)
                        Log.e("copupon response", response.toString())
                        if (response.getString("status") == "1") {
                            showToast("Review deleted successfully")
                        } else {
                            showToast("Something went wrong !")
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


