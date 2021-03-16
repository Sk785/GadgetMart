package com.gadgetmart.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.gadgetmart.R
import com.gadgetmart.ui.category.OffersAdapter
import com.gadgetmart.ui.category.PopularGadgetsAdapter
import com.gadgetmart.ui.products.PopularActivity
import com.gadgetmart.ui.products.ProductsActivity
import kotlinx.android.synthetic.main.deals_layout.view.*
import kotlinx.android.synthetic.main.home_layout_items.view.*
import kotlinx.android.synthetic.main.offers_layout.view.*
import java.util.*
import kotlin.collections.ArrayList


class HomeDataAdapter(
    val context: Context?,
    private var sections: ArrayList<DashboardSections>?,
    private val adapterListener: DashboardAdapterListener, var timer:Timer

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_BANNER = 0
    private val TYPE_POPULAR = 1
    private val TYPE_RECENT = 2
    private val TYPE_CUSTOM = 3

    private var viewPool = RecyclerView.RecycledViewPool()
    private var horizontalLayoutManager: LinearLayoutManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var layout = -1
        val viewHolder: RecyclerView.ViewHolder?

            when (viewType) {
                TYPE_BANNER -> {
                    layout = R.layout.deals_layout
                    val view = LayoutInflater
                        .from(context)
                        .inflate(layout, parent, false)
                    viewHolder = BannerViewHolder(view,timer)
                }
                TYPE_POPULAR -> {
                    layout = R.layout.home_layout_items
                    val view = LayoutInflater
                        .from(context)
                        .inflate(layout, parent, false)
                    viewHolder = PopularGadgetsViewHolder(view)
                }
                TYPE_RECENT -> {
                    layout = R.layout.offers_layout
                    val view = LayoutInflater
                        .from(context)
                        .inflate(layout, parent, false)
                    viewHolder = OffersViewHolder(view)
                }
                TYPE_CUSTOM -> {
                    layout = R.layout.home_layout_items
                    val view = LayoutInflater
                        .from(context)
                        .inflate(layout, parent, false)
                    viewHolder = PopularGadgetsViewHolder(view)
                }
                else -> {
                    viewHolder = null
                }
            }
            return viewHolder!!

//        val view = LayoutInflater.from(context)
//            .inflate(R.layout.home_layout_items, parent, false)
    }

    override fun getItemCount(): Int {
        return sections?.size!!
    }

    override fun getItemViewType(position: Int): Int {

        return when (sections!![position].appKey) {
          
            SectionType.BANNER -> TYPE_BANNER
            SectionType.POPULAR -> TYPE_POPULAR

            SectionType.RECENT -> TYPE_RECENT
            SectionType.CUSTOM -> TYPE_CUSTOM
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            TYPE_BANNER -> {

                horizontalLayoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                horizontalLayoutManager!!.initialPrefetchItemCount = sections?.size!!
                val viewHolder = holder as BannerViewHolder
//                adapterListener.onAdapterItemTapped(sections!![position].dealsId, sections!![position].name)
                viewHolder.bindView(sections!![position], adapterListener,context!!,timer)
            }
            TYPE_POPULAR -> {



                    val horizontalDivider = context?.let { ContextCompat.getDrawable(it, R.drawable.line_divider) }
                    val verticalDivider =
                        context?.let { ContextCompat.getDrawable(it, R.drawable.line_divider_vertical) }

                var popularGadgetsLayoutManager:GridLayoutManager
                    popularGadgetsLayoutManager = GridLayoutManager(context, 2)
                popularGadgetsLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int = 1
                }

//                    popularGadgetsLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                        override fun getSpanSize(position: Int): Int = 1
//                    }
                    val viewHolder = holder as PopularGadgetsViewHolder

                    popularGadgetsLayoutManager.initialPrefetchItemCount = sections!![position].variations!!.size
                    viewHolder.itemView.section_recycler_view?.layoutManager = popularGadgetsLayoutManager
                if(sections!![position].variations!!.size>0) {
//                    if (sections!![position].variations!!.size == 1) {
//
//                    } else {
//                        viewHolder.itemView.section_recycler_view?.addItemDecoration(
//                            GridDividerItemDecoration(
//                                horizontalDivider,
//                                verticalDivider,
//                                2
//                            )
//                        )
//                    }

                    viewHolder.itemView.section_recycler_view.apply {
                        adapter = context?.let {
                            PopularGadgetsAdapter(
                                it,
                                sections!![position].variations,
                                adapterListener, sections!![position].name

                            )
                        }
                        setRecycledViewPool(viewPool)
                    }
                    viewHolder.itemView.view_all_gadgets_layout?.setOnClickListener {
                        if(sections!![position].name.equals("Most Popular")){
                            sections!![position].dealsId?.let { it1 ->
                                sections!![position].name?.let { it2 ->
                                    PopularActivity.start(
                                        holder.itemView.context,
                                        it1, it2, false
                                    )
                                }
                            }
                        }else {
                            sections!![position].dealsId?.let { it1 ->
                                sections!![position].name?.let { it2 ->
                                    ProductsActivity.start(
                                        holder.itemView.context,
                                        it1, it2, false
                                    )
                                }
                            }
                        }
//                    adapterListener.onAdapterItemTapped(sections!![position].dealsId, sections!![position].name)
//                    sections!![position].dealsId?.let { it1 ->
//                        ProductsofSubCategory.start(context,
//                            it1, sections!![position].name)
//                    }
                    }
                    viewHolder.bindView(sections!![position], adapterListener)
                }

            }

            TYPE_RECENT -> {
                // Offers

if(sections!![position]!=null) {
    val offersLayoutManager = GridLayoutManager(context, 3)

    offersLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int = 1
    }
    offersLayoutManager.initialPrefetchItemCount = sections?.size!!

    val viewHolder = holder as OffersViewHolder
    viewHolder.itemView.offers_recycler_view?.layoutManager = offersLayoutManager
    viewHolder.itemView.offers_recycler_view.apply {
        adapter = context?.let {
            OffersAdapter(
                it,
                sections!![position].variations,
                adapterListener,
                "Recent History"
            )
        }
        setRecycledViewPool(viewPool)
    }
    viewHolder.itemView.view_all_offers_layout?.setOnClickListener {
        sections!![position].dealsId?.let { it1 ->
            sections!![position].name?.let { it2 ->
                ProductsActivity.start(
                    holder.itemView.context,
                    it1, it2, false
                )
            }
        }

//                    adapterListener.onAdapterItemTapped(sections!![position].dealsId, sections!![position].name)
//                    sections!![position].dealsId?.let { it1 ->
//                        ProductsofSubCategory.start(context,
//                            it1, sections!![position].name)
//                    }
    }
    viewHolder.itemView.view_all_offers_layout.visibility=View.GONE
    viewHolder.bindView(sections!![position], adapterListener)
}

            }

            TYPE_CUSTOM -> {


                    val horizontalDivider = context?.let { ContextCompat.getDrawable(it, R.drawable.line_divider) }
                    val verticalDivider =
                        context?.let { ContextCompat.getDrawable(it, R.drawable.line_divider_vertical) }
                var popularGadgetsLayoutManager:GridLayoutManager
                    popularGadgetsLayoutManager = GridLayoutManager(context, 2)

                popularGadgetsLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int = 1
                }
