package NITABuddy.Authentication

data class SignupRequestDataClass(
    val email: String?="",
    val password: String?="",
    val name: String?="",
    val enrollment: String,
    val phone: String?="",
    val hostel:String?="",
    val branch: String?="",
    val year: String?=""
)

data class SignupResponseDataClass(
    val status: Boolean?=false,
    val message: String?="",
    val token: String?=""
)

data class LoginRequestDataClass(
    val Email: String?="",
    val Password: String?=""
)

data class LoginResponseDataClass(
    val status: Boolean?=false,
    val message: String?="",
    val token: String?=""
)