package com.example.eletronicengineer.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import java.util.*

class PermissionHelper
{
    companion object{
        val PermissionList:MutableList<String> = Arrays.asList(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA
        )
        fun getPermission(activity:Activity,requestCode:Int)
        {
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M)
            {
                for (permission in PermissionList)
                {
                    if (activity.checkSelfPermission(permission)!=PackageManager.PERMISSION_GRANTED)
                    {
                        activity.requestPermissions(PermissionList.toTypedArray(),requestCode)
                    }
                }
            }
        }
    }


}