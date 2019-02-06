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
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import com.anywhere.adelivery.data.request.UserDetails
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import com.anywhere.adelivery.ui.activity.SCHEDULE_DELIVERY_FRAGMENT
import com.anywhere.adelivery.utils.CommonMethod
import com.anywhere.adelivery.viewmodel.CreateUserViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.app_header_layout.view.*
import kotlinx.android.synthetic.main.fragment_my_detail.view.*
import javax.inject.Inject

class MyDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var userDetailViewModel: CreateUserViewModel
    private var dialog: ProgressDialog? = null

    private var activityContext = RegistrationActivity()
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activityContext = context as RegistrationActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_detail, container, false)

        userDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateUserViewModel::class.java)

        dialog = ProgressDialog(activity, R.style.MyTheme)
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog!!.setTitle("Loading")
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.setIndeterminate(true)
        dialog!!.setCanceledOnTouchOutside(false)

        observeExistingUserStatus(view)
        view.txtMobileNumber.visibility = View.GONE
        view.btnNext.setOnClickListener {

            if (!view.edtFullName.text.toString().equals("")) {
                if (view.edtMobileNumber.text.toString().length == 10) {
                    if (!view.edtEmail.text.toString().equals("")) {
                        if (!view.edtCity.text.toString().equals("")) {
                            dialog!!.show()
                            userDetailViewModel.createdUserDetail(setUserDetail(view))
                        } else {
                            CommonMethod.showCustomToast(activityContext, "Please enter city.")
                        }
                    } else {
                        CommonMethod.showCustomToast(activityContext, "Please enter email address.")
                    }
                } else {
                    CommonMethod.showCustomToast(activityContext, "Please enter valid mobile number.")
                }
            } else {
                CommonMethod.showCustomToast(activityContext, "Please enter your name.")
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_my_detail)

    }

    private fun setUserDetail(view: View): CreatedUserDetailRequest {
        var userDetail = UserDetails(
            view.edtFullName.text.toString(),
            view.edtMobileNumber.text.toString(),
            view.edtEmail.text.toString(),
            view.edtCity.text.toString()
        )
        return CreatedUserDetailRequest(AdeliveryApplication.prefHelper!!.userId, userDetail)
    }

    private fun observeExistingUserStatus(view: View) {
        userDetailViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {
                if (response.data!!.data.equals("Done")) {
                    activityContext.displaySelectedScreen(SCHEDULE_DELIVERY_FRAGMENT, null)
                    AdeliveryApplication.prefHelper!!.getUserName = view.edtFullName.text.toString()
                }
            } else {
                Toast.makeText(activityContext, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
            dialog!!.dismiss()
        })
    }
}// Required empty public constructor
