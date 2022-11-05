package com.fipl.mypraticalkhushaltask.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.fipl.mypraticalkhushaltask.R


class ShowSnackBar(contex : AppCompatActivity, rootLayout : View){

    val context = contex
    val rootLayout = rootLayout

    fun showNoInterNet(){
        MyUtils.showSnackbarkotlin(context, rootLayout, context.resources.getString(R.string.error_common_network))
    }

    fun showSomthingRong(){
        MyUtils.showSnackbarkotlin(context, rootLayout, context.resources.getString(R.string.error_crash_error_message))
    }

    fun customMessage(message : String){
        MyUtils.showSnackbarkotlin(context, rootLayout, message)
    }

}