package NITABuddy.Authentication

import NITABuddy.Retrofit.RetrofitService
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class AuthViewModel(private val retrofitService: RetrofitService) : ViewModel() {

    private val _signupResponse = MutableLiveData<SignupResponseDataClass>()
    val signupResponse: LiveData<SignupResponseDataClass> get() = _signupResponse

    private val _loginResponse = MutableLiveData<LoginResponseDataClass>()
    val loginResponse: LiveData<LoginResponseDataClass> get() = _loginResponse

    fun Signup(signupDetails: SignupRequestDataClass) {
        viewModelScope.launch {
            try {
                val request = signupDetails
                val response = retrofitService.Signup(request)

                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    _signupResponse.postValue(signupResponse!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    var errorMessage = "Unknown error"

                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val jsonObject = JSONObject(errorBody)
                            errorMessage = jsonObject.optString("message")
                        } catch (e: Exception) {
                            errorMessage = "Failed to parse error response"
                        }
                    }

                    _signupResponse.postValue(SignupResponseDataClass(false, errorMessage, ""))
                }
            } catch (e: Exception) {
                _signupResponse.postValue(SignupResponseDataClass(false, "Error: ${e.localizedMessage}", ""))
            }
        }
    }


    fun Login(loginDetails: LoginRequestDataClass){
        viewModelScope.launch {
            try {
                val request = loginDetails
                val response = retrofitService.Login(request)

                if(response.isSuccessful){
                    val loginResponse = response.body()
                    _loginResponse.postValue(loginResponse!!)
                }
                else {
                    val errorBody = response.errorBody()?.string()
                    var errorMessage = "Unknown error"

                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            val jsonObject = JSONObject(errorBody)
                            errorMessage = jsonObject.optString("message")
                        } catch (e: Exception) {
                            errorMessage = "Failed to parse error response"
                        }
                    }

                    _loginResponse.postValue(LoginResponseDataClass(false, errorMessage, ""))
                }
            }
            catch(e: Exception){
                _loginResponse.postValue(LoginResponseDataClass(false, "Error: ${e.localizedMessage}", ""))
            }
        }
    }

}