package com.example.eletronicengineer.aninterface

import android.view.View

class ItemMenu(val name: String) {
    var viewOnClickListener: View.OnClickListener?=null
    var checked = false
}