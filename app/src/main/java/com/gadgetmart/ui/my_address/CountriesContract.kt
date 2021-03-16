package com.gadgetmart.ui.my_address

import com.gadgetmart.base.BaseContract

interface CountriesContract : BaseContract{

    fun onCountriesFound(countries: ArrayList<Country>?, message: String? , apiFlag : Int?)

    fun onCountriesNotFound(message: String?)

    fun onStatesFound(states: ArrayList<State>?, message: String? , apiFlag : Int?)

    fun onStatesNotFound(message: String?)

    fun onCitiesFound(cities: ArrayList<City>?, message: String? , apiFlag : Int?)

    fun onCitiesNotFound(message: String?)
}