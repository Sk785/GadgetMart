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
import com.gadgetmart.databinding.TermsLayoutBinding
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.*
import kotlinx.android.synthetic.main.layout_toolbar_back_activity.view.*

class WebActivity : BaseActivity<TermsLayoutBinding>() {
    private var binding: TermsLayoutBinding? = null

    override fun getContentView(): Int {
        return R.layout.terms_layout
    }

    override fun init(binding: TermsLayoutBinding) {
        this.binding = binding
        binding.toolbar.toolbar_title_text_view.text = intent.extras!!.getString("type")
        toolbar_cart_icon.visibility = View.GONE
        toolbar_ohnik_image_view.visibility = View.GONE
        setListeners(binding)
        Log.e("data", intent.extras!!.getString("data"))

        binding.webview.webViewClient = MyWebViewClient(this)
        binding.webview.loadUrl(intent.extras!!.getString("data").toString())

//        binding.webview.setHtml(
//            intent.extras!!.getString("data").toString(),
//             HtmlHttpImageGetter(binding.webview)
//        );

    }
    private fun setListeners(binding: TermsLayoutBinding) {
        binding.toolbarID.backIcon.setOnClickListener { finish() }
    }

    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }
}