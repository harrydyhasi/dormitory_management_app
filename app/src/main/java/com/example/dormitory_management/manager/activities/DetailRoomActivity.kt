package com.example.dormitory_management.manager.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.adapters.UserAdapter
import com.example.dormitory_management.manager.models.RoomModel
import com.example.dormitory_management.manager.models.UserModel
import com.example.dormitory_management.student.repositories.RoomRepository

class DetailRoomActivity: AppCompatActivity() {
    val key = "DetailRoomActivity"

    //view
    private lateinit var room: RoomModel
    private val roomRepository: RoomRepository = RoomRepository()
    private lateinit var rcv: RecyclerView
    private lateinit var txt_pending: TextView
    private lateinit var txt_avaliable: TextView
    // data
    private lateinit var adapter : UserAdapter
    private var users: MutableList<UserModel> = mutableListOf()

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_room_detail)
        val txt_name: TextView = findViewById(R.id.txt_name_room)
        val txt_building: TextView = findViewById(R.id.txt_name_building)
        txt_avaliable = findViewById(R.id.txt_available)
        txt_pending = findViewById(R.id.txt_pending)

        adapter = UserAdapter()

        rcv = findViewById(R.id.rcv_list_user)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = adapter

        try {
            room = intent.getSerializableExtra("room") as RoomModel

            txt_name.text = room.roomNumber
            txt_building.text = room.buildingNumber

            getListUser()
        } catch (e: Exception){
            Log.i(key, e.toString())
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chi tiết thông tin phòng"
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        // Optional: Add any custom behavior here
        super.onBackPressed()  // This will navigate back to the previous Activity
    }

    private fun getListUser(){
        roomRepository.getListUserByIdRoom(
            room.idRoom.toString(),
            object : MyCallBack<List<UserModel>, String> {
                @SuppressLint("SetTextI18n")
                override fun success(param: List<UserModel>) {
                    users = param.toMutableList()
                    val actives = users.filter { it.status == "Active" }
                    val inactives = users.filter { it.status == "Inactive" }
                    txt_pending.text = "${inactives.size}"
                    txt_avaliable.text = "${actives.size}"
                    loadView(actives)
                }
                override fun fail(param: String) {
                    Log.d(key, "fail getListRoomBy ")
                }
            }
        )
    }

    private fun loadView(users: List<UserModel>){
        adapter.setData(users)
    }

}