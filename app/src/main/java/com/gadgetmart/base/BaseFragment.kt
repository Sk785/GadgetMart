package com.gadgetmart.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.gadgetmart.util.permission.PermissionSupport

abstract class BaseFragment<BINDING : ViewDataBinding> : Fragment(), BaseActivityExtension,
    PermissionSupport.Callback {

    private lateinit var binding: BINDING
    private var activityExtension: BaseActivityExtension? = null
    private var permissionSupport: PermissionSupport? = null

    override fun onAttach(@NonNull context: Context) {
        super.onAttach(context)

        if (context is BaseActivityExtension) {
            activityExtension = context
        } else {
            throw ClassCastException("This fragment is not a part of BaseActivity.")
        }
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, getContentView(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(binding)
        initPresenters()
        initListeners(binding)
    }


    @LayoutRes
    abstract fun getContentView(): Int

    abstract fun initView(binding: BINDING)

    abstract fun initListeners(binding: BINDING)

    abstract fun initPresenters()

    fun getActivityExtension(): BaseActivityExtension? {
        return activityExtension!!
    }

    override fun clearUser() {
        // Clear shared preferences data, if any
    }

    override fun hasNetwork(): Boolean {
        return activityExtension!!.hasNetwork()
    }

    override fun showToast(message: String) {
        return activityExtension!!.showToast(message)
    }

    fun showToast(stringResourceId: Int) {
        showToast(getString(stringResourceId))
    }

    override fun replaceFragment(
        fragment: BaseFragment<*>,
        tag: String,
        addToBackStack: Boolean,
        shouldAnimateTransition: Boolean
    ) {
        return activityExtension!!.replaceFragment(
            fragment,
            tag,
            addToBackStack,
            shouldAnimateTransition
        )
    }


    fun getPermissionSupport(): PermissionSupport {

        if (permissionSupport == null) {
            permissionSupport = PermissionSupport.Builder()
                .setFragment(this)
                .setCallback(this)
                .build()
        }

        return permissionSupport!!
    }

    override fun onPermissionsAllowed(who: Int) {}

    override fun onPermissionsNotAllowed() {}

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {

        val isConsumed =
            getPermissionSupport().consumePermissionResult(requestCode, permissions, grantResults)
        if (!isConsumed) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // Because we don't want to implement it in all sub-fragments
    fun onFragmentVisibilityChanged(visible: Boolean, forward: Boolean) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}