package NITABuddy.ViewModels

import NITABuddy.DataClass.CreateOrderResponseDataClass
import NITABuddy.DataClass.FetchRewardsResponseDataClass
import NITABuddy.Retrofit.RetrofitService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class RewardsViewModel() : ViewModel() {

    private val _fetchRewardsResponse = MutableLiveData<FetchRewardsResponseDataClass>()
    val fetchRewardsResponse: LiveData<FetchRewardsResponseDataClass> get() = _fetchRewardsResponse

    fun fetchRewards(retrofitService: RetrofitService, token: String){
        viewModelScope.launch {
            try {
                val response = retrofitService.fetchRewards(token)
                if(response.isSuccessful){
                    val rewardResponse = response.body()
                    _fetchRewardsResponse.postValue(rewardResponse!!)
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

                    _fetchRewardsResponse.postValue(FetchRewardsResponseDataClass(false, errorMessage, 0))
                }
            }
            catch (e: Exception){
                _fetchRewardsResponse.postValue(FetchRewardsResponseDataClass(false, "Error: ${e.localizedMessage}", 0))
            }
        }
    }
}