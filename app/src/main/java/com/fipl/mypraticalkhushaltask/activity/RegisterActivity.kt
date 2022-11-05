package com.fipl.mypraticalkhushaltask.activity

import android.Manifest
import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fipl.mypraticalkhushaltask.R
import com.fipl.mypraticalkhushaltask.model.OnRegisterModel
import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.fipl.mypraticalkhushaltask.retrofit.RestClient
import com.fipl.mypraticalkhushaltask.utils.MyUtils
import com.fipl.mypraticalkhushaltask.utils.ShowSnackBar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat

class RegisterActivity : AppCompatActivity() {

    val currentapiVersion = Build.VERSION.SDK_INT
    var mYear: Int = 0
    var cYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var dateSpecified: Date? = null
    var today: Date? = null
    var dob = ""
    var str_birthday = ""
    var Gender = ""

    var Mobileno = ""
    private var mediaChooseBottomSheet = MediaChooseBottomSheet()
    var enterFirstNametoast = ""
    var enterFamilyNametoast = ""
    var enterOtherNametoast = ""
    var enterDateOfBirthtoast = ""
    var enterPlaceOfBirthtoast = ""
    var minAge = ""
    var enterMobileNo = ""
    var enterEmailAdress = ""
    var enterCorrectEmail = ""
    var enterPasswordtoast = ""
    var enterselectgender = ""
    var strongPassword = ""
    var Enter_referral_code_Optional = ""
    var minimuFamilyName = ""
    var minimuFirstName = ""
    var minimuLastName = ""
    var minimuOtherName = ""
    var minimuPlaceOfBirth = ""
    var enterMinPassword = ""
    var enterSamepassword = ""

    var err_special_character = ""
    var messageNoData = ""
    var messageNoInternet = ""
    var messageSomthingRong = ""
    var mobileNumberNineCharacter = ""
    var objMessages: ShowSnackBar? = null
    private var AcceptTermsAndPoilicy = false
    var chregiin: CheckBox? = null

    private var actualImage: File? = null
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var compressedImage: File? = null
    private val TAKE_PICTURE = 1
    private val SELECT_PICTURE = 2
    private var pictureUri: Uri? = null
    private var timeForImageName: Long = 0
    private var imgName: String? = null
    private var picturePath: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        staitcLabel()

        objMessages = ShowSnackBar(this, rootRegisterLayout)

        svUserProfile?.setOnClickListener {
            val currentapiVersion = Build.VERSION.SDK_INT
            if (currentapiVersion >= Build.VERSION_CODES.M) {
                getWriteStoragePermissionOther()
            } else {
                mediaChooseBottomSheet.show(this@RegisterActivity!!.supportFragmentManager, "BottomSheet demoFragment")
            }
        }

