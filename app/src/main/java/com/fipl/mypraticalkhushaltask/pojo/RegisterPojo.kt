package com.fipl.mypraticalkhushaltask.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RegisterPojo : Serializable {
    @SerializedName("RESULT")
    @Expose
    var result: Result? = null

    @SerializedName("STATUS")
    @Expose
    var status: String? = null

    @SerializedName("MESSAGE")
    @Expose
    var message: String? = null

    class Result: Serializable {
        @SerializedName("user")
        @Expose
        var user: User? = null

        class User : Serializable {
            @SerializedName("user_id")
            @Expose
            var userId: String? = null

            @SerializedName("email")
            @Expose
            var email: String? = null

            @SerializedName("password")
            @Expose
            var password: String? = null

            @SerializedName("firstname")
            @Expose
            var firstname: String? = null

            @SerializedName("photo")
            @Expose
            var photo: String? = null

            @SerializedName("age")
            @Expose
            var age: String? = null

            @SerializedName("gender")
            @Expose
            var gender: String? = null

            @SerializedName("user_type")
            @Expose
            var userType: String? = null

            @SerializedName("fb_id")
            @Expose
            var fbId: String? = null

            @SerializedName("devicetype")
            @Expose
            var devicetype: String? = null

            @SerializedName("devicetoken")
            @Expose
            var devicetoken: String? = null

            @SerializedName("activation_token")
            @Expose
            var activationToken: String? = null

            @SerializedName("lastlogin_datetime")
            @Expose
            var lastloginDatetime: String? = null

            @SerializedName("created_datetime")
            @Expose
            var createdDatetime: String? = null

            @SerializedName("is_active")
            @Expose
            var isActive: String? = null
        }
    }
}