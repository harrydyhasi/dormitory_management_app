package com.example.dormitory_management.manager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.dormitory_management.R
import com.example.dormitory_management.auth.LoginActivity
import com.example.dormitory_management.manager.fragments.ApproveReturnRoomRequestFragment
import com.example.dormitory_management.manager.fragments.DormitoryLookupFragment
import com.example.dormitory_management.manager.fragments.ManagerApproveRegistrationFragment
import com.example.dormitory_management.manager.fragments.RenewRequestFragment
import com.example.dormitory_management.manager.fragments.RepairRequestFragment
import com.example.dormitory_management.manager.fragments.RoomManagerFragment
import com.example.dormitory_management.manager.fragments.UpdateElectricWaterBillFragment
import com.example.dormitory_management.student.fragments.InformationStudentFragment

import com.google.android.material.navigation.NavigationView

class ManagerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        drawerLayout = findViewById(R.id.manager_drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.manager_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar.setTitleTextAppearance(this, R.style.SmallToolbarTitle)
        val navigationView = findViewById<NavigationView>(R.id.manager_nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ManagerApproveRegistrationFragment())
                .commit()
            supportActionBar?.title = "Duyệt yêu cầu đăng ký"
            navigationView.setCheckedItem(R.id.nav_manager_approve_registration)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_manager_approve_registration -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ManagerApproveRegistrationFragment())
                    .commit()
                supportActionBar?.title = "Duyệt yêu cầu đăng ký"
            }
            R.id.nav_manager_repair_request -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RepairRequestFragment())
                    .commit()
                supportActionBar?.title = "Yêu cầu sửa chữa"
            }
            R.id.nav_manager_room_info_management -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RoomManagerFragment())
                    .commit()
                supportActionBar?.title = "Quản lý phòng ở"
            }
            R.id.nav_manager_dormitory_lookup -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DormitoryLookupFragment())
                    .commit()
                supportActionBar?.title = "Tra cứu thông tin"
            }
            R.id.nav_manager_update_utility -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, UpdateElectricWaterBillFragment())
                    .commit()
                supportActionBar?.title = "Cập nhật điện nước"
            }
            R.id.nav_manager_renewal -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RenewRequestFragment())
                    .commit()
                supportActionBar?.title = "Gia hạn phòng ở"
            }
            R.id.nav_manager_return_room -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ApproveReturnRoomRequestFragment())
                    .commit()
                supportActionBar?.title = "Yêu cầu trả phòng"
            }

            R.id.nav_manager_logout -> {
                Toast.makeText(this, "Log out Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
