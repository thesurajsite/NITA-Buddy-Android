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
import NITABuddy.DataClass.OrderDataClass
import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.Retrofit.RetrofitService
import NITABuddy.ViewModels.OrderViewModel
import NITABuddy.ViewModels.ProfileViewModel
import com.android.volley.DefaultRetryPolicy
import org.json.JSONObject

class Profile_Fragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var jsonObject: JSONObject
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var vibrator: Vibrator
    private lateinit var arrMyOrders:ArrayList<OrderDataClass>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: myRequest_RecyclerAdapter
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var retrofitService: RetrofitService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel = ProfileViewModel()
        orderViewModel = OrderViewModel()
        retrofitService = RetrofitInstance.retrofitService
        sharedPreferences= SharedPreferencesManager(requireContext())
        vibrator=requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        binding.Progressbar.visibility=View.VISIBLE

        fetchUserProfile()
        myRequestRecyclerView()
        fetchMyRequests()


        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchUserProfile()
            fetchMyRequests()
            myRequestRecyclerView()
            binding.swipeRefreshLayout.isRefreshing=false
        }

        binding.logoutBtn.setOnClickListener{ logout() }

        // Observer CancelOrder live data for response, when cancel order is clicked
        orderViewModel.cancelMyOrderResponse.observe(viewLifecycleOwner) { response ->
            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            if(response.status==true) fetchMyRequests() // Refresh
        }


        return binding.root
    }

    private fun myRequestRecyclerView() {

        arrMyOrders=ArrayList()

        adapter = myRequest_RecyclerAdapter(  // Adapter Initialization and Callback
            requireContext(),
            arrMyOrders,
            onCancelOrderClicked = { orderId ->
                Toast.makeText(context, "Cancelling...", Toast.LENGTH_SHORT).show()
                val token = sharedPreferences.getUserToken()
                orderViewModel.cancelMyOrder(retrofitService, token, orderId)
                // we are also observing the livedata response in profile fragment
            }
        )


        binding.myRequestRecyclerView.adapter=adapter
        binding.myRequestRecyclerView.layoutManager=LinearLayoutManager(requireContext())

    }

    private fun fetchMyRequests() {
        binding.recyclerViewProgressBar.visibility = View.VISIBLE
        val token = sharedPreferences.getUserToken()
        orderViewModel.fetchMyOrders(retrofitService, token)

        orderViewModel.myOrdersResponse.observe(viewLifecycleOwner) { response->
            //Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            binding.recyclerViewProgressBar.visibility = View.GONE

            if(response.status==true){
                val myOrders = response.orders
                arrMyOrders.clear()
                arrMyOrders.addAll(myOrders)
                adapter.notifyDataSetChanged()

                if (arrMyOrders.isEmpty()) {
                    binding.emptyImage.visibility = View.VISIBLE
                } else {
                    binding.emptyImage.visibility = View.GONE
                }
            }
        }

    }

    private fun fetchUserProfile() {
        binding.Progressbar.visibility= View.VISIBLE
        val token = sharedPreferences.getUserToken()
        profileViewModel.getUserProfile(retrofitService, token)

        profileViewModel.userProfileResponse.observe(viewLifecycleOwner) { response ->
            //Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
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
