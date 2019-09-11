package com.example.eletronicengineer.fragment

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo

import com.example.eletronicengineer.adapter.FriendRequestAdapter
import com.example.eletronicengineer.model.FriendRequestItem
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.layout_search_user.*
import kotlinx.android.synthetic.main.layout_search_user.et_search_user_content
import kotlinx.android.synthetic.main.layout_search_user.rv_search_user_content
import kotlinx.android.synthetic.main.layout_search_user.tv_search_user_back
import kotlinx.android.synthetic.main.layout_search_user.view.*

class AddFriendSearchUserFragment:Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root=inflater.inflate(R.layout.layout_search_user,container,false)
        root.run {
            rv_search_user_content.adapter= FriendRequestAdapter(ArrayList())
            rv_search_user_content.layoutManager= LinearLayoutManager(context)
            tv_search_user_back.setOnClickListener {
                fragmentManager?.popBackStackImmediate()
            }
            et_search_user_content.addTextChangedListener(object: TextWatcher
            {
                override fun afterTextChanged(s: Editable?)
                {
                    val content=s.toString()
                    JMessageClient.getUserInfo(content,object: GetUserInfoCallback()
                    {
                        override fun gotResult(p0: Int, p1: String?, p2: UserInfo?)
                        {
                            val adapter=(rv_search_user_content.adapter as FriendRequestAdapter)
                            if (p2!=null)
                            {
                                val source=adapter.mData.toMutableList()
                                val avatar=if (p2.avatarFile!=null)
                                {
                                    BitmapDrawable(context?.resources, BitmapFactory.decodeFile(p2.avatarFile.absolutePath))
                                }
                                else
                                    ContextCompat.getDrawable(context!!,R.drawable.user_avatar_xml)!!
                                val userItem= FriendRequestItem(p2.userName,avatar)
                                source.add(userItem)
                                adapter.mData=source
                                adapter.notifyDataSetChanged()
                            }
                            else
                            {
                                if (adapter.mData.isNotEmpty())
                                {
                                    adapter.mData=ArrayList()
                                    adapter.notifyDataSetChanged()
                                }
                            }
                        }
                    })
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
            })
        }
        return root
    }
}