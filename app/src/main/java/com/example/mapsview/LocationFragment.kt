package com.example.mapsview

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.mapsview.model.Location
import com.example.mapsview.utils.Connection.linkImages
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationFragment : DialogFragment() {
    private lateinit var location: Location
    private lateinit var titleId: TextView
    private lateinit var titleName: TextView
    private lateinit var titleGaelicName: TextView
    private lateinit var titleLocation: TextView
    private lateinit var titleType: TextView
    private lateinit var titleLatitude: TextView
    private lateinit var titleLongitude: TextView
    private lateinit var imgShow: ImageView

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables", "MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location, container, false)
        imgShow = view.findViewById(R.id.img)
        titleId = view.findViewById(R.id.title_id)
        titleLocation = view.findViewById(R.id.title_location)
        titleName = view.findViewById(R.id.title_name)
        titleGaelicName = view.findViewById(R.id.gaelic_name)
        titleLatitude = view.findViewById(R.id.title_lat)
        titleLongitude = view.findViewById(R.id.title_long)
        titleType = view.findViewById(R.id.title_type)
        view.findViewById<View>(R.id.img).setOnClickListener { view1: View? -> dismiss() }
        view.findViewById<View>(R.id.btn_close).setOnClickListener { view1: View? -> dismiss() }
        location = MainActivity.currentLocationItem!!
        this.titleId.text="ID:  " + location.id
        this.titleType.text="Place Type ID:  " + location.place_type_id
        this.titleName.setText("Name:  " + location.name)
        this.titleGaelicName.setText("Gaelic Name:   " + location.gaelic_name)
        this.titleLongitude.setText("Longitude: " + location!!.longitude)
        this.titleLatitude.setText("Latitude:  " + location.latitude)
        this.titleLocation.setText("Location:  " + location.location)


            imgShow.setImageResource(linkImages[Random().nextInt(linkImages.size)])


        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditFlowerFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): LocationFragment {
            return LocationFragment()
        }
    }
}