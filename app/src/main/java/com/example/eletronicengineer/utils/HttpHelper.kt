package com.example.eletronicengineer.utils

import android.util.Log
import com.example.eletronicengineer.DisplayYellowPages.YellowPages
import com.example.eletronicengineer.DisplayYellowPages.YellowPagesDetail
import com.example.eletronicengineer.db.DistributionFileSave.*
import com.example.eletronicengineer.db.DisplayDemand.*
import com.example.eletronicengineer.db.DisplaySupply.*
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
import com.example.eletronicengineer.db.My.*
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
import com.example.eletronicengineer.lg.login
import com.example.eletronicengineer.lg.resources
import com.example.eletronicengineer.model.Constants
import com.example.eletronicengineer.model.HttpResult
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONObject
import retrofit2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

interface HttpHelper {
    //行业黄页获取数量
    @POST(Constants.HttpUrlPath.DisplayForYellowPages.YellowPages)
    fun getYellowPages(@Body data:RequestBody,@Header("zouxiaodong")token:String):Observable<HttpResult<YellowPages<YellowPagesDetail>>>
    //行业黄页通过id查找详情
    @GET(Constants.HttpUrlPath.DisplayForYellowPages.YellowPagesDetail)
    fun getYellowPagesDetail(@Path("id")id:String,@Header("zouxiaodong")token:String):Observable<HttpResult<YellowPagesDetail>>


