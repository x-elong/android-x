package com.example.eletronicengineer.fragment.shoppingmall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.adapter.GoodsAdapter
import com.example.eletronicengineer.aninterface.Goods
import com.example.eletronicengineer.utils.getPopularityGoods
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_shop_list.view.*

class RecommendGoodsFragment :Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_list, container, false)
        initFragment(view)
        return view
    }
    private fun initFragment(view: View) {
        val result = getPopularityGoods("http://192.168.1.133:8022/").subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    val goodsList:MutableList<Goods> = ArrayList()
                    var recommendGoodsList = it.message
                    for ( j in recommendGoodsList){
                        val item = Goods(j.coverPicture,j.goodsName,j.price)
                        goodsList.add(item)
                    }
                    view.rv_shop_list.adapter = GoodsAdapter(goodsList)
                    view.rv_shop_list.layoutManager = GridLayoutManager(context,2)
                },{
                    it.printStackTrace()
                }
            )
    }
}