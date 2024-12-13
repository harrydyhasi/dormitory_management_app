package com.example.dormitory_management.student.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dormitory_management.R
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.adapters.RenewRequestAdapter
import com.example.dormitory_management.student.models.RenewRequestModel
import com.example.dormitory_management.student.repositories.RenewRequestRepository
import com.example.dormitory_management.student.repositories.UserRepository
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class RenewRequestFragment : Fragment() {

    private lateinit var renewRequestAdapter: RenewRequestAdapter
    private lateinit var renewRequestRepository: RenewRequestRepository
    private var userId: String? = null  // Declare a variable to hold the userId
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    companion object {
        private const val USER_ID_KEY = "USER_ID"

        fun newInstance(userId: String): RenewRequestFragment {
            val fragment = RenewRequestFragment()
            val args = Bundle()
            args.putString(USER_ID_KEY, userId)  // Pass userId to the fragment
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_renew_request, container, false)

        renewRequestRepository = RenewRequestRepository()
        renewRequestAdapter = RenewRequestAdapter()

        val renewRequestList: RecyclerView = view.findViewById(R.id.renew_request_list)
        renewRequestList.layoutManager = LinearLayoutManager(context)
        renewRequestList.adapter = renewRequestAdapter

        val createNewRequestButton: Button = view.findViewById(R.id.btn_create_new_request)
        createNewRequestButton.setOnClickListener {
            // Fetch the latest endDate of the user
            if (userId != null) {
                val userRepository = UserRepository()  // Initialize the UserRepository
                userRepository.getEndDate(userId!!, object : MyCallBack<Date, String> {
                    override fun success(param: Date) {
                        showCreateRequestDialog(param)
                    }

                    override fun fail(param: String) {
                        // If there is an error, show the dialog without latestEndDate
                        showCreateRequestDialog(null)
                        Toast.makeText(context, param, Toast.LENGTH_SHORT).show() // Display error message
                    }
                })
            }
        }


        // Retrieve userId from arguments
        userId = arguments?.getString(USER_ID_KEY)

        if (userId != null) {
            loadRenewRequests()  // Only load requests if userId is available
        } else {
            Toast.makeText(context, "User ID is missing", Toast.LENGTH_SHORT).show()
        }

        return view
    }


    private fun showCreateRequestDialog(latestEndDate: Date?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_student_renew_request_form, null)

        val startDateTextView: TextView = dialogView.findViewById(R.id.txt_selected_start_date)
        val endDateTextView: TextView = dialogView.findViewById(R.id.txt_selected_end_date)
        val datePickerButton: ImageButton = dialogView.findViewById(R.id.btn_date_picker)
        val submitButton: Button = dialogView.findViewById(R.id.btn_submit)

        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = builder.create()

        // Handle the date picker button
        datePickerButton.setOnClickListener {
            showDateRangePicker(latestEndDate) { start, end ->
                startDate = start
                endDate = end

                startDateTextView.text = dateFormat.format(startDate)
                endDateTextView.text = dateFormat.format(endDate)
            }
        }

        // Handle the submit button
        submitButton.setOnClickListener {
            val startDateText = startDateTextView.text.toString()
            val endDateText = endDateTextView.text.toString()

            if (startDateText == "Từ ngày" || endDateText == "Đến ngày") {
                Toast.makeText(context, "Vui lòng chọn ngày bắt đầu và ngày kết thúc", Toast.LENGTH_SHORT).show()
            } else {
                val start = dateFormat.parse(startDateText)
                val end = dateFormat.parse(endDateText)

                if (start != null && end != null) {
                    if (start.after(end)) {
                        Toast.makeText(context, "Ngày bắt đầu phải trước ngày kết thúc", Toast.LENGTH_SHORT).show()
                    } else {
                        if (latestEndDate != null && start.before(latestEndDate)) {
                            Toast.makeText(context, "Ngày bắt đầu phải sau ngày kết thúc sinh viên đã đăng ký trước đó.", Toast.LENGTH_SHORT).show()
                        } else {
                            addRenewRequest(startDate, endDate, "pending")
                            alertDialog.dismiss()
                        }
                    }
                } else {
                    Toast.makeText(context, "Lỗi định dạng ngày", Toast.LENGTH_SHORT).show()
                }
            }
        }

        alertDialog.show()
    }




    private fun showDateRangePicker(latestEndDate: Date?, onDateRangeSelected: (Date, Date) -> Unit) {
        val constraintsBuilder = CalendarConstraints.Builder()

        // If latestEndDate is not null, set it as the minimum start date
        if (latestEndDate != null) {
            constraintsBuilder.setStart(latestEndDate.time) // Restrict the start date after the latest end date
        }

        // Optional: Set an end date constraint (e.g., 5 years from now)
        val fiveYearsFromNow = Calendar.getInstance()
        fiveYearsFromNow.add(Calendar.YEAR, 5)
        constraintsBuilder.setEnd(fiveYearsFromNow.timeInMillis)

        val constraints = constraintsBuilder.build()

        // Build the date range picker
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Chọn khoảng thời gian")
            .setCalendarConstraints(constraints)
            .build()

        // Handle date range selection
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startTimestamp = selection.first
            val endTimestamp = selection.second

            val startDate = Date(startTimestamp - TimeZone.getDefault().rawOffset)
            val endDate = Date(endTimestamp - TimeZone.getDefault().rawOffset)

            onDateRangeSelected(startDate, endDate)
        }

        // Show the date range picker
        dateRangePicker.show(childFragmentManager, "date_range_picker")
    }



    private fun addRenewRequest(startDate: Date, endDate: Date, status: String) {
        if (userId != null) {
            renewRequestRepository.addRenewRequest(userId!!, startDate, endDate, status, object :
                MyCallBack<Unit, String> {
                override fun success(data: Unit) {
                    Toast.makeText(context, "Yêu cầu gia hạn đã được gửi", Toast.LENGTH_SHORT).show()
                    loadRenewRequests()
                }

                override fun fail(error: String) {
                    Toast.makeText(context, "Lỗi khi gửi yêu cầu: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun loadRenewRequests() {
        if (userId != null) {
            renewRequestRepository.getRenewRequests(userId!!, object :
                MyCallBack<List<RenewRequestModel>, String> {
                override fun success(data: List<RenewRequestModel>) {
                    renewRequestAdapter.setRenewRequests(data)
                }

                override fun fail(error: String) {
                    Toast.makeText(context, "Lỗi khi tải yêu cầu: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
