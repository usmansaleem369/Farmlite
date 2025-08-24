package com.tracko.automaticchickendoor.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.tracko.automaticchickendoor.R

class FarmLiteLoader private constructor() {

    private var currentDialog: Dialog? = null

    fun showLoader(context: Context): Dialog {
        val networkDialogLoader = Dialog(context, R.style.MyTheme)

        networkDialogLoader.setContentView(R.layout.progress_loader)
        networkDialogLoader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        networkDialogLoader.setCancelable(false)
        networkDialogLoader.setCanceledOnTouchOutside(false)
        networkDialogLoader.show()

        currentDialog = networkDialogLoader
        return networkDialogLoader
    }

    fun hideLoader() {
        currentDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        currentDialog = null
    }

    companion object {
        val instance: FarmLiteLoader by lazy { FarmLiteLoader() }
    }
}