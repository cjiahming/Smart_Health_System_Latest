package com.example.smarthealthsystem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthealthsystem.databinding.ActivityUpdateUserBinding
import com.google.firebase.database.*

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserBinding
    private lateinit var databaseReference: DatabaseReference

    private var userID: String? = null
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("User Information")

        var roleValue: Int
        spinner = binding.updateSpinnerRole
        val listItems = listOf("User", "Admin")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }

        userID = intent.getStringExtra("user_id")
        if (userID == null) {
            finish()
            return
        }

        // Retrieve user details from Firebase based on userID
        readData()

        // Set value into spinner based on selected userID
        // (This will be done inside onDataChange callback)

        binding.updateSaveBtn.setOnClickListener {
            val userID = binding.disabledUserIDText.text.toString()
            val newUsername = binding.updateTitleEnterUsername.text.toString()
            val newEmail = binding.updateTitleEnterEmail.text.toString()
            val newPassword = binding.updateTitleEnterPassword.text.toString()
            val confirmPassword = binding.updateTitleConfirmPassword.text.toString()
            val newRole = binding.updateSpinnerRole.selectedItem.toString()

            updateUser(userID, newUsername, newEmail, newPassword, newRole)

            Toast.makeText(this, "User Record Updated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData() {
        userID?.let { uid ->
            databaseReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(UserData::class.java)
                        user?.let {
                            // Set value into spinner based on selected userID
                            if (it.role == "User") {
                                binding.updateSpinnerRole.setSelection(0)
                            } else {
                                binding.updateSpinnerRole.setSelection(1)
                            }

                            // Set other user details to corresponding fields
                            binding.disabledUserIDText.setText(it.userID)
                            binding.updateTitleEnterUsername.setText(it.username)
                            binding.updateTitleEnterEmail.setText(it.email)
                            binding.updateTitleEnterPassword.setText(it.password)
                            binding.updateTitleConfirmPassword.setText(it.password)
                        }
                    } else {
                        // Handle case where user data doesn't exist
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        } ?: run {
            // Handle case where userID is null
        }
    }

    private fun updateUser(userID: String, username: String, email: String, password: String, role: String){
        databaseReference = FirebaseDatabase.getInstance().getReference("User Information")
        val userData = mapOf<String, String>("username" to username, "email" to email, "password" to password, "role" to role)
        databaseReference.child(userID).updateChildren(userData).addOnSuccessListener {
            binding.updateTitleEnterUsername.text.clear()
            binding.updateTitleEnterEmail.text.clear()
            binding.updateTitleEnterPassword.text.clear()
            binding.updateSpinnerRole.selectedItem.equals(0)
            Toast.makeText(this, "User Record Updated!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityCrudMain::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this, "User Record Not Updated!", Toast.LENGTH_SHORT).show()
        }
    }
}
