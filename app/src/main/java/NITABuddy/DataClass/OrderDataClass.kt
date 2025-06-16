package NITABuddy.DataClass

data class CreateOrderRequestDataClass(
    val store: String?="",
    val order_details: String?=""
)

data class CreateOrderResponseDataClass(
    val message: String?="",
    val status: Boolean?=false
)

data class OrderDataClass(
    val id: String?="",
    val custom_order_id: String?="",
    val store: String?="",
    val order_details: String?="",
    val status: String?="",
    val otp: String?="",
    val phone: String?="",
    val placed_by: String?="",
    val placed_by_name: String?="",
    val accepted_by: String?="",
    val created_at: String?=""
)

data class MyOrderResponseDataClass(
    val status: Boolean?= false,
    val message: String?= "",
    val orders: List<OrderDataClass> = emptyList()
)

data class CancelMyOrderResponseDataClass(
    val status: Boolean?=false,
    val message: String?=""
)

data class AllOrderResponseDataClass(
    val status: Boolean?=false,
    val message: String?="",
    val orders: List<OrderDataClass>
)

