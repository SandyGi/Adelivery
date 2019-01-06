package com.anywhere.adelivery.ui.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anywhere.adelivery.R
import com.anywhere.adelivery.ui.activity.HomeActivity
import com.anywhere.adelivery.ui.activity.RegistrationActivity
import kotlinx.android.synthetic.main.fragment_confirmation.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ConfirmationFragment : Fragment() {


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
        var view = inflater.inflate(R.layout.fragment_confirmation, container, false)
        view.btnConfirm.setOnClickListener {
            var intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activityContext.finish()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = activity!!.getString(R.string.str_confirmation_detail)

    }

}
