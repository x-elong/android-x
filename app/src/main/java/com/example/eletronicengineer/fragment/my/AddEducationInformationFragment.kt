package com.example.eletronicengineer.fragment.my

import android.content.DialogInterface
import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_education_information.view.*
import kotlinx.android.synthetic.main.fragment_my_information.view.*

class AddEducationInformationFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):AddEducationInformationFragment{
            val addEducationInformationFragment = AddEducationInformationFragment()
            addEducationInformationFragment.arguments = args
            return addEducationInformationFragment
        }
    }
    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_education_information,container,false)
        initFragment()
        return mView

    }

    private fun initFragment() {
        val mMultiStyleItemList = ArrayList<MultiStyleItem>()
        var item = MultiStyleItem(MultiStyleItem.Options.TITLE,"学历信息","3")

        item.backListener = View.OnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        item.tvSelect = View.OnClickListener {
            val networkAdapter = NetworkAdapter(mMultiStyleItemList,context!!)
            if(networkAdapter.check())
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"学历","")
        mMultiStyleItemList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"毕业院校","")
        mMultiStyleItemList.add(item)
        if(true) {
            val item = MultiStyleItem(MultiStyleItem.Options.SHIFT_INPUT, "毕业时间", "")
            mMultiStyleItemList.add(item)
            item.jumpListener = View.OnClickListener {
                val dialog = AlertDialog.Builder(context!!)
                val mDialogView = layoutInflater.inflate(R.layout.dialog_datepicker, null)
                if (item.shiftInputContent != "") {
                    val s = item.shiftInputContent.split("-")
                    mDialogView.date_picker.init(s[0].toInt(), s[1].toInt() - 1, s[2].toInt(), null)
                }
                dialog.setTitle("毕业时间")
                    .setView(mDialogView)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", DialogInterface.OnClickListener() { _, _ ->
                        item.shiftInputContent =
                            "${mDialogView.date_picker.year}-${mDialogView.date_picker.month + 1}-${mDialogView.date_picker.dayOfMonth}"
                        mView.rv_education_information_content.adapter!!.notifyItemChanged(mMultiStyleItemList.indexOf(item))
                    })
                    .create()
                    .show()
            }
        }
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT,"所学专业","")
        mMultiStyleItemList.add(item)
        (mView.rv_education_information_content.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        mView.rv_education_information_content.adapter= RecyclerviewAdapter(mMultiStyleItemList)
        mView.rv_education_information_content.layoutManager= LinearLayoutManager(context)

    }
}