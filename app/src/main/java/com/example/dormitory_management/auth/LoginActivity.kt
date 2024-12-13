package com.example.dormitory_management.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dormitory_management.R
import com.example.dormitory_management.admin.AdminActivity
import com.example.dormitory_management.admin.repositories.AdminRepository
import com.example.dormitory_management.auth.repositories.AuthRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.ManagerActivity
import com.example.dormitory_management.student.StudentActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var adminRepository: AdminRepository

    // initialize Auth repository
    private lateinit var authRepository: AuthRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupVar()

        findViewById<Button>(R.id.loginButton).setOnClickListener{
            handlerLogin()
        }

        findViewById<Button>(R.id.registerButton).setOnClickListener{
            val intent = Intent(
                this,
                RoomSelectionActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun handlerLogin() {
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

//        val username = "123@gmail.com"
//        val password =  "12345"
//        val intent = Intent(this@LoginActivity, ManagerActivity::class.java)
//        startActivity(intent)


        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show()
            return
        }

//        adminRepository.loginAdmin(username, password, object : MyCallBack<String, String> {
//            override fun success(data: String) {
//                Toast.makeText(this@LoginActivity, data, Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@LoginActivity, AdminActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//
//            override fun fail(error: String) {
//                Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
//                val intent = Intent(this@LoginActivity, StudentActivity::class.java)
//                startActivity(intent)
//            }
//        })

        authRepository.login(username, password, object : MyCallBack<Triple<String, String, String>, String> {
            override fun success(data: Triple<String, String, String>) {
                val (role, userId, roomId) = data
//                Toast.makeText(this@LoginActivity, "Đăng nhập thành công với quyền $role, ID: $userId, và phòng: $roomId", Toast.LENGTH_SHORT).show()

                val intent = when (role) {
                    "admin" -> Intent(this@LoginActivity, AdminActivity::class.java)
                    "manager" -> Intent(this@LoginActivity, ManagerActivity::class.java)
                    "student" -> Intent(this@LoginActivity, StudentActivity::class.java).apply {
                        putExtra("USER_ID", userId) // Pass UserId to StudentActivity
                        putExtra("ROOM_ID", roomId) // Pass RoomId to StudentActivity
                    }
                    else -> {
                        Toast.makeText(this@LoginActivity, "Unknown role: $role", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                startActivity(intent)
                finish()
            }

            override fun fail(error: String) {
                Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun setupVar()  {
//        edtUsername = findViewById(R.id.edt_login_username)
//        edtPassword = findViewById(R.id.edt_login_pass)
//        adminRepository = AdminRepository()
//        authRepository = AuthRepository()
//    }

    private lateinit var togglePassword: ImageView
    private var isPasswordVisible = false

    private fun setupVar() {
        edtUsername = findViewById(R.id.edt_login_username)
        edtPassword = findViewById(R.id.edt_login_pass)
        togglePassword = findViewById(R.id.togglePassword)
        adminRepository = AdminRepository()
        authRepository = AuthRepository()

        togglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                edtPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePassword.setImageResource(R.drawable.ic_visibility_off)
            } else {
                edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePassword.setImageResource(R.drawable.ic_visibility_on)
            }
            // Move the cursor to the end of the text
            edtPassword.setSelection(edtPassword.text.length)
        }
    }

}
