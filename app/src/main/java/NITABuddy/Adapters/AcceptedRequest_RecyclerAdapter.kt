package NITABuddy.Adapters


import NITABuddy.Activities.student_details
import NITABuddy.DataClass.OrderDataClass
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Vibrator
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
import com.gharaana.nitabuddy.R

class acceptedRequest_RecyclerAdapter(val context: Context,
                                      val arrAcceptedRequest: ArrayList<OrderDataClass>,
                                      val onVerifyOtpClicked: (String, String) -> Unit
) : RecyclerView.Adapter<acceptedRequest_RecyclerAdapter.ViewHolder>() {

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

        val vibrator = itemView.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.accepted_request_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return arrAcceptedRequest.size


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderId.text=arrAcceptedRequest[position].custom_order_id
        holder.orderTime.text=arrAcceptedRequest[position].created_at
        holder.orderDetails.text=arrAcceptedRequest[position].order_details
        holder.studentName.text=arrAcceptedRequest[position].placed_by_name
        holder.orderStatus.text=arrAcceptedRequest[position].status
        holder.phoneNo.text=arrAcceptedRequest[position].phone


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
        if(arrAcceptedRequest[position].status=="COMPLETED") {
            holder.orderStatus.setBackgroundResource(R.drawable.button_shape_green)
        }

        holder.phoneNo.setOnClickListener {
            holder.vibrator.vibrate(50)
            Toast.makeText(context, "Calling " + arrAcceptedRequest[position].phone, Toast.LENGTH_SHORT).show()
            val callintent = Intent(Intent.ACTION_DIAL)
            callintent.setData(Uri.parse("tel:" + arrAcceptedRequest[position].phone))
            context.startActivity(callintent)
        }

        holder.completeRequests.setOnClickListener {
            holder.vibrator.vibrate(50)
            holder.verifyOtpLayout.visibility=View.VISIBLE
        }

        if(arrAcceptedRequest[position].status=="COMPLETED")
        {
            holder.completeRequests.visibility=View.GONE        //enables otp editText
            holder.completedButton.visibility=View.VISIBLE      // Just a text showing "Completed" (Non-functional Button)
        }

        holder.studentName.setOnClickListener {
            holder.vibrator.vibrate(50)
            val intent=Intent(context, student_details::class.java)
            intent.putExtra("userID", arrAcceptedRequest[position].placed_by)
            context.startActivity(intent)
        }

        holder.verifyOtp.setOnClickListener {

            holder.vibrator.vibrate(50)
            holder.verifyOtp.setText("Verifying OTP...")

            val otp = holder.otpEditText.text.toString()
            val orderId = arrAcceptedRequest[position].id.toString()

            onVerifyOtpClicked(orderId, otp)

        }
    }
}