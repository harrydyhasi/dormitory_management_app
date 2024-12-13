package com.example.dormitory_management.manager.repositories

import android.util.Log
import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.RenewRequestModel
import com.example.dormitory_management.manager.models.RoomModel

class RenewRequestRepository {
    val collectionRenewRequest = "renewRequests"
    val collectionRoom = "rooms"
    val collectionUser = "users"

    fun getListRenewRequest(callback: MyCallBack<List<RenewRequestModel>, String>){
        val doc = FirebaseUtils.instance.get_collection(collectionUser)

        doc.get().addOnSuccessListener { documentSnapshot ->
            val list = mutableListOf<RenewRequestModel>()
            for (document in documentSnapshot) {
                val nameUser = document.getString("fullName").toString()
                val idRoom = document.getString("idRoom").toString()

                getRenewRequestByCollection(document.id,nameUser, idRoom,
                   object: MyCallBack<List<RenewRequestModel>, String>{
                       override fun success(param: List<RenewRequestModel>) {
                           list.addAll(param)
                           Log.d("2222", list.toString())
                           callback.success(list)
                       }
                       override fun fail(param: String) {
                           callback.fail("fail getAllRenewRequest")
                       }
                   })
            }
        }.addOnFailureListener { exception ->
            callback.fail("fail getAllRenewRequest")
        }
    }

    private fun getRoomById(id:String, callback: MyCallBack<RoomModel, String>){
        val doc = FirebaseUtils.instance.get_document(collectionRoom, id)
        doc.get().addOnSuccessListener { documentSnapshot ->
            callback.success(RoomModel(id, documentSnapshot))
        }.addOnFailureListener { exception ->
            callback.fail(exception.toString())
        }
    }

    fun changeStatusRenewRequest(renewRequestModel: RenewRequestModel, status: String, callback: MyCallBack<String, String> ){
        try {
            val doc = FirebaseUtils.instance.get_document(collectionUser,
                renewRequestModel.idUser.toString()
            ).collection(collectionRenewRequest).document(renewRequestModel.id.toString() )
            doc.update("status", status)
            callback.success("success")
        } catch (e: Exception){
            callback.fail("fail")

        }
    }

    private fun getRenewRequestByCollection(idUser: String, nameUser: String, idRoom:String, callback: MyCallBack<List<RenewRequestModel>, String>){
        val doc = FirebaseUtils.instance.get_document(collectionUser, idUser).collection(collectionRenewRequest)
        doc.get().addOnSuccessListener { documentSnapshot ->
            documentSnapshot.map { document ->
                val list = mutableListOf<RenewRequestModel>()
                getRoomById(idRoom,
                    object : MyCallBack<RoomModel, String> {
                    override fun success(param: RoomModel) {
                        list.add(RenewRequestModel(
                            idUser,
                            document.id,
                            nameUser,
                            param.roomNumber.toString(),
                            param.buildingNumber.toString(),
                            document
                        ))
                        callback.success(list)
                    }
                    override fun fail(param: String) {
                        callback.fail(param)
                    }
                })
            }
        }.addOnFailureListener { exception ->
            callback.fail(exception.toString())
        }

    }
}