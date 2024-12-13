package com.example.dormitory_management.admin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.admin.models.RoomModelOut
import com.example.dormitory_management.student.models.UserModel

class RoomAdapter(
    private val roomList: MutableList<RoomModelOut>,
    private val onEdit: (RoomModelOut) -> Unit,
    private val onDelete: (RoomModelOut) -> Unit,
    private val studentList: List<UserModel>
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoomNumber: TextView = itemView.findViewById(R.id.tv_room_number)
        val tvBuildingNumber: TextView = itemView.findViewById(R.id.tv_building_number)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvAvailable: TextView = itemView.findViewById(R.id.tv_available)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnDelete: Button = itemView.findViewById(R.id.btn_delete)

        val tvStudentName1: TextView = itemView.findViewById(R.id.tv_student_name_1)
        val tvStudentPhone1: TextView = itemView.findViewById(R.id.tv_student_phone_1)

        val tvStudentName2: TextView = itemView.findViewById(R.id.tv_student_name_2)
        val tvStudentPhone2: TextView = itemView.findViewById(R.id.tv_student_phone_2)

        val tvStudentName3: TextView = itemView.findViewById(R.id.tv_student_name_3)
        val tvStudentPhone3: TextView = itemView.findViewById(R.id.tv_student_phone_3)

        val tvStudentName4: TextView = itemView.findViewById(R.id.tv_student_name_4)
        val tvStudentPhone4: TextView = itemView.findViewById(R.id.tv_student_phone_4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = roomList[position]
        holder.tvRoomNumber.text = room.roomNumber
        holder.tvBuildingNumber.text = room.buildingNumber
        holder.tvPrice.text = "${room.price} VND"
        holder.tvStatus.text = room.status
        holder.tvAvailable.text = room.available.toString()

        holder.btnEdit.setOnClickListener {
            onEdit(room)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(room)
        }

        val studentsInRoom = studentList.filter { it.idRoom == room.id }

        // Hiển thị thông tin sinh viên
        when (studentsInRoom.size) {
            1 -> {
                holder.tvStudentName1.text = studentsInRoom[0].fullName
                holder.tvStudentPhone1.text = studentsInRoom[0].phoneNumber
            }
            2 -> {
                holder.tvStudentName1.text = studentsInRoom[0].fullName
                holder.tvStudentPhone1.text = studentsInRoom[0].phoneNumber
                holder.tvStudentName2.text = studentsInRoom[1].fullName
                holder.tvStudentPhone2.text = studentsInRoom[1].phoneNumber
            }
            3 -> {
                holder.tvStudentName1.text = studentsInRoom[0].fullName
                holder.tvStudentPhone1.text = studentsInRoom[0].phoneNumber
                holder.tvStudentName2.text = studentsInRoom[1].fullName
                holder.tvStudentPhone2.text = studentsInRoom[1].phoneNumber
                holder.tvStudentName3.text = studentsInRoom[2].fullName
                holder.tvStudentPhone3.text = studentsInRoom[2].phoneNumber
            }
            4 -> {
                holder.tvStudentName1.text = studentsInRoom[0].fullName
                holder.tvStudentPhone1.text = studentsInRoom[0].phoneNumber
                holder.tvStudentName2.text = studentsInRoom[1].fullName
                holder.tvStudentPhone2.text = studentsInRoom[1].phoneNumber
                holder.tvStudentName3.text = studentsInRoom[2].fullName
                holder.tvStudentPhone3.text = studentsInRoom[2].phoneNumber
                holder.tvStudentName4.text = studentsInRoom[3].fullName
                holder.tvStudentPhone4.text = studentsInRoom[3].phoneNumber
            }
        }
    }

    override fun getItemCount(): Int = roomList.size
}