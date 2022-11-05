package com.fipl.mypraticalkhushaltask.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fipl.mypraticalkhushaltask.R
import com.fipl.mypraticalkhushaltask.model.OnBoardingModel
import com.fipl.mypraticalkhushaltask.model.OnUserDataModel
import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.fipl.mypraticalkhushaltask.retrofit.RestClient
import com.fipl.mypraticalkhushaltask.utils.MyUtils
import com.fipl.mypraticalkhushaltask.utils.SessionManager
import com.fipl.mypraticalkhushaltask.utils.ShowSnackBar

import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity() {
    var sessionManager: SessionManager? = null
    var userData: RegisterPojo.Result.User? = null
    var objMessages: ShowSnackBar? = null
    var Gender : String?=null
    var doubleBackToExitPressedOnce = false
    var userId : String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        objMessages = ShowSnackBar(this, rootRegisterLayout)
        sessionManager = SessionManager(this@MainActivity)
        try {
            if(sessionManager != null && sessionManager?.isLoggedIn()!!){
                userData = sessionManager?.get_Authenticate_User()
                userId = userData?.userId
            }else {
                userData = null
                userId = "0"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        layoutPwd?.visibility = View.GONE
        editDateofBirthReg?.isEnabled = false
        editDateofBirthReg?.isClickable = false
        btnSumbitReg?.visibility = View.GONE
        btnCancelReg?.visibility = View.GONE
        btnLogoutReg?.visibility = View.VISIBLE

        if(userData != null){
            userId = userData?.userId
            if (MyUtils.isInternetAvailable(this@MainActivity!!)) {
                apiCallUserDetails()
            }else {
                MyUtils.showSnackbarkotlin(this@MainActivity!!, rootRegisterLayout, this@MainActivity!!.resources.getString(R.string.error_common_network))
            }
        }

        btnLogoutReg?.setOnClickListener {
            MyUtils.showProgressDialog(this@MainActivity!!)
            val sessionManager = SessionManager(this@MainActivity)
            sessionManager.clear_login_session()
            MyUtils.showSnackbarkotlin(this@MainActivity!!, rootRegisterLayout, "Logout Successfully")
            Handler().postDelayed(Runnable {
                MyUtils.closeProgress()
                var mIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(mIntent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finishAffinity()

            }, 1000)


        }


    }

    fun setUserData(){

        if(userData != null){
            if (!userData?.firstname.isNullOrEmpty() ){
                editFirstNameReg?.setText("${userData?.firstname}")
            }else{
                editFirstNameReg?.setText("")
            }

            if (!userData?.email.isNullOrEmpty() ){
                editEmailAddressReg?.setText("${userData?.email}")
            }else{
                editEmailAddressReg?.setText("")
            }

            if (!userData?.gender.isNullOrEmpty() ){
                Gender = userData?.gender!!
                if(userData?.gender.equals("Male")){
                    rbMale.isChecked = true
                    rbFemale.isChecked = false
                }else {
                    rbMale.isChecked = false
                    rbFemale.isChecked = true
                }
            }

            var imgUri = ""
            if (!userData?.photo.isNullOrEmpty()){
                imgUri = userData?.photo!!
            }

            if (imgUri.isNullOrEmpty()) {
                svUserProfile?.setActualImageResource(R.drawable.user_icon_profile)
            } else svUserProfile?.setImageURI(Uri.parse(imgUri),this@MainActivity!!)

            if (!userData?.age.isNullOrEmpty() ){
                editDateofBirthReg?.setText("${userData?.age}")
            }else{
                editDateofBirthReg?.setText("")
            }

        }


    }

    override fun onBackPressed() {
        showexit()

    }

    private fun showexit() {
        //Store Cart Data before exit app
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
            return
        }
        doubleBackToExitPressedOnce = true
        MyUtils.showSnackbarkotlin(this@MainActivity!!, rootRegisterLayout, "To exit, press back again.")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 3000)

    }

    private fun apiCallUserDetails() {

        val myOrderListModel =
            ViewModelProviders.of(this@MainActivity).get(OnUserDataModel::class.java)
        myOrderListModel.apiFunction(this@MainActivity, true, "","getUserDetails",
            RestClient.data,
            userId.toString())
            .observe(this@MainActivity,
                Observer { response ->

                    if (response != null) {
                        var data: RegisterPojo.Result? = null
                        data = response.result
                        if(response.status.equals("1")){
                            if(data != null){

                                MyUtils.storeSessionManager(this, data.user)
                                objMessages?.customMessage(response.message!!)
                                sessionManager = SessionManager(this@MainActivity)
                                try {
                                    if(sessionManager != null && sessionManager?.isLoggedIn()!!){
                                        userData = sessionManager?.get_Authenticate_User()
                                        userId = userData?.userId
                                    }else {
                                        userData = null
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                layoutlastName?.visibility = View.GONE
                                layoutPwd?.visibility = View.GONE
                                setUserData()
                            }else {
                                if (MyUtils.isInternetAvailable(this)) {
                                    if (response.message.isNullOrEmpty()) {
                                        objMessages?.customMessage(resources.getString(R.string.msg_fail_to_login))
                                    } else objMessages?.customMessage(response.message!!)
                                } else objMessages?.showNoInterNet()
                            }
                        }else {
                            if (MyUtils.isInternetAvailable(this)) {
                                if (response.message.isNullOrEmpty()) {
                                    objMessages?.customMessage(resources.getString(R.string.msg_fail_to_login))
                                } else objMessages?.customMessage(response.message!!)
                            } else objMessages?.showNoInterNet()
                        }

                    } else {
                        if (MyUtils.isInternetAvailable(this)) objMessages?.showSomthingRong() else objMessages?.showNoInterNet()
                    }
                })

    }
}