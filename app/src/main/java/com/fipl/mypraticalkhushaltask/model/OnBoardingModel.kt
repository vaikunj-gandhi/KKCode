package com.fipl.mypraticalkhushaltask.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.fipl.mypraticalkhushaltask.retrofit.RestClient
import com.fipl.mypraticalkhushaltask.utils.MyUtils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnBoardingModel : ViewModel(){

    lateinit var getResponse: LiveData<RegisterPojo>
    lateinit var mContext: Context
    var isShowing: Boolean = false
    var json: String = ""
    var jsonaction: String = ""
    var jsondata: String = ""
    var jsonemail: String = ""
    var jsonpassword: String = ""
    var jsonuser_type: String = ""
    var jsondevicetoken: String = ""
    var jsondevicetype: String = ""
    var from = -1

    fun apiFunction(context: Context, isShowing: Boolean,
                          json: String,action: String,data : String,email : String,password : String,
                    user_type :String,devicetoken:String,devicetype:String): LiveData<RegisterPojo> {
        this.mContext = context
        this.isShowing = isShowing
        this.json = json
        this.jsonaction= action
        this.jsondata= data
        this.jsonemail= email
        this.jsonpassword= password
        this.jsonuser_type=user_type
        this.jsondevicetoken= devicetoken
        this.jsondevicetype= devicetype
        getResponse = apiREsponse()
        return getResponse
    }
    private fun apiREsponse(): LiveData<RegisterPojo> {
        if (isShowing) {
            MyUtils.showProgressDialog(this!!.mContext!!)
        }
        val data = MutableLiveData<RegisterPojo>()
        var call = RestClient.get()!!.login(jsonaction,jsondata,jsonemail,jsonpassword,jsonuser_type,jsondevicetoken,jsondevicetype)

        call.enqueue(object : Callback<RegisterPojo> {
            override fun onResponse(call: Call<RegisterPojo>, response: Response<RegisterPojo>) {
                if (isShowing)
                    MyUtils.closeProgress()
                if(response.isSuccessful){
                    data.value = response.body()

                }else {
                    data.value = null
                }
            }
            override fun onFailure(call: Call<RegisterPojo>, t: Throwable) {
                if (isShowing)
                    MyUtils.closeProgress()
                data.value = null
            }
        })
        return data
    }
}