package com.fipl.mypraticalkhushaltask.utils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.util.Log
import com.fipl.mypraticalkhushaltask.R


import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson


class SessionManager {
    companion object {
              val KEY_Currentlocation:String="Location"
              val PREF_NAME:String="LoginSession"
              val KEY_IS_LOGGEDIN:String="isLoggedIn"
              val KEY_USER_OBJ:String="AuthenticateUser"
              val KEY_USER_SETTINGS:String="DefaultSettings"
              val KEY_USER_EMAIL:String="UserEmail"
              val KEY_USER_PASSWORD:String="Location"
              val KEY_SYNCED:String = "Synced"
              val KEY_USER_SOCIAL:String = "isSocial"
              val KEY_IsVerify:String = "isVerify"
              val Key_Email:String= "isEmailLogin"
              val Key_PosterId :String= "posterId"
              val Key_DeviceToken:String = "deviceToken"//fireBase
              val Key_PushCounter:String = "pushCounter"//fireBase
              val TAG = SessionManager::class.java.simpleName
              val Key_Seller_Admin = "SELLERADMIN"


    }

      var pref: SharedPreferences
      var editor: SharedPreferences.Editor
      var _context: Context
      var PRIVATE_MODE = 0

    public constructor(context: Context){
        this._context = context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
    public fun create_login_session(AuthenticateUser:String,email:String,password:String, isVerify:Boolean,isEmail:Boolean){
        clear_login_session()
        editor.putBoolean(KEY_IS_LOGGEDIN,true)
        editor.putString(KEY_USER_OBJ,AuthenticateUser)
        editor.putString(Key_Email,email)
        editor.putString(KEY_USER_PASSWORD,password)
        editor.putBoolean(KEY_IsVerify,isVerify)
        editor.putBoolean(Key_Email,isEmail)
        editor.commit()
        Log.d(TAG, "CouponCodeList login session modified!");


    }

   public fun syncContacts(Synced: String) {
        editor.putString(KEY_SYNCED, Synced)
        editor.commit()
    }

    public fun Current_city(Synced: String) {
        editor.putString(KEY_Currentlocation, Synced)
        editor.commit()
    }

    public fun getPreferences(Key: String): String {
        return pref.getString(Key, "")!!
    }

    public fun isLoggedIn(): Boolean {

        if ((!getIsVerify()!!)!!)
            clear_login_session()
        return pref.getBoolean(KEY_IS_LOGGEDIN, false)
    }

    public fun isEmailLogin(): Boolean {

        return pref.getBoolean(Key_Email, false)
    }

    public fun getISsellerAdmin(): Boolean {
        return pref.getBoolean(Key_Seller_Admin, false)
    }

    public fun issellerAdmin(status : Boolean){
        editor.putBoolean(Key_Seller_Admin, status)
    }

    fun get_Authenticate_User(): RegisterPojo.Result.User {

        val gson = Gson()
        val json = pref.getString(KEY_USER_OBJ, "")
        return gson.fromJson<Any>(json, RegisterPojo.Result.User::class.java) as RegisterPojo.Result.User
    }

    var bookingId: String
        get() = pref.getString("bookingId", "-1")!!
        set(bookingId) {
            editor.putString("bookingId", bookingId)
            editor.commit()
        }

    var tripLogType: String
        get() = pref.getString("tripLogType", "No Trip")!!
        set(tripLogType) {
            editor.putString("tripLogType", tripLogType)
            editor.commit()
        }

    var tripType: String
        get() = pref.getString("tripType", "")!!
        set(tripType) {
            editor.putString("tripType", tripType)
            editor.commit()
        }



    public fun set_UserSettings(defaultSettings: String) {

        editor.putString(KEY_USER_SETTINGS, defaultSettings)
        editor.commit()
        Log.d(TAG, "CouponCodeList settings modified!")
    }

    public fun get_Email(): String {
        return pref.getString(KEY_USER_EMAIL, "")!!
    }

    public fun get_Password(): String {
        return pref.getString(KEY_USER_PASSWORD, "")!!
    }

    public fun getIsVerify(): Boolean? {
        return pref.getBoolean(KEY_IsVerify, false)
    }

    public fun setIsVerify(isVerfiy: Boolean?) {
        editor.putBoolean(KEY_IsVerify, isVerfiy!!)
        // commit changes
        editor.commit()
        Log.d(TAG, "CouponCodeList login session modified!")
    }



    public fun getPosterId(): String {
        return pref.getString(Key_PosterId, "")!!
    }

    public fun setPosterId(posterId: String) {
        editor.putString(Key_PosterId, posterId)
        // commit changes
        editor.commit()

    }

    public fun getDeviceToken(): String {
        return pref.getString(Key_DeviceToken, "")!!
    }

    public fun setDeviceToken(posterId: String) {
        editor.putString(Key_DeviceToken, posterId)
        editor.commit()

    }

    public fun sessionExpire(context : Context,okListener: DialogInterface.OnClickListener): Dialog {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setMessage(R.string.session_expire)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok", okListener)

        val alert = builder.create()
//        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.show()
        alert.setCanceledOnTouchOutside(false)

        return alert
    }

    public fun set_password(password: String) {
        editor.putString(KEY_USER_PASSWORD, password)
        // commit changes
        editor.commit()
        Log.d(TAG, "CouponCodeList login session modified!")
    }

    public fun clear_login_session() {
        editor.clear()
        editor.commit()
    }

}