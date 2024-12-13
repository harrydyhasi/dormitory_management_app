package com.example.dormitory_management

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        testFirestoreConnection()
    }


    private fun testFirestoreConnection() {
        val firestore = FirebaseFirestore.getInstance()
        val testDoc = firestore.collection("testCollection").document("testDocument")

        // Write data to Firestore
        val data = hashMapOf("connected" to true)
        testDoc.set(data)
            .addOnSuccessListener {
                Log.d("FirestoreTest", "Data written successfully!")
                // Now read it back
                testDoc.get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            Log.d("FirestoreTest", "Data read successfully: ${document.data}")
                        } else {
                            Log.d("FirestoreTest", "No such document!")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreTest", "Error reading document", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreTest", "Error writing document", e)
            }
    }
}