package com.example.smarthealthsystem

import FragmentUpdateProfile
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class FragmentProfile : Fragment() {

    private var userData: UserData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Retrieve user data from fragment arguments
        userData = arguments?.getSerializable("userData") as? UserData

        val updateButton = view.findViewById<ImageView>(R.id.updateProfileBtn)
        val logOutButton = view.findViewById<Button>(R.id.btnLogOut)

        updateButton.setOnClickListener {
            // Navigate to EditProfileFragment and pass user data
            val editProfileFragment = FragmentUpdateProfile().apply {
                arguments = Bundle().apply {
                    putSerializable("userData", userData)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_container, editProfileFragment)
                .addToBackStack(null)
                .commit()
        }

        logOutButton.setOnClickListener {
            signOut()
            val intent = Intent(requireContext(), ActivityLogin::class.java)
            startActivity(intent)
            requireActivity().finish() // Close the current activity
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update UI with user data
        updateUI(userData)
    }

    internal fun updateUI(updatedUserData: UserData?) {
        if (updatedUserData != null) {
            val usernameTextView = requireView().findViewById<TextView>(R.id.profileUsernameTextView)
            val emailTextView = requireView().findViewById<TextView>(R.id.profileEmailTextView)
            val passwordTextView = requireView().findViewById<TextView>(R.id.profilePasswordTextView)
            usernameTextView.text = "Username: ${updatedUserData.username}"
            emailTextView.text = "Email: ${updatedUserData.email}"
            passwordTextView.text = "Password: ${updatedUserData.password}"
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

}
