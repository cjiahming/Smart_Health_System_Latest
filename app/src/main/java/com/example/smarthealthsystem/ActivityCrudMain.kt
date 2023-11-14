package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthealthsystem.databinding.ActivityCrudMainBinding
import com.example.smarthealthsystem.databinding.ActivityMainBinding

class ActivityCrudMain : AppCompatActivity() {

    private lateinit var binding: ActivityCrudMainBinding
    private lateinit var db: UserDatabaseHelper
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabaseHelper(this)
        usersAdapter = UsersAdapter(db.getAllUser(), this)

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.usersRecyclerView.adapter = usersAdapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        usersAdapter.refreshData(db.getAllUser())
    }
}
