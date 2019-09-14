package com.example.eletronicengineer.utils

import android.util.Log
import com.example.eletronicengineer.db.*
import com.example.eletronicengineer.db.AerialFoundationCasting.*
import com.example.eletronicengineer.db.AerialFoundationExcavation.*
import com.example.eletronicengineer.db.AerialGroundConnectionLay.*
import com.example.eletronicengineer.db.CableBridge.CableBridgeEntity
import com.example.eletronicengineer.db.CableBridge.CableBridgesEntity
import com.example.eletronicengineer.db.CableFireroofing.CableFireroofingEntity
import com.example.eletronicengineer.db.CableFireroofing.CableFireroofingMaterialsTypesEntity
import com.example.eletronicengineer.db.CableHeadMake.CableHeadMakeEntity
import com.example.eletronicengineer.db.CableHeadMake.CableHeadMakesEntity
import com.example.eletronicengineer.db.CableLaying.CableLayingEntity
import com.example.eletronicengineer.db.CableLaying.CableLayingsEntity
import com.example.eletronicengineer.db.CableTest.CableTestEntity
import com.example.eletronicengineer.db.CableTest.CableTestMaterialsTypesEntity
import com.example.eletronicengineer.db.ListCablePipe.ListCablePipeEntity
import com.example.eletronicengineer.db.ListCablePipe.ListCablePipesEntity
import com.example.eletronicengineer.db.NodeBasisExcavation.BasisExcavationEntity
import com.example.eletronicengineer.db.NodeBasisExcavation.BreakingRoadsEntity
import com.example.eletronicengineer.db.NodeBasisExcavation.EarthRockExcavationEntity
import com.example.eletronicengineer.db.NodeBasisExcavation.RoadCuttingsEntity
import com.example.eletronicengineer.db.NodeBasisPouring.*
import com.example.eletronicengineer.db.NodeGroundingInstallation.*
import com.example.eletronicengineer.db.NodeHouseholdTableInstallation.HouseholdTableInstallationEntity
import com.example.eletronicengineer.db.NodeHouseholdTableInstallation.MeterMaterialsEntity
import com.example.eletronicengineer.db.NodeHouseholdTableInstallation.MeterIndexRegistersEntity
import com.example.eletronicengineer.db.NodeMaterial.MaterialEntity
import com.example.eletronicengineer.db.NodeMaterial.MaterialSubmitEntity
import com.example.eletronicengineer.db.NodePreformedUnitMakeInstallation.InstallVolumesEntity
import com.example.eletronicengineer.db.NodePreformedUnitMakeInstallation.MakeVolumesEntity
import com.example.eletronicengineer.db.NodePreformedUnitMakeInstallation.PreformedUnitMakeInstallationEntity
import com.example.eletronicengineer.db.Shopping.GoodsEntity
import com.example.eletronicengineer.distributionFileSave.*
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.HttpResult
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

interface HttpHelper {
    //需求查看界面
    //需求个人
    @GET(Constants.HttpUrlPath.DisplayForRequirementAndSupply.RequirementPerson)
    fun getReqiurementPerson():Observable<HttpResult<Requirement<ReqiurementPersonDetail>>>
    //需求个人通过id查找详情
    @GET(Constants.HttpUrlPath.DisplayForRequirementAndSupply.RequirementPersonDetail)
    fun getReqiurementPersonDetail(@Path("id")id:String,@Header("zouxiaodong")zouxiaodong:String):Observable<HttpResult<ReqiurementPersonDetail>>
//    //需求团队 主网
//    @GET(Constants.HttpUrlPath.DisplayForRequirementAndSupply.RequirementMajorNetwork)
//    fun getRequirementMajorNetWork(@Path("page")page:Long,@Header("zouxiaodong")zouxiaodong: String):Observable<Requirement<reqiurementMajorNetwork<String>>>
//    //需求团队 变电
//    @GET(Constants.HttpUrlPath.DisplayForRequirementAndSupply.RequirementPowerTransformation)
//    fun getRequirementPowerTransformation(@Path("page")page:Long,@Path("number")number:Long,@Header("zouxiaodong")zouxiaodong: String):Observable<HttpResult<ReqiurementPersonDetail>>
    //需求团队 配网

