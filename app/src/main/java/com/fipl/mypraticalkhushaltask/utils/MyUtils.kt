package com.fipl.mypraticalkhushaltask.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.provider.Settings
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.fipl.mypraticalkhushaltask.R
import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo
import com.fipl.mypraticalkhushaltask.pojo.RegisterPojo1

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson


import java.io.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Chandresh on 15/03/2017.
 */

object MyUtils {

    var selectedDate: Date? = null


    var currentLat = 0.0
    var currentLong = 0.0
    var isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    var timeToAcceptOrder = 30
    var serverLiveTime = ""
    internal lateinit var userDialog: Dialog
    var mHtmlText: String =
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."

    fun addPermission(activity: Activity, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun returnFloat(rating: String): Float {

        return if (rating.isEmpty()) {
            0f
        } else {
            java.lang.Float.valueOf(rating)
        }
    }

    fun priceFormat(price: Double?): String {

        val priceFormat = price.toString()
        //        priceFormat = new DecimalFormat("#,##,##0.00").format(price);
        try {
            return String.format("%,.2f", price)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return priceFormat
    }

    fun priceFormat(price: String): String {
        var priceFormat1 = price
        val priceFormat = java.lang.Double.parseDouble(price)

        try {
            priceFormat1 = DecimalFormat("#,##,###.00").format(priceFormat)
            return String.format("%,.2f", priceFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return price


    }

    fun updateServerTime(serverTime: String, counter: Int): String {
        val calender = Calendar.getInstance()
        calender.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(serverLiveTime)
        calender.set(Calendar.SECOND, calender.get(Calendar.SECOND) + counter)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calender?.time!!)
    }

    fun create_logcatNew(context: Context) {
        val cacheDir: File

        if (android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED)
            cacheDir = File(android.os.Environment.getExternalStorageDirectory(), ""+context.resources.getString(R.string.app_name))
        else
            cacheDir = context.cacheDir

        if (!cacheDir.exists())
            cacheDir.mkdirs()

        val logFile = File(cacheDir, "logoutput.log") // log file name
        val sizePerFile = 60 // size in kilobytes
        val rotationCount = 10 // file rotation count
        val filter = "D" // Debug priority

        val args = arrayOf(
            "logcat",
            "-v",
            "time",
            "-f",
            logFile.absolutePath,
            "-r",
            Integer.toString(sizePerFile),
            "-n",
            Integer.toString(rotationCount),
            "*:$filter"
        )

        try {
            Runtime.getRuntime().exec(args)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun checkCurretDate(srcDateS: String, destDateS: String): Boolean {

        var destDate: Date? = null;
        var srcDate: Date? = null;
        var c: Calendar = Calendar.getInstance();
        // System.out.println("Current time => " + c.getTime());
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
        var formattedDate: String = sdf?.format(c.getTime());
        Log.d("System out", "srcDate:  " + srcDateS);
        Log.d("System out", "destDate:  " + destDateS);

        try {
            destDate = sdf.parse(destDateS);
            srcDate = sdf.parse(srcDateS);
            if (srcDate.equals(destDate)) {
                return true;
            } else {
                return false;
            }
            /*else if(srcDate.before(destDate))
            {
                return true;
            }else if(srcDate.after(destDate))
            {
                return false;
            }*/

            /*if (date1.before(date2)) {
                return true;
            } else {
                return false;
            }*/
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    fun checkCurretDate1(pass: String?): Boolean {

        var date2: Date? = null;
        var date1: Date? = null;
        var c: Calendar = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
        var formattedDate = sdf?.format(c.getTime());
        try {
            date2 = sdf.parse(formattedDate);
            date1 = sdf.parse(pass);

            if (date1.before(date2)) {
                return true;
            } else {
                return false;
            }

        } catch (e: ParseException) {
            e.printStackTrace();
        }

        return false;
    }


    fun fromHtml(source: String?): Spanned {
        return when {
            source == null -> Html.fromHtml("")
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> Html.fromHtml(
                source,
                Html.FROM_HTML_MODE_LEGACY
            )
            else -> @Suppress("DEPRECATION")
            Html.fromHtml(source)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun formatDate(date: String, initDateFormat: String, endDateFormat: String): String {
        val initDate = SimpleDateFormat(initDateFormat).parse(date)
        val formatter = SimpleDateFormat(endDateFormat)
        return formatter.format(initDate)
    }

    fun formatTime(time: String, initDateFormat: String): String {
        var time24: String = SimpleDateFormat("HH:mm:ss").format(
            SimpleDateFormat(initDateFormat, Locale.US).parse(time)
        )
//        Log.e("System out","final lll : "+ time24+" llll");
        return time24
        //End "HH:mm:ss"
        //Start HH:mm
    }

    fun formatTimeToAMPM(time: String, initDateFormat: String): String {
        var time24: String = SimpleDateFormat("hh:mm:ss a").format(
            SimpleDateFormat(initDateFormat, Locale.US).parse(time)
        )
//        Log.e("System out","final lll : "+ time24+" llll");
        return time24
        //End "HH:mm:ss"
        //Start HH:mm
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        System.out.println(dateFormat.format(date)) //2016/11/16 12:08:43
        return dateFormat.format(date)
    }

    fun getNextDate(curDate: String): String {
        val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");
        val date: Date = format.parse(curDate)
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return format.format(calendar.time)
    }

    fun getPath(contentUri: Uri?, activity: Activity): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor?.moveToFirst()!!) {
            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor?.getString(column_index!!)
        }
        cursor?.close()
        return res
    }

    fun showDialogMessage(c: Context, title: String, body: String) {

        val builder = AlertDialog.Builder(c)
        builder.setTitle(title).setMessage(body).setNeutralButton("OK") { dialog, which ->
            try {
                userDialog.dismiss()

            } catch (e: Exception) {

            }
        }
        userDialog = builder.create()
        userDialog.show()
    }

    fun removeLastComma(str: String): String {
        var str = str
        val x = ""
        val lastCommaIndex = 0
        try {
            if (str.length > 1 && str.endsWith(", ")) {
                str = str.substring(0, str.length - 2)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str

    }

    fun removeLastCommaId(str: String): String {
        var str = str
        try {
            if (str.length > 1 && str.endsWith(",")) {
                str = str.substring(0, str.length - 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str

    }

    fun removeLastLastCharacter(str: String, format : String): String {
        var str = str
        try {
            if (str.length > 1 && str.endsWith(format)) {
                str = str.substring(0, str.length - format.length)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str

    }

    fun dismissDialog(context: Context, dialog: Dialog) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                val cx = dialog.window!!.decorView.width / 2
                val cy = dialog.window!!.decorView.height / 2


                val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()


                val anim = ViewAnimationUtils.createCircularReveal(
                    dialog.window!!.decorView,
                    cx,
                    cy,
                    initialRadius,
                    0f
                )


                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {

                        dialog.dismiss()

                    }
                })
                anim.start()
            } else
                dialog.dismiss()
        } catch (e: Exception) {
        }

    }

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     */
    fun milliSecondsToTimer(milliseconds: Long): String {
        var finalTimerString = ""
        var secondsString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours.toString() + ":"
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0$seconds"
        } else {
            secondsString = "" + seconds
        }

        finalTimerString = "$finalTimerString$minutes:$secondsString"

        // return timer string
        return finalTimerString
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {
        var percentage: Double? = 0.toDouble()

        val currentSeconds = (currentDuration / 1000).toInt().toLong()
        val totalSeconds = (totalDuration / 1000).toInt().toLong()

        // calculating percentage
        percentage = currentSeconds.toDouble() / totalSeconds * 100

        // return percentage
        return percentage.toInt()
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    fun progressToTimer(progress: Int, totalDuration: Int): Int {
        var totalDuration = totalDuration
        var currentDuration = 0
        totalDuration = totalDuration / 1000
        currentDuration = (progress.toDouble() / 100 * totalDuration).toInt()

        // return current duration in milliseconds
        return currentDuration * 1000
    }

    val Per_REQUEST_ACCESS_FINE_LOCATION = 1044
    val Per_REQUEST_ACCESS_FINE_LOCATIONOS10 = 1022
    val Per_REQUEST_WRITE_EXTERNAL_STORAGE_1 = 1053
    val Per_REQUEST_READ_EXTERNAL_STORAGE_1 = 1054
    val Per_REQUEST_CAMERA_1 = 1057
    val GSTINFORMAT_REGEX =
        "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}"
    val GSTN_CODEPOINT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val TEXT_TYPE = 0
    val Loder_TYPE = 1
    var menu_selection = 0
    var LOG = true
    var imageName = "snapzo.png"
    var isIntentServiceRunning = false
    var dialog: Dialog? = null

    fun expand(v: View) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targtetHeight = v.measuredHeight

        v.layoutParams.height = 0
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targtetHeight * interpolatedTime).toInt()
                v.requestLayout()

                Log.d("System out", "Interpolated time : $interpolatedTime")

            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = (targtetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboardWithFocus(v: View, a: Activity) {
        try {
            v.requestFocus()
            Handler().postDelayed({
                val imm = a.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }, 50)
            // a.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun hideKeyboard(activity: Activity, v: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun validGSTIN(gstin: String): Boolean {
        var isValidFormat = false
        if (checkPattern(gstin, GSTINFORMAT_REGEX)) {
            isValidFormat = verifyCheckDigit(gstin)
        }
        return isValidFormat

    }

    /**
     * Method for checkDigit verification.
     *
     * @param gstinWCheckDigit
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun verifyCheckDigit(gstinWCheckDigit: String): Boolean {
        var isCDValid: Boolean? = false
        val newGstninWCheckDigit = getGSTINWithCheckDigit(
            gstinWCheckDigit.substring(0, gstinWCheckDigit.length - 1)
        )

        if (gstinWCheckDigit.trim { it <= ' ' } == newGstninWCheckDigit) {
            isCDValid = true
        }
        return isCDValid!!
    }

    fun showSnackbarkotlin(context: Activity?, view: View, msg: String) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLACK)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.btn_color))
        val tv = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, context!!.resources.getDimension(R.dimen._16sdp))
        tv.maxLines = 4
        snackbar.show()
    }

    fun showSnackbarkotlin(context: AppCompatActivity?, view: View, msg: String) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLACK)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.btn_color))
        val tv = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, context!!.resources.getDimension(R.dimen._16sdp))
        tv.maxLines = 4
        snackbar.show()
    }

    fun showSnackbarkotlin1(context: AppCompatActivity?, view: View, msg: String) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLACK)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.black))
        val tv = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, context!!.resources.getDimension(R.dimen._16sdp))
        tv.maxLines = 4
        snackbar.show()
    }

    fun showSnackbarkotlinThems(context: AppCompatActivity?, view: View, msg: String) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.BLACK)
        val sbView = snackbar.view
        sbView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.btn_color))
        val tv = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tv.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, context!!.resources.getDimension(R.dimen._16sdp))
        tv.maxLines = 4
        snackbar.show()
    }

    fun getPath1(uri: Uri, activity: Activity): String? {

        return getFilePathFromURI(activity, uri, createFileName(Date(), ""))

    }

    fun getFilePathFromURI(context: Context, contentUri: Uri, fileName: String?): String? {
        //copy file and send new file path

        if (!TextUtils.isEmpty(fileName)) {

            val copyFile = File(context.cacheDir, fileName!!)

            copy(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    fun copy(context: Context, srcUri: Uri, dstFile: File) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
            val outputStream = FileOutputStream(dstFile)
            //            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    fun createImageFile(today: Date, tyope: String): File? {
        // Create an image file name
        val imageFileName = createFileName(today, tyope)
        val albumF = File(Environment.getExternalStorageDirectory().absolutePath + "/Sanpzo")
        if (!albumF.exists()) {
            if (!albumF.mkdirs()) {
                return null
            }
        }
        return File.createTempFile(imageFileName!!, "JPEG", albumF)
    }

    val outputMediaFile: File?
        get() {
            val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ), "CameraDemo"
            )

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            return File(
                mediaStorageDir.path + File.separator +
                        "IMG_" + timeStamp + ".jpg"
            )
        }

    fun createFileName(today: Date, type: String): String? {
        var filename: String? = null
        try {
            val dateFormatter = SimpleDateFormat("dd_MM_yyyy_hh_mm_ss")
            dateFormatter.isLenient = false
            //Date today = new Date();
            val s = dateFormatter.format(today)
            val min = 1
            val max = 1000

            val r = Random()
            val i1 = r.nextInt(max - min + 1) + min

            if (type.equals("Video", ignoreCase = true)) {
                filename = "VIDEO_" + s + i1.toString() + ".mp4"
            } else if (type.equals("Audio", ignoreCase = true)) {
                filename = "Audio" + s + i1.toString() + ".mp3"
            } else {
                filename = "IMG_" + s + i1.toString() + ".jpg"
            }

            //filename = "IMG_"+s+String.valueOf(i1)+".JPEG";

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return filename
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        v.startAnimation(a)
    }

    /**
     * Checks if the input parameter is a valid email.
     * **
     */


    fun isValidEmail(email: String): Boolean {
        val emailPattern =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val matcher: Matcher?
        val pattern = Pattern.compile(emailPattern)

        matcher = pattern.matcher(email)

        return matcher?.matches() ?: false
    }


    /**
     * Checks if the input parameter is a valid Name
     * **
     */
    fun isValidName(name: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z\\s]*$")
        val matcher = pattern.matcher(name)
        when {
            name.isEmpty() -> false
            matcher.matches() -> //if pattern matches
                true
            else -> //if pattern does not matches
                false
        }
        return false

    }


    // password validation check one lowercase,one uppercase,special symbol,numeric


    fun isValidPassword(pwd: String): Boolean {
        val pattern = Pattern.compile("(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#\$%^&+=]).{6,16}$")
        return if (pwd.isNullOrEmpty()) {
            false
        } else if (pwd.matches(pattern.toRegex())) {
            return true
        } else {
            return false
        }
    }

    fun isValidGSTNumber(gstNumber: String): Boolean {
        val pattern = Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}\$")
        return if (gstNumber.isNullOrEmpty()) {
            false
        } else if (gstNumber.matches(pattern.toRegex())) {
            return true
        } else {
            return false
        }
    }


    /**
     * hide soft keyboard
     * **
     */
    fun hideSoftKeyBoardOnTabClicked(context: Context?, v: View?) {
        if (v != null && context != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                v.applicationWindowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * get progressbar dialog
     * **
     */

    fun get_dialog(context: Context, layoutid: Int): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutid)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent)))
        return dialog
    }

    /**
     * Get the correctly appended name from the given name parameters
     *
     * @param firstName First name
     * @param lastName  Last name
     * @return Returns correctly formatted full name. Returns null if both the values are null.
     * **
     */

    fun create_logcat(context: Context) {
        val fullName = context.getString(R.string.app_name) + "log.txt"
        val file = File(Environment.getExternalStorageDirectory(), fullName)

        // //clears a file
        if (file.exists()) {
            file.delete()
            clear_log()

        }
        //
        //
        // //write log to file
        try {
            val command = String.format("logcat -d")
            val process = Runtime.getRuntime().exec(command)

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val result = StringBuilder()

            //
            while ((reader.readLine() != null)) {
                //

                result.append(reader.readLine())
                result.append("\n")
                //
            }
            //
            val out = FileWriter(file)
            out.write(result.toString())
            out.close()
            //
            //
        } catch (e: IOException) {
            // // Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        //
    }

    fun clear_log() {
        //clear the log
        try {
            Runtime.getRuntime().exec("logcat -c")
        } catch (e: IOException) {

        }

    }

    fun show_message(msg: String, tv_msg: TextView) {

        tv_msg.text = msg
        tv_msg.clearAnimation()

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator() //add this
        fadeIn.duration = 3000

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator() //and this
        fadeOut.startOffset = 3000
        fadeOut.duration = 3000

        val animation = AnimationSet(false) //change to false
        animation.addAnimation(fadeIn)
        animation.addAnimation(fadeOut)

        animation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationEnd(animation: Animation) {
                // TODO Auto-generated method stub
                tv_msg.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {
                // TODO Auto-generated method stub

            }

            override fun onAnimationStart(animation: Animation) {
                // TODO Auto-generated method stub
                tv_msg.visibility = View.VISIBLE

            }

        })
        tv_msg.animation = animation
    }

    /**
     * Checks if the Internet connection is available.
     *
     * @return Returns true if the Internet connection is available. False otherwise.
     * *
     */
    @SuppressLint("MissingPermission")
    fun isInternetAvailable(ctx: Context): Boolean {
        val check = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (check != null) {
            val info = check.allNetworkInfo
            if (info != null)
                for (i in info.indices)
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                        //Toast.makeText(context, "Internet is connected", Toast.LENGTH_SHORT).show();
                    }
        }
        return false
    }

    fun startActivity(
        oneActivity: Context,
        secondActivity: Class<*>,
        finishCurrentActivity: Boolean
    ) {
        val i = Intent(oneActivity, secondActivity)
        oneActivity.startActivity(i)
        if (finishCurrentActivity)
            (oneActivity as Activity).finish()

        (oneActivity as Activity).overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )

    }

    fun getResourseId(
        context: Context,
        pVariableName: String,
        pResourseName: String,
        pPackageName: String
    ): Int {

        try {
            return context.resources.getIdentifier(pVariableName, pResourseName, pPackageName)

        } catch (e: Exception) {
            throw RuntimeException("Error getting ResoureId.", e)
        }

    }

    fun finishActivity(activity: Context, lefttorightanimation: Boolean) {
        (activity as Activity).finish()
        if (lefttorightanimation)
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        else
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }

    fun startActivity(
        oneActivity: Context,
        secondActivity: Class<*>,
        finishCurrentActivity: Boolean,
        lefttorightanimation: Boolean
    ) {
        val i = Intent(oneActivity, secondActivity)
        oneActivity.startActivity(i)
        if (finishCurrentActivity)
            (oneActivity as Activity).finish()

        if (lefttorightanimation)
            (oneActivity as Activity).overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        else
            (oneActivity as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )

    }

    fun showSnackbar(c: Context, msg: String, v: View) {
        val snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view

        sbView.setBackgroundColor(ContextCompat.getColor(c, R.color.colorSecondary))
        snackbar.show()
    }

    fun showMessageYesNo_(
        context: Context,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context,R.style.MaterialAlertDialogg)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setNegativeButton("No, Thanks") { dialog, which -> dialog.dismiss() }
        builder.setPositiveButton("Give Permission", okListener)
        val alert = builder.create()
//        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.show()

        return alert
    }

    fun openPermissionScreen(ctx : Context) {
        val inent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        inent.data = Uri.fromParts("package", ctx!!.packageName, null)
        ctx!!.startActivity(inent)
    }

    fun showMessageGotoSettingPostitiveButton(
        context: Context,
        title:String,
        message: String,positiveBtnText :String,
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogg)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(""+positiveBtnText, okListener)

//        builder.setNegativeButton(""+nagativeBtnText) { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()

        return alert
    }

    fun showMessageGotoSetting(
        context: Context,
        title:String,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogg)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("GOTO SETTINGS", okListener)

//        builder.setNegativeButton("No Thanks") { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()

        return alert
    }

    fun showMessageGotoSettingSplash(
        context: Context,
        title:String,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogg)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("GOTO SETTINGS", okListener)

//        builder.setNegativeButton("No Thanks") { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()

        return alert
    }

