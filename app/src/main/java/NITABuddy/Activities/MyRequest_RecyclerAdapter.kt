package NITABuddy.Activities


import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.R
import org.json.JSONObject

class myRequest_RecyclerAdapter(val context: Context,val arrMyRequest: ArrayList<myRequest_model>) : RecyclerView.Adapter<myRequest_RecyclerAdapter.ViewHolder>() {

    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager

    init {
        SharedPreferencesManager= SharedPreferencesManager(context)

    }

    fun <T> addtoRequestQueue(request: Request<T>){
        requestQueue.add(request)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        // FROM RECYCLER VIEW LAYOUT
        val storeName=itemView.findViewById<TextView>(R.id.storeName)
        val orderId=itemView.findViewById<TextView>(R.id.orderId)
        val orderTime=itemView.findViewById<TextView>(R.id.orderTime)
        val orderDetails=itemView.findViewById<TextView>(R.id.orderDetails)
        val orderStatus=itemView.findViewById<TextView>(R.id.orderStatus)
        val storeImage=itemView.findViewById<ImageView>(R.id.storeImage)

        val myRequestLayout=itemView.findViewById<LinearLayout>(R.id.myRequestLayout)
        val tapLayout=itemView.findViewById<LinearLayout>(R.id.tapLayout)
        val generateOtp=itemView.findViewById<TextView>(R.id.generateOtp)
        val whoAccepted=itemView.findViewById<TextView>(R.id.whoAccepted)
        val cancelOrder=itemView.findViewById<TextView>(R.id.cancelOrder)
        val recyclerLayout=itemView.findViewById<LinearLayout>(R.id.myRequestLayout)
        //VIBRATOR VIBRATOR VIBRATOR
        val vibrator = itemView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_requests_layout, parent, false))

    }

    override fun getItemCount(): Int {
        return arrMyRequest.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderId.text=arrMyRequest[position].orderId
        holder.orderTime.text=arrMyRequest[position].orderTime
        holder.orderDetails.text=arrMyRequest[position].orderDetails
        holder.storeName.text=arrMyRequest[position].store
        holder.orderStatus.text="Status: "+arrMyRequest[position].orderstatus
        holder.storeImage.setImageResource(arrMyRequest[position].image)



        //TAPLAYOUT (OTP & CANCEL BUTTON) VISIBILITY CONTROLS
        var flag=0
        holder.myRequestLayout.setOnClickListener {
            if(flag==0){
                flag=1
                holder.vibrator.vibrate(50)
                holder.tapLayout.visibility=View.VISIBLE
            }
            else{
                flag=0
                holder.vibrator.vibrate(50)
                holder.tapLayout.visibility=View.GONE

            }
        }

        // VISIBILITY CONTROLS FOR OTP & CANCEL BUTTONS (INDIVIDUALLY)

        if (arrMyRequest[position].orderstatus=="ACCEPTED"){
            holder.generateOtp.visibility=View.VISIBLE
            holder.whoAccepted.visibility=View.VISIBLE
            holder.cancelOrder.visibility=View.GONE
        }
        else if(arrMyRequest[position].orderstatus=="NOT_ACCEPTED"){
            holder.cancelOrder.visibility=View.VISIBLE
            holder.generateOtp.visibility=View.GONE
            holder.whoAccepted.visibility=View.GONE
        }
        else{
            holder.generateOtp.visibility=View.GONE
            holder.whoAccepted.visibility=View.GONE
            holder.cancelOrder.visibility=View.GONE
        }

        // GREEN BACKGROUND TO ACCEPTED AND COMPLETED ORDERS
        if(arrMyRequest[position].orderstatus=="ACCEPTED" || arrMyRequest[position].orderstatus=="COMPLETED" || arrMyRequest[position].orderstatus=="IN_PROGRESS")
        {
            holder.orderStatus.setBackgroundResource(R.drawable.button_shape_green)
        }

        holder.generateOtp.setOnClickListener {
            holder.vibrator.vibrate(50)
            holder.generateOtp.setText("Generating OTP...")

            val orderId=arrMyRequest[position].orderId

            jsonObject= JSONObject()
            jsonObject.put("orderId", orderId)
            val url = "https://gharaanah.onrender.com/engineering/generateotp"
            val request = object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                { jsonData ->
                    val action = jsonData.getBoolean("status")
                    if(action){
                        val response = jsonData.getString("response") // Contains text "Your OTP is: "
                        val otp=jsonData.getLong("otp")
                        holder.generateOtp.setText("\" $otp \"")
                    }
                },
                {
                    Toast.makeText(context, "Some Error Occured", Toast.LENGTH_SHORT).show()
                    Log.w("otp-request", "${it.message}")
                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    val token=SharedPreferencesManager.getUserToken()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }

            }

            addtoRequestQueue(request)
        }

        // WHO ACCEPTED BUTTON
        holder.whoAccepted.setOnClickListener {
            holder.vibrator.vibrate(50)

            val intent= Intent(context, student_details::class.java)
            intent.putExtra("name", arrMyRequest[position].studentName)
            intent.putExtra("branch", arrMyRequest[position].branch)
            intent.putExtra("enrollmentNo", arrMyRequest[position].enrollmentNo)
            intent.putExtra("year", arrMyRequest[position].year)
            intent.putExtra("hostel", arrMyRequest[position].hostel)
            intent.putExtra("phoneNo", arrMyRequest[position].phoneNo)
            context.startActivity(intent)

        }

        holder.cancelOrder.setOnClickListener {
            holder.vibrator.vibrate(50)
            holder.cancelOrder.setText("Cancelling...")
            val orderId=arrMyRequest[position].orderId

            jsonObject= JSONObject()
            jsonObject.put("orderId", orderId)
            val url = "https://gharaanah.onrender.com/engineering/cancel"
            val request = object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                { jsonData ->
                    val action = jsonData.getBoolean("action")
//                    val response=jsonData.getString("response")
                    if(action){
                        holder.cancelOrder.setText("Order Cancelled")
                        Toast.makeText(context, "Order Cancelled, Please Refresh", Toast.LENGTH_SHORT).show()
                    }
                },
                {
                    Toast.makeText(context, "Some Error Occured", Toast.LENGTH_SHORT).show()
                    Log.w("otp-request", "${it.message}")
                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    val token=SharedPreferencesManager.getUserToken()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }

            }

            addtoRequestQueue(request)

        }
    }
}
