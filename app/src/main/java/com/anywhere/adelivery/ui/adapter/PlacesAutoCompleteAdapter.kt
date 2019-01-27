package com.anywhere.adelivery.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import com.anywhere.adelivery.R
import com.anywhere.adelivery.data.model.PlaceAutocomplete
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*
import java.util.concurrent.TimeUnit

class PlacesAutoCompleteAdapter(
    private val mContext: Context, private var mGoogleApiClient: GoogleApiClient?,
    private val mBounds: LatLngBounds, private val mPlaceFilter: AutocompleteFilter
) : RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder>(), Filterable {
    private var mResultList: ArrayList<PlaceAutocomplete>? = null
    fun setGoogleApiClient(googleApiClient: GoogleApiClient?) {
        if (googleApiClient == null || !googleApiClient.isConnected) {
            mGoogleApiClient = null
        } else {
            mGoogleApiClient = googleApiClient
        }
    }

    fun setResultList(resultList: ArrayList<PlaceAutocomplete>) {
        this.mResultList = resultList
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.v(TAG, "inside performFiltering constraint::" + constraint!!)
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint)
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList
                        results.count = mResultList!!.size
                    }
                    Log.v(TAG, "inside mResultList::" + mResultList!!)
                }
                return results
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {

            }


        }
    }

    private fun getAutocomplete(constraint: CharSequence?): ArrayList<PlaceAutocomplete>? {
        if (mGoogleApiClient!!.isConnected) {
            Log.i("", "Starting autocomplete query for: " + constraint!!)

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            val results = Places.GeoDataApi
                .getAutocompletePredictions(
                    mGoogleApiClient!!, constraint.toString(),
                    mBounds, mPlaceFilter
                )

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            val autocompletePredictions = results
                .await(60, TimeUnit.SECONDS)

            // Confirm that the query completed successfully, otherwise return null
            val status = autocompletePredictions.status
            if (!status.isSuccess) {
                Toast.makeText(
                    mContext, "Error contacting API: " + status.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("", "Error getting autocomplete prediction API call: " + status.toString())
                autocompletePredictions.release()
                return null
            }

            Log.i(
                "", "Query completed. Received " + autocompletePredictions.count
                        + " predictions."
            )

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            val iterator = autocompletePredictions.iterator()
            val resultList = ArrayList<PlaceAutocomplete>(autocompletePredictions.count)
            while (iterator.hasNext()) {
                val prediction = iterator.next()
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                //resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),prediction.getDescription()));
                val primaryText = prediction.getPrimaryText(null) as String
                val fullText = prediction.getFullText(null) as String
                resultList.add(PlaceAutocomplete(prediction.placeId, primaryText, fullText))
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release()

            return resultList
        }
        Log.e("", "Google API client is not connected for autocomplete query.")
        return null
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PredictionHolder {
        val layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = layoutInflater.inflate(R.layout.location_item_layout, viewGroup, false)
        return PredictionHolder(convertView)
    }

    override fun onBindViewHolder(mPredictionHolder: PredictionHolder, i: Int) {
        mPredictionHolder.textViewLocName.setText(mResultList!![i].placePrimaryText)
        mPredictionHolder.textViewLocDetails.setText(mResultList!![i].description)

    }

    override fun getItemCount(): Int {
        return if (mResultList != null)
            mResultList!!.size
        else
            0
    }

    fun getItem(position: Int): PlaceAutocomplete? {
        return if (mResultList != null && mResultList!!.size > 0) {
            mResultList!![position]
        } else null
    }

    inner class PredictionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewLocName: TextView = itemView.findViewById(R.id.textview_loc_name)
        var textViewLocDetails: TextView = itemView.findViewById(R.id.textview_loc_details)

        init {
            var face = Typeface.createFromAsset(
                mContext.assets,
                "fonts/AvenirNext-Medium.ttf"
            )
            textViewLocName.typeface = face

            face = Typeface.createFromAsset(
                mContext.assets,
                "fonts/AvenirNext-UltraLight.ttf"
            )
            textViewLocDetails.typeface = face
        }

    }

    companion object {
        private val TAG = "PlacesAutoCompleteAdapt"
    }

}