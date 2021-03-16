package com.gadgetmart.ui.checkout

import android.content.Context
import android.content.Intent
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.OrderConfirmationActivityBinding
import com.gadgetmart.ui.home.HomeActivity

class OrderConfirmationActivity : BaseActivity<OrderConfirmationActivityBinding>() {

    override fun getContentView(): Int = R.layout.order_confirmation_activity

    companion object {
        const val EXTRA_ORDER_ID = "intent_extra_order_id"

        fun start(context: Context, orderId: String) {
            context.startActivity(
                Intent(context, OrderConfirmationActivity::class.java)
                    .apply { putExtra(EXTRA_ORDER_ID, orderId) }
            )
        }
    }

    override fun init(binding: OrderConfirmationActivityBinding) {
        if (intent != null) {
            binding.orderPlaceMessageTextView.text = getString(
                R.string.label_order_placed_success_msg,
                intent.getStringExtra(EXTRA_ORDER_ID)
            )
        }
        binding.continueShoppingButton.setOnClickListener { HomeActivity.start(this) }
    }

    override fun onBackPressed() {

    }
}
