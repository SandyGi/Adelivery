package com.anywhere.adelivery.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import com.anywhere.adelivery.R
import com.anywhere.adelivery.ui.fragment.*
import com.anywhere.adelivery.utils.CommonMethod
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_toolbar_layout.*
import kotlinx.android.synthetic.main.header_mobile_layout.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val SUBMIT_FRAGMENT = 100
    val CONFIRMATION_FRAGMENT = 101
    val ORDER_DETAIL_FRAGMENT = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        displaySelectedScreen(R.id.nav_my_order)
//        txtMobileNumber.visibility = GONE
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

        displaySelectedScreen(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun displaySelectedScreen(position: Int) {
        // update the main content by replacing fragments
        val fragment: Fragment = when (position) {
            R.id.nav_my_detail -> ProfileFragment()
            R.id.nav_my_order -> MyOrderFragment()
            R.id.nav_setting -> SettingFragment()
            R.id.nav_contact_us -> ContactUsFragment()
            R.id.nav_term_condition -> TermAndConditionFragment()
            SUBMIT_FRAGMENT -> SubmitFragment()
            CONFIRMATION_FRAGMENT -> ConfirmationFragment()
            ORDER_DETAIL_FRAGMENT -> OrderDetailFragment()
            else -> MyOrderFragment()

        }
//        if (position != R.id.nav_my_detail) txtMobileNumber.visibility = VISIBLE else this.txtMobileNumber.visibility =
//                GONE

        val fragmentManager = supportFragmentManager // For AppCompat use getSupportFragmentManager

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}
