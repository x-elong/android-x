package com.example.eletronicengineer.aninterface

import android.view.View


class Image (val imagePath:String,var imageListener: View.OnClickListener?){
    var deleteListener:View.OnClickListener? = null
    var isX = true
}