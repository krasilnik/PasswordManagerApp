package com.example.managerpass.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.LineNumberReader
import java.lang.Exception


enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}


fun Context.longToastShow(msg:String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Dialog.setupDialog(layoutResId: Int){
    setContentView(layoutResId)
    window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    setCancelable(false)
}

fun validateEditText(editText: EditText, textTextInputLayout: TextInputLayout): Boolean {
    return when {
        editText.text.toString().trim().isEmpty() -> {
            textTextInputLayout.error = "Required"
            false
        }
        else -> {
            textTextInputLayout.error = null
            true
        }
    }
}

fun validateEditEmail(edItemEmail: TextInputEditText, edItemEmailL: TextInputLayout): Boolean {
    val emailPattern = Regex("[a-zA-Z\\d._-]+@[a-z]+\\.+[a-z]+")
    return when {
        edItemEmail.text.toString().trim().isEmpty() -> {
            edItemEmailL.error = "Required"
            false
        }
        !edItemEmail.text.toString().trim().matches(emailPattern) -> {
            edItemEmailL.error = "Valid E-mail"
            false
        }
        else -> {
            edItemEmailL.error = null
            true
        }
    }
}

fun validateEditPassword(
    edItemPassword: TextInputEditText,
    edItemPasswordL: TextInputLayout,
): Boolean {
    return when {
        edItemPassword.text.toString().trim().isEmpty() -> {
            edItemPasswordL.error = "Required"
            false
        }
        edItemPassword.text.toString().trim().length < 8 -> {
            edItemPasswordL.error = "Password must be more than 8 characters!"
            false
        }
        else -> {
            edItemPasswordL.error = null
            true
        }
    }
}

fun clearEditText(editText: EditText, textTextInputLayout: TextInputLayout){
    editText.text = null
    textTextInputLayout.error = null
}