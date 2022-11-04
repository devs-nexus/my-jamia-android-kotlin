package com.example.myjamia.ui.personalData

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myjamia.R
import com.example.myjamia.databinding.ActivityPersonalDataBinding
import com.example.myjamia.network.ResponseCallback
import com.example.myjamia.preferences.ProfilePrefs
import com.example.myjamia.utils.Utils
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PersonalDataActivity : AppCompatActivity() {
    private val TAG = "PersonalDataActivity"
    private lateinit var binding: ActivityPersonalDataBinding
    private lateinit var viewModel: PersonalDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_data)

        viewModel = ViewModelProvider(this)[PersonalDataViewModel::class.java]

        val profileImage = ProfilePrefs.getImageFromStorage(this)
        Glide.with(this).load(profileImage).placeholder(R.drawable.ic_profile)
            .into(binding.personalDataIv)

        binding.personalDataBackBtn.setOnClickListener {
            finish()
        }

        binding.editProfileImageBtn.setOnClickListener { editProfileImage() }
        binding.lifecycleOwner = this
        getProfile()

        initRadioButtons()
        initDOBDialog()
        initDeptAndCourseDD()

        binding.updateProfileBtn.setOnClickListener {
            updateProfile()
            getProfile()
        }
    }

    /**
     *  METHOD TO GET PROFILE DATA FROM SERVER AND SET IT TO UI
     */
    private fun getProfile() {
        //setting progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading Profile")
        progressDialog.setCancelable(false)
        progressDialog.show()

        viewModel.isProfileLoaded.observe(this) {
            if (it != null) {
                //clearing progress dialog
                progressDialog.dismiss()

                //setting data
                if (it) {
                    binding.viewModel = viewModel
                    //setting gender radios
                    setRadioButtons(viewModel.gender)
                } else {
                    Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        viewModel.getProfile(this)
    }

    private fun updateProfile() {
        val firstName = binding.firstNameEt.text.toString()
        val lastName = binding.lastNameEt.text.toString()
        val gender = if (binding.maleRadio.isChecked) "M" else "F"
        val fatherName = binding.fatherNameEt.text.toString()
        val rollNo = binding.rollNoEt.text.toString()
        val mobileNo = binding.mobileNoEt.text.toString()
        val dob = binding.dobEt.text.toString()

        if (firstName.isEmpty()) {
            binding.firstNameEt.error = "First name is required"
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (lastName.isEmpty()) {
            binding.lastNameEt.error = "Last name is required"
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (fatherName.isEmpty()) {
            binding.fatherNameEt.error = "Father name is required"
            Toast.makeText(this, "Father name is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (rollNo.isEmpty()) {
            binding.rollNoEt.error = "Roll no is required"
            Toast.makeText(this, "Roll no is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (mobileNo.isEmpty()) {
            binding.mobileNoEt.error = "Mobile no is required"
            Toast.makeText(this, "Mobile No. is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (dob.isEmpty()) {
            binding.dobEt.error = "Date of birth is required"
            Toast.makeText(this, "Date of birth is required", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating Profile")
        progressDialog.setCancelable(false)
        progressDialog.show()
        viewModel.updateProfile(this,
            firstName,
            lastName,
            gender,
            fatherName,
            rollNo,
            mobileNo,
            dob,
            object : ResponseCallback {
                override fun onSuccess(response: String) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@PersonalDataActivity,
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    ProfilePrefs.saveUserName(this@PersonalDataActivity, "$firstName $lastName")
                    finish()
                }

                override fun onError(response: String) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@PersonalDataActivity, response, Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    /**
     * METHOD FOR INITIALIZING RADIO BUTTONS CLICK LISTENERS
     */
    private fun initRadioButtons() {
        //settings male radio custom background on check
        binding.maleRadio.setOnClickListener {
            binding.maleRadioLayout.setBackgroundResource(R.drawable.radio_checked)
            binding.femaleRadioLayout.setBackgroundResource(R.drawable.radio_regular)
            binding.femaleRadio.isChecked = false
        }

        //settings female radio custom background on check
        binding.femaleRadio.setOnClickListener {
            binding.femaleRadioLayout.setBackgroundResource(R.drawable.radio_checked)
            binding.maleRadioLayout.setBackgroundResource(R.drawable.radio_regular)
            binding.maleRadio.isChecked = false
        }
    }

    /**
     * METHOD FOR INITIALIZING DATE OF BIRTH DIALOG AND GET DATE FROM IT
     */
    private fun initDOBDialog() {
        binding.dobEt.showSoftInputOnFocus = false
        binding.dobEt.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        binding.dobEt.setOnClickListener {

            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                binding.dobEt.setText(
                    resources.getString(
                        R.string.personal_data_label_text_dob, year, month + 1, dayOfMonth
                    )
                )
            }
            datePickerDialog.show()
        }

    }

    /**
     * METHOD FOR INITIALIZING DEPARTMENT AND COURSE DROP DOWNS
     */
    private fun initDeptAndCourseDD() {
        val departmentList = listOf("CSE", "ECE", "EEE", "ME", "CE")
        val departmentAdapter =
            ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, departmentList)
        binding.departmentActv.setAdapter(departmentAdapter)

        val courseList = listOf("B.Tech", "M.Tech", "B.E.")
        val courseAdapter =
            ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, courseList)
        binding.courseActv.setAdapter(courseAdapter)
    }

    /**
     * METHOD FOR SETTING GENDER RADIO BUTTONS ON PROFILE LOAD
     */
    private fun setRadioButtons(radio: String = "M") {
        if (radio == "M") {
            binding.maleRadio.isChecked = true
            binding.maleRadioLayout.setBackgroundResource(R.drawable.radio_checked)
            binding.femaleRadioLayout.setBackgroundResource(R.drawable.radio_regular)
            binding.femaleRadio.isChecked = false
        } else {
            binding.femaleRadio.isChecked = true
            binding.femaleRadioLayout.setBackgroundResource(R.drawable.radio_checked)
            binding.maleRadioLayout.setBackgroundResource(R.drawable.radio_regular)
            binding.maleRadio.isChecked = false
        }
    }

    /**
     * METHOD FOR EDITING PROFILE IMAGE BY LAUNCHING CAMERA, GALLERY OR FILE INTENT
     */
    private fun editProfileImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
            }
        val fileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent, fileIntent))

        getImageCallback.launch(chooserIntent)
    }

    /**
     * CALLBACK FOR GETTING IMAGE FROM INTENT
     */
    private var getImageCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {

                //in camera intent data is null
                //in gallery intent extras is null
                var imageData =
                    if (it.data?.data != null) it.data?.data else it.data?.extras?.get("data")

                if (imageData is Bitmap) imageData =
                    Utils.getUriFromBitmap(imageData, contentResolver)

                var uri: Uri? = null

                //async call to create temp file
                CoroutineScope(Dispatchers.Main).launch {
                    uri = Uri.fromFile(withContext(Dispatchers.IO) {
                        File.createTempFile( //creating temp file
                            "temp", ".jpg", cacheDir
                        )
                    })
                }
                    //on completion of async call
                    .invokeOnCompletion {
                        //Crop activity
                        val uCrop = UCrop.of(imageData as Uri, uri!!).withAspectRatio(1f, 1f)
                            .withMaxResultSize(1080, 1080)

                        cropImageCallback.launch(uCrop.getIntent(this))
                    }
            }
        }

    /**
     * CALLBACK FOR CROPPING RECEIVED IMAGE
     */
    private var cropImageCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = UCrop.getOutput(it.data!!)

                //deleting clicked image
                val file = File("/storage/emulated/0/Pictures/Title.jpg")
                if (file.exists()) file.delete()

                ProfilePrefs.saveImageToStorage(this, uri!!)
                //setting image to image view
                Glide.with(this).load(uri).into(binding.personalDataIv)
            }
        }
}