    //需求个人获取数量
    @POST(Constants.HttpUrlPath.DisplayForRequirement.RequirementPerson)
    fun getRequirementPerson(@Body data:RequestBody,@Header("zouxiaodong")token:String):Observable<HttpResult<Requirement<RequirementPersonDetail>>>
    //需求个人通过id查找详情
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementPersonDetail)
    fun getRequirementPersonDetail(@Path("id")id:String,@Header("zouxiaodong")token:String):Observable<HttpResult<RequirementPersonDetail>>

    //获取需求团队的数量
    @POST(Constants.HttpUrlPath.DisplayForRequirement.ReqiurementTeam)
    fun getRequirementTeam(@Body data:RequestBody,@Header("zouxiaodong")token:String):Observable<HttpResult<Requirement<RequirementTeam>>>
    //需求团队 主网
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementMajorNetwork)
    fun getRequirementMajorNetWork(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementMajorNetWork>>
    //需求团队 变电
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementPowerTransformation)
    fun getRequirementPowerTransformation(@Path("id")page:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementPowerTransformation>>
    //需求团队 配网
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementDistributionNetwork)
    fun getRequirementDistributionNetWork(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementDistributionNetwork>>
    //需求团队 测量设计
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementMeasureDesign)
    fun getRequirementMeasureDesign(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementMeasureDesign>>
    //需求团队 马帮运输
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementCaravanTransport)
    fun getRequirementCaravanTransport(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementCaravanTransport>>
    //需求团队 桩机服务
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementPileFoundation)
    fun getRequirementPileFoundation(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementPileFoundation>>
    //需求团队 非开挖顶管拉管作业
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementUnexcavation)
    fun getRequirementUnexcavation(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementUnexcavation>>
    //需求团队 实验调试
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementTest)
    fun getRequirementTest(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementTest>>
    //需求团队 跨越架
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementSpanWoodenSupprt)
    fun getRequirementSpanWoodenSupprt(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementSpanWoodenSupprt>>
    //需求团队 运行维护
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementRunningMaintain)
    fun getRequirementRunningMaintain(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementRunningMaintain>>

    //获取需求租赁数量
    @POST(Constants.HttpUrlPath.DisplayForRequirement.RequirementLease)
    fun getRequirementLease(@Body data:RequestBody,@Header("zouxiaodong")token: String):Observable<HttpResult<Requirement<RequirementLease>>>
    //车辆租赁
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementLeaseCar)
    fun getRequirementLeaseCar(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementLeaseCar>>
    //工器具租赁
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementLeaseConstructionTool)
    fun getRequirementLeaseConstructionTool(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementLeaseConstructionTool>>
    //设备租赁
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementLeaseFacility)
    fun getRequirementLeaseFacility(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementLeaseFacility>>
    //机械租赁
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementLeaseMachinery)
    fun getRequirementLeaseMachinery(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementLeaseMachinery>>

    //需求三方获取数量
    @POST(Constants.HttpUrlPath.DisplayForRequirement.RequirementThirdParty)
    fun getRequirementThirdParty(@Body data:RequestBody,@Header("zouxiaodong")token: String):Observable<HttpResult<Requirement<RequirementThirdParty>>>
    //需求三方通过id查找
    @GET(Constants.HttpUrlPath.DisplayForRequirement.RequirementThirdPartyDetail)
    fun getRequirementThirdPartyDetail(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<RequirementThirdPartyDetail>>


    //个人劳务
    @POST(Constants.HttpUrlPath.DisplayForSupply.PersonalIssue)
    fun getSupplyPerson(@Body data:RequestBody,@Header("zouxiaodong")token:String):Observable<HttpResult<Supply<SupplyPersonDetail>>>
    //个人劳务查询详情
    @GET(Constants.HttpUrlPath.DisplayForSupply.PersonalIssueDetail)
    fun getSupplyPersonDetail(@Path("id")id:String,@Header("zouxiaodong")token:String):Observable<HttpResult<SupplyPersonDetail>>

    //团队服务
    @POST(Constants.HttpUrlPath.DisplayForSupply.TeamIssue)
    fun getSupplyTeam(@Body data:RequestBody,@Header("zouxiaodong")token:String):Observable<HttpResult<Supply<SupplyTeam>>>
    //主网
    @GET(Constants.HttpUrlPath.DisplayForSupply.MajorNetwork)
    fun getSupplyMajorNetWork(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<Network>>
    //变电
    @GET(Constants.HttpUrlPath.DisplayForSupply.PowerTransformation)
    fun getSupplyPowerTransformation(@Path("id")page:String,@Header("zouxiaodong")token: String):Observable<HttpResult<Network>>
    //配网
    @GET(Constants.HttpUrlPath.DisplayForSupply.DistribuionNetwork)
    fun getSupplyDistributionNetWork(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<Network>>
    //测量设计
    @GET(Constants.HttpUrlPath.DisplayForSupply.MeasureDesign)
    fun getSupplyMeasureDesign(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<Network>>
    //马帮运输
    @GET(Constants.HttpUrlPath.DisplayForSupply.CaravanTransport)
    fun getSupplyCaravanTransport(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<Caravan>>
    //桩机服务
    @GET(Constants.HttpUrlPath.DisplayForSupply.PileFoundation)
    fun getSupplyPileFoundation(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<Pile>>
    //非开挖顶管拉管作业
    @GET(Constants.HttpUrlPath.DisplayForSupply.Unexcavation)
    fun getSupplyUnexcavation(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<SupplyUnexcavation>>
    //实验调试
    @GET(Constants.HttpUrlPath.DisplayForSupply.TestTeam)
    fun getSupplyTest(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<SupplyTest>>
    //需求团队 跨越架
    @GET(Constants.HttpUrlPath.DisplayForSupply.SpanWoodenSupprt)
    fun getSupplySpanWoodenSupprt(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<SupplySpanWoodenSupprt>>
    //需求团队 运行维护
    @GET(Constants.HttpUrlPath.DisplayForSupply.RunningMaintain)
    fun getSupplyRunningMaintain(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<SupplyRunningMaintain>>


    //获取租赁服务数量
    @POST(Constants.HttpUrlPath.DisplayForSupply.LeaseIssue)
    fun getSupplyLease(@Body data:RequestBody,@Header("zouxiaodong")token: String):Observable<HttpResult<Supply<SupplyLease>>>
    //车辆租赁
    @GET(Constants.HttpUrlPath.DisplayForSupply.LeaseCar)
    fun getSupplyLeaseCar(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<SupplyLeaseCar>>
    //工器具租赁
    @GET(Constants.HttpUrlPath.DisplayForSupply.LeaseConstructionTool)
    fun getSupplyLeaseConstructionTool(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<OtherLease>>
    //设备租赁
    @GET(Constants.HttpUrlPath.DisplayForSupply.LeaseFacility)
    fun getSupplyLeaseFacility(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<OtherLease>>
    //机械租赁
    @GET(Constants.HttpUrlPath.DisplayForSupply.LeaseMachinery)
    fun getSupplyLeaseMachinery(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<OtherLease>>

    //需求三方获取数量
    @POST(Constants.HttpUrlPath.DisplayForSupply.ThirdIssue)
    fun getSupplyThirdParty(@Body data:RequestBody,@Header("zouxiaodong")token: String):Observable<HttpResult<Supply<SupplyThirdParty>>>
    //需求三方通过id查找
    @GET(Constants.HttpUrlPath.DisplayForSupply.ThirdIssueDetail)
    fun getSupplyThirdPartyDetail(@Path("id")id:String,@Header("zouxiaodong")token: String):Observable<HttpResult<SupplyThirdParty>>



    /**
     * 我的
     */

    @GET(Constants.HttpUrlPath.My.openPowerNotify)
    fun openPowerNotify(@Path("transactionOrderId") transactionOrderId: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.creatOrder)
    fun creatOrder(@Path("productId") productId: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.payNotify)
    fun payNotify(@Path("orderNumber") orderNumber: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getAliPayOrderStr)
    fun getAliPayOrderStr(@Path("productId") productId: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getRemoteCode)
    fun getRemoteCode(): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.numPayCreatOrder)
    fun numPayCreatOrder(@Path("productId") productId: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getRequirementPersonMore)
    fun getRequirementPersonMore(@Path("id") id: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getPersonalIssueMore)
    fun getPersonalIssueMore(@Path("id") id: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getUser)
    fun getUser():Observable<HttpResult<UserEntity>>

    @GET(Constants.HttpUrlPath.My.getUserOpenPower)
    fun getUserOpenPower():Observable<HttpResult<OpenPowerEntity>>

    @GET(Constants.HttpUrlPath.My.getHomeChildren)
    fun getHomeChildren():Observable<HttpResult<List<HomeChildrensEntity>>>

    @DELETE(Constants.HttpUrlPath.My.delectChildren)
    fun deleteChildren(@Path("id") id: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getEducationBackground)
    fun getEducationBackground():Observable<HttpResult<List<EducationBackgroundsEntity>>>

    @DELETE(Constants.HttpUrlPath.My.deleteEducationBackground)
    fun deleteEducationBackground(@Path("id") id: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getUrgentPeople)
    fun getUrgentPeople():Observable<HttpResult<List<UrgentPeoplesEntity>>>

    @DELETE(Constants.HttpUrlPath.My.deleteUrgentPeople)
    fun deleteUrgentPeople(@Path("id") id: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getBankCard)
    fun getBankCard():Observable<HttpResult<List<BankCardsEntity>>>

    @DELETE(Constants.HttpUrlPath.My.deleteBankCard)
    fun deleteBankCard(@Path("id") id: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getCertificate)
    fun getCertificate():Observable<HttpResult<List<CertificatesEntity>>>

    @DELETE(Constants.HttpUrlPath.My.deleteCertificate)
    fun deleteCertificate(@Path("id") id: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.getALLOrganizationCertificationDTOList)
    fun getALLOrganizationCertificationDTOList():Observable<ResponseBody>


    /**
     * @登录
     */
    @GET(Constants.HttpUrlPath.Login.sendCode)
    fun sendMobile(@Path("mobileNumber") mobileNumber: String): Observable<HttpResult<String>>

    @GET(Constants.HttpUrlPath.Login.validFindCode)
    fun validFindCode(@Path("mobileNumber") mobileNumber: String,@Path("code") code: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.sendEmailCode)
    fun sendEmailCode(@Path("receiver") receiver: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.retrievePasswordSendMailCode)
    fun retrievePasswordSendMailCode(@Path("receiver") receiver: String): Observable<HttpResult<String>>

    @GET(Constants.HttpUrlPath.My.retrievePasswordValidMailCode)
    fun retrievePasswordValidMailCode(@Path("receiver") receiver: String,@Path("mailCode") mailCode: String): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.bindEmail)
    fun validBindEmail(@Path("receiver") receiver: String,@Path("mailCode") mailCode: String): Observable<ResponseBody>


    @POST(Constants.HttpUrlPath.Login.sendRegister)
    fun sendRegister(@Body data:RequestBody): Observable<HttpResult<String>>

    @POST(Constants.HttpUrlPath.Login.sendLogin)
    fun sendLogin(@Body data:RequestBody): Observable<HttpResult<login<resources<Int>>>>
    //分销
/*
 @GET(Constants.HttpUrlPath.Distribution.QRCode)
    fun getQRCode(@Field("username") username:String):Observable<HttpResult<String>>
 */
    @GET(Constants.HttpUrlPath.Distribution.QRCode)
    fun getQRCode():Observable<HttpResult<String>>

    @GET(Constants.HttpUrlPath.Distribution.OwnIntegral)
    fun getOwnIntegral():Observable<HttpResult<Double>>

    @GET(Constants.HttpUrlPath.Distribution.UserIncome)
    fun getUserIncome(@Path("type")type:String):Observable<HttpResult<String>>

    @GET(Constants.HttpUrlPath.Distribution.OwnIntegralOfRebate)
    fun getOwnIntegralOfRebate(@Path("date")date:String):Observable<HttpResult<OwnIntegralOfRebate>>

    @GET(Constants.HttpUrlPath.Distribution.OwnExtendUAndI)
    fun getOwnExtendUAndI(@Path("vipLevel")vipLevel: Int,@Path("year")year:Int):Observable<HttpResult<OwnExtendUAndI>>

    @GET(Constants.HttpUrlPath.Distribution.OwnExtendUser)
    fun getOwnExtendUser(@Path("vipLevel")vipLevel:Int):Observable<HttpResult<List<OwnExtendUserDetails>>>

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

    @POST("file/uploadImageSample")
    @Multipart
    fun uploadImage(@Part data: MultipartBody.Part): Observable<ResponseBody>

    @POST(".")
    fun sendSimpleMessage(@Body data: RequestBody): Observable<ResponseBody>

    @POST(Constants.HttpUrlPath.Login.findPasswordByPhone)
    @FormUrlEncoded
    fun findPasswordByPhone(@Field("mobileNumber") emailNumber:String,@Field("newPassword") newPassword:String,@Field("reNewPassword") reNewPassword:String): Observable<ResponseBody>


    @POST(Constants.HttpUrlPath.Login.findPasswordByEmail)
    @FormUrlEncoded
    fun findPasswordByEmail(@Field("emailNumber") emailNumber:String,@Field("newPassword") newPassword:String,@Field("reNewPassword") reNewPassword:String): Observable<ResponseBody>

    @Multipart
    @POST(".")
    fun SendMultiPartMessage(@PartMap data: Map<String, @JvmSuppressWildcards RequestBody>): Observable<ResponseBody>

    @Multipart
    @POST("file/uploadFileSample")
    fun uploadFile(@PartMap data: Map<String, @JvmSuppressWildcards RequestBody>): Observable<ResponseBody>

    @PUT(".")
    fun putSimpleMessage(@Body data: RequestBody): Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.Shopping.PopularityGoods)
    fun getPopularityGoods():Observable<HttpResult<List<GoodsEntity>>>

    @GET(Constants.HttpUrlPath.Shopping.NewGoods)
    fun getNewGoods():Observable<HttpResult<List<GoodsEntity>>>

    @GET(Constants.HttpUrlPath.Shopping.GeneralizeGoods)
    fun getGeneralizeGoods():Observable<HttpResult<List<GoodsEntity>>>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementPerson)
    fun deleteRequirementPerson(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementPowerTransformation)
    fun deleteRequirementPowerTransformation(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementMajorNetwork)
    fun deleteRequirementMajorNetwork(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementDistribuionNetwork)
    fun deleteRequirementDistribuionNetwork(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementMeasureDesign)
    fun deleteRequirementMeasureDesign(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementCaravanTransport)
    fun deleteRequirementCaravanTransport(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementPileFoundation)
    fun deleteRequirementPileFoundation(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementUnexcavation)
    fun deleteRequirementUnexcavation(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementSpanWoodenSupprt)
    fun deleteRequirementSpanWoodenSupprt(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementTest)
    fun deleteRequirementTest(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementRunningMaintain)
    fun deleteRequirementRunningMaintain(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementLeaseCar)
    fun deleteRequirementLeaseCar(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementLeaseConstructionTool)
    fun deleteRequirementLeaseConstructionTool(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementLeaseFacility)
    fun deleteRequirementLeaseFacility(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementLeaseMachinery)
    fun deleteRequirementLeaseMachinery(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Requirement.deleteRequirementThirdParty)
    fun deleteRequirementThirdParty(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deletePersonalIssue)
    fun deletePersonalIssue(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteMajorNetwork)
    fun deleteMajorNetwork(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deletePowerTransformation)
    fun deletePowerTransformation(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteDistribuionNetwork)
    fun deleteDistribuionNetwork(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteMeasureDesign)
    fun deleteMeasureDesign(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteCaravanTransport)
    fun deleteCaravanTransport(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deletePileFoundation)
    fun deletePileFoundation(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteUnexcavation)
    fun deleteUnexcavation(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteTestTeam)
    fun deleteTestTeam(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteSpanWoodenSupprt)
    fun deleteSpanWoodenSupprt(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteRunningMaintain)
    fun deleteRunningMaintain(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteLeaseCar)
    fun deleteLeaseCar(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteLeaseConstructionTool)
    fun deleteLeaseConstructionTool(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteLeaseFacility)
    fun deleteLeaseFacility(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteLeaseMachinery)
    fun deleteLeaseMachinery(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.Provider.deleteThirdServices)
    fun deleteThirdServices(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.My.deletePersonRequirementInformationCheck)
    fun deletePersonRequirementInformationCheck(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.My.deleteRequirementTeamLoggingCheck)
    fun deleteRequirementTeamLoggingCheck(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.My.deleteLeaseLoggingCheckController)
    fun deleteLeaseLoggingCheckController(@Path("id") id:String):Observable<ResponseBody>

    @DELETE(Constants.HttpUrlPath.My.deleteRequirementThirdLoggingCheck)
    fun deleteRequirementThirdLoggingCheck(@Path("id") id:String):Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.updateCheckDemandIndividual)
    fun updateCheckDemandIndividual(@Path("requirementPersonId") requirementPersonId:String):Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.updateCheckDemandGroup)
    fun updateCheckDemandGroup(@Path("requirementTeamId") requirementTeamId:String):Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.updateCheckDemandLease)
    fun updateCheckDemandLease(@Path("requirementLeaseId") requirementLeaseId:String):Observable<ResponseBody>

    @GET(Constants.HttpUrlPath.My.updateCheckDemandTripartite)
    fun updateCheckDemandTripartite(@Path("requirementThirdId") requirementThirdId:String):Observable<ResponseBody>
}

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        //获取并处理request，得到response
        val response = chain.proceed(chain.request())
        if(response.header("cookie")!=null){
            UnSerializeDataBase.cookie = response.header("cookie").toString()
        }
        val modified = response.newBuilder().addHeader("Content-Type", "application/octet-stream;charset=utf-8").build()
        return modified
    }
}

internal fun getProjects(
    id: Long,
    page: Int,
    baseUrl: String
): Observable<HttpResult<PageEntity<MajorDistribuionProjectEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getProjects(id, page)
}

internal fun getCableLaying(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableLayingEntity<CableLayingsEntity>>>
{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableLaying(nodeSubitemId)
}

internal fun getListCablePipe(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<ListCablePipeEntity<ListCablePipesEntity>>>
{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getListCablePipe(nodeSubitemId)
}

internal fun getPowerTowerParameters(
    majorDistributionProjectId: Long,
    pageNumber: Int,
    baseUrl: String
): Observable<HttpResult<PageEntity<PowerTowerParametersEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getPowerTowerParameters(majorDistributionProjectId, pageNumber)
}

internal fun getGalleryParameter(
    id: Long,
    page: Int,
    baseUrl: String
): Observable<HttpResult<GalleryEntity<GalleryParameterEntity<GalleryNodeEntity>>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getGalleryParameter(id, page)
}

internal fun getNodeParameter(
    id: Long,
    page: Int,
    baseUrl: String
): Observable<HttpResult<NodeEntity<NodeParameterEntity<NodeNodeEntity>>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeParameter(id, page)
}

internal fun getMaterialsTransport(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialMaterialsTransportEntity<AerialMaterialSubitemEntity>>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getMaterialsTransport(towerSubitemId)
}

internal fun getPreformedUnitLandFill(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<PreformedUnitLandFillEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getPreformedUnitLandFill(towerSubitemId)
}

internal fun getALLAerialFacilityInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<FacilityInstallEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getALLAerialFacilityInstall(towerSubitemId)
}

internal fun getAerialsStayguyMakeList(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialsStayguyMakeEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialsStayguyMakeList(towerSubitemId)
}

internal fun getAerialEnclosureInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialEnclosureInstallEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialEnclosureInstall(towerSubitemId)
}

internal fun getAerialInsulatorInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialInsulatorInstallEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialInsulatorInstall(towerSubitemId)
}

