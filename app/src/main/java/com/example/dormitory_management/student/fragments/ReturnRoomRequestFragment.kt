package com.example.dormitory_management.student.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.adapters.ReturnRoomRequestAdapter
import com.example.dormitory_management.student.models.ReturnRoomRequestModel
import com.example.dormitory_management.student.repositories.ReturnRoomRequestRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReturnRoomRequestFragment : Fragment() {

    private lateinit var returnRoomRequestAdapter: ReturnRoomRequestAdapter
    private lateinit var returnRoomRequestRepository: ReturnRoomRequestRepository
    private var userId: String? = null  // Declare a variable to hold the userId

    companion object {
        private const val USER_ID_KEY = "USER_ID"

        fun newInstance(userId: String): ReturnRoomRequestFragment {
            val fragment = ReturnRoomRequestFragment()
            val args = Bundle()
            args.putString(USER_ID_KEY, userId) // Pass userId to the fragment
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_return_room_request, container, false)

        returnRoomRequestRepository = ReturnRoomRequestRepository()
        returnRoomRequestAdapter = ReturnRoomRequestAdapter()

        val returnRoomRequestList: RecyclerView = view.findViewById(R.id.return_room_request_list)
        returnRoomRequestList.layoutManager = LinearLayoutManager(context)
        returnRoomRequestList.adapter = returnRoomRequestAdapter

        val createNewRequestButton: Button = view.findViewById(R.id.btn_create_new_request)
        createNewRequestButton.setOnClickListener {
            showReturnRoomRequestDialog()
        }

        // Retrieve userId from arguments
        userId = arguments?.getString(USER_ID_KEY)

        if (userId != null) {
            loadReturnRoomRequests()  // Only load requests if userId is available
        } else {
            Toast.makeText(context, "User ID is missing", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showReturnRoomRequestDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_student_return_room_request_form, null)
        val selectedDateTextView: TextView = dialogView.findViewById(R.id.txt_return_date)
        val datePickerButton: ImageButton = dialogView.findViewById(R.id.btn_date_picker)
        val submitButton: Button = dialogView.findViewById(R.id.btn_submit)

        val calendar = Calendar.getInstance()
        datePickerButton.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                selectedDateTextView.text = dateFormat.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        submitButton.setOnClickListener {
            val selectedDate = selectedDateTextView.text.toString()

            if (selectedDate == "Chọn ngày trả phòng") {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = dateFormat.parse(selectedDate)

                if (date != null) {
                    addReturnRoomRequest(date, "pending")
                    selectedDateTextView.text = "Chọn ngày trả phòng"
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(context, "Lỗi định dạng ngày", Toast.LENGTH_SHORT).show()
                }
            }
        }

        alertDialog.show()
    }

    private fun addReturnRoomRequest(date: Date, status: String) {
        if (userId != null) {
            returnRoomRequestRepository.addReturnRoomRequest(userId!!, date, status, object :
                MyCallBack<Unit, String> {
                override fun success(data: Unit) {
                    Toast.makeText(context, "Yêu cầu trả phòng đã được gửi", Toast.LENGTH_SHORT).show()
                    loadReturnRoomRequests()
                }

                override fun fail(error: String) {
                    Toast.makeText(context, "Lỗi khi gửi yêu cầu: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun loadReturnRoomRequests() {
        if (userId != null) {
            returnRoomRequestRepository.getReturnRoomRequests(userId!!, object :
                MyCallBack<List<ReturnRoomRequestModel>, String> {
                override fun success(data: List<ReturnRoomRequestModel>) {
                    returnRoomRequestAdapter.setReturnRoomRequests(data)
                }

                override fun fail(error: String) {
                    Toast.makeText(context, "Lỗi khi tải yêu cầu: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
