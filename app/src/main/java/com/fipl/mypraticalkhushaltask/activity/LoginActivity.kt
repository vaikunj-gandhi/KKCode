package com.fipl.mypraticalkhushaltask.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fipl.mypraticalkhushaltask.R
import com.fipl.mypraticalkhushaltask.model.OnBoardingModel
import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.fipl.mypraticalkhushaltask.retrofit.RestClient
import com.fipl.mypraticalkhushaltask.utils.MyUtils
import com.fipl.mypraticalkhushaltask.utils.ShowSnackBar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var objMessages: ShowSnackBar? = null
    var ch_sign_in: CheckBox? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        objMessages = ShowSnackBar(this, rootLoginLayout)

        editEmailMobilenoLogin?.requestFocus()
        val imm: InputMethodManager =
            this@LoginActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editEmailMobilenoLogin, InputMethodManager.SHOW_IMPLICIT)

        ch_sign_in = findViewById(R.id.ch_sign_in) as CheckBox
        ch_sign_in?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    editPasswordLogin?.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    // hide password
                    editPasswordLogin?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
                editPasswordLogin?.setSelection(editPasswordLogin?.text.toString().length)
            }
        })

        editPasswordLogin?.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                return if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSiginIn.performClick()
                    true
                } else false
            }
        })


        registerNestedScrollView.post(Runnable {
            registerNestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN)

        })

        btnSiginIn?.setOnClickListener {

            MyUtils.hideKeyboardFrom(this@LoginActivity, btnSiginIn)

            if (checkValidation()) {

                if (MyUtils.isInternetAvailable(this@LoginActivity)) {

                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                        try {
                            if (!task.isSuccessful) {
                                Log.w("System out", "getInstanceId failed", task.exception)
                                return@OnCompleteListener
                            }
                        } catch (e: Exception) {
                        }
                        val newtoken = task.result
//                        Log.e("System out", "device token:= "+newtoken!!)

                        apiCallLogin(newtoken!!)
                    })

                } else {

                    MyUtils.showSnackbarkotlin(
                        this@LoginActivity,
                        rootLoginLayout,
                        resources.getString(R.string.error_common_network)
                    )
                }

            }
        }

        btnSumbitReg?.setOnClickListener {
            val myIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(myIntent)
        }


    }

    private fun checkValidation(): Boolean {

        var checkFlag = true

        if (editEmailMobilenoLogin!!.text.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(this@LoginActivity,rootLoginLayout,"Please enter email Id")
            checkFlag = false
        } else if (!MyUtils.isEmailValid(editEmailMobilenoLogin?.text.toString().trim())) {
            MyUtils.showSnackbarkotlin(
                this@LoginActivity,
                rootLoginLayout,"Please enter valid email Id")
            checkFlag = false
        } else if (editPasswordLogin?.text.toString().trim().isEmpty()) {
            MyUtils.showSnackbarkotlin(
                this@LoginActivity,
                rootLoginLayout,
                "Please enter password")
            checkFlag = false
        }
        return checkFlag
    }



    private fun apiCallLogin(newtoken: String) {


        try {
            btnSiginIn.startAnimation()
        } catch (e: Exception) {
        }

        MyUtils.setViewAndChildrenEnabled(rootLoginLayout, false)
        val myOrderListModel =
            ViewModelProviders.of(this@LoginActivity).get(OnBoardingModel::class.java)
        myOrderListModel.apiFunction(this@LoginActivity, false, "","userAuthentication",RestClient.data,
            "${editEmailMobilenoLogin?.text.toString().trim()}","${editPasswordLogin?.text.toString().trim()}",RestClient.user_type,newtoken,
            RestClient.apiType)
            .observe(this@LoginActivity,
                Observer { response ->
                    try {
                        if (btnSiginIn != null)
                            btnSiginIn.endAnimation()
                    } catch (e: Exception) {
                    }

                    MyUtils.setViewAndChildrenEnabled(rootLoginLayout, true)
                    if (response != null) {
                        var data: RegisterPojo.Result? = null
                        data = response.result
                        if(response.status.equals("1")){
                            if(data != null){
                                editEmailMobilenoLogin?.setText("")
                                editPasswordLogin?.setText("")

                                MyUtils.storeSessionManager(this, data.user)
                                objMessages?.customMessage(response.message!!)
                                Handler().postDelayed(Runnable {
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    overridePendingTransition(
                                        R.anim.slide_in_right,
                                        R.anim.slide_out_left
                                    )
                                    finishAffinity()

                                }, 1500)

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
