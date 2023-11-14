package com.example.smarthealthsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.smarthealthsystem.databinding.ActivityAddUserBinding

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding
    private lateinit var db: UserDatabaseHelper
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabaseHelper(this)

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
            val username = binding.titleEnterUsername.text.toString()
            val email = binding.titleEnterEmail.text.toString()
            val password = binding.titleEnterPassword.text.toString()
            val confirmPassword = binding.titleConfirmPassword.text.toString()
            val role = binding.spinnerRole.selectedItem.toString()
            val user = User(0, username, email, password, role)
            db.addUser(user)
            finish()
            Toast.makeText(this, "User Added!", Toast.LENGTH_SHORT).show()
        }
    }
}