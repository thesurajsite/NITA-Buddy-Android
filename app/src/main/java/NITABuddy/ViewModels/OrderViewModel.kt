package NITABuddy.ViewModels

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
}