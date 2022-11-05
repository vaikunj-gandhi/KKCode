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

class OnUserDataModel : ViewModel(){

    lateinit var getResponse: LiveData<RegisterPojo>
    lateinit var mContext: Context
    var isShowing: Boolean = false
    var json: String = ""
    var jsonaction: String = ""
    var jsondata: String = ""
    var jsonUserId: String? = ""

    var from = -1

    fun apiFunction(context: Context, isShowing: Boolean,
                          json: String,action: String,data : String,UserId : String?): LiveData<RegisterPojo> {
        this.mContext = context
        this.isShowing = isShowing
        this.json = json
        this.jsonaction= action
        this.jsondata= data
        this.jsonUserId= UserId

        getResponse = apiREsponse()
        return getResponse
    }
    private fun apiREsponse(): LiveData<RegisterPojo> {
        if (isShowing) {
            MyUtils.showProgressDialog(this!!.mContext!!)
        }
        val data = MutableLiveData<RegisterPojo>()
        var call = RestClient.get()!!.getUserDetails(jsonaction,jsondata,jsonUserId!!)

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