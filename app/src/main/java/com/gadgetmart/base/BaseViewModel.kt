package com.gadgetmart.base

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gadgetmart.util.ResourceProvider

open class BaseViewModel(
    private val resourceProvider: ResourceProvider
) : ViewModel() {


    /*
     * Toast Control
     */

    var toastLiveData: MutableLiveData<String> = MutableLiveData()

    fun toast(message: String?) {
        toastLiveData.value = message
    }

    fun toast(@StringRes messageRes: Int, vararg formatArgs: Any = arrayOf()) {
        toastLiveData.value = resourceProvider.stringRes(messageRes, *formatArgs)
    }
}