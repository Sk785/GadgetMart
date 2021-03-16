package com.gadgetmart.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.gadgetmart.base.User
import com.gadgetmart.ui.auth._common.AuthResult
import com.google.gson.Gson

class PreferenceManager() {

    private val TAG: String =
        PreferenceManager::class.java.simpleName

    private val PREF_NAME = "com.gadgets_zone"
    private val KEY_USER_ID = "com.gadgets_zone.user_id"
    private val KEY_USER_NAME = "com.gadgets_zone.user_name"
    private val KEY_USER_EMAIL = "com.gadgets_zone.user_email"
    private val KEY_USER_COUNTRY = "com.gadgets_zone.user_country"
    private val KEY_USER_COUNTRY_CODE = "com.gadgets_zone.user_country_code"
    private val KEY_USER_PHONE = "com.gadgets_zone.user_phone"
    private val keyOneSignalNotificationId = "com.gadgets_zone.oneSignalNotificationId"
    private val KEY_USER_GENDER = "com.gadgets_zone.user_gender"
    private val KEY_AUTH_TOKEN = "com.gadgets_zone.auth_token"
    private val KEY_FIRE_BASE_TOKEN = "com.gadgets_zone.fire_base_token"
    private val KEY_PROFILE_IMAGE = "com.gadgets_zone.profile_image"
    private val KEY_COUNTRY_NAME = "com.gadgets_zone.country_name"
    private val KEY_CODE_NAME = "com.gadgets_zone.code_name"

    private val KEY_FACEBOOK_TOKEN = "com.gadgets_zone.facebook_token"

    private val KEY_GOOGLE_TOKEN = "com.gadgets_zone.google_token"
    private val Notifications = "notifications"

    private val isNotification = "customNotifications"



    private var sInstance: PreferenceManager? = null
    private var mPref: SharedPreferences? = null
    private var nPref: SharedPreferences? = null


    constructor(context: Context?) : this() {
        mPref = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    }

    @Deprecated("")
    @Synchronized
    fun getInstance(): PreferenceManager {
        if (sInstance == null) {
            throw IllegalStateException(
                PreferenceManager::class.java.simpleName
                        + "is not initialized, call initailizeInstance(..) method first."
            )
        }
        return sInstance!!
    }

    @Synchronized
    fun getInstance(context: Context?): PreferenceManager {
        if (sInstance == null) {
            checkNotNull(context) { "Null Context for $TAG" }
            sInstance =
                PreferenceManager(context.applicationContext)
        }
        return sInstance!!
    }


    fun createUser() {
        User.userId = getUserId()
        User.userName = getUserName()
        User.email = getUserEmail()
        User.authToken = getAuthToken()
        User.fireBaseToken = getFireBaseToken()
    }

    fun getUserDetail() {
        User.userName = getUserName()
        User.email = getUserEmail()
        User.country = getUserCountry()
        User.countryCode = getUserCountryCode()
        User.phone = getUserPhone()
        User.gender = getUserGender()
    }


    fun setIsNotification(code: String) {
        mPref!!.edit().putString(isNotification, code).commit()
    }

    fun getIsNotification(): String? {
        return mPref!!.getString(isNotification, "false")
    }

    fun setCountryNameCode(code: String?) {
        User.code = code
        mPref!!.edit().putString(KEY_CODE_NAME, code).apply()
    }

    fun getCountryNameCode(): String? {
        return mPref!!.getString(KEY_CODE_NAME, "")
    }

    fun setOneSignalNotificationId(notificationId: String) {
        User.notificationId = notificationId
        mPref?.edit()?.putString(keyOneSignalNotificationId, notificationId)?.apply()
    }

    fun getOneSignalNotificationId(): String {
        return mPref?.getString(keyOneSignalNotificationId, "")!!
    }

    fun setCountryName(countryname: String?) {
        User.countryname = countryname
        mPref!!.edit().putString(KEY_COUNTRY_NAME, countryname).apply()
    }

    fun getCountryName(): String? {
        return mPref!!.getString(KEY_COUNTRY_NAME, "")
    }

