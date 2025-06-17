package NITABuddy.Adapters


import NITABuddy.Activities.student_details
import NITABuddy.DataClass.OrderDataClass
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gharaana.nitabuddy.R

class myRequest_RecyclerAdapter(val context: Context,
                                val arrMyRequest: ArrayList<OrderDataClass>,
                                val onCancelOrderClicked: (String) -> Unit
) : RecyclerView.Adapter<myRequest_RecyclerAdapter.ViewHolder>() {

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

        holder.orderId.text=arrMyRequest[position].custom_order_id
        holder.orderTime.text=arrMyRequest[position].created_at
        holder.orderDetails.text=arrMyRequest[position].order_details
        holder.storeName.text=arrMyRequest[position].store
        holder.orderStatus.text=arrMyRequest[position].status

        if(arrMyRequest[position].store=="Amazon") holder.storeImage.setImageResource(R.drawable.amazon)
        if(arrMyRequest[position].store=="Flipkart") holder.storeImage.setImageResource(R.drawable.flipkart)
        if(arrMyRequest[position].store=="Samrat") holder.storeImage.setImageResource(R.drawable.samrat)
        if(arrMyRequest[position].store=="John") holder.storeImage.setImageResource(R.drawable.john)
        if(arrMyRequest[position].store=="Wow") holder.storeImage.setImageResource(R.drawable.wow)
        if(arrMyRequest[position].store=="Shopping complex") holder.storeImage.setImageResource(R.drawable.shopping)

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

        if (arrMyRequest[position].status=="Accepted"){
            holder.generateOtp.visibility=View.VISIBLE
            holder.whoAccepted.visibility=View.VISIBLE
            holder.cancelOrder.visibility=View.GONE
        }
        else if(arrMyRequest[position].status=="NotAccepted"){
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
        if(arrMyRequest[position].status=="Accepted" || arrMyRequest[position].status=="Completed" || arrMyRequest[position].status=="IN_PROGRESS") {
            holder.orderStatus.setBackgroundResource(R.drawable.button_shape_green)
        }

        holder.cancelOrder.setOnClickListener {
            val id = arrMyRequest[position].id.toString()
            onCancelOrderClicked(id) // makes callback and sends id to fragment
        }


        holder.generateOtp.setOnClickListener {
            holder.vibrator.vibrate(50)
            holder.generateOtp.setText("OTP: "+arrMyRequest[position].otp)
        }


        holder.whoAccepted.setOnClickListener {
            holder.vibrator.vibrate(50)
            val intent= Intent(context, student_details::class.java)
            intent.putExtra("userID", arrMyRequest[position].accepted_by)
            context.startActivity(intent)
        }

    }
}
