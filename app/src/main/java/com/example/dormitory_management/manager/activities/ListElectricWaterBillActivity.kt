package com.example.dormitory_management.manager.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.adapters.ManagerElectricWaterBillAdapter
import com.example.dormitory_management.manager.repositories.ManagerElectricWaterBillRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.models.BillModel

class ListElectricWaterBillActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ManagerElectricWaterBillAdapter
    private val repository = ManagerElectricWaterBillRepository()

    companion object {
        const val ADD_BILL_REQUEST_CODE = 1001
        const val UPDATE_BILL_REQUEST_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_electric_water_bill)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cập nhật điện nước"
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val roomId = intent.getStringExtra("roomId") ?: "Unknown Room ID"

        recyclerView = findViewById(R.id.rcv_list_bills)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadBills(roomId)

        val btnAdd = toolbar.findViewById<ImageButton>(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this@ListElectricWaterBillActivity, AddBillActivity::class.java)
            intent.putExtra("roomId", roomId)
            startActivityForResult(intent, ADD_BILL_REQUEST_CODE)
        }
    }

    private fun loadBills(roomId: String) {
        repository.loadAllElectricWaterBills(roomId, object : MyCallBack<List<BillModel>, String> {
            override fun success(data: List<BillModel>) {
                adapter = ManagerElectricWaterBillAdapter(data)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

                adapter.setOnItemClickListener { bill ->
                    val intent = Intent(this@ListElectricWaterBillActivity, UpdateElectricWaterBillActivity::class.java)
                    val selectedBillWithoutTimestamp = bill.copyWithoutConfirmDate()

                    intent.putExtra("selectedBill", selectedBillWithoutTimestamp)
                    intent.putExtra("roomId", roomId)
                    startActivityForResult(intent, UPDATE_BILL_REQUEST_CODE)
                }
            }

            override fun fail(message: String) {
                Toast.makeText(this@ListElectricWaterBillActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_BILL_REQUEST_CODE && resultCode == RESULT_OK) {
            val roomId = intent.getStringExtra("roomId") ?: "Unknown Room ID"
            loadBills(roomId)
        }
        if (requestCode == UPDATE_BILL_REQUEST_CODE && resultCode == RESULT_OK) {
            val roomId = intent.getStringExtra("roomId") ?: "Unknown Room ID"
            loadBills(roomId)
        }
    }
}
