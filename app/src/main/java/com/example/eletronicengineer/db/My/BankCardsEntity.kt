package com.example.eletronicengineer.db.My

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class BankCardsEntity (
    var id:String?,
    var vipId:String?,
    var bankCardNumber:String,
    var bankType:String,
    var foundTime:String?,
    var founder:String?,
    var alterTime:String?,
    var alterPeople:String?,
    var delFlag:String?,
    var version:String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(vipId)
        parcel.writeString(bankCardNumber)
        parcel.writeString(bankType)
        parcel.writeString(foundTime)
        parcel.writeString(founder)
        parcel.writeString(alterTime)
        parcel.writeString(alterPeople)
        parcel.writeString(delFlag)
        parcel.writeString(version)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BankCardsEntity> {
        override fun createFromParcel(parcel: Parcel): BankCardsEntity {
            return BankCardsEntity(parcel)
        }

        override fun newArray(size: Int): Array<BankCardsEntity?> {
            return arrayOfNulls(size)
        }
    }
}