package com.gadgetmart.ui.auth.registeration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationRequest {

    @SerializedName("country_code")
    @Expose
    private var countryCode: String? = ""

    @SerializedName("phone")
    @Expose
    private var phoneNumber: String? = ""

    @SerializedName("email")
    @Expose
    private var emailId: String? = ""

    @SerializedName("name")
    @Expose
    private var userName: String? = ""

    @SerializedName("gender")
    @Expose
    private var gender: String? = ""

    @SerializedName("photo")
    @Expose
    private var profilePhoto: String? = null

    @SerializedName("country")
    @Expose
    private var userCountry: String? = ""

    @SerializedName("google_token")
    @Expose
    private var google_token: String? = ""

    @SerializedName("facebook_token")
    @Expose
    private var facebook_token: String? = ""

    @SerializedName("device_token")
    @Expose
    private var deviceToken: String? = ""



    /*
     * Getters and Setters
     */
    fun getGoogleToken(): String? {
        return google_token
    }

    fun setGoogleToken(google_token: String?): RegistrationRequest {
        this.google_token = google_token
        return this
    }

    fun getFacebookToken(): String? {
        return facebook_token
    }

    fun setFacebookToken(facebook_token: String?): RegistrationRequest {
        this.facebook_token = facebook_token
        return this
    }


    fun getCountryCode(): String? {
        return countryCode
    }

    fun setCountryCode(countryCode: String?): RegistrationRequest {
        this.countryCode = countryCode
        return this
    }

    fun getPhoneNumber(): String? {
        return phoneNumber
    }

    fun setPhoneNumber(phoneNumber: String?): RegistrationRequest {
        this.phoneNumber = phoneNumber
        return this
    }

    fun getEmailId(): String? {
        return emailId
    }

    fun setEmailId(emailId: String?): RegistrationRequest {
        this.emailId = emailId
        return this
    }

    fun getUserName(): String? {
        return userName
    }

    fun setUserName(userName: String?): RegistrationRequest {
        this.userName = userName
        return this
    }

    fun getGender(): String? {
        return gender
    }

    fun setGender(gender: String?): RegistrationRequest {
        this.gender = gender
        return this
    }

    fun getProfilePhoto(): String? {
        return profilePhoto
    }

    fun setProfilePhoto(profilePhoto: String?): RegistrationRequest {
        this.profilePhoto = profilePhoto
        return this
    }

    fun getUserCountry(): String? {
        return userCountry
    }

    fun setUserCountry(userCountry: String?): RegistrationRequest {
        this.userCountry = userCountry
        return this
    }

    fun getDeviceToken(): String? {
        return deviceToken
    }

    fun setDeviceToken(deviceToken: String?): RegistrationRequest {
        this.deviceToken = deviceToken
        return this
    }

}