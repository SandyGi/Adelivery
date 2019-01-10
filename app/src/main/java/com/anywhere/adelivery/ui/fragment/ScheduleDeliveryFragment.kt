package com.anywhere.adelivery.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anywhere.adelivery.R
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_schedule_delivery.*
import kotlinx.android.synthetic.main.fragment_schedule_delivery.view.*
import java.text.SimpleDateFormat
import java.util.*


class ScheduleDeliveryFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_schedule_delivery, container, false)
        val calendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            tiDeliveryDate.setText(sdf.format(calendar.time))
        }

        view.iconCalender.setOnClickListener {
            DatePickerDialog(
                activity, date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        view.btnSubmit.setOnClickListener {
            activityContext.displaySelectedScreen(activityContext.CONFIRMATION_FRAGMENT)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity!!.title = activity!!.getString(R.string.str_submit_detail)
    }

}// Required empty public constructor
