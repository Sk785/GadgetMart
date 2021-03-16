package com.gadgetmart.ui.edit_profile

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.gadgetmart.R
import com.gadgetmart.base.BaseActivity
import com.gadgetmart.data.domain.AwsBucketApi
import com.gadgetmart.databinding.EditProfileActivityBinding
import com.gadgetmart.ui.auth.RegisterSetupActivity
import com.gadgetmart.ui.auth._common.AuthResult
import com.gadgetmart.ui.auth.registeration.RegistrationRequest
import com.gadgetmart.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File


class EditProfileActivity : BaseActivity<EditProfileActivityBinding>(),
    EditProfileContract, AwsBucketApi.AwsBucketApiListener {


    private var editProfilePresenter: EditProfilePresenter? = null
    private var countryCode: String? = ""
    private var countryCodeName: String? = ""
    private var countryName: String? = ""
    private lateinit var bsd: BottomSheetDialog
    private var CAMERA = 101
    private var GALLERY = 102
    private var CROP_PIC = 103

    private var isUpload: Boolean = false


    private var realPath: String? = ""
    private var bucketImagePath: String? = null
    private lateinit var gender: String
    lateinit var binding: EditProfileActivityBinding

    companion object {
        fun start(context: Context?) {

        }
    }

    override fun getContentView(): Int {
        return R.layout.edit_profile_activity
    }

    override fun init(binding: EditProfileActivityBinding) {
        this.binding = binding
        countryCodeName = PreferenceManager().getInstance(this).getCountryNameCode()!!
        //   countryName = PreferenceManager().getInstance(this).getCountryName()!!
        // countryName.let { binding.nationalityet?.setText(it) }
        countryCode = binding.ccpCountryCode?.selectedCountryCodeWithPlus
        binding.toolbarId.toolbarBackIcon?.setOnClickListener { finish() }
        binding.toolbarId.toolbarBackIcon?.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.black_arrow_left
            )
        )
        binding.toolbarId.toolbarParentLayout.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        binding.toolbarId.toolbarTitleTextView?.text = "Profile"
        binding.toolbarId.toolbarTitleTextView?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.black
            )
        )
