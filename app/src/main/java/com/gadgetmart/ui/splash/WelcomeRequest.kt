package com.gadgetmart.ui.splash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WelcomeRequest {
    @SerializedName("google_token")
    @Expose
    private var google_token: String? = null

    @SerializedName("facebook_token")
    @Expose
    private var facebook_token: String? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("photo")
    @Expose
    private var photo: String? = null

    @SerializedName("device_token")
    @Expose
    private var deviceToken: String? = null

    fun getGoogleToken(): String? {
        return google_token
    }

    fun setGoogleToken(google_token: String?): WelcomeRequest {
        this.google_token = google_token
        return this
    }

    fun getFacebookToken(): String? {
        return facebook_token
    }

    fun setFacebookToken(facebook_token: String?): WelcomeRequest {
        this.facebook_token = facebook_token
        return this
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?): WelcomeRequest {
        this.email = email
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): WelcomeRequest {
        this.name = name
        return this
    }

    fun getPhoto(): String? {
        return name
    }

    fun setPhoto(photo: String?): WelcomeRequest {
        this.photo = photo
        return this
    }

    fun getDeviceToken(): String? {
        return deviceToken
    }

    fun setDeviceToken(deviceToken: String?): WelcomeRequest {
        this.deviceToken = deviceToken
        return this
    }

}