internal fun getAerialPoleArmInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialPoleArmInstallEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialPoleArmInstall(towerSubitemId)
}

internal fun getAerialElectrificationJob(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialElectrificationJobEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialElectrificationJob(towerSubitemId)
}

internal fun getAerialHouseholdInstall(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<AerialHouseholdInstallEntity<AerialHouseholdInstallCommentEntity, AerialHouseholdInstallRegistrationEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
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
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialFoundationExcavation(towerId)
}

internal fun getAerialWeldMake(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<AerialWeldMakeEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialWeldMake(towerSubitemId)
}

internal fun getAerialPoleTowerAssemblage(
    id: String,
    baseUrl: String
): Observable<HttpResult<List<AerialPoleTowerAssemblageEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAerialPoleTowerAssemblage(id)
}

internal fun getOverHeadEconomy(
    aerialId: String,
    baseUrl: String
): Observable<HttpResult<OverHeadAerialEconomyEntity<AerialTwentyKvTypesEntity, AerialThirtyfiveKvTypesEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOverHeadEconomy(aerialId)
}

internal fun getNodeEconomy(cableId: String, baseUrl: String): Observable<HttpResult<NodeEconomyEntity>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeEconomy(cableId)
}

internal fun getGalleryEconomy(cableId: String, baseUrl: String): Observable<HttpResult<GalleryEconomyEntity>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getGalleryEconomy(cableId)
}

