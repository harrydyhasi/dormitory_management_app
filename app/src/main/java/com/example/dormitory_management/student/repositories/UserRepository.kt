package com.example.dormitory_management.student.repositories

import com.example.dormitory_management.Utils.FirebaseUtils
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import java.util.Date

class UserRepository() {
    val collection = "users"

    fun getUserById(id:String, callback: MyCallBack<UserModel, String>){
        val doc = FirebaseUtils.instance.get_document(collection, id)

        doc.get().addOnSuccessListener { documentSnapshot ->
            callback.success(UserModel(documentSnapshot))
        }.addOnFailureListener { exception ->
            callback.fail("fail getUserById")
        }
    }

    fun updatePass(id: String, oldPass: String, newPass: String, callback: MyCallBack<String, String>){
        val doc = FirebaseUtils.instance.get_document(collection, id)
        doc.get().addOnSuccessListener { documentSnapshot ->
            val pass = documentSnapshot.getString("password").toString()
            if (pass != oldPass ){
                callback.fail("Mật khẩu không đúng!")
            } else if (pass == newPass){
                callback.fail("Mật khẩu đã dùng!")
            } else{
                doc.update("password", newPass)
                callback.success("success updatePass")
            }
        }.addOnFailureListener { exception ->
            callback.fail("fail updatePass")
        }

    }

    fun addUser(user: UserModel, callback: MyCallBack<String, String>) {
        val userPhoneNumber = user.phoneNumber ?: return callback.fail("User phone number is null")
        callback.success(userPhoneNumber)
        FirebaseUtils.instance.get_collection(collection).document(userPhoneNumber)
            .set(user)
            .addOnSuccessListener {
                callback.success("User added with phone number ID: $userPhoneNumber")
            }
            .addOnFailureListener { e ->
                callback.fail("Failed to add user: ${e.message}")
            }
    }
    fun getEndDate(id: String, callback: MyCallBack<Date, String>) {
        val doc = FirebaseUtils.instance.get_document(collection, id)

        doc.get().addOnSuccessListener { documentSnapshot ->
            // Assuming the 'endDate' is stored in Firestore as a timestamp
            val endDate = documentSnapshot.getDate("dateEnd") // Replace with actual field name if necessary
            if (endDate != null) {
                callback.success(endDate)
            } else {
                callback.fail("End date not found for user with ID: $id")
            }
        }.addOnFailureListener { exception ->
            callback.fail("Failed to retrieve end date: ${exception.message}")
        }
    }



}