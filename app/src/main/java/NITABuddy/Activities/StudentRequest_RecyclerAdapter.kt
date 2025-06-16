package NITABuddy.Activities


import NITABuddy.DataClass.OrderDataClass
import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
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

class studentRequest_RecyclerAdapter(
    val context: Context,
    val arrStudentRequest: ArrayList<OrderDataClass>,
    val myInterface: myInterface
) : RecyclerView.Adapter<studentRequest_RecyclerAdapter.ViewHolder>() {

    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        // FROM RECYCLER VIEW LAYOUT
        val studentName=itemView.findViewById<TextView>(R.id.studentName)
        val orderId=itemView.findViewById<TextView>(R.id.orderId)
        val orderTime=itemView.findViewById<TextView>(R.id.orderTime)
        val orderDetails=itemView.findViewById<TextView>(R.id.orderDetails)
        val orderStatus=itemView.findViewById<TextView>(R.id.orderStatus)
        val storeImage=itemView.findViewById<ImageView>(R.id.storeImage)
        val phoneNo=itemView.findViewById<TextView>(R.id.phoneNo)

        val studentRequestLayout=itemView.findViewById<LinearLayout>(R.id.studentRequestLayout)
        val tapLayout=itemView.findViewById<LinearLayout>(R.id.tapLayout)
        val acceptRequest=itemView.findViewById<TextView>(R.id.acceptRequest)


        val recyclerLayout=itemView.findViewById<LinearLayout>(R.id.myRequestLayout)
        val vibrator = itemView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.students_request_layout, parent, false))

    }

    override fun getItemCount(): Int {
        return arrStudentRequest.size


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderId.text=arrStudentRequest[position].custom_order_id
        holder.orderTime.text=arrStudentRequest[position].created_at
        holder.orderDetails.text=arrStudentRequest[position].order_details
        holder.studentName.text=arrStudentRequest[position].placed_by_name
        holder.orderStatus.text=arrStudentRequest[position].status
        holder.phoneNo.text=arrStudentRequest[position].phone

        if(arrStudentRequest[position].store=="Amazon") holder.storeImage.setImageResource(R.drawable.amazon)
        if(arrStudentRequest[position].store=="Flipkart") holder.storeImage.setImageResource(R.drawable.flipkart)
        if(arrStudentRequest[position].store=="Samrat") holder.storeImage.setImageResource(R.drawable.samrat)
        if(arrStudentRequest[position].store=="John") holder.storeImage.setImageResource(R.drawable.john)
        if(arrStudentRequest[position].store=="Wow") holder.storeImage.setImageResource(R.drawable.wow)
        if(arrStudentRequest[position].store=="Shopping complex") holder.storeImage.setImageResource(R.drawable.shopping)


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
            }
        }

        holder.phoneNo.setOnClickListener {
            holder.vibrator.vibrate(50)
            Toast.makeText(context, "Calling " + arrStudentRequest[position].phone, Toast.LENGTH_SHORT).show()
            val callintent = Intent(Intent.ACTION_DIAL)
            callintent.setData(Uri.parse("tel:" + arrStudentRequest[position].phone))
            context.startActivity(callintent)
        }

        holder.studentName.setOnClickListener {
            holder.vibrator.vibrate(50)
//            val intent=Intent(context, student_details::class.java)
//            intent.putExtra("name", arrStudentRequest[position].studentName)
//            intent.putExtra("branch", arrStudentRequest[position].branch)
//            intent.putExtra("enrollmentNo", arrStudentRequest[position].enrollmentNo)
//            intent.putExtra("year", arrStudentRequest[position].year)
//            intent.putExtra("hostel", arrStudentRequest[position].hostel)
//            intent.putExtra("phoneNo", arrStudentRequest[position].phoneNo)
//            context.startActivity(intent)

        }


//        holder.acceptRequest.setOnClickListener {
//            holder.vibrator.vibrate(50)
//
//            val builder = AlertDialog.Builder(context)
//                .setTitle("Accept Request")
//                .setIcon(R.drawable.accept_request)
//                .setMessage("Do you want to Accept this Request?")
//                .setPositiveButton(
//                    "Yes"
//                ) { dialogInterface, i ->
//                    try {
//                        holder.vibrator.vibrate(50)
//                        holder.acceptRequest.setText("Accepting...")
//
//                        val orderId=arrStudentRequest[position].orderId
//
//                        jsonObject= JSONObject()
//                        jsonObject.put("orderId", orderId)
//                        val url = "https://gharaanah.onrender.com/engineering/acceptrequest"
//                        val request = object : JsonObjectRequest(
//                            Method.POST, url, jsonObject,
//                            { jsonData ->
//                                val action = jsonData.getBoolean("action")
//                                if(action){
//                                    val response = jsonData.getString("response")
//                                    holder.acceptRequest.setText("Order Accepted")
//                                    Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show()
//
//                                    // REFRESH THE RECYCLER VIEW
//                                    myInterface.fetchStudentsRequest()
//                                    myInterface.studentRequestRecyclerView()
//
//                                }
//                            },
//                            {
//                                Toast.makeText(context, "Some Error Occured", Toast.LENGTH_SHORT).show()
//                                Log.w("otp-request", "${it.message}")
//                            }
//                        ){
//                            override fun getHeaders(): MutableMap<String, String> {
//                                val headers = HashMap<String, String>()
//                                val token=SharedPreferencesManager.getUserToken()
//                                headers["Authorization"] = "Bearer $token"
//                                return headers
//                            }
//
//                        }
//
//                        addtoRequestQueue(request)
//
//                    } catch (e: Exception) {
//                        Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
//                        Log.w("accept-request", e)
//                    }
//
//                }.setNegativeButton("No")
//                { dialogInterface, i ->
//                    //Nothing
//                }
//            builder.show()
//            true
//
//        }

    }




}