package com.example.dormitory_management.student.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import com.example.dormitory_management.student.repositories.UserRepository
import com.google.android.material.textfield.TextInputEditText


class ChangePasswordStudentActivity: AppCompatActivity() {
    //
    private val userRepository: UserRepository = UserRepository()
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_change_password)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Đổi mật khẩu"
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val userId = intent.getStringExtra("USER_ID") ?: ""

        val txt_oldPass: TextInputEditText = findViewById(R.id.txt_oldPass)
        val txt_newPass: TextInputEditText = findViewById(R.id.txt_newPass)
        val txt_check : TextView = findViewById(R.id.txt_check_pass)
        val txt_confirm: TextInputEditText = findViewById(R.id.txt_confirmPass)
        val txt_new: TextView = findViewById(R.id.txt_check_new)
        val txt_old: TextView = findViewById(R.id.txt_check_old)

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            var check = true
            if (txt_oldPass.text.toString().isEmpty() ){
                check = false
                txt_old.text = "Vui lòng nhập mật khẩu!!"
            } else{
                txt_old.text = ""
            }
            if (txt_newPass.text.toString().isEmpty() ){
                check = false
                txt_new.text = "Vui lòng nhập mật khẩu!!"
            } else {
                txt_new.text = ""
            }
            if (txt_newPass.text.toString() !=  txt_confirm.text.toString()){
                check = false
                txt_check.text = "Không trùng khớp!!"
            } else {
                txt_check.text = ""
            }

            if ( check ){
                savePass(userId, txt_oldPass.text.toString(), txt_newPass.text.toString())
            }
        }

        txt_oldPass.addTextChangedListener(object: TextWatcher{
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable) {
                if (s.toString().isNotEmpty()){
                    txt_old.text = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

        })
        txt_newPass.addTextChangedListener(object :
            TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable) {
                if (txt_confirm.text.toString().isEmpty()){
                    txt_check.text = ""
                } else
                if (s.toString() != txt_confirm.text.toString()) {
                    txt_check.text = "Không trùng khớp!!"
                } else{
                    txt_check.text = ""
                }
                if (s.toString().isNotEmpty()){
                    txt_new.text = ""
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        });

        txt_confirm.addTextChangedListener(object :
            TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable) {
                if (txt_newPass.text.toString().isEmpty()){
                    txt_check.text = ""
                } else
                if (s.toString() != txt_newPass.text.toString()) {
                    txt_check.text = "Không trùng khớp!!"
                } else{
                    txt_check.text = ""
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        });
    }

    private fun savePass(idUser: String, old: String, new: String){
        userRepository.updatePass(
            idUser, old, new,
            object : MyCallBack<String, String> {
                override fun success(param: String) {
                    Toast.makeText(this@ChangePasswordStudentActivity, param.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun fail(param: String) {
                    Toast.makeText(this@ChangePasswordStudentActivity, param.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            )

    }

    override fun onBackPressed() {
        // Optional: Add any custom behavior here
        super.onBackPressed()  // This will navigate back to the previous Activity
    }
}