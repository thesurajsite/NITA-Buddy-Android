package NITABuddy.ViewModels

import NITABuddy.DataClass.CancelMyOrderRequestDataClass
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

class OrderViewModel() : ViewModel() {

    private val _createOrderResponse = MutableLiveData<CreateOrderResponseDataClass>()
    val createOrderResponse: LiveData<CreateOrderResponseDataClass> get() = _createOrderResponse

    private val _myOrdersResponse = MutableLiveData<MyOrderResponseDataClass>()
    val myOrdersResponse: LiveData<MyOrderResponseDataClass> get() = _myOrdersResponse

    private val _cancelMyOrderResponse = MutableLiveData<CancelMyOrderResponseDataClass>()
    val cancelMyOrderResponse: LiveData<CancelMyOrderResponseDataClass> get() = _cancelMyOrderResponse

    fun placeOrder(retrofitService: RetrofitService, token: String, order: CreateOrderRequestDataClass){
        viewModelScope.launch {
            try {
                val response = retrofitService.placeOrder(token, order)
                if(response.isSuccessful){
                    val placeOrderResponse = response.body()
                    _createOrderResponse.postValue(placeOrderResponse!!)
                }
                else{
                    _createOrderResponse.postValue(CreateOrderResponseDataClass("Server Error", false))
                }
            }
            catch(e: Exception){
                _createOrderResponse.postValue(CreateOrderResponseDataClass("${e.localizedMessage}", false))
            }
        }
    }

    fun fetchMyOrders(retrofitService: RetrofitService, token: String){
        viewModelScope.launch {
            try {
                val response = retrofitService.fetchMyOrders(token)
                if(response.isSuccessful){
                    val myOrdersResponse = response.body()
                    _myOrdersResponse.postValue(myOrdersResponse!!)
                }
                else{
                    _myOrdersResponse.postValue(MyOrderResponseDataClass(false, "Server Error", emptyList()))
                }
            }
            catch (e: Exception){
                _myOrdersResponse.postValue(MyOrderResponseDataClass(false, "${e.localizedMessage}", emptyList()))
            }
        }
    }

    fun cancelMyOrder(retrofitService: RetrofitService, token: String, id: String){
        viewModelScope.launch {
            try {
                val response = retrofitService.cancelMyOrder(token, id)
                if(response.isSuccessful){
                    val cancelMyOrderResponse = response.body()
                    _cancelMyOrderResponse.postValue(cancelMyOrderResponse!!)
                }
                else{
                    _cancelMyOrderResponse.postValue(CancelMyOrderResponseDataClass(false, "Server Error"))
                }
            }
            catch (e: Exception){
                _cancelMyOrderResponse.postValue(CancelMyOrderResponseDataClass(false, "${e.localizedMessage}"))
            }
        }
    }
}