package com.anywhere.adelivery.ui.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.viewmodel.UserDetailViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_profile.view.*
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var userDetailViewModel: UserDetailViewModel
    private var activityContext = HomeActivity()
    private var dialog: ProgressDialog? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activityContext = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        userDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailViewModel::class.java)

        observeOrderDetailStatus(view)
        view.imgLogo.visibility =View.GONE
        view.txtMobileNumber.text = AdeliveryApplication.prefHelper!!.userId
        dialog = ProgressDialog(activity, R.style.MyTheme)
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
//        dialog!!.setTitle("Loading")
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.setIndeterminate(true)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
        userDetailViewModel.getUserDetail(AdeliveryApplication.prefHelper!!.userId)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_my_detail)
    }

    private fun observeOrderDetailStatus(view: View) {
        userDetailViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {
                view.txtFullName.text = response.data!!.data.full_name
                view.txtMobile.text = response.data.data.contact_number
                view.txtEmail.text = response.data.data.email
                view.txtCity.text = response.data.data.city
            } else {
                Toast.makeText(activity, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
            dialog!!.dismiss()
        })
    }
}