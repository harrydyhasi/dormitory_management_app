package com.example.dormitory_management.manager.models

import java.io.Serializable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class BillModel(
    var id: String?,
    var confirmDate: Timestamp?,
    var electricStartNumber: Double?,
    var electricEndNumber: Double?,
    var electricUsed: Int?,
    var status: String?,
    var title: String?,
    var totalBill: Int?,
    var totalElectric: Int?,
    var totalWater: Int?,
    var waterStartNumber: Double?,
    var waterEndNumber: Double?,
    var waterUsed: Int?
) : Serializable {

    constructor(document: DocumentSnapshot) : this(
        id = document.id,
        confirmDate = document.getTimestamp("confirmDate"),
        electricStartNumber = document.getDouble("electricStartNumber"),
        electricEndNumber = document.getDouble("electricEndNumber"),
        electricUsed = document.getLong("electricUsed")?.toInt(),
        status = document.getString("status"),
        title = document.getString("title"),
        totalBill = document.getLong("totalBill")?.toInt(),
        totalElectric = document.getLong("totalElectric")?.toInt(),
        totalWater = document.getLong("totalWater")?.toInt(),
        waterStartNumber = document.getDouble("waterStartNumber"),
        waterEndNumber = document.getDouble("waterEndNumber"),
        waterUsed = document.getLong("waterUsed")?.toInt()
    )
    fun copyWithoutConfirmDate(): BillModel {
        return BillModel(
            id = this.id,
            confirmDate = null,
            electricStartNumber = this.electricStartNumber,
            electricEndNumber = this.electricEndNumber,
            electricUsed = this.electricUsed,
            status = this.status,
            title = this.title,
            totalBill = this.totalBill,
            totalElectric = this.totalElectric,
            totalWater = this.totalWater,
            waterStartNumber = this.waterStartNumber,
            waterEndNumber = this.waterEndNumber,
            waterUsed = this.waterUsed
        )
    }
}
