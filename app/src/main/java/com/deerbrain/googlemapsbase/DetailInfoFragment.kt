package com.deerbrain.googlemapsbase

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.deerbrain.googlemapsbase.Parcel.ParcelDetailInfo
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class DetailInfoFragment : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /*    val mDataRecieved: String = getArguments().getString(TITLE, "defaultTitle")*/
        val builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
        val inflater: LayoutInflater = getActivity()!!.getLayoutInflater()
        val view: View = inflater.inflate(R.layout.detail_dialog, null)
        var ownerName = view.findViewById<TextView>(R.id.ownerName)
        var mailingAddress = view.findViewById<TextView>(R.id.mailingAddress)
        var calculatedAcres = view.findViewById<TextView>(R.id.calculatedAcres)
        ownerName.setText(data.ownerName.toString())
        mailingAddress.setText(data.address1.toString())
        calculatedAcres.setText(data.calcAcres.toString())
        setCancelable(false)
        var cancel = view.findViewById<Button>(R.id.close).setOnClickListener({

            map.removePolyLine()
            dismiss()
        })
        var storeShape = view.findViewById<Button>(R.id.storeShapeSize).setOnClickListener({
            map.drawPolyLine(data.latLng)

            dismiss()
        })
        builder.setView(view)
        val dialog: Dialog = builder.create()
        dialog.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        return dialog
    }

    companion object {
        const val TITLE = "dataKey"
        lateinit var data: ParcelDetailInfo
        lateinit var map: MapsActivity
        fun newInstance(dataToShow: ParcelDetailInfo, context: MapsActivity): DetailInfoFragment {
            val frag = DetailInfoFragment()
            data = dataToShow
            this.map = context

            return frag
        }
    }
}