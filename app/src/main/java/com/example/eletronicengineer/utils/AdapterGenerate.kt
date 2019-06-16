package com.example.eletronicengineer.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import kotlinx.android.synthetic.main.item_hint.*

class AdapterGenerate
{
    /*
        get json file with context
    */
    lateinit var context:Context
    /*
    *   upload file with activity
    * */
    lateinit var activity:AppCompatActivity
    fun DemandGroupCrossingFrame():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        itemGenerate.context=context
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        val uploadListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        multiButtonListeners.add(uploadListener)
        multiButtonListeners.add(cameraListener)
        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Test debugging).json")
        mData[4].buttonListener=multiButtonListeners


        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
    fun ProviderGroupTestDebuggingMain():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        itemGenerate.context=context
        val mData=ItemGenerate().getJsonFromAsset("Provider/ProvicerGroup(Test debugging Main).json")
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
}