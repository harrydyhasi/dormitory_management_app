package com.example.dormitory_management.student.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class ElectricWaterBillModel(
    var id: String?,
    var confirmDate: Timestamp?,
    var electricStartNumber: Double?,
    var electricEndNumber: Double?,
    var electricUsed: Double?,
    var status: String?,
    var title: String?,
    var totalBill: Int?,
    var totalElectric: Int?,
    var totalWater: Int?,
    var waterStartNumber: Double?,
    var waterEndNumber: Double?,
    var waterUsed: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    constructor(document: DocumentSnapshot) : this(
        id = document.id,
        confirmDate = document.getTimestamp("confirmDate"),
        electricStartNumber = document.getDouble("electricStartNumber"),
        electricEndNumber = document.getDouble("electricEndNumber"),
        electricUsed = document.getDouble("electricUsed"),
        status = document.getString("status"),
        title = document.getString("title"),
        totalBill = document.getLong("totalBill")?.toInt(),
        totalElectric = document.getLong("totalElectric")?.toInt(),
        totalWater = document.getLong("totalWater")?.toInt(),
        waterStartNumber = document.getDouble("waterStartNumber"),
        waterEndNumber = document.getDouble("waterEndNumber"),
        waterUsed = document.getLong("waterUsed")?.toInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(confirmDate, flags)
        parcel.writeValue(electricStartNumber)
        parcel.writeValue(electricEndNumber)
        parcel.writeValue(electricUsed)
        parcel.writeString(status)
        parcel.writeString(title)
        parcel.writeValue(totalBill)
        parcel.writeValue(totalElectric)
        parcel.writeValue(totalWater)
        parcel.writeValue(waterStartNumber)
        parcel.writeValue(waterEndNumber)
        parcel.writeValue(waterUsed)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ElectricWaterBillModel> {
        override fun createFromParcel(parcel: Parcel): ElectricWaterBillModel {
            return ElectricWaterBillModel(parcel)
        }

        override fun newArray(size: Int): Array<ElectricWaterBillModel?> {
            return arrayOfNulls(size)
        }
    }

}

