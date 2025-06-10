package NITABuddy.Fragments

import NITABuddy.Authentication.User_Login_Activity
import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.R
import com.gharaana.nitabuddy.databinding.FragmentProfileBinding
import NITABuddy.Activities.myRequest_RecyclerAdapter
import NITABuddy.Activities.myRequest_model
import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.Retrofit.RetrofitService
import NITABuddy.ViewModels.ProfileViewModel
import com.android.volley.DefaultRetryPolicy
import org.json.JSONObject

class Profile_Fragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var jsonObject: JSONObject
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var vibrator: Vibrator
    private lateinit var arrMyRequest:ArrayList<myRequest_model>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: myRequest_RecyclerAdapter
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var retrofitService: RetrofitService

    fun <T> addtoRequestQueue(request: Request<T>, timeoutMillis: Int) {
        request.retryPolicy = DefaultRetryPolicy( timeoutMillis,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT )
        requestQueue.add(request)
    }


    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel = ProfileViewModel()
        retrofitService = RetrofitInstance.retrofitService
        sharedPreferences= SharedPreferencesManager(requireContext())
        vibrator=requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        binding.Progressbar.visibility=View.VISIBLE

        fetchUserProfile()
        fetchMyRequests()
        myRequestRecyclerView()


        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchUserProfile()
            fetchMyRequests()
            myRequestRecyclerView()
            binding.swipeRefreshLayout.isRefreshing=false
        }


        binding.logoutBtn.setOnClickListener{
            logout()
        }


        return binding.root
    }

    private fun myRequestRecyclerView() {

        arrMyRequest=ArrayList()
        adapter = myRequest_RecyclerAdapter(requireContext(), arrMyRequest)
        binding.myRequestRecyclerView.adapter=adapter
        binding.myRequestRecyclerView.layoutManager=LinearLayoutManager(requireContext())

//        arrMyRequest.add(myRequest_model("GHIN12345","parcel", "Amazon", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2"))
//        arrMyRequest.add(myRequest_model("GHIN12345","parcel", "Amazon", "12:36 | 18-04-24", "Placed", "This is an Order", "Gate 2"))

    }

    private fun fetchMyRequests() {

//        Toast.makeText(requireContext(), "Fetching Details...", Toast.LENGTH_SHORT).show()
        //API Call
        jsonObject= JSONObject()
        val url = "https://gharaanah.onrender.com/engineering/studentrequest"
        val request = object : JsonObjectRequest(
            Method.GET, url, jsonObject,
            { jsonData ->
                val action=jsonData.getBoolean("action")
                if(action==true){
                    val orderNITList=jsonData.getJSONArray("orderNITList")
                    for (i in (orderNITList.length() - 1) downTo 0) {

                        val orderObject = orderNITList.getJSONObject(i)



                        // Extract specific fields from the orderObject
                        val orderId = orderObject.getString("orderId")
                        val type = orderObject.getString("type")
                        var store = orderObject.getString("storeName")
                        val orderTime = orderObject.getString("orderTime")
                        val orderstatus = orderObject.getString("orderStatus")
                        val orderDetails = orderObject.getString("orderDetails")
                        val orderPoint = orderObject.getString("orderPoint")


                        // Student Details: Who Accepted the Request
                        var studentName=""
                        var enrollmentNo=""
                        var branch=""
                        var year=""
                        var hostel=""
                        var phoneNo=""

                        var studentDetails=jsonObject
                        if (orderstatus=="ACCEPTED") {
                            studentDetails=orderObject.getJSONObject("buddy")

                            studentName = studentDetails.getString("name")
                            enrollmentNo = studentDetails.getString("enrollmentNo")
                            branch = studentDetails.getString("branch")
                            year = studentDetails.getString("year")
                            hostel = studentDetails.getString("hostel")
                            phoneNo = studentDetails.getString("phoneNo")
                        }


                        // Image Allocation
                        var image= R.drawable.amazon
                        if(store=="amazon"){
                            image= R.drawable.amazon
                        }
                        else if(store=="flipkart"){
                            image= R.drawable.flipkart
                        }
                        else if(store=="Samrat"){
                            image= R.drawable.samrat
                        }
                        else if(store=="John"){
                            image= R.drawable.john
                        }
                        else if(store=="Joydip"){
                            image= R.drawable.wow
                            store="Wow"
                        }
                        else if(store=="Shopping Complex"){
                            image= R.drawable.shopping
                        }

                        store = if (store.isNotEmpty()) {
                            store.substring(0, 1).uppercase() + store.substring(1)
                        } else {
                            store
                        }


                        // Create a myRequest_model object and add it to the ArrayList
                        arrMyRequest.add(myRequest_model(image,orderId, type, store, orderTime, orderstatus, orderDetails, orderPoint, studentName, enrollmentNo, branch, year, hostel, phoneNo))

                    }
                    adapter.notifyDataSetChanged()

                }


                binding.recyclerViewProgressBar.visibility=View.INVISIBLE
                Log.d("my-requests", "$jsonData")
                //Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            },
            {
                binding.recyclerViewProgressBar.visibility=View.INVISIBLE
                Toast.makeText(requireContext(), "Some Error Occured", Toast.LENGTH_SHORT).show()
                Log.e("my-requests", "Error: ${it.message}")
            }
        ) {
            // Override getHeaders to add the Authorization header
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                //val token=SharedPreferencesManager.getUserToken()
                //headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        addtoRequestQueue(request, 30000)

    }

    private fun fetchUserProfile() {
        binding.Progressbar.visibility= View.VISIBLE
        val token = sharedPreferences.getUserToken()
        profileViewModel.getUserProfile(retrofitService, token)

        profileViewModel.userProfileResponse.observe(viewLifecycleOwner) { response ->
            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            binding.Progressbar.visibility= View.INVISIBLE

            if(response.status==true){
                val userDetails = response.user
                binding.nameTv.text=userDetails.name
                binding.enrollmentTv.text=userDetails.enrollment
                binding.branchTv.text=userDetails.branch
                binding.yearTv.text=userDetails.year
                binding.hostelTv.text=userDetails.hostel
                binding.phoneTv.text=userDetails.phone
                binding.emailTv.text=userDetails.email
            }
        }
    }

    private fun logout() {
        binding.logoutBtn.setOnClickListener {
            vibrator.vibrate(50)

            sharedPreferences.updateLoginState(false)
            sharedPreferences.updateUserToken("")
            Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()

            startActivity(Intent(requireContext(), User_Login_Activity::class.java))
            (activity as AppCompatActivity).finish()
        }
    }
}
