package com.example.eletronicengineer.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.electric.engineering.utils.ItemGenerate
import com.example.eletronicengineer.MainActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants

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
        val hybridButtonListeners:MutableList<View.OnClickListener> =ArrayList()

        val uploadProjectListListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_FILE.ordinal)
        }
        val cameraListener=View.OnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            activity.startActivityForResult(intent,Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }
        val uploadSalaryStandard=View.OnClickListener {
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type=MediaStore.Files.FileColumns.MIME_TYPE
        }
        multiButtonListeners.add(uploadProjectListListener)//工程量清册
        multiButtonListeners.add(cameraListener)
        hybridButtonListeners.add(uploadSalaryStandard)//薪资标准

        val mData=itemGenerate.getJsonFromAsset("Demand/DemandGroup(Crossing frame).json")
        mData[4].buttonListener=multiButtonListeners
        mData[14].hybridButtonListeners=hybridButtonListeners

        val adapter=RecyclerviewAdapter(mData)
        adapter.adapterObserver=object:ObserverFactory.RecyclerviewAdapterObserver{
            override fun onBindComplete() {
            }
            override fun onBindRunning() {
                if (adapter.VHList.size==20)
                {
                    adapter.VHList[19].etInputUnitValue.hint = "1-92"
                }
            }
        }
        return adapter
    }
    fun ProviderGroupTestDebuggingMain():RecyclerviewAdapter
    {
        val itemGenerate=ItemGenerate()
        val multiButtonListeners:MutableList<View.OnClickListener> =ArrayList()
        itemGenerate.context=context
        val mData=itemGenerate.getJsonFromAsset("Provider/ProvicerGroup(Test debugging Main).json")
        val adapter=RecyclerviewAdapter(mData)
        return adapter
    }
}