internal fun getAerialFoundationCasting(
    towerSubitemId: String,
    baseUrl: String
): Observable<HttpResult<AerialFoundationCastingEntity<AerialCastingFirstsEntity, AerialCastingSecondEntity, AerialCastingFoundationFencesEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
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
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
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
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeGroundingInstallation(nodeSubitemId)
}

internal fun getListFacility(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<List<NodeListFacilitysEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getListFacility(nodeSubitemId)
}

internal fun getNodeHouseholdTableInstallation(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<HouseholdTableInstallationEntity<MeterMaterialsEntity, MeterIndexRegistersEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeHouseholdTableInstallation(nodeSubitemId)
}

internal fun getNodePreformedUnitMakeInstallation(
    id: String,
    baseUrl: String
): Observable<HttpResult<PreformedUnitMakeInstallationEntity<InstallVolumesEntity, MakeVolumesEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
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
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeBasisPouring(nodeSubitemId)
}

internal fun getNodeBasisExcavation(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<BasisExcavationEntity<RoadCuttingsEntity, BreakingRoadsEntity, EarthRockExcavationEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeBasisExcavation(nodeSubitemId)
}

internal fun getNodeMaterial(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<MaterialEntity<MaterialSubmitEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getNodeMaterial(nodeSubitemId)
}

