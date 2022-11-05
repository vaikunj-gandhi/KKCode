package com.fipl.mypraticalkhushaltask.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CommonPojo {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}