package com.example.dormitory_management.student.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.manager.models.RoomModel
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.UserModel

class RoomRepository() {
    val collection = "rooms"
    val collectionUser = "users"

    fun getRoomById(id:String, callback: MyCallBack<RoomModel, String>){
        val doc = FirebaseUtils.instance.get_document(collection, id)

        doc.get().addOnSuccessListener { documentSnapshot ->
            callback.success(RoomModel(id, documentSnapshot))
        }.addOnFailureListener { exception ->
            callback.fail(exception.toString())
        }
    }

    fun getListUserByIdRoom(idRoom: String, callback: MyCallBack<List<UserModel>, String>){
        val coll = FirebaseUtils.instance.get_collection(collectionUser)
            .whereEqualTo("idRoom", idRoom)
        coll.get().addOnSuccessListener { documents ->
            val list = mutableListOf<UserModel>()
            for (document in documents) {
                val user = UserModel(document)
                list.add(user)
            }
            callback.success(list)
        }
            .addOnFailureListener { exception ->
                callback.fail(exception.toString())
            }
    }

    fun getListRoomBy(callback: MyCallBack<List<RoomModel>, String>){
        val col = FirebaseUtils.instance.get_collection(collection)

        col.get().addOnSuccessListener { documents ->
            val list = mutableListOf<RoomModel>()
            for (document in documents) {
                val data: MutableMap<String, Any> = document.data
                val room = RoomModel(document.id, data)
                list.add(room)
            }
            callback.success(list)
        }
        .addOnFailureListener { exception ->
            callback.fail(exception.toString())
        }
    }

}