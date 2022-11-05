package com.fipl.mypraticalkhushaltask.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.fipl.mypraticalkhushaltask.retrofit.RestClient
import com.fipl.mypraticalkhushaltask.utils.MyUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Part
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class OnRegisterModel : ViewModel(){

    lateinit var getResponse: LiveData<RegisterPojo>
    lateinit var mContext: Context
    var isShowing: Boolean = false
    var json: String = ""
    var jsonaction: String = ""
    var jsondata: String = ""
    var jsonemail: String = ""
    var jsonfirstname: String = ""
    var jsonlastname: String = ""
    var jsonpassword: String = ""
    var jsonage: String = ""
    var jsongender: String = ""
    var jsonuser_type: String = ""
    var jsondevicetoken: String = ""
    var jsondevicetype: String = ""
    var photo : File?= null
    var from = -1

    fun apiFunction(context: Context, isShowing: Boolean,
                          json: String,action: String,data : String,email : String,password : String,firstname: String,lastname:String,
                    age:String,gender:String,
                    user_type :String,devicetoken:String,devicetype:String,file: File?
    ): LiveData<RegisterPojo> {
        this.mContext = context
        this.isShowing = isShowing
        this.jsonaction= action
        this.jsondata= data
        this.jsonemail= email
        this.jsonfirstname= firstname
        this.jsonlastname= lastname
        this.jsonpassword= password
        this.jsonage= age
        this.jsongender= gender
        this.jsonuser_type=user_type
        this.jsondevicetoken= devicetoken
        this.jsondevicetype= devicetype
        this.photo = file
        getResponse = apiREsponse()
        return getResponse
    }
    private fun apiREsponse(): LiveData<RegisterPojo> {
        if (isShowing) {
            MyUtils.showProgressDialog(this!!.mContext!!)
        }
        val data = MutableLiveData<RegisterPojo>()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val filePart = MultipartBody.Part.createFormData(
            "photo",
            "IMG_" + timeStamp + ".jpg",
            RequestBody.create(MediaType.parse("image*//*"), photo!!)
        )

        var call = RestClient.get()!!.registration(
            RequestBody.create(MediaType.parse("text/plain")!!, jsonaction),
            RequestBody.create(MediaType.parse("text/plain")!!, jsondata),
            RequestBody.create(MediaType.parse("text/plain")!!, jsonemail),
            RequestBody.create(MediaType.parse("text/plain")!!, jsonpassword),
            RequestBody.create(MediaType.parse("text/plain")!!, jsonfirstname),
            RequestBody.create(MediaType.parse("text/plain")!!, jsonlastname),
            RequestBody.create(MediaType.parse("text/plain")!!, jsonage),
            RequestBody.create(MediaType.parse("text/plain")!!, jsongender),
            RequestBody.create(MediaType.parse("text/plain")!!, jsonuser_type),
            RequestBody.create(MediaType.parse("text/plain")!!, jsondevicetoken),
            RequestBody.create(MediaType.parse("text/plain")!!, jsondevicetype),
            filePart)

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