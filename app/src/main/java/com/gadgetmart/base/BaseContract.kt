package com.gadgetmart.base



/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 */

interface BaseContract {

    fun hasNetwork(): Boolean

    fun showToast(message: String)

    fun showToast(stringResourceId: Int)
}