package com.example.dormitory_management.student.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dormitory_management.R
import com.example.dormitory_management.student.models.UserModel
import java.text.SimpleDateFormat
import java.util.Locale

class StudentTabCommonFragment(val user: UserModel):Fragment(){
    val key = "StudentTabCommonFragment"
    // view
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtGender: TextView
    private lateinit var txtCCCD: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtNumberPhone: TextView

    // data

    //
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_tab_common, container, false)
        // view
        txtName  = view.findViewById(R.id.txt_fullname)
        txtEmail = view.findViewById(R.id.txt_email)
        txtGender = view.findViewById(R.id.txt_gender)
        txtCCCD = view.findViewById(R.id.txt_cccd)
        txtDate = view.findViewById(R.id.txt_date)
        txtNumberPhone = view.findViewById(R.id.txt_number_phone)

        // data

        // feature
        loadView(user)

        return  view
    }

    fun loadView(user: UserModel){
        txtName.setText(user.fullName)
        txtCCCD.setText(user.cccd)
        txtEmail.setText(user.email)
        txtGender.setText(user.gender)
        txtNumberPhone.setText(user.phoneNumber)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        txtDate.setText(user.dateStart?.let { dateFormat.format(it) } ?: "N/A")
    }
}