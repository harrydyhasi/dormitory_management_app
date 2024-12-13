package com.example.dormitory_management.manager.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.BillModel
import com.example.dormitory_management.manager.repositories.ManagerElectricWaterBillRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp

class AddBillActivity : AppCompatActivity() {

    private lateinit var edtTitle: EditText
    private lateinit var edtElectricStart: EditText
    private lateinit var edtElectricEnd: EditText
    private lateinit var edtWaterStart: EditText
    private lateinit var edtWaterEnd: EditText
    private lateinit var btnSave: MaterialButton

    private val roomId: String? by lazy { intent.getStringExtra("roomId") }
    private val managerElectricWaterBillRepository = ManagerElectricWaterBillRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bill)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Thêm hóa đơn điện nước"
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        edtTitle = findViewById(R.id.edtTitle)
        edtElectricStart = findViewById(R.id.edtElectricStart)
        edtElectricEnd = findViewById(R.id.edtElectricEnd)
        edtWaterStart = findViewById(R.id.edtWaterStart)
        edtWaterEnd = findViewById(R.id.edtWaterEnd)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val title = edtTitle.text.toString().trim()
            val electricStart = edtElectricStart.text.toString().toDoubleOrNull()
            val electricEnd = edtElectricEnd.text.toString().toDoubleOrNull()
            val waterStart = edtWaterStart.text.toString().toDoubleOrNull()
            val waterEnd = edtWaterEnd.text.toString().toDoubleOrNull()

            if (title.isEmpty() || electricStart == null || electricEnd == null || waterStart == null || waterEnd == null) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val electricUsed = (electricEnd - electricStart).toInt()
            val waterUsed = (waterEnd - waterStart).toInt()
            val totalElectric = electricUsed * 3500
            val totalWater = waterUsed * 12500
            val totalBill = totalElectric + totalWater

            val newBill = BillModel(
                id = null,
                confirmDate = Timestamp.now(),
                electricStartNumber = electricStart,
                electricEndNumber = electricEnd,
                electricUsed = electricUsed,
                status = "Chưa thanh toán",
                title = title,
                totalBill = totalBill,
                totalElectric = totalElectric,
                totalWater = totalWater,
                waterStartNumber = waterStart,
                waterEndNumber = waterEnd,
                waterUsed = waterUsed
            )

            managerElectricWaterBillRepository.addElectricWaterBill(
                roomId.toString(),
                newBill,
                object : MyCallBack<Boolean, String> {
                    override fun success(result: Boolean) {
                        Toast.makeText(this@AddBillActivity, "Hóa đơn đã được thêm thành công", Toast.LENGTH_SHORT).show()

                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun fail(error: String) {
                        Toast.makeText(this@AddBillActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}