    fun showMessageYesNo1(
        context: Context,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("No"){ dialog, which -> dialog.dismiss() }

        builder.setNegativeButton("Yes", okListener)
        val alert = builder.create()
// alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.show()

        return alert
    }



    fun isValidMobile(phone: String): Boolean {
        var check = false
        check = if (!Pattern.matches("[a-zA-Z]+", phone)) {
            !(phone.length < 9 || phone.length > 13)
        } else {
            false
        }
        return check
    }

    /*public static String convert_object_string(Object obj) {
Gson gson = new Gson();
String json = gson.toJson(obj); // myObject - instance of MyObject
return json;
}*/

    fun isVaildPassword(password: String): Boolean? {
        var check = false

        if (password.length >= 6) {
            check = true
        }
        return check
    }

    /*@Throws(ParseException::class)
    fun formatDate(date: String, initDateFormat: String, endDateFormat: String): String {
        val initDate = SimpleDateFormat(initDateFormat).parse(date.trim { it <= ' ' })
        val formatter = SimpleDateFormat(endDateFormat)
        return formatter.format(initDate)
    }*/

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }


    fun dismissView(context: Context, view: View) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                val cx = view.width / 2
                val cy = view.height / 2


                val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()


                val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)


                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {

                        view.visibility = View.GONE

                    }
                })
                anim.start()
            } else
                view.visibility = View.GONE

        } catch (e: Exception) {
        }

    }

    @Throws(ParseException::class)
    fun getDisplayableTime(minute: Long): String {
        var minute = minute

        var isRemaining = false
        if (minute < 0)
            isRemaining = true

        minute = Math.abs(minute)


        val seconds = minute * 60
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 31
        val years = days / 365
        val week = days / 7


        if (seconds < 0) {
            return "not yet"
        } else if (seconds == 0L) {
            return "Just now"
        } else if (seconds == 1L) {
            return "Just now"
        } else if (seconds < 60 && seconds > 2) {
            return if (!isRemaining)
                seconds.toString() + " seconds"
            else
                seconds.toString() + " seconds "
        } else if (seconds < 120) {
            return if (!isRemaining)
                "a minute ago"
            else
                "a minute remaining"
        } else if (seconds < 2700)
        // 45 * 60
        {
            return if (!isRemaining)
                minutes.toString() + " minutes"
            else
                minutes.toString() + " minutes "
        } else if (seconds < 5400)
        // 90 * 60
        {
            return if (!isRemaining)
                "an hour"
            else
                "an hour "
        } else if (seconds < 86400)
        // 24  60  60
        {
            return if (!isRemaining)
                hours.toString() + " hours"
            else
                hours.toString() + " hours "
        } else if (days >= 1 && days <= 6) {
            return if (!isRemaining) {
                if (days == 1L)
                    "1 day "
                else
                    days.toString() + " days "
            } else {
                if (days == 1L)
                    "1 day "
                else
                    days.toString() + " days "
            }
        } else if (week >= 1) {
            return if (week == 1L)
                week.toString() + " week "
            else
                week.toString() + " weeks "
        }

        return minute.toString() + " ago"
    }

    fun showMessageOKCancel1(
        context: Context,
        message: String,
        title: String = "",
        okListener: DialogInterface.OnClickListener
    ): MaterialAlertDialogBuilder {
        val builder = MaterialAlertDialogBuilder(context,R.style.MaterialAlertDialogg)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok", okListener)

        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }

        builder.show()

        return builder
    }


    fun showMessageOKCancel(
        context: Context,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): Dialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogg)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok", okListener)

        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
