package com.gadgetmart.ui.product_details

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.ActivityProductDescriptionBinding
import com.gadgetmart.ui.cart_bag.CartActivity
import com.gadgetmart.ui.category.ProductVariation
import com.gadgetmart.ui.products_of_sub_category.ProductItemsTax
import com.gadgetmart.ui.products_of_sub_category.ProductsDataItems
import kotlinx.android.synthetic.main.activity_product_description.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import java.math.RoundingMode
import java.text.DecimalFormat

class ProductDescriptionActivity : BaseActivity<ActivityProductDescriptionBinding>() {
    private lateinit var binding: ActivityProductDescriptionBinding
    private lateinit var pagerAdapter: MyViewPagerAdapter
    private var productsDataItems: ProductsDataItems? = null
    private var variation: ProductVariation? = null
    private var specifications: ArrayList<ProductSpecificationNew>? = null


    override fun getContentView(): Int {
        return R.layout.activity_product_description
    }

    override fun init(binding: ActivityProductDescriptionBinding) {
        this.binding = binding
        getIntentdata()

        setListeners(binding)
    }

    private fun getIntentdata() {
        if (intent.extras != null) {
            variation = intent.extras?.getParcelable("productDetail")
            specifications=intent.extras?.getParcelableArrayList("specifications")
//            for(i in 0 until productsDataItems?.variations?.size!!){
//                if (productsDataItems?.variations?.get(i)!!.isSelected==true){
//                    variation = productsDataItems?.variations?.get(i)
//break
//                }
//            }
        }
    }

    private fun setListeners(binding: ActivityProductDescriptionBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
        binding.toolbarID.toolbarTitleTextView.text = "Product Description"
        binding.productNameTextView.text = variation?.product_title
        val df = DecimalFormat("#.##")
        df.setRoundingMode(RoundingMode.CEILING)
        if (variation?.currentBatch != null) {
            if(variation?.offer_available!!){
                binding.productNetPriceTextView.visibility = View.VISIBLE

                binding.productNetPriceTextView.paintFlags =
                    binding.productNetPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                val str = resources.getText(R.string.us_dollar).toString()
                    .plus(df.format(variation?.currentBatch!!.product_mrp!!))
                binding.productNetPriceTextView.text = str
                binding.productDiscountedPriceTextView.text =
                    "\u20B9" + df.format(variation?.offer_discount_price)
                binding.productTax.visibility = View.VISIBLE

                binding.productTax.text = intent.extras!!.getString("save_amount")
            }else {
                if (variation?.currentBatch!!.base_rate == variation?.currentBatch!!.product_mrp!!) {
                    binding.productNetPriceTextView.visibility = View.INVISIBLE
                    binding.productDiscountedPriceTextView.text =
                        "\u20B9" + df.format(variation?.currentBatch!!.product_mrp!!)

                    binding.productTax.visibility = View.GONE


                } else {
                    binding.productNetPriceTextView.visibility = View.VISIBLE

                    binding.productNetPriceTextView.paintFlags =
                        binding.productNetPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    val str = resources.getText(R.string.us_dollar).toString()
                        .plus(df.format(variation?.currentBatch!!.product_mrp!!))
                    binding.productNetPriceTextView.text = str
                    binding.productDiscountedPriceTextView.text =
                        "\u20B9" + df.format(variation?.currentBatch!!.base_rate!!)
                    binding.productTax.visibility = View.VISIBLE

                    binding.productTax.text = intent.extras!!.getString("save_amount")

                }
            }

        } else {
            price_main_view.visibility = View.GONE
            binding.productTax.text = "Out of stock"

        }

        if (variation!!.productImages != null) {
            Glide.with(this)
                .apply {
                    RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_logo_toolbar)
                        .override(256, 140)
                        .fitCenter()
                }
                .load(variation!!.productImages!!.get(0).name)
                .placeholder(R.drawable.ic_logo_toolbar)
                .into(binding.productImageView)
        } else {
            Glide.with(this)
                .load(R.drawable.ic_logo_toolbar)
                .into(binding.productImageView)
        }

        pagerAdapter = MyViewPagerAdapter(supportFragmentManager, variation!!,specifications!!)
        binding.viewpager.adapter = pagerAdapter
        binding.tablayout.setupWithViewPager(binding.viewpager)
        toolbar_cart_icon.setOnClickListener {
            val intent = Intent(applicationContext, CartActivity::class.java)

            startActivity(intent)
        }
    }

    companion object {
        fun start(context: Context, productsDataItems: ProductVariation?,save_amount:String,specifications:ArrayList<ProductSpecificationNew>) {
            val intent = Intent(context, ProductDescriptionActivity::class.java)
            intent.putExtra("productDetail", productsDataItems)
            intent.putExtra("specifications",specifications)
            intent.putExtra("save_amount", save_amount)


            context.startActivity(intent)
        }
    }
}
