package com.example.dormitory_management.auth

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.admin.models.RoomModelOut
import com.example.dormitory_management.auth.models.RoomModel
import com.example.dormitory_management.auth.adapters.RoomAdapter
import com.example.dormitory_management.auth.repositories.AuthRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import com.google.android.material.button.MaterialButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    private lateinit var edtUsername: EditText
    private lateinit var edtIdentify: EditText
    private lateinit var edtPhoneNumber: EditText
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var edtEmail: EditText
    private lateinit var btnDob: TextView
    private lateinit var btnRegister: MaterialButton
    private lateinit var authRepository: AuthRepository
    private var selectedRoom: String? = null
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var btnBack: MaterialButton

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupVar()
        setupEvent()
    }

    private fun setupVar() {

        edtUsername = findViewById(R.id.edt_register_username)
        edtIdentify = findViewById(R.id.edt_register_identify)
        edtPhoneNumber = findViewById(R.id.edt_register_phoneNumber)
        rbMale = findViewById(R.id.rb_nam)
        rbFemale = findViewById(R.id.rb_nu)
        edtEmail = findViewById(R.id.edt_register_email)
        btnDob = findViewById(R.id.btn_register_dob)
        btnRegister = findViewById(R.id.requestButton)
        selectedRoom = intent.getStringExtra("selectedRoomId")
        startDate = intent.getStringExtra("startDate") ?: ""
        endDate = intent.getStringExtra("endDate") ?: ""
        btnBack = findViewById(R.id.btn_back_to_room)
    }

    @SuppressLint("SetTextI18n")
    private fun setupEvent() {
        btnDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    btnDob.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        btnRegister.setOnClickListener {
            handleRegistration()
        }
        authRepository = AuthRepository()

        btnBack.setOnClickListener {
            navigateBackToRoom()
        }
    }

    private fun navigateBackToRoom() {
        val intent = Intent(this, RoomSelectionActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Đảm bảo kết thúc activity hiện tại
    }

    private fun handleRegistration() {
        if (selectedRoom == null) {
            Toast.makeText(this, "Vui lòng chọn một phòng", Toast.LENGTH_SHORT).show()
            return
        }

        authRepository.getRoom(selectedRoom!!, object : MyCallBack<RoomModel?, String> {
            override fun success(data: RoomModel?) {
                if (data == null || data.available!! >= 4) {
                    Toast.makeText(this@RegisterActivity, "Phòng đã đầy, vui lòng chọn phòng khác", Toast.LENGTH_SHORT).show()
                    return
                }

                val username = edtUsername.text.toString().trim()
                val identify = edtIdentify.text.toString().trim()
                val phoneNumber = edtPhoneNumber.text.toString().trim()
                val email = edtEmail.text.toString().trim()
                val dobString = btnDob.text.toString().trim()
                val gender = if (rbMale.isChecked) "Nam" else "Nữ"

                if (username.isEmpty() || identify.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || dobString.isEmpty()) {
                    Toast.makeText(this@RegisterActivity, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return
                }

                if (identify.length != 12) {
                    Toast.makeText(this@RegisterActivity, "Căn cước công dân phải là 12 số", Toast.LENGTH_SHORT).show()
                    return
                }

                if (phoneNumber.length != 10) {
                    Toast.makeText(this@RegisterActivity, "Số điện thoại phải là 10 số", Toast.LENGTH_SHORT).show()
                    return
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(this@RegisterActivity, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                    return
                }

                val dob = convertToDate(dobString)

                val newUser = UserModel(
                    fullName = username,
                    cccd = identify,
                    email = email,
                    date = dob,
                    gender = gender,
                    password = "123",
                    phoneNumber = phoneNumber,
                    idRoom = selectedRoom,
                    dateStart =  convertToDate(startDate),
                    dateEnd =  convertToDate(endDate),
                    dateReturn = null,
                    price = 0L,
                    status = "Inactive",
                    statusReturn = "",
                    role = "student"
                )

                authRepository.addUser(newUser, selectedRoom!!, object : MyCallBack<String, String> {
                    override fun success(userId: String) {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                    override fun fail(param: String) {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thất bại: $param", Toast.LENGTH_SHORT).show()
                        Log.e("RegisterActivity", "Failed to add user: $param")
                    }
                })
            }
            override fun fail(param: String) {
                Toast.makeText(this@RegisterActivity, "Không thể lấy thông tin phòng: $param", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun convertToDate(dateString: String): Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())  // Định dạng ngày của bạn
        return try {
            dateFormat.parse(dateString)
        } catch (e: ParseException) {
            Toast.makeText(this, "Lỗi format: " + e.message, Toast.LENGTH_SHORT).show()
            null
        }
    }
}
