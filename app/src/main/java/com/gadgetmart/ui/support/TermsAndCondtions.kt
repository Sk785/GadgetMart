package com.gadgetmart.ui.support

import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.CondtionsLayoutBinding
import kotlinx.android.synthetic.main.condtions_layout.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*
import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter

class TermsAndCondtions: BaseActivity<CondtionsLayoutBinding>() {
    private var binding: CondtionsLayoutBinding? = null

    override fun getContentView(): Int {
        return R.layout.condtions_layout
    }

    override fun init(binding: CondtionsLayoutBinding) {
        this.binding = binding
        binding.toolbar.toolbar_title_text_view.text = intent.extras!!.getString("type")
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE
        setListeners(binding)
        Log.e("data", intent.extras!!.getString("data"))



        binding.webview.setHtml(
            intent.extras!!.getString("data").toString(),
             HtmlHttpImageGetter(binding.webview)
        );

    }
    private fun setListeners(binding: CondtionsLayoutBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
    }


}