    //需求团队 马帮运输

    //需求团队 桩机服务

    //需求团队 非开挖顶管拉管作业

    //需求团队 跨越架

    //需求团队 运行维护

    //需求团队

    //需求租赁

    //需求三方








    @GET(Constants.HttpUrlPath.Professional.MajorDistribuionProject)
    fun getProjects(@Path("id") id: Long, @Path("page") page: Int): Observable<HttpResult<PageEntity<MajorDistribuionProjectEntity>>>

    @GET(Constants.HttpUrlPath.Professional.PowerTowerParameters)
    fun getPowerTowerParameters(@Path("majorDistribuionProjectId") majorDistributionProjectId: Long, @Path("pageNumber") pageNumber: Int): Observable<HttpResult<PageEntity<PowerTowerParametersEntity>>>

    @GET(Constants.HttpUrlPath.Professional.MaterialsTransport)
    fun getMaterialsTransport(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<AerialMaterialsTransportEntity<AerialMaterialSubitemEntity>>>>

    @GET(Constants.HttpUrlPath.Professional.PreformedUnitLandfill)
    fun getPreformedUnitLandFill(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<PreformedUnitLandFillEntity>>>

    @GET(Constants.HttpUrlPath.Professional.GalleryParameter)
    fun getGalleryParameter(@Path("id") id: Long, @Path("page") page: Int): Observable<HttpResult<GalleryEntity<GalleryParameterEntity<GalleryNodeEntity>>>>// data:

    @GET(Constants.HttpUrlPath.Professional.NodeParameter)
    fun getNodeParameter(@Path("id") id: Long, @Path("page") page: Int): Observable<HttpResult<NodeEntity<NodeParameterEntity<NodeNodeEntity>>>>

    @GET(Constants.HttpUrlPath.Professional.FacilityInstall)
    fun getALLAerialFacilityInstall(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<FacilityInstallEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialsStayguyMake)
    fun getAerialsStayguyMakeList(@Path("id") towerSubitemId: String): Observable<HttpResult<List<AerialsStayguyMakeEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialElectrificationJob)
    fun getAerialElectrificationJob(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<AerialElectrificationJobEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialEnclosureInstall)
    fun getAerialEnclosureInstall(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<AerialEnclosureInstallEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialPoleArmInstall)
    fun getAerialPoleArmInstall(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<AerialPoleArmInstallEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialInsulatorInstall)
    fun getAerialInsulatorInstall(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<AerialInsulatorInstallEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialHouseholdInstall)
    fun getAerialHouseholdInstall(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<AerialHouseholdInstallEntity<AerialHouseholdInstallCommentEntity, AerialHouseholdInstallRegistrationEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialFoundationExcavation)
    fun getAerialFoundationExcavation(@Path("towerId") towerId: String): Observable<HttpResult
    <AerialFoundationExcavationsEntity<
            AerialExcavationRoadCutsEntity,
            AerialExplodeRoadsEntity,
            AerialPolePitExcavationSubitemEntity,
            AerialStayguyHoleExcavationSubitemEntity,
            AerialTowerPitExcavationSubitemEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialWeldMake)
    fun getAerialWeldMake(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<List<AerialWeldMakeEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialPoleTowerAssemblage)
    fun getAerialPoleTowerAssemblage(@Path("id") id: String): Observable<HttpResult<List<AerialPoleTowerAssemblageEntity>>>

    @GET(Constants.HttpUrlPath.Professional.OverHeadAerialEconomy)
    fun getOverHeadEconomy(@Path("aerialId") aerialId: String): Observable<HttpResult<OverHeadAerialEconomyEntity<AerialTwentyKvTypesEntity, AerialThirtyfiveKvTypesEntity>>>

