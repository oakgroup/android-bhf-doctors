package com.active.orbit.baseapp.design.activities.engine

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.active.orbit.baseapp.R
import androidx.drawerlayout.widget.DrawerLayout
import com.active.orbit.baseapp.core.broadcast.BaseBroadcast
import com.active.orbit.baseapp.core.broadcast.BroadcastHost
import com.active.orbit.baseapp.core.utils.Utils
import com.active.orbit.baseapp.design.components.MenuComponent
import com.active.orbit.baseapp.design.widgets.BaseImageButton
import com.active.orbit.baseapp.design.widgets.BaseImageView
import com.active.orbit.baseapp.design.widgets.BaseTextView
import com.google.android.material.navigation.NavigationView

/**
 * Abstract activity that exposes useful methods
 *
 * @author omar.brugna
 */
abstract class AbstractActivity : AppCompatActivity(), DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener, BroadcastHost {

    private lateinit var mRootView: ViewGroup

    private var mToolbar: Toolbar? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null


    private var mProgressView: ViewGroup? = null

    private var isProgressViewVisible = false

    private val identifier = ++identifierCount
    private val mBroadcasts = ArrayList<BaseBroadcast>()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackPress()
        }
    }

    companion object {
        private var identifierCount = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        onSetContentView()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        onSetContentView()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        onSetContentView()
    }

    private fun onSetContentView() {

        mRootView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

        if (mRootView is DrawerLayout) {
            mDrawerLayout = mRootView as DrawerLayout
            mDrawerLayout?.addDrawerListener(this)
            mRootView = mDrawerLayout?.getChildAt(0) as ViewGroup
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar != null) {
            mToolbar = toolbar
        } else {
            if (getToolbarResource() != null) {
                // inflate toolbar
                mToolbar = LayoutInflater.from(this).inflate(getToolbarResource()!!, mRootView, false) as Toolbar
                mRootView.addView(mToolbar, 0)
            }
        }
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            val leftIcon = mToolbar?.findViewById<BaseImageButton>(R.id.toolbarLeftIcon)
            val menuComponent = mToolbar?.findViewById<MenuComponent>(R.id.toolbarMenuComponent)

            leftIcon?.setOnClickListener {
                onNavigateUp()
            }
            menuComponent?.setOnClickListener {
                onMenu()
            }
        }

        mNavigationView = findViewById(R.id.navigationView)
        if (mNavigationView != null) {
            mNavigationView?.setNavigationItemSelectedListener(this)
            showNavigationViewData()
            val navigationClose = mNavigationView?.getHeaderView(0)?.findViewById<BaseImageView>(R.id.closeDrawer)

            navigationClose?.setOnClickListener {
                handleBackPress()
            }
        }

        // inflate progress view
        mProgressView = LayoutInflater.from(this).inflate(R.layout.progress, mRootView, false) as ViewGroup
        mRootView.addView(mProgressView)
        mProgressView?.setOnClickListener { /* invalidate clicks on below views */ }
        mProgressView?.visibility = ViewGroup.GONE
    }

    override fun onDestroy() {
        broadcastUnregister()
        super.onDestroy()
    }

    /**
     * Get the toolbar resource layout for the specific activity
     * override to customize, or return null to avoid the toolbar to be inflated
     *
     * @return the toolbar resource layout
     */
    protected open fun getToolbarResource(): Int? {
        return R.layout.toolbar
    }

    protected fun setToolbarTitle(resourceId: Int) {
        setToolbarTitle(getString(resourceId))
    }

    @SuppressLint("CutPasteId")
    protected fun setToolbarTitle(title: String) {
        mToolbar?.findViewById<BaseTextView>(R.id.toolbarTitle)?.visibility = View.VISIBLE
        mToolbar?.findViewById<BaseTextView>(R.id.toolbarTitle)?.text = title
        mToolbar?.findViewById<BaseImageView>(R.id.toolbarLogo)?.visibility = View.GONE
    }

    protected fun setToolbarTitleUppercase() {
        mToolbar?.findViewById<BaseTextView>(R.id.toolbarTitle)?.isAllCaps = true
    }

    protected fun setToolbarTitleColor(colour: Int) {
        mToolbar?.findViewById<BaseTextView>(R.id.toolbarTitle)?.setTextColor(ContextCompat.getColor(this, colour))
    }

    protected fun showBackButton() {
        mToolbar?.findViewById<BaseImageButton>(R.id.toolbarLeftIcon)?.visibility = View.VISIBLE
    }

    protected fun hideBackButton() {
        mToolbar?.findViewById<BaseImageButton>(R.id.toolbarLeftIcon)?.visibility = View.INVISIBLE
    }

    protected fun showTitle() {
        mToolbar?.findViewById<BaseTextView>(R.id.toolbarTitle)?.visibility = View.VISIBLE
    }

    protected fun hideTitle() {
        mToolbar?.findViewById<BaseTextView>(R.id.toolbarTitle)?.visibility = View.INVISIBLE
    }

    protected fun showLogo() {
        mToolbar?.findViewById<BaseImageView>(R.id.toolbarLogo)?.visibility = View.VISIBLE
    }

    protected fun hideLogo() {
        mToolbar?.findViewById<BaseImageView>(R.id.toolbarLogo)?.visibility = View.INVISIBLE
    }

    protected fun showMenuComponent() {
        mToolbar?.findViewById<MenuComponent>(R.id.toolbarMenuComponent)?.visibility = View.VISIBLE
    }

    protected fun hideMenuComponent() {
        mToolbar?.findViewById<MenuComponent>(R.id.toolbarMenuComponent)?.visibility = View.INVISIBLE
    }

    protected fun setMenuIconLight() {
        mToolbar?.findViewById<MenuComponent>(R.id.toolbarMenuComponent)?.setIconLight()
    }

    protected fun hideToolbar() {
        mToolbar?.visibility = View.GONE
    }

    protected fun showToolbar() {
        mToolbar?.visibility = View.VISIBLE
    }

    fun setToolbarBackgroundColour(@ColorInt colour: Int) {
        mToolbar?.setBackgroundColor(colour)
    }

    fun setToolbarBackButtonImage(image: Int) {
        mToolbar?.findViewById<BaseImageView>(R.id.toolbarLeftIcon)?.setImageResource(image)

    }

    override fun onNavigateUp(): Boolean {
        handleBackPress()
        return true
    }

    open fun onMenu() {
        if (mDrawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            mDrawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            mDrawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    private fun showNavigationViewData() {
        val userName = mNavigationView?.getHeaderView(0)?.findViewById<BaseTextView>(R.id.userFullNameText)

        userName?.text = "Username"

        showNavigationAvatar()
    }

    private fun showNavigationAvatar() {
        val cardView = mNavigationView?.getHeaderView(0)?.findViewById<CardView>(R.id.cardView)
        val avatar = mNavigationView?.getHeaderView(0)?.findViewById<BaseImageView>(R.id.avatar)

        if (cardView != null && avatar != null) {
            avatar.setImageDrawable(ContextCompat.getDrawable(this@AbstractActivity, R.drawable.ic_app_logo_alpha))
        }
    }

    open fun handleBackPress() {
        if (mDrawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            mDrawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onNavigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDrawerStateChanged(state: Int) {
        // override to customize
    }

    override fun onDrawerSlide(view: View, offset: Float) {
        // override to customize
    }

    override fun onDrawerClosed(view: View) {
        // override to customize
    }

    override fun onDrawerOpened(view: View) {
        // override to customize
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.help -> {
            }
            R.id.privacy -> {
            }
            R.id.logout -> {
            }

        }
        mDrawerLayout?.closeDrawer(GravityCompat.START)
        return false
    }

    protected fun showKeyboard() {
        Utils.showKeyboard(currentFocus)
    }

    protected fun hideKeyboard() {
        Utils.hideKeyboard(currentFocus)
    }

    fun showProgressView() {
        if (mProgressView != null) {
            mProgressView!!.visibility = ViewGroup.VISIBLE
            isProgressViewVisible = true
        }
    }

    fun hideProgressView() {
        if (isProgressViewVisible && mProgressView != null) {
            mProgressView!!.visibility = ViewGroup.GONE
            isProgressViewVisible = false
        }
    }

    override fun broadcastRegister(broadcast: BaseBroadcast) {
        mBroadcasts.add(broadcast)
    }

    override fun broadcastUnregister() {
        for (broadcast in mBroadcasts) broadcast.unregister()
        mBroadcasts.clear()
    }

    override fun broadcastIdentifier(): Int {
        return identifier
    }

    override fun getContext(): Context? {
        return this
    }
}