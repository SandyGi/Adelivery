package com.anywhere.adelivery.ui.fragment


import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
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
import com.anywhere.adelivery.email.SendMail
import com.anywhere.adelivery.listener.ChangeFragmentListener
import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.utils.CommonMethod
import com.anywhere.adelivery.utils.Constants
import com.anywhere.adelivery.utils.ORDER_ID
import com.anywhere.adelivery.viewmodel.ConfirmationViewModel
import com.google.android.gms.common.internal.service.Common
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

    private var orderId = ""
    private var pickUpLocation = ""
    private var dropLocation = ""
    private lateinit var changeFragmentListener: ChangeFragmentListener
    private var dialog: ProgressDialog? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            changeFragmentListener = context as ChangeFragmentListener
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_confirmation, container, false)

        confirmationViewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmationViewModel::class.java)

        orderId = arguments!!.getString(ORDER_ID)
        pickUpLocation = arguments!!.getString(Constants.EXTRA_PICK_UP_ADDRESS)
        dropLocation = arguments!!.getString(Constants.EXTRA_DROP_ADDRESS)
        Log.e("Confirmation fragment ", "$orderId")

        observeConfirmationStatus()
        view.btnConfirm.setOnClickListener {
            if (!view.tiEdtOTP.text.toString().equals("") && view.tiEdtOTP.text.toString().length == 4) {
                confirmationViewModel.doConfirmation(doConfirmation(view))
            }else{
                CommonMethod.showCustomToast(activity!!, "Please enter valid OTP.")
            }
        }
        dialog = ProgressDialog(activity)
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog!!.setTitle("Loading");
        dialog!!.setMessage("Loading. Please wait...");
        dialog!!.setIndeterminate(true);
        dialog!!.setCanceledOnTouchOutside(false);
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
                SendMailTask().execute()
            } else {
                Toast.makeText(activity, "Server Side Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class SendMailTask() : AsyncTask<String, Unit, Unit>() {

        override fun onPreExecute() {
            super.onPreExecute()
            dialog!!.show()
        }

        override fun doInBackground(vararg params: String?) {

            try {
                val sm = SendMail(Constants.ADMIN_EMAIL_ID, Constants.ADMIN_PASSWORD)

                //Executing sendmail to send email
                sm.sendMail(
                    "Order Booking Id $orderId",
                    "You have received order $orderId from ${AdeliveryApplication.prefHelper!!.getUserName}, " +
                            "please contact ${AdeliveryApplication.prefHelper!!.userId} and pick up location :$pickUpLocation and drop location : $dropLocation.",
                    Constants.ADMIN_EMAIL_ID, Constants.ADMIN_RECIPIENT
                )

            } catch (e: Exception) {
                Log.e("MailApp", "Could not send email", e)
//                Toast.makeText(this@OfferActivity, "Email was not sent. $e" , Toast.LENGTH_LONG).show()
            }

        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            dialog!!.dismiss()
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

    }
}