        chregiin = findViewById(R.id.ch_regi_in) as CheckBox
        chregiin?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    editPasswordReg?.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    // hide password
                    editPasswordReg?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
                editPasswordReg?.setSelection(editPasswordReg?.text.toString().length)
            }
        })

        editFirstNameReg.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
        editFirstNameReg.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub

            }

            @SuppressLint("LongLogTag")
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub

                if (editFirstNameReg!!.text.toString().length > 0) {
                    try {

                        var x: Char
                        val t = IntArray(editFirstNameReg!!.text.toString().length)

                        for (i in 0 until editFirstNameReg!!.text.toString().length) {
                            x = editFirstNameReg!!.text.toString().toCharArray()[i]
                            val z = x.toInt()
                            t[i] = z

                            if (z > 64 && z < 91
                                || z > 96 && z < 123 || z == 32
                            ) {

                            } else {
                                /*if (z > 47 && z < 58) {

                                } else {*/
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "" + err_special_character,
                                        Toast.LENGTH_SHORT
                                    ).show()
//                                }
                                var ss = editFirstNameReg!!.text.toString()
                                    .substring(0, editFirstNameReg!!.text.toString().length - 1)
                                editFirstNameReg.setText(ss)
                                editFirstNameReg.setSelection(editFirstNameReg!!.text.toString().length)


                            }

                        }
                    } catch (e: IndexOutOfBoundsException) {
//                        Log.d("IndexOutOfBoundsException", e.toString())
                    }
                }
            }
        })


        editLastNameReg.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
        editLastNameReg.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub

            }

            @SuppressLint("LongLogTag")
            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub

                if (editLastNameReg!!.text.toString().length > 0) {
                    try {

                        var x: Char
                        val t = IntArray(editLastNameReg!!.text.toString().length)

                        for (i in 0 until editLastNameReg!!.text.toString().length) {
                            x = editLastNameReg!!.text.toString().toCharArray()[i]
                            val z = x.toInt()
                            t[i] = z

                            if (z > 64 && z < 91
                                || z > 96 && z < 123 || z == 32
                            ) {

                            } else {
                                /*if (z > 47 && z < 58) {

                                } else {*/
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "" + err_special_character,
                                    Toast.LENGTH_SHORT
                                ).show()
//                                }
                                var ss = editLastNameReg!!.text.toString()
                                    .substring(0, editLastNameReg!!.text.toString().length - 1)
                                editLastNameReg.setText(ss)
                                editLastNameReg.setSelection(editLastNameReg!!.text.toString().length)


                            }

                        }
                    } catch (e: IndexOutOfBoundsException) {
//                        Log.d("IndexOutOfBoundsException", e.toString())
                    }
                }
            }
        })

        RadioGender?.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {

                R.id.rbFemale -> Gender = "female"
                R.id.rbMale -> Gender = "Male"
            }
        }

        editDateofBirthReg?.setOnClickListener {
            MyUtils.hideKeyboardFrom(this@RegisterActivity, editDateofBirthReg)
            DataPickerDialog()
        }

        /*editPasswordReg?.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                return if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSumbitReg.performClick()
                    true
                } else false
            }
        })*/

        btnSumbitReg?.setOnClickListener {
            MyUtils.hideKeyboardFrom(this@RegisterActivity, btnSumbitReg)

            if (checkValidation()) {

                getFirebaseToken()

            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getWriteStoragePermissionOther() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            getReadStoragePermissionOther()
        } else {
            ActivityCompat.requestPermissions(
                this@RegisterActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MyUtils.Per_REQUEST_WRITE_EXTERNAL_STORAGE_1
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getReadStoragePermissionOther() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            getCameraPermissionOther()
        } else {
            ActivityCompat.requestPermissions(
                this@RegisterActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MyUtils.Per_REQUEST_READ_EXTERNAL_STORAGE_1
            )
        }
    }

    fun getCameraPermissionOther() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            mediaChooseBottomSheet.show(this@RegisterActivity!!.supportFragmentManager, "BottomSheet demoFragment")

        } else {
            ActivityCompat.requestPermissions(this@RegisterActivity,arrayOf(CAMERA), MyUtils.Per_REQUEST_CAMERA_1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MyUtils.Per_REQUEST_WRITE_EXTERNAL_STORAGE_1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getReadStoragePermissionOther()
                } else {
                    showSettingsDialog()
                }
            }
            MyUtils.Per_REQUEST_READ_EXTERNAL_STORAGE_1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCameraPermissionOther()
                } else {
                    showSettingsDialog()
                }
            }

            MyUtils.Per_REQUEST_CAMERA_1 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mediaChooseBottomSheet.show(this@RegisterActivity!!.supportFragmentManager, "BottomSheet demoFragment")

            } else {
                showSettingsDialogCamara()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            timeForImageName = System.currentTimeMillis()
            imgName = "img$timeForImageName.jpg"

            when (requestCode) {
                TAKE_PICTURE -> if (mediaChooseBottomSheet.selectedImage() != null) {
                    pictureUri = mediaChooseBottomSheet.selectedImage()
                    picturePath = pictureUri?.path
                    svUserProfile!!.setImageURI(Uri.fromFile(File(picturePath)), this@RegisterActivity)
                    actualImage = File(picturePath)
                }
                SELECT_PICTURE -> {
                    pictureUri = data!!.data
                    picturePath = MyUtils.getPath(pictureUri, this@RegisterActivity)

                    svUserProfile!!.setImageURI(Uri.fromFile(File(picturePath)), this@RegisterActivity)
                    actualImage = File(picturePath)
                }
            }
        }else if(requestCode == 56){
            if (currentapiVersion >= Build.VERSION_CODES.M) {
                getWriteStoragePermissionOther()
            } else {
                mediaChooseBottomSheet.show(this@RegisterActivity!!.supportFragmentManager, "BottomSheet demoFragment")
            }
        }

    }

    private fun showSettingsDialog() {
        MyUtils.showMessageGotoSettingPostitiveButton(
            this,
            "Need Permissions",
            "" + "Allow Permission Request to access photos, media, and files on yourdevice? Without this permission the app is unable to access your photos, media, etc.. from your phone. You can grant them in app settings.",
            "GOTO SETTINGS",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    openSettings()

                }
            })
    }

    private fun showSettingsDialogCamara() {
        MyUtils.showMessageGotoSetting(
            this@RegisterActivity!!, "Need_Permissions",
            ""+this@RegisterActivity.resources.getString(R.string.msg_picture_access_camera) ,
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    openSettings()
                }
            })
    }



    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + this@RegisterActivity.packageName)
        startActivityForResult(intent, 56)
    }

    private fun staitcLabel() {

        enterFirstNametoast = "Please enter first name"
        enterFamilyNametoast = "Please enter middle name"
        enterOtherNametoast = "Please enter last name"
        enterDateOfBirthtoast = "Please select date of birth"
        enterPlaceOfBirthtoast = "Please enter city of birth"
        minAge = "Age Should Be Min 18 Years"
        enterMobileNo = "Please enter Phone Number"
        enterEmailAdress = "Please enter email address"
        enterCorrectEmail = "Please enter valid email Id"
        enterPasswordtoast = "Please enter password"
        enterselectgender = "Please select gender"


        strongPassword = "Password must contain at least 6 characters including Upper,lowercase ,special characters and numbers"

        minimuFirstName = "First name should be minimum of 3 characters"
        minimuLastName = "Last name should be minimum of 3 characters"
        minimuPlaceOfBirth = "Birth place should be minimum of 3 characters"
        enterMinPassword = "Please enter minimum 6 character password"
        enterSamepassword = "Password and verify password doesnt match"
        enterSamepassword = "Password and verify password doesnt match"
        mobileNumberNineCharacter = "Mobile number must contain at least 10 characters"

        messageNoData = this@RegisterActivity.resources.getString(R.string.no_data_found)
        messageNoInternet = this@RegisterActivity.resources.getString(R.string.error_common_network)
        messageSomthingRong = this@RegisterActivity.resources.getString(R.string.error_crash_error_message)
        err_special_character = "Special Character not allowed"

    }

    private fun DataPickerDialog() {
        val c = Calendar.getInstance()
        today = c.getTime()
        mYear = c.get(Calendar.YEAR) - 18
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val mincalendar = Calendar.getInstance()
        mincalendar.set(mYear, mMonth, mDay)
//        this@RegisterActivity,R.style.my_dialog_theme,

        val dpd = DatePickerDialog(
            this@RegisterActivity,R.style.MyDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                Log.d("year", year.toString() + "")
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateSpecified = c.getTime()

                val minAdultAge = GregorianCalendar()
                minAdultAge.add(Calendar.YEAR, -18)
                if (minAdultAge.before(c)) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "" + "" + minAge,
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    dob = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                    cYear = year
                    str_birthday = dob
                    try {
                        editDateofBirthReg.setText(
                            MyUtils.formatDate(
                                dob,
                                "yyyy-MM-dd",
                                "dd MMM yyyy"
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }, mYear, mMonth, mDay
        )
        dpd.datePicker.maxDate = mincalendar.getTimeInMillis()
        dpd.show()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        MyUtils.hideKeyboard1(this@RegisterActivity)
        MyUtils.finishActivity(this@RegisterActivity, true)
    }

    override fun onDestroy() {
        super.onDestroy()
    }



    private fun checkValidation(): Boolean {
        var checkFlag = true

        if (editFirstNameReg?.text.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterFirstNametoast
            )
            checkFlag = false
        } else if (editFirstNameReg?.text.toString().trim().length < 3) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                minimuFirstName
            )
            checkFlag = false
        }else if (editLastNameReg?.text.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterOtherNametoast
            )
            checkFlag = false
        } else if (editLastNameReg?.text.toString().trim().length < 3) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                minimuLastName
            )
            checkFlag = false
        }
        else if (editEmailAddressReg?.text.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterEmailAdress
            )
            checkFlag = false
        } else if (!MyUtils.isEmailValid(editEmailAddressReg?.text.toString().trim())) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterCorrectEmail
            )
            checkFlag = false

        }
        else if (editPasswordReg?.text.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterPasswordtoast
            )
            checkFlag = false
        } else if (editPasswordReg?.text.toString().length < 6) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterMinPassword
            )
            checkFlag = false
        }else if(MyUtils.isValidPassword(editPasswordReg?.text.toString().trim()) == false){
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                strongPassword
            )

            checkFlag = false
        }else if (Gender.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterselectgender
            )
            checkFlag = false
        }
        else if (editDateofBirthReg?.text.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                enterDateOfBirthtoast
            )
            checkFlag = false
        }else if(actualImage == null){
            MyUtils.showSnackbarkotlin(
                this@RegisterActivity,
                rootRegisterLayout,
                "Please Select Profile Picture"
            )
            checkFlag = false
        }

        return checkFlag
    }


    fun getFirebaseToken() {
        if(MyUtils.isInternetAvailable(this@RegisterActivity)){

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                try {
                    if (!task.isSuccessful) {
                        Log.w("System out", "getInstanceId failed", task.exception)
    //                    MyUtils.showSnackbarkotlin(this@SplashActivity, splashLLMain, resources.getString(R.string.try_later))
                        return@OnCompleteListener
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val newtoken = task.result
//                Log.e("System out", "device token:= "+newtoken!!)

                apiCallForRegister(newtoken!!)
            })


        }else {
            MyUtils.showSnackbarkotlin(this@RegisterActivity,rootRegisterLayout!!,resources.getString(R.string.error_common_network))
        }


    }

    private fun apiCallForRegister(newtoken: String) {
        try {
            btnSumbitReg.startAnimation()
        } catch (e: Exception) {
        }

        MyUtils.setViewAndChildrenEnabled(rootRegisterLayout, false)
        val age = 0
         try {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            currentYear - cYear
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val myOrderListModel = ViewModelProviders.of(this@RegisterActivity).get(OnRegisterModel::class.java)
        myOrderListModel.apiFunction(this@RegisterActivity, false, "", "userRegistration",RestClient.data,
            "${editEmailAddressReg?.text.toString().trim()}","${editPasswordReg?.text.toString().trim()}",
            "${editFirstNameReg?.text.toString().trim()}",
            "${editLastNameReg?.text.toString().trim()}",
            age.toString(),"$Gender",
            RestClient.user_type,newtoken,RestClient.apiType,actualImage)
            .observe(this@RegisterActivity,
                Observer { response ->
                    try {
                        if (btnSumbitReg != null)
                            btnSumbitReg.endAnimation()
                    } catch (e: Exception) {
                    }

                    MyUtils.setViewAndChildrenEnabled(rootRegisterLayout, true)
                    if (response != null) {
                        var data: RegisterPojo.Result? = null
                        data = response.result
                        if(response.status!!.equals("1")){
                            if(data != null){
                                MyUtils.storeSessionManager(this@RegisterActivity, response.result?.user)
                                objMessages?.customMessage(response.message!!)
                                Handler().postDelayed(Runnable {
                                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                    finishAffinity()

                                }, 1500)
                            }else {
                                if (MyUtils.isInternetAvailable(this)) {
                                    if (response.message.isNullOrEmpty()){
                                        objMessages?.customMessage(resources.getString(R.string.msg_fail_to_register))
                                    }else objMessages?.customMessage(response.message!!)
                                }else objMessages?.showNoInterNet()
                            }

                        }else {
                            if (MyUtils.isInternetAvailable(this)) {
                                if (response.message.isNullOrEmpty()){
                                    objMessages?.customMessage(resources.getString(R.string.msg_fail_to_register))
                                }else objMessages?.customMessage(response.message!!)
                            }else objMessages?.showNoInterNet()
                        }
                    }else {
                        if (MyUtils.isInternetAvailable(this)) objMessages?.showSomthingRong() else objMessages?.showNoInterNet()
                    }

                })
    }


}
