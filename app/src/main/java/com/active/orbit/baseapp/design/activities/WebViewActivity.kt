package com.active.orbit.baseapp.design.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.active.orbit.baseapp.core.routing.enums.Extra
import com.active.orbit.baseapp.core.utils.Constants
import com.active.orbit.baseapp.databinding.ActivityWebViewBinding
import com.active.orbit.baseapp.design.activities.engine.BaseActivity
import com.active.orbit.baseapp.design.utils.UiUtils
import java.util.*

class WebViewActivity : BaseActivity() {

    private lateinit var binding: ActivityWebViewBinding

    var webViewUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideToolbar()

        if (activityBundle.getString(Extra.WEB_VIEW_URL.key) != null) {
            webViewUrl = activityBundle.getString(Extra.WEB_VIEW_URL.key, Constants.EMPTY)!!
        }


        prepare()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun prepare() {

        if (webViewUrl != Constants.EMPTY) {
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webViewClient = MyWebViewClient(this)
            binding.webView.loadUrl(webViewUrl!!)

        } else {
            UiUtils.showLongToast(this, "Something went wrong")
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    class MyWebViewClient internal constructor(private val activity: BaseActivity) : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            UiUtils.showShortToast(activity, "Got Error! $error")
        }
    }
}
