package com.fipl.mypraticalkhushaltask.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.fipl.mypraticalkhushaltask.R


class ProgressHUD : Dialog {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, theme: Int) : super(context, theme) {}

    /* public void onWindowFocusChanged(boolean hasFocus) {
        ProgressBar imageView = (ProgressBar) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }*/

    fun setMessage(message: CharSequence?) {
        if (message != null && message.length > 0) {
            findViewById<View>(R.id.message).visibility = View.VISIBLE
            val txt = findViewById<View>(R.id.message) as TextView
            txt.text = message
            txt.invalidate()
        }
    }

    companion object {

        fun show(context: Context, message: CharSequence?, indeterminate: Boolean, cancelable: Boolean,
                 cancelListener: DialogInterface.OnCancelListener?): ProgressHUD {
            val dialog = ProgressHUD(context, R.style.ProgressHUD)
            dialog.setTitle("")
            dialog.setContentView(R.layout.custom_progressbar)
            if (message == null || message.length == 0) {
                dialog.findViewById<View>(R.id.message).visibility = View.GONE
            } else {
                val txt = dialog.findViewById<View>(R.id.message) as TextView
                txt.text = message
            }
            dialog.setCancelable(cancelable)
            dialog.setOnCancelListener(cancelListener)
            dialog.window!!.attributes.gravity = Gravity.CENTER
            val lp = dialog.window!!.attributes
            lp.dimAmount = 0.2f
            dialog.window!!.attributes = lp
            //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            try {
                if (!(context is Activity && (context as Activity).isFinishing)) {
                    if (!dialog.isShowing) {
                        dialog.show()
                    }else {
                        dialog.dismiss()
                    }
                }else {
                    dialog.dismiss()
                }
            }catch (ee : WindowManager.BadTokenException ){
                ee.printStackTrace()
            }
            catch (ex: Exception) {
                ex.printStackTrace()
            }


            return dialog
        }
    }
}