internal fun getCableHeadMake(
    nodeSubitemId: String,
    baseUrl: String
): Observable<HttpResult<CableHeadMakeEntity<CableHeadMakesEntity>>> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableHeadMake(nodeSubitemId)
}

internal fun getCableFireroofing(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableFireroofingEntity<CableFireroofingMaterialsTypesEntity>>>
{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableFireroofing(nodeSubitemId)
}

internal fun getCableBridge(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableBridgeEntity<CableBridgesEntity>>>
{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableBridge(nodeSubitemId)
}

internal fun getCableTest(nodeSubitemId:String,baseUrl: String):Observable<HttpResult<CableTestEntity<CableTestMaterialsTypesEntity>>>
{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCableTest(nodeSubitemId)
}

/*
 * 商城
 */
//分销
internal fun getQRCode():Observable<HttpResult<String>>{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getQRCode()
}

internal fun getOwnIntegral(baseUrl: String):Observable<HttpResult<Double>>{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOwnIntegral()
}

internal fun getUserIncome(type: String,baseUrl: String):Observable<HttpResult<String>>{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getUserIncome(type)
}

internal fun getOwnIntegralOfRebate(date:String,baseUrl: String):Observable<HttpResult<OwnIntegralOfRebate>>{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOwnIntegralOfRebate(date)
}

internal fun getOwnExtendUser(vipLevel: Int,baseUrl: String):Observable<HttpResult<List<OwnExtendUserDetails>>>{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getOwnExtendUser(vipLevel)
}
internal fun getOwnExtendUAndI(vipLevel: Int, year: Int, baseUrl: String):Observable<HttpResult<OwnExtendUAndI>>{
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
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
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
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
    val interceptor= Interceptor {
        val request=it.request().newBuilder()
            .addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.sendSimpleMessage(requestBody)
}

internal fun findPasswordByPhone(phoneNumber:String,password:String,rePassword:String):Observable<ResponseBody> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder()
            .addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.findPasswordByPhone(phoneNumber,password,rePassword)
}


internal fun findPasswordByEmail(phoneNumber:String,password:String,rePassword:String):Observable<ResponseBody> {
    val interceptor= Interceptor {
        val request=it.request().newBuilder()
            .addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.upmsBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.findPasswordByEmail(phoneNumber,password,rePassword)
}

internal fun uploadImage(data: MultipartBody.Part): Observable<ResponseBody> {
    val retrofit =
        Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
    val interceptor= Interceptor {
        val request=it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build()
        it.proceed(request)
    }
    val client=OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.putSimpleMessage(requestBody)
}

internal fun uploadFile(map: Map<String, RequestBody>):Observable<ResponseBody>
{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =
        Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.uploadFile(map)
}

internal fun sendMobile(mobileNumber: String): Observable<HttpResult<String>> {
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.upmsBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.sendMobile(mobileNumber)
}

internal fun validFindCode(mobileNumber: String,code:String): Observable<ResponseBody> {
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.upmsBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.validFindCode(mobileNumber,code)
}


internal fun sendEmailCode(receiver: String): Observable<ResponseBody> {
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.sendEmailCode(receiver)
}

internal fun retrievePasswordSendMailCode(receiver: String): Observable<HttpResult<String>> {
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.retrievePasswordSendMailCode(receiver)
}

internal fun retrievePasswordValidMailCode(receiver: String,mailCode:String): Observable<ResponseBody> {
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.retrievePasswordValidMailCode(receiver,mailCode)
}

internal fun validBindEmail(receiver: String,mailCode:String): Observable<ResponseBody> {
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.validBindEmail(receiver,mailCode)
}

internal fun sendRegister(requestBody: RequestBody, baseUrl: String): Observable<HttpResult<String>> {
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.sendRegister(requestBody)
}

internal fun sendLogin(requestBody: RequestBody,baseUrl: String): Observable<HttpResult<login<resources<Int>>>> {

    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        val response=it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
        val cookieList:MutableList<Cookie> =ArrayList()
        for (i in response.headers("Set-Cookie"))
        {
            cookieList.add(Cookie.parse(it.request().url(),i)!!)
        }
        for (i in cookieList)
        {
            if (i.name()=="JSESSIONID")
            {
                UnSerializeDataBase.cookie=i.value()
                Log.i("cookie",i.value())
                break
            }
        }
        response
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(baseUrl).client(client).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.sendLogin(requestBody)
}

internal fun getUser():Observable<HttpResult<UserEntity>>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getUser()
}

internal fun getUserOpenPower():Observable<HttpResult<OpenPowerEntity>>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getUserOpenPower()
}


internal fun getHomeChildren():Observable<HttpResult<List<HomeChildrensEntity>>>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getHomeChildren()
}

