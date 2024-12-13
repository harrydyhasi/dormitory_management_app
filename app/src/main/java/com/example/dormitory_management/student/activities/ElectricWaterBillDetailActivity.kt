package com.example.dormitory_management.student.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.formatters.DateTimeFormat
import com.example.dormitory_management.student.models.ElectricWaterBillModel
import com.example.dormitory_management.student.repositories.ElectricWaterBillRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack

class ElectricWaterBillDetailActivity : AppCompatActivity() {
    private lateinit var txtTitle: TextView
    private lateinit var txtConfirmDate: TextView
    private lateinit var txtElecStartNum: TextView
    private lateinit var txtElecEndNum: TextView
    private lateinit var txtElecUsedNum: TextView
    private lateinit var txtElecAmount: TextView
    private lateinit var txtWaterStartNum: TextView
    private lateinit var txtWaterEndNum: TextView
    private lateinit var txtWaterUsedNum: TextView
    private lateinit var txtWaterAmount: TextView
    private lateinit var txtTotalBill: TextView
    private lateinit var statusBadge: TextView
    private lateinit var btnPay: Button
    private val billRepository = ElectricWaterBillRepository()
    private lateinit var bill: ElectricWaterBillModel
    private lateinit var roomId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_eletronic_water_bill_detail)

        // Initialize the views
        txtTitle = findViewById(R.id.txt_title)
        txtConfirmDate = findViewById(R.id.txt_confirm_date)
        txtElecStartNum = findViewById(R.id.txt_elec_start_num)
        txtElecEndNum = findViewById(R.id.txt_elec_end_num)
        txtElecUsedNum = findViewById(R.id.txt_elec_used_num)
        txtElecAmount = findViewById(R.id.txt_elec_amount)
        txtWaterStartNum = findViewById(R.id.txt_water_start_num)
        txtWaterEndNum = findViewById(R.id.txt_water_end_num)
        txtWaterUsedNum = findViewById(R.id.txt_water_used_num)
        txtWaterAmount = findViewById(R.id.txt_water_amount)
        txtTotalBill = findViewById(R.id.txt_total)
        statusBadge = findViewById(R.id.badge_payment_status)
        btnPay = findViewById(R.id.btn_pay)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chi tiết hóa đơn"
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Get bill and roomId from the intent
        bill = intent.getParcelableExtra("bill")!!
        roomId = intent.getStringExtra("roomId")!!

        setWidgetEvent()

        loadData()
    }

    fun loadData() {
        Log.d("TAG", "Loading data with bill: $bill")
        bill?.let {
            txtTitle.text = it.title ?: "N/A"
            txtConfirmDate.text = it.confirmDate?.let { timestamp -> DateTimeFormat.formatDate(timestamp.toDate()) }
            txtElecStartNum.text = "${it.electricStartNumber?.toString()} kWh" ?: "N/A"
            txtElecEndNum.text = "${it.electricStartNumber?.toString()} kWh" ?: "N/A"
            txtElecUsedNum.text = "${it.electricEndNumber?.toString()} kWh" ?: "N/A"
            txtElecAmount.text = "${it.totalElectric?.toString()} VNĐ"
            txtWaterStartNum.text = "${it.waterStartNumber?.toString()} khối" ?: "N/A"
            txtWaterEndNum.text = "${it.waterEndNumber?.toString()} khối" ?: "N/A"
            txtWaterUsedNum.text = "${it.waterUsed?.toString()} khối" ?: "N/A"
            txtWaterAmount.text = "${it.totalWater?.toString()} VNĐ" ?: "N/A"
            txtTotalBill.text = "${it.totalBill?.toString()} VNĐ"
            statusBadge.text = it.status ?: "Chưa thanh toán"
            when (it.status) {
                "Chưa thanh toán" -> {
                    statusBadge.setBackgroundResource(R.drawable.status_badge_bg)
                    statusBadge.text = "Chưa thanh toán"
                    statusBadge.isSelected = false
                }
                "Đã thanh toán" -> {
                    statusBadge.setBackgroundResource(R.drawable.status_badge_bg)
//                    statusBadge.setText(it.status)
                    statusBadge.text = "Đã thanh toán"
                    statusBadge.isSelected = true
                }
                else -> {
                    statusBadge.setBackgroundResource(R.drawable.status_badge_bg)
                    statusBadge.text = it.status
                    statusBadge.isSelected = true
                }
            }
        }
    }

    fun setWidgetEvent() {
        btnPay.setOnClickListener {
            val intent = Intent(this@ElectricWaterBillDetailActivity, PaymentActivity::class.java)
            startActivityForResult(intent, REQUEST_PAYMENT)

        }
    }



    private fun updateStatus(){
        val status = "Đã thanh toán"
        bill.id?.let {
            billRepository.updateStatusElectricWaterBill(roomId, it, status, object :
                MyCallBack<ElectricWaterBillModel, String> {
                override fun success(data: ElectricWaterBillModel) {
                    Log.d("TAG", "Bill status updated in repository, updating UI")
                    bill = data

                    loadData()
                }

                override fun fail(error: String) {
                    Log.e("TAG", "fail: $error")
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PAYMENT && resultCode == RESULT_OK) {
            Log.d("TAG", "onActivityResult called")
            updateStatus()
        }
    }

    companion object {
        private const val REQUEST_PAYMENT = 1001
    }
}

