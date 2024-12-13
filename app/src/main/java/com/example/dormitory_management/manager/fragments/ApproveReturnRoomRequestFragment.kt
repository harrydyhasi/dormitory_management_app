package com.example.dormitory_management.manager.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.manager.adapters.ReturnRoomRequestAdapter
import com.example.dormitory_management.manager.models.ReturnRoomRequestModel
import com.example.dormitory_management.manager.repositories.ReturnRoomRequestRepository

class ApproveReturnRoomRequestFragment : Fragment() {

    private lateinit var returnRoomRequestAdapter: ReturnRoomRequestAdapter
    private var returnRoomRequests: MutableList<ReturnRoomRequestModel> = mutableListOf()

    private val returnRoomRequestRepository: ReturnRoomRequestRepository =
        ReturnRoomRequestRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manager_approve_return_room_request, container, false)

        returnRoomRequestAdapter = ReturnRoomRequestAdapter()

        val returnRoomRequestList: RecyclerView =
            view.findViewById(R.id.rcv_return_room_request)
        returnRoomRequestList.layoutManager = LinearLayoutManager(context)
        returnRoomRequestList.adapter = returnRoomRequestAdapter

        loadListReturnRoomRequests()

        return view
    }

    private fun onAction(returnRoomRequest: ReturnRoomRequestModel, status: Boolean) {
        returnRoomRequestRepository.changeStatusReturnRoomRequest(
            returnRoomRequest,
            if (status) "approved" else "rejected",
            object : MyCallBack<String, String> {
                override fun success(param: String) {
                    Log.d("APPROVE_ACTION", param)
                    loadListReturnRoomRequests()
                }

                override fun fail(param: String) {
                    Log.e("APPROVE_ACTION", param)
                }
            }
        )
    }

    private fun loadListReturnRoomRequests() {
        returnRoomRequestAdapter.setOnApproveClickListener { request, status ->
            onAction(request, status)
        }

        returnRoomRequestRepository.getListReturnRoomRequests(
            object : MyCallBack<List<ReturnRoomRequestModel>, String> {
                override fun success(param: List<ReturnRoomRequestModel>) {
                    returnRoomRequests = param.filter { it.status == "pending" }.toMutableList()
                    returnRoomRequestAdapter.setReturnRoomRequests(returnRoomRequests)
                }

                override fun fail(param: String) {
                    Log.e("LOAD_REQUESTS", param)
                }
            }
        )
    }
}
