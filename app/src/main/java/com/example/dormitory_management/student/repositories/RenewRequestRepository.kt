package com.example.dormitory_management.student.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.RenewRequestModel
import java.util.Date

class RenewRequestRepository {

    fun addRenewRequest(userId: String, startDate: Date, endDate: Date, status: String, callback: MyCallBack<Unit, String>) {
        val renewRequest = hashMapOf(
            "startDate" to startDate,
            "endDate" to endDate,
            "status" to status
        )

        FirebaseUtils.instance.get_document("users", userId)
            .collection("renewRequests")
            .add(renewRequest)
            .addOnSuccessListener {
                callback.success(Unit)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to add renew request: ${exception.message}")
            }
    }

    fun getRenewRequests(userId: String, callback: MyCallBack<List<RenewRequestModel>, String>) {
        FirebaseUtils.instance.get_document("users", userId)
            .collection("renewRequests")
            .get()
            .addOnSuccessListener { result ->
                val renewRequests = result.map { document -> RenewRequestModel(document) }
                callback.success(renewRequests)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to load renew requests: ${exception.message}")
            }
    }
}
