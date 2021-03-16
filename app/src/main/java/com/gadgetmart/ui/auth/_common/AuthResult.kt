package com.gadgetmart.ui.auth._common

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class AuthResult {
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("email")
    @Expose
    private var email: String? = null
    @SerializedName("country")
    @Expose
    private var country: String? = null
    @SerializedName("country_code")
    @Expose
    private var countryCode: String? = null
    @SerializedName("phone")
    @Expose
    private var phone: String? = null
    @SerializedName("photo")
    @Expose
    private var photo: String? = null
    @SerializedName("gender")
    @Expose
    private var gender: String? = null
    @SerializedName("google_token")
    @Expose
    private var googleToken: String? = null
    @SerializedName("facebook_token")
    @Expose
    private var facebookToken: String? = null
    @SerializedName("instagram_token")
    @Expose
    private var instagramToken: String? = null
    @SerializedName("linked_in_token")
    @Expose
    private var linkedInToken: String? = null
    @SerializedName("device_token")
    @Expose
    private var deviceToken: String? = null
    @SerializedName("notifications")
    @Expose
    private var notifications: String? = null
    @SerializedName("enabled")
    @Expose
    private var enabled: String? = null
    @SerializedName("api_token")
    @Expose
    private var apiToken: Any? = null
    @SerializedName("role")
    @Expose
    private var role: String? = null
    @SerializedName("created_at")
    @Expose
    private var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    private var updatedAt: String? = null
    @SerializedName("auth_token")
    @Expose
    private var authToken: String? = null



    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getCountry(): String? {
        return country
    }

    fun setCountry(country: String?) {
        this.country = country
    }

    fun getCountryCode(): String? {
        return countryCode
    }

    fun setCountryCode(countryCode: String?) {
        this.countryCode = countryCode
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }

    fun getPhoto(): String? {
        return photo
    }

    fun setPhoto(photo: String?) {
        this.photo = photo
    }

    fun getGender(): String? {
        return gender
    }

    fun setGender(gender: String?) {
        this.gender = gender
    }

    fun getGoogleToken(): String? {
        return googleToken
    }

    fun setGoogleToken(googleToken: String?) {
        this.googleToken = googleToken
    }

    fun getFacebookToken(): String? {
        return facebookToken
    }

    fun setFacebookToken(facebookToken: String?) {
        this.facebookToken = facebookToken
    }

    fun getInstagramToken(): String? {
        return instagramToken
    }

    fun setInstagramToken(instagramToken: String?) {
        this.instagramToken = instagramToken
    }

    fun getLinkedInToken(): String? {
        return linkedInToken
    }

    fun setLinkedInToken(linkedInToken: String?) {
        this.linkedInToken = linkedInToken
    }

    fun getDeviceToken(): String? {
        return deviceToken
    }

    fun setDeviceToken(deviceToken: String?) {
        this.deviceToken = deviceToken
    }

    fun getNotifications(): String? {
        return notifications
    }

    fun setNotifications(notifications: String?) {
        this.notifications = notifications
    }

    fun getEnabled(): String? {
        return enabled
    }

    fun setEnabled(enabled: String?) {
        this.enabled = enabled
    }

    fun getApiToken(): Any? {
        return apiToken
    }

    fun setApiToken(apiToken: Any?) {
        this.apiToken = apiToken
    }

    fun getRole(): String? {
        return role
    }

    fun setRole(role: String?) {
        this.role = role
    }

    fun getCreatedAt(): String? {
        return createdAt
    }

    fun setCreatedAt(createdAt: String?) {
        this.createdAt = createdAt
    }

    fun getUpdatedAt(): String? {
        return updatedAt
    }

    fun setUpdatedAt(updatedAt: String?) {
        this.updatedAt = updatedAt
    }

    fun getAuthToken(): String? {
        return authToken
    }

    fun setAuthToken(authToken: String?) {
        this.authToken = authToken
    }

}