binding.countryCodeEditText.isEnabled=false
        binding.phoneNumberEditText.isFocusable=false


        binding.nameEditText.setText(PreferenceManager().getInstance(this).getUserName())
        binding.emailIdEditText.setText(PreferenceManager().getInstance(this).getUserEmail())
        binding.phoneNumberEditText.setText(PreferenceManager().getInstance(this).getUserPhone())
        if (PreferenceManager().getInstance(this).getUserCountryCode().equals("")) {
        } else {
            binding.countryCodeEditText.setText(PreferenceManager().getInstance(this).getUserCountryCode())

        }
        bucketImagePath = PreferenceManager().getInstance(this).getProfileImage()
        countryName = PreferenceManager().getInstance(this).getCountryName()
        binding.nationalityet.setText(countryName)
        if (PreferenceManager().getInstance(this).getProfileImage() != null && PreferenceManager().getInstance(
                this
            ).getProfileImage()!!.isNotEmpty()
        ) {
            Glide.with(this)
                .load(PreferenceManager().getInstance(this).getProfileImage())
                .circleCrop().placeholder(R.drawable.dp_palceholder)
                .into(binding.profilePicImageView)
        } /*else {
            val drawable2 = TextDrawable.builder()
                .buildRound(
                    PreferenceManager().getInstance(this).getUserName()!![0].toString(),
                    resources.getColor(R.color.colorPrimary)
                )
            binding.profilePicImageView.setImageDrawable(drawable2)
        }*/

        gender = PreferenceManager().getInstance(this).getUserGender()!!
        if (gender.equals("male") || gender.equals("")) {
            binding.gendergroup.check(R.id.maleradio)
            gender = "male"
        }
        else {
            gender = "female"

            binding.gendergroup.check(R.id.femaleradio)
        }

        initListeners(binding)
        editProfilePresenter =
            EditProfilePresenter(this, this)

    }

    fun initListeners(binding: EditProfileActivityBinding) {

        binding.btnfinish.setOnClickListener {
            Log.e("Data ::: ", "" + countryCode)
            editProfilePresenter?.validateFields(
                ContextUtils.getAuthToken(this),
                RegistrationRequest().setUserName(binding.nameEditText?.text.toString())
                    .setEmailId(binding.emailIdEditText?.text.toString())
                    .setCountryCode(countryCode)
                    .setPhoneNumber(binding.phoneNumberEditText?.text.toString())
                    .setUserCountry(countryName)
                    .setProfilePhoto(bucketImagePath)
                    .setGender(gender),isUpload
            )

        }

        binding.countryCodeEditText.setOnClickListener {
            binding.ccpCountryCode?.launchCountrySelectionDialog(
                countryCode
            )
        }

        binding.ccpCountryCode?.setOnCountryChangeListener {
            getCountryCode(binding)
        }

        binding.nationalityet?.setOnClickListener {
            binding.ccpCountryName?.launchCountrySelectionDialog(
                countryName
            )
        }

        binding.ccpCountryName?.setOnCountryChangeListener {
            getCountryName(binding)
        }

        binding.profileImageFrame?.setOnClickListener {
            checkPermissions()
        }

        binding.gendergroup?.setOnCheckedChangeListener { radioGroup, radioId ->
            val rb = radioGroup.findViewById<RadioButton>(radioId)
            gender = rb?.text.toString()
        }
        binding.phoneNumberEditText.setOnClickListener {

            val intent = Intent(applicationContext, RegisterSetupActivity::class.java)
            intent.putExtra("name", "")
            intent.putExtra("type1", "0")


            intent.putExtra(Constants.email, "")
            intent.putExtra(Constants.socialId, "")
            intent.putExtra(Constants.type, "")
            intent.putExtra("isLogin", "no")

            startActivityForResult(intent,104)        }



//            if (PreferenceManager().getInstance(this).getUserEmail().equals("")) {
//                binding.emailIdEditText.isEnabled = true
//
//            } else {
//                binding.emailIdEditText.isEnabled = false
//
//            }


//        binding.myAddressesTextView.setOnClickListener { onMyAddressesTapped() }

//        binding.myReviewsTextView.setOnClickListener { onMyReviewsTapped() }

    }

    private fun onNotificationsSettingsTapped() {

    }


    private fun getCountryCode(binding: EditProfileActivityBinding) {
        countryCodeName = binding.ccpCountryCode.selectedCountryNameCode
        Log.e("Name ::::: ", "" + countryCodeName)
        countryCode = binding.ccpCountryCode.selectedCountryCodeWithPlus
        countryName = binding.ccpCountryName.selectedCountryName
        PreferenceManager().getInstance(this).setCountryNameCode(countryCodeName)
        countryName = binding.ccpCountryName.selectedCountryName
        PreferenceManager().getInstance(this).setCountryName(countryName)
        countryCode.let { binding.countryCodeEditText?.setText(it) }
    }

    private fun getCountryName(binding: EditProfileActivityBinding) {
        countryCodeName = binding.ccpCountryCode.selectedCountryNameCode
        Log.e("Name ::::: ", "" + countryCodeName)
        countryName = binding.ccpCountryName.selectedCountryName
        PreferenceManager().getInstance(this).setCountryNameCode(countryCodeName)
        countryName = binding.ccpCountryName.selectedCountryName
        PreferenceManager().getInstance(this).setCountryName(countryName)
        countryCode = binding.ccpCountryCode.selectedCountryCodeWithPlus
        countryName.let { binding.nationalityet?.setText(it) }
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
                val selectedImage = AppValidationUtil.getImageUri(this, photo)
                CropImage.activity(selectedImage)
                    .start(this);

            }
            GALLERY -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri? = data!!.data
                CropImage.activity(selectedImage)
                    .start(this);

