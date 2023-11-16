import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smarthealthsystem.UserData

class UserViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData

    fun updateUserData(updatedUserData: UserData) {
        _userData.value = updatedUserData
    }
}
