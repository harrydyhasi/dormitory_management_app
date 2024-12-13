package com.example.dormitory_management.manager.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.RoomModel
import com.example.dormitory_management.manager.models.UserModel

class LookupStudentRepository {
    fun getListUser(callback: MyCallBack<List<UserModel>, String>) {
        val col = FirebaseUtils.instance.get_collection("users")

        col.whereEqualTo("status", "Active")
            .whereEqualTo("role", "student")
            .get()
            .addOnSuccessListener { documents ->
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

    fun getRoomById(id:String, callback: MyCallBack<RoomModel, String>){
        val doc = FirebaseUtils.instance.get_document("rooms", id)

        doc.get().addOnSuccessListener { documentSnapshot ->
            callback.success(RoomModel(id, documentSnapshot))
        }.addOnFailureListener { exception ->
            callback.fail("fail getRoom")
        }
    }
}