//                    popularGadgetsLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                        override fun getSpanSize(position: Int): Int = 1
//                    }
                    val viewHolder = holder as PopularGadgetsViewHolder

                    popularGadgetsLayoutManager.initialPrefetchItemCount = sections!![position].variations!!.size
                    viewHolder.itemView.section_recycler_view?.layoutManager = popularGadgetsLayoutManager
                if(sections!![position].variations!!.size>0) {

//                    if (sections!![position].variations!!.size == 1) {
//
//                    } else {
//                        viewHolder.itemView.section_recycler_view?.addItemDecoration(
//                            GridDividerItemDecoration(
//                                horizontalDivider,
//                                verticalDivider,
//                                2
//                            )
//                        )
//                    }
                    viewHolder.itemView.section_recycler_view.apply {
                        adapter = context?.let {
                            PopularGadgetsAdapter(
                                it,
                                sections!![position].variations,
                                adapterListener,sections!![position].name

                            )
                        }
                        setRecycledViewPool(viewPool)
                    }
                    viewHolder.itemView.view_all_gadgets_layout?.setOnClickListener {


                        sections!![position].dealsId?.let { it1 ->
                            sections!![position].name?.let { it2 ->
                                ProductsActivity.start(
                                    holder.itemView.context,
                                    it1, it2, false
                                )
                            }
                        }

//                    adapterListener.onAdapterItemTapped(sections!![position].dealsId, sections!![position].name)
//                    sections!![position].dealsId?.let { it1 ->
//                        ProductsofSubCategory.start(context,
//                            it1, sections!![position].name)
//                    }
                    }
                    viewHolder.bindView(sections!![position], adapterListener)
                }



            }
        }
    }

    fun updateList(sections: ArrayList<DashboardSections>?,timer:Timer) {
        this.sections = sections
        this.timer=timer
        notifyDataSetChanged()
    }

    class BannerViewHolder(itemView: View, timer:Timer) : RecyclerView.ViewHolder(itemView) {
        fun bindView(sectionData: DashboardSections, adapterListener: DashboardAdapterListener,context:Context, timer:Timer) {
            var dealsAdapter: DemoInfiniteAdapter? =
                sectionData.banners?.let { DemoInfiniteAdapter(itemView.context, sectionData!!.banners, true) }
            itemView.dashboard_view_pager?.adapter = dealsAdapter
           // itemView.dots_indicator?.attachToViewPager(itemView.dashboard_view_pager)
            //val dots: ArrayList<ImageView>
            if(sectionData!!.banners!!.size!=0) {
                var dots: ArrayList<ImageView> = ArrayList<ImageView>()
                dots.clear()
                itemView.dots_indicator.removeAllViews()
                for (i in 0 until sectionData!!.banners!!.size) {
                    dots.add(ImageView(context))
                    dots.get(i).setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.non_active_dot
                        )
                    )
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(8, 0, 8, 0)
                    itemView.dots_indicator.addView(dots.get(i), params)
                }

                dots.get(0).setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.active_dot
                    )
                )

                itemView.dashboard_view_pager?.addOnPageChangeListener(object :
                    ViewPager.OnPageChangeListener {

                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {

                    }

                    override fun onPageSelected(position: Int) {
                        for (i in 0 until sectionData!!.banners!!.size) {
                            dots.get(i).setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.non_active_dot
                                )
                            )
                        }
                        var pos = position
                        if (position > sectionData!!.banners!!.size) {
                            pos = position - 2
                        }
                        Log.e("pos", "" + position)
                        if (pos == 0) {
                            dots.get(pos).setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.active_dot
                                )
                            )
                        } else {
                            dots.get(pos - 1).setImageDrawable(
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.active_dot
                                )
                            )
                        }


                    }

                })
            }
//            try {
//                val mScroller =
//                    ViewPager::class.java.getDeclaredField("mScroller")
//                mScroller.isAccessible = true
//                val scroller = SpeedSlowScroller(context)
//                mScroller[ itemView.dashboard_view_pager] = scroller
//                timer.scheduleAtFixedRate(SliderTimer( itemView.dashboard_view_pager, sectionData.banners?.size!!,
//                    context as Activity?
//                ), 6000, 12000)
//            } catch (ignored: Exception) {
//            }
        }


    }

    class PopularGadgetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            productVariation: DashboardSections?,
            adapterListener: DashboardAdapterListener
        ) {
            itemView.section_title_text_view.text = productVariation?.name
        }
    }

    class OffersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(
            gadget: DashboardSections?,
            adapterListener: DashboardAdapterListener
        ) {
            itemView.offers_title_text_view.text = gadget?.name
        }
    }
}