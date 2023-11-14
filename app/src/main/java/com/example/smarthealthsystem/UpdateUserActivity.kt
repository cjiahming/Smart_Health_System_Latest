package com.example.smarthealthsystem

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.smarthealthsystem.databinding.ActivityUpdateUserBinding

class UpdateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserBinding
    private lateinit var db: UserDatabaseHelper
    private var userID: Int = -1
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabaseHelper(this)

        var roleValue: Int
        spinner = binding.updateSpinnerRole
        val listItems = listOf("User", "Admin")
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, listItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        userID = intent.getIntExtra("user_id", -1)
        if(userID == -1){
            finish()
            return
        }

        val user = db.getUserByID(userID)

        //for set value into spinner based on selected userID
        if(user.role == "User"){
            roleValue = 0
        }
        else{
            roleValue = 1
        }
        binding.updateTitleEnterUsername.setText(user.username)
        binding.updateTitleEnterEmail.setText(user.email)
        binding.updateTitleEnterPassword.setText(user.password)
        binding.updateSpinnerRole.setSelection(roleValue)

        binding.updateSaveBtn.setOnClickListener {
            val newUsername = binding.updateTitleEnterUsername.text.toString()
            val newEmail = binding.updateTitleEnterEmail.text.toString()
            val newPassword = binding.updateTitleEnterPassword.text.toString()
            val newRole = binding.updateSpinnerRole.selectedItem.toString()
            val updatedUser = User(userID, newUsername, newEmail, newPassword, newRole)
            db.updateUser(updatedUser)
            finish()
            Toast.makeText(this, "User Record Updated!", Toast.LENGTH_SHORT).show()
        }

    }
}