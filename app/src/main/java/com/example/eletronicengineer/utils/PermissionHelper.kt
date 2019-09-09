package com.example.eletronicengineer.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.util.*

class PermissionHelper
{
    companion object{
        val PermissionList:MutableList<String> = Arrays.asList(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        )
        fun getPermission(activity:Activity,requestCode:Int)
        {
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M)//Marllshow
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