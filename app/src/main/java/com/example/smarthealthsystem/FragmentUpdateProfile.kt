import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smarthealthsystem.FragmentProfile
import com.example.smarthealthsystem.R
import com.example.smarthealthsystem.UserData
import com.example.smarthealthsystem.databinding.FragmentUpdateProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentUpdateProfile : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // Retrieve user data from fragment arguments
        val userData = arguments?.getSerializable("userData") as? UserData

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // Fill input fields with current user data
        if (userData != null) {
            val usernameEditText = binding.updateProfileEnterUsername
            val emailEditText = binding.updateProfileEnterEmail
            val passwordEditText = binding.updateProfileEnterPassword
            val confirmPasswordEditText = binding.updateProfileConfirmPassword

            usernameEditText.setText(userData.username)
            emailEditText.setText(userData.email)
            passwordEditText.setText(userData.password)
            confirmPasswordEditText.setText(userData.password)
        }

        binding.updateProfileSaveBtn.setOnClickListener {
            val usernameEditText = binding.updateProfileEnterUsername.text.toString()
            val emailEditText = binding.updateProfileEnterEmail.text.toString()
            val passwordEditText = binding.updateProfileEnterPassword.text.toString()
            val confirmPasswordEditText = binding.updateProfileConfirmPassword.text.toString()

            updateProfile(userData?.userID.toString(), usernameEditText, emailEditText, passwordEditText)
            Toast.makeText(requireContext(), "Profile Updated!", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun updateProfile(userID: String, username: String, email: String, password: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User Information")
        val userData = mapOf("username" to username, "email" to email, "password" to password)
        databaseReference.child(userID).updateChildren(userData).addOnSuccessListener {
            binding.updateProfileEnterUsername.text.clear()
            binding.updateProfileEnterEmail.text.clear()
            binding.updateProfileEnterPassword.text.clear()
            binding.updateProfileConfirmPassword.text.clear()
            Toast.makeText(requireContext(), "User Record Updated!", Toast.LENGTH_SHORT).show()

            // Retrieve updated user data from the database
            databaseReference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val updatedUserData = dataSnapshot.getValue(UserData::class.java)
                    // Update UserViewModel with the updated user data
                    updatedUserData?.let {
                        userViewModel.updateUserData(it)

                        // Navigate to FragmentProfile after updating user record
                        val fragmentProfile = FragmentProfile().apply {
                            arguments = Bundle().apply {
                                putSerializable("userData", it)
                            }
                        }
                        replaceFragment(fragmentProfile)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                }
            })

        }.addOnFailureListener {
            Toast.makeText(requireContext(), "User Record Not Updated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
