package com.example.smarthealthsystem

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UsersAdapter(private var users: List<UserData>, private val databaseReference: DatabaseReference) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userIdTextView = itemView.findViewById<TextView>(R.id.userIdTextView)
        val usernameTextView = itemView.findViewById<TextView>(R.id.usernameTextView)
        val emailTextView = itemView.findViewById<TextView>(R.id.emailTextView)
        val passwordTextView = itemView.findViewById<TextView>(R.id.passwordTextView)
        val roleTextVieww = itemView.findViewById<TextView>(R.id.roleTextView)
        val updateBtn = itemView.findViewById<ImageView>(R.id.updateUserBtn)
        val deleteBtn = itemView.findViewById<ImageView>(R.id.deleteUserBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userIdTextView.text = "User ID: ${user.userID.toString()}"
        holder.usernameTextView.text = "Username: ${user.username}"
        holder.emailTextView.text = "Email Address: ${user.email}"
        holder.passwordTextView.text = "Password: ${user.password}"
        holder.roleTextVieww.text = "Role: ${user.role}"

        holder.updateBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateUserActivity::class.java).apply {
                putExtra("user_id", user.userID)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener {
            val userKey = user.userID

            // Remove the user from the Firebase Realtime Database
            databaseReference.child(userKey.toString()).removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(holder.itemView.context, "User Deleted!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(holder.itemView.context, "Failed to delete user", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    fun refreshData(newUsers: List<UserData>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
