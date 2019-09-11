package com.example.eletronicengineer.db

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class MajorDistribuionProjectEntity(
    var id:Long,
    var vipId:Long,
    var name:String,
    var projectBatch:String,
    var site:String?,
    var property:String,
    var nature:String,
    var voltageClasses:String,
    var timeLimit:String?,
    var constrctCooperatingOrgId:Long,
    var designCooperatingOrgId:Long,
    var surpervisorCooperatingOrgId:Long,
    var dateCommence:String,
    var reportCommence:String,
    var completionDate:String,
    var drawingPath:String,
    var taskAllocation:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Serializable
{

}