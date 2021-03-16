package com.gadgetmart.ui.auth.registeration

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.auth._common.AuthResult


interface RegistrationContract : BaseContract {

    fun onUserRegistered(result: AuthResult?, message: String,status:Int)

    fun onUserRegistrationFailed(message: String)
}