package com.gadgetmart.ui.support

import android.content.Intent
import android.net.Uri
import android.view.View
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.SupportCenterLayoutBinding
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import kotlinx.android.synthetic.main.support_center_layout.*


class SupportCenterActivity : BaseActivity<SupportCenterLayoutBinding>() {
    private var binding: SupportCenterLayoutBinding? = null
    override fun getContentView(): Int {
        return R.layout.support_center_layout

    }

    override fun init(binding: SupportCenterLayoutBinding) {
        this.binding = binding
        binding.toolbar.toolbar_title_text_view.text = "Support center"
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE
        whtsup_no.setText(intent.extras!!.getString("whtsup"))
        phone_no.setText(intent.extras!!.getString("phone"))
        email_txt.setText(intent.extras!!.getString("email"))

        setListeners(binding)
    }

    private fun setListeners(binding: SupportCenterLayoutBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
        whtsup_view.setOnClickListener {
            openIntent(whtsup_no.text.toString())
        }
        phone_view.setOnClickListener {
            val u =
                Uri.parse("tel:" + phone_no.text.toString())
            val i = Intent(Intent.ACTION_DIAL, u)
            try {
                startActivity(i)
            } catch (s: SecurityException) {
            }
        }
        email_view.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data =
                Uri.parse("mailto:" + email_txt.text.toString()) // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    fun openIntent(number: String) {
        try {
            val text = "" // Replace with your message.
            val toNumber =
                number // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://api.whatsapp.com/send?phone=$toNumber&text=$text")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}