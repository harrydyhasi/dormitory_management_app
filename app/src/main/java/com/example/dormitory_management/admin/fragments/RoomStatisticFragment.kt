package com.example.dormitory_management.admin.fragments

import android.R
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dormitory_management.admin.models.RoomModelOut
import com.example.dormitory_management.admin.repositories.AdminRepository
import com.example.dormitory_management.helper.interfaces.MyCallBack
import com.example.dormitory_management.student.models.UserModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class RoomStatisticsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var barChartStatus: BarChart
    private lateinit var pieChart: PieChart
    private lateinit var tvAvailableRooms: TextView
    private lateinit var tvFullRooms: TextView
    private lateinit var tvRoomStats: TextView
    private lateinit var tvUser: TextView
    private lateinit var spinner: Spinner  // Spinner để chọn loại thống kê

    private val adminRepository = AdminRepository()
    private var inactiveUser: Int = 0
    private var activeUser: Int = 0

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Tạo ScrollView để cuộn nội dung
        val rootLayout = ScrollView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Tạo LinearLayout để chứa các thành phần trong ScrollView
        val linearLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        // Tạo Spinner
        val options = listOf("Thống kê trạng thái phòng", "Thống kê loại người dùng", "Thống kê trạng thái người dùng")
        spinner = Spinner(requireContext()).apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)
        }



        // Tạo TextView
        tvAvailableRooms = TextView(requireContext()).apply {
            textSize = 18f
            setTextColor(Color.BLACK)
        }
        tvFullRooms = TextView(requireContext()).apply {
            textSize = 18f
            setTextColor(Color.BLACK)
        }

        tvRoomStats = TextView(requireContext()).apply {
            textSize = 20f
            setTextColor(Color.BLACK)
            gravity = android.view.Gravity.CENTER
        }
        tvUser = TextView(requireContext()).apply {
            textSize = 20f
            setTextColor(Color.BLACK)
            gravity = android.view.Gravity.CENTER
        }

        // Thêm Spinner và TextView vào layout
        linearLayout.addView(spinner)
        linearLayout.addView(tvRoomStats)
        linearLayout.addView(tvAvailableRooms)
        linearLayout.addView(tvFullRooms)

        // Tạo BarChart và PieChart
        barChart = BarChart(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                600
            )
        }

        pieChart = PieChart(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                850
            )
        }

        barChartStatus = BarChart(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                600
            )
        }

        // Thêm BarChart và PieChart vào layout
        linearLayout.addView(barChart)
        linearLayout.addView(tvUser)
        linearLayout.addView(pieChart)
        linearLayout.addView(barChartStatus)


        // Thêm LinearLayout vào ScrollView
        rootLayout.addView(linearLayout)
        return rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Xử lý sự kiện khi người dùng chọn từ Spinner

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> showRoomStats()  // Thống kê phòng
                    1 -> showUserRole()  // Thống kê người dùng
                    2 -> showUserStatus()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Xử lý khi không có mục nào được chọn
            }
        }


        // Lấy dữ liệu thống kê phòng từ repository
        adminRepository.getRoomList(object : MyCallBack<List<RoomModelOut>, String> {
            override fun success(data: List<RoomModelOut>) {
                var availableRooms = 0
                var fullRooms = 0

                // Tính số phòng còn chỗ và hết chỗ
                for (room in data) {
                    val currentOccupancy = room.available ?: 0
                    val maxOccupancy = 4

                    if (currentOccupancy < maxOccupancy) {
                        availableRooms++
                    } else {
                        fullRooms++
                    }
                }

                // Cập nhật biểu đồ BarChart
                updateBarChart(availableRooms,fullRooms, barChart, isRoom = true)

                // Cập nhật PieChart (số lượng người dùng)
                // updatePieChart(availableRooms, fullRooms)
            }
            override fun fail(message: String) {
                // Xử lý khi thất bại
            }
        })

        adminRepository.getUserList(object : MyCallBack<List<UserModel>, String> {
            override fun success(data: List<UserModel>) {
                var adminCount = 0
                var managerCount = 0
                var studentCount = 0

                for (user in data) {
                    when (user.role) {
                        "admin" -> adminCount++
                        "manager" -> managerCount++
                        "student" -> studentCount++
                    }
                    when (user.status)
                    {
                        "Inactive" -> inactiveUser++
                        "Active" -> activeUser++
                    }
                }

                // Cập nhật PieChart cho người dùng
                updateUserPieChart(adminCount, managerCount, studentCount)
                updateBarChart(inactiveUser, activeUser, barChartStatus, isRoom = false)
            }

            override fun fail(message: String) {
                // Xử lý khi lấy danh sách người dùng thất bại
            }
        })
    }

    private fun showRoomStats() {
        tvUser.visibility = View.GONE
        pieChart.visibility = View.GONE
        barChart.visibility = View.VISIBLE
        barChartStatus.visibility = View.GONE
    }

    private fun showUserRole() {
        tvUser.visibility = View.VISIBLE
        pieChart.visibility = View.VISIBLE
        barChart.visibility = View.GONE
        barChartStatus.visibility = View.GONE
    }
    private fun showUserStatus() {
        pieChart.visibility = View.GONE
        barChart.visibility = View.GONE
        barChartStatus.visibility = View.VISIBLE
    }

    private fun updateUserPieChart(adminCount: Int, managerCount: Int, studentCount: Int) {
        val entries = listOf(
            PieEntry(adminCount.toFloat(), "Quản trị viên"),
            PieEntry(managerCount.toFloat(), "Quản lý"),
            PieEntry(studentCount.toFloat(), "Sinh viên")
        )

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(Color.parseColor("#623EBA"), Color.parseColor("#FB9900"), Color.parseColor("#EF3C59"))

        }
        val pieData = PieData(dataSet).apply {
            setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() // Loại bỏ phần thập phân
                }
            })
        }

