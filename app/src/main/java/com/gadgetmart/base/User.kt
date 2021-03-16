package com.gadgetmart.base

import android.text.TextUtils

class User {
    companion object {

        var fireBaseToken: String? = null
        var userId: String? = null
        var userName: String? = null
        var authToken: String? = null
        var email: String? = null
        var gender: String? = null
        var country: String? = null
        var countryCode: String? = null
        var phone: String? = null
        var profileImage: String? = null
        var notificationId: String? = null
        var code:String? =null
        var countryname:String? =null
        var facebooktoken:String? =""
        var googleToken:String?=""



        fun toUrlParams(): String? {
            val sessionMap = toMap()
            val urlParamsBuilder = StringBuilder()

            for (sessionMapEntry in sessionMap) {

                if (urlParamsBuilder.isNotEmpty()) {
                    urlParamsBuilder.append("&")
                }

                urlParamsBuilder
                    .append(sessionMapEntry.key)
                    .append("=")
                    .append(sessionMapEntry.value)
            }
            return urlParamsBuilder.toString()
        }

        fun toMap(): MutableMap<String, String> {
            val sessionMap = mutableMapOf<String, String>()
            if (userId != null) {
                sessionMap["user_id"] = userId!!
                if (!TextUtils.isEmpty(authToken)) {
                    sessionMap["auth_token"] = authToken!!
                }
            }
            if (!TextUtils.isEmpty(fireBaseToken)) {
                sessionMap["firebase_token"] = fireBaseToken!!
            }
            return sessionMap
        }

        fun clear() {
            userId = null
            authToken = null
            fireBaseToken = null
            userName = null
            email = null
            phone = null
            country = null
            countryCode = null
            notificationId = null
            gender = null
            facebooktoken=""
            googleToken=""
        }
    }
}