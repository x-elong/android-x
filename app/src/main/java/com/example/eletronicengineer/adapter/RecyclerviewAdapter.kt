package com.example.eletronicengineer.adapter

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.item_dialog_select.view.*
import kotlinx.android.synthetic.main.item_input_range.view.*
import kotlinx.android.synthetic.main.item_input_with_unit.view.*
import kotlinx.android.synthetic.main.item_multi.view.*
import kotlinx.android.synthetic.main.item_single_input.view.*
import kotlinx.android.synthetic.main.item_title.view.*
import android.os.Handler
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eletronicengineer.R
import com.example.eletronicengineer.custom.CustomDialog
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.utils.AdapterGenerate
import com.example.eletronicengineer.utils.ObserverFactory
import com.example.eletronicengineer.utils.startSendMessage
import kotlinx.android.synthetic.main.fragment_project_more.view.*
import kotlinx.android.synthetic.main.item_confirm.view.*
import kotlinx.android.synthetic.main.item_expand.view.*
import kotlinx.android.synthetic.main.item_expand.view.tv_expand_title
import kotlinx.android.synthetic.main.item_expand_list.view.*
import kotlinx.android.synthetic.main.item_expand_right.view.*
import kotlinx.android.synthetic.main.item_five_display.view.*
import kotlinx.android.synthetic.main.item_four_display.view.*
import kotlinx.android.synthetic.main.item_hint.view.*
import kotlinx.android.synthetic.main.item_image_check.view.*
import kotlinx.android.synthetic.main.item_input_with_multi_unit.view.*
import kotlinx.android.synthetic.main.item_input_with_textarea.view.*
import kotlinx.android.synthetic.main.item_new_project_disk1.view.*
import kotlinx.android.synthetic.main.item_new_project_disk2.view.*
import kotlinx.android.synthetic.main.item_public_point_position1.view.*
import kotlinx.android.synthetic.main.item_public_point_position2.view.*
import kotlinx.android.synthetic.main.item_public_point_position3.view.*
import kotlinx.android.synthetic.main.item_public_point_position4.view.*
import kotlinx.android.synthetic.main.item_search.view.*
import kotlinx.android.synthetic.main.item_shift_input.view.*
import kotlinx.android.synthetic.main.item_single_display_right.view.*
import kotlinx.android.synthetic.main.item_single_display_left.view.*
import kotlinx.android.synthetic.main.item_text_expand_select.view.*
import kotlinx.android.synthetic.main.item_title.view.tv_title_back
import kotlinx.android.synthetic.main.item_two_column_display.view.*
import kotlinx.android.synthetic.main.item_two_dialog.view.*
import kotlinx.android.synthetic.main.item_two_display.view.*
import kotlinx.android.synthetic.main.item_two_pair_input.view.*

