package com.example.dormitory_management.manager.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.BillModel

class ManagerElectricWaterBillAdapter(
    private val bills: List<BillModel>,
) : RecyclerView.Adapter<ManagerElectricWaterBillAdapter.BillViewHolder>() {

    private var onItemClickListener: ((BillModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_bill, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = bills[position]
        holder.txtTitle.text = bill.title
        holder.txtStatus.text = bill.status

        // Gọi sự kiện click khi bấm vào item
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(bill)
        }
    }

    override fun getItemCount(): Int = bills.size

    fun setOnItemClickListener(listener: (BillModel) -> Unit) {
        onItemClickListener = listener
    }

    class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        val txtStatus: TextView = itemView.findViewById(R.id.txt_status)
    }
}
