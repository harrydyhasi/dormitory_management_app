package com.example.dormitory_management.Utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtils {
    companion object {
        val instance = FirebaseUtils()
    }

    val firestore = FirebaseFirestore.getInstance()

    fun get_document(colection: String, document: String): DocumentReference{
        return firestore.collection(colection).document(document)
    }

    fun get_collection(colection: String): CollectionReference {
        return firestore.collection(colection)
    }
}