    @GET(Constants.HttpUrlPath.Professional.NodeEconomy)
    fun getNodeEconomy(@Path("cableId") cableId: String): Observable<HttpResult<NodeEconomyEntity>>

    @GET(Constants.HttpUrlPath.Professional.GalleryEconomy)
    fun getGalleryEconomy(@Path("cableId") cableId: String): Observable<HttpResult<GalleryEconomyEntity>>

    @GET(Constants.HttpUrlPath.Professional.AerialFoundationCasting)
    fun getAerialFoundationCasting(@Path("towerSubitemId") towerSubitemId: String): Observable<HttpResult<AerialFoundationCastingEntity<AerialCastingFirstsEntity, AerialCastingSecondEntity, AerialCastingFoundationFencesEntity>>>

    @GET(Constants.HttpUrlPath.Professional.AerialGroundConnectionLay)
    fun getAerialGroundConnectionLay(@Path("aerialId") aerialId: String): Observable<HttpResult<AerialGroundConnectionLayEntity<
            AerialGroundAczoilingsEntity,
            AerialGroundBodyInstallsEntity,
            AerialGroundDownInstallsEntity,
            AerialGroundEarthPolesEntity,
            AerialGroundGeneratrixLaysEntity,
            AerialGroundDitchExcavationEntity>>>

    @GET(Constants.HttpUrlPath.Professional.NodeGroundingInstallation)
    fun getNodeGroundingInstallation(@Path("nodeSubitemId") nodeSubitemId: String): Observable<HttpResult<GroundingInstallationEntity<
            GroundingElectrodeMakeInstallsEntity,
            GroundingBusLayingsEntity,
            UnderInstallationsEntity,
            TrenchExcavationEntity,
            GroundingAnticorrosionsEntity,
            GroundingInstallsEntity>>>

    @GET(Constants.HttpUrlPath.Professional.ListFacility)
    fun getListFacility(@Path("nodeSubitemId") nodeSubitemId: String): Observable<HttpResult<List<NodeListFacilitysEntity>>>

    @GET(Constants.HttpUrlPath.Professional.NodeHouseholdTableInstallation)
    fun getNodeHouseholdTableInstallation(@Path("nodeSubitemId") nodeSubitemId: String): Observable<HttpResult<HouseholdTableInstallationEntity<MeterMaterialsEntity, MeterIndexRegistersEntity>>>

    @GET(Constants.HttpUrlPath.Professional.NodePreformedUnitMakeInstallation)
    fun getNodePreformedUnitMakeInstallation(@Path("id") id: String): Observable<HttpResult<PreformedUnitMakeInstallationEntity<InstallVolumesEntity, MakeVolumesEntity>>>

    @GET(Constants.HttpUrlPath.Professional.NodeBasisPouring)
    fun getNodeBasisPouring(@Path("nodeSubitemId") nodeSubitemId: String): Observable<HttpResult<BasisPouring<
            BrickSettingsEntity,
            ConcretePouringsEntity,
            CurbsEntity,
            LoadMoldEntity,
            PlasterersEntity,
            PrimaryLevelRecoversEntity,
            SurfaceRecoversEntity>>>

    @GET(Constants.HttpUrlPath.Professional.NodeBasisExcavation)
    fun getNodeBasisExcavation(@Path("nodeSubitemId") nodeSubitemId: String): Observable<HttpResult<BasisExcavationEntity<RoadCuttingsEntity, BreakingRoadsEntity, EarthRockExcavationEntity>>>

    @GET(Constants.HttpUrlPath.Professional.NodeMaterial)
    fun getNodeMaterial(@Path("nodeSubitemId") nodeSubitemId: String): Observable<HttpResult<MaterialEntity<MaterialSubmitEntity>>>

    @GET(Constants.HttpUrlPath.Professional.CableHeadMake)
    fun getCableHeadMake(@Path("nodeSubitemId") nodeSubitemId: String): Observable<HttpResult<CableHeadMakeEntity<CableHeadMakesEntity>>>

