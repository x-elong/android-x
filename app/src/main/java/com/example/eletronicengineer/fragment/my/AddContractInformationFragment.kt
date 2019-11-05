package com.example.eletronicengineer.fragment.my

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import kotlinx.android.synthetic.main.dialog_datepicker.view.*
import kotlinx.android.synthetic.main.fragment_contract_information.view.*

class AddContractInformationFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):AddContractInformationFragment{
            val addContractInformationFragment = AddContractInformationFragment()
            addContractInformationFragment.arguments = args
            return addContractInformationFragment
        }
    }
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_contract_information,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        val mMultiStyleItemList = ArrayList<MultiStyleItem>()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"合同信息","3")

        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item.tvSelectListener = View.OnClickListener {
            val networkAdapter = NetworkAdapter(mMultiStyleItemList,context!!)
            if(networkAdapter.check())
                activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"合同公司","")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"合同类型","")
        mMultiStyleItemList.add(item)
        if(true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "首次合同起始日", "")
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val dialog = AlertDialog.Builder(context!!)
                val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                if (item.shiftInputContent != "") {
                    val s = item.shiftInputContent.split("-")
                    mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                }
                dialog.setTitle("首次合同起始日")
                    .setView(mDialogView)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_contract_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }

        if(true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "首次合同到期日", "")
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val dialog = AlertDialog.Builder(context!!)
                val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                if (item.shiftInputContent != "") {
                    val s = item.shiftInputContent.split("-")
                    mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                }
                dialog.setTitle("首次合同到期日")
                    .setView(mDialogView)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_contract_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }

        if(true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "现次合同起始日", "")
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val dialog = AlertDialog.Builder(context!!)
                val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                if (item.shiftInputContent != "") {
                    val s = item.shiftInputContent.split("-")
                    mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                }
                dialog.setTitle("现次合同起始日")
                    .setView(mDialogView)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_contract_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }

        if(true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "现次合同到期日", "")
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val dialog = AlertDialog.Builder(context!!)
                val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                if (item.shiftInputContent != "") {
                    val s = item.shiftInputContent.split("-")
                    mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                }
                dialog.setTitle("现次合同到期日")
                    .setView(mDialogView)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_contract_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }

        item = MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT,"合同期限","(天)")
        mMultiStyleItemList.add(item)

        item = MultiStyleItem(MultiStyleItem.Options.INPUT_WITH_UNIT,"续签次数","(次)")
        mMultiStyleItemList.add(item)

        (mView.rv_contract_information_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_contract_information_content.adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_contract_information_content.layoutManager= LinearLayoutManager(context)

        (mView.rv_contract_information_content.adapter as RecyclerviewAdapter).notifyDataSetChanged()
    }
}