internal fun deleteChildren(id: String): Observable<ResponseBody> {
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteChildren(id)
}

internal fun getEducationBackground():Observable<HttpResult<List<EducationBackgroundsEntity>>>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getEducationBackground()
}

internal fun deleteEducationBackground(id: String): Observable<ResponseBody> {
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteEducationBackground(id)
}

internal fun getUrgentPeople():Observable<HttpResult<List<UrgentPeoplesEntity>>>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getUrgentPeople()
}

internal fun deleteUrgentPeople(id:String):Observable<ResponseBody>{
    val client = OkHttpClient.Builder().addInterceptor(ResponseInterceptor()).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteUrgentPeople(id)
}

internal fun getBankCard():Observable<HttpResult<List<BankCardsEntity>>>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getBankCard()
}

internal fun deleteBankCard(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteBankCard(id)
}

internal fun getCertificate():Observable<HttpResult<List<CertificatesEntity>>>{
    val interceptor = Interceptor {
        Log.i("body", it.call().request().body().toString())
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().baseUrl(UnSerializeDataBase.mineBasePath).client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getCertificate()
}

internal fun deleteCertificate(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteCertificate(id)
}

internal fun getALLOrganizationCertificationDTOList():Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getALLOrganizationCertificationDTOList()
}

internal fun getRequirementPersonMore(id: String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementPersonMore(id)
}

internal fun getPersonalIssueMore(id: String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getPersonalIssueMore(id)
}


/**
 * @用户支付后开通权限接口
 */
internal fun openPowerNotify(orderNumber:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.openPowerNotify(orderNumber)
}

/**
 * @微信支付
 */
internal fun creatOrder(productId:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.creatOrder(productId)
}

internal fun payNotify(orderNumber:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.payNotify(orderNumber)
}

/**
 * @支付宝支付
 */
internal fun getAliPayOrderStr(productId:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getAliPayOrderStr(productId)
}

/**
 * @推广二维码
 */
internal fun getRemoteCode():Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRemoteCode()
}

/**
 * @推广人数支付
 */
internal fun numPayCreatOrder(productId:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.mineBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.numPayCreatOrder(productId)
}
/*
      行业黄页
 */

//行页黄页数量
internal fun getYellowPages(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<YellowPages<YellowPagesDetail>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getYellowPages(requestBody,token)
}
//根据id查询行页黄页详情
internal fun getYellowPagesDetail(id: String,token: String,baseUrl: String):Observable<HttpResult<YellowPagesDetail>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper. getYellowPagesDetail( id,token)
}




//供需查看
//查询需求个人数量
internal fun getRequirementPerson(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Requirement<RequirementPersonDetail>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementPerson(requestBody,token)
}
//根据id查询需求个人详情
internal fun getRequirementPersonDetail(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementPersonDetail>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementPersonDetail(id,token)
}

//需求团队
internal fun getRequirementTeam(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Requirement<RequirementTeam>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementTeam(requestBody,token)
}

