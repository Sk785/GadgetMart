package com.gadgetmart.ui.my_address

import com.gadgetmart.base.BaseContract

interface MyAddressContract : BaseContract{

    fun onAddressDataFound(addressResult: AddressResult?, message: String? , apiFlag : Int?)

    fun onAddressDataNotFound(message: String?)

    fun onAddressSetAsDefault(message: String?)

    fun onAddressNotSetAsDefault(message: String?)
}