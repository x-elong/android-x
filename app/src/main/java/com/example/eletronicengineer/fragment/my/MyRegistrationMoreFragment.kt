package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.custom.LoadingDialog
import com.example.eletronicengineer.db.My.RequirementLeaseRegistrationEntity
import com.example.eletronicengineer.db.My.RequirementThirdRegistrationEntity
import com.example.eletronicengineer.db.My.RequirementPersonalRegistrationEntity
import com.example.eletronicengineer.db.My.RequirementTeamRegistrationEntity
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.deletePersonRequirementInformationCheck
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my_registration_more.view.*
import org.json.JSONObject
import java.io.Serializable

class MyRegistrationMoreFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):MyRegistrationMoreFragment{
            val myRegistrationMoreFragment = MyRegistrationMoreFragment()
            myRegistrationMoreFragment.arguments = args
            return myRegistrationMoreFragment
        }
    }

    lateinit var mView: View
    val mMultiStyleItemList:MutableList<MultiStyleItem> =ArrayList()
    var type = -1
    var frame = R.id.frame_my_registration
    val gson = GsonBuilder().create()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_my_registration_more,container,false)
        type = arguments!!.getInt("type")
        if(activity is MyReleaseActivity)
            frame = R.id.frame_my_release
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_my_registration_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        initData()
    }

    private fun initData() {
        lateinit var adapter: RecyclerviewAdapter
        mMultiStyleItemList.clear()
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as AppCompatActivity
        when(type){
            Constants.FragmentType.DEMAND_INDIVIDUAL_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandIndividual()
                val requirementPersonalRegistrationEntity = gson.fromJson(arguments!!.getString("demandIndividual"),RequirementPersonalRegistrationEntity::class.java)
                adapter.mData[1].singleDisplayRightContent = requirementPersonalRegistrationEntity.requirementMajor
                adapter.mData[2].singleDisplayRightContent = requirementPersonalRegistrationEntity.name
                adapter.mData[3].singleDisplayRightContent = requirementPersonalRegistrationEntity.phone
                adapter.mData[4].jumpListener = View.OnClickListener {
                    val enrollProvideCrewList = requirementPersonalRegistrationEntity.enrollProvideCrewList
                    if(enrollProvideCrewList!=null){
                        val bundle = Bundle()
                        bundle.putSerializable("inventoryList",requirementPersonalRegistrationEntity as Serializable)
                        bundle.putString("type", "成员清册")
                        FragmentHelper.switchFragment(activity!!, MyInventoryListFragment.newInstance(bundle), frame, "register")
                    }
                    else
                        ToastHelper.mToast(mView.context, "没有数据")
                }
                if(requirementPersonalRegistrationEntity.personRequirementInformationCheck.comment!=null)
                    adapter.mData[5].textAreaContent = requirementPersonalRegistrationEntity.personRequirementInformationCheck.comment

                mView.btn_delete_my_registration.setOnClickListener {
                    val loadDialog = LoadingDialog(mView.context,"正在删除...")
                    loadDialog.show()
                    val result =
                        deletePersonRequirementInformationCheck(requirementPersonalRegistrationEntity.personRequirementInformationCheck.id)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                loadDialog.dismiss()
                                val json = JSONObject(it.string())
                                if(json.getInt("code")==200){
                                    activity!!.supportFragmentManager.popBackStackImmediate()
                                    ToastHelper.mToast(mView.context,"删除成功")
                                }else{
                                    ToastHelper.mToast(mView.context,"删除失败")
                                }
                            },{
                                loadDialog.dismiss()
                                ToastHelper.mToast(mView.context,"删除信息异常")
                                it.printStackTrace()
                            })
                }
            }
            Constants.FragmentType.DEMAND_GROUP_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandGroup()
                val requirementTeamRegistrationEntity = gson.fromJson(arguments!!.getString("demandGroup"),RequirementTeamRegistrationEntity::class.java)
                adapter.mData[0].singleDisplayRightContent = requirementTeamRegistrationEntity.requirementTeamLoggingCheck.type
                adapter.mData[1].singleDisplayRightContent = requirementTeamRegistrationEntity.name
                adapter.mData[2].singleDisplayRightContent = requirementTeamRegistrationEntity.phone
                adapter.mData[3].jumpListener = View.OnClickListener {
                    val enrollProvideCrewLists = requirementTeamRegistrationEntity.enrollProvideCrewLists
                    if(enrollProvideCrewLists!=null){
                        val bundle = Bundle()
                        bundle.putSerializable("inventoryList",requirementTeamRegistrationEntity as Serializable)
                        bundle.putInt("requirementType",1)
                        bundle.putString("type", "成员清册")
                        FragmentHelper.switchFragment(activity!!, MyInventoryListFragment.newInstance(bundle), frame, "register")
                    }
                    else
                        ToastHelper.mToast(mView.context, "没有数据")
                }
                adapter.mData[4].jumpListener = View.OnClickListener {
                    val enrollCars = requirementTeamRegistrationEntity.enrollCars
                    if(enrollCars!=null){
                        val bundle = Bundle()
                        bundle.putSerializable("inventoryList",requirementTeamRegistrationEntity as Serializable)
                        bundle.putString("type", "车辆清册")
                        bundle.putInt("requirementType",1)
                        FragmentHelper.switchFragment(activity!!, MyInventoryListFragment.newInstance(bundle), frame, "register")
                    }
                    else
                        ToastHelper.mToast(mView.context, "没有数据")
                }
                adapter.mData[5].jumpListener = View.OnClickListener {
                    if(requirementTeamRegistrationEntity.enrollMachineries!=null){
                        val bundle = Bundle()
                        bundle.putSerializable("inventoryList",requirementTeamRegistrationEntity as Serializable)
                        bundle.putString("type", "机械设备")
                        FragmentHelper.switchFragment(activity!!, MyInventoryListFragment.newInstance(bundle), frame, "register")
                    }
                    else
                        ToastHelper.mToast(mView.context, "没有数据")
                }
                if(requirementTeamRegistrationEntity.requirementTeamLoggingCheck.comment!=null)
                    adapter.mData[6].textAreaContent = requirementTeamRegistrationEntity.requirementTeamLoggingCheck.comment

                mView.btn_delete_my_registration.setOnClickListener {
                    val loadDialog = LoadingDialog(mView.context,"正在删除...")
                    loadDialog.show()
                    val result =
                        deleteRequirementTeamLoggingCheck(requirementTeamRegistrationEntity.requirementTeamLoggingCheck.id)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                loadDialog.dismiss()
                                val json = JSONObject(it.string())
                                if(json.getInt("code")==200){
                                    activity!!.supportFragmentManager.popBackStackImmediate()
                                    ToastHelper.mToast(mView.context,"删除成功")
                                }else{
                                    ToastHelper.mToast(mView.context,"删除失败")
                                }
                            },{
                                loadDialog.dismiss()
                                ToastHelper.mToast(mView.context,"删除信息异常")
                                it.printStackTrace()
                            })
                }
                if(adapter.mData[5].singleDisplayRightTitle=="机械设备" && requirementTeamRegistrationEntity.enrollMachineries==null){
                    val mData = adapter.mData.toMutableList()
                    mData.removeAt(5)
                    adapter.mData = mData
                }
                when(requirementTeamRegistrationEntity.requirementTeamLoggingCheck.type){
                    "马帮运输"->{
                        val mData = adapter.mData.toMutableList()
                        for (j in 0 until 2)
                            mData.removeAt(3)
                        adapter.mData = mData
                    }
                    "桩基服务","非开挖顶拉管作业"->{
                        val mData = adapter.mData.toMutableList()
                        mData.removeAt(3)
                        adapter.mData = mData
                    }
                }
            }
            Constants.FragmentType.DEMAND_LEASE_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandLease()
                val requirementLeaseRegistrationEntity = gson.fromJson(arguments!!.getString("demandLease"),
                    RequirementLeaseRegistrationEntity::class.java)
                adapter.mData[0].singleDisplayRightContent = requirementLeaseRegistrationEntity.leaseLoggingCheck.type
                adapter.mData[1].singleDisplayRightContent = requirementLeaseRegistrationEntity.name
                adapter.mData[2].singleDisplayRightContent = requirementLeaseRegistrationEntity.phone
                if(adapter.mData[0].singleDisplayRightContent.contains("车辆")){
                    adapter.mData[3].singleDisplayRightTitle = "车辆清册"
                    adapter.mData[3].jumpListener = View.OnClickListener {
                        val enrollCar = requirementLeaseRegistrationEntity.enrollCar
                        if(enrollCar!=null){
                            val bundle = Bundle()
                            bundle.putSerializable("inventoryList",requirementLeaseRegistrationEntity as Serializable)
                            bundle.putString("type", "车辆清册")
                            FragmentHelper.switchFragment(activity!!, MyInventoryListFragment.newInstance(bundle), frame, "register")
                        }
                        else
                            ToastHelper.mToast(mView.context, "没有数据")
                    }
                }else{
                    adapter.mData[3].singleDisplayRightTitle = "租赁清单"
                    adapter.mData[3].jumpListener = View.OnClickListener{
                        val enrollRequirementLeaseList = requirementLeaseRegistrationEntity.enrollRequirementLeaseList
                        if(enrollRequirementLeaseList!=null){
                            val bundle = Bundle()
                            bundle.putSerializable("inventoryList",requirementLeaseRegistrationEntity as Serializable)
                            bundle.putString("type", "租赁清单")
                            FragmentHelper.switchFragment(activity!!, MyInventoryListFragment.newInstance(bundle), frame, "register")
                        }
                        else
                            ToastHelper.mToast(mView.context, "没有数据")
                    }
                }
                if(requirementLeaseRegistrationEntity.leaseLoggingCheck.comment!=null)
                    adapter.mData[4].textAreaContent = requirementLeaseRegistrationEntity.leaseLoggingCheck.comment

                mView.btn_delete_my_registration.setOnClickListener {
                    val loadDialog = LoadingDialog(mView.context,"正在删除...")
                    loadDialog.show()
                    val result =
                        deleteLeaseLoggingCheckController(requirementLeaseRegistrationEntity.leaseLoggingCheck.id)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                loadDialog.dismiss()
                                val json = JSONObject(it.string())
                                if(json.getInt("code")==200){
                                    activity!!.supportFragmentManager.popBackStackImmediate()
                                    ToastHelper.mToast(mView.context,"删除成功")
                                }else{
                                    ToastHelper.mToast(mView.context,"删除失败")
                                }
                            },{
                                loadDialog.dismiss()
                                ToastHelper.mToast(mView.context,"删除信息异常")
                                it.printStackTrace()
                            })
                }


            }
            Constants.FragmentType.DEMAND_TRIPARTITE_TYPE.ordinal-> {
                adapter = adapterGenerate.registrationDisplayDemandTripartite()
                val requirementThirdRegistrationEntity = gson.fromJson(arguments!!.getString("demandTripartite"),RequirementThirdRegistrationEntity::class.java)
                adapter.mData[0].singleDisplayRightContent = requirementThirdRegistrationEntity.requirementVariety
                adapter.mData[1].singleDisplayRightContent = requirementThirdRegistrationEntity.name
                adapter.mData[2].singleDisplayRightContent = requirementThirdRegistrationEntity.phone
                adapter.mData[3].jumpListener = View.OnClickListener {
                    val enrollThirdLists = requirementThirdRegistrationEntity.enrollThirdLists
                    if(enrollThirdLists!=null){
                        val bundle = Bundle()
                        bundle.putSerializable("inventoryList",requirementThirdRegistrationEntity as Serializable)
                        bundle.putString("type", "三方服务清单")
                        FragmentHelper.switchFragment(activity!!, MyInventoryListFragment.newInstance(bundle), frame, "register")
                    }
                    else
                        ToastHelper.mToast(mView.context, "没有数据")
                }
                if(requirementThirdRegistrationEntity.requirementThirdLoggingCheck.comment!=null)
                    adapter.mData[4].textAreaContent = requirementThirdRegistrationEntity.requirementThirdLoggingCheck.comment

                mView.btn_delete_my_registration.setOnClickListener {
                    val loadDialog = LoadingDialog(mView.context,"正在删除...")
                    loadDialog.show()
                    val result =
                        deleteRequirementThirdLoggingCheck(requirementThirdRegistrationEntity.requirementThirdLoggingCheck.id)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                                loadDialog.dismiss()
                                val json = JSONObject(it.string())
                                if(json.getInt("code")==200){
                                    activity!!.supportFragmentManager.popBackStackImmediate()
                                    ToastHelper.mToast(mView.context,"删除成功")
                                }else{
                                    ToastHelper.mToast(mView.context,"删除失败")
                                }
                            },{
                                loadDialog.dismiss()
                                ToastHelper.mToast(mView.context,"删除信息异常")
                                it.printStackTrace()
                            })
                }
            }
        }
        mView.rv_registration_more_content.adapter = adapter
        mView.rv_registration_more_content.layoutManager = LinearLayoutManager(context)
    }
}