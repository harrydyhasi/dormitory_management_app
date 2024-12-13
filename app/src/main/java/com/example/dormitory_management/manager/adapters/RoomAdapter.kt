package com.example.dormitory_management.manager.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.RoomModel


class RoomAdapter: RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private val rooms = mutableListOf<RoomModel>()
    private var onApproveClickListener: ((RoomModel) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manager_room, parent, false)
        return RoomViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.txt_name_room.text = room.roomNumber
        holder.txt_name_building.text = room.buildingNumber
        holder.txt_available.text = "${room.available}/4"
        holder.txt_status.text = room.status
        holder.txt_card.setOnClickListener{
            try{
                onApproveClickListener?.invoke(room)

            } catch (e: Exception){
                Log.i("2222", e.toString())
            }
        }

        val color_status: Int = if (room.available?.toInt() == 4)
            ContextCompat.getColor(holder.card_status.context , R.color.waring);
        else
            ContextCompat.getColor(holder.card_status.context, R.color.green);

        holder.card_status.setCardBackgroundColor(color_status)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRooms(room: List<RoomModel>) {
        rooms.clear()
        rooms.addAll(room)
        notifyDataSetChanged()
    }

    override fun getItemCount():  Int = rooms.size

    fun setOnApproveClickListener(listener: (RoomModel) -> Unit) {
        onApproveClickListener = listener
    }

    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_card: CardView = itemView.findViewById(R.id.txt_card)
        val txt_name_room: TextView = itemView.findViewById(R.id.txt_name_room)
        val txt_name_building: TextView = itemView.findViewById(R.id.txt_name_building)
        val txt_available: TextView = itemView.findViewById(R.id.txt_available)
        val card_status: CardView = itemView.findViewById(R.id.card_status)
        val txt_status: TextView = itemView.findViewById(R.id.txt_status)
    }

}