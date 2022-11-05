package com.fipl.mypraticalkhushaltask.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fipl.mypraticalkhushaltask.R
import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.fipl.mypraticalkhushaltask.utils.*
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    var sessionManager: SessionManager? = null
    var userData: RegisterPojo.Result.User? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sessionManager = SessionManager(this@SplashActivity)
        tvVersionCodeName?.visibility = View.VISIBLE
        try {
            if(sessionManager != null && sessionManager?.isLoggedIn()!!){
                userData = sessionManager?.get_Authenticate_User()
            }else {
                userData = null
            }
            getVersionInfo()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        proceedAhead()

    }


    fun proceedAhead(){
        Handler().postDelayed({
            if (userData != null ) {
                val myIntent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(myIntent)
                finishAffinity()
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )

            }else {
                val myIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(myIntent)
                finishAffinity()
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }

        }, 3000)
    }


    fun getVersionInfo() {
        var versionName: String = ""
        var versionCode: Int = -1
        try {
            var packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
        Log.e("System Out", "Version Code: " + String.format("V. %s", versionName))
//        Log.d("System Out", "Version Code: " + String.format("V. %s", versionName))

        tvVersionCodeName?.text = "" + String.format("V. %s", versionName).toString()
    }



}
