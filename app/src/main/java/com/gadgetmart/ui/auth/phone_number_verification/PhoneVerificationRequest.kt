package com.gadgetmart.ui.auth.phone_number_verification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PhoneVerificationRequest {

    @SerializedName("country_code")
    @Expose
    private var countryCode: String? = null

    @SerializedName("phone")
    @Expose
    private var phoneNumber: String? = null

    @SerializedName("device_token")
    @Expose
    private var deviceToken: String? = null

    /*
     * Getters and Setters
     */
    fun getCountryCode(): String? {
        return countryCode
    }

    fun setCountryCode(countryCode: String?): PhoneVerificationRequest {
        this.countryCode = countryCode
        return this
    }

    fun getPhoneNumber(): String? {
        return phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String?): PhoneVerificationRequest {
        this.phoneNumber = phoneNumber
        return this
    }

    fun getDeviceToken(): String? {
        return deviceToken
    }

    fun setDeviceToken(deviceToken: String?): PhoneVerificationRequest {
        this.deviceToken = deviceToken
        return this
    }

}