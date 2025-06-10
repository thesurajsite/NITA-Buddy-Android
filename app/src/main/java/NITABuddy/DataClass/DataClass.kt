package NITABuddy.DataClass

data class UserDataClass(
    val id: String?="",
    val email: String?="",
    val password: String?="",
    val name: String?="",
    val enrollment: String?="",
    val phone: String?="",
    val hostel: String?="",
    val branch: String?="",
    val year: String?="",
    val created_at: String?=""
)

data class UserProfileResponseDataClass(
    val status: Boolean?= false,
    val message: String?= "",
    val user: UserDataClass
)