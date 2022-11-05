package com.fipl.mypraticalkhushaltask.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider

import com.fipl.mypraticalkhushaltask.R
import com.fipl.mypraticalkhushaltask.utils.MyUtils


import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.media_choose.*
import java.io.File

/**
 * Created by FUSION on 19/06/2018.
 */

class MediaChooseBottomSheet : BottomSheetDialogFragment() {

    private var cameraPermission = false
    private var readPermission = false
    private var file = File("")
    private var timeForImageName: Long = 0
    private var imgName = ""
    private var mIntent = Intent()
    private var pictureUri = Uri.fromFile(file)

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.media_choose, null)
        dialog.setContentView(contentView)

        dialog.takePhotoTextView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                checkCameraPermission()
            } else {
                if (dialog != null) {
                    dismiss()
                }
                startCameraActivity()
            }
        }

        dialog.cancelTextView.setOnClickListener {
            if (dialog != null) {
                dismiss()
            }
        }

        dialog.chooseGalleryTextView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                checkReadPermission()
            } else {
                if (dialog != null) {
                    dismiss()
                }
                openGallery()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (cameraPermission) {
            if (dialog != null) {
                dismiss()
            }
            cameraPermission = false
        }

        if (readPermission) {
            if (dialog != null) {
                dismiss()
            }
            readPermission = false
        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun checkCameraPermission() {
        if (!MyUtils.addPermission(this.activity!!, Manifest.permission.CAMERA)) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CODE_CAMERA_PERMISSIONS)
        } else {
            if (dialog != null) {
                dismiss()
            }
            startCameraActivity()
        }
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private fun checkReadPermission() {
        if (!MyUtils.addPermission(this.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) || !MyUtils.addPermission(this.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_PERMISSIONS)
        } else {
            if (dialog != null) {
                dismiss()
            }
            openGallery()
        }
    }

    private fun openGallery() {
        mIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        mIntent.type = "image/*"
        requireActivity().startActivityForResult(Intent.createChooser(mIntent, "Select Picture"), SELECT_PICTURE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (dialog != null) {
            dismiss()
        }

        when (requestCode) {
            REQUEST_CODE_CAMERA_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity()
            }
            REQUEST_CODE_READ_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
            REQUEST_CODE_WRITE_PERMISSIONS -> {
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun startCameraActivity() {
        mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        timeForImageName = System.currentTimeMillis()
        imgName = "img$timeForImageName.jpeg"
        file = File(requireActivity().getExternalFilesDir(null), imgName)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        } else {
//            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity!!, activity!!.packageName + ".provider", file))
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(requireActivity(), "com.fipl.mypraticalkhushaltask.provider", file))
        }

        pictureUri = Uri.fromFile(file)
        requireActivity().startActivityForResult(mIntent, TAKE_PICTURE)
    }

    fun selectedImage(): Uri? {
        return pictureUri
    }

    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 11
        private const val REQUEST_CODE_READ_PERMISSIONS = 12
        private const val REQUEST_CODE_WRITE_PERMISSIONS = 13
        private const val TAKE_PICTURE = 1
        private const val SELECT_PICTURE = 2
    }
}