class RecyclerviewAdapter: RecyclerView.Adapter<RecyclerviewAdapter.VH> {
    companion object {
        const val TITLE_TYPE:Int=1
        const val SELECT_DIALOG_TYPE:Int=2

        const val SINGLE_INPUT_TYPE:Int=3
        const val INPUT_RANGE_TYPE:Int=4
        const val INPUT_WITH_UNIT_TYPE=5
        const val INPUT_WITH_MULTI_UNIT_TYPE=16
        const val INPUT_WITH_TEXTAREA_TYPE=11
        const val SHIFT_INPUT_TYPE=17
        const val TWO_PAIR_INPUT_TYPE=18

        const val MULTI_BUTTON_TYPE:Int=6
        const val MULTI_RADIO_BUTTON_TYPE:Int=7
        const val MULTI_CHECKBOX_TYPE=8
        const val MULTI_HYBRID_TYPE=14
        const val TWO_OPTIONS_SELECT_DIALOG_TYPE=9
        const val THREE_OPTIONS_SELECT_DIALOG_TYPE=10

        const val HINT_TYPE=12
        const val SUBMIT_TYPE=13
        const val EXPAND_TYPE=15
        const val SINGLE_DISPLAY_RIGHT_TYPE=19

        const val FOUR_DISPLAY_TYPE=20
        const val TWO_COLUMN_DISPLAY_TYPE=21
        const val EXPAND_LIST_TYPE=22
        const val SINGLE_DISPLAY_LEFT_TYPE=23
        const val TWO_DISPLAY_TYPE=24

        const val EXPAND_RIGHT_TYPE = 25
        const val TWO_TEXT_DIALOG_TYPE = 26
        const val SEARCH_TYPE = 27
        const val FIVE_DISPLAY_TYPE=29

        // alterPosition time:2019/7/15
        // function:公共点定位
        const val POSITION_ADD_TYPE = 30
        const val POSITION_DELETE_TYPE = 31
        const val POSITION_START_TYPE = 32
        const val POSITION_END_TYPE = 33
        // alterPosition time:2019/7/15
        // function://新建项目盘中标题+带边框的输入框
        const val TITLE_INPUT_BG_TYPE = 34
        //新建项目盘中标题+带边框的输入框+选择
        const val TITLE_INPUT_BG_SELECT_TYPE = 35
        // alterPosition time:2019/7/15
        // function:扩展+选择
        const val TEXT_EXPAND_SELECT_TYPE = 36

        const val NODE_CHECK_IMAGE_SHOW_TYPE = 37

        const val BLANK_TYPE=50
        const val MESSAGE_SELECT_OK:Int=100
    }
    fun dataToMap(adapter: RecyclerviewAdapter):Map<String,String>
    {
        val result=HashMap<String,String>()
        val mData=adapter.mData
        val VHList=adapter.VHList
        for (i in 0 until mData.size)
        {
            val multiStyleItem=mData[i]
            val viewHolder=VHList[i]
            when(viewHolder.itemViewType)
            {
                TITLE_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.tvTitle1.text.toString()
                }
                SINGLE_INPUT_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etSingleInputContent.text.toString()
                }
                INPUT_WITH_UNIT_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etInputUnitValue.text.toString()
                }
                INPUT_WITH_MULTI_UNIT_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etMultiInputContent.text.toString()
                }
                INPUT_RANGE_TYPE->
                {
                    val keys=multiStyleItem.key.split(" ")
                    result[keys[0]]=viewHolder.etRangeValue1.text.toString()
                    result[keys[1]]=viewHolder.etRangeValue2.text.toString()
                }
                INPUT_WITH_TEXTAREA_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etTextAreaContent.text.toString()
                }
                TWO_PAIR_INPUT_TYPE->
                {
                    val keys = multiStyleItem.key.split(" ")
                    result[keys[0]] = viewHolder.etTwoPairInputValue1.text.toString()
                    result[keys[1]] = viewHolder.etTwoPairInputValue2.text.toString()
                }
                SELECT_DIALOG_TYPE, TWO_OPTIONS_SELECT_DIALOG_TYPE, THREE_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    result[multiStyleItem.key]=viewHolder.etDialogSelectItem.text.toString()
                }
                SHIFT_INPUT_TYPE->
                {

                }
                MULTI_RADIO_BUTTON_TYPE->
                {
                    var value=1
                    for (j in 0 until viewHolder.llContainer.childCount)
                    {
                        val radioButton=viewHolder.llContainer.getChildAt(j) as RadioButton
                        if (radioButton.isChecked)
                        {
                            result[multiStyleItem.key]=value.toString()
                            break
                        }
                        else
                            value--
                    }
                }
            }//end when
        }//end for
        return result
    }
    var mData:List<MultiStyleItem>
    var adapterObserver:ObserverFactory.RecyclerviewAdapterObserver?=null
    var expandList:MutableList<VH> =ArrayList()
    var expandPosition:Int=-1
    val VHList:MutableList<VH> =ArrayList()

    inner class VH:RecyclerView.ViewHolder
    {
        var itemPosition=-1
        lateinit var tvTitle1:TextView
        lateinit var tvBack:TextView
        lateinit var tvSelectMore:TextView
        lateinit var tvSelectAdd:TextView

        lateinit var multiTitle:TextView
        lateinit var llContainer: LinearLayout
        var btnList:MutableList<Button> = mutableListOf()

        lateinit var tvSingleInputTitle:TextView
        lateinit var etSingleInputContent:EditText

        lateinit var tvRangeTitle:TextView
        lateinit var tvRangeUnit1:TextView
        lateinit var tvRangeUnit2:TextView
        lateinit var etRangeValue1:EditText
        lateinit var etRangeValue2:EditText

        lateinit var tvInputUnit: TextView
        lateinit var tvInputUnitTitle:TextView
        lateinit var etInputUnitValue:EditText

        lateinit var tvShiftInputTitle:TextView
        lateinit var tvShiftInputContent:TextView
        lateinit var tvShiftInputShow:TextView

        lateinit var tvTwoPairInputTitle:TextView
        lateinit var tvTwoPairInputItem1:TextView
        lateinit var tvTwoPairInputItem2:TextView
        lateinit var tvTwoPairInputItem3:TextView
        lateinit var etTwoPairInputValue1:EditText
        lateinit var etTwoPairInputValue2:EditText

        //four
        lateinit var tvfourDisplayTitle:TextView
        lateinit var tvfourDisplayItem1:TextView
        lateinit var tvfourDisplayItem2:TextView
        lateinit var tvfourDisplayItem3:TextView

        //fivedisplay
        lateinit var tvFiveDisplayTitle:TextView
        lateinit var tvFiveDisplayItem1:TextView
        lateinit var tvFiveDisplayItem2:TextView
        lateinit var tvFiveDisplayItem3:TextView
        lateinit var tvFiveDisplayItem4:TextView

        lateinit var tvTwoColumnDisplayTitle1:TextView
        lateinit var tvTwoColumnDisplayContent1:TextView
        lateinit var tvTwoColumnDisplayTitle2:TextView
        lateinit var tvTwoColumnDisplayContent2:TextView

        lateinit var tvExpandListTitle:TextView
        lateinit var rvExpandListContent:RecyclerView
        lateinit var tvExpandListButton:TextView

        lateinit var tvsingleDisplayLeftTitle:TextView
        lateinit var tvsingleDisplayLeftContent:TextView

        lateinit var tvTwoDisplayTitle:TextView
        lateinit var tvTwoDisplayContent1:TextView
        lateinit var tvTwoDisplayContent2:TextView

        lateinit var spMultiInputUnit:Spinner
        lateinit var tvMultiInputUnitTitle:TextView
        lateinit var etMultiInputContent:EditText
        lateinit var tvMultiSelectShow:TextView

        lateinit var tvDialogSelectTitle:TextView
        lateinit var etDialogSelectItem:TextView
        lateinit var tvDialogSelectShow:TextView

        lateinit var tvTextAreaTitle:TextView
        lateinit var etTextAreaContent:TextView

        lateinit var tvHint:TextView

        lateinit var btnSubmit:Button

        lateinit var tvExpandTitle:TextView
        lateinit var tvExpandContent:TextView
        lateinit var tvExpandButton:TextView
        lateinit var tvExpandMore:TextView

        lateinit var tvsingleDisplayRightTitle:TextView
        lateinit var tvsingleDisplayRightContent:TextView

        //带菜单项的标题
        lateinit var tvTitle2:TextView
        //业主单位
        lateinit var tvTextFirstDialog:TextView
        lateinit var tvTextSecondDialog:TextView
        //滑动
        lateinit var tvTextDetails:TextView
        lateinit var tvTextMore:TextView
        lateinit var tvTextDelete:TextView

        // alterPosition time:2019/7/15
        // function: //公共点定位     仓库定位  +
        lateinit var tvPositionTitle1:TextView
        // alterPosition time:2019/7/15
        // function: //公共点定位     1号仓库 输入框 定位图标   -
        lateinit var tvPositionInputTitle:TextView
        lateinit var etPositionInputContent:EditText
        // alterPosition time:2019/7/15
        // function: //公共点定位     1号 始 输入框 定位图标   -
        lateinit var tvPositionInputTitle1:TextView
        lateinit var tvPositionInputTitle2:TextView
        lateinit var etPositionInputContent1:EditText
        // alterPosition time:2019/7/15
        // function: //公共点定位     终 输入框 定位图标
        lateinit var tvPositionInputTitle3:TextView
        lateinit var etPositionInputContent2:EditText
        // alterPosition time:2019/7/15
        // function: // //新建项目盘中标题+带边框的输入框
        lateinit var tvSingleInputTitleWithBg:TextView
        lateinit var etSingleInputContentWithBg:EditText
        // alterPosition time:2019/7/15
        // function: // //新建项目盘中标题+带边框的输入框+选择
        lateinit var tvSingleInputTitleWithBgSelect:TextView
        lateinit var etSingleInputContentWithBgSelect:EditText

        // function:自查自检图片显示
        lateinit var tvContentDetail:TextView
        lateinit var tvCheckMore:TextView

        // function:搜索
        lateinit var etSearchContent:EditText

        var mHandler:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                MESSAGE_SELECT_OK->
                {
                    val selectContent=it.data.getString("selectContent")
                    if(selectContent.equals("自定义")){
                        etDialogSelectItem.isEnabled=true
                        etDialogSelectItem.hint="请输入"
                        etDialogSelectItem.text=""
                    }else if(selectContent.equals("填写")){
                        mData[position].options=MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT
                        mData[position].inputMultiUnitTitle=mData[position].selectTitle
                        mData[position].inputMultiUnit= listOf("元/天","元/月","元/年")
                        tvMultiSelectShow.setOnClickListener {
                        }

                    }else {
                        etDialogSelectItem.isEnabled=false
                        etDialogSelectItem.text=selectContent
                        mData[itemPosition].selectContent=selectContent!!
                    }
                    false
                }
                else->
                {
                    false
                }
            }
        })

        var mHandler1:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                MESSAGE_SELECT_OK->
                {
                    val selectContent=it.data.getString("selectContent")
                        tvTextFirstDialog.text=selectContent
                    false
                }
                else->
                {
                    false
                }
            }
        })
        var mHandler2:Handler= Handler(Handler.Callback {
            when(it.what)
            {
                MESSAGE_SELECT_OK->
                {
                    val selectContent=it.data.getString("selectContent")
                        tvTextSecondDialog.text=selectContent
                    false
                }
                else->
                {
                    false
                }
            }
        })

        constructor(v:View,viewType: Int):super(v)
        {
            when(viewType)
            {
                TITLE_TYPE->
                {
                    //root root.findViewById(R,id.tv_title_title1)
                    tvTitle1=v.tv_title_title1
                    tvBack=v.tv_title_back
                    tvSelectMore=v.tv_select_more
                    tvSelectAdd=v.tv_select_add
                }
                MULTI_BUTTON_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                MULTI_RADIO_BUTTON_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                MULTI_CHECKBOX_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                MULTI_HYBRID_TYPE->
                {
                    llContainer=v.cl_multi_container
                    multiTitle=v.tv_multi_title
                }
                SINGLE_INPUT_TYPE->
                {
                    tvSingleInputTitle=v.tv_input_title
                    etSingleInputContent=v.et_input_content
                }
                TWO_PAIR_INPUT_TYPE->
                {
                    tvTwoPairInputTitle=v.tv_two_pair_input_title
                    tvTwoPairInputItem1=v.tv_two_pair_input_item1
                    tvTwoPairInputItem2=v.tv_two_pair_input_item2
                    tvTwoPairInputItem3=v.tv_two_pair_input_item3
                    etTwoPairInputValue1=v.et_two_pair_input_value1
                    etTwoPairInputValue2=v.et_two_pair_input_value2
                }

                FOUR_DISPLAY_TYPE->
                {
                    tvfourDisplayTitle=v.tv_four_display_title
                    tvfourDisplayItem1=v.tv_four_display_item1
                    tvfourDisplayItem2=v.tv_four_display_item2
                    tvfourDisplayItem3=v.tv_four_display_item3
                }
                FIVE_DISPLAY_TYPE->{
                    tvFiveDisplayTitle=v.tv_five_display_title
                    tvFiveDisplayItem1=v.tv_five_display_item1
                    tvFiveDisplayItem2=v.tv_five_display_item2
                    tvFiveDisplayItem3=v.tv_five_display_item3
                    tvFiveDisplayItem4=v.tv_five_display_item4
                }
                TWO_COLUMN_DISPLAY_TYPE->
                {
                    tvTwoColumnDisplayTitle1=v.tv_two_column_display_title1
                    tvTwoColumnDisplayContent1=v.tv_two_column_display_item1
                    tvTwoColumnDisplayTitle2=v.tv_two_column_display_title2
                    tvTwoColumnDisplayContent2=v.tv_two_column_display_item2
                }
                EXPAND_LIST_TYPE->
                {
                    tvExpandListTitle=v.tv_expand_list_title
                    tvExpandListButton=v.tv_expand_list_button
                    rvExpandListContent=v.rv_expand_list_content
                }
                SINGLE_DISPLAY_LEFT_TYPE->
                {
                    tvsingleDisplayLeftTitle=v.tv_single_display_expand_title
                    tvsingleDisplayLeftContent=v.tv_single_display_expand_content
                }
                TWO_DISPLAY_TYPE->
                {
                    tvTwoDisplayTitle=v.tv_two_display_title
                    tvTwoDisplayContent1=v.tv_two_display_content1
                    tvTwoDisplayContent2=v.tv_two_display_content2
                }
                INPUT_RANGE_TYPE->
                {
                    tvRangeTitle=v.tv_range_title
                    tvRangeUnit1=v.tv_range_unit1
                    tvRangeUnit2=v.tv_range_unit2
                    etRangeValue1=v.et_range_value1
                    etRangeValue2=v.et_range_value2
                }
                INPUT_WITH_UNIT_TYPE->
                {
                    tvInputUnitTitle=v.tv_input_unit_title
                    tvInputUnit=v.tv_input_unit
                    etInputUnitValue=v.et_input_unit_value
                }
                INPUT_WITH_MULTI_UNIT_TYPE->
                {
                    spMultiInputUnit=v.sp_input_multi_unit_unit
                    tvMultiInputUnitTitle=v.tv_input_multi_unit_title
                    etMultiInputContent=v.et_input_multi_unit_content
                    tvMultiSelectShow=v.tv_multi_select_show
                }
                SHIFT_INPUT_TYPE->
                {
                    tvShiftInputTitle=v.tv_shift_input_title
                    tvShiftInputContent=v.et_shift_input_item
                    tvShiftInputShow=v.tv_shift_input_show
                }
                SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    etDialogSelectItem=v.et_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                TWO_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    etDialogSelectItem=v.et_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                THREE_OPTIONS_SELECT_DIALOG_TYPE->
                {
                    tvDialogSelectTitle=v.tv_dialog_select_title
                    etDialogSelectItem=v.et_dialog_select_item
                    tvDialogSelectShow=v.tv_dialog_select_show
                }
                INPUT_WITH_TEXTAREA_TYPE->
                {
                    tvTextAreaTitle=v.tv_textarea_title
                    etTextAreaContent=v.et_textarea_content
                }
                HINT_TYPE->
                {
                    tvHint=v.tv_hint_content
                }
                SUBMIT_TYPE->
                {
                    btnSubmit=v.btn_confirm_submit
                }
                EXPAND_TYPE->
                {
                    tvExpandButton=v.tv_expand_button
                    tvExpandTitle=v.tv_expand_title
                    tvExpandContent=v.tv_expand_content
                }
                SINGLE_DISPLAY_RIGHT_TYPE->
                {
                    tvsingleDisplayRightTitle=v.tv_single_display_title
                    tvsingleDisplayRightContent=v.tv_single_display_content
                }
                //业主单位
                TWO_TEXT_DIALOG_TYPE->
                {
                    tvTextFirstDialog=v.tv_dialog_select_unit_show
                    tvTextSecondDialog=v.tv_dialog_select_project_show
                }

                EXPAND_RIGHT_TYPE->
                {
                    tvExpandButton=v.tv_expand_button1
                    tvExpandTitle=v.tv_expand_title1
                    tvExpandContent=v.tv_expand_content1
                }
                // alterPosition time:2019/7/15
                // function: //公共点定位     仓库定位  +
                POSITION_ADD_TYPE->
                {
                    //root root.findViewById(R,id.tv_title_title1)
                    tvPositionTitle1=v.tv_position_title
                }
                // alterPosition time:2019/7/15
                // function: //公共点定位     1号仓库 输入框 定位图标   -
                POSITION_DELETE_TYPE->
                {
                    tvPositionInputTitle=v.tv_position_child_title
                    etPositionInputContent=v.tv_position_child_item1
                }
                // alterPosition time:2019/7/15
                // function: //公共点定位     1号 始 输入框 定位图标   -
                POSITION_START_TYPE->
                {
                    tvPositionInputTitle1=v.tv_text_title
                    tvPositionInputTitle2=v.tv_text_start_title
                    etPositionInputContent1=v.tv_position_child_item2
                }
                // alterPosition time:2019/7/15
                // function: //公共点定位     终 输入框 定位图标
                POSITION_END_TYPE->
                {
                    tvPositionInputTitle3=v.tv_text_end_title
                    etPositionInputContent2=v.tv_position_child_item3
                }
                // alterPosition time:2019/7/15
                // function: //新建项目标题+带边框的输入框
                TITLE_INPUT_BG_TYPE->
                {
                    tvSingleInputTitleWithBg = v.tv_new_project_disk1_title
                    etSingleInputContentWithBg=v.et_new_project_disk1_input
                }
                // alterPosition time:2019/7/15
                // function: //新建项目标题+带边框的输入框+选择
                TITLE_INPUT_BG_SELECT_TYPE->
                {
                    tvSingleInputTitleWithBgSelect = v.tv_new_project_disk2_title
                    etSingleInputContentWithBgSelect=v.et_new_project_disk2_input
                }
                // alterPosition time:2019/7/15
                // function:扩展+选择
                TEXT_EXPAND_SELECT_TYPE->
                {
                    tvExpandButton=v.tv_project_expand
                    tvExpandTitle=v.tv_project_expand_title
                    tvExpandContent=v.tv_project_expand_content
                    tvExpandMore=v.tv_project_expand_more
                }
                NODE_CHECK_IMAGE_SHOW_TYPE->{
                    tvContentDetail=v.tv_check_title
                    tvCheckMore=v.tv_check_more
                }
                // function:搜索
                SEARCH_TYPE->
                {
                    etSearchContent = v.et_search
                }
                BLANK_TYPE->{

                }
            }
            VHList.add(this)
        }
    }
    constructor(data:List<MultiStyleItem>)
    {
        this.mData=data
        for(i in 0 until mData.size/2)
        {

        }
    }
    override fun getItemViewType(position: Int): Int {
        val multiStyleItem=mData.get(position)
        when(multiStyleItem.options)
        {
            MultiStyleItem.Options.TITLE->return TITLE_TYPE
            MultiStyleItem.Options.MULTI_BUTTON->return MULTI_BUTTON_TYPE
            MultiStyleItem.Options.SINGLE_INPUT->return SINGLE_INPUT_TYPE
            MultiStyleItem.Options.INPUT_WITH_UNIT->return INPUT_WITH_UNIT_TYPE
            MultiStyleItem.Options.INPUT_WITH_MULTI_UNIT->return INPUT_WITH_MULTI_UNIT_TYPE
            MultiStyleItem.Options.SELECT_DIALOG->return SELECT_DIALOG_TYPE
            MultiStyleItem.Options.INPUT_RANGE->return INPUT_RANGE_TYPE
            MultiStyleItem.Options.SHIFT_INPUT->return SHIFT_INPUT_TYPE
            MultiStyleItem.Options.MULTI_RADIO_BUTTON->return MULTI_RADIO_BUTTON_TYPE
            MultiStyleItem.Options.MULTI_CHECKBOX->return MULTI_CHECKBOX_TYPE
            MultiStyleItem.Options.TWO_OPTIONS_SELECT_DIALOG->return TWO_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG->return THREE_OPTIONS_SELECT_DIALOG_TYPE
            MultiStyleItem.Options.INPUT_WITH_TEXTAREA->return INPUT_WITH_TEXTAREA_TYPE
            MultiStyleItem.Options.HINT->return HINT_TYPE
            MultiStyleItem.Options.SUBMIT->return SUBMIT_TYPE
            MultiStyleItem.Options.MULTI_HYBRID-> return MULTI_HYBRID_TYPE
            MultiStyleItem.Options.EXPAND->return EXPAND_TYPE
            MultiStyleItem.Options.TWO_PAIR_INPUT->return TWO_PAIR_INPUT_TYPE
            MultiStyleItem.Options.SINGLE_DISPLAY_RIGHT->return SINGLE_DISPLAY_RIGHT_TYPE
            MultiStyleItem.Options.FOUR_DISPLAY->return FOUR_DISPLAY_TYPE
            MultiStyleItem.Options.FIVE_DISPLAY->return FIVE_DISPLAY_TYPE
            MultiStyleItem.Options.TWO_COLUMN_DISPLAY->return TWO_COLUMN_DISPLAY_TYPE
            MultiStyleItem.Options.EXPAND_LIST->return EXPAND_LIST_TYPE
            MultiStyleItem.Options.SINGLE_DISPLAY_LEFT->return SINGLE_DISPLAY_LEFT_TYPE
            MultiStyleItem.Options.TWO_DISPLAY->return TWO_DISPLAY_TYPE
            MultiStyleItem.Options.EXPAND_RIGHT->return EXPAND_RIGHT_TYPE
            MultiStyleItem.Options.TWO_TEXT_DIALOG->return TWO_TEXT_DIALOG_TYPE
            // alterPosition time:2019/7/15
            // function:公共点定位
            MultiStyleItem.Options.POSITION_ADD->return POSITION_ADD_TYPE
            MultiStyleItem.Options.POSITION_DELETE->return POSITION_DELETE_TYPE
            MultiStyleItem.Options.POSITION_START->return POSITION_START_TYPE
            MultiStyleItem.Options.POSITION_END->return POSITION_END_TYPE
            // alterPosition time:2019/7/15
            // function: //新建项目盘
            MultiStyleItem.Options.TITLE_INPUT_BG->return TITLE_INPUT_BG_TYPE
            MultiStyleItem.Options.TITLE_INPUT_BG_SELECT->return TITLE_INPUT_BG_SELECT_TYPE
            // alterPosition time:2019/7/15
            // function:扩展+选择
            MultiStyleItem.Options.TEXT_EXPAND_SELECT->return TEXT_EXPAND_SELECT_TYPE
            MultiStyleItem.Options.NODE_CHECK_IMAGE_SHOW->return NODE_CHECK_IMAGE_SHOW_TYPE
            MultiStyleItem.Options.SEARCH->return SEARCH_TYPE
            MultiStyleItem.Options.BLANK->return BLANK_TYPE
            else->
            {
                return TITLE_TYPE
            }
        }
    }
    var time=0
    override fun onBindViewHolder(vh: VH, position: Int) {
        vh.itemPosition=position
        vh.setIsRecyclable(false)
        if (adapterObserver!=null)
        {
            if (position==mData.size-1)
            {
                adapterObserver!!.onBindComplete()
            }
            adapterObserver!!.onBindRunning()
        }
        when(getItemViewType(position))
        {
            TITLE_TYPE->
            {
                //vh.tvTitle1.setText(mData[position].getTitle1())
                vh.tvTitle1.text=mData[position].title1
                if (mData[position].backListener!=null)
                {
                    vh.tvBack.setOnClickListener(mData[position].backListener)
                }
                if(mData[position].styleType.equals("1")){
                    vh.tvSelectMore.setOnClickListener(mData[position].tvSelect)
                    vh.tvSelectMore.visibility=View.VISIBLE
                }else if(mData[position].styleType.equals("2")){
                    vh.tvSelectAdd.setOnClickListener(mData[position].tvSelect)
                    vh.tvSelectAdd.visibility=View.VISIBLE
                }

            }
            MULTI_BUTTON_TYPE->
            {
                vh.multiTitle.text=mData[position].buttonTitle
                for (name:String in mData[position].buttonName)
                {
                    val index=mData[position].buttonName.indexOf(name)
                    val btnItem= Button(vh.itemView.context)
                    btnItem.text=name
                    if (index<=mData[position].buttonListener.size-1)
                    {
                        btnItem.setOnClickListener(mData[position].buttonListener[index])
                    }
                    btnItem.setBackgroundResource(R.drawable.btn_style1)
                    vh.llContainer.addView(btnItem)
                    val params:ViewGroup.MarginLayoutParams= btnItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=vh.itemView.resources.getDimension(R.dimen.general_20).toInt()
                    btnItem.layoutParams=params
                    vh.btnList.add(btnItem)
                }
            }
            MULTI_RADIO_BUTTON_TYPE->
            {
                val context=vh.itemView.context
                vh.multiTitle.text=mData[position].radioButtonTitle
                val radioGroup=RadioGroup(vh.itemView.context)
                radioGroup.orientation=LinearLayout.HORIZONTAL
                for (name:String in mData[position].radioButtonName)
                {
                    val radioItem=RadioButton(context)
                    radioItem.text=name
                    radioItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    radioItem.setOnCheckedChangeListener{
                            compoundButton, bool ->
                        if (bool)
                        {
                            val index=mData[position].radioButtonName.indexOf(compoundButton.text.toString())
                            mData[position].radioButtonValue=(1-index).toString()
                        }
                    }
                    radioGroup.addView(radioItem)
                    val params=radioItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    radioItem.layoutParams=params
                }
                if (mData[position].radioButtonValue!="")
                {
                    val item=radioGroup.getChildAt(1-mData[position].radioButtonValue.toInt()) as RadioButton
                    item.isChecked=true
                }
                vh.llContainer.addView(radioGroup)
            }
            MULTI_CHECKBOX_TYPE->
            {
                val context=vh.itemView.context
                vh.multiTitle.text=mData[position].checkboxTitle
                mData[position].checkboxValueList=ArrayList(mData[position].checkboxNameList.size)
                for (name in mData[position].checkboxNameList)
                {
                    val checkboxItem=CheckBox(context)
                    checkboxItem.text=name
                    checkboxItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    vh.llContainer.addView(checkboxItem)
                    mData[position].checkboxValueList.add(false)
                    checkboxItem.setOnCheckedChangeListener{
                            compoundButton, bool ->
                        val index=mData[position].checkboxNameList.indexOf(compoundButton.text)
                        if (bool)
                        {
                            mData[position].checkboxValueList.set(index,true)
                        }
                        else
                        {
                            mData[position].checkboxValueList.set(index,false)
                        }
                    }
                    val params=checkboxItem.layoutParams as ViewGroup.MarginLayoutParams
                    params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                    checkboxItem.layoutParams=params
                }
                for (i in 0 until vh.llContainer.childCount-1)
                {
                    val item=vh.llContainer.getChildAt(i) as CheckBox
                    item.isChecked=mData[position].checkboxValueList[i]
                }
            }
            MULTI_HYBRID_TYPE->
            {
                val context=vh.itemView.context
                vh.multiTitle.text=mData[position].hybridTitle
                val radioGroup=RadioGroup(context)
                if (mData[position].hybridCheckBoxName.size>0)
                {
                    for (checkboxName in mData[position].hybridCheckBoxName)
                    {
                        val checkboxItem=CheckBox(context)
                        checkboxItem.text=checkboxName
                        checkboxItem.textSize=context.resources.getDimension(R.dimen.font_tv_hint_15)
                        vh.llContainer.addView(checkboxItem)
                        checkboxItem.setOnCheckedChangeListener{
                                compoundButton, bool ->
                            val index=mData[position].checkboxNameList.indexOf(compoundButton.text)
                            if (bool)
                            {
                                mData[position].checkboxValueList.set(index,true)
                            }
                            else
                            {
                                mData[position].checkboxValueList.set(index,false)
                            }
                        }
                        val params=checkboxItem.layoutParams as ViewGroup.MarginLayoutParams
                        params.topMargin=context.resources.getDimension(R.dimen.top_5).toInt()
                        params.leftMargin=context.resources.getDimension(R.dimen.general_10).toInt()
                        checkboxItem.layoutParams=params
                    }
                }
                if (mData[position].hybridRadioButtonName.size>0)
                {
                    radioGroup.orientation=LinearLayout.HORIZONTAL
                    for (name:String in mData[position].hybridRadioButtonName)
                    {
                        val radioItem=RadioButton(context)
                        radioItem.text=name
                        radioItem.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                        radioGroup.addView(radioItem)
                        radioItem.setOnCheckedChangeListener{
                                compoundButton, bool ->
                            if (bool)
                            {
                                val index=mData[position].radioButtonName.indexOf(compoundButton.text.toString())
                                mData[position].radioButtonValue=(1-index).toString()
                            }
                        }
                        val params=radioItem.layoutParams as ViewGroup.MarginLayoutParams
                        params.topMargin=context.resources.getDimension(R.dimen.top_5).toInt()
                        params.leftMargin=context.resources.getDimension(R.dimen.general_20).toInt()
                        radioItem.layoutParams=params
                    }
                    vh.llContainer.addView(radioGroup)
                }
                if (mData[position].hybridButtonName.size>0)
                {
                    for (name:String in mData[position].hybridButtonName)
                    {
                        val index=mData[position].hybridButtonName.indexOf(name)
                        val btnItem= Button(vh.itemView.context)
                        btnItem.text=name
                        btnItem.setBackgroundResource(R.drawable.btn_style1)
                        if (index<=mData[position].hybridButtonListeners.size-1)
                        {
                            btnItem.setOnClickListener(mData[position].hybridButtonListeners[index])
                        }
                        vh.llContainer.addView(btnItem)
                        val params:ViewGroup.MarginLayoutParams= btnItem.layoutParams as ViewGroup.MarginLayoutParams
                        params.leftMargin=vh.itemView.resources.getDimension(R.dimen.general_20).toInt()
                        btnItem.layoutParams=params
                        vh.btnList.add(btnItem)
                    }
                }
            }
            SINGLE_INPUT_TYPE->
            {
                vh.tvSingleInputTitle.text=mData[position].inputSingleTitle
                vh.etSingleInputContent.addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputSingleContent=p0.toString()
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }
                })
                if (mData[position].inputSingleContent!="")
                {
                    vh.etSingleInputContent.setText(mData[position].inputSingleContent)
                }
                if(mData[position].inputSingleTitle.equals("类别")){
                    vh.tvShiftInputContent.hint="规格/型号状态"
                }
            }
            INPUT_WITH_UNIT_TYPE->
            {
                vh.tvInputUnitTitle.text=mData[position].inputUnitTitle
                vh.tvInputUnit.text=mData[position].inputUnit
                vh.etInputUnitValue.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputUnitContent=p0.toString()
                    }
                })
                if (mData[position].inputUnitContent!="")
                {
                    vh.etInputUnitValue.setText(mData[position].inputUnitContent)
                }
            }
            TWO_PAIR_INPUT_TYPE->
            {
                vh.tvTwoPairInputTitle.text=mData[position].twoPairInputTitle
                vh.tvTwoPairInputItem1.text=mData[position].twoPairInputItem1
                vh.tvTwoPairInputItem2.text=mData[position].twoPairInputItem2
                vh.tvTwoPairInputItem3.text=mData[position].twoPairInputItem3
                vh.etTwoPairInputValue1.addTextChangedListener(object:TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].twoPairInputValue1=p0.toString()
                    }
                })
                vh.etTwoPairInputValue2.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].twoPairInputValue2=p0.toString()
                    }
                })
            }
            FOUR_DISPLAY_TYPE->
            {
                vh.tvfourDisplayTitle.text=mData[position].fourDisplayTitle
                vh.tvfourDisplayItem1.text=mData[position].fourDisplayContent1
                vh.tvfourDisplayItem2.text=mData[position].fourDisplayContent2
                vh.tvfourDisplayItem3.text=mData[position].fourDisplayContent3

                if (mData[position].fourDisplayContent2.equals("详情"))
                {
                    vh.tvfourDisplayItem2.setBackgroundResource(R.drawable.btn_style1)
                    vh.tvfourDisplayItem2.setOnClickListener(mData[position].fourDisplayListener)
                    vh.tvfourDisplayItem2.setOnClickListener(mData[position].fiveDisplayListener)
                }
                if (mData[position].fourDisplayContent3.equals("···"))
                {
                    vh.tvfourDisplayItem3.setOnClickListener(mData[position].fourDisplayListener)
                    vh.tvfourDisplayItem3.setTypeface(Typeface.DEFAULT_BOLD)
                }
            }
            FIVE_DISPLAY_TYPE->{
                vh.tvFiveDisplayTitle.text=mData[position].fiveDisplayTitle
                vh.tvFiveDisplayItem1.text=mData[position].fiveDisplayContent1
                vh.tvFiveDisplayItem2.text=mData[position].fiveDisplayContent2
                vh.tvFiveDisplayItem3.text=mData[position].fiveDisplayContent3
                vh.tvFiveDisplayItem4.text=mData[position].fiveDisplayContent4
                if (mData[position].fiveDisplayContent4.equals("···"))
                {
                    vh.tvFiveDisplayItem4.setOnClickListener(mData[position].fiveDisplayListener)
                    vh.tvFiveDisplayItem4.setTypeface(Typeface.DEFAULT_BOLD)
                }
            }
            TWO_COLUMN_DISPLAY_TYPE->
            {
                vh.tvTwoColumnDisplayTitle1.text=mData[position].twoColumnDisplayTitle1
                vh.tvTwoColumnDisplayContent1.text=mData[position].twoColumnDisplayContent1
                vh.tvTwoColumnDisplayTitle2.text=mData[position].twoColumnDisplayTitle2
                vh.tvTwoColumnDisplayContent2.text=mData[position].twoColumnDisplayContent2
            }
            SINGLE_DISPLAY_LEFT_TYPE->
            {
                vh.tvsingleDisplayLeftTitle.text=mData[position].singleDisplayLeftTitle
                vh.tvsingleDisplayLeftContent.text=mData[position].singleDisplayLeftContent
            }
            TWO_DISPLAY_TYPE->
            {
                vh.tvTwoDisplayTitle.text=mData[position].twoDisplayTitle
                vh.tvTwoDisplayContent1.text=mData[position].twoDisplayContent1
                vh.tvTwoDisplayContent2.text=mData[position].twoDisplayContent2

                if (mData[position].twoDisplayContent2.equals("上传照片"))
                {
                    vh.tvTwoDisplayContent2.setBackgroundResource(R.drawable.btn_style1)
                    vh.tvTwoDisplayContent2.setOnClickListener(mData[position].twoDisplayListener)
                }
            }
            INPUT_WITH_MULTI_UNIT_TYPE->
            {
                val context=vh.itemView.context
                vh.tvMultiInputUnitTitle.text=mData[position].inputMultiUnitTitle
                val adapter=ArrayAdapter(context,R.layout.item_dropdown,mData[position].inputMultiUnit)
                adapter.setDropDownViewResource(R.layout.item_dropdown)
                vh.etMultiInputContent.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputMultiContent=p0.toString()
                    }
                })
                vh.spMultiInputUnit.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        mData[position].inputMultiSelectUnit=adapter.getItem(p2).toString()
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
                if (mData[position].inputMultiContent!="")
                {
                    vh.etMultiInputContent.setText(mData[position].inputMultiContent)
                }
                if (mData[position].inputMultiSelectUnit!="")
                {
                    val position=adapter.getPosition(mData[position].inputMultiSelectUnit)
                    vh.spMultiInputUnit.setSelection(position)
                }
                vh.spMultiInputUnit.adapter=adapter
                vh.spMultiInputUnit.setSelection(1)
            }
            INPUT_RANGE_TYPE->
            {
                vh.tvRangeTitle.text=mData[position].inputRangeTitle
                vh.tvRangeUnit1.text=mData[position].inputRangeUnit
                vh.etRangeValue1.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                    override fun afterTextChanged(p0: Editable?) {
                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputRangeValue1=p0.toString()
                    }
                })
                vh.etRangeValue2.addTextChangedListener(object :TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                    override fun afterTextChanged(p0: Editable?) {
                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].inputRangeValue2=p0.toString()
                    }
                })
                if (mData[position].inputRangeValue1!="")
                {
                    vh.etRangeValue1.setText(mData[position].inputRangeValue1)
                }
                if (mData[position].inputRangeValue2!="")
                {
                    vh.etRangeValue2.setText(mData[position].inputRangeValue2)
                }

            }
            INPUT_WITH_TEXTAREA_TYPE->
            {
                vh.tvTextAreaTitle.text=mData[position].textAreaTitle
                vh.etTextAreaContent.addTextChangedListener(object:TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        mData[position].textAreaContent=p0.toString()
                    }
                })
                if (mData[position].textAreaContent!="")
                {
                    vh.etTextAreaContent.setText(mData[position].textAreaContent)
                }

            }
            SHIFT_INPUT_TYPE->
            {
                vh.tvShiftInputTitle.text=mData[position].shiftInputTitle
                vh.tvShiftInputContent.text=mData[position].shiftinputContent
                vh.tvShiftInputShow.setOnClickListener(mData[position].jumpListener)
                if (mData[position].necessary)
                {
                    val context=vh.itemView.context
                    val tvNecessary=TextView(context)
                    tvNecessary.text="*"
                    tvNecessary.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.resources.getDimensionPixelSize(R.dimen.font_tv_hint_15).toFloat())
                    tvNecessary.setTextColor(context.resources.getColor(R.color.red,null))
                    (vh.itemView as ViewGroup).addView(tvNecessary,0)
                }
            }
            SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                //vh.etDialogSelectItem.text=mData[position].selectOption1Items[0]
                mData[position].selectContent=mData[position].selectOption1Items[0]
                vh.tvDialogSelectShow.setOnClickListener{
                    val selectDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,vh.itemView.context,mData[position].selectOption1Items,vh.mHandler).dialog
                    selectDialog.show()
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvDialogSelectShow.setOnClickListener(mData[position].selectListener)
                }
            }
            TWO_OPTIONS_SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                //vh.etDialogSelectItem.text=mData[position].selectOption1Items[0]
                if (mData[position].selectContent!="")
                {
                    vh.etDialogSelectItem.text=mData[position].selectContent
                }
                vh.tvDialogSelectShow.setOnClickListener {
                    val selectDialog=CustomDialog(CustomDialog.Options.TWO_OPTIONS_SELECT_DIALOG,vh.tvDialogSelectTitle.context,vh.mHandler,mData[position].selectOption1Items,mData[position].selectOption2Items).multiDialog
                    selectDialog.show()
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvDialogSelectShow.setOnClickListener(mData[position].selectListener)
                }
            }
            THREE_OPTIONS_SELECT_DIALOG_TYPE->
            {
                vh.tvDialogSelectTitle.text=mData[position].selectTitle
                //vh.etDialogSelectItem.text=mData[position].selectOption1Items[0]
                vh.tvDialogSelectShow.setOnClickListener {
                    val selectDialog=CustomDialog(CustomDialog.Options.THREE_OPTIONS_SELECT_DIALOG,vh.tvDialogSelectTitle.context,vh.mHandler,mData[position].selectOption1Items,mData[position].selectOption2Items,mData[position].selectOption3Items).multiDialog
                    selectDialog.show()
                }
                if (mData[position].selectContent!="")
                {
                    vh.etDialogSelectItem.text=mData[position].selectContent
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvDialogSelectShow.setOnClickListener(mData[position].selectListener)
                }
            }
            HINT_TYPE->
            {
                if (mData[position].customFontColor==null)
                {
                    vh.tvHint.text=mData[position].hintContent
                }
                else
                {
                    val style=SpannableStringBuilder(mData[position].hintContent)
                    style.setSpan(ForegroundColorSpan(Color.parseColor(mData[position].customFontColor!!))
                        ,mData[position].startPosition!!
                        ,mData[position].endPosition!!
                        ,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    vh.tvHint.text=style
                }
            }
            SUBMIT_TYPE->
            {
                vh.btnSubmit.text=mData[position].submitContent
                vh.btnSubmit.setOnClickListener{
                    val map=saveSimpleData()
                    val filesDir=it.context.filesDir.absolutePath
                    //downloadFile(filesDir,"工程量清册.xlsx","工程量清册模板","http://10.1.5.90:8012")
                    startSendMessage(map,"http://10.1.5.90:8012")
                }

            }
            EXPAND_TYPE->
            {
                vh.tvExpandTitle.text=mData[position].expandTitle
                vh.tvExpandContent.text=mData[position].expandContent
                vh.tvExpandButton.setOnClickListener{it->
                    if (expandPosition!=-1)
                    {
                        val valueAnimator=ValueAnimator.ofFloat(90f,0f)
                        valueAnimator.duration=200
                        val positionTemp=expandPosition
                        valueAnimator.addUpdateListener {
                            expandList[positionTemp].tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (expandList[positionTemp].tvExpandButton.rotation==90f)
                            {
                                expandList[positionTemp].tvExpandContent.visibility=View.GONE
                            }
                        }
                        valueAnimator.start()
                    }
                    if (expandList.indexOf(vh)!=expandPosition)
                    {
                        val valueAnimator2=ValueAnimator.ofFloat(0.toFloat(),90.toFloat())
                        valueAnimator2.duration=200
                        valueAnimator2.addUpdateListener {
                            vh.tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (vh.tvExpandButton.rotation==90f)
                            {
                                vh.tvExpandContent.visibility = View.VISIBLE
                            }
                        }
                        valueAnimator2.start()
                        vh.tvExpandButton.rotation = "90".toFloat()
                        expandPosition=expandList.indexOf(vh)
                    }
                    else
                    {
                        expandPosition=-1
                    }
                }
                expandList.add(vh)
            }
            EXPAND_LIST_TYPE->
            {
                vh.tvExpandListTitle.text=mData[position].expandListTitle
                vh.rvExpandListContent.adapter=mData[position].expandListAdapter
                vh.rvExpandListContent.layoutManager=LinearLayoutManager(vh.itemView.context)
                vh.itemView.setOnClickListener{it->
                    if (expandPosition!=-1)
                    {
                        val valueAnimator=ValueAnimator.ofFloat(90f,0f)
                        valueAnimator.duration=200
                        val positionTemp=expandPosition
                        valueAnimator.addUpdateListener {
                            expandList[positionTemp].tvExpandListButton.rotation=it.animatedValue.toString().toFloat()
                            if (expandList[positionTemp].tvExpandListButton.rotation==90f)
                            {
                                expandList[positionTemp].rvExpandListContent.visibility=View.GONE
                            }
                        }
                        valueAnimator.start()
                    }
                    if (expandList.indexOf(vh)!=expandPosition)
                    {
                        val valueAnimator2=ValueAnimator.ofFloat(0.toFloat(),90.toFloat())
                        valueAnimator2.duration=200
                        valueAnimator2.addUpdateListener {
                            vh.tvExpandListButton.rotation=it.animatedValue.toString().toFloat()
                            if (vh.tvExpandListButton.rotation==90f)
                            {
                                vh.rvExpandListContent.visibility = View.VISIBLE
                            }
                        }
                        valueAnimator2.start()
                        vh.tvExpandListButton.rotation = "90".toFloat()
                        expandPosition=expandList.indexOf(vh)
                    }
                    else
                    {
                        expandPosition=-1
                    }
                }
                expandList.add(vh)
            }
            SINGLE_DISPLAY_RIGHT_TYPE->
            {
                vh.tvsingleDisplayRightTitle.text=mData[position].singleDisplayRightTitle
                vh.tvsingleDisplayRightContent.text=mData[position].singleDisplayRightContent
            }
            //业主单位
            TWO_TEXT_DIALOG_TYPE->
            {
                vh.tvTextFirstDialog.text=mData[position].textFirstDialog
                vh.tvTextSecondDialog.text=mData[position].textSecondDialog
                if(mData[position].selectFirstDialogOption1Items[0].equals("其他")){

                    vh.tvTextFirstDialog.isEnabled=true
                    vh.tvTextFirstDialog.hint="请选择入"
                    vh.tvTextFirstDialog.text=""
                    vh.tvTextFirstDialog.visibility=View.GONE
                }
                vh.tvTextFirstDialog.setOnClickListener{
                    val selectDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,vh.itemView.context,mData[position].selectFirstDialogOption1Items,vh.mHandler1).dialog
                    selectDialog.show()
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvTextFirstDialog.setOnClickListener(mData[position].selectListener)
                }

                if(mData[position].selectSecondDialogOption1Items[0].equals("其他")){

                    vh.tvTextSecondDialog.isEnabled=true
                    vh.tvTextSecondDialog.hint="请选择入"
                    vh.tvTextSecondDialog.text=""
                    vh.tvTextSecondDialog.visibility=View.GONE
                }
                vh.tvTextSecondDialog.setOnClickListener{
                    val selectDialog=CustomDialog(CustomDialog.Options.SELECT_DIALOG,vh.itemView.context,mData[position].selectSecondDialogOption1Items,vh.mHandler2).dialog
                    selectDialog.show()
                }
                if(mData[position].selectListener!=null)
                {
                    vh.tvTextSecondDialog.setOnClickListener(mData[position].selectListener)
                }
            }
            EXPAND_RIGHT_TYPE->
            {
                vh.tvExpandTitle.text=mData[position].expandTitle
                vh.tvExpandContent.text=mData[position].expandContent
                vh.itemView.setOnClickListener{it->
                    if (expandPosition!=-1)
                    {
                        val valueAnimator=ValueAnimator.ofFloat(90f,0f)
                        valueAnimator.duration=200
                        val positionTemp=expandPosition
                        valueAnimator.addUpdateListener {
                            expandList[positionTemp].tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (expandList[positionTemp].tvExpandButton.rotation==90f)
                            {
                                expandList[positionTemp].tvExpandContent.visibility=View.GONE
                            }
                        }
                        valueAnimator.start()
                    }
                    if (expandList.indexOf(vh)!=expandPosition)
                    {
                        val valueAnimator2=ValueAnimator.ofFloat(0.toFloat(),90.toFloat())
                        valueAnimator2.duration=200
                        valueAnimator2.addUpdateListener {
                            vh.tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (vh.tvExpandButton.rotation==90f)
                            {
                                vh.tvExpandContent.visibility = View.VISIBLE
                            }
                        }
                        valueAnimator2.start()
                        vh.tvExpandButton.rotation = "90".toFloat()
                        expandPosition=expandList.indexOf(vh)
                    }
                    else
                    {
                        expandPosition=-1
                    }
                }
                expandList.add(vh)
            }
            // alterPosition time:2019/7/15
            // function:公共点定位
            POSITION_ADD_TYPE->
            {
                //vh.tvTitle1.setText(mData[position].getTitle1())
                vh.tvPositionTitle1.text=mData[position].positionTitle
            }
            // alterPosition time:2019/7/15
            // function:公共点定位
            POSITION_DELETE_TYPE->
            {
                vh.tvPositionInputTitle.text=mData[position].inputPositionTitle
                vh.etPositionInputContent.hint = "湖南省娄底市湖南人文科技学院"
            }
            // alterPosition time:2019/7/15
            // function:公共点定位
            POSITION_START_TYPE->
            {
                vh.tvPositionInputTitle1.text=mData[position].inputPositionDeleteTitle1
                vh.tvPositionInputTitle2.text=mData[position].inputPositionDeleteTitle2
                vh.etPositionInputContent1.hint = "湖南省娄底市湖南人文科技学院"
            }
            // alterPosition time:2019/7/15
            // function:公共点定位
            POSITION_END_TYPE->
            {
                vh.tvPositionInputTitle3.text=mData[position].inputPositionDeleteTitle3
                vh.etPositionInputContent2.hint = "湖南省娄底市湖南人文科技学院"
            }
            // alterPosition time:2019/7/15
            // function: //新建项目盘
            TITLE_INPUT_BG_TYPE->
            {
                vh.tvSingleInputTitleWithBg.text=mData[position].inputSingleTitleWithBg
            }
            // alterPosition time:2019/7/15
            // function://新建项目盘
            TITLE_INPUT_BG_SELECT_TYPE->
            {
                vh.tvSingleInputTitleWithBgSelect.text=mData[position].inputSingleTitleWithBgSelect
            }
            // alterPosition time:2019/7/15
            // function: //扩展+选择
            TEXT_EXPAND_SELECT_TYPE->
            {
                vh.tvExpandTitle.text=mData[position].expandTitle
                vh.tvExpandContent.text=mData[position].expandContent
                vh.tvExpandTitle.setOnClickListener(mData[position].tvExpandTitleListener)
                vh.tvExpandMore.setOnClickListener(mData[position].tvExpandMoreListener)
                vh.tvExpandButton.setOnClickListener{it->
                    if (expandPosition!=-1)
                    {
                        val valueAnimator=ValueAnimator.ofFloat(90f,0f)
                        valueAnimator.duration=200
                        val positionTemp=expandPosition
                        valueAnimator.addUpdateListener {
                            expandList[positionTemp].tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (expandList[positionTemp].tvExpandButton.rotation==90f)
                            {
                                expandList[positionTemp].tvExpandContent.visibility=View.GONE
                            }
                        }
                        valueAnimator.start()
                    }
                    if (expandList.indexOf(vh)!=expandPosition)
                    {
                        val valueAnimator2=ValueAnimator.ofFloat(0.toFloat(),90.toFloat())
                        valueAnimator2.duration=200
                        valueAnimator2.addUpdateListener {
                            vh.tvExpandButton.rotation=it.animatedValue.toString().toFloat()
                            if (vh.tvExpandButton.rotation==90f)
                            {
                                vh.tvExpandContent.visibility = View.VISIBLE
                            }
                        }
                        valueAnimator2.start()
                        vh.tvExpandButton.rotation = "90".toFloat()
                        expandPosition=expandList.indexOf(vh)
                    }
                    else
                    {
                        expandPosition=-1
                    }
                }
                expandList.add(vh)
            }
            // function:节点自查自检图片显示
            NODE_CHECK_IMAGE_SHOW_TYPE->
            {
                vh.tvContentDetail.text=mData[position].title_image_show
                vh.tvCheckMore.setOnClickListener(mData[position].checkMoreListener)
            }
            // function:搜索
            SEARCH_TYPE->
            {
                vh.etSearchContent.hint = "搜索"
            }
            BLANK_TYPE->{

            }
        }
        mData[position].viewHolderInitialize=true
        //VHList.add(vh)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VH {
        var root:View?=null
        when(viewType)
        {
            TITLE_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_title,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_BUTTON_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_RADIO_BUTTON_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_CHECKBOX_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            MULTI_HYBRID_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_multi,viewGroup,false)
                return VH(root,viewType)
            }
            SINGLE_INPUT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_single_input,viewGroup,false)
                return VH(root,viewType)
            }
            TWO_PAIR_INPUT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_two_pair_input,viewGroup,false)
                return VH(root,viewType)
            }

            FOUR_DISPLAY_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_four_display,viewGroup,false)
                return VH(root,viewType)
            }
            FIVE_DISPLAY_TYPE->{
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_five_display,viewGroup,false)
                return VH(root,viewType)
            }
            TWO_COLUMN_DISPLAY_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_two_column_display,viewGroup,false)
                return VH(root,viewType)
            }
            EXPAND_LIST_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_expand_list,viewGroup,false)
                return VH(root, viewType)
            }
            SINGLE_DISPLAY_LEFT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_single_display_left,viewGroup,false)
                return VH(root, viewType)
            }
            TWO_DISPLAY_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_two_display,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_WITH_UNIT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_with_unit,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_WITH_MULTI_UNIT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_with_multi_unit,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_RANGE_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_range,viewGroup,false)
                return VH(root, viewType)
            }
            INPUT_WITH_TEXTAREA_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_input_with_textarea,viewGroup,false)
                return VH(root, viewType)
            }
            SHIFT_INPUT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_shift_input,viewGroup,false)
                return VH(root, viewType)
            }
            SELECT_DIALOG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dialog_select,viewGroup,false)
                return VH(root, viewType)
            }
            TWO_OPTIONS_SELECT_DIALOG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dialog_select,viewGroup,false)
                return VH(root, viewType)
            }
            THREE_OPTIONS_SELECT_DIALOG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dialog_select,viewGroup,false)
                return VH(root, viewType)
            }
            HINT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_hint,viewGroup,false)
                return VH(root, viewType)
            }
            SUBMIT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_confirm,viewGroup,false)
                return VH(root, viewType)
            }
            EXPAND_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_expand,viewGroup,false)
                return VH(root, viewType)
            }
            SINGLE_DISPLAY_RIGHT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_single_display_right,viewGroup,false)
                return VH(root, viewType)
            }
            TWO_TEXT_DIALOG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_two_dialog,viewGroup,false)
                return VH(root, viewType)
            }
            EXPAND_RIGHT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_expand_right,viewGroup,false)
                return VH(root, viewType)
            }
            // alterPosition time:2019/7/15
            // function: //公共点定位
            POSITION_ADD_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_public_point_position1,viewGroup,false)
                return VH(root,viewType)
            }
            POSITION_DELETE_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_public_point_position2,viewGroup,false)
                return VH(root,viewType)
            }
            POSITION_START_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_public_point_position3,viewGroup,false)
                return VH(root,viewType)
            }
            POSITION_END_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_public_point_position4,viewGroup,false)
                return VH(root,viewType)
            }
            // alterPosition time:2019/7/15
            // function: //新建项目盘1
            TITLE_INPUT_BG_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_new_project_disk1,viewGroup,false)
                return VH(root,viewType)
            }
            // alterPosition time:2019/7/15
            // function: //新建项目盘2
            TITLE_INPUT_BG_SELECT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_new_project_disk2,viewGroup,false)
                return VH(root,viewType)
            }
            // alterPosition time:2019/7/15
            // function:扩展+选择
            TEXT_EXPAND_SELECT_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_text_expand_select,viewGroup,false)
                return VH(root, viewType)
            }
            // function: //自查自检标题
            NODE_CHECK_IMAGE_SHOW_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_image_check,viewGroup,false)
                return VH(root,viewType)
            }
            // function:搜索
            SEARCH_TYPE->
            {
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_search,viewGroup,false)
                return VH(root,viewType)
            }
            BLANK_TYPE->{
                root=LayoutInflater.from(viewGroup.context).inflate(R.layout.item_blank,viewGroup,false)
                return VH(root, viewType)
            }
        }
        return VH(root!!,TITLE_TYPE)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun saveSimpleData():HashMap<String,String>
    {
        val map=HashMap<String,String>(mData.size)
        for (i in 0 until mData.size-1)
        {
            val multiStyleItem=mData[i]
            val viewHolder=VHList[i]
            if (multiStyleItem.key=="")
                continue
            when(viewHolder.itemViewType)
            {
                TITLE_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.title1!="")
                    {
                        multiStyleItem.title1
                    }
                    else
                    {
                        ""
                    }
                }
                SINGLE_INPUT_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.inputSingleContent!="")
                    {
                        multiStyleItem.inputSingleContent
                    }
                    else
                        ""
                }
                INPUT_WITH_UNIT_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.inputUnitContent!="")
                        multiStyleItem.inputUnitContent
                    else
                        ""
                }
                INPUT_WITH_MULTI_UNIT_TYPE->
                {
                    map[multiStyleItem.key]=if (multiStyleItem.inputMultiContent!="")
                        multiStyleItem.inputMultiContent
                    else
                        ""
                }
                INPUT_RANGE_TYPE->
                {
                    val keys=multiStyleItem.key.split(" ")

                    map[keys[0]]=if (multiStyleItem.inputRangeValue1!="")
                        multiStyleItem.inputRangeValue1
                    else
                        ""

                    map[keys[1]]=if (multiStyleItem.inputRangeValue2!="")
                        multiStyleItem.inputRangeValue2
                    else
                        ""
                }
                INPUT_WITH_TEXTAREA_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.textAreaContent!="")
                        multiStyleItem.textAreaContent
                    else
                        ""
                }
                TWO_PAIR_INPUT_TYPE->
                {
                    val keys=multiStyleItem.key.split(" ")

                    map[keys[0]]=if (multiStyleItem.twoPairInputValue1!="")
                        multiStyleItem.twoPairInputValue1
                    else
                        ""
                    map[keys[1]]=if (multiStyleItem.twoPairInputValue2!="")
                        multiStyleItem.twoPairInputValue2
                    else
                        ""
                }
                SHIFT_INPUT_TYPE->
                {

                }
                SELECT_DIALOG_TYPE, TWO_OPTIONS_SELECT_DIALOG_TYPE, THREE_OPTIONS_SELECT_DIALOG_TYPE->
                {

                    map[multiStyleItem.key]=if (multiStyleItem.selectContent!="")
                        multiStyleItem.selectContent
                    else
                        ""
                }
                MULTI_RADIO_BUTTON_TYPE->
                {
                    map[multiStyleItem.key]=if (multiStyleItem.radioButtonValue!="")
                    {
                        multiStyleItem.radioButtonValue
                    }
                    else
                        ""
                }
                MULTI_CHECKBOX_TYPE->
                {
                    var checkNum=0
                    for (j in 0 until multiStyleItem.checkboxValueList.size-1)
                    {
                        if (multiStyleItem.checkboxValueList[j])
                        {
                            checkNum++
                            if (checkNum>=2)
                            {
                                map[multiStyleItem.key]="-1"
                            }
                            else
                            {
                                map[multiStyleItem.key]=(1-j).toString()
                            }
                        }
                    }
                }
            }//end when
        }//end for
        return map
    }
}