package NITABuddy.DataClass

data class CreateOrderRequestDataClass(
    val store: String?="",
    val order_details: String?=""
)

data class CreateOrderResponseDataClass(
    val message: String?="",
    val status: Boolean?=false
)