    @GET(Constants.HttpUrlPath.Professional.CableFireroofing)
    fun getCableFireroofing(@Path("nodeSubitemId")nodeSubitemId:String):Observable<HttpResult<CableFireroofingEntity<CableFireroofingMaterialsTypesEntity>>>

    @GET(Constants.HttpUrlPath.Professional.CableBridge)
    fun getCableBridge(@Path("nodeSubitemId")nodeSubitemId:String):Observable<HttpResult<CableBridgeEntity<CableBridgesEntity>>>

    @GET(Constants.HttpUrlPath.Professional.CableLaying)
    fun getCableLaying(@Path("nodeSubitemId")nodeSubitemId:String):Observable<HttpResult<CableLayingEntity<CableLayingsEntity>>>

    @GET(Constants.HttpUrlPath.Professional.ListCablePipe)
    fun getListCablePipe(@Path("nodeSubitemId")nodeSubitemId:String):Observable<HttpResult<ListCablePipeEntity<ListCablePipesEntity>>>

    @GET(Constants.HttpUrlPath.Professional.CableTest)
    fun getCableTest(@Path("nodeSubitemId")nodeSubitemId:String):Observable<HttpResult<CableTestEntity<CableTestMaterialsTypesEntity>>>

    @DELETE(Constants.HttpUrlPath.Professional.deleteMajorDistribuionProject)
    fun deleteMajorDistribuionProject(@Path("id") id: Long): Observable<ResponseBody>

    @POST(Constants.HttpUrlPath.Requirement.excel)
    fun downloadExcel(@Path("fileName") filename: String): Call<ResponseBody>

    @POST("api/upload/url")
    @Multipart
    fun uploadImage(@Part data: MultipartBody.Part): Observable<ResponseBody>

    @POST(".")
    fun sendSimpleMessage(@Body data: RequestBody): Observable<ResponseBody>

    @Multipart
    @POST(".")
    fun SendMultiPartMessage(@PartMap data: Map<String, @JvmSuppressWildcards RequestBody>): Observable<ResponseBody>

    @Multipart
    @POST(".")
    fun uploadFile(@PartMap data: Map<String, @JvmSuppressWildcards RequestBody>): Observable<ResponseBody>

    @PUT(".")
    fun putSimpleMessage(@Body data: RequestBody): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.Shopping.PopularityGoods)
    fun getPopularityGoods():Observable<HttpResult<List<GoodsEntity>>>

    @GET(Constants.HttpUrlPath.Shopping.NewGoods)
    fun getNewGoods():Observable<HttpResult<List<GoodsEntity>>>

    @GET(Constants.HttpUrlPath.Shopping.GeneralizeGoods)
    fun getGeneralizeGoods():Observable<HttpResult<List<GoodsEntity>>>

    //分销
    @GET(Constants.HttpUrlPath.Distribution.QRCode)
    fun getQRCode():Observable<HttpResult<String>>

    @GET(Constants.HttpUrlPath.Distribution.OwnIntegral)
    fun getOwnIntegral():Observable<HttpResult<Double>>

    @GET(Constants.HttpUrlPath.Distribution.UserIncome)
    fun getUserIncome(@Path("type")type:String):Observable<HttpResult<String>>

    @GET(Constants.HttpUrlPath.Distribution.OwnIntegralOfRebate)
    fun getOwnIntegralOfRebate(@Path("date")date:String):Observable<HttpResult<OwnIntegralOfRebate<OwnIntegralOfRebateDetail>>>

    @GET(Constants.HttpUrlPath.Distribution.OwnExtendUAndI)
    fun getOwnExtendUAndI(@Path("vipLevel")vipLevel: Int,@Path("year")year:Int):Observable<HttpResult<OwnExtendUAndI>>

    @GET(Constants.HttpUrlPath.Distribution.OwnExtendUser)
    fun getOwnExtendUser(@Path("vipLevel")vipLevel:Int):Observable<HttpResult<List<OwnExtendUserDetails>>>

}

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        //获取并处理request，得到response
        val response = chain.proceed(chain.request())
        val modified = response.newBuilder().addHeader("Content-Type", "application/octet-stream;charset=utf-8").build()
        return modified
    }
}

