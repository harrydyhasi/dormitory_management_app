package com.example.dormitory_management.student.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.dormitory_management.R

class PaymentActivity: AppCompatActivity() {
//    private lateinit var btnBack: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_payment)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Thanh toán"
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        toolbar.setNavigationOnClickListener {
            setResult(RESULT_OK)
            onBackPressed()

        }
        setWidgetEvent()
    }
    fun setWidgetEvent(){
//        btnBack.setOnClickListener {
//            setResult(RESULT_OK)
//            finish() }
    }
}