//        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.show()

        return alert
    }

    fun showMessageYesNo(
        context: Context,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogg)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("Yes", okListener)

        builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
        alert.setCanceledOnTouchOutside(false)

        return alert
    }

    fun showMessageYesNo(
        context: Context,
        message: String, title: String = "",
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogg)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setCancelable(false)
        builder.setPositiveButton("Yes", okListener)

        builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()

        return alert
    }


    fun showMessageRecheckConfirm(
        context: Context,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialogg)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("Confirm", okListener)

        builder.setNegativeButton("Recheck") { dialog, which -> dialog.dismiss() }
        val alert = builder.create()
//        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.show()

        return alert
    }

    fun showMessageYesNoBothClick(
        context: Context,
        message: String,
        yesListener: DialogInterface.OnClickListener,
        noListener: DialogInterface.OnClickListener
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("Yes", yesListener)

        builder.setNegativeButton("No", noListener) /*{ dialog, which -> dialog.dismiss() }*/
        val alert = builder.create()
//        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.show()

        return alert
    }

    fun showMessageOK(
        context: Context,
        message: String,
        okListener: DialogInterface.OnClickListener
    ): Dialog {
//        val builder = android.app.AlertDialog.Builder(context)
        val builder = MaterialAlertDialogBuilder(context,  R.style.MaterialAlertDialogg)
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok", okListener)


        val alert = builder.create()
//        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alert.show()

        return alert
    }

    /*fun frescoImageRoatate(mDraweeMain: SimpleDraweeView, imagePath: String) {

        try {

            if (imagePath.length > 0 && imagePath.indexOf("&") > -1) {

                val uri = imagePath.substring(0, imagePath.indexOf("&"))
                if (imagePath.indexOf("&w=") > 0 && imagePath.indexOf("&h=") > 0) {
                    val w = imagePath.substring(imagePath.indexOf("&w=") + 3, imagePath.indexOf("&h="))
                    val h = imagePath.substring(imagePath.indexOf("&h=") + 3)

                    val request = ImageRequestBuilder
                        .newBuilderWithSource(Uri.parse(uri))
                        .setResizeOptions(ResizeOptions(Integer.valueOf(w), Integer.valueOf(h)))
                        .setLocalThumbnailPreviewsEnabled(true).build()
                    val controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(mDraweeMain.controller)
                        .build()
                    mDraweeMain.controller = controller
                } else {
                    val timThumbPath = imagePath.replace(RestClient.image_base_url, RestClient.timthumb_path1)

                    mDraweeMain.setImageURI(Uri.parse(timThumbPath))
                }


            } else {
                val timThumbPath = imagePath.replace(RestClient.image_base_url, RestClient.timthumb_path1)

                mDraweeMain.setImageURI(Uri.parse(timThumbPath))
            }
        } catch (e: Exception) {

            val timThumbPath = imagePath.replace(RestClient.image_base_url, RestClient.timthumb_path1)

            mDraweeMain.setImageURI(Uri.parse(timThumbPath))
        }

    }*/

    fun setViewAndChildrenEnabled(v: View, enabled: Boolean) {
        v.isEnabled = enabled
        if (v is ViewGroup) {

            for (i in 0 until v.childCount) {
                var child = v.getChildAt(i)
                setViewAndChildrenEnabled(child, enabled)
            }
        }
    }



    fun ValidPANNumber(Pan: String): Boolean {
        val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher = pattern.matcher(Pan)
        return matcher.matches()

    }

    fun validateEmail(email: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun hideKeyboard1(ctx: Context) {
        val inputManager = ctx
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val v = (ctx as Activity).currentFocus ?: return

        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun commaToArray(text: String): List<String> {

        return Arrays.asList(*text.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

    /**
     * Method to check if a GSTIN is valid. Checks the GSTIN format and the
     * check digit is valid for the passed input GSTIN
     *
     * @param gstin
     * @return boolean - valid or not
     * @throws Exception
     */


    fun checkPattern(inputval: String, regxpatrn: String): Boolean {
        var result = false
        if (inputval.trim { it <= ' ' }.matches(regxpatrn.toRegex())) {
            result = true
        }
        return result
    }

    /**
     * Method to get the check digit for the gstin (without checkdigit)
     *
     * @param gstinWOCheckDigit
     * @return : GSTIN with check digit
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getGSTINWithCheckDigit(gstinWOCheckDigit: String?): String {
        var factor = 2
        var sum = 0
        var checkCodePoint = 0
        var cpChars: CharArray?
        var inputChars: CharArray?

        try {
            if (gstinWOCheckDigit == null) {
                throw Exception("GSTIN supplied for checkdigit calculation is null")
            }
            cpChars = GSTN_CODEPOINT_CHARS.toCharArray()
            inputChars = gstinWOCheckDigit.trim { it <= ' ' }.toUpperCase().toCharArray()

            val mod = cpChars.size
            for (i in inputChars.indices.reversed()) {
                var codePoint = -1
                for (j in cpChars.indices) {
                    if (cpChars[j] == inputChars[i]) {
                        codePoint = j
                    }
                }
                var digit = factor * codePoint
                factor = if (factor == 2) 1 else 2
                digit = digit / mod + digit % mod
                sum += digit
            }
            checkCodePoint = (mod - sum % mod) % mod
            return gstinWOCheckDigit + cpChars[checkCodePoint]
        } finally {
            inputChars = null
            cpChars = null
        }
    }

    fun formatPrice(totalOneProductPrice: String?): Double {
        val priceFormat = java.lang.Double.parseDouble(totalOneProductPrice!!)
        var roundofValue: String? = null

        var df = DecimalFormat("#.##")
//        df.roundingMode = RoundingMode.CEILING
        roundofValue = df.format(priceFormat)

        return roundofValue.toDouble()
    }

    fun formatPrice(totalOneProductPrice: Double?): Double {
        var roundofValue: String? = null

        var df = DecimalFormat("#.##")
//        df.roundingMode = RoundingMode.CEILING
        roundofValue = df.format(totalOneProductPrice)

        return roundofValue.toDouble()
    }

    fun priceFormat1(price: Double?): String {

        val priceFormat = price.toString()
        //        priceFormat = new DecimalFormat("#,##,##0.00").format(price);
        try {
            return String.format("%,.2f", price)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return priceFormat
    }


    fun coordinatesFormat(price: Double?): String {

        val priceFormat = price.toString()
        //        priceFormat = new DecimalFormat("#,##,##0.00").format(price);
        try {
            return String.format("%,.4f", price)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return priceFormat
    }

    fun priceFormat1(price: String): String {
        var priceFormat1 = price
        val priceFormat = java.lang.Double.parseDouble(price)

        try {
            priceFormat1 = DecimalFormat("#,##,###.00").format(priceFormat)
            return String.format("%,.2f", priceFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return price


    }


    @JvmStatic
    val EMAIL_REGEX =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    fun isEmailValid(email: String): Boolean {
        return EMAIL_REGEX.toRegex().matches(email)
    }

    @SuppressLint("MissingPermission")
    fun internetConnectionCheck(ctx: Context): Boolean {
        var Connected: Boolean? = false
        val connectivity = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null)
                for (i in info.indices)
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        Log.e("My Network is: ", "Connected ")
                        Connected = true
                    } else {
                    }

        } else {
            Log.e("My Network is: ", "Not Connected")

            Toast.makeText(
                ctx.applicationContext,
                "Please Check Your internet connection",
                Toast.LENGTH_LONG
            ).show()
            Connected = false

        }
        return Connected!!
    }

    fun calculatePrice(flag: Int, numberOfProduct: Double, productPrice: Double): Double? {
        var totalOneProductPrice: Double? = null
        when (flag) {
            0 -> {
                totalOneProductPrice = numberOfProduct.toDouble() + 1
            }

            1 -> {
                totalOneProductPrice = numberOfProduct.toDouble() - 1
            }

            2 -> {
                totalOneProductPrice = (productPrice * numberOfProduct)
            }
            4 -> {
                totalOneProductPrice = numberOfProduct + productPrice
            }
            5 -> {
                totalOneProductPrice = numberOfProduct - productPrice
            }
        }

        return formatPrice(totalOneProductPrice)
    }

    fun calculateGST(subtotalValue: Double): Double {
        var totalGSTAmount: Double? = null
        var roundofValue: String? = null

        var GSTValue: Int = 18
        var divideByValue: Int = 100
        totalGSTAmount = ((subtotalValue * GSTValue) / divideByValue)

        return formatPrice(totalGSTAmount)
    }


    fun showProgress(activity: Activity) {
        try {
            if (dialog != null && dialog!!.isShowing) {
                dialog!!.dismiss()
            }
            dialog = showProgressDialog(activity);
            dialog!!.show()
        } catch (e: Exception) {
        }

    }

    fun closeProgress() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    fun showProgressDialog(c: Context): Dialog {

        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }

        dialog = ProgressHUD.show(c, c!!.getString(R.string.wait), true, false, null)
        return dialog as ProgressHUD
    }

    enum class SharedPreferencesenum {
        languageId, Modeofbusiness
    }

    fun formatPricePerCountery(priceValue: Double): String {

        val format = NumberFormat.getNumberInstance(Locale.US)
//        format.setCurrency(Currency.getInstance("IND"))
        val result = format.format(priceValue)
        return result
    }


    fun checkCompareDateTime(startdatetime: String, enddatetime: String): Boolean {

        //String dateStart = "01/14/2012 09:29:58";
        //String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var d1: Date? = null
        var d2: Date? = null

        try {
            d1 = format.parse(startdatetime)
            d2 = format.parse(enddatetime)

            //in milliseconds
            val diff = d2!!.time - d1!!.time
//            val diff = d2!!.time - d1!!.time
//            Log.e(Constants.SYSTEM_TAG, "sdsfat :  $diff")

//            val diffHours = diff / (60 * 60 * 1000) % 12
            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
//            val diffDays = diff / (24 * 60 * 60 * 1000)
//            Log.e(Constants.SYSTEM_TAG, "diffHours :  $diffHours")

            val days = (diff / (1000 * 60 * 60 * 24));
            val diffHours = ((diff - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60))


//43320000 / (60 * 60 * 1000) % 12
//            print("$diffDays days, ")
            print("$diffHours hours, ")
            print("$diffMinutes minutes, ")
            print("$diffSeconds seconds.")

//            Log.e(Constants.SYSTEM_TAG, "hours,:==  $diffHours")
//            Log.e(Constants.SYSTEM_TAG, "minutes,:==  $diffMinutes")

            if (diffHours >= 1) {
                return true
//                if (diffMinutes < 30) {
//                }
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            print(" seconds.")
        }
        return false
    }

    fun priceFormatNoDot(price: String): String {
        var priceFormat1 = price
        val priceFormat = java.lang.Double.parseDouble(price)
        var temp = ""

        try {
            priceFormat1 = DecimalFormat("######").format(priceFormat)
//            return String.format("%,.2f", priceFormat)
            val tyon = Math.round(java.lang.Double.parseDouble(price!!))
            val lngDouble1 = java.lang.Double.valueOf(tyon.toDouble())

            temp = NumberFormat.getNumberInstance(Locale.getDefault()).format(lngDouble1.toInt())
            Log.e("System out", "Printe jsut iiii:  " + temp)
            return temp
//            return String.format("%,", Math.round(priceFormat))
        } catch (e: Exception) {
            e.printStackTrace()
        }



        return priceFormat1.format(java.lang.Double.valueOf(price))
//        return price


    }

    fun changeStatusBarColor(context: Activity, colorId: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = context.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = context.resources.getColor(colorId)
        }
    }


    fun getPathFromInputStreamUri(context: Context, uri: Uri?): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null

        if (uri?.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                val photoFile = createTemporalFileFrom(inputStream!!)

                filePath = photoFile?.path
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return filePath
    }

    @Throws(IOException::class)
    fun createTemporalFileFrom(inputStream: InputStream): File? {
//        throw IOException()


        if (inputStream != null) {
            var read1 = 0
            val buffer = ByteArray(8 * 1024)

            val targetFile = createTemporalFile()
            val outputStream = FileOutputStream(targetFile)
//                while ((read1 = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, read1)
//            }
            while(true){
                read1= inputStream.read(buffer)

                if(read1 == -1 ) break

                outputStream.write(buffer, 0, read1)
            }

            outputStream.flush()

            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return targetFile
        }else return null
    }

    fun createTemporalFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(path, "IMG_" + timeStamp + ".jpg")
    }

    fun checkCompareDate(startdatetime: String, enddatetime: String): Boolean {

        //String dateStart = "01/14/2012 09:29:58";
        //String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var d1: Date? = null
        var d2: Date? = null

        try {
            d1 = format.parse(startdatetime)
            d2 = format.parse(enddatetime)

            //in milliseconds
            val diff = d2!!.time - d1!!.time
//            val diff = d2!!.time - d1!!.time
//            Log.e(Constants.SYSTEM_TAG, "sdsfat :  $diff")

//            val diffHours = diff / (60 * 60 * 1000) % 12
            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
//            val diffDays = diff / (24 * 60 * 60 * 1000)
//            Log.e(Constants.SYSTEM_TAG, "diffHours :  $diffHours")

            val days = (diff / (1000 * 60 * 60 * 24));
            val diffHours = ((diff - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60))


//43320000 / (60 * 60 * 1000) % 12
//            print("$diffDays days, ")
            print("$diffHours hours, ")
            print("$diffMinutes minutes, ")
            print("$diffSeconds seconds.")

//            Log.e(Constants.SYSTEM_TAG, "hours,:==  $diffHours")
//            Log.e(Constants.SYSTEM_TAG, "minutes,:==  $diffMinutes")

            if (days > 0) {
                return true
//                if (diffMinutes < 30) {
//                }
            } else {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            print(" seconds.")
        }
        return false
    }

    fun checkCompareDateNew(startdatetime: String, enddatetime: String): Int {
        //String dateStart = "01/14/2012 09:29:58";
        //String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var d1: Date? = null
        var d2: Date? = null

        try {
            d1 = format.parse(startdatetime)
            d2 = format.parse(enddatetime)

            //in milliseconds
            val diff = d2!!.time - d1!!.time
            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val days = (diff / (1000 * 60 * 60 * 24));
            val diffHours = ((diff - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60))

//43320000 / (60 * 60 * 1000) % 12
//            print("$diffDays days, ")
            print("$diffHours hours, ")
            print("$diffMinutes minutes, ")
            print("$diffSeconds seconds.")

            Log.d("Time Difference", "$diffSeconds:$diffMinutes:$diffHours")

            var differenceSecond = diffSeconds
            var differenceMinute = diffMinutes

            if (diffHours > 0) differenceMinute += (diffHours * 60)
            if (differenceMinute > 0) differenceSecond += (differenceMinute * 60)

            if (differenceSecond < MyUtils.timeToAcceptOrder){
                return MyUtils.timeToAcceptOrder - differenceSecond.toInt()
            }  else {
                return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            print(" seconds.")
            return 0
        }
    }

    /*fun checkCompareDateNewFinalValue(startdatetime: String, enddatetime: String): DiffrentHoursMinSecond {
        //String dateStart = "01/14/2012 09:29:58";
        //String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var diffrentHoursMinSecond : DiffrentHoursMinSecond?=null
        var d1: Date? = null
        var d2: Date? = null

        try {
            d1 = format.parse(startdatetime)
            d2 = format.parse(enddatetime)

            //in milliseconds
            val diff = d2!!.time - d1!!.time
            val diffSeconds = diff / 1000 % 60
            val diffMinutes = diff / (60 * 1000) % 60
            val days = (diff / (1000 * 60 * 60 * 24))

            val diffHours = ((diff - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60))

//43320000 / (60 * 60 * 1000) % 12
//            print("$diffDays days, ")
            print("$diffHours hours, ")
            print("$diffMinutes minutes, ")
            print("$diffSeconds seconds.")

            Log.d("Time Difference", "$diffSeconds:$diffMinutes:$diffHours")

            var differenceSecond = diffSeconds
            var differenceMinute = diffMinutes

            if (diffHours > 0) differenceMinute += (diffHours * 60)
            if (differenceMinute > 0) differenceSecond += (differenceMinute * 60)

            diffrentHoursMinSecond = DiffrentHoursMinSecond(diffHours, diffMinutes,diffSeconds,differenceSecond.toLong())

            return diffrentHoursMinSecond!!
           *//* if (differenceSecond < MyUtils.timeToAcceptOrder){

            }  else {
                return 0
            }*//*
        } catch (e: Exception) {
            e.printStackTrace()
            print(" seconds.")
            return diffrentHoursMinSecond!!
        }
    }*/

    fun TextView.setSubstringTypeface(vararg textsToStyle: Pair<String, Int>) {
        val spannableString = SpannableString(this.text)
        for (textToStyle in textsToStyle) {
            val startIndex = this.text.toString().indexOf(textToStyle.first)
            val endIndex = startIndex + textToStyle.first.length

            if (startIndex >= 0) {
                spannableString.setSpan(
                    StyleSpan(textToStyle.second),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }


    fun showKeyboard(activity : AppCompatActivity, layout : View){
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(layout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0)
    }


    fun storeSessionManager(context: Context, driverdata: RegisterPojo.Result.User?) {

        val sessionManager = SessionManager(context)
        val gson = Gson()
        val json = gson.toJson(driverdata!!)
        sessionManager?.create_login_session(
            json,
            driverdata.email!!,
            "",
            true,
            sessionManager?.isEmailLogin()!!
        )
    }

    fun storeSessionManager(context: Context, driverdata: RegisterPojo1.Result.User?) {

        val sessionManager = SessionManager(context)
        val gson = Gson()
        val json = gson.toJson(driverdata!!)
        sessionManager?.create_login_session(
            json,
            driverdata.email!!,
            "",
            true,
            sessionManager?.isEmailLogin()!!
        )
    }

    fun storeSessionManager1(context: Context, driverdata: RegisterPojo.Result.User?) {

        val sessionManager = SessionManager(context)
        val gson = Gson()
        val json = gson.toJson(driverdata!!)
        sessionManager?.create_login_session(
            json,
            driverdata?.email!!,
            "",
            true,
            sessionManager?.isEmailLogin()!!
        )
    }

    fun findMinutesAndHourFromSecond(seconds : Int) : String{

        var second = seconds
        var minutes = seconds / 60
        var hours = minutes / 60
        var days = hours / 24
        var finalValue = ""

        if (second > 59)
            second = (second - (minutes * 60))

        if (minutes > 59)
            minutes = (minutes - (hours * 60))

        if (hours > 23)
            hours = (hours - (days * 24))


        finalValue = "${if (hours < 9) "0$hours" else hours}:${if (minutes < 9) "0$minutes" else minutes}"

        return "$finalValue"
    }

    fun INTtoSDP(i : Int, contect : Context) : Int{
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i.toFloat(), contect.resources.displayMetrics))
    }

    fun setSpanTExt(activity : AppCompatActivity, view: AppCompatTextView, string1 : String, string2 : String, color1 : Int) {

        val spanTxt = SpannableStringBuilder(string1  + "")

        spanTxt.append(string2)

        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
//                val myIntent = Intent(activity!!, TermsandConditionsActivity::class.java)
//                myIntent.putExtra("Type", "Terms")
//                startActivity(myIntent)
//                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

        }, spanTxt.length - string2.length, spanTxt.length, 0)

        spanTxt.setSpan(
            ForegroundColorSpan(activity.resources.getColor(R.color.black_0)),
            spanTxt.length - string2.length, spanTxt.length,
            0
        )

        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }

    fun getCityName(ctx : Context, lattitude: Double?, longitude: Double?) : String{
        val geocoder: Geocoder
        var addresses: List<Address>? = null
        geocoder = Geocoder(ctx, Locale.getDefault())
        Log.d("System out", "print :== " + lattitude!!)
        Log.d("System out", "print :== " + longitude!!)
        try {
            addresses = geocoder.getFromLocation(
                lattitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses != null && addresses.size > 0) {

                return if(!addresses[0].locality.isNullOrEmpty()) addresses[0].locality else ""
            } else {
                return ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }

    fun validDrivingLicenceNo(s : String) : Boolean{
        val pattern = Pattern.compile("^([A-Z]|[a-z]){2}[0-9]{13}\$")
        return if (s.isNullOrEmpty()) {
            false
        } else if (s.matches(pattern.toRegex())) {
            return true
        } else {
            return false
        }
    }

    fun validCarNo(s : String) : Boolean{
        val pattern = Pattern.compile("^([A-Z]|[a-z]){2}[0-9]{2}([A-Z]|[a-z]){2}[0-9]{4}\$")
        return if (s.isNullOrEmpty()) {
            false
        } else if (s.matches(pattern.toRegex())) {
            return true
        } else {
            return false
        }
    }


    fun removeError(view1 : TextInputLayout) {
        Handler().postDelayed({
            view1?.error = null
        }, 2000)
    }

    fun movingMarker(context : Context, lattitude: Double?, longitude: Double?, textView : AppCompatTextView) {
        textView?.text = context!!.resources.getString(R.string.search_for_location)

        val geocoder: Geocoder
        var addresses: List<Address>? = null
        geocoder = Geocoder(context!!, Locale.getDefault())
        Log.d("System out", "print :== " + lattitude!!)
        Log.d("System out", "print :== " + longitude!!)
        try {
            addresses = geocoder.getFromLocation(
                lattitude,
                longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses != null && addresses.size > 0) {

                var city = if(!addresses[0].locality.isNullOrEmpty()) addresses[0].locality else ""
                textView?.text = "$city"
            } else {
                textView?.text = context!!.resources.getString(R.string.location_not_found)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            textView?.text = context!!.resources.getString(R.string.location_not_found)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPath5(uri: Uri, activity: Activity): String? {

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(activity, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                Log.e("System out","Print isMediaDocument ")
                return getDataColumn(
                    activity, contentUri,
                    selection, selectionArgs
                )
            }// DownloadsProvider
            // MediaProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            Log.e("System out","Print content ")
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                activity,
                uri,
                null,
                null
            )

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    fun getDataColumn(
        context: Context, uri: Uri?,
        selection: String?, selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
            .authority
    }

    fun getPathFromInputStreamUri1(context: Context, uri: Uri?): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null

        if (uri?.authority != null) {
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                val photoFile = createTemporalFileFrom(inputStream!!)

                filePath = photoFile?.path
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return filePath
    }

}