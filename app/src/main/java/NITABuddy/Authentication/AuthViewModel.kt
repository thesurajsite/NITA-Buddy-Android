package NITABuddy.Authentication

import NITABuddy.Retrofit.RetrofitService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.sign

class AuthViewModel(private val retrofitService: RetrofitService) : ViewModel() {

    private val _signupResponse = MutableLiveData<SignupResponseDataClass>()
    val signupResponse: LiveData<SignupResponseDataClass> get() = _signupResponse

    fun Signup(signupDetails: SignupRequestDataClass){


        viewModelScope.launch {
            try {
                val request = signupDetails
                val response = retrofitService.Signup(request)

                if(response.isSuccessful){
                    val signupResponse = response.body()
                    _signupResponse.postValue(signupResponse!!)
                }
                else{
                    _signupResponse.postValue(SignupResponseDataClass(false, "Server Error", ""))
                }
            }
            catch(e:Exception){
                _signupResponse.postValue(SignupResponseDataClass(false, "Error: ${e.localizedMessage}", ""))
            }
        }
    }

}