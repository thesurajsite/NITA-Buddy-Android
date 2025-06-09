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
    val status: Boolean,
    val message: String,
    val token: String
)