//        val pieData = PieData(dataSet)

        pieChart.apply {
            data = pieData
            description.isEnabled = false
            holeRadius = 40f
            transparentCircleRadius = 45f
            animateY(1000)
            invalidate()
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(14f)
            val boldTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            setEntryLabelTypeface(boldTypeface)
            data.setValueTypeface(boldTypeface)
            val dataSet = data.dataSet
            if (dataSet is PieDataSet) {
                dataSet.valueTextColor = Color.WHITE
                dataSet.valueTextSize = 18f
            }
            legend.apply {
                textSize = 16f // Tăng kích thước chữ của chú thích lên 16f
                textColor = Color.BLACK
                yOffset = 10f // Tăng khoảng cách chú thích xuống phía dưới
                xEntrySpace = 50f // Tăng khoảng cách giữa các mục chú thích
//                form = com.github.mikephil.charting.components.Legend.LegendForm.SQUARE
//                horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.LEFT
            }
        }

    }
    private fun updateBarChart(firstChart: Int, secondChart: Int, param: BarChart, isRoom: Boolean) {
        var text1 : String = "Còn chỗ"
        var text2 : String = "Hết chỗ"
        if(!isRoom){
            text1 = "Chưa duyệt"
            text2 = "Đã duyệt"
        }



        val entries = listOf(
            BarEntry(0f, firstChart.toFloat()),
            BarEntry(1f, secondChart.toFloat())
        )

        val dataSet = BarDataSet(entries, "").apply {
            colors = listOf(Color.parseColor("#204A9C"), Color.parseColor("#FFD757"))
            valueTextColor = Color.BLACK
            valueTextSize = 14f
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.5f
        }

        param.apply {
            data = barData
            setFitBars(true)
            description.isEnabled = false

            xAxis.apply {
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                textColor = Color.DKGRAY
                textSize = 12f
                valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(
                    listOf("", "")
                )
            }

            axisLeft.apply {
                textColor = Color.DKGRAY
                textSize = 12f
                axisMinimum = 0f
                granularity = 1f
                setDrawGridLines(true)
                gridColor = Color.LTGRAY
            }

            axisRight.isEnabled = false

            legend.apply {
                isEnabled = true
                textSize = 16f
                yOffset = 10f
                textColor = Color.DKGRAY
                form = com.github.mikephil.charting.components.Legend.LegendForm.SQUARE
                horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.LEFT
                xEntrySpace = 90f
                setCustom(
                    listOf(
                        com.github.mikephil.charting.components.LegendEntry(
                            text1,  // Dùng text1 cho trạng thái đầu tiên
                            com.github.mikephil.charting.components.Legend.LegendForm.SQUARE,
                            10f,
                            Float.NaN,
                            null,
                            Color.parseColor("#204A9C")
                        ),
                        com.github.mikephil.charting.components.LegendEntry(
                            text2,  // Dùng text2 cho trạng thái thứ hai
                            com.github.mikephil.charting.components.Legend.LegendForm.SQUARE,
                            10f,
                            Float.NaN,
                            null,
                            Color.parseColor("#FFD757")
                        )
                    )
                )
            }

            animateY(1000)
            invalidate()
        }
    }
}

