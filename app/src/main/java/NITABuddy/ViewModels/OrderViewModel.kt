package NITABuddy.ViewModels

import NITABuddy.DataClass.AcceptOrderResponseDataClass
import NITABuddy.DataClass.AllOrderResponseDataClass
import NITABuddy.DataClass.CancelMyOrderResponseDataClass
import NITABuddy.DataClass.CreateOrderRequestDataClass
import NITABuddy.DataClass.CreateOrderResponseDataClass
import NITABuddy.DataClass.MyOrderResponseDataClass
import NITABuddy.DataClass.OrderDataClass
import NITABuddy.Retrofit.RetrofitService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class OrderViewModel() : ViewModel() {

    private val _createOrderResponse = MutableLiveData<CreateOrderResponseDataClass>()
    val createOrderResponse: LiveData<CreateOrderResponseDataClass> get() = _createOrderResponse

    private val _myOrdersResponse = MutableLiveData<MyOrderResponseDataClass>()
    val myOrdersResponse: LiveData<MyOrderResponseDataClass> get() = _myOrdersResponse

    private val _cancelMyOrderResponse = MutableLiveData<CancelMyOrderResponseDataClass>()
    val cancelMyOrderResponse: LiveData<CancelMyOrderResponseDataClass> get() = _cancelMyOrderResponse

    private val _allOrderResponse = MutableLiveData<AllOrderResponseDataClass>()
    val allOrderResponse: LiveData<AllOrderResponseDataClass> get() = _allOrderResponse

    private val _acceptOrderResponse = MutableLiveData<AcceptOrderResponseDataClass>()
    val acceptOrderResponse: LiveData<AcceptOrderResponseDataClass> get() = _acceptOrderResponse



    fun placeOrder(retrofitService: RetrofitService, token: String, order: CreateOrderRequestDataClass) {
        viewModelScope.launch {
            try {
                val response = retrofitService.placeOrder(token, order)

                if (response.isSuccessful) {
                    val placeOrderResponse = response.body()
                    _createOrderResponse.postValue(placeOrderResponse!!)
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

                    _createOrderResponse.postValue(CreateOrderResponseDataClass(errorMessage, false))
                }
            } catch (e: Exception) {
                _createOrderResponse.postValue(CreateOrderResponseDataClass("Error: ${e.localizedMessage}", false))
            }
        }
    }


    fun fetchMyOrders(retrofitService: RetrofitService, token: String) {
        viewModelScope.launch {
            try {
                val response = retrofitService.fetchMyOrders(token)
                if (response.isSuccessful) {
                    val myOrdersResponse = response.body()
                    _myOrdersResponse.postValue(myOrdersResponse!!)
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

                    _myOrdersResponse.postValue(MyOrderResponseDataClass(false, errorMessage, emptyList()))
                }
            } catch (e: Exception) {
                _myOrdersResponse.postValue(MyOrderResponseDataClass(false, "Error: ${e.localizedMessage}", emptyList()))
            }
        }
    }


    fun cancelMyOrder(retrofitService: RetrofitService, token: String, id: String) {
        viewModelScope.launch {
            try {
                val response = retrofitService.cancelMyOrder(token, id)
                if (response.isSuccessful) {
                    val cancelMyOrderResponse = response.body()
                    _cancelMyOrderResponse.postValue(cancelMyOrderResponse!!)
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

                    _cancelMyOrderResponse.postValue(CancelMyOrderResponseDataClass(false, errorMessage))
                }
            } catch (e: Exception) {
                _cancelMyOrderResponse.postValue(CancelMyOrderResponseDataClass(false, "Error: ${e.localizedMessage}"))
            }
        }
    }


    fun fetchAllOrders(retrofitService: RetrofitService, token: String) {
        viewModelScope.launch {
            try {
                val response = retrofitService.fetchOtherOrders(token)
                if (response.isSuccessful) {
                    val allOrderResponse = response.body()
                    _allOrderResponse.postValue(allOrderResponse!!)
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

                    _allOrderResponse.postValue(AllOrderResponseDataClass(false, errorMessage, emptyList()))
                }
            } catch (e: Exception) {
                _allOrderResponse.postValue(AllOrderResponseDataClass(false, "Error: ${e.localizedMessage}", emptyList()))
            }
        }
    }

    fun AcceptOrder(retrofitService: RetrofitService, token: String, orderID: String){
        viewModelScope.launch {
            try {
                val response = retrofitService.acceptOrder(token, orderID)
                if (response.isSuccessful) {
                    val acceptOrderResponse = response.body()
                    _acceptOrderResponse.postValue(acceptOrderResponse!!)
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

                    _acceptOrderResponse.postValue(AcceptOrderResponseDataClass(false, errorMessage))
                }
            } catch (e: Exception) {
                _acceptOrderResponse.postValue(AcceptOrderResponseDataClass(false, "Error: ${e.localizedMessage}"))
            }
        }
    }

}