//                realPath = selectedImage?.let { AppValidationUtil.getRealPathFromURI(it, this) }
//
////                realPath = selectedImage.toString()
//                Log.e("ImageRealPath : ", "" + realPath)
//
//                if (realPath == null) {
//                    showToast("Invalid image")
//                }
//                val timeInMillis = System.currentTimeMillis().toString()
//                val file = File(realPath!!)
//                AwsBucketApi.uploadImageToBucket(
//                    this,
//                    file,
//                    timeInMillis + "_" + file.name,
//                    this
//                )


            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->


                if (resultCode == Activity.RESULT_OK) {
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.e("########uri",resultUri.toString())
              //  val photo: Bitmap = data!!.extras!!.get("data") as Bitmap
               // val selectedImage = AppValidationUtil.getImageUri(this, photo)
                realPath = AppValidationUtil.getFilePath(this,resultUri)

                Log.e("ImageRealPath : ", "" + realPath)
                if (realPath == null) {
                    showToast("Invalid image")
                }
                val timeInMillis = System.currentTimeMillis().toString()
                    isUpload=true;
                val file = File(realPath!!)
                    bucketImagePath=realPath
                    binding.profilePicImageView.setImageURI(resultUri)
//                AwsBucketApi.uploadImageToBucket(
//                    this,
//                    file,
//                    timeInMillis + "_" + file.name,
//                    this
//                )


            }

104 ->if (resultCode == Activity.RESULT_OK){
    binding.phoneNumberEditText.setText(data!!.extras!!.getString("phone"))

}
        }
    }

    private fun checkPermissions() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    bsd = BottomSheetDialog(this@EditProfileActivity)
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

    override fun onProfileUpdated(result: AuthResult?, message: String) {
        isUpload=false;
        Log.e("result edit",Gson().toJson(result))
        PreferenceManager().getInstance(this).setProfileImage(result?.getPhoto())
        PreferenceManager().getInstance(this).setUserCountry(result?.getCountry())
        PreferenceManager().getInstance(this).setUserCountryCode(result?.getCountryCode())
        PreferenceManager().getInstance(this).setUserGender(result?.getGender())
        PreferenceManager().getInstance(this).setUserPhone(result?.getPhone())
        PreferenceManager().getInstance(this).setUserName(result?.getName())
        PreferenceManager().getInstance(this).setUserEmail(result?.getEmail())
        finish()
        Log.e(
            "Profile Updated :::: ",
            "" + message + " ::: name ::: " + result?.getName() + " ::: " + result?.getPhoto()
        )
//        binding().userNameTextView.text = dashboardResult
    }

    override fun onProfileUpdationFailed(message: String) {
        AppUtil.firebaseEvent(applicationContext,"error","error_events",message
        )
        showToast(message)
        Log.e("Profile Failed :::: ", "" + message)
    }

    override fun onPicUploadedOnBucket(imagePath: String?) {
        bucketImagePath = imagePath
        PreferenceManager().getInstance(this).setProfileImage(bucketImagePath)

        Glide.with(this)
            .load(bucketImagePath)
            .circleCrop()
            .into(binding.profilePicImageView)
        Log.e("Bucket Path ::: ", "" + bucketImagePath)
    }

    override fun onPicNotUploadedOnBucket(message: String?) {
        AppUtil.showToast(this, message!!)
        Log.e("Bucket Error ::: ", "" + message)
    }

    override fun onPicUpdationProgress(percent: Int?) {

    }

    private fun performCrop(picUri:Uri) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            val cropIntent = Intent("com.android.camera.action.CROP")
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*")
            // set crop properties
            cropIntent.putExtra("crop", "true")
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2)
            cropIntent.putExtra("aspectY", 1)
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256)
            cropIntent.putExtra("outputY", 256)
            // retrieve data on return
            cropIntent.putExtra("return-data", true)
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC)
        } // respond to users whose devices do not support the crop action
        catch (anfe: ActivityNotFoundException) {
            val toast = Toast
                .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}