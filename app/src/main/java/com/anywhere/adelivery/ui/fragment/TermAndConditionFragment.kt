package com.anywhere.adelivery.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_term_and_condition.view.*

class TermAndConditionFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_term_and_condition, container, false)
        view.imgLogo.visibility = View.GONE
        view.txtMobileNumber.text = AdeliveryApplication.prefHelper!!.userId
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_term_amp_condition)
    }

}
