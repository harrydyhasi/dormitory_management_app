package com.example.dormitory_management.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.auth.adapters.RoomAdapter
import com.example.dormitory_management.auth.models.RoomModel
import com.example.dormitory_management.auth.repositories.AuthRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomSelectionActivity : AppCompatActivity() {
    private lateinit var recyclerViewRooms: RecyclerView
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var authRepository: AuthRepository
    private lateinit var tvDateRange: TextView
    private lateinit var btnNextToUserInfo: MaterialButton
    private var selectedRoom: RoomModel? = null
    private lateinit var btnBackToLogin: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_room_register)

        recyclerViewRooms = findViewById(R.id.recyclerViewRooms)
        tvDateRange = findViewById(R.id.tv_date_range)
        btnNextToUserInfo = findViewById(R.id.btn_next_to_register)
        btnBackToLogin = findViewById(R.id.btn_back_to_login)

        calculateDateRange()

        roomAdapter = RoomAdapter(emptyList()) { room ->
            selectedRoom = room // Lưu phòng được chọn
        }
        recyclerViewRooms.layoutManager = LinearLayoutManager(this)
        recyclerViewRooms.adapter = roomAdapter

        authRepository = AuthRepository()
        loadRooms()

        btnNextToUserInfo.setOnClickListener {
            handleNext()
        }

        btnBackToLogin.setOnClickListener {
            navigateBackToLogin()
        }
    }

    private fun loadRooms() {
        authRepository.getAvailableRooms(object : MyCallBack<List<RoomModel>, String> {
            override fun success(param: List<RoomModel>) {
                roomAdapter.updateData(param)
                Log.d("TAG", param.toString())
            }

            override fun fail(param: String) {
                Toast.makeText(this@RoomSelectionActivity, "Không thể tải phòng: $param", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun calculateDateRange() {
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_MONTH)

        // Xác định ngày bắt đầu
        if (today > 25) {
            calendar.add(Calendar.MONTH, 1) // Sang tháng sau
        }
        calendar.set(Calendar.DAY_OF_MONTH, 25)
        val startDate = calendar.time

        // Xác định ngày hết hạn
        calendar.add(Calendar.MONTH, 3)
        val endDate = calendar.time

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        tvDateRange.text = "Ngày bắt đầu: ${dateFormat.format(startDate)}, Ngày hết hạn: ${dateFormat.format(endDate)}"
    }

    private fun handleNext() {
        if (selectedRoom == null) {
            Toast.makeText(this, "Vui lòng chọn một phòng trước khi tiếp tục.", Toast.LENGTH_SHORT).show()
            return
        }

        // Chuyển sang trang RegisterActivity
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("selectedRoomId", selectedRoom!!.id)
        intent.putExtra("startDate", tvDateRange.text.toString().split(",")[0].split(":")[1].trim())
        intent.putExtra("endDate", tvDateRange.text.toString().split(",")[1].split(":")[1].trim())
        startActivity(intent)
    }

    private fun navigateBackToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Đảm bảo kết thúc activity hiện tại
    }
}
