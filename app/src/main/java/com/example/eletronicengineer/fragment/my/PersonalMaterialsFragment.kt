package com.example.eletronicengineer.fragment.my

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eletronicengineer.R
import com.example.eletronicengineer.activity.MyInformationActivity
import com.example.eletronicengineer.adapter.ImageAdapter
import com.example.eletronicengineer.aninterface.Image
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.utils.*
import com.example.eletronicengineer.utils.getUser
import com.example.eletronicengineer.utils.startSendMessage
import com.example.eletronicengineer.utils.uploadImage
import com.google.gson.Gson
import com.lcw.library.imagepicker.ImagePicker
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_personal_materials.view.*
import kotlinx.android.synthetic.main.personal_materials.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class PersonalMaterialsFragment :Fragment(){
    lateinit var educationAdapter: ImageAdapter
    lateinit var diplomaAdapter: ImageAdapter
    lateinit var employeePhotoAdapter: ImageAdapter
    lateinit var professionalCertificateAdapter: ImageAdapter
    lateinit var otherCertificateAdapter: ImageAdapter
    lateinit var mView: View
    var selectImage=-1
    val names = arrayListOf("","学历证书","学位证书","员工照片","专业证书","其他证书")
    lateinit var adapterList:List<ImageAdapter>
    val glideLoader = GlideLoader()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_personal_materials,container,false)
        initFragment()
        return mView
    }

    private fun initFragment() {
        mView.view_personal_materials.setOnClickListener {
            activity!!.supportFragmentManager.popBackStackImmediate()
        }
        val imageList1:MutableList<Image> = ArrayList()
        imageList1.add(Image("",View.OnClickListener {
            selectImage = 1
            ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(glideLoader)//设置自定义图片加载器
                .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }))
        imageList1[0].isX = false
        educationAdapter = ImageAdapter(imageList1)
        mView.rv_education_content.adapter = educationAdapter
        mView.rv_education_content.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)

        val imageList2:MutableList<Image> = ArrayList()
        imageList2.add(Image("",View.OnClickListener {
            selectImage = 2
            ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(glideLoader)//设置自定义图片加载器
                .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }))
        imageList2[0].isX = false
        diplomaAdapter = ImageAdapter(imageList2)
        mView.rv_diploma_content.adapter = diplomaAdapter
        mView.rv_diploma_content.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)

        val imageList3:MutableList<Image> = ArrayList()
        imageList3.add(Image("",View.OnClickListener {
            selectImage = 3
            ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(glideLoader)//设置自定义图片加载器
                .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }))
        imageList3[0].isX = false
        employeePhotoAdapter = ImageAdapter(imageList3)
        mView.rv_employee_photo_content.adapter = employeePhotoAdapter
        mView.rv_employee_photo_content.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)

        val imageList4:MutableList<Image> = ArrayList()
        imageList4.add(Image("",View.OnClickListener {
            selectImage = 4
            ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(glideLoader)//设置自定义图片加载器
                .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }))
        imageList4[0].isX = false
        professionalCertificateAdapter = ImageAdapter(imageList4)
        mView.rv_professional_certificate_content.adapter = professionalCertificateAdapter
        mView.rv_professional_certificate_content.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)

        val imageList5:MutableList<Image> = ArrayList()
        imageList5.add(Image("",View.OnClickListener {
            selectImage = 5
            ImagePicker.getInstance()
                .setTitle("图片")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .showVideo(true)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                //.setImagePaths(mImagePaths)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(glideLoader)//设置自定义图片加载器
                .start(activity, Constants.RequestCode.REQUEST_PICK_IMAGE.ordinal)
        }))
        imageList5[0].isX = false
        otherCertificateAdapter = ImageAdapter(imageList5)
        mView.rv_other_certificate_content.adapter = otherCertificateAdapter
        mView.rv_other_certificate_content.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        adapterList = arrayListOf(ImageAdapter(ArrayList()),educationAdapter,diplomaAdapter,employeePhotoAdapter,professionalCertificateAdapter,otherCertificateAdapter)
        val result = getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe( {
                val certificates = it.message.certificates
                if(certificates!=null)
                    for (j in certificates){
                        when(j.name){
                            "身份证照片(双面)"->{
                                val imagePath = j.certificatePath.split("|")
                                glideLoader.loadImage(mView.iv_id_card_people,imagePath[0])
                                glideLoader.loadImage(mView.iv_id_card_nation,imagePath[0])
                            }
                            "学历证书"->{
                                val imageList = adapterList[1].mImageList.toMutableList()
                                    imageList.add(imageList.size-1,Image(j.certificatePath,null))
                                imageList[imageList.size-2].deleteListener = View.OnClickListener {
                                    deleteCertificate(imageList.size-2,1,j.id)
                                }
                                adapterList[1].mImageList = imageList
                                adapterList[1].notifyDataSetChanged()
                            }
                            "学位证书"->{
                                val imagePath = j.certificatePath.split("|")
                                val imageList = adapterList[2].mImageList.toMutableList()
                                for (k in imagePath)
                                    imageList.add(imageList.size-1,Image(k,null))
                                imageList[imageList.size-2].deleteListener = View.OnClickListener {
                                    deleteCertificate(imageList.size-2,2,j.id)
                                }
                                adapterList[2].mImageList = imageList
                                adapterList[2].notifyDataSetChanged()
                            }
                            "员工照片"->{
                                val imagePath = j.certificatePath.split("|")
                                val imageList = adapterList[3].mImageList.toMutableList()
                                for (k in imagePath)
                                    imageList.add(imageList.size-1,Image(k,null))
                                imageList[imageList.size-2].deleteListener = View.OnClickListener {
                                    deleteCertificate(imageList.size-2,3,j.id)
                                }
                                adapterList[3].mImageList = imageList
                                adapterList[3].notifyDataSetChanged()
                            }
                            "专业证书"->{
                                val imagePath = j.certificatePath.split("|")
                                val imageList = adapterList[4].mImageList.toMutableList()
                                for (k in imagePath)
                                    imageList.add(imageList.size-1,Image(k,null))
                                imageList[imageList.size-2].deleteListener = View.OnClickListener {
                                    deleteCertificate(imageList.size-2,4,j.id)
                                }
                                adapterList[4].mImageList = imageList
                                adapterList[4].notifyDataSetChanged()
                            }
                            "其他证书"->{
                                val imagePath = j.certificatePath.split("|")
                                val imageList = adapterList[5].mImageList.toMutableList()
                                for (k in imagePath)
                                    imageList.add(imageList.size-1,Image(k,null))
                                imageList[imageList.size-2].deleteListener = View.OnClickListener {
                                    deleteCertificate(imageList.size-2,5,j.id)
                                }
                                adapterList[5].mImageList = imageList
                                adapterList[5].notifyDataSetChanged()
                            }
                        }
                    }
            },{
                it.printStackTrace()
            })
    }
    fun uploadImg(imagePath: String){
        val results = try {
            val file = File(imagePath)
                Log.i("File path is : ",file.path)
                val imagePart = MultipartBody.Part.createFormData("file",file.name,
                    RequestBody.create(MediaType.parse("image/*"),file))
                uploadImage(imagePart).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {

                            insertCertificate(it.string())
                    },
                    {
                        it.printStackTrace()
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun insertCertificate(imagePath: String){
        val result = Observable.create<RequestBody> {
            val json = JSONObject().put("name",names[selectImage]).put("certificatePath",imagePath)
            val requestBody = RequestBody.create(MediaType.parse("application/json"),json.toString())
            it.onNext(requestBody)
        }.subscribe {
            val result = startSendMessage(it,UnSerializeDataBase.mineBasePath+Constants.HttpUrlPath.My.insertCertificate)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    val json = JSONObject(it.string())
                    if(json.getInt("code")==200){
                        ToastHelper.mToast(mView.context,"添加成功")
                        val id = json.getString("message")
                        val imageList = adapterList[selectImage].mImageList.toMutableList()
                        imageList.add(imageList.size-1,Image(imagePath,null))
                        imageList[imageList.size-2].deleteListener = View.OnClickListener {
                            deleteCertificate(imageList.size-2,selectImage,id)
                        }
                        adapterList[selectImage].mImageList = imageList
                        adapterList[selectImage].notifyDataSetChanged()
                    }else{
                        ToastHelper.mToast(mView.context,"添加失败")
                    }
                },{
                    it.printStackTrace()
                })
        }
    }
    fun deleteCertificate(position:Int,selected:Int,id:String){
            val result = deleteCertificate(id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    val json = JSONObject(it.string())
                    if(json.getInt("code")==200){
                        ToastHelper.mToast(mView.context,"删除成功")
                        val imageList = adapterList[selected].mImageList.toMutableList()
                        imageList.removeAt(position)
                        adapterList[selected].mImageList = imageList
                        adapterList[selected].notifyDataSetChanged()
                    }else{
                        ToastHelper.mToast(mView.context,"删除失败")
                    }
                },{
                    it.printStackTrace()
                })
    }
}