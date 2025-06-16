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
import com.gharaana.nitabuddy.R
import org.json.JSONObject

class studentRequest_RecyclerAdapter(
    val context: Context,
    val arrStudentRequest: ArrayList<OrderDataClass>,
    val onAcceptOrderClicked: (String) -> Unit
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
            val intent=Intent(context, student_details::class.java)
            intent.putExtra("userID", arrStudentRequest[position].placed_by)
            context.startActivity(intent)
        }

        holder.acceptRequest.setOnClickListener {
            holder.vibrator.vibrate(50)
            val orderID = arrStudentRequest[position].id.toString()
            onAcceptOrderClicked(orderID) // makes callback and sends id to fragment
        }
    }




}