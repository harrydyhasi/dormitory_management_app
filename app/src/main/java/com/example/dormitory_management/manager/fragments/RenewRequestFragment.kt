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
import com.example.dormitory_management.manager.models.RenewRequestModel
import com.example.dormitory_management.manager.repositories.RenewRequestRepository
import com.example.dormitory_management.manager.adapters.RenewRequestAdapter

class RenewRequestFragment : Fragment() {
    //
    private lateinit var renewRequestAdapter: RenewRequestAdapter
    private var renewRequest: MutableList<RenewRequestModel> = mutableListOf()

    private val renewRequestRepository:  RenewRequestRepository = RenewRequestRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manager_renew_request, container, false)

        renewRequestAdapter = RenewRequestAdapter()

        val renewRequestList: RecyclerView = view.findViewById(R.id.rcv_renew_request)
        renewRequestList.layoutManager = LinearLayoutManager(context)
        renewRequestList.adapter = renewRequestAdapter

        loadListRenewRequest()

        return view
    }

    private fun onAction(renewRequestModel: RenewRequestModel, status: Boolean){

        renewRequestRepository.changeStatusRenewRequest(renewRequestModel, if (status) "approved" else "rejected",
            object : MyCallBack<String, String> {
                override fun success(param: String) {
                    Log.d("2222", param)
                    loadListRenewRequest()
                }
                override fun fail(param: String) {
                    Log.d("22222", param)
                }
        })
    }

    private fun loadListRenewRequest(){
        renewRequestAdapter.setOnApproveClickListener{
            renewRequest, status -> onAction(renewRequest, status)
        }

        renewRequestRepository.getListRenewRequest(
            object : MyCallBack<List<RenewRequestModel>, String> {
                override fun success(param: List<RenewRequestModel>) {
                    renewRequest = param.filter { it.status == "pending" }.toMutableList()
                    renewRequestAdapter.setRenewRequests(renewRequest)
                }
                override fun fail(param: String) {
                    Log.d("22222", param)
                }
            })
    }
}