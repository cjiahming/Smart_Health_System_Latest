package com.example.smarthealthsystem

import android.content.Context
import android.content.Intent
import android.media.Image
import android.provider.ContactsContract
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class UsersAdapter(private var users: List<User>, context: Context) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    private val db: UserDatabaseHelper = UserDatabaseHelper(context)

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
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
            val intent = Intent(holder.itemView.context, UpdateUserActivity::class.java).apply{
                putExtra("user_id", user.userID)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener {
            db.deleteUser(user.userID)
            refreshData(db.getAllUser())
            Toast.makeText(holder.itemView.context, "User Deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshData(newUsers: List<User>){
        users = newUsers
        notifyDataSetChanged()
    }
}