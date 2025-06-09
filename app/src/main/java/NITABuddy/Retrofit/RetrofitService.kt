package NITABuddy.Retrofit

import NITABuddy.Authentication.LoginRequestDataClass
import NITABuddy.Authentication.LoginResponseDataClass
import NITABuddy.Authentication.SignupRequestDataClass
import NITABuddy.Authentication.SignupResponseDataClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("/register")
    suspend fun Signup(
        @Body signupRequest: SignupRequestDataClass
    ) : Response<SignupResponseDataClass>


    @POST("/login")
    suspend fun Login(
        @Body loginRequest: LoginRequestDataClass
    ) : Response<LoginResponseDataClass>


}