internal fun getProjects(
    id: Long,
    page: Int,
    baseUrl: String
): Observable<HttpResult<PageEntity<MajorDistribuionProjectEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getProjects(id, page)
}

internal fun getCableLaying(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableLayingEntity<CableLayingsEntity>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableLaying(nodeSubitemId)
}

internal fun getListCablePipe(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<ListCablePipeEntity<ListCablePipesEntity>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getListCablePipe(nodeSubitemId)
}

internal fun getPowerTowerParameters(
    majorDistributionProjectId: Long,
    pageNumber: Int,
    baseUrl: String
): Observable<HttpResult<PageEntity<PowerTowerParametersEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getPowerTowerParameters(majorDistributionProjectId, pageNumber)
}

internal fun getGalleryParameter(
    id: Long,
    page: Int,
    baseUrl: String
): Observable<HttpResult<GalleryEntity<GalleryParameterEntity<GalleryNodeEntity>>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getGalleryParameter(id, page)
}

internal fun getNodeParameter(
    id: Long,
    page: Int,
    baseUrl: String
): Observable<HttpResult<NodeEntity<NodeParameterEntity<NodeNodeEntity>>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeParameter(id, page)
}

internal fun getMaterialsTransport(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialMaterialsTransportEntity<AerialMaterialSubitemEntity>>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getMaterialsTransport(towerSubitemId)
}

internal fun getPreformedUnitLandFill(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<PreformedUnitLandFillEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getPreformedUnitLandFill(towerSubitemId)
}

internal fun getALLAerialFacilityInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<FacilityInstallEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getALLAerialFacilityInstall(towerSubitemId)
}

internal fun getAerialsStayguyMakeList(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialsStayguyMakeEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialsStayguyMakeList(towerSubitemId)
}

internal fun getAerialEnclosureInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialEnclosureInstallEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialEnclosureInstall(towerSubitemId)
}

internal fun getAerialInsulatorInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialInsulatorInstallEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialInsulatorInstall(towerSubitemId)
}

internal fun getAerialPoleArmInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialPoleArmInstallEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialPoleArmInstall(towerSubitemId)
}

internal fun getAerialElectrificationJob(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialElectrificationJobEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialElectrificationJob(towerSubitemId)
}

internal fun getAerialHouseholdInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<AerialHouseholdInstallEntity<AerialHouseholdInstallCommentEntity, AerialHouseholdInstallRegistrationEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialHouseholdInstall(towerSubitemId)
}

internal fun getAerialFoundationExcavation(
    towerId: String,
    baseUrl: String
): Observable<HttpResult<AerialFoundationExcavationsEntity<
        AerialExcavationRoadCutsEntity,
        AerialExplodeRoadsEntity,
        AerialPolePitExcavationSubitemEntity,
        AerialStayguyHoleExcavationSubitemEntity,
        AerialTowerPitExcavationSubitemEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialFoundationExcavation(towerId)
}

internal fun getAerialWeldMake(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialWeldMakeEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialWeldMake(towerSubitemId)
}

internal fun getAerialPoleTowerAssemblage(
    id: String,
    baseUrl: String
): Observable<HttpResult<List<AerialPoleTowerAssemblageEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialPoleTowerAssemblage(id)
}

internal fun getOverHeadEconomy(
    aerialId: String,
    baseUrl: String
): Observable<HttpResult<OverHeadAerialEconomyEntity<AerialTwentyKvTypesEntity, AerialThirtyfiveKvTypesEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOverHeadEconomy(aerialId)
}

internal fun getNodeEconomy(cableId: String, baseUrl: String): Observable<HttpResult<NodeEconomyEntity>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeEconomy(cableId)
}