//需求主网
internal fun getRequirementMajorNetWork(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementMajorNetWork>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementMajorNetWork(id,token)
}
//变电
internal fun getRequirementPowerTransformation(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementPowerTransformation>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementPowerTransformation(id,token)
}
//配网
internal fun getRequirementDistributionNetWork(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementDistributionNetwork>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementDistributionNetWork(id,token)
}
//测量设计
internal fun getRequirementMeasureDesign(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementMeasureDesign>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementMeasureDesign(id,token)
}
//马帮
internal fun getRequirementCaravanTransport(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementCaravanTransport>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementCaravanTransport(id,token)
}
//桩基
internal fun getRequirementPileFoundation(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementPileFoundation>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementPileFoundation(id,token)
}
//非开挖
internal fun getRequirementUnexcavation(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementUnexcavation>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementUnexcavation(id,token)
}
//试验调试
internal fun getRequirementTest(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementTest>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementTest(id,token)
}
//跨越架
internal fun getRequirementSpanWoodenSupprt(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementSpanWoodenSupprt>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementSpanWoodenSupprt(id,token)
}
//运行维护
internal fun getRequirementRunningMaintain(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementRunningMaintain>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementRunningMaintain(id,token)
}

//获取需求租赁数量
internal fun getRequirementLease(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Requirement<RequirementLease>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementLease(requestBody,token)
}
//车辆租赁
internal fun getRequirementLeaseCar(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementLeaseCar>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementLeaseCar(id,token)
}
//工器具租赁
internal fun getRequirementLeaseConstructionTool(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementLeaseConstructionTool>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementLeaseConstructionTool(id,token)
}
//设备租赁
internal fun getRequirementLeaseFacility(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementLeaseFacility>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementLeaseFacility(id,token)
}
//机械租赁
internal fun getRequirementLeaseMachinery(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementLeaseMachinery>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementLeaseMachinery(id,token)
}
//需求三方数量
internal fun getRequirementThirdParty(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Requirement<RequirementThirdParty>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementThirdParty(requestBody,token)
}
//需求三方
internal fun getRequirementThirdPartyDetail(id: String,token: String,baseUrl: String):Observable<HttpResult<RequirementThirdPartyDetail>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getRequirementThirdPartyDetail(id,token)
}





//供应
//个人劳务
internal fun getSupplyPerson(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Supply<SupplyPersonDetail>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyPerson(requestBody,token)
}
//个人劳务详情
internal fun getSupplyPersonDetail(id: String,token: String,baseUrl: String):Observable<HttpResult<SupplyPersonDetail>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyPersonDetail(id,token)
}
//团队服务
internal fun getSupplyTeam(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Supply<SupplyTeam>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyTeam(requestBody,token)
}
//主网
internal fun getSupplyMajorNetWork(id: String,token: String,baseUrl: String):Observable<HttpResult<Network>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyMajorNetWork(id,token)
}
//配网
internal fun getSupplyDistributionNetWork(id: String,token: String,baseUrl: String):Observable<HttpResult<Network>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyDistributionNetWork(id,token)
}
//变电
internal fun getSupplyPowerTransformation(id: String,token: String,baseUrl: String):Observable<HttpResult<Network>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyPowerTransformation(id,token)
}
//测量设计
internal fun getSupplyMeasureDesign(id: String,token: String,baseUrl: String):Observable<HttpResult<Network>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyMeasureDesign(id,token)
}
//马帮
internal fun getSupplyCaravanTransport(id: String,token: String,baseUrl: String):Observable<HttpResult<Caravan>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyCaravanTransport(id,token)
}
//桩基
internal fun getSupplyPileFoundation(id: String,token: String,baseUrl: String):Observable<HttpResult<Pile>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyPileFoundation(id,token)
}
//非开挖
internal fun getSupplyUnexcavation(id: String,token: String,baseUrl: String):Observable<HttpResult<SupplyUnexcavation>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyUnexcavation(id,token)
}
//试验调试
internal fun getSupplyTest(id: String,token: String,baseUrl: String):Observable<HttpResult<SupplyTest>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyTest(id,token)
}
//跨越架
internal fun getSupplySpanWoodenSupprt(id: String,token: String,baseUrl: String):Observable<HttpResult<SupplySpanWoodenSupprt>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplySpanWoodenSupprt(id,token)
}
//运行维护
internal fun getSupplyRunningMaintain(id: String,token: String,baseUrl: String):Observable<HttpResult<SupplyRunningMaintain>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyRunningMaintain(id,token)
}
//租赁服务
internal fun getSupplyLease(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Supply<SupplyLease>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyLease(requestBody,token)
}
//车辆租赁
internal fun getSupplyLeaseCar(id: String,token: String,baseUrl: String):Observable<HttpResult<SupplyLeaseCar>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyLeaseCar(id,token)
}
//工器具租赁
internal fun getSupplyLeaseConstructionTool(id: String,token: String,baseUrl: String):Observable<HttpResult<OtherLease>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyLeaseConstructionTool(id,token)
}
//设备租赁
internal fun getSupplyLeaseFacility(id: String,token: String,baseUrl: String):Observable<HttpResult<OtherLease>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyLeaseFacility(id,token)
}
//机械租赁
internal fun getSupplyLeaseMachinery(id: String,token: String,baseUrl: String):Observable<HttpResult<OtherLease>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyLeaseMachinery(id,token)
}

//三方服务
internal fun getSupplyThirdParty(requestBody: RequestBody,token: String,baseUrl: String): Observable<HttpResult<Supply<SupplyThirdParty>>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyThirdParty(requestBody,token)
}
//三方详情
internal fun getSupplyThirdPartyDetail(id: String,token: String,baseUrl: String):Observable<HttpResult<SupplyThirdParty>>
{
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.getSupplyThirdPartyDetail(id,token)
}
//删除发布 需求个人
internal fun deleteRequirementPerson(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementPerson(id)
}

