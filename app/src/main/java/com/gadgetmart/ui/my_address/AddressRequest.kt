package com.gadgetmart.ui.my_address

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class AddressRequest {


    @SerializedName("id")
    @Expose
    private var id: String? = null


    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("address1")
    @Expose
    private var address1: String? = null

    @SerializedName("address2")
    @Expose
    private var address2: String? = null

    @SerializedName("city")
    @Expose
    private var city: String? = null

    @SerializedName("state")
    @Expose
    private var state: String? = null

    @SerializedName("country")
    @Expose
    private var country: String? = null

    @SerializedName("phone")
    @Expose
    private var phoneNumber: String? = null

    @SerializedName("zip")
    @Expose
    private var zip: String? = null

    @SerializedName("address_type")
    @Expose
    private var addressType: String? = null

    @SerializedName("latitude")
    @Expose
    private var latitude: String? = null

    @SerializedName("landmark")
    @Expose
    private var landmark: String? = null

    @SerializedName("longitude")
    @Expose
    private var longitude: String? = null

    @SerializedName("country_code")
    @Expose
    private var countryCode: String? = null


    /*
     * Getters and Setters
     */


    fun getCountryCode(): String? {
        return countryCode
    }

    fun setCountryCode(countryCode: String?): AddressRequest {
        this.countryCode = countryCode
        return this
    }


    fun getName(): String? {
        return name
    }

    fun setName(name: String?): AddressRequest {
        this.name = name
        return this
    }

    fun getAddress1(): String? {
        return address1
    }

    fun setAddress1(address1: String?): AddressRequest {
        this.address1 = address1
        return this
    }

    fun getAddress2(): String? {
        return address2
    }

    fun setAddress2(address2: String?): AddressRequest {
        this.address2 = address2
        return this
    }

    fun getCity(): String? {
        return city
    }

    fun setCity(city: String?): AddressRequest {
        this.city = city
        return this
    }

    fun getPhoneNumber(): String? {
        return phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String?): AddressRequest {
        this.phoneNumber = phoneNumber
        return this
    }

    fun getAddressType(): String? {
        return addressType
    }

    fun setAddressType(addressType: String?): AddressRequest {
        this.addressType = addressType
        return this
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String?): AddressRequest {
        this.latitude = latitude
        return this
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String?): AddressRequest {
        this.longitude = longitude
        return this
    }

    fun getLandmark(): String? {
        return landmark
    }

    fun setLandmark(landmark: String?): AddressRequest {
        this.landmark = landmark
        return this
    }

    fun getState(): String? {
        return state
    }

    fun setState(state: String?): AddressRequest {
        this.state = state
        return this
    }

    fun getCountry(): String? {
        return country
    }

    fun setCountry(country: String?): AddressRequest {
        this.country = country
        return this
    }

    fun getZip(): String? {
        return zip
    }

    fun setZip(zip: String?): AddressRequest {
        this.zip = zip
        return this
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?): AddressRequest {
        this.id = id
        return this
    }
}