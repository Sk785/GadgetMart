package com.gadgetmart.ui.my_address

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.databinding.MyAddressesActivityBinding
import com.gadgetmart.ui.subcategory.AddressAdapterListener
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.ContextUtils
import com.gadgetmart.util.custom.StyledAlertDialog

class MyAddressesActivity : BaseActivity<MyAddressesActivityBinding>(), MyAddressContract,
    AddressAdapterListener {


    lateinit var binding: MyAddressesActivityBinding
    private val ADDRESS_SAVED = 101
    lateinit var addressPresenter: AddressPresenter
    lateinit var myAddresses: ArrayList<AddAddressItems>
    private var myAddressAdapter: MyAddressAdapter? = null
    private var adapterPosition: Int = 0
    override fun getContentView(): Int {
        return R.layout.my_addresses_activity
    }

    override fun init(binding: MyAddressesActivityBinding) {
        this.binding = binding
        initPresenter()
        initRecyclerView()

        binding.toolbarID.toolbarTitleTextView.text = "My Addresses"
        binding.toolbarID.toolbarExtraIcon.visibility = View.GONE
//        binding.toolbarID.toolbarExtraIcon.setImageDrawable(
//            ContextCompat.getDrawable(
//                this,
//                R.drawable.add_address
//            )
//        )
        setListeners(binding)
    }

    private fun initRecyclerView() {
        myAddresses = ArrayList()
        val subCategoriesLayoutManager = LinearLayoutManager(this)

        subCategoriesLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.addressesRecyclerView.layoutManager = subCategoriesLayoutManager

        myAddressAdapter = MyAddressAdapter(this, myAddresses, this)
        binding.addressesRecyclerView.adapter = myAddressAdapter
    }

    private fun initPresenter() {
        addressPresenter = AddressPresenter(this, this)
        addressPresenter.getAddresses(ContextUtils.getAuthToken(this), true)
    }

    private fun setListeners(binding: MyAddressesActivityBinding) {
        binding.toolbarID.toolbarBackIcon.setOnClickListener {
            if (myAddresses.size > 0) {
                finish()
            } else {
                val i = Intent()
                i.putExtra("id", 0)
                i.putExtra("name", "")

                setResult(Activity.RESULT_OK, i)
                finish()
            }
        }
        binding.addNewAddressTextView.setOnClickListener {
            startActivityForResult(
                Intent(this, AddNewAddressActivity::class.java).putExtra(
                    "type",
                    "new"
                ), ADDRESS_SAVED
            )
        }

        binding.noAddressLayout?.btnfinish?.setOnClickListener {
            startActivityForResult(
                Intent(this, AddNewAddressActivity::class.java).putExtra(
                    "type",
                    "new"
                ), ADDRESS_SAVED
            )
        }

        binding.swipeToRefresh?.setOnRefreshListener {
            binding.swipeToRefresh.isRefreshing = true
            myAddresses.clear()
            binding.homeLayout?.visibility = View.GONE
            addressPresenter.getAddresses(ContextUtils.getAuthToken(this), false)
        }

        binding.swipeToRefresh?.setColorSchemeColors(
            Color.rgb(255, 93, 94),
            Color.GREEN,
            Color.BLUE
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADDRESS_SAVED && resultCode == Activity.RESULT_OK) {
            if (data?.extras != null) {
                if (data.extras?.getString("address_status").equals("addressAdded")) {
                    addressPresenter.getAddresses(ContextUtils.getAuthToken(this), true)
                } else if (data.extras?.getString("address_status").equals("addressUpdated")) {
                    val addressItems = data.extras?.getParcelable<AddAddressItems>("address")
                    myAddresses[adapterPosition] = addressItems!!
                    myAddressAdapter?.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onAddressDataFound(
        addressResult: AddressResult?,
        message: String?,
        apiFlag: Int?
    ) {
        when (apiFlag) {
            AppUtil.GetAddressesFlag -> {
                binding.swipeToRefresh?.isRefreshing = false
                binding.homeLayout?.visibility = View.VISIBLE

                if (addressResult?.addresses == null || addressResult.addresses!!.size == 0) {
//            if (subCategoryResult?.categories!![0].subCategories == null)
                    binding.mainLayout?.visibility = View.GONE
                    binding.mainLayoutEmpty?.visibility = View.VISIBLE
                    return
                } else {
                    binding.savedAddressesCountTextView.setText(
                        getString(
                            R.string.format_saved_address_count,
                            "" + addressResult.addresses?.size
                        )
                    )
                    binding.mainLayout?.visibility = View.VISIBLE
                    binding.mainLayoutEmpty?.visibility = View.GONE

                    myAddresses.clear()
                    myAddresses = addressResult.addresses as ArrayList<AddAddressItems>
                    myAddressAdapter?.updateList(myAddresses)
                }
            }

            AppUtil.RemoveAddressFlag -> {
                myAddresses.removeAt(adapterPosition)
                myAddressAdapter?.notifyDataSetChanged()
                initPresenter()
            }
        }

    }

    override fun onAddressDataNotFound(message: String?) {
        binding.swipeToRefresh?.isRefreshing = false
        binding.homeLayout?.visibility = View.VISIBLE

    }

    override fun onAddressSetAsDefault(message: String?) {
        message?.let { showToast(it) }
    }

    override fun onAddressNotSetAsDefault(message: String?) {
        message?.let { showToast(it) }
    }

    override fun onAdapterItemTapped(adapterItem: Int?, categoryName: String?, itemPosition: Int?) {
        adapterPosition = itemPosition!!
        when (categoryName) {
            "editAddress" -> {
                for (i in myAddresses.indices) {
                    if (i == itemPosition) {
                        val intent = Intent(this, AddNewAddressActivity::class.java)
                        intent.putExtra("id", adapterItem)
                        intent.putExtra("name", myAddresses[i].name)
                        intent.putExtra("address1", myAddresses[i].address1)
                        intent.putExtra("address2", myAddresses[i].address2)
                        intent.putExtra("city", myAddresses[i].city)
                        intent.putExtra("state", myAddresses[i].state)
                        intent.putExtra("country", myAddresses[i].country)
                        intent.putExtra("phone_number", myAddresses[i].phone)
                        intent.putExtra("zipcode", myAddresses[i].zip)
                        intent.putExtra("address_type", myAddresses[i].addressType)
                        intent.putExtra("type", "edit")
                        startActivityForResult(intent, ADDRESS_SAVED)
                        break
                    }
                }

            }
            "removeAddress" -> {
                StyledAlertDialog.builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.text_remove)
                    .setMessage(R.string.msg_remove_address)
                    .setPositiveButton(R.string.text_yes) { dialog, _ ->
                        dialog.dismiss()
                        addressPresenter.removeAddresses(
                            adapterItem.toString(),
                            ContextUtils.getAuthToken(this)
                        )
                    }
                    .setNegativeButton(R.string.text_no) { dialog, _ ->
                        dialog.dismiss()
                    }.show()
//                val dialog = Dialog(this)
//                dialog.setCancelable(true)
//                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                dialog.setContentView(R.layout.custom_alert_dialog)
//                if (dialog.window != null) {
//                    dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
//                }
//                val btnok = dialog.findViewById(R.id.okbtn) as TextView
//                val btncancel = dialog.findViewById(R.id.cancelbtn) as TextView
//
//                btnok.setOnClickListener {
//
//                    addressPresenter.removeAddresses(
//                        adapterItem.toString(),
//                        ContextUtils.getAuthToken(this)
//                    )
//                    dialog.dismiss()
//
//                    finishAffinity()
//                }
//
//                btncancel.setOnClickListener {
//                    dialog.dismiss()
//                }
//                dialog.show()
            }
        }
    }

    override fun onAdapterItemClick(id: Int, name: String,pinCode:String) {
        if (intent.extras!!.getString("type")!!.equals("account")) {

        } else {
            val i = Intent()
            i.putExtra("id", id)
            i.putExtra("name", name)
            i.putExtra("pincode", pinCode)


            setResult(Activity.RESULT_OK, i)
            finish()
        }

    }

    override fun onSetAsDefaultMenuItemSelected(addressId: String?) {
        addressId?.let { addressPresenter.setAddressAsDefault(it, ContextUtils.getAuthToken(this)) }
    }
}
