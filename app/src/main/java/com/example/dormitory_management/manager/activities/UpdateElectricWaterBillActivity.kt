package com.example.dormitory_management.manager.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.BillModel
import com.example.dormitory_management.manager.repositories.ManagerElectricWaterBillRepository

class UpdateElectricWaterBillActivity : AppCompatActivity() {
    private val roomId: String? by lazy { intent.getStringExtra("roomId") }
    private val repository = ManagerElectricWaterBillRepository()
    private lateinit var btnDelete: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_electric_water_bill)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cập nhật điện nước"
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        loadDetailBill()

        val btnSave = toolbar.findViewById<ImageButton>(R.id.btnSave)
        btnSave.setOnClickListener {
            updateBill()
        }
        btnDelete = findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener {
            showDeleteConfirmDialog()
        }
    }

    private fun loadDetailBill() {
        val txtStatus: TextView = findViewById(R.id.txt_status)
        val edtTitle: EditText = findViewById(R.id.edtTitle)
        val edtElectricStart: EditText = findViewById(R.id.edtElectricStart)
        val edtElectricEnd: EditText = findViewById(R.id.edtElectricEnd)
        val edtWaterStart: EditText = findViewById(R.id.edtWaterStart)
        val edtWaterEnd: EditText = findViewById(R.id.edtWaterEnd)

        val selectedBill = intent.getSerializableExtra("selectedBill") as? BillModel

        if (selectedBill == null) {
            Toast.makeText(this, "Lỗi khi tải thông tin hóa đơn", Toast.LENGTH_SHORT).show()
            return
        }

        txtStatus.text = selectedBill.status
        edtTitle.setText(selectedBill.title)
        edtElectricStart.setText(selectedBill.electricStartNumber?.toString())
        edtElectricEnd.setText(selectedBill.electricEndNumber?.toString())
        edtWaterStart.setText(selectedBill.waterStartNumber?.toString())
        edtWaterEnd.setText(selectedBill.waterEndNumber?.toString())
    }

    private fun updateBill() {
        val txtStatus: TextView = findViewById(R.id.txt_status)
        val edtTitle: EditText = findViewById(R.id.edtTitle)
        val edtElectricStart: EditText = findViewById(R.id.edtElectricStart)
        val edtElectricEnd: EditText = findViewById(R.id.edtElectricEnd)
        val edtWaterStart: EditText = findViewById(R.id.edtWaterStart)
        val edtWaterEnd: EditText = findViewById(R.id.edtWaterEnd)

        val selectedBill = intent.getSerializableExtra("selectedBill") as? BillModel
        if (selectedBill == null || roomId.isNullOrEmpty()) {
            Toast.makeText(this, "Lỗi khi tải thông tin hóa đơn hoặc phòng", Toast.LENGTH_SHORT).show()
            return
        }

        val title = edtTitle.text.toString().trim()
        val electricStart = edtElectricStart.text.toString().toDoubleOrNull()
        val electricEnd = edtElectricEnd.text.toString().toDoubleOrNull()
        val waterStart = edtWaterStart.text.toString().toDoubleOrNull()
        val waterEnd = edtWaterEnd.text.toString().toDoubleOrNull()

        if (title.isEmpty() || electricStart == null || electricEnd == null || waterStart == null || waterEnd == null) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val electricUsed = (electricEnd - electricStart).toInt()
        val waterUsed = (waterEnd - waterStart).toInt()
        val totalElectric = electricUsed * 3500
        val totalWater = waterUsed * 12500
        val totalBill = totalElectric + totalWater

        val updatedBill = selectedBill.copy(
            title = title,
            electricStartNumber = electricStart,
            electricEndNumber = electricEnd,
            electricUsed = electricUsed,
            waterStartNumber = waterStart,
            waterEndNumber = waterEnd,
            waterUsed = waterUsed,
            totalElectric = totalElectric,
            totalWater = totalWater,
            totalBill = totalBill,
            status = "Chưa thanh toán"
        )

        repository.updateElectricWaterBill(
            roomId!!,
            selectedBill.id.toString(),
            updatedBill,
            object : MyCallBack<Boolean, String> {
                override fun success(result: Boolean) {
                    Toast.makeText(this@UpdateElectricWaterBillActivity, "Cập nhật hóa đơn thành công", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun fail(error: String) {
                    Toast.makeText(this@UpdateElectricWaterBillActivity, "Lỗi khi cập nhật hóa đơn: $error", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    private fun showDeleteConfirmDialog() {
        val selectedBill = intent.getSerializableExtra("selectedBill") as? BillModel
        if (selectedBill == null || roomId.isNullOrEmpty()) {
            Toast.makeText(this, "Lỗi khi tải thông tin hóa đơn hoặc phòng", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_confirm_delete_bill, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnYes = dialogView.findViewById<Button>(R.id.btnYes)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
            deleteBill(selectedBill)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteBill(selectedBill: BillModel) {
        repository.deleteElectricWaterBill(
            roomId!!,
            selectedBill.id.toString(),
            object : MyCallBack<Boolean, String> {
                override fun success(result: Boolean) {
                    Toast.makeText(this@UpdateElectricWaterBillActivity, "Xóa hóa đơn thành công", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }

                override fun fail(error: String) {
                    Toast.makeText(this@UpdateElectricWaterBillActivity, "Lỗi khi xóa hóa đơn: $error", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
