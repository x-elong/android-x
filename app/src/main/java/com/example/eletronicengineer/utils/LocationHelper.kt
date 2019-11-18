package com.example.eletronicengineer.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

class LocationHelper
{
    lateinit var mClient:AMapLocationClient
    lateinit var mOption:AMapLocationClientOption
    lateinit var mLocationListener:AMapLocationListener
    lateinit var mConnectManager:ConnectivityManager
    /**
     *
     * @param context 用于配置定位服务器(context决定了服务器的生命周期)
     *
     * */
    constructor(context:Context,locationListener: AMapLocationListener)
    {
        mClient=AMapLocationClient(context)
        mLocationListener=locationListener
        (mClient).setLocationListener(mLocationListener)
        mOption= AMapLocationClientOption()
        mConnectManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            val cability=(mConnectManager).getNetworkCapabilities((mConnectManager).activeNetwork)
            if (cability!=null)
            {
                //WIFI状态设置定位为高精度
                if (cability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                {
                    mOption.locationMode=AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                }
                //移动数据设置定位为省电模式
                else if (cability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                {
                    mOption.locationMode=AMapLocationClientOption.AMapLocationMode.Battery_Saving
                }
                //否则只设置成GPS定位
                else
                {
                    mOption.locationMode=AMapLocationClientOption.AMapLocationMode.Device_Sensors
                }
            }
        }
        else
        {
            when((mConnectManager).activeNetworkInfo.type)
            {
                ConnectivityManager.TYPE_WIFI->
                {
                    mOption.locationMode=AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
                }
                ConnectivityManager.TYPE_MOBILE->
                {
                    mOption.locationMode=AMapLocationClientOption.AMapLocationMode.Battery_Saving
                }
            }
        }
        mOption.isOnceLocation=true
        mOption.isOnceLocationLatest=true
        mOption.isNeedAddress = true
        mOption.isLocationCacheEnable=true
        mClient.setLocationOption(mOption)
        mClient.startLocation()
    }
}