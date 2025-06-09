package NITABuddy.Retrofit

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


}