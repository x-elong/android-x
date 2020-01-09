package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eletronicengineer.R
import kotlinx.android.synthetic.main.fragment_help_center_more.view.*

class HelpCenterMoreFragment :Fragment(){
    companion object{
        fun newInstance(args:Bundle):HelpCenterMoreFragment{
            val helpCenterMoreFragment = HelpCenterMoreFragment()
            helpCenterMoreFragment.arguments = args
            return helpCenterMoreFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_help_center_more,container,false)
        mView.tv_help_center_more_back.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        when(arguments!!.getString("title")){
            "如何找到电企通"->{
                mView.tv_help_center_content.text = "\t\t\t\t搜索引擎输入 www.ycdlfw.com 或直接搜索“电企通.com”即可在找到电企通首页，手机端按推广码直接下载或在应用市场直接搜索“电企通”即可下载APP。"
            }
            "如何注册与登录"->{
                mView.tv_help_center_content.text = "\t\t\t\t电企通电脑端和手机端首页都有注册界面，用户输入本人手机号码，获取短信验证码，设置6位以上密码并重复确认密码，点击注册即可注册成功。\n" +
                        "\t\t\t\t注册成功后页面会自动跳转到登录页面，输入密码即可完成登录。\n" +
                        "\t\t\t\t电企通账号就是本人的电话号码，软件内有绑定QQ号码功能，用户在后期输入本人电话号码或QQ号码都可以完成登录。"
            }
            "如何个人认证"->{
                mView.tv_help_center_content.text = "\t\t\t\t电企通新用户登录电企通后，点击右上角圆形个人信息按钮或手机端的个人中心，左边菜单栏选择个人认证，进入个人认证，填写您个人姓名 /住址/身份证号码，上传身份证正反面申请认证。\n" +
                        "\t\t\t\t“电企通”是电力行业软件，您所登记的信息和您以后很多权限和权益等息息相关，所以请您务必填写您的真实有效信息。"
            }
            "如何公司认证"->{
                mView.tv_help_center_content.text = "\t\t\t\t电企通用户可为个人注册，也可为公司注册，但先必须是个人注册在先。如有需要是公司注册账号，先个人注册并认证完成后，方可发起公司认证，点击右上角圆形个人信息按钮，从左边菜单栏中选择公司认证，在下拉框中选择主体类型，按提示填写企业名称、主营产品、统一社会信用代码等信息，同时上传营业执照、法人身份证正反面影印件。\n" +
                        "\t\t\t\t电企通公司注册必须由其公司管理人员完成，系统有确认管理员身份的公函模板，您在系统内下载公函并按要求填写内容信息（含签字盖章）后上传系统完成公司认证。"
            }
            "邮箱绑定"->{
                mView.tv_help_center_content.text = "\t\t\t\t进入个人中心，在左侧菜单栏选择账号绑定，选择邮箱绑定，绑定成功后，方便您可用绑定邮箱号登录或找回密码。"
            }
            "忘记密码"->{
                mView.tv_help_center_content.text = "\t\t\t\t忘记密码可以通过两种方式找回账号：\n" +
                        "\t\t\t\t1、输入注册手机号码，通过手机获取验证码找回；\n" +
                        "\t\t\t\t2、输入绑定邮箱号码，通过邮箱获取验证码找回。"
            }
            "如何开通VIP会员"->{
                mView.tv_help_center_content.text = "\t\t\t\t电企通第一期分普通会员、精英会员、黄金会员三个级别。您可在个人中心选择付费或推荐好友注册电企通账号直接提高您的会员等级，级别越高，权限越多。"
            }
            "报名查看"->{
                mView.tv_help_center_content.text = "\t\t\t\t进入个人中心，在左侧菜单栏点击我的报名，根据您报名的栏目，可查看我已经报名的项目。"
            }
            "付费帮助"->{
                mView.tv_help_center_content.text = "\t\t\t\t1、购买流程与须知\n" +
                        "\t\t\t\t在购买前，请确认登录的是您电企通账号，检查是否已通过认证，如果没有请先通过个人认证，您才能付费购买。\n" +
                        "\t\t\t\t2、支付方式\n" +
                        "\t\t\t\t目前电企通平台支持微信和支付宝支付"
            }
        }
        return mView
    }
}