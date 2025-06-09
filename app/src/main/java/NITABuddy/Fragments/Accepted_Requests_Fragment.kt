package NITABuddy.Fragments

import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gharaana.nitabuddy.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import NITABuddy.Activities.acceptedRequest_RecyclerAdapter
import NITABuddy.Activities.acceptedRequest_model
import com.gharaana.nitabuddy.databinding.FragmentAcceptedRequestsBinding
import org.json.JSONObject


class Accepted_Requests_Fragment : Fragment() {

    lateinit var binding: FragmentAcceptedRequestsBinding
    private lateinit var arrAcceptedRequest:ArrayList<acceptedRequest_model>
    private lateinit var adapter: acceptedRequest_RecyclerAdapter
    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAcceptedRequestsBinding.inflate(inflater, container, false)
        SharedPreferencesManager= SharedPreferencesManager(requireContext())

        acceptedRequestRecyclerView()
        fetchAcceptedRequests()

        binding.swipeRefreshLayout.setOnRefreshListener {
            acceptedRequestRecyclerView()
            fetchAcceptedRequests()
            binding.swipeRefreshLayout.isRefreshing=false
        }

        return binding.root
    }

    fun <T> addtoRequestQueue(request: Request<T>, timeoutMillis: Int) {
        request.retryPolicy = DefaultRetryPolicy( timeoutMillis,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT )
        requestQueue.add(request)
    }


    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(requireContext())
    }


    private fun acceptedRequestRecyclerView() {
        arrAcceptedRequest= ArrayList()
        adapter= acceptedRequest_RecyclerAdapter(requireContext(), arrAcceptedRequest)
        binding.acceptedRequestRecyclerView.adapter=adapter
        binding.acceptedRequestRecyclerView.layoutManager= LinearLayoutManager(requireContext())

//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.amazon,"GHIN12345","parcel", "Amazon", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2", "Suraj Verma","7667484399"))
//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.flipkart,"GHIN12345","parcel", "Flipkart", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2","Sunny Kumar" ,"7667484399"))
//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.amazon,"GHIN12345","parcel", "Amazon", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2", "Suraj Verma","7667484399"))
//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.flipkart,"GHIN12345","parcel", "Flipkart", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2","Sunny Kumar" ,"7667484399"))
//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.amazon,"GHIN12345","parcel", "Amazon", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2", "Suraj Verma","7667484399"))
//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.flipkart,"GHIN12345","parcel", "Flipkart", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2","Sunny Kumar" ,"7667484399"))
//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.amazon,"GHIN12345","parcel", "Amazon", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2", "Suraj Verma","7667484399"))
//        arrAcceptedRequest.add(acceptedRequest_model(R.drawable.flipkart,"GHIN12345","parcel", "Flipkart", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2","Sunny Kumar" ,"7667484399"))

    }

    private fun fetchAcceptedRequests() {
        binding.ProgressBar.visibility=View.VISIBLE

        //API Call
        jsonObject= JSONObject()
        val url = "https://gharaanah.onrender.com/engineering/buddy"
        val request = object : JsonObjectRequest(
            Method.GET, url, jsonObject,
            { jsonData->
                binding.ProgressBar.visibility=View.GONE
                val action=jsonData.getBoolean("action")
                if(action){
                    val nitOrderArray= jsonData.getJSONArray("orderNITList")
                    for(i in nitOrderArray.length() - 1 downTo 0){
                        val nitOrderObject=nitOrderArray.getJSONObject(i)
                        val orderDetails=nitOrderObject.getJSONObject("orderNIT")
                        val studentDetails= nitOrderObject.getJSONObject("student")

                        //orderDetails
                        val storeName=orderDetails.getString("storeName")
                        val orderId= orderDetails.getString("orderId")
                        val orderTime= orderDetails.getString("orderTime")
                        val orderPoint= orderDetails.getString("orderPoint")
                        val orderType= orderDetails.getString("type")
                        val orderStatus= orderDetails.getString("orderStatus")
                        val orderDescription= orderDetails.getString("orderDetails")

                        //studentDetails
                        val studentName=studentDetails.getString("name")
                        val phoneNo= studentDetails.getString("phoneNo")
                        val year= studentDetails.getString("year")
                        val hostel= studentDetails.getString("hostel")
                        val enrollmentNo= studentDetails.getString("enrollmentNo")
                        val branch= studentDetails.getString("branch")

                        // Image Allocation
                        var image= R.drawable.amazon
                        if(storeName=="amazon"){
                            image= R.drawable.amazon
                        }
                        else if(storeName=="flipkart"){
                            image= R.drawable.flipkart
                        }
                        else if(storeName=="Samrat"){
                            image= R.drawable.flipkart
                        }
                        else if(storeName=="John"){
                            image= R.drawable.john
                        }
                        else if(storeName=="Joydip"){
                            image= R.drawable.wow
                        }
                        else if(storeName=="Shopping Complex"){
                            image= R.drawable.shopping
                        }

                        arrAcceptedRequest.add(acceptedRequest_model(image,orderId,orderType, storeName, orderTime, orderStatus, orderDescription, orderPoint,studentName, phoneNo, year, hostel, enrollmentNo, branch))

                    }
                    adapter.notifyDataSetChanged()

                }

            },{
                binding.ProgressBar.visibility=View.GONE
                Log.e("accepted-requests", "Error: ${it.message}")
                Toast.makeText(requireContext(), "Some Error Occured", Toast.LENGTH_SHORT).show()
                binding.ProgressBar.visibility=View.INVISIBLE
            }
        ){
            // Override getHeaders to add the Authorization header
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val token=SharedPreferencesManager.getUserToken()
                headers["Authorization"] = "Bearer $token"
                return headers
            }

        }
        addtoRequestQueue(request, 30000)

    }


}