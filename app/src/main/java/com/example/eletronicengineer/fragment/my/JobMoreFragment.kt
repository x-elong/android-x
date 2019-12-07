package com.example.eletronicengineer.fragment.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ImageDisplayActivity
import com.example.eletronicengineer.activity.MyReleaseActivity
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.sdf.ApplicationSubmitFragment
import com.example.eletronicengineer.fragment.sdf.ProjectListFragment
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getRequirementCaravanTransport
import com.example.eletronicengineer.utils.getRequirementDistributionNetWork
import com.example.eletronicengineer.utils.getRequirementLeaseCar
import com.example.eletronicengineer.utils.getRequirementLeaseConstructionTool
import com.example.eletronicengineer.utils.getRequirementLeaseFacility
import com.example.eletronicengineer.utils.getRequirementLeaseMachinery
import com.example.eletronicengineer.utils.getRequirementMajorNetWork
import com.example.eletronicengineer.utils.getRequirementMeasureDesign
import com.example.eletronicengineer.utils.getRequirementPersonDetail
import com.example.eletronicengineer.utils.getRequirementPileFoundation
import com.example.eletronicengineer.utils.getRequirementPowerTransformation
import com.example.eletronicengineer.utils.getRequirementRunningMaintain
import com.example.eletronicengineer.utils.getRequirementSpanWoodenSupprt
import com.example.eletronicengineer.utils.getRequirementTest
import com.example.eletronicengineer.utils.getRequirementThirdPartyDetail
import com.example.eletronicengineer.utils.getRequirementUnexcavation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_job_more.view.*
import org.json.JSONObject
import java.io.Serializable

class JobMoreFragment : Fragment() {
    companion object {
        fun newInstance(args: Bundle): JobMoreFragment {
            val jobMoreFragment = JobMoreFragment()
            jobMoreFragment.arguments = args
            return jobMoreFragment
        }
    }

