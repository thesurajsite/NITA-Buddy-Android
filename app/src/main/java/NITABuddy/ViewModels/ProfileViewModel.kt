package NITABuddy.ViewModels

import NITABuddy.Authentication.SignupResponseDataClass
import NITABuddy.DataClass.UserDataClass
import NITABuddy.DataClass.UserProfileResponseDataClass
import NITABuddy.Retrofit.RetrofitService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel(): ViewModel(){

    private val _userProfileResponse = MutableLiveData<UserProfileResponseDataClass>()
    val userProfileResponse: LiveData<UserProfileResponseDataClass> get() = _userProfileResponse

    fun getUserProfile(retrofitService: RetrofitService, token: String){
        viewModelScope.launch {
            try {
                val response  = retrofitService.getUserProfile("Bearer $token")
                if(response.isSuccessful){
                    val userProfileResponse = response.body()
                    _userProfileResponse.postValue(userProfileResponse!!)
                }else{
                    _userProfileResponse.postValue(UserProfileResponseDataClass(false, "Server Error", UserDataClass()))
                }
            }
            catch (e: Exception){
                _userProfileResponse.postValue(UserProfileResponseDataClass(false, "Error: ${e.localizedMessage}", UserDataClass()))
            }
        }
    }

}