//删除发布 需求团队 变电
internal fun deletePowerTransformation(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deletePowerTransformation(id)
}

//删除发布 需求团队 主网
internal fun deleteMajorNetwork(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteMajorNetwork(id)
}

//删除发布 需求团队 配网
internal fun deleteDistribuionNetwork(id:String):Observable<ResponseBody> {
    val interceptor = Interceptor {
        it.proceed(
            it.request().newBuilder().addHeader(
                "zouxiaodong",
                UnSerializeDataBase.userToken
            ).build()
        )
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteDistribuionNetwork(id)
}

//删除发布 测量设计
internal fun deleteMeasureDesign(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteMeasureDesign(id)
}

//删除发布 马帮运输
internal fun deleteCaravanTransport(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteCaravanTransport(id)
}

//删除发布 桩基服务
internal fun deletePileFoundation(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deletePileFoundation(id)
}

//删除发布 非开挖
internal fun deleteUnexcavation(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteUnexcavation(id)
}

//删除发布 试验调试
internal fun deleteTestTeam(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteTestTeam(id)
}

//删除发布 跨越架
internal fun deleteSpanWoodenSupprt(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteSpanWoodenSupprt(id)
}

//删除发布 运行维护
internal fun deleteRunningMaintain(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRunningMaintain(id)
}
//删除发布 车辆租赁
internal fun deleteRequirementLeaseCar(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementLeaseCar(id)
}

//删除发布 工器具租赁
internal fun deleteRequirementLeaseConstructionTool(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementLeaseConstructionTool(id)
}

//删除发布 设备租赁
internal fun deleteRequirementLeaseFacility(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementLeaseFacility(id)
}

//删除发布 机械租赁
internal fun deleteRequirementLeaseMachinery(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementLeaseMachinery(id)
}

//删除发布 需求三方
internal fun deleteRequirementThirdParty(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementThirdParty(id)
}

/**
 * @删除供应发布
 */

//删除发布 供应个人
internal fun deletePersonalIssue(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deletePersonalIssue(id)
}

//删除发布 团队服务 变电
internal fun deleteRequirementPowerTransformation(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementPowerTransformation(id)
}

//删除发布 团队服务 主网
internal fun deleteRequirementMajorNetwork(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementMajorNetwork(id)
}

//删除发布 团队服务 配网
internal fun deleteRequirementDistribuionNetwork(id:String):Observable<ResponseBody> {
    val interceptor = Interceptor {
        it.proceed(
            it.request().newBuilder().addHeader(
                "zouxiaodong",
                UnSerializeDataBase.userToken
            ).build()
        )
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementDistribuionNetwork(id)
}

//删除发布 测量设计
internal fun deleteRequirementMeasureDesign(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementMeasureDesign(id)
}

//删除发布 马帮运输
internal fun deleteRequirementCaravanTransport(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementCaravanTransport(id)
}

//删除发布 桩基服务
internal fun deleteRequirementPileFoundation(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementPileFoundation(id)
}

//删除发布 非开挖
internal fun deleteRequirementUnexcavation(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementUnexcavation(id)
}

//删除发布 试验调试
internal fun deleteRequirementTest(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementTest(id)
}

//删除发布 跨越架
internal fun deleteRequirementSpanWoodenSupprt(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementSpanWoodenSupprt(id)
}

//删除发布 运行维护
internal fun deleteRequirementRunningMaintain(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementRunningMaintain(id)
}

//删除发布 供应车辆租赁
internal fun deleteLeaseCar(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteLeaseCar(id)
}

//删除发布 供应设备
internal fun deleteLeaseFacility(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteLeaseFacility(id)
}

//删除发布 供应工器具
internal fun deleteLeaseConstructionTool(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteLeaseConstructionTool(id)
}

//删除发布 供应机械
internal fun deleteLeaseMachinery(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteLeaseMachinery(id)
}

//删除发布 供应三方
internal fun deleteThirdServices(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteThirdServices(id)
}
/**
 * @删除我的报名信息
 */
//个人
internal fun deletePersonRequirementInformationCheck(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deletePersonRequirementInformationCheck(id)
}

//团队
internal fun deleteRequirementTeamLoggingCheck(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementTeamLoggingCheck(id)
}

//租赁
internal fun deleteLeaseLoggingCheckController(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteLeaseLoggingCheckController(id)
}

//三方
internal fun deleteRequirementThirdLoggingCheck(id:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit =  Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.deleteRequirementThirdLoggingCheck(id)
}

internal fun updateCheckDemandIndividual(requirementPersonId:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).
        addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.updateCheckDemandIndividual(requirementPersonId)
}

internal fun updateCheckDemandGroup(requirementTeamId:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).
        addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.updateCheckDemandGroup(requirementTeamId)
}

internal fun updateCheckDemandLease(requirementLeaseId:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).
        addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.updateCheckDemandLease(requirementLeaseId)
}

internal fun updateCheckDemandTripartite(requirementThirdId:String):Observable<ResponseBody>{
    val interceptor = Interceptor {
        it.proceed(it.request().newBuilder().addHeader("zouxiaodong",UnSerializeDataBase.userToken).build())
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val retrofit = Retrofit.Builder().client(client).baseUrl(UnSerializeDataBase.dmsBasePath).
        addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()
    val httpHelper = retrofit.create(HttpHelper::class.java)
    return httpHelper.updateCheckDemandTripartite(requirementThirdId)
}