internal fun getGalleryEconomy(cableId: String, baseUrl: String): Observable<HttpResult<GalleryEconomyEntity>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getGalleryEconomy(cableId)
}

internal fun getAerialFoundationCasting(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<AerialFoundationCastingEntity<AerialCastingFirstsEntity, AerialCastingSecondEntity, AerialCastingFoundationFencesEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialFoundationCasting(towerSubitemId)
}

internal fun getAerialGroundConnectionLay(
    aerialId: String,
    baseUrl: String
): Observable<HttpResult<AerialGroundConnectionLayEntity<
        AerialGroundAczoilingsEntity,
        AerialGroundBodyInstallsEntity,
        AerialGroundDownInstallsEntity,
        AerialGroundEarthPolesEntity,
        AerialGroundGeneratrixLaysEntity,
        AerialGroundDitchExcavationEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialGroundConnectionLay(aerialId)
}

internal fun getNodeGroundingInstallation(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<GroundingInstallationEntity<
        GroundingElectrodeMakeInstallsEntity,
        GroundingBusLayingsEntity,
        UnderInstallationsEntity,
        TrenchExcavationEntity,
        GroundingAnticorrosionsEntity,
        GroundingInstallsEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeGroundingInstallation(nodeSubitemId)
}

internal fun getListFacility(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<NodeListFacilitysEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getListFacility(nodeSubitemId)
}

internal fun getNodeHouseholdTableInstallation(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<HouseholdTableInstallationEntity<MeterMaterialsEntity, MeterIndexRegistersEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeHouseholdTableInstallation(nodeSubitemId)
}

internal fun getNodePreformedUnitMakeInstallation(
    id: String,
    baseUrl: String
): Observable<HttpResult<PreformedUnitMakeInstallationEntity<InstallVolumesEntity, MakeVolumesEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodePreformedUnitMakeInstallation(id)
}

internal fun getNodeBasisPouring(nodeSubitemId: String, baseUrl: String): Observable<HttpResult<BasisPouring<
        BrickSettingsEntity,
        ConcretePouringsEntity,
        CurbsEntity,
        LoadMoldEntity,
        PlasterersEntity,
        PrimaryLevelRecoversEntity,
        SurfaceRecoversEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeBasisPouring(nodeSubitemId)
}

internal fun getNodeBasisExcavation(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<BasisExcavationEntity<RoadCuttingsEntity, BreakingRoadsEntity, EarthRockExcavationEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeBasisExcavation(nodeSubitemId)
}

internal fun getNodeMaterial(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<MaterialEntity<MaterialSubmitEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeMaterial(nodeSubitemId)
}

internal fun getCableHeadMake(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<CableHeadMakeEntity<CableHeadMakesEntity>>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableHeadMake(nodeSubitemId)
}

internal fun getCableFireroofing(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableFireroofingEntity<CableFireroofingMaterialsTypesEntity>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableFireroofing(nodeSubitemId)
}

internal fun getCableBridge(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableBridgeEntity<CableBridgesEntity>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableBridge(nodeSubitemId)
}

internal fun getCableTest(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableTestEntity<CableTestMaterialsTypesEntity>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableTest(nodeSubitemId)
}

//分销
internal fun getQRCode(baseUrl: String):Observable<HttpResult<String>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getQRCode()
}

internal fun getOwnIntegral(baseUrl: String):Observable<HttpResult<Double>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOwnIntegral()
}
/*
* 分销
*/
internal fun getUserIncome(type: String,baseUrl: String):Observable<HttpResult<String>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getUserIncome(type)
}

internal fun getOwnIntegralOfRebate(date:String,baseUrl: String):Observable<HttpResult<OwnIntegralOfRebate<OwnIntegralOfRebateDetail>>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOwnIntegralOfRebate(date)
}

