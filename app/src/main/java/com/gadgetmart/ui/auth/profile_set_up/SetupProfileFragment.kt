package com.gadgetmart.ui.auth.profile_set_up

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.gadgetmart.R
import com.gadgetmart.base.BaseFragment
import com.gadgetmart.data.domain.AwsBucketApi
import com.gadgetmart.databinding.FragmentSetupProfileBinding
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.auth.registeration.RegistrationContract
import com.gadgetmart.ui.auth.registeration.RegistrationPresenter
import com.gadgetmart.ui.auth.registeration.RegistrationRequest
import com.gadgetmart.ui.home.HomeActivity
import com.gadgetmart.util.AppUtil
import com.gadgetmart.util.AppValidationUtil
import com.gadgetmart.util.Constants
import com.gadgetmart.util.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File

class SetupProfileFragment : BaseFragment<FragmentSetupProfileBinding>(), RegistrationContract,
    AwsBucketApi.AwsBucketApiListener {
    private var gender = "male"
    private var countryCode = ""
    private var countryNameCode = ""
    private var phoneNumber = ""
    private var registrationPresenter: RegistrationPresenter? = null
    private val intentExtraCountryCode = "extra_country_code"
    private val intentExtraPhoneNumber = "extra_phone_number"
    private var countryName: String? = ""
    private lateinit var bsd: BottomSheetDialog
    private var CAMERA = 101
    private var GALLERY = 102
    private var realPath: String? = ""
    private var bucketImagePath: String? = null
    lateinit var binding: FragmentSetupProfileBinding

    private var email = ""
    private var socilaId = ""
    private var socilaType = ""
    private var name = ""


    fun newInstance(bundle: Bundle): SetupProfileFragment {
        val fragment =
            SetupProfileFragment()

        fragment.arguments = bundle

        return fragment
    }

    override fun getContentView(): Int {
        return R.layout.fragment_setup_profile
    }

    override fun initView(binding: FragmentSetupProfileBinding) {
        this.binding = binding
        countryNameCode = PreferenceManager().getInstance(activity).getCountryNameCode()!!
        countryName = PreferenceManager().getInstance(activity).getCountryName()!!
        countryName.let { binding.nationalityet?.setText(it) }
        initData()
    }

    private fun initData() {
        val bundle = arguments

        if (bundle != null) {
            countryCode = bundle.getString(Constants.countryCode)!!
            phoneNumber = bundle.getString(Constants.phoneNumber)!!
            email = bundle.getString(Constants.email)!!
            name = bundle.getString("name")!!

            socilaId = bundle.getString(Constants.socialId)!!
            socilaType = bundle.getString(Constants.type)!!


        }
        binding.emailIdEditText.setText(email)
        binding.nameEditText.setText(name)



        binding.emailIdEditText.isEnabled = true

    }

    override fun initPresenters() {
        registrationPresenter = RegistrationPresenter(activity!!, this)
    }

    override fun initListeners(binding: FragmentSetupProfileBinding) {
        if(gender.equals("male")){
            binding.maleradio?.isChecked=true
        }
        binding.femaleradio?.setOnCheckedChangeListener { _, p1 ->
            if (p1) gender = "female"
        }

        binding.maleradio?.setOnCheckedChangeListener { _, p1 ->
            if (p1) gender = "male"
        }

        binding.nationalityet?.setOnClickListener {
            binding.ccpCountryName?.launchCountrySelectionDialog(
                countryNameCode
            )
        }

        binding.ccpCountryName?.setOnCountryChangeListener {
            getCountryName(binding)
        }

        binding.profileImageFrame?.setOnClickListener {
            checkPermissions()
        }

        binding.btnfinish?.setOnClickListener {
            when {
                binding.nameEditText?.text.toString() == "" -> {
                    AppUtil.showToast(
                        activity!!,
                        resources.getString(R.string.error_msg_person_name)
                    )
                    binding.nameEditText.requestFocus()
                }
                binding.emailIdEditText?.text.toString() == "" -> {
                    AppUtil.showToast(activity!!, resources.getString(R.string.error_msg_email_id))
                    binding.emailIdEditText.requestFocus()
                }
                binding.nationalityet?.text.toString() == "" -> {
                    AppUtil.showToast(
                        activity!!,
                        resources.getString(R.string.error_msg_nationality)
                    )
                    binding.nationalityet.requestFocus()
                }

                else -> {
                    onFinishButtonTapped(binding)
                }
            }
        }
    }

    private fun getCountryName(binding: FragmentSetupProfileBinding) {
        countryNameCode = binding.ccpCountryName.selectedCountryNameCode
        PreferenceManager().getInstance(activity).setCountryNameCode(countryNameCode)
        countryName = binding.ccpCountryName.selectedCountryName
        PreferenceManager().getInstance(activity).setCountryName(countryName)
        countryName.let { binding.nationalityet?.setText(it) }
    }

    private fun checkPermissions() {
        TedPermission.with(activity)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    bsd = BottomSheetDialog(activity!!)
                    bsd.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    bsd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    bsd.setContentView(R.layout.choose_image_from_layout)
                    bsd.setCanceledOnTouchOutside(false)
                    bsd.setCancelable(false)
                    bsd.show()
                    onclick()
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {

                }
            })
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    private fun onclick() {

        val camera: LinearLayout? = bsd.findViewById(R.id.camera_layout) as LinearLayout?
        val gallery: LinearLayout? = bsd.findViewById(R.id.gallery_layout) as LinearLayout?
        val cancelLayout: LinearLayout? = bsd.findViewById(R.id.cancel_layout) as LinearLayout?

        camera?.setOnClickListener {
            bsd.dismiss()
            openCamera()
        }
        gallery?.setOnClickListener {
            bsd.dismiss()
            openGallery()
        }
        cancelLayout?.setOnClickListener { bsd.dismiss() }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    private fun openGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA -> if (resultCode == Activity.RESULT_OK) {
                val photo: Bitmap = data!!.extras!!.get("data") as Bitmap
                val selectedImage = AppValidationUtil.getImageUri(activity!!, photo)
                realPath = AppValidationUtil.getRealPathFromURI(selectedImage, activity!!)

                Log.e("ImageRealPath : ", "" + realPath)
                if (realPath == null) {
                    showToast("Invalid image")
                }
                val timeInMillis = System.currentTimeMillis().toString()
                val file = File(realPath!!)
                bucketImagePath=realPath
//                AwsBucketApi.uploadImageToBucket(
//                    activity!!,
//                    file,
//                    timeInMillis + "_" + file.name,
//                    this
//                )
                binding.profilePicImageView.setImageURI(selectedImage)

            }
            GALLERY -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri? = data!!.data
                realPath =
                    selectedImage?.let { AppValidationUtil.getRealPathFromURI(it, activity!!) }

//                realPath = selectedImage.toString()
                Log.e("ImageRealPath : ", "" + realPath)

                if (realPath == null) {
                    showToast("Invalid image")
                }
                val timeInMillis = System.currentTimeMillis().toString()
                val file = File(realPath!!)
//                AwsBucketApi.uploadImageToBucket(
//                    activity!!,
//                    file,
//                    timeInMillis + "_" + file.name,
//                    this
//                )
                bucketImagePath=realPath
                binding.profilePicImageView.setImageURI(selectedImage)
            }
        }
    }

    override fun openInternetDialog() {}

    private fun onFinishButtonTapped(binding: FragmentSetupProfileBinding) {
        if (socilaType.equals("facebook")) {
            AppUtil.firebaseEvent(activity!!,"type","signup","facebook")
            registrationPresenter!!.validateFields(

                RegistrationRequest()
                    .setCountryCode(countryCode)
                    .setPhoneNumber(phoneNumber)
                    .setUserName(binding.nameEditText?.text.toString())
                    .setGender(gender)
                    .setEmailId(binding.emailIdEditText.text.toString().trim())
                    .setProfilePhoto(bucketImagePath)
                    .setDeviceToken(PreferenceManager(context).getOneSignalNotificationId())
                    .setFacebookToken(socilaId)

            )
        } else if (socilaType.equals("google")) {
            AppUtil.firebaseEvent(activity!!,"type","signup","google")

            registrationPresenter!!.validateFields(

                RegistrationRequest()
                    .setCountryCode(countryCode)
                    .setPhoneNumber(phoneNumber)
                    .setDeviceToken(PreferenceManager(context).getOneSignalNotificationId())
                    .setUserName(binding.nameEditText?.text.toString())
                    .setGender(gender)
                    .setEmailId(binding.emailIdEditText.text.toString().trim())

                    .setProfilePhoto(bucketImagePath)
                    .setGoogleToken(socilaId)

            )
        } else {
            AppUtil.firebaseEvent(activity!!,"type","signup","phone")

            registrationPresenter!!.validateFields(

                RegistrationRequest()
                    .setCountryCode(countryCode)
                    .setDeviceToken(PreferenceManager(context).getOneSignalNotificationId())
                    .setPhoneNumber(phoneNumber)
                    .setUserName(binding.nameEditText?.text.toString())
                    .setGender(gender)
                    .setEmailId(binding.emailIdEditText.text.toString().trim())

                    .setProfilePhoto(bucketImagePath)

            )
        }
    }

    override fun onUserRegistered(result: AuthResult?, message: String, status: Int) {
        if (status == 1) {
            if (activity != null) {
                HomeActivity.start(activity)
                activity?.finishAffinity()
            }
        } else {
            showToast(message)
        }
        // showToast(message)


    }

    override fun onUserRegistrationFailed(message: String) {
        showToast(message)
    }

    override fun onPicUploadedOnBucket(imagePath: String?) {
        bucketImagePath = imagePath
        Glide.with(activity!!)
            .load(bucketImagePath)
            .into(binding.profilePicImageView)
    }

    override fun onPicNotUploadedOnBucket(message: String?) {

    }

    override fun onPicUpdationProgress(percent: Int?) {

    }
}
