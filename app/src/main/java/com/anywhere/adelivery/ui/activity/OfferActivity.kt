package com.anywhere.adelivery.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.Toast
import com.anywhere.adelivery.R
import com.anywhere.adelivery.ui.adapter.OfferAdapter
import com.anywhere.adelivery.utils.Customization
import com.anywhere.adelivery.utils.DummyData
import kotlinx.android.synthetic.main.activity_offer.*
import kotlinx.android.synthetic.main.app_header_layout.*
import kotlinx.android.synthetic.main.custom_edt_dialog.view.*
import kotlinx.android.synthetic.main.offer_view_pager_layout.*
import java.util.*


class OfferActivity : AppCompatActivity() {

    var customization = Customization()
    private var currentPage = 0
    private var NUM_PAGES = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)
        init()
    }

    @SuppressLint("InflateParams")
    private fun init() {

        txtMobileNumber.visibility = GONE
        vpOffer.adapter = OfferAdapter(this@OfferActivity, DummyData.getOfferList())

        pageIndicatorView.setViewPager(vpOffer)
        vpOffer.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val density = resources.displayMetrics.density

        NUM_PAGES = DummyData.getOfferList().size
        //Set circle indicator radius
        pageIndicatorView.setRadius(5 * density)

//        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            vpOffer.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 2000, 3000)

        btnScheduleDelivery.setOnClickListener {

            val layoutInflater = LayoutInflater.from(this)
            val view = layoutInflater.inflate(R.layout.custom_edt_dialog, null)

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setView(view)

            alertDialog.setCancelable(false)
                .setPositiveButton("Next") { _, _ ->
                    if (!view.userInputDialog.text.equals("") && view.userInputDialog.length()== 10){
                        val intent = Intent(this, RegistrationActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
                    }

                }
                .setNegativeButton("Cancel") { _, _ ->

                }
            alertDialog.show()

        }
        updateIndicator()
    }

    private fun updateIndicator() {

        pageIndicatorView.setAnimationType(customization.animationType);
        pageIndicatorView.setOrientation(customization.orientation);
        pageIndicatorView.setRtlMode(customization.rtlMode);
        pageIndicatorView.setInteractiveAnimation(customization.isInteractiveAnimation);
        pageIndicatorView.setAutoVisibility(customization.isAutoVisibility);

    }
}
