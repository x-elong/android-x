package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.my.*
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.FragmentHelper
import com.lcw.library.imagepicker.ImagePicker
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import java.io.File
import java.io.FileOutputStream

class MyInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_information)
        initData()
    }

    private fun initData() {
        FragmentHelper.addFragment(this,MyInformationFragment(),R.id.frame_my_information,"MyInformation")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.RequestCode.REQUEST_PHOTOGRAPHY.ordinal->{
                    val bmp=data?.extras?.get("data") as Bitmap
                    val file= File(this.filesDir.absolutePath+"tmp.png")
                    val fos= FileOutputStream(file)
                    bmp.compress(Bitmap.CompressFormat.PNG,100,fos)
                    //val bmpToDrawable=BitmapDrawable.createFromPath(file.absolutePath)
                    val fragment=(this.supportFragmentManager.findFragmentByTag("MyInformation") as MyInformationFragment)
                    fragment.refresh(file.path)
                }
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal-> {
                    val mImagePaths = data!!.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES)
                    val fragment=this.supportFragmentManager.findFragmentByTag("MyInformation")
                    if(fragment is MyInformationFragment)
                        fragment.refresh(mImagePaths[0])
                    else if(fragment is PersonalMaterialsFragment)
                        fragment.uploadImg(mImagePaths[0])
                }
                Constants.RequestCode.MY_SCAN_REQUEST_CODE.ordinal->{
                    var resultDisplayStr = ""
                    if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {

                        val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
                        //                        data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
                        // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                        //resultDisplayStr = "银行卡号: " + scanResult.getRedactedCardNumber() + "\n"; //只显示尾号
                        resultDisplayStr = "银行卡号: " + scanResult.formattedCardNumber + "\n"  //显示银行卡号

                        Log.i("resultDisplayStr is", "银行卡号:" + resultDisplayStr)
                        // Do something with the raw number, e.g.:
                        // myService.setCardNumber( scanResult.cardNumber );

                        if (scanResult.isExpiryValid()) {
                            resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n"
                            Log.i("aaa", "银行卡号有效期:" + resultDisplayStr)
                        }

                        if (scanResult.cvv != null) {
                            // Never log or display a CVV
                            resultDisplayStr += "CVV has " + scanResult.cvv.length + " digits.\n"
                        }

                        if (scanResult.postalCode != null) {
                            resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n"
                        }
                    } else {
                        resultDisplayStr = "Scan was canceled."
                    }
                    val fragment=this.supportFragmentManager.findFragmentByTag("AddBankCard") as AddBankCardFragment
                    fragment.setBankCardAccount(resultDisplayStr)
                }
            }
        }
    }
}