    var adapter = RecyclerviewAdapter(ArrayList())
    lateinit var mView: View
    var type: Int = 0
    lateinit var id: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_job_more, container, false)
        type = arguments!!.getInt("type")
        id = arguments!!.getString("id")
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.tv_job_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        val bundle = Bundle()
        val adapterGenerate = AdapterGenerate()
        adapterGenerate.context = mView.context
        adapterGenerate.activity = activity as AppCompatActivity
        lateinit var adapter: RecyclerviewAdapter
        when (type) {
            //需求个人显示模板
            Constants.FragmentType.PERSONAL_TYPE.ordinal -> {
                adapter = adapterGenerate.demandIndividualDisplay()
                val result = getRequirementPersonDetail(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent = data.requirementMajor

                        adapter.mData[2].singleDisplayRightContent = data.projectName


                        adapter.mData[3].singleDisplayRightContent =
                            data.projectSite.replace(" / ", " ")

                        adapter.mData[4].singleDisplayRightContent = data.planTime

                        adapter.mData[5].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[6].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[7].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "1" -> adapter.mData[7].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "-1" -> adapter.mData[7].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[7].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[8].singleDisplayRightContent =
                                "全包"
                            data.roomBoardStandard == "1" -> adapter.mData[8].singleDisplayRightContent =
                                "队部自理"
                            else -> {
                                adapter.mData[8].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[9].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[10].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[11].singleDisplayRightContent =

                            data.needPeopleNumber

                        adapter.mData[12].singleDisplayRightContent = if(data.salaryStandard!="-1.0")
                            "${data.salaryStandard} ${data.salaryUnit}"
                        else "面议"

                        adapter.mData[14].singleDisplayRightContent =
                            data.name

                        adapter.mData[15].singleDisplayRightContent =
                            data.phone

                        adapter.mData[16].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[17].singleDisplayRightContent =

                            data.additonalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            val typeStr =
                                if(data.requirementVariety in arrayListOf("普工","特种作业","专业操作","测量工","驾驶员","九大员","注册类"))
                                    "${data.requirementType} ${data.requirementVariety}"
                                else
                                    "${data.requirementType} 其他"
                            val type = adapterGenerate.getType(typeStr)
                            bundle.putInt(
                                "type",
                                type
                            )
                            bundle.putString("id", data.id)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //需求主网
            Constants.FragmentType.MAINNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementMajorNetWork(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName


                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite.replace(" / ", " ")


                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.requirementTeamVoltageClasses

                        adapter.mData[6].singleDisplayRightContent =

                            data.requirementConstructionWorkKind

                        adapter.mData[7].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[8].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[9].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[9].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[9].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[9].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[10].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[10].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[10].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[11].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[12].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[13].singleDisplayRightContent =

                            data.needPeopleNumber

                        /*adapter.mData[14].singleDisplayRightContent=if(data.vehicle==null) {
                            " " } else{ data.vehicle}
                        when {
                            data.requirementPowerTransformationSalary == null && data.requirementListQuotations == null ->adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context,"没有数据")
                            },View.OnClickListener { ToastHelper.mToast(mView.context,"没有数据") })
                            data.requirementPowerTransformationSalary == null &&data.requirementListQuotations !=null -> adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context,"没有数据")
                            }, View.OnClickListener {
                                if(data.requirementListQuotations!!.isEmpty())
                                {
                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    val listData = data.requirementListQuotations
                                    mdata.putSerializable("listData1", listData as Serializable)
                                    mdata.putInt("type",3)
                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                }
                            })
                            data.requirementListQuotations == null &&data.requirementPowerTransformationSalary !=null -> adapter.mData[15].buttonListener = listOf(
                                View.OnClickListener {
                                    if(data.requirementPowerTransformationSalary!!.isEmpty())
                                    {
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val listData = data.requirementPowerTransformationSalary
                                        mdata.putSerializable("listData2", listData as Serializable)
                                        mdata.putInt("type", 2)
                                        (activity as DemandDisplayActivity).switchFragment(
                                            ProjectListFragment.newInstance(mdata)
                                        )
                                    }
                                },
                                View.OnClickListener {
                                    ToastHelper.mToast(mView.context,"没有数据")
                                })
                            else -> adapter.mData[15].buttonListener = listOf(
                                View.OnClickListener {
                                    if(data.requirementPowerTransformationSalary!!.isEmpty())
                                    {
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val listData = data.requirementPowerTransformationSalary
                                        mdata.putSerializable("listData2", listData as Serializable)
                                        mdata.putInt("type", 2)
                                        (activity as DemandDisplayActivity).switchFragment(
                                            ProjectListFragment.newInstance(mdata)
                                        )
                                    }},
                                View.OnClickListener {
                                    if(data.requirementListQuotations!!.isEmpty())
                                    {
                                        Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        val listData = data.requirementListQuotations
                                        mdata.putSerializable("listData1", listData as Serializable)
                                        mdata.putInt("type",3)
                                        (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
                                    } })
                            //薪资标准
                        }*/
                        when {
                            data.constructionEquipment == "0" -> adapter.mData[14].singleDisplayRightContent =
                                "不需要提供"
                            data.constructionEquipment == "1" -> adapter.mData[14].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[14].singleDisplayRightContent = " "
                            }
                        }
                        if (data.requirementMembersLists == null) {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context, "没有数据")
                            })//成员清册查看
                        } else {
                            if (data.requirementMembersLists!!.isEmpty()) {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })//成员清册查看
                            } else {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    bundle.putSerializable("listData2", listData as Serializable)
                                    bundle.putString("type", "成员清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent =
                            data.name

                        adapter.mData[18].singleDisplayRightContent =
                            data.phone

                        adapter.mData[19].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[20].singleDisplayRightContent =

                            data.additonalExplain

                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //配网
            Constants.FragmentType.DISTRIBUTIONNET_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementDistributionNetWork(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName



                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite.replace(" / ", " ")
                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册查看
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册查看
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册查看
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.requirementTeamVoltageClasses

                        adapter.mData[6].singleDisplayRightContent =

                            data.requirementConstructionWorkKind

                        adapter.mData[7].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[8].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[9].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[9].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[9].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[9].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[10].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[10].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[10].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[11].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[12].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[13].singleDisplayRightContent =

                            data.needPeopleNumber

                        when {
                            data.constructionEquipment == "0" -> adapter.mData[14].singleDisplayRightContent =
                                "不需要提供"
                            data.constructionEquipment == "1" -> adapter.mData[14].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[14].singleDisplayRightContent = " "
                            }
                        }
                        if (data.requirementMembersLists == null) {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context, "没有数据")
                            })//成员清册查看
                        } else {
                            if (data.requirementMembersLists!!.isEmpty()) {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })//成员清册查看
                            } else {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    bundle.putSerializable("listData2", listData as Serializable)
                                    bundle.putString("type", "成员清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent =
                            data.name

                        adapter.mData[18].singleDisplayRightContent =
                            data.phone

                        adapter.mData[19].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[20].singleDisplayRightContent =

                            data.additonalExplain

                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //变电
            Constants.FragmentType.SUBSTATION_CONSTRUCTION_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementPowerTransformation(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent = data.requirementVariety
                        adapter.mData[1].singleDisplayRightContent = data.projectName
                        adapter.mData[2].singleDisplayRightContent = data.projectSite.replace(" / ", " ")
                        adapter.mData[3].singleDisplayRightContent = data.projectTime
                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent = data.requirementTeamVoltageClasses

                        adapter.mData[6].singleDisplayRightContent = data.requirementConstructionWorkKind

                        adapter.mData[7].singleDisplayRightContent = data.workerExperience

                        adapter.mData[8].singleDisplayRightContent = "${data.minAgeDemand}~${data.maxAgeDemand}"
                        when {
                            data.sexDemand == "0" -> adapter.mData[9].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[9].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[9].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[9].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[10].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[10].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[10].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[11].singleDisplayRightContent = data.journeyCarFare

                        adapter.mData[12].singleDisplayRightContent = data.journeySalary

                        adapter.mData[13].singleDisplayRightContent = data.needPeopleNumber

                        when {
                            data.constructionEquipment == "0" -> adapter.mData[14].singleDisplayRightContent =
                                "不需要提供"
                            data.constructionEquipment == "1" -> adapter.mData[14].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[14].singleDisplayRightContent = " "
                            }
                        }
                        if (data.requirementMembersLists == null) {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context, "没有数据")
                            })//成员清册查看
                        } else {
                            if (data.requirementMembersLists!!.isEmpty()) {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })//成员清册查看
                            } else {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    bundle.putSerializable("listData2", listData as Serializable)
                                    bundle.putString("type", "成员清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent = data.name

                        adapter.mData[18].singleDisplayRightContent = data.phone

                        adapter.mData[19].singleDisplayRightContent = data.validTime

                        adapter.mData[20].singleDisplayRightContent = data.additonalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //测量设计
            Constants.FragmentType.MEASUREMENT_DESIGN_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplay()
                val result = getRequirementMeasureDesign(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =

                            data.projectName



                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite.replace(" / ", " ")
                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.requirementTeamVoltageClasses

                        adapter.mData[6].singleDisplayRightContent =

                            data.requirementConstructionWorkKind

                        adapter.mData[7].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[8].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[9].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[9].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[9].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[9].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[10].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[10].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[10].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[11].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[12].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[13].singleDisplayRightContent =

                            data.needPeopleNumber

                        when {
                            data.equipment == "0" -> adapter.mData[14].singleDisplayRightContent =
                                "不需要提供"
                            data.equipment == "1" -> adapter.mData[14].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[14].singleDisplayRightContent = " "
                            }
                        }
                        if (data.requirementMembersLists == null) {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                Toast.makeText(mView.context, "没有数据", Toast.LENGTH_SHORT).show()
                            })//成员清册查看
                        } else {
                            if (data.requirementMembersLists!!.isEmpty()) {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    Toast.makeText(mView.context, "没有数据", Toast.LENGTH_SHORT).show()
                                })//成员清册查看
                            } else {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    bundle.putSerializable("listData2", listData as Serializable)
                                    bundle.putString("type", "成员清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })//乙方材料清册
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent =
                            data.name

                        adapter.mData[18].singleDisplayRightContent =
                            data.phone

                        adapter.mData[19].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[20].singleDisplayRightContent =

                            data.additonalDxplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //马帮运输
            Constants.FragmentType.CARAVAN_TRANSPORTATION_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayGongTrans()
                val result = getRequirementCaravanTransport(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

//                        if(data.requirementTeamProjectList == null )
//                        {
//                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
//                                ToastHelper.mToast(mView.context,"没有数据")
//                            })
//                        }
//                        else
//                        {
//                            if(data.requirementTeamProjectList!!.isEmpty())
//                            {
//                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
//                                    ToastHelper.mToast(mView.context,"没有数据")
//                                })
//                            }
//                            else
//                            {
//                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //工程量清册
//                                    val listData = data.requirementTeamProjectList
//                                    mdata.putSerializable("listData1", listData as Serializable)
//                                    mdata.putInt("type",1)
//                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
//                                })
//                            }
//                        }
                        if(data.materialsType!=null)
                        adapter.mData[4].singleDisplayRightContent = data.materialsType

                        adapter.mData[5].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[6].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[7].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[7].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[7].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[7].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[8].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[8].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[8].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[9].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[10].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[11].singleDisplayRightContent =

                            data.needHorseNumber

//                        if(data.requirementListQuotations == null )
//                        {
//                            adapter.mData[12].buttonListener = listOf(View.OnClickListener {
//                                ToastHelper.mToast(mView.context,"没有数据")
//                            })
//                        }
//                        else
//                        {
//                            adapter.mData[12].buttonListener = listOf(View.OnClickListener {
//                                if(data.requirementListQuotations!!.isEmpty())
//                                {
//                                    Toast.makeText(context,"没有数据",Toast.LENGTH_SHORT).show()
//                                }
//                                else{
//                                    val listData = data.requirementListQuotations
//                                    mdata.putSerializable("listData1", listData as Serializable)
//                                    mdata.putInt("type",3)
//                                    (activity as DemandDisplayActivity).switchFragment(ProjectListFragment.newInstance(mdata))
//                                }
//                            })
//                        }
                        adapter.mData[13].singleDisplayRightContent =
                            data.name

                        adapter.mData[14].singleDisplayRightContent =
                            data.phone

                        adapter.mData[15].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[16].singleDisplayRightContent =

                            data.additonalExplain

                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //桩机服务
            Constants.FragmentType.PILE_FOUNDATION_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayPile()
                val result = getRequirementPileFoundation(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[6].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[7].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[7].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[7].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[7].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[8].singleDisplayRightContent =

                            data.requirementConstructionWorkKind

                        adapter.mData[9].singleDisplayRightContent =
                            data.roleMaxType

                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[10].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[10].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[10].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[11].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[12].singleDisplayRightContent =

                            data.journeySalary

                        when {
                            data.salaryStandard == "0" -> adapter.mData[13].singleDisplayRightContent =
                                " "
                            data.salaryStandard == "1" -> adapter.mData[13].singleDisplayRightContent =
                                "面议"
                            else -> {
                                adapter.mData[13].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[14].singleDisplayRightContent =

                            data.needPileFoundationEquipment

                        when {
                            data.otherMachineEquipment == "0" -> adapter.mData[15].singleDisplayRightContent =
                                "不需要提供"
                            data.otherMachineEquipment == "1" -> adapter.mData[15].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[15].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent =
                            data.name

                        adapter.mData[18].singleDisplayRightContent =
                            data.phone

                        adapter.mData[19].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[20].singleDisplayRightContent =

                            data.additonalExplain

                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //非开挖
            Constants.FragmentType.NON_EXCAVATION_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayTrenchiless()
                val result = getRequirementUnexcavation(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.requirementConstructionWorkKind

                        adapter.mData[6].singleDisplayRightContent =

                            "单管直径${data.diameterStandardBranchesNumber}毫米(mm)×${data.holeStandardBranchesNumber}孔"

                        adapter.mData[7].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[8].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[9].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[9].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[9].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[9].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[10].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[10].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[10].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[11].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[12].singleDisplayRightContent =

                            data.journeySalary

                        when {
                            data.salaryStandard == "0" -> adapter.mData[13].singleDisplayRightContent =
                                " "
                            data.salaryStandard == "1" -> adapter.mData[13].singleDisplayRightContent =
                                "面议"
                            else -> {
                                adapter.mData[13].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[14].singleDisplayRightContent =

                            data.needPileFoundation

                        when {
                            data.otherMachineEquipment == "0" -> adapter.mData[15].singleDisplayRightContent =
                                "不需要提供"
                            data.otherMachineEquipment == "1" -> adapter.mData[15].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[15].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[17].singleDisplayRightContent =
                            data.name

                        adapter.mData[18].singleDisplayRightContent =
                            data.phone

                        adapter.mData[19].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[20].singleDisplayRightContent =

                            data.additonalExplain

                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //试验调试
            Constants.FragmentType.TEST_DEBUGGING_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayTestAndDebugging()
                val result = getRequirementTest(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.requirementTeamVoltageClasses

                        adapter.mData[6].singleDisplayRightContent =

                            data.requirementConstructionWorkKind

                        adapter.mData[7].singleDisplayRightContent =

                            data.operationClasses

                        adapter.mData[8].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[9].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[10].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[10].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[10].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[10].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[11].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[11].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[11].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[12].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[13].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[14].singleDisplayRightContent =

                            data.needPeopleNumber

                        if (data.requirementMembersLists == null) {
                            adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context, "没有数据")
                            })//成员清册查看
                        } else {
                            if (data.requirementMembersLists!!.isEmpty()) {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })//成员清册查看
                            } else {
                                adapter.mData[15].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    bundle.putSerializable("listData2", listData as Serializable)
                                    bundle.putString("type", "成员清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })//乙方材料清册
                            }
                        }
                        when {
                            data.machineEquipment == "0" -> adapter.mData[16].singleDisplayRightContent =
                                "不需要提供"
                            data.machineEquipment == "1" -> adapter.mData[16].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[16].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[18].singleDisplayRightContent =
                            data.name

                        adapter.mData[19].singleDisplayRightContent =
                            data.phone

                        adapter.mData[20].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[21].singleDisplayRightContent =

                            data.additonalExplain

                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //跨越架
            Constants.FragmentType.CROSSING_FRAME_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayCrossFrame()
                val result = getRequirementSpanWoodenSupprt(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.spanShelfMaterial

                        adapter.mData[6].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[7].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[8].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[8].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[8].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[8].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[9].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[9].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[9].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[10].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[11].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[12].singleDisplayRightContent =

                            data.needPeopleNumber

                        if (data.requirementMembersLists == null) {
                            adapter.mData[13].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context, "没有数据")
                            })//成员清册查看
                        } else {
                            if (data.requirementMembersLists!!.isEmpty()) {
                                adapter.mData[13].buttonListener = listOf(View.OnClickListener {
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })//成员清册查看
                            } else {
                                adapter.mData[13].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    bundle.putSerializable("listData2", listData as Serializable)
                                    bundle.putString("type", "成员清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })//乙方材料清册
                            }
                        }
                        when {
                            data.machineEquipment == "0" -> adapter.mData[14].singleDisplayRightContent =
                                "不需要提供"
                            data.machineEquipment == "1" -> adapter.mData[14].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[14].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[16].singleDisplayRightContent =
                            data.name

                        adapter.mData[17].singleDisplayRightContent =
                            data.phone

                        adapter.mData[18].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[19].singleDisplayRightContent =

                            data.additonalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //运维
            Constants.FragmentType.OPERATION_AND_MAINTENANCE_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayOperationAndMaintenance()
                val result = getRequirementRunningMaintain(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementCarLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //车辆清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementCarLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type", "车辆清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        adapter.mData[5].singleDisplayRightContent =

                            data.workerExperience

                        adapter.mData[6].singleDisplayRightContent =

                            "${data.minAgeDemand}~${data.maxAgeDemand}"

                        when {
                            data.sexDemand == "0" -> adapter.mData[7].singleDisplayRightContent =
                                "女"
                            data.sexDemand == "1" -> adapter.mData[7].singleDisplayRightContent =
                                "男"
                            data.sexDemand == "-1" -> adapter.mData[7].singleDisplayRightContent =
                                "男女不限"
                            else -> {
                                adapter.mData[7].singleDisplayRightContent = " "
                            }
                        }
                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[8].singleDisplayRightContent =
                                "队部自理"
                            data.roomBoardStandard == "1" -> adapter.mData[8].singleDisplayRightContent =
                                "全包"
                            else -> {
                                adapter.mData[8].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[9].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[10].singleDisplayRightContent =

                            data.journeySalary

                        adapter.mData[11].singleDisplayRightContent =

                            data.needPeopleNumber

                        if (data.requirementMembersLists == null) {
                            adapter.mData[12].buttonListener = listOf(View.OnClickListener {
                                ToastHelper.mToast(mView.context, "没有数据")
                            })//成员清册查看
                        } else {
                            if (data.requirementMembersLists!!.isEmpty()) {
                                adapter.mData[12].buttonListener = listOf(View.OnClickListener {
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })//成员清册查看
                            } else {
                                adapter.mData[12].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementMembersLists
                                    bundle.putSerializable("listData2", listData as Serializable)
                                    bundle.putString("type", "成员清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })//乙方材料清册
                            }
                        }
                        if(data.vehicle!=null)
                        adapter.mData[13].singleDisplayRightContent =
                            data.vehicle

                        when {
                            data.machineEquipment == "0" -> adapter.mData[14].singleDisplayRightContent =
                                "不需要提供"
                            data.machineEquipment == "1" -> adapter.mData[14].singleDisplayRightContent =
                                "全部提供"
                            else -> {
                                adapter.mData[14].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[15].singleDisplayRightContent =

                            data.requirementConstructionWorkKind

                        adapter.mData[16].singleDisplayRightContent =

                            data.requirementTeamVoltageClasses

                        adapter.mData[18].singleDisplayRightContent =
                            data.name

                        adapter.mData[19].singleDisplayRightContent =
                            data.phone

                        adapter.mData[20].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[21].singleDisplayRightContent =

                            data.additonalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirmentTeamServeId",data.requirmentTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //车辆租赁
            Constants.FragmentType.VEHICLE_LEASING_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayVehicleLeasing()
                val result = getRequirementLeaseCar(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if(data.requirementCarLists == null )
                        {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                ToastHelper.mToast(mView.context,"没有数据")
                            })
                        }
                        else
                        {
                            if(data.requirementCarLists!!.isEmpty())
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    ToastHelper.mToast(mView.context,"没有数据")
                                })
                            }
                            else
                            {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener { //车辆清册
                                    val listData = data.requirementCarLists
                                    bundle.putSerializable("listData1", listData as Serializable)
                                    bundle.putString("type","车辆清册查看")

                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }

                        adapter.mData[5].singleDisplayRightContent =

                            data.workerExperience

                        when {
                            data.roomBoardStandard == "0" -> adapter.mData[6].singleDisplayRightContent =
                                "全包"
                            data.roomBoardStandard == "1" -> adapter.mData[6].singleDisplayRightContent =
                                "队部自理"
                            else -> {
                                adapter.mData[6].singleDisplayRightContent = " "
                            }
                        }
                        adapter.mData[7].singleDisplayRightContent =

                            data.journeyCarFare

                        adapter.mData[8].singleDisplayRightContent =

                            data.journeySalary

                        if(data.salaryStandard=="-1.0"){
                            adapter.mData[9].singleDisplayRightContent= "面议"
                        }else {
                            adapter.mData[9].singleDisplayRightContent= "${data.salaryStandard} ${data.salaryUnit}"
                        }


                        adapter.mData[11].singleDisplayRightContent =
                            data.name

                        adapter.mData[12].singleDisplayRightContent =
                            data.phone

                        adapter.mData[13].singleDisplayRightContent =
                            data.validTime
                        if(data.additonalExplain!=null)
                        adapter.mData[14].singleDisplayRightContent = data.additonalExplain
                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requirementTeamServeId",data.requirementTeamServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //工器具租赁
            Constants.FragmentType.TOOL_LEASING_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseConstructionTool(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =

                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime


                        if (data.requirementLeaseLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //需求租赁清单
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementLeaseLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //需求租赁清单
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementLeaseLists
                                    bundle.putSerializable("listData4", listData as Serializable)
                                    bundle.putString("type", "租赁清单查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        when {

                            data.financeTransportationInsurance == "0" -> adapter.mData[5].singleDisplayRightContent =
                                "出租方承担"
                            data.financeTransportationInsurance == "1" -> adapter.mData[5].singleDisplayRightContent =
                                "租赁方承担"
                        }
                        when {

                            data.distribution == "0" -> adapter.mData[6].singleDisplayRightContent =
                                "出租方承担"
                            data.distribution == "1" -> adapter.mData[6].singleDisplayRightContent =
                                "租赁方承担"
                        }
                        when {

                            data.partnerAttribute == "0" -> adapter.mData[7].singleDisplayRightContent =
                                "单位"
                            data.partnerAttribute == "1" -> adapter.mData[7].singleDisplayRightContent =
                                "个人"
                        }
                        if (data.hireFareStandard == "0") {
                            adapter.mData[8].singleDisplayRightContent = "按清单报价"
                        } else if (data.hireFareStandard == "1") {
                            adapter.mData[8].singleDisplayRightContent = "面议"
                        }
                        adapter.mData[10].singleDisplayRightContent =
                            data.name

                        adapter.mData[11].singleDisplayRightContent =
                            data.phone

                        adapter.mData[12].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[13].singleDisplayRightContent =

                            data.additonalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requiremenLeaseServeId",data.requiremenLeaseServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //设备租赁
            Constants.FragmentType.EQUIPMENT_LEASING_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseFacility(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementLeaseLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //需求租赁清单
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementLeaseLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //需求租赁清单
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementLeaseLists
                                    bundle.putSerializable("listData4", listData as Serializable)
                                    bundle.putString("type", "租赁清单查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        when {

                            data.financeTransportationInsurance == "0" -> adapter.mData[5].singleDisplayRightContent =
                                "出租方承担"
                            data.financeTransportationInsurance == "1" -> adapter.mData[5].singleDisplayRightContent =
                                "租赁方承担"
                        }
                        when {

                            data.distribution == "0" -> adapter.mData[6].singleDisplayRightContent =
                                "出租方承担"
                            data.distribution == "1" -> adapter.mData[6].singleDisplayRightContent =
                                "租赁方承担"
                        }
                        when {

                            data.partnerAttribute == "0" -> adapter.mData[7].singleDisplayRightContent =
                                "单位"
                            data.partnerAttribute == "1" -> adapter.mData[7].singleDisplayRightContent =
                                "个人"
                        }
                        if (data.hireFareStandard == "0") {
                            adapter.mData[8].singleDisplayRightContent = "按清单报价"
                        } else if (data.hireFareStandard == "1") {
                            adapter.mData[8].singleDisplayRightContent = "面议"
                        }
                        adapter.mData[10].singleDisplayRightContent =
                            data.name

                        adapter.mData[11].singleDisplayRightContent =

                            data.phone

                        adapter.mData[12].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[13].singleDisplayRightContent =

                            data.additonalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requiremenLeaseServeId",data.requiremenLeaseServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //机械租赁
            Constants.FragmentType.MACHINERY_LEASING_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayEquipmentLeasing()
                val result = getRequirementLeaseMachinery(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        adapter.mData[1].singleDisplayRightContent =
                            data.projectName

                        adapter.mData[2].singleDisplayRightContent =
                            data.projectSite

                        adapter.mData[3].singleDisplayRightContent =
                            data.projectTime

                        if (data.requirementLeaseLists == null) {
                            adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                //需求租赁清单
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.requirementLeaseLists!!.isEmpty()) {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    //需求租赁清单
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[4].buttonListener = listOf(View.OnClickListener {
                                    val listData = data.requirementLeaseLists
                                    bundle.putSerializable("listData4", listData as Serializable)
                                    bundle.putString("type", "租赁清单查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        when {

                            data.financeTransportationInsurance == "0" -> adapter.mData[5].singleDisplayRightContent =
                                "出租方承担"
                            data.financeTransportationInsurance == "1" -> adapter.mData[5].singleDisplayRightContent =
                                "租赁方承担"
                        }
                        when {

                            data.distribution == "0" -> adapter.mData[6].singleDisplayRightContent =
                                "出租方承担"
                            data.distribution == "1" -> adapter.mData[6].singleDisplayRightContent =
                                "租赁方承担"
                        }
                        when {

                            data.partnerAttribute == "0" -> adapter.mData[7].singleDisplayRightContent =
                                "单位"
                            data.partnerAttribute == "1" -> adapter.mData[7].singleDisplayRightContent =
                                "个人"
                        }
                        if (data.hireFareStandard == "0") {
                            adapter.mData[8].singleDisplayRightContent = "按清单报价"
                        } else if (data.hireFareStandard == "1") {
                            adapter.mData[8].singleDisplayRightContent = "面议"
                        }
                        adapter.mData[10].singleDisplayRightContent =
                            data.name

                        adapter.mData[11].singleDisplayRightContent =

                            data.phone

                        adapter.mData[12].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[13].singleDisplayRightContent =

                            data.additonalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putInt(
                                "type",
                                adapterGenerate.getType("${data.requirementType} ${data.requirementVariety}")
                            )
                            bundle.putString("id", data.id)
                            bundle.putString("requiremenLeaseServeId",data.requiremenLeaseServeId)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
            //需求三方 除资质合作
            Constants.FragmentType.TRIPARTITE_TYPE.ordinal -> {
                adapter = adapterGenerate.demandTeamDisplayDemandTripartite()
                val result = getRequirementThirdPartyDetail(
                    id,
                    UnSerializeDataBase.userToken,
                    UnSerializeDataBase.dmsBasePath
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        var data = it.message
                        adapter.mData[0].singleDisplayRightContent =

                            data.requirementVariety

                        if (data.thirdLists == null) {
                            adapter.mData[1].buttonListener = listOf(View.OnClickListener {
                                //三方服务清册
                                ToastHelper.mToast(mView.context, "没有数据")
                            })
                        } else {
                            if (data.thirdLists!!.isEmpty()) {
                                adapter.mData[1].buttonListener = listOf(View.OnClickListener {
                                    //三方服务清册
                                    ToastHelper.mToast(mView.context, "没有数据")
                                })
                            } else {
                                adapter.mData[1].buttonListener = listOf(View.OnClickListener {
                                    //三方服务清册
                                    val listData = data.thirdLists
                                    bundle.putSerializable("listData5", listData as Serializable)
                                    bundle.putString("type", "三方清册查看")
                                    FragmentHelper.switchFragment(
                                        activity!!,
                                        ProjectListFragment.newInstance(bundle),
                                        R.id.frame_my_release,
                                "register"
                                    )
                                })
                            }
                        }
                        when {
                            data.partnerAttribute == "1" -> adapter.mData[2].singleDisplayRightContent =
                                "单位"
                            data.partnerAttribute == "0" -> adapter.mData[2].singleDisplayRightContent =
                                "个人"
                        }
                        when {

                            data.fareStandard == "1" -> adapter.mData[3].singleDisplayRightContent =
                                "面议"
                            data.fareStandard == "0" -> adapter.mData[3].singleDisplayRightContent =
                                "按清单报价"
                        }
                        adapter.mData[5].singleDisplayRightContent =
                            data.name

                        adapter.mData[6].singleDisplayRightContent =
                            data.phone

                        adapter.mData[7].singleDisplayRightContent =
                            data.validTime

                        adapter.mData[8].singleDisplayRightContent =

                            data.additionalExplain


                        mView.rv_job_more_content.adapter = adapter
                        mView.rv_job_more_content.layoutManager = LinearLayoutManager(mView.context)
                        mView.btn_edit_job_information.setOnClickListener {
                            val bundle = Bundle()
                            val typeStr =
                                if(data.requirementVariety in arrayListOf("培训办证","财务记账","代办资格","标书服务","法律咨询","软件服务"))
                                    "${data.requirementType} ${data.requirementVariety}"
                                else
                                    "${data.requirementType} 其他"
                            val type = adapterGenerate.getType(typeStr)
                            bundle.putInt(
                                "type",type
                            )
                            bundle.putString("id", data.id)
                            bundle.putSerializable("data", data)
                            FragmentHelper.switchFragment(
                                activity!!,
                                ModifyJobInformationFragment.newInstance(bundle),
                                R.id.frame_my_release,
                                "register"
                            )
                        }
                    }, {
                        it.printStackTrace()
                    })
            }
        }
    }
//    private fun initFragment() {
//        when(type){
//            "需求个人"->getDataDemandIndividual()
//            "个人劳务"->getDataPersonalServices()
//        }
//        mView.tv_job_more_back.setOnClickListener {
//            activity!!.supportFragmentManager.popBackStackImmediate()
//        }
//    }
//
//    private fun getDataDemandIndividual() {
//        val result = getRequirementPersonMore(arguments!!.getString("id"))
//            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
//                val jsonObject = JSONObject(it.string())
//                val code = jsonObject.getInt("code")
//                var result = ""
//                if(code==200){
//                    val roomBoardStandard = arrayListOf("队部自理","全包")
//                    val sexs = arrayListOf("女","男","男女不限")
//                    result="当前数据获取成功"
//                    val json = jsonObject.getJSONObject("message")
//                    val sex = 1 - json.getString("sexDemand").toInt()
//                    val adapterGenerate = AdapterGenerate()
//                    adapterGenerate.context = mView.context
//                    adapter = adapterGenerate.demandIndividualDisplay()
//                    val data = adapter.mData.toMutableList()
//                    data.removeAt(data.size-1)
//                    data[0].singleDisplayRightContent = json.getString("requirementVariety")
//                    data[1].singleDisplayRightContent = json.getString("requirementMajor")
//                    data[2].singleDisplayRightContent = json.getString("projectName")
//                    data[3].singleDisplayRightContent = json.getString("projectSite")
//                    data[4].singleDisplayRightContent = json.getString("planTime")
//                    data[5].singleDisplayRightContent = json.getString("workerExperience")
//                    data[6].singleDisplayRightContent = "${json.getString("minAgeDemand")}-${json.getString("maxAgeDemand")}"
//                    data[7].singleDisplayRightContent = sexs[sex]
//                    data[8].singleDisplayRightContent = roomBoardStandard[json.getString("roomBoardStandard").toInt()]
//                    data[9].singleDisplayRightContent = json.getString("journeySalary")
//                    data[10].singleDisplayRightContent = json.getString("journeyCarFare")
//                    data[11].singleDisplayRightContent = json.getString("needPeopleNumber")
//                    data[12].singleDisplayRightContent = json.getString("salaryStandard")+json.getString("salaryUnit")
//                    data[14].singleDisplayRightContent = json.getString("name")
//                    data[15].singleDisplayRightContent = json.getString("phone")
//                    data[16].singleDisplayRightContent = json.getString("validTime")
//                    if(!json.isNull("additonalExplain"))
//                        data[17].singleDisplayRightContent = json.getString("additonalExplain")
//                    adapter.mData = data
//                    mView.rv_job_more_content.adapter = adapter
//                    mView.rv_job_more_content.layoutManager = LinearLayoutManager(context)
//                }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
//                    result="当前数据为空"
//                }
//                Toast.makeText(context,result, Toast.LENGTH_SHORT).show()
//            },{
//                it.printStackTrace()
//            })
//    }
//
//    private fun getDataPersonalServices() {
//        val result = getPersonalIssueMore(arguments!!.getString("id"))
//            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
//                val jsonObject = JSONObject(it.string())
//                val code = jsonObject.getInt("code")
//                var result = ""
//                if(code==200){
//                    val sexs = arrayListOf("女","男")
//                    result="当前数据获取成功"
//                    val json = jsonObject.getJSONObject("message")
//                    val adapterGenerate = AdapterGenerate()
//                    adapterGenerate.context = mView.context
//                    adapter = adapterGenerate.supplyIndividualDisplay()
//                    val data = adapter.mData.toMutableList()
//                    data.removeAt(data.size-1)
//                    data[0].singleDisplayRightContent = json.getString("issuerWorkType")
//                    data[1].singleDisplayRightContent = json.getString("issuerWorkerKind")
////                    data[2].singleDisplayRightContent = json.getString("contact")
//                    data[3].singleDisplayRightContent = sexs[json.getInt("sex")]
//                    data[4].singleDisplayRightContent = json.getString("age")
//                    data[5].singleDisplayRightContent = json.getString("workExperience")
//                    data[6].singleDisplayRightContent = json.getString("workMoney")+json.getString("salaryUnit")
//                    data[7].singleDisplayRightContent = json.getString("contactPhone")
//
//                    data[9].singleDisplayRightContent = json.getString("validTime")
//                    data[10].singleDisplayRightContent = json.getString("issuerBelongSite")
//                    data[11].singleDisplayRightContent = json.getString("remark")
//                    adapter.mData = data
//                    mView.rv_job_more_content.adapter = adapter
//                    mView.rv_job_more_content.layoutManager = LinearLayoutManager(context)
//                }else if(code==400 && jsonObject.getString("message")=="没有该数据"){
//                    result="当前数据为空"
//                }
//                Toast.makeText(context,result, Toast.LENGTH_SHORT).show()
//            },{
//                it.printStackTrace()
//            })
//    }
}