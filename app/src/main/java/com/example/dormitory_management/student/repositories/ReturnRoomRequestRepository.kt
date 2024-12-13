package com.example.dormitory_management.student.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.ReturnRoomRequestModel
import java.util.Date

class ReturnRoomRequestRepository {
    fun addReturnRoomRequest(userId: String, date: Date, status: String, callback: MyCallBack<Unit, String>) {
        val returnRoomRequest = hashMapOf(
            "dateReturn" to date,
            "status" to status
        )

        FirebaseUtils.instance.get_document("users", userId)
            .collection("returnRoomRequests")
            .add(returnRoomRequest)
            .addOnSuccessListener {
                callback.success(Unit)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to add return room request: ${exception.message}")
            }
    }

    fun getReturnRoomRequests(userId: String, callback: MyCallBack<List<ReturnRoomRequestModel>, String>) {
        FirebaseUtils.instance.get_document("users", userId)
            .collection("returnRoomRequests")
            .get()
            .addOnSuccessListener { result ->
                val returnRoomRequests = result.map { document -> ReturnRoomRequestModel(document) }
                callback.success(returnRoomRequests)
            }
            .addOnFailureListener { exception ->
                callback.fail("Failed to load return room requests: ${exception.message}")
            }
    }
}
