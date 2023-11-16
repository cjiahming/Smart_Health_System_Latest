package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smarthealthsystem.databinding.ActivityCrudMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*

class ActivityCrudMain : AppCompatActivity() {

    private lateinit var binding: ActivityCrudMainBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("User Information")

        usersAdapter = UsersAdapter(emptyList(), databaseReference)

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.usersRecyclerView.adapter = usersAdapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        binding.adminLogOutBtn.setOnClickListener {
            signOut()
            val intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish() // Close the current activity
        }

    }

    override fun onResume() {
        super.onResume()
        readData()
    }

    private fun readData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserData>()
                for (userSnapshot in snapshot.children) {
                    val userData = userSnapshot.getValue(UserData::class.java)
                    userData?.let { userList.add(it) }
                }
                usersAdapter.refreshData(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}
