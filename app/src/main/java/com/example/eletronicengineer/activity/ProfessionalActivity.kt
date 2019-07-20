package com.example.eletronicengineer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import com.example.eletronicengineer.fragment.projectdisk.ProjectMoreFragment
import com.example.eletronicengineer.utils.AdapterGenerate

class ProfessionalActivity : AppCompatActivity() {

    lateinit var title:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional)
        supportActionBar?.hide()
        initFragment()
    }

    private fun initFragment() {
        initData()
        val data = Bundle()
        data.putString("title",title)
        data.putInt("type",AdapterGenerate().getType(title))
        addFragment(ProjectMoreFragment.newInstance(data))
    }
    //获取加载的界面类型
    private fun initData() {
        val intent = getIntent()
        title = intent.getStringExtra("title")
    }
    fun addFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_professional,fragment)
        transaction.commit()
    }
    fun switchFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_professional,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
