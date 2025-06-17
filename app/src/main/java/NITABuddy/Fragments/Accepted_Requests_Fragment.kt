package NITABuddy.Fragments

import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import NITABuddy.Adapters.acceptedRequest_RecyclerAdapter
import NITABuddy.DataClass.OrderDataClass
import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.Retrofit.RetrofitService
import NITABuddy.ViewModels.OrderViewModel
import android.widget.Toast
import com.gharaana.nitabuddy.databinding.FragmentAcceptedRequestsBinding

class Accepted_Requests_Fragment : Fragment() {

    lateinit var binding: FragmentAcceptedRequestsBinding
    private lateinit var arrAcceptedRequest:ArrayList<OrderDataClass>
    private lateinit var adapter: acceptedRequest_RecyclerAdapter
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var retrofitService: RetrofitService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAcceptedRequestsBinding.inflate(inflater, container, false)
        sharedPreferences = SharedPreferencesManager(requireContext())
        orderViewModel = OrderViewModel()
        retrofitService = RetrofitInstance.retrofitService

        acceptedRequestRecyclerView()
        fetchAcceptedRequests()

        binding.swipeRefreshLayout.setOnRefreshListener {
            acceptedRequestRecyclerView()
            fetchAcceptedRequests()
            binding.swipeRefreshLayout.isRefreshing=false
        }

        // Observer CompleteOrder live data for response, when verify order is clicked
        orderViewModel.completeOrderResponse.observe(viewLifecycleOwner) { response ->
            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
            if(response.status==true) fetchAcceptedRequests() // Refresh
        }

        return binding.root
    }

    private fun acceptedRequestRecyclerView() {
        val token = sharedPreferences.getUserToken()
        arrAcceptedRequest= ArrayList()
        adapter = acceptedRequest_RecyclerAdapter(  // Adapter Initialization and Callback
            requireContext(),
            arrAcceptedRequest,
            onVerifyOtpClicked = { orderId, otp ->
                orderViewModel.completeOrder(retrofitService, token, orderId, otp)
            }
        )


        binding.acceptedRequestRecyclerView.adapter=adapter
        binding.acceptedRequestRecyclerView.layoutManager= LinearLayoutManager(requireContext())
    }

    private fun fetchAcceptedRequests() {
        binding.ProgressBar.visibility=View.VISIBLE
        val token = sharedPreferences.getUserToken()

        orderViewModel.fetchAcceptedOrders(retrofitService, token)
        orderViewModel.acceptedOrderResponse.observe(viewLifecycleOwner) { response->

            binding.ProgressBar.visibility= View.GONE
            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

            if(response.status==true){
                val acceptedOrders = response.orders
                arrAcceptedRequest.clear()
                arrAcceptedRequest.addAll(acceptedOrders)
                adapter.notifyDataSetChanged()

                if(arrAcceptedRequest.isEmpty){
                    binding.nothingToShowImage.visibility=View.VISIBLE
                } else binding.nothingToShowImage.visibility=View.GONE
            }
        }

    }
}