    fun setAuthToken(token: String?) {
        User.authToken = token
        mPref!!.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun getAuthToken(): String? {
        return mPref!!.getString(KEY_AUTH_TOKEN, "")
    }

    fun setProfileImage(profileImage: String?) {
        User.profileImage = profileImage
        mPref!!.edit().putString(KEY_PROFILE_IMAGE, profileImage).apply()
    }

    fun getProfileImage(): String? {
        return mPref!!.getString(KEY_PROFILE_IMAGE, "")
    }

    fun saveSignInResult(authResult: AuthResult?) {
        if (authResult == null) {
            return
        }
        var json= Gson()
        Log.e("auth result",json.toJson(authResult))
        setAuthToken(authResult.getAuthToken())
        setUserId(authResult.getId().toString())
        setUserName(authResult.getName())
        setUserEmail(authResult.getEmail())
        setFaceBookToken(authResult.getFacebookToken())
        setGoogleToken(authResult.getGoogleToken())
        setNotifications(authResult.getNotifications())


        setCountryName(authResult.getCountry())


    }

    fun setUserId(id: String?) {
        User.userId = id
        mPref!!.edit().putString(KEY_USER_ID, id).apply()
    }

    fun getUserId(): String? {
        return mPref!!.getString(KEY_USER_ID, "")
    }

    fun setUserName(userName: String?) {
        User.userName = userName
        mPref!!.edit().putString(KEY_USER_NAME, userName).apply()
    }

    fun getUserName(): String? {
        return mPref!!.getString(KEY_USER_NAME, "")
    }

    fun setUserEmail(email: String?) {
        User.email = email
        mPref!!.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserEmail(): String? {
        return mPref!!.getString(KEY_USER_EMAIL, "")
    }

    fun setUserCountry(country: String?) {
        User.country = country
        mPref!!.edit().putString(KEY_USER_COUNTRY, country).apply()
    }

    fun getUserCountry(): String? {
        return mPref!!.getString(KEY_USER_COUNTRY, "")
    }

    fun setUserCountryCode(countryCode: String?) {
        User.countryCode = countryCode
        mPref!!.edit().putString(KEY_USER_COUNTRY_CODE, countryCode).apply()
    }

    fun getUserCountryCode(): String? {
        return mPref!!.getString(KEY_USER_COUNTRY_CODE, "")
    }

    fun setUserPhone(phone: String?) {
        User.phone = phone
        mPref!!.edit().putString(KEY_USER_PHONE, phone).apply()
    }

    fun getUserPhone(): String? {
        return mPref!!.getString(KEY_USER_PHONE, "")
    }

    fun setUserGender(gender: String?) {
        User.gender = gender
        mPref!!.edit().putString(KEY_USER_GENDER, gender).apply()
    }

    fun getUserGender(): String? {
        return mPref!!.getString(KEY_USER_GENDER, "")
    }

    fun getFireBaseToken(): String? {
        return mPref!!.getString(KEY_FIRE_BASE_TOKEN, "")
    }

    fun getFaceBookTOken(): String? {
        return mPref!!.getString(KEY_FACEBOOK_TOKEN, "")
    }

    fun setFaceBookToken(token: String?) {
        User.facebooktoken = token
        mPref!!.edit().putString(KEY_FACEBOOK_TOKEN, token).apply()
    }

    fun getGoogleTOken(): String? {
        return mPref!!.getString(KEY_GOOGLE_TOKEN, "")
    }

    fun setGoogleToken(token: String?) {
        User.googleToken = token
        mPref!!.edit().putString(KEY_GOOGLE_TOKEN, token).apply()
    }

    fun getNotifications(): String? {
        return mPref!!.getString(Notifications, "")
    }

    fun setNotifications(notifications: String?) {
        mPref!!.edit().putString(Notifications, notifications).apply()
    }

    fun setFireBaseToken(fireBaseToken: String?) {
        User.fireBaseToken = fireBaseToken
        mPref!!.edit().putString(KEY_FIRE_BASE_TOKEN, fireBaseToken).apply()
    }

    fun clearUserData() {
        User.clear()
        mPref!!.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_NAME)
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_FIRE_BASE_TOKEN)
            .remove(KEY_FACEBOOK_TOKEN)
            .remove(KEY_COUNTRY_NAME)
            .remove(KEY_GOOGLE_TOKEN)
                .remove(isNotification)
            .remove(KEY_PROFILE_IMAGE)
            .remove(KEY_USER_EMAIL)



            .apply()
    }
}