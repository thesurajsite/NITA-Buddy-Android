package NITABuddy.Activities

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUp_Interface {
    @POST("signup")
    suspend fun signup(@Body signupData: SignUp_Data): Response<SignUp_Data>

}