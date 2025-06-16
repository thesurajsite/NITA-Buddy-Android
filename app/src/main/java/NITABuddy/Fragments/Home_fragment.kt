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
import com.gharaana.nitabuddy.databinding.FragmentHomeBinding
import NITABuddy.Activities.myInterface
import NITABuddy.Activities.studentRequest_RecyclerAdapter
import NITABuddy.DataClass.OrderDataClass
import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.Retrofit.RetrofitService
import NITABuddy.ViewModels.OrderViewModel

class Home_fragment : Fragment(), myInterface {

    lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var vibrator: Vibrator
    private lateinit var arrStudentRequest:ArrayList<OrderDataClass>
    private lateinit var adapter: studentRequest_RecyclerAdapter
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var retrofitService: RetrofitService

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        sharedPreferences= SharedPreferencesManager(requireContext())
        vibrator=requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
         orderViewModel = OrderViewModel()
         retrofitService = RetrofitInstance.retrofitService

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

         orderViewModel.acceptOrderResponse.observe(viewLifecycleOwner) { response->
             Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
             if(response.status==true) fetchStudentsRequest() // Refresh
         }



        return binding.root
    }

    override fun fetchStudentsRequest() {
        binding.ProgressBar.visibility=View.VISIBLE
        val token = sharedPreferences.getUserToken()

        orderViewModel.fetchAllOrders(retrofitService, token)
        orderViewModel.allOrderResponse.observe(viewLifecycleOwner) { response->

            binding.ProgressBar.visibility= View.GONE
            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

            if(response.status==true){
                val allOrders = response.orders
                arrStudentRequest.clear()
                arrStudentRequest.addAll(allOrders)
                adapter.notifyDataSetChanged()

                if(arrStudentRequest.isEmpty){
                    binding.nothingToShowImage.visibility=View.VISIBLE
                } else binding.nothingToShowImage.visibility=View.GONE

            }
        }
    }

    override fun studentRequestRecyclerView() {
        arrStudentRequest= ArrayList()
        adapter= studentRequest_RecyclerAdapter(
            requireContext(),
            arrStudentRequest,
            onAcceptOrderClicked = { orderID ->
                Toast.makeText(context, "Accepting...", Toast.LENGTH_SHORT).show()
                val token = sharedPreferences.getUserToken()
                orderViewModel.AcceptOrder(retrofitService, token, orderID)
                // we are also observing the livedata response in Home fragment

            }
        )





        binding.studentRequestRecyclerView.adapter=adapter
        binding.studentRequestRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }
}