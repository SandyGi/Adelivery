package com.anywhere.adelivery.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anywhere.adelivery.R
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import kotlinx.android.synthetic.main.app_header_layout.*
import kotlinx.android.synthetic.main.fragment_my_detail.view.*

class MyDetailFragment : Fragment() {

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
        view.btnNext.setOnClickListener {
            activityContext.displaySelectedScreen(activityContext.SUBMIT_FRAGMENT)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_my_detail)

    }

}// Required empty public constructor
