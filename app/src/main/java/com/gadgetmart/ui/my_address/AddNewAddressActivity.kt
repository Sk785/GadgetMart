package com.gadgetmart.ui.my_address

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.ActivityAddNewAddressBinding
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.custom.CustomProgressDialog
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class AddNewAddressActivity : BaseActivity<ActivityAddNewAddressBinding>(), MyAddressContract,
    CountriesContract, DialogAdapterListener {

    lateinit var binding: ActivityAddNewAddressBinding
    lateinit var addressPresenter: AddressPresenter
    private var countriesPresenter: CountriesPresenter? = null
    private var countryDialogAdapter: DialogListAdapter? = null
    private var stateDialogAdapter: StateDialogListAdapter? = null
    private lateinit var myDialog: CustomProgressDialog
    private var countries: ArrayList<Country>? = null
    private var states: ArrayList<State>? = null
    private var cities: ArrayList<City>? = null
    private var type: String? = ""
    private var addressType: String? = ""
    private var selectedCountryId: String? = ""
    private var selectedStateId: String? = ""
    private var id: String? = ""
    private var mDialog: AlertDialog? = null
    private var countryDialog: Dialog? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var locationManager: LocationManager? = null
    private var countryCode: String? = "+91"
    private var countryNameCode: String? = "IN"
    private var countryName: String? = "India"

    override fun getContentView(): Int {
        return R.layout.activity_add_new_address
    }

    override fun init(binding: ActivityAddNewAddressBinding) {
        this.binding = binding
        getIntentData()
        myDialog = CustomProgressDialog(this)
        addressPresenter = AddressPresenter(this, this)
        countriesPresenter = CountriesPresenter(this, this)
        fetchCountries()
        binding.toolbarID.toolbarBackIcon.setOnClickListener { finish() }
        binding.toolbarID.toolbarExtraIcon.visibility = View.GONE
        binding.toolbarID.toolbarTitleTextView.text = "Address"
        setListeners(binding)
    }

    private fun fetchCountries() {
        myDialog.dialogShow()
        countriesPresenter?.getCountries(ContextUtils.getAuthToken(this), false)
    }

    private fun fetchStates() {
        selectedCountryId?.let {
            countriesPresenter?.getStates(
                ContextUtils.getAuthToken(this),
                it, false
            )
        }
    }

    private fun fetchCities() {
        selectedStateId?.let {
            countriesPresenter?.getCities(
                ContextUtils.getAuthToken(this),
                it, false
            )
        }
    }

    private fun getIntentData() {
        if (intent.extras != null) {
            binding.nameEditText.setText(intent.extras!!.getString("name"))
            binding.houseBuildingNoEditText.setText(intent.extras!!.getString("address1"))
            binding.roadAreaColonyEditText.setText(intent.extras!!.getString("address2"))
            binding.cityEditText.setText(intent.extras!!.getString("city"))
            binding.stateEditText.setText(intent.extras!!.getString("state"))
            binding.countryEditText.setText(intent.extras!!.getString("country"))
            binding.phoneNumberEditText.setText(intent.extras!!.getString("phone_number"))
            binding.pinCodeEditText.setText(intent.extras!!.getString("zipcode"))

            val cc = intent.extras!!.getString("country_code");
            if (cc != null) {
                if (cc.isNotEmpty()) {
                    binding.countryCodeEditText.setText(intent.extras!!.getString("country_code"))
                }
            }
            type = intent.extras!!.getString("type")
            addressType = intent.extras!!.getString("address_type")
            if (addressType == "Work") {
                binding.workAddressRadioButton.isChecked = true
                binding.homeAddressRadioButton.isChecked = false
            } else if (addressType == "Home") {
                binding.homeAddressRadioButton.isChecked = true
                binding.workAddressRadioButton.isChecked = false
            }
            id = intent.extras!!.getInt("id").toString()
        }
    }

    private fun getCountryCode(binding: ActivityAddNewAddressBinding) {
        countryNameCode = binding.ccpCountryCode.selectedCountryNameCode
        countryName = binding.ccpCountryCode.selectedCountryName
        countryCode = binding.ccpCountryCode.selectedCountryCodeWithPlus
        countryCode.let {
            binding.countryCodeEditText.setText(it)
        }
    }

    private fun setListeners(binding: ActivityAddNewAddressBinding) {

        binding.countryCodeEditText.setOnClickListener {
            binding.ccpCountryCode.launchCountrySelectionDialog(countryNameCode)
        }

        binding.ccpCountryCode.setOnCountryChangeListener {
            getCountryCode(binding)
        }


        binding.swipeToRefresh?.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = true
            countries?.clear()
            fetchCountries()
        }

        binding.swipeToRefresh?.setColorSchemeColors(
            Color.rgb(255, 93, 94),
            Color.GREEN,
            Color.BLUE
        )

        binding.useCurrentLocationLayout.setOnClickListener { makeLocationPermissionRequest() }

        binding.countryEditText.setOnClickListener {

            countryDialog = Dialog(this)
            countryDialog?.setContentView(R.layout.dialog_list_layout)
            countryDialog?.setCancelable(true)

            val dialogTitleTextView =
                countryDialog?.findViewById(R.id.dialog_title_text_view) as TextView
            val recyclerView =
                countryDialog?.findViewById(R.id.dialog_recycler_view) as RecyclerView
            val etDialogSearch =
                countryDialog?.findViewById(R.id.etDialogSearch) as EditText

            dialogTitleTextView.text = getString(R.string.text_select_country)
            val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
            recyclerView.layoutManager = mLayoutManager

            countryDialogAdapter = DialogListAdapter(this, countries, this)
            recyclerView.adapter = countryDialogAdapter
            countryDialog?.show()

            etDialogSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty()) {
                        val searchCountryList:ArrayList<Country> = ArrayList()
                        if (countries!!.size > 0) {
                            for (i in 0 until countries!!.size) {
                                if (countries!!.get(i).name!!.contains(s.toString(),true)){
                                    searchCountryList.add(countries!!.get(i))
                                }
                            }
                            if(searchCountryList.size>0){
                                countryDialogAdapter!!.notifyList(searchCountryList)
                            }
                        }
                    }else{
                        countryDialogAdapter!!.notifyList(countries!!)
                    }
                }

            })


