package com.anywhere.adelivery.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.anywhere.adelivery.R
import com.anywhere.adelivery.ui.fragment.ConfirmationFragment
import com.anywhere.adelivery.ui.fragment.UserDetailFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false);

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        onNavigationDrawerItemSelected(0)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        onNavigationDrawerItemSelected(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun onNavigationDrawerItemSelected(position: Int) {
        // update the main content by replacing fragments
        val fragment: Fragment
        val fragmentManager = supportFragmentManager // For AppCompat use getSupportFragmentManager
        when (position) {
            0 -> fragment = UserDetailFragment()
//            1 -> fragment = MyFragment2()
            else -> fragment = ConfirmationFragment()
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
