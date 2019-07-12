package com.example.eletronicengineer.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.bin.david.form.data.Column
import com.bin.david.form.data.table.TableData
import com.codekidlabs.storagechooser.StorageChooser
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.User
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.downloadFile
import kotlinx.android.synthetic.main.activity_demand.*
import java.io.File
import java.lang.Exception
import kotlin.collections.ArrayList

class DemandActivity : AppCompatActivity() {
    lateinit var mData: List<MultiStyleItem>
    lateinit var multiButtonListeners: MutableList<View.OnClickListener>
    //List<MultiStyleItem> mData=new ArrayList<>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demand)
        initData()
    }

    fun Finish() {
        finish()
    }

    fun initData() {
        supportActionBar?.hide()
        multiButtonListeners = ArrayList()
        val intent = getIntent()
        val type = intent.getIntExtra("type", 0)
        val adapter = switchAdapter(type)
        rv_main_content.adapter = adapter
        rv_main_content.layoutManager = LinearLayoutManager(this@DemandActivity)
    }

    fun switchAdapter(Type: Int): RecyclerviewAdapter {
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = this@DemandActivity
        adapterGenerate.activity = this@DemandActivity
        lateinit var adapter: RecyclerviewAdapter
        when (Type) {
            Constants.FragmentType.PERSONAL_GENERAL_WORKERS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayContent = "普工"
                var selectOption1Items: List<String> = listOf("普工")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SPECIAL_WORK_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayContent = "特种作业"
                var selectOption1Items: List<String> =
                    listOf("低压电工作业", "高压电工作业", "电力电缆作业", "继电保护作业", "电气试验作业", "融化焊接与热切割作业", "登高架设作业")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items

            }
            Constants.FragmentType.PERSONAL_PROFESSIONAL_OPERATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayContent = "专业操作"
                var selectOption1Items: List<String> =
                    listOf("压接操作", "机动绞磨操作", "牵张设备操作", "起重机械操作", "钢筋工", "混凝土工", "木工", "模板工", "油漆工", "砌筑工", "通风工", "打桩工", "架子工工")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_SURVEYOR_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayContent = "测量工"
                var selectOption1Items: List<String> = listOf("测量工")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items

            }
            Constants.FragmentType.PERSONAL_DRIVER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayContent = "驾驶员"
                var selectOption1Items: List<String> =
                    listOf("驾驶证A1", "驾驶证A2", "驾驶证A3", "驾驶证B1", "驾驶证B2", "驾驶证C1", "驾驶证C2", "驾驶证C3", "驾驶证D", "驾驶证E")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_NINE_MEMBERS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayContent = "九大员"
                var selectOption1Items: List<String> =
                    listOf("施工员", "安全员", "质量员", "材料员", "资料员", "预算员", "标准员", "机械员", "劳务员")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_REGISTRATION_CLASS_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                var singleDisplayContent = "注册类"
                var selectOption1Items: List<String> = listOf("造价工程师", "一级建造师", "安全工程师", "电气工程师")
                adapter.mData[1].singleDisplayContent = singleDisplayContent
                adapter.mData[2].selectOption1Items = selectOption1Items
            }
            Constants.FragmentType.PERSONAL_OTHER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandIndividual()
                adapter.mData[1].options=MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[1].inputSingleTitle=adapter.mData[1].singleDisplayTitle
                adapter.mData[2].options=MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[2].inputSingleTitle=adapter.mData[2].selectTitle
            }
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                var singleDisplayContent = "变电施工队"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                var singleDisplayContent = "主网施工队"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupSubstationConstruction()
                var singleDisplayContent = "配网施工队"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupMeasurementDesign()
            }
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupCaravanTransportation()
            }
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupPileFoundationConstruction()
            }
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupNonExcavation()
            }
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupTestDebugging()
            }
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupCrossingFrame()
            }
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandGroupOperationAndMaintenance()
            }
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandLeaseVehicleLeasing()
                adapter.mData[1].singleDisplayContent="车辆租赁"
            }
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[1].singleDisplayContent="工器具租赁"
            }
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal -> {
                var singleDisplayContent = "机械租赁"
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal -> {
                var singleDisplayContent = "设备租赁"
                adapter = adapterGenerate.DemandEquipmentLeasing()
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.TRIPARTITE_TRAINING_CERTIFICATE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayContent = "代办资格"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.TRIPARTITE_FINANCIAL_ACCOUNTING_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayContent = "培训办证"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.TRIPARTITE_AGENCY_QUALIFICATION_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayContent = "财务记账"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayContent = "标书服务"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.TRIPARTITE_LEGAL_ADVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayContent = "法律咨询"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.TRIPARTITE_SOFTWARE_SERVICE_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                var singleDisplayContent = "软件服务"
                adapter.mData[1].singleDisplayContent = singleDisplayContent
            }
            Constants.FragmentType.TRIPARTITE_OTHER_TYPE.ordinal -> {
                adapter = adapterGenerate.DemandTripartite()
                adapter.mData[1].options=MultiStyleItem.Options.SINGLE_INPUT
                adapter.mData[1].inputSingleTitle=adapter.mData[1].singleDisplayTitle
            }
        }
        return adapter
    }

    fun initTable()
    {
        val column1 = Column<String>("姓名", "name")
        val column2 = Column<String>("年龄", "age")
        val column3 = Column<String>("更新时间", "time")
        val column4 = Column<String>("头像", "portrait")
        //组合列
        val totalColumn1 = Column<Any>("组合列名", column1, column2,column3,column4)
        val userList:MutableList<User> =ArrayList(5)

        val tableData:TableData<User> = TableData("表格名",userList,column1,column2,column3,column4)
        st_main_table.tableData=tableData
        st_main_table.setZoom(true)
    }
    fun initRetrofit(targetFileName:String,webFileName:String,baseUrl:String)
    {
        val chooser=
            StorageChooser.Builder().withActivity(this).allowCustomPath(true).setType(StorageChooser.DIRECTORY_CHOOSER).withFragmentManager(fragmentManager).withMemoryBar(true).build()
        chooser.show()
        chooser.setOnSelectListener {
            downloadFile(it,targetFileName,webFileName,baseUrl)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal -> {
                    val uri = data!!.data
                    val path = getRealPathFromURI(uri!!)
                    Log.i("path", path)
                }
                Constants.RequestCode.REQUEST_PICK_FILE.ordinal -> {
                    val uri = data!!.data
                    if (uri!!.toString().contains("content")) {
                        val path = getRealPathFromURI(uri)
                        Log.i("path", path)
                    }
                    val file = File(uri.toString())
                    if (file.exists()) {
                        Log.i("file", file.name)
                    }
                    //val resultFile= File()
                }
            }
        }
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val projection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(contentUri, projection, null, null, null)
        try {
            if (cursor != null) {
                val column = cursor.getColumnIndexOrThrow(projection[0])
                if (cursor.moveToFirst()) {
                    res = cursor.getString(column)
                }
                cursor.close()
            }
            if (res == null) {
                cursor =
                    contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
                if (cursor != null) {
                    val column = cursor.getColumnIndexOrThrow(projection[0])
                    if (cursor.moveToFirst()) {
                        res = cursor.getString(column)
                    }
                    cursor.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return res
    }

}
