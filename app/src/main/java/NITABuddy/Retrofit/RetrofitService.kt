package NITABuddy.Retrofit

import NITABuddy.Authentication.LoginRequestDataClass
import NITABuddy.Authentication.LoginResponseDataClass
import NITABuddy.Authentication.SignupRequestDataClass
import NITABuddy.Authentication.SignupResponseDataClass
import NITABuddy.DataClass.AcceptOrderResponseDataClass
import NITABuddy.DataClass.AllOrderResponseDataClass
import NITABuddy.DataClass.CancelMyOrderResponseDataClass
import NITABuddy.DataClass.CreateOrderRequestDataClass
import NITABuddy.DataClass.CreateOrderResponseDataClass
import NITABuddy.DataClass.MyOrderResponseDataClass
import NITABuddy.DataClass.UserProfileResponseDataClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitService {

    @POST("/register")
    suspend fun Signup(
        @Body signupRequest: SignupRequestDataClass
    ) : Response<SignupResponseDataClass>


    @POST("/login")
    suspend fun Login(
        @Body loginRequest: LoginRequestDataClass
    ) : Response<LoginResponseDataClass>


    @GET("/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponseDataClass>

    @GET("profile/{id}")
    suspend fun getUserProfileFromID(
        @Path("id") id: String
    ): Response<UserProfileResponseDataClass>


    @POST("/order")
    suspend fun placeOrder(
        @Header("Authorization") token : String,
        @Body createOrderRequest: CreateOrderRequestDataClass
    ) : Response<CreateOrderResponseDataClass>


    @GET("/myOrders")
    suspend fun fetchMyOrders(
        @Header("Authorization") token: String
    ) : Response<MyOrderResponseDataClass>


    @DELETE("cancelMyOrder/{id}")
    suspend fun cancelMyOrder(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<CancelMyOrderResponseDataClass>


    @GET("/allOrders")
    suspend fun fetchOtherOrders(
        @Header("Authorization") token: String
    ): Response<AllOrderResponseDataClass>


    @PUT("/acceptOrder/{id}")
    suspend fun acceptOrder(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : Response<AcceptOrderResponseDataClass>

}