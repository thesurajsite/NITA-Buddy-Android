package NITABuddy.Fragments

import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.content.Context.VIBRATOR_SERVICE
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.R
import com.gharaana.nitabuddy.databinding.FragmentHomeBinding
import NITABuddy.Activities.myInterface
import NITABuddy.Activities.studentRequest_RecyclerAdapter
import NITABuddy.Activities.studentRequest_model
import com.android.volley.DefaultRetryPolicy
import org.json.JSONObject


class Home_fragment : Fragment(), myInterface {

    lateinit var binding: FragmentHomeBinding
    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager
    private lateinit var vibrator: Vibrator
    private lateinit var arrStudentRequest:ArrayList<studentRequest_model>
    private lateinit var adapter: studentRequest_RecyclerAdapter

    fun <T> addtoRequestQueue(request: Request<T>, timeoutMillis: Int) {
        request.retryPolicy = DefaultRetryPolicy( timeoutMillis,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT )
        requestQueue.add(request)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        SharedPreferencesManager= SharedPreferencesManager(requireContext())
        vibrator=requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator

        studentRequestRecyclerView()
        fetchStudentsRequest()


        binding.swipeRefreshLayout.setOnRefreshListener {

            studentRequestRecyclerView()
            fetchStudentsRequest()
            binding.swipeRefreshLayout.isRefreshing=false


        }

        if(arrStudentRequest.isEmpty()){
            binding.nothingToShowImage.visibility=View.VISIBLE
        }
        else{
            binding.nothingToShowImage.visibility=View.GONE
        }



        return binding.root
    }

    override fun fetchStudentsRequest() {

        binding.ProgressBar.visibility=View.VISIBLE
//        Toast.makeText(requireContext(), "Fetching Requests for you...", Toast.LENGTH_SHORT).show()

        //API Call
        jsonObject= JSONObject()
        val url = "https://gharaanah.onrender.com/engineering/requests"
        val request = object : JsonObjectRequest(
            Method.GET, url, jsonObject,
            { jsonData->
                binding.ProgressBar.visibility=View.INVISIBLE
                val action=jsonData.getBoolean("action")
                if(action){

                    val nitOrderArray= jsonData.getJSONArray("nitCheckOrder")
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
                            image= R.drawable.samrat
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

                        arrStudentRequest.add(studentRequest_model(image,orderId,orderType, storeName, orderTime, orderStatus, orderDescription, orderPoint,studentName, phoneNo, year, hostel, enrollmentNo, branch))

                    }
                    adapter.notifyDataSetChanged()

                    if(arrStudentRequest.isEmpty()){
                        binding.nothingToShowImage.visibility=View.VISIBLE
                    }
                    else{
                        binding.nothingToShowImage.visibility=View.GONE
                    }

                }

            },{
                Toast.makeText(context, "Unable to fetch Requests", Toast.LENGTH_SHORT).show()
                Log.e("student-requests", "Error: ${it.message}")
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


        context?.let {
            addtoRequestQueue(request, 30000)
        }

    }

    override fun studentRequestRecyclerView() {
        arrStudentRequest= ArrayList()
        adapter= studentRequest_RecyclerAdapter(requireContext(), arrStudentRequest, this )
        binding.studentRequestRecyclerView.adapter=adapter
        binding.studentRequestRecyclerView.layoutManager=LinearLayoutManager(requireContext())

//        arrStudentRequest.add(studentRequest_model(R.drawable.amazon,"GHIN12345","parcel", "Amazon", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2", "7667484399"))
//        arrStudentRequest.add(studentRequest_model(R.drawable.flipkart,"GHIN12345","parcel", "Flipkart", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2", "7667484399"))

    }



}