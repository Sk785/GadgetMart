package com.gadgetmart.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Every presenter in the app must extend BasePresenter indicating the Contract type t
 * hat wants to be attached with.
 */

open class BasePresenter<CONTRACT : BaseContract>(
    private var contract: CONTRACT?,
    private var compositeDisposable: CompositeDisposable?
) {

    fun stop() {
        if (compositeDisposable != null && !compositeDisposable!!.isDisposed) {
            compositeDisposable!!.clear()
        }
        contract = null
    }

    fun addToCompositeDisposable(disposable: Disposable) {

        if (compositeDisposable != null) {
            compositeDisposable!!.add(disposable)
        }
    }

    fun getContract(): CONTRACT {
        return contract!!
    }

    fun hasContract(): Boolean {
        return contract != null
    }


    fun hasNetwork(): Boolean {
        return getContract().hasNetwork()
    }

}