internal fun getOwnExtendUser(vipLevel: Int,baseUrl: String):Observable<HttpResult<List<OwnExtendUserDetails>>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOwnExtendUser(vipLevel)
}
internal fun getOwnExtendUAndI(vipLevel: Int, year: Int, baseUrl: String):Observable<HttpResult<OwnExtendUAndI>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOwnExtendUAndI(vipLevel,year)
}

/*
 * 商城
 */
internal fun getPopularityGoods(baseUrl: String):Observable<HttpResult<List<GoodsEntity>>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getPopularityGoods()
}

internal fun getNewGoods(baseUrl: String):Observable<HttpResult<List<GoodsEntity>>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNewGoods()
}

internal fun getGeneralizeGoods(baseUrl: String):Observable<HttpResult<List<GoodsEntity>>>{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getGeneralizeGoods()
}

internal fun deleteMajorDistribuionProject(id: Long, baseUrl: String): Observable<ResponseBody> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteMajorDistribuionProject(id)
}

internal fun startSendMessage(keys: Array<String>, values: Array<String>, baseUrl: String) {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    var jsonObject = JSONObject()
    for (i in 0 until keys.size) {
        val result = values[i].toIntOrNull()
        if (result == null) {
            jsonObject.put(keys[i], values[i])
        } else {
            jsonObject.put(keys[i], result)
        }
    }
    val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
    val result = httpHelper.sendSimpleMessage(requestBody).subscribe(
        {
            Log.i("responseBody", it.toString())
        },
        {
            it.printStackTrace()
        }
    )
}

internal fun startSendMultiPartMessage(map: Map<String, RequestBody>, baseUrl: String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =
        Retrofit.Builder().baseUrl(baseUrl).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.SendMultiPartMessage(map)
}

internal fun startSendMessage(requestBody: RequestBody, baseUrl: String):Observable<ResponseBody> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
        return httpHelper.sendSimpleMessage(requestBody)
}

internal fun uploadImage(data: MultipartBody.Part): Observable<ResponseBody> {
    val retrofit =
        Retrofit.Builder().baseUrl("http://shiptux.cn:8088/").addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.uploadImage(data).subscribeOn(Schedulers.newThread())
}

internal fun downloadFile(targetPath: String, targetFileName: String, webFileName: String, baseUrl: String) {
    val okhttp = OkHttpClient.Builder().addInterceptor(ResponseInterceptor()).build()
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).client(okhttp).build()
    val file = File(targetPath, targetFileName)
    val httpHelper = retrofit.create(HttpHelper::class.java)
    httpHelper.downloadExcel(webFileName).enqueue(object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            t.printStackTrace()
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val inputStream = response.body()!!.byteStream()
            var buf = ByteArray(2048)
            var fos: FileOutputStream? = null
            try {
                val totalLength = response.body()!!.contentLength()
                var current = 0
                var len: Int
                fos = FileOutputStream(file)
                while (inputStream.read(buf).let { len = it;len != -1 }) {
                    fos.write(buf, 0, len)
                    current += len
                }
                fos.write(buf, 0, len)
                Log.i("size", current.toString())
                fos.flush()
            } catch (ioExp: IOException) {
                ioExp.printStackTrace()
            } finally {
                inputStream.close()
                if (fos != null)
                    fos.close()
            }
        }
    })
}

internal fun putSimpleMessage(requestBody: RequestBody, baseUrl: String): Observable<ResponseBody> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
        return httpHelper.putSimpleMessage(requestBody)
}

internal fun uploadFile(map: Map<String, RequestBody>, baseUrl: String):Observable<ResponseBody>
{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =
        Retrofit.Builder().baseUrl(baseUrl).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.SendMultiPartMessage(map)
}

internal fun getRequirementPerson(baseUrl: String): Observable<HttpResult<Requirement<ReqiurementPersonDetail>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getReqiurementPerson()
}
internal fun getRequirementPersonDetail(id: String,zouxiaodong: String,baseUrl: String):Observable<HttpResult<ReqiurementPersonDetail>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getReqiurementPersonDetail(id,zouxiaodong)
}