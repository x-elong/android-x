package com.example.eletronicengineer.fragment.shopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.electric.engineering.model.MultiStyleItem
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.ShippingAddressActivity
import com.example.eletronicengineer.adapter.NetworkAdapter
import com.example.eletronicengineer.adapter.RecyclerviewAdapter
import com.example.eletronicengineer.fragment.my.ShippingAddressFragment
import kotlinx.android.synthetic.main.fragment_address.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class AddressFragment : Fragment() {

    companion object{
        fun newInstance(args: Bundle): AddressFragment
        {
            val fragment= AddressFragment ()
            fragment.arguments=args
            return fragment
        }
    }
    lateinit var title:String
    var position = -1

    var addressList: MutableList<MultiStyleItem> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_address, container, false)
        title=arguments!!.getString("title")
        position = arguments!!.getInt("position")
        Log.i("position",position.toString())
        view.tv_new_address_title.text=title
        if(addressList.size==0)
        initData()
        initFragment(view)
        return view
    }
    fun initData(){
        var item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT, "收货人")
        addressList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT, "手机号码")
        addressList.add(item)
        val selectOption1Items:MutableList<String> =ArrayList()
        val selectOption2Items:MutableList<MutableList<String>> =ArrayList()
        val selectOption3Items:MutableList<MutableList<MutableList<String>>> =ArrayList()
        val resultBuilder= StringBuilder()
        val bf= BufferedReader(InputStreamReader(context!!.assets.open("pca.json")))
        try {
            var line=bf.readLine()
            while (line!=null)
            {
                resultBuilder.append(line)
                line=bf.readLine()
            }
        }
        catch (io: IOException)
        {
            io.printStackTrace()
        }
        val json= JSONObject(resultBuilder.toString())
        val keys:Iterator<String> = json.keys()
        while (keys.hasNext()){
            val key = keys.next()
            selectOption1Items.add(key)
            val js = json.getJSONObject(key)
            val jskeys = js.keys()
            selectOption2Items.add(ArrayList())
            selectOption3Items.add(ArrayList())
            while (jskeys.hasNext()){
                val jskey = jskeys.next()
                selectOption2Items[selectOption2Items.size-1].add(jskey)
                selectOption3Items[selectOption3Items.size-1].add(ArrayList())
                var valueArray = js.getJSONArray(jskey)
                for (j in 0 until valueArray.length()){
                    selectOption3Items[selectOption3Items.size-1][selectOption3Items[selectOption3Items.size-1].size-1].add(valueArray[j].toString())
                }
            }
        }
        item = MultiStyleItem(MultiStyleItem.Options.THREE_OPTIONS_SELECT_DIALOG,selectOption1Items,selectOption2Items,selectOption3Items, "收货地址")
        addressList.add(item)
        item = MultiStyleItem(MultiStyleItem.Options.SINGLE_INPUT, "详细地址")
        addressList.add(item)
    }
    private fun initFragment(v: View) {
        v.tv_new_address_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        v.tv_address_ok.setOnClickListener {
            if (NetworkAdapter(addressList, context!!).check()) {
                val fragment = if (activity is ShippingAddressActivity) {
                    activity!!.supportFragmentManager.findFragmentByTag("my")
                } else {
                    activity!!.supportFragmentManager.findFragmentByTag("search")
                }
                (fragment as ShippingAddressFragment).modify(
                    position,
                    addressList[0].inputSingleContent,
                    addressList[1].inputSingleContent,
                    "${addressList[2].selectContent}|${addressList[3].inputSingleContent}"
                )
                activity!!.supportFragmentManager.popBackStackImmediate()
            }
        }
        val recyclerView = v.rv_address_content
        recyclerView.layoutManager =  LinearLayoutManager(context)
        recyclerView.adapter = RecyclerviewAdapter(addressList)
    }
    fun getData(data:MutableList<MultiStyleItem>){
        this.addressList = data
    }
}