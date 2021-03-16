package com.gadgetmart.ui.auth.phone_number_verification

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.auth._common.AuthResult


interface PhoneVerificationContract : BaseContract {

    fun onPhoneNumberVerified(authResult: AuthResult?, profileStatus: Int, message: String)

    fun onPhoneNumberVerificationFailed(message: String)
}