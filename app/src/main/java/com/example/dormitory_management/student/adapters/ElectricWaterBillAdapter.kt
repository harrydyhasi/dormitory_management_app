package com.example.dormitory_management.student.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.student.activities.ElectricWaterBillDetailActivity
import com.example.dormitory_management.student.models.ElectricWaterBillModel

class ElectricWaterBillAdapter(
    private val bills: List<ElectricWaterBillModel>,
    private val building: String,
    private val room: String
) : RecyclerView.Adapter<ElectricWaterBillAdapter.BillViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_bill, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = bills[position]
        holder.txtTitle.text = bill.title
        holder.txtBuilding.text = building
        holder.txtRoom.text = room
        holder.txtTotalBill.text = "${bill.totalBill} VNĐ"
        holder.txtStatus.text = bill.status
        when (bill.status) {
            "Chưa thanh toán" -> {
                holder.txtStatus.setBackgroundResource(R.drawable.status_badge_bg)
                holder.txtStatus.isSelected = false
            }
            "Đã thanh toán" -> {
                holder.txtStatus.setBackgroundResource(R.drawable.status_badge_bg)
                holder.txtStatus.isSelected = true
            }
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ElectricWaterBillDetailActivity::class.java)
            intent.putExtra("bill", bill)
            intent.putExtra("roomId", room)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = bills.size

    class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        val txtBuilding: TextView = itemView.findViewById(R.id.txt_building)
        val txtRoom: TextView = itemView.findViewById(R.id.txt_room)
        val txtTotalBill: TextView = itemView.findViewById(R.id.txt_total)
        val txtStatus: TextView = itemView.findViewById(R.id.txt_status)
    }
}