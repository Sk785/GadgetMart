package com.gadgetmart.ui.product_details

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.OfferListingLayoutBinding
import com.gadgetmart.ui.category.OffersModel
import com.gadgetmart.ui.support.TermsAndCondtions
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*

class OfferListingActivity: BaseActivity<OfferListingLayoutBinding>(),OfferItemClick {
    private lateinit var binding: OfferListingLayoutBinding
    lateinit var offers: ArrayList<OffersModel>

    override fun getContentView(): Int {
        return R.layout.offer_listing_layout

    }

    override fun init(binding: OfferListingLayoutBinding) {
        this.binding=binding
        offers= ArrayList<OffersModel>()
        binding.toolbar.toolbar_title_text_view.text ="Offers"
        binding.toolbar.toolbar_ohnik_image_view.visibility = View.GONE
        binding.toolbar.toolbar_cart_item_count.visibility = View.GONE
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE
        setListner()
        val subCategoriesLayoutManager = LinearLayoutManager(this)
        binding.listing.layoutManager = subCategoriesLayoutManager
        offers= intent.getSerializableExtra("offers") as ArrayList<OffersModel>
        binding.listing.adapter=OfferListingAdapter(this,offers,this)

    }

    fun setListner(){
        binding.toolbar.back_icon.setOnClickListener {
            finish()
        }
    }

    override fun onOfferItemClick(pos: Int) {
        val i = Intent(applicationContext, TermsAndCondtions::class.java)
        i.putExtra("type", offers[pos].name)
        i.putExtra("data", offers[pos].terms)

        startActivity(i)
    }
}