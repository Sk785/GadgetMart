package com.gadgetmart.base

interface BaseActivityExtension {

    fun clearUser()

    fun hasNetwork(): Boolean

    fun showToast(message: String)

    fun replaceFragment(
        fragment: BaseFragment<*>,
        tag: String,
        addToBackStack : Boolean = true,
        shouldAnimateTransition : Boolean = true
    )
    fun openInternetDialog()
}