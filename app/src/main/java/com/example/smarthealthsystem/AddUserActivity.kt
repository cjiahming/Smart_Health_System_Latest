package com.example.smarthealthsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.smarthealthsystem.databinding.ActivityAddUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinnerRole
        val listItems = listOf("User", "Admin")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@AddUserActivity, "You have selected $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.saveBtn.setOnClickListener {
            val userID = "U005"
            val username = binding.titleEnterUsername.text.toString()
            val email = binding.titleEnterEmail.text.toString()
            val password = binding.titleEnterPassword.text.toString()
            val confirmPassword = binding.titleConfirmPassword.text.toString()
            val role = binding.spinnerRole.selectedItem.toString()

            databaseReference = FirebaseDatabase.getInstance().getReference("User Information")
            val userData = UserData(userID, username, email, password, role)
            databaseReference.child(userID).setValue(userData).addOnSuccessListener {
                binding.titleEnterUsername.text.clear()
                binding.titleEnterEmail.text.clear()
                binding.titleEnterPassword.text.clear()
                binding.titleConfirmPassword.text.clear()
                binding.spinnerRole.selectedItem.equals(0)

                Toast.makeText(this, "User Added!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AddUserActivity, ActivityCrudMain::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "User Not Added!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}