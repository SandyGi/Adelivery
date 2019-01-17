package com.anywhere.adelivery.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.listener.ChangeFragmentListener
import com.anywhere.adelivery.ui.fragment.*
import com.anywhere.adelivery.utils.CommonMethod
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_toolbar_layout.*
import kotlinx.android.synthetic.main.header_mobile_layout.*

const val ORDER_DETAIL_FRAGMENT = 102

class HomeActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ChangeFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        txtMobileNumber.text = AdeliveryApplication.prefHelper!!.userId
        nav_view.setNavigationItemSelectedListener(this)
        displaySelectedScreen(R.id.nav_my_order, null)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
//            super.onBackPressed()
            CommonMethod.showExitDialog(this)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        displaySelectedScreen(item.itemId, null)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onChangeFragmentListener(type: Int, bundle: Bundle) {
        displaySelectedScreen(type, bundle)
    }

    fun displaySelectedScreen(position: Int, bundle: Bundle?) {
        // update the main content by replacing fragments
        val fragment: Fragment = when (position) {
            R.id.nav_my_detail -> ProfileFragment()
            R.id.nav_my_order -> MyOrderFragment()
            R.id.nav_contact_us -> ContactUsFragment()
            R.id.nav_term_condition -> TermAndConditionFragment()
            SCHEDULE_DELIVERY_FRAGMENT -> ScheduleDeliveryFragment()
            CONFIRMATION_FRAGMENT -> ConfirmationFragment()
            ORDER_DETAIL_FRAGMENT -> OrderDetailFragment()
            else -> MyOrderFragment()

        }
        if (bundle != null) {
            fragment.arguments = bundle
        }
        val fragmentManager = supportFragmentManager // For AppCompat use getSupportFragmentManager

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }


}
