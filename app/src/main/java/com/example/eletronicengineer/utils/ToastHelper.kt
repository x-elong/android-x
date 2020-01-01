package com.example.eletronicengineer.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class ToastHelper {
    companion object {
        fun mToast(context: Context, result: String) {
            val toast = Toast.makeText(context, result, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
        fun netWorkToast(context: Context){
            val toast = Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}

