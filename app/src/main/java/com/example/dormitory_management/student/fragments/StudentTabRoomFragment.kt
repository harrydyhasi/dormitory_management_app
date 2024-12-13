package com.example.dormitory_management.student.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.StudentRoomModel
import com.example.dormitory_management.student.models.UserModel
import com.example.dormitory_management.student.repositories.StudentRoomRepository
import java.text.SimpleDateFormat
import java.util.Locale

class StudentTabRoomFragment(var user: UserModel): Fragment() {
    private val key = "StudentTabRoomFragment"

    // view
    private lateinit var txtBuilding: TextView
    private lateinit var txtRoom: TextView
    private lateinit var txtDateStart: TextView
    private lateinit var txtDateEnd: TextView
    private val studentRoomRepository = StudentRoomRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_tab_room, container, false)
        txtBuilding = view.findViewById(R.id.txt_building)
        txtRoom = view.findViewById(R.id.txt_room)
        txtDateStart = view.findViewById(R.id.txt_date_start)
        txtDateEnd = view.findViewById(R.id.txt_date_end)

        loadView(user)
        val idRoom = user.idRoom?.toString()
        if (idRoom.isNullOrEmpty()) {
            Log.d(key, "idRoom is null or empty")
        } else {
            studentRoomRepository.getRoomById(idRoom, object : MyCallBack<StudentRoomModel, String> {
                override fun success(param: StudentRoomModel) {
                    txtBuilding.text = param.buildingNumber
                    txtRoom.text = param.roomNumber
                    Log.d(key, "Lấy thông tin phòng thành công")
                }

                override fun fail(param: String) {
                    Log.d(key, "Lấy thông tin phòng thất bại: $param")
                }
            })
        }

        return view
    }

    private fun loadView(user: UserModel) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        txtDateStart.text = user.dateStart?.let { dateFormat.format(it) } ?: "N/A"
        txtDateEnd.text = user.dateEnd?.let { dateFormat.format(it) } ?: "N/A"
    }
}
