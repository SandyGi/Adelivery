package com.anywhere.adelivery.ui.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.widget.LinearLayout
import android.widget.Toast
import com.anywhere.adelivery.BuildConfig
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.ui.adapter.OfferAdapter
import com.anywhere.adelivery.utils.Customization
import com.anywhere.adelivery.viewmodel.ExistingUserViewModel
import com.anywhere.adelivery.viewmodel.OfferViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_offer.*
import kotlinx.android.synthetic.main.app_header_layout.*
import kotlinx.android.synthetic.main.custom_edt_dialog.view.*
import kotlinx.android.synthetic.main.offer_view_pager_layout.*
import java.util.*
import javax.inject.Inject


class OfferActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var offerViewModel: OfferViewModel
    private lateinit var existingUserViewModel: ExistingUserViewModel


    var customization = Customization()
    private var currentPage = 0
    private var NUM_PAGES = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)

        offerViewModel = ViewModelProviders.of(this, viewModelFactory).get(OfferViewModel::class.java)
        existingUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExistingUserViewModel::class.java)
        init()
    }

    @SuppressLint("InflateParams")
    private fun init() {
        observeOfferLoadingStatus()
        offerViewModel.loadData()

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        pageIndicatorView.setRadius(5 * density)

//        // Auto start of viewpager
        val handler = Handler()
        val update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            vpOffer.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 2000, 3000)
        btnScheduleDelivery.setOnClickListener {

            val layoutInflater = LayoutInflater.from(this)
            val view = layoutInflater.inflate(R.layout.custom_edt_dialog, null)

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setView(view)

            alertDialog.setCancelable(false)
                .setPositiveButton("Next") { _, _ ->
                    if (!view.userInputDialog.text.equals("") && view.userInputDialog.length() == 10) {
//                        val intent = Intent(this, RegistrationActivity::class.java)
//                        startActivity(intent)
                        observeExistingUserStatus()
                        existingUserViewModel.loadData()
                    } else {
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

    private fun observeOfferLoadingStatus() {
        offerViewModel.response.observe(this,
            Observer { response ->
                if (response != null && response.status == Status.SUCCESS) {
                    txtMobileNumber.visibility = GONE
                    NUM_PAGES = response.data!!.size
                    vpOffer.adapter = OfferAdapter(this@OfferActivity, response.data)

                    pageIndicatorView.setViewPager(vpOffer)
                    vpOffer.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)


                } else {
                    if ((response != null && response.status == Status.ERROR) && BuildConfig.DEBUG) {
                        Log.e("get users error", response.error.toString())
                    }
                }
            })
    }

    private fun observeExistingUserStatus(){
        existingUserViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS){
                Log.e("get users success", response.data.toString())
            }else{
                Log.e("get users error", response!!.error.toString())
            }
        })
    }
}
