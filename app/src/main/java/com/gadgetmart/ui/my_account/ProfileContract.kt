package com.gadgetmart.ui.my_account

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.auth._common.AuthResult


interface ProfileContract : BaseContract {

    fun onProfileFound(authResult : AuthResult?, message: String)

    fun onProfileNotFound(message: String)
}