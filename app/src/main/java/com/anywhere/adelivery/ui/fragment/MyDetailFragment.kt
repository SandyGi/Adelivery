package com.anywhere.adelivery.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.request.CreatedUserDetailRequest
import com.anywhere.adelivery.data.request.UserDetails
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import com.anywhere.adelivery.utils.PreferencesManager
import com.anywhere.adelivery.viewmodel.UserDetailViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_my_detail.*
import kotlinx.android.synthetic.main.fragment_my_detail.view.*
import javax.inject.Inject

class MyDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var userDetailViewModel: UserDetailViewModel

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

        userDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailViewModel::class.java)
        observeExistingUserStatus()
        view.btnNext.setOnClickListener {

            userDetailViewModel.createdUserDetail(setUserDetail(view))

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
        return CreatedUserDetailRequest(view.edtMobileNumber.text.toString(), userDetail)
    }

    private fun observeExistingUserStatus() {
        userDetailViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {
                if (response.data!!.data.equals("Done")) {
                    activityContext.displaySelectedScreen(activityContext.SUBMIT_FRAGMENT)
                }
            } else {
                Toast.makeText(activityContext, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}// Required empty public constructor
