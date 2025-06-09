package NITABuddy.Authentication

data class SignupRequestDataClass(
    val Email: String?="",
    val Password: String?="",
    val Name: String?="",
    val Enrollment: String,
    val Phone: String?="",
    val Hostel:String?="",
    val Branch: String?="",
    val Year: String?=""
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