package com.example.dormitory_management.manager.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.manager.models.UserModel
import com.example.dormitory_management.manager.repositories.LookupStudentRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.adapters.LookupStudentAdapter
import android.widget.EditText
import com.example.dormitory_management.manager.models.RoomModel

class DormitoryLookupFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LookupStudentAdapter
    private lateinit var userList: List<UserModel>
    private lateinit var lookupStudentRepository: LookupStudentRepository
    private lateinit var searchEditText: EditText
    private var roomId: String? = null
    private val roomMap = mutableMapOf<String, RoomModel>()
    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_manager_dormitory_lookup, container, false)
        progressDialog = ProgressDialog(context).apply {
            setCancelable(false)
        }
        recyclerView = rootView.findViewById(R.id.rcv_liststudent)
        recyclerView.layoutManager = LinearLayoutManager(context)

        searchEditText = rootView.findViewById(R.id.edt_search)

        lookupStudentRepository = LookupStudentRepository()
        progressDialog.show()
        lookupStudentRepository.getListUser(object : MyCallBack<List<UserModel>, String> {
            override fun success(param: List<UserModel>) {
                userList = param
                if (userList.isNotEmpty()) {
                    for (user in userList) {
                        val idRoom = user.idRoom
                        if (!idRoom.isNullOrEmpty()) {
                            lookupStudentRepository.getRoomById(idRoom, object : MyCallBack<RoomModel, String> {
                                override fun success(param: RoomModel) {
                                    roomMap[idRoom] = param
                                    adapter.notifyDataSetChanged()
                                    progressDialog.dismiss()
                                }

                                override fun fail(param: String) {
                                    Log.d("RoomLookup", "Không thể lấy thông tin phòng: $param")
                                    progressDialog.dismiss()
                                }
                            })
                        }
                    }
                }
                adapter = LookupStudentAdapter(userList, roomMap)
                recyclerView.adapter = adapter
            }

            override fun fail(param: String) {
                Log.d("fail", "fail getListUser")
                progressDialog.dismiss()
            }
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().toLowerCase()
                filterList(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return rootView
    }

    private fun filterList(query: String) {
        val filteredList = userList.filter { user ->
            user.fullName?.toLowerCase()?.contains(query) == true ||
            user.phoneNumber?.toLowerCase()?.contains(query) == true ||
            user.cccd?.toLowerCase()?.contains(query) == true ||
            user.email?.toLowerCase()?.contains(query) == true ||
            roomMap[user.idRoom]?.buildingNumber?.toLowerCase()?.contains(query) == true ||
            roomMap[user.idRoom]?.roomNumber?.toLowerCase()?.contains(query) == true
        }
        adapter = LookupStudentAdapter(filteredList, roomMap)
        recyclerView.adapter = adapter
    }
}
