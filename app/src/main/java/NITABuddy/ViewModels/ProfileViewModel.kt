package NITABuddy.ViewModels

import NITABuddy.DataClass.UserDataClass
import NITABuddy.DataClass.UserProfileResponseDataClass
import NITABuddy.Retrofit.RetrofitService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileViewModel(): ViewModel(){

    private val _userProfileResponse = MutableLiveData<UserProfileResponseDataClass>()
    val userProfileResponse: LiveData<UserProfileResponseDataClass> get() = _userProfileResponse

    fun getUserProfile(retrofitService: RetrofitService, token: String) {
        viewModelScope.launch {
            try {
                val response = retrofitService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    val userProfileResponse = response.body()
                    _userProfileResponse.postValue(userProfileResponse!!)
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

                    _userProfileResponse.postValue(UserProfileResponseDataClass(false, errorMessage, UserDataClass()))
                }
            } catch (e: Exception) {
                _userProfileResponse.postValue(UserProfileResponseDataClass(false, "Error: ${e.localizedMessage}", UserDataClass()))
            }
        }
    }

    fun getUserProfileFromID(retrofitService: RetrofitService, userID: String) {
        viewModelScope.launch {
            try {
                val response = retrofitService.getUserProfileFromID(userID)
                if (response.isSuccessful) {
                    val userProfileResponse = response.body()
                    _userProfileResponse.postValue(userProfileResponse!!)
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

                    _userProfileResponse.postValue(UserProfileResponseDataClass(false, errorMessage, UserDataClass()))
                }
            } catch (e: Exception) {
                _userProfileResponse.postValue(UserProfileResponseDataClass(false, "Error: ${e.localizedMessage}", UserDataClass()))
            }
        }
    }


}