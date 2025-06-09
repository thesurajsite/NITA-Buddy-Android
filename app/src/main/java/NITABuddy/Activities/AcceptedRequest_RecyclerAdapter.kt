package NITABuddy.Activities


import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

class acceptedRequest_RecyclerAdapter(val context: Context,val arrAcceptedRequest: ArrayList<acceptedRequest_model>) : RecyclerView.Adapter<acceptedRequest_RecyclerAdapter.ViewHolder>() {

    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager

    fun <T> addtoRequestQueue(request: Request<T>){
        requestQueue.add(request)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }

    init {
        SharedPreferencesManager= SharedPreferencesManager(context)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){



        // FROM RECYCLER VIEW LAYOUT
        val studentName=itemView.findViewById<TextView>(R.id.studentName)
        val orderId=itemView.findViewById<TextView>(R.id.orderId)
        val orderTime=itemView.findViewById<TextView>(R.id.orderTime)
        val orderDetails=itemView.findViewById<TextView>(R.id.orderDetails)
        val orderStatus=itemView.findViewById<TextView>(R.id.orderStatus)
        val storeImage=itemView.findViewById<ImageView>(R.id.storeImage)
        val phoneNo=itemView.findViewById<TextView>(R.id.phoneNo)
        val completeRequests=itemView.findViewById<TextView>(R.id.completeRequest)
        val completedButton=itemView.findViewById<TextView>(R.id.completedButton)

        val studentRequestLayout=itemView.findViewById<LinearLayout>(R.id.studentRequestLayout)
        val tapLayout=itemView.findViewById<LinearLayout>(R.id.tapLayout)
        val verifyOtpLayout=itemView.findViewById<LinearLayout>(R.id.verifyOtpLayout)
        val otpEditText=itemView.findViewById<EditText>(R.id.otpEditText)
        val verifyOtp=itemView.findViewById<Button>(R.id.verifyOTP)


//        val acceptRequest=itemView.findViewById<TextView>(R.id.acceptRequest)


//        val recyclerLayout=itemView.findViewById<LinearLayout>(R.id.myRequestLayout)
        val vibrator = itemView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.accepted_request_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return arrAcceptedRequest.size


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderId.text=arrAcceptedRequest[position].orderId
        holder.orderTime.text=arrAcceptedRequest[position].orderTime
        holder.orderDetails.text=arrAcceptedRequest[position].orderDetails
        holder.studentName.text=arrAcceptedRequest[position].studentName
        holder.orderStatus.text=arrAcceptedRequest[position].orderstatus
        holder.phoneNo.text=arrAcceptedRequest[position].phoneNo
        holder.storeImage.setImageResource(arrAcceptedRequest[position].image)

        // VISIBILITY CONTROLS FOR PHONE NUMBER
        var flag=0
        holder.studentRequestLayout.setOnClickListener {
            if(flag==0){
                flag=1
                holder.vibrator.vibrate(50)
                holder.tapLayout.visibility=View.VISIBLE
            }
            else{
                flag=0
                holder.vibrator.vibrate(50)
                holder.tapLayout.visibility=View.GONE
                holder.verifyOtpLayout.visibility=View.GONE
            }
        }

        // GREEN BACKGROUND TO COMPLETED ORDERS
        if(arrAcceptedRequest[position].orderstatus=="COMPLETED")
        {
            holder.orderStatus.setBackgroundResource(R.drawable.button_shape_green)
        }

        holder.phoneNo.setOnClickListener {
            holder.vibrator.vibrate(50)
            Toast.makeText(context, "Calling " + arrAcceptedRequest[position].phoneNo, Toast.LENGTH_SHORT).show()
            val callintent = Intent(Intent.ACTION_DIAL)
            callintent.setData(Uri.parse("tel:" + arrAcceptedRequest[position].phoneNo))
            context.startActivity(callintent)
        }


        holder.completeRequests.setOnClickListener {
            holder.vibrator.vibrate(50)
            holder.verifyOtpLayout.visibility=View.VISIBLE
        }

        if(arrAcceptedRequest[position].orderstatus=="COMPLETED")
        {
            holder.completeRequests.visibility=View.GONE        //enables otp editText
            holder.completedButton.visibility=View.VISIBLE      // Just a text showing "Completed" (Non-functional Button)
        }

        holder.studentName.setOnClickListener {
            holder.vibrator.vibrate(50)
            val intent=Intent(context, student_details::class.java)
            intent.putExtra("name", arrAcceptedRequest[position].studentName)
            intent.putExtra("branch", arrAcceptedRequest[position].branch)
            intent.putExtra("enrollmentNo", arrAcceptedRequest[position].enrollmentNo)
            intent.putExtra("year", arrAcceptedRequest[position].year)
            intent.putExtra("hostel", arrAcceptedRequest[position].hostel)
            intent.putExtra("phoneNo", arrAcceptedRequest[position].phoneNo)
            context.startActivity(intent)
        }

        holder.verifyOtp.setOnClickListener {

            holder.vibrator.vibrate(50)
            holder.verifyOtp.setText("Verifying OTP...")

            val otp=holder.otpEditText.text
            val orderId=arrAcceptedRequest[position].orderId

            jsonObject= JSONObject()
            jsonObject.put("orderId", orderId)
            jsonObject.put("otp", otp)
            val url = "https://gharaanah.onrender.com/engineering/completerequest"
            val request = object : JsonObjectRequest(
                Method.POST, url, jsonObject,
                { jsonData ->
                    val action = jsonData.getBoolean("action")
                    if(action){
                        val response = jsonData.getString("response")
                        holder.verifyOtp.visibility=View.GONE
                        holder.otpEditText.visibility=View.GONE
                        holder.completeRequests.visibility=View.GONE        //enables otp editText
                        holder.completedButton.visibility=View.VISIBLE      // Just a text showing "Completed" (Non-functional Button)
                        Toast.makeText(context, "Order Completed, Please Refresh...", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                        holder.verifyOtp.setText("Verify Again")
                    }

                },
                {
                    Toast.makeText(context, "Some Error Occured", Toast.LENGTH_SHORT).show()
                    Log.w("complete-request", "${it.message}")
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