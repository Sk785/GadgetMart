package com.gadgetmart.ui.edit_profile

import com.gadgetmart.base.BaseContract
import com.gadgetmart.ui.auth._common.AuthResult


interface EditProfileContract : BaseContract {

    fun onProfileUpdated(result: AuthResult?, message: String)

    fun onProfileUpdationFailed(message: String)
}