//            val mBuilder = AlertDialog.Builder(this, StyledAlertDialog.getStyleRes())
//            mBuilder.setTitle("Select country")
//            mBuilder.setAdapter(countryAdapter) { dialog, which ->
//                selectedCountryId = countries?.get(which)?.id
//                binding.countryEditText.setText(countries?.get(which)?.name)
//                fetchStates()
//                dialog.dismiss()
//            }
            // Set the neutral/cancel button click listener
//            mBuilder.setNeutralButton("Cancel") { dialog, _ ->
            // Do something when click the neutral button
//                dialog.cancel()
//            }
//
//            mDialog = mBuilder.create()
//            mDialog!!.show()
        }

        binding.stateEditText.setOnClickListener {
            if (selectedCountryId.isNullOrEmpty()) {
                showToast("Please select country first")
                return@setOnClickListener
            }

            countryDialog = Dialog(this)
            countryDialog?.setContentView(R.layout.dialog_list_layout)
            countryDialog?.setCancelable(true)

            val dialogTitleTextView =
                countryDialog?.findViewById(R.id.dialog_title_text_view) as TextView
            val recyclerView =
                countryDialog?.findViewById(R.id.dialog_recycler_view) as RecyclerView

            dialogTitleTextView.text = getString(R.string.text_select_state)

            val etDialogSearch =
                countryDialog?.findViewById(R.id.etDialogSearch) as EditText

            val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
            recyclerView.layoutManager = mLayoutManager

            stateDialogAdapter = StateDialogListAdapter(this, states, this)
            recyclerView.adapter = stateDialogAdapter
            countryDialog?.show()


            etDialogSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty()) {
                        val searchCountryList:ArrayList<State> = ArrayList()
                        if (states!!.size > 0) {
                            for (i in 0 until states!!.size) {
                                if (states!!.get(i).name!!.contains(s.toString(),true)){
                                    searchCountryList.add(states!!.get(i))
                                }
                            }
                            if(searchCountryList.size>0){
                                stateDialogAdapter!!.updateList(searchCountryList)
                            }
                        }
                    }else{
                        stateDialogAdapter!!.updateList(states!!)
                    }
                }

            })


        }

        binding.cityEditText.setOnClickListener {
//            if (selectedStateId.isNullOrEmpty()) {
//                showToast("Please select country and state first")
//                return@setOnClickListener
//            }
//            if (cities.isNullOrEmpty()) {
//                binding.cityEditText.isFocusableInTouchMode = true
//            } else {
//                binding.cityEditText.isFocusableInTouchMode = false
//                countryDialog = Dialog(this)
//                countryDialog?.setContentView(R.layout.dialog_list_layout)
//                countryDialog?.setCancelable(true)
//
//                val dialogTitleTextView =
//                    countryDialog?.findViewById(R.id.dialog_title_text_view) as TextView
//                val recyclerView =
//                    countryDialog?.findViewById(R.id.dialog_recycler_view) as RecyclerView
//
//                dialogTitleTextView.text = getString(R.string.text_select_city)
//
//                val mLayoutManager: RecyclerView.LayoutManager =
//                    LinearLayoutManager(applicationContext)
//                recyclerView.layoutManager = mLayoutManager
//
//                cityDialogAdapter = CityDialogListAdapter(this, cities, this)
//                recyclerView.adapter = cityDialogAdapter
//                countryDialog?.show()
//            }
////            val mBuilder = AlertDialog.Builder(this, StyledAlertDialog.getStyleRes())
////            mBuilder.setTitle("Select city")
////            mBuilder.setAdapter(cityAdapter) { dialog, which ->
////                binding.cityEditText.setText(cities?.get(which)?.name)
////                dialog.dismiss()
////            }
//            // Set the neutral/cancel button click listener
////            mBuilder.setNeutralButton("Cancel") { dialog, which ->
//            // Do something when click the neutral button
////                dialog.cancel()
////            }
//
////            mDialog = mBuilder.create()
////            mDialog!!.show()
        }

        if (type == "new") {
            binding.saveAddressButton.setOnClickListener {
                val addressType = if (binding.homeAddressRadioButton.isChecked) "Home" else "Work"
                addressPresenter.validateFields(
                    AddressRequest().setName(binding.nameEditText.text.toString())
                        .setAddress1(binding.houseBuildingNoEditText.text.toString())
                        .setAddress2(binding.roadAreaColonyEditText.text.toString())
                        .setCity(binding.cityEditText.text.toString())
                        .setState(binding.stateEditText.text.toString())
                        .setCountry(binding.countryEditText.text.toString())
                        .setPhoneNumber(binding.phoneNumberEditText.text.toString())
                        .setLandmark(binding.landmarkEditText.text.toString())
                        .setAddressType(addressType)
                        .setLatitude("" + latitude)
                        .setLongitude("" + longitude)
                        .setCountryCode(countryCode)
                        .setZip(binding.pinCodeEditText.text.toString()),
                    ContextUtils.getAuthToken(this)
                )
            }
        } else if (type == "edit") {
            binding.saveAddressButton.setOnClickListener {
                val addressType = if (binding.homeAddressRadioButton.isChecked) "Home" else "Work"
                addressPresenter.updateFields(
                    AddressRequest().setId(id)
                        .setName(binding.nameEditText.text.toString())
                        .setAddress1(binding.houseBuildingNoEditText.text.toString())
                        .setAddress2(binding.roadAreaColonyEditText.text.toString())
                        .setCity(binding.cityEditText.text.toString())
                        .setState(binding.stateEditText.text.toString())
                        .setCountry(binding.countryEditText.text.toString())
                        .setLandmark(binding.landmarkEditText.text.toString())
                        .setPhoneNumber(binding.phoneNumberEditText.text.toString())
                        .setAddressType(addressType)
                        .setLatitude("" + latitude)
                        .setCountryCode(countryCode)
                        .setLongitude("" + longitude)
                        .setZip(binding.pinCodeEditText.text.toString()),
                    ContextUtils.getAuthToken(this)
                )
            }
        }


    }

    private fun showDialog(title: String, items: ArrayList<Country>?) {
    }

    override fun onAddressDataFound(
        addressResult: AddressResult?,
        message: String?,
        apiFlag: Int?
    ) {
        myDialog.dialogDismiss()
        binding.swipeToRefresh?.isRefreshing = false
        when (apiFlag) {
            AppUtil.AddAddressFlag -> {
                val addressItems = addressResult?.address
                Log.e("Address ::: ", " added : " + addressItems?.name)
                val intent = Intent()
                intent.putExtra("address_status", "addressAdded")
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            AppUtil.EditAddressFlag -> {
                val addressItems = addressResult?.address
                Log.e("Address ::: ", " added : " + addressItems?.name)
                val intent = Intent()
                intent.putExtra("address_status", "addressUpdated")
                intent.putExtra("address", addressItems)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onAddressDataNotFound(message: String?) {
        AppUtil.firebaseEvent(applicationContext, "error", "error_events", message!!)

        myDialog.dialogDismiss()
        binding.swipeToRefresh?.isRefreshing = false
        Log.e("Address ::: ", " failed : $message")
    }

    override fun onAddressSetAsDefault(message: String?) {
        myDialog.dialogDismiss()
        message?.let { showToast(it) }
    }

    override fun onAddressNotSetAsDefault(message: String?) {
        myDialog.dialogDismiss()
        message?.let { showToast(it) }
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, AddNewAddressActivity::class.java)
            context!!.startActivity(intent)
        }

    }

    override fun onCountriesFound(countries: ArrayList<Country>?, message: String?, apiFlag: Int?) {
        myDialog.dialogDismiss()
        binding.swipeToRefresh?.isRefreshing = false
        this.countries = countries
//        countryDialogAdapter?.updateList(countries)
//        countryAdapter = CountryDialogAdapter(this, countries, this)
    }

    override fun onCountriesNotFound(message: String?) {
        myDialog.dialogDismiss()
        binding.swipeToRefresh?.isRefreshing = false
    }

    override fun onStatesFound(states: ArrayList<State>?, message: String?, apiFlag: Int?) {
        myDialog.dialogDismiss()
        this.states = states
//        stateDialogAdapter?.updateList(states)
//        stateAdapter = StateDialogAdapter(this, states, this)
//        stateAdapter?.updateList(states)
    }

    override fun onStatesNotFound(message: String?) {
        myDialog.dialogDismiss()
    }

    override fun onCitiesFound(cities: ArrayList<City>?, message: String?, apiFlag: Int?) {
        myDialog.dialogDismiss()
        this.cities = cities
//        cityDialogAdapter?.updateList(cities)
//        cityAdapter = CityDialogAdapter(this, cities, this)
//        cityAdapter?.updateList(cities)
    }

    override fun onCitiesNotFound(message: String?) {
        myDialog.dialogDismiss()
    }

    override fun onAdapterItemClick(type: String, id: String, name: String) {

        when (type) {
            "country" -> {
                selectedCountryId = id
                selectedStateId = null
                binding.countryEditText.setText(name)
                binding.stateEditText.setText("")
                binding.cityEditText.setText("")
                myDialog.dialogShow()
                fetchStates()
            }
            "state" -> {
                selectedStateId = id
                myDialog.dialogShow()
                binding.stateEditText.setText(name)
                binding.cityEditText.setText("")
                fetchCities()
            }
            "city" -> {
                binding.cityEditText.setText(name)
            }
        }
        mDialog?.dismiss()
        countryDialog?.dismiss()
    }

    override fun onPermissionsAllowed(who: Int) {
        reportPermissionResult(true, who)
    }

    override fun onPermissionsNotAllowed() {
        reportPermissionResult(false, -1)
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
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            stopLocationUpdate()
            myDialog.dialogDismiss()
            binding().roadAreaColonyEditText.setText(addresses[0].locality)
//            binding().countryEditText.setText(addresses[0].countryName)
            binding().pinCodeEditText.setText(addresses[0].postalCode)
            selectedCountryId = null
            selectedStateId = null
            binding.countryEditText.text = null
            binding.stateEditText.text = null
            binding.cityEditText.text = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun showCurrentLocationData() {
        // Create persistent LocationManager reference
        locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager?
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
