package com.example.eletronicengineer.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract

class AddUtil {
    companion object{
        fun addContact(context: Context){
            val values = ContentValues()
            val rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values)
            val rawContactId = ContentUris.parseId(rawContactUri)
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "啊哈")
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values)
            values.clear()

            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "13154758474")
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values)
            values.clear()
            ToastHelper.mToast(context, "联系人数据添加成功")
        }
    }
}