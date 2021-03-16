package com.gadgetmart.ui.product_details

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.ui.my_address.AddAddressItems
import com.gadgetmart.ui.my_address.AddressPresenter
import com.gadgetmart.ui.my_address.AddressResult
import com.gadgetmart.ui.my_address.MyAddressContract
import com.gadgetmart.ui.subcategory.AddressAdapterListener
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.custom.CustomProgressDialog
import com.gadgetmart.util.permission.PermissionSupport
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class AddressBottomSheet : BottomSheetDialogFragment(), View.OnClickListener,
    MyAddressContract, AddressAdapterListener,PermissionSupport.Callback {
    companion object {

        const val TAG = "AddressBottomSheet"
        fun newInstance(context: Context): AddressBottomSheet {
            return AddressBottomSheet()
        }
    }

    private var addressesRecyclerView: RecyclerView? = null
    private var submitTextView: TextView? = null
    private var useCurrentLocationTextView: TextView? = null
    private var closeDialogImageView: ImageView? = null
    private var pinCodeEditText: EditText? = null
    private var mListener: ItemClickListener? = null
    lateinit var myAddresses: ArrayList<AddAddressItems>
    private var addressPresenter: AddressPresenter? = null
    private var addressAdapter: SavedAddressesAdapter? = null
    private var permissionSupport: PermissionSupport? = null
    val RC_ACCESS_LOCATION_PERM = 10006
    private lateinit var myDialog: CustomProgressDialog
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var locationManager: LocationManager? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.address_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionSupport = getPermissionSupport()

        submitTextView = view.findViewById(R.id.submit_text_view) as TextView
        addressesRecyclerView =
            view.findViewById(R.id.available_addresses_recycler_view) as RecyclerView
        pinCodeEditText = view.findViewById(R.id.pin_code_edit_text) as EditText
        useCurrentLocationTextView = view.findViewById(R.id.use_current_location_text_view) as TextView
        closeDialogImageView = view.findViewById(R.id.close_dialog_image_view) as ImageView
        closeDialogImageView!!.setOnClickListener(this)
        useCurrentLocationTextView!!.setOnClickListener {
            makeLocationPermissionRequest()
        }
        myDialog = CustomProgressDialog(activity!!)

        submitTextView!!.setOnClickListener {
            mListener?.onItemClick("submit",pinCodeEditText!!.text.toString())
        }
        pinCodeEditText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(pinCodeEditText!!.text.toString().length>0){
                    submitTextView!!.setBackgroundResource(R.drawable.shape_rectangle_filled_red_rounded_corners)

                }else{
                    submitTextView!!.setBackgroundResource(R.drawable.shape_rectangle_filled_grey_rounded_corners)

                }
            }
        })
        initRecyclerView()
        initPresenter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context
        } else {
            throw RuntimeException(
                "$context must implement ItemClickListener"
            )
        }
    }

    private fun initRecyclerView() {
        myAddresses = ArrayList()
        val addressLayoutManager = LinearLayoutManager(context)

        addressLayoutManager.orientation = LinearLayoutManager.VERTICAL

        addressesRecyclerView?.layoutManager = addressLayoutManager

        addressAdapter = SavedAddressesAdapter(context, ArrayList(), this)
        addressesRecyclerView?.adapter = addressAdapter
    }

    private fun initPresenter() {
        addressPresenter = AddressPresenter(context as Activity, this)
        addressPresenter!!.getAddresses(ContextUtils.getAuthToken(context), true)
    }

    interface ItemClickListener {
        fun onItemClick(item: String?,code:String?)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.close_dialog_image_view -> mListener?.onItemClick("close","")


        }
    }

    override fun onAddressDataFound(
        addressResult: AddressResult?,
        message: String?,
        apiFlag: Int?
    ) {
        if (addressResult == null) {
            addressesRecyclerView?.visibility = View.GONE
        }
        addressAdapter?.updateList(addressResult?.addresses)
    }

    override fun onAddressDataNotFound(message: String?) {
        message?.let { showToast(it) }
    }

    override fun onAddressSetAsDefault(message: String?) {

    }

    override fun onAddressNotSetAsDefault(message: String?) {
    }

    override fun hasNetwork(): Boolean {
        return true
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showToast(stringResourceId: Int) {
        showToast(getString(stringResourceId))
    }

    override fun onAdapterItemTapped(adapterItem: Int?, categoryName: String?, itemPosition: Int?) {

    }

    override fun onAdapterItemClick(id: Int, name: String,pinCode:String) {
        pinCodeEditText!!.setText(name)
        submitTextView!!.setBackgroundResource(R.drawable.shape_rectangle_filled_red_rounded_corners)

    }

    override fun onSetAsDefaultMenuItemSelected(addressId: String?) {
    }

    fun getPermissionSupport(): PermissionSupport {

        if (permissionSupport == null) {
            permissionSupport = PermissionSupport.Builder()
                .setActivity(activity!!)
                .setCallback(this)
                .build()
        }

        return permissionSupport!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {

        val isConsumed = getPermissionSupport()
            .consumePermissionResult(requestCode, permissions, grantResults)
        if (!isConsumed) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onPermissionsAllowed(who: Int) {
        reportPermissionResult(true,who)
    }

    override fun onPermissionsNotAllowed() {
        reportPermissionResult(false,-1)

    }
    fun makeLocationPermissionRequest() {
        permissionSupport!!.checkAndRequestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), false, RC_ACCESS_LOCATION_PERM
        )
    }


    private fun reportPermissionResult(isAllowed: Boolean, who: Int) {
        if (isAllowed) {
            if (who == RC_ACCESS_LOCATION_PERM) {
                myDialog.dialogShow()
                showCurrentLocationData()
            }
        }
    }

    fun getAddressFromLatLong(latitude: Double, longitude: Double) {
        val addresses: List<Address>
        val geocoder = Geocoder(activity!!, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            stopLocationUpdate()
            myDialog.dialogDismiss()
            pinCodeEditText!!.setText(addresses[0].postalCode)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun showCurrentLocationData() {
        // Create persistent LocationManager reference
        locationManager =
            activity!!.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
        try {
            // Request location updates
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch (ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }
    }

    //define the listener
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            longitude = location.longitude
            latitude = location.latitude
            getAddressFromLatLong(latitude, longitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun stopLocationUpdate() {
        locationManager?.removeUpdates(locationListener)
    }

}