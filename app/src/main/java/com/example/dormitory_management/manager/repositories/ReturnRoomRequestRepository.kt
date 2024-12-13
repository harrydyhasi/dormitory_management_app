package com.example.dormitory_management.manager.repositories

import android.util.Log
import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.ReturnRoomRequestModel
import com.example.dormitory_management.manager.models.RoomModel

class ReturnRoomRequestRepository {
    private val collectionReturnRoomRequest = "returnRoomRequests"
    private val collectionUser = "users"
    val collectionRoom = "rooms"

    fun getListReturnRoomRequests(callback: MyCallBack<List<ReturnRoomRequestModel>, String>) {
        val doc = FirebaseUtils.instance.get_collection(collectionUser)

        doc.get().addOnSuccessListener { documentSnapshot ->
            val list = mutableListOf<ReturnRoomRequestModel>()
            for (document in documentSnapshot) {
                val fullName = document.getString("fullName").toString()
                val idRoom = document.getString("idRoom").toString()
                getReturnRoomRequestsByCollection(
                    document.id,
                    fullName,
                    idRoom,
                    object : MyCallBack<List<ReturnRoomRequestModel>, String> {
                        override fun success(param: List<ReturnRoomRequestModel>) {
                            list.addAll(param)
                            callback.success(list)
                        }

                        override fun fail(param: String) {
                            callback.fail("Failed to retrieve return room requests.")
                        }
                    })
            }
        }.addOnFailureListener {
            callback.fail("Failed to retrieve return room requests.")
        }
    }

    fun changeStatusReturnRoomRequest(
        returnRoomRequestModel: ReturnRoomRequestModel,
        status: String,
        callback: MyCallBack<String, String>
    ) {
        try {
            val doc = FirebaseUtils.instance.get_document(
                collectionUser,
                returnRoomRequestModel.idUser.toString()
            ).collection(collectionReturnRoomRequest).document(returnRoomRequestModel.id.toString())

            doc.update("status", status).addOnSuccessListener {
                callback.success("Status updated successfully.")
            }.addOnFailureListener {
                callback.fail("Failed to update status.")
            }
        } catch (e: Exception) {
            callback.fail("Error: ${e.message}")
        }
    }

    fun getReturnRoomRequestsByCollection(
        idUser: String,
        fullName: String,
        idRoom: String,
        callback: MyCallBack<List<ReturnRoomRequestModel>, String>
    ) {
        val doc = FirebaseUtils.instance.get_document(collectionUser, idUser)
            .collection(collectionReturnRoomRequest)

        doc.get().addOnSuccessListener { documentSnapshot ->
            getRoomById(idRoom, object : MyCallBack<RoomModel, String> {
                override fun success(param: RoomModel) {
                    val list = documentSnapshot.map { document ->
                        ReturnRoomRequestModel(
                            idUser = idUser,
                            id = document.id,
                            fullName = fullName,
                            roomNumber = param.roomNumber.toString(),
                            buildingNumber = param.buildingNumber.toString(),
                            dataSnapshot = document
                        )
                    }
                    callback.success(list)
                }

                override fun fail(param: String) {
                    callback.fail(param)
                }
            })
        }.addOnFailureListener { exception ->
            callback.fail("Failed to retrieve documents: ${exception.message}")
        }
    }


    private fun getRoomById(id: String, callback: MyCallBack<RoomModel, String>) {
        val doc = FirebaseUtils.instance.get_document(collectionRoom, id)
        doc.get().addOnSuccessListener { documentSnapshot ->
            callback.success(RoomModel(id, documentSnapshot))
        }.addOnFailureListener { exception ->
            callback.fail(exception.toString())
        }
    }


}


