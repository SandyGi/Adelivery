package com.anywhere.adelivery.ui.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anywhere.adelivery.AdeliveryApplication
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.entity.Status
import com.anywhere.adelivery.data.request.ConfirmationRequest
import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import com.anywhere.adelivery.utils.ORDER_ID
import com.anywhere.adelivery.viewmodel.ConfirmationViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_confirmation.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
    class ConfirmationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var confirmationViewModel: ConfirmationViewModel

    private var activityContext = RegistrationActivity()
    private var orderId = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activityContext = context as RegistrationActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_confirmation, container, false)

        confirmationViewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmationViewModel::class.java)

        orderId = arguments!!.getString(ORDER_ID)
        Log.e("Confirmation fragment ", "$orderId")

        observeConfirmationStatus()
        view.btnConfirm.setOnClickListener {

            confirmationViewModel.doConfirmation(doConfirmation(view))

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_confirmation_detail)

    }

    private fun doConfirmation(view: View): ConfirmationRequest {
        return ConfirmationRequest(orderId, AdeliveryApplication.prefHelper!!.userId)
    }

    private fun observeConfirmationStatus() {
        confirmationViewModel.response.observe(this, Observer { response ->

            if (response != null && response.status == Status.SUCCESS) {
                var intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activityContext.finish()
            } else {
                Toast.makeText(activityContext, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
