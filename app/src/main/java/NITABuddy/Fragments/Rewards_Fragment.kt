package NITABuddy.Fragments

import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.databinding.FragmentRewardsBinding
import org.json.JSONObject

class Rewards_Fragment : Fragment() {
    lateinit var binding: FragmentRewardsBinding
    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRewardsBinding.inflate(inflater, container, false)
        SharedPreferencesManager= SharedPreferencesManager(requireContext())
//        binding.ProgressBar.visibility=View.VISIBLE


        fetchRewards()

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchRewards()
            binding.swipeRefreshLayout.isRefreshing=false
        }

        return binding.root
    }

    fun <T> addtoRequestQueue(request: Request<T>){
        requestQueue.add(request)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(requireContext())
    }

    private fun fetchRewards() {
        binding.coins.setText("Fetching...")

        //API Call
        jsonObject= JSONObject()
        val url = "https://gharaanah.onrender.com/engineering/rewards"
        val request = object : JsonObjectRequest(
            Method.GET, url, jsonObject,
            { jsonData->
                val action=jsonData.getBoolean("action")
                if(action){
                    val jsonObject=jsonData.getJSONObject("coin")
                    val coin=jsonObject.getLong("coin")
                    binding.coins.setText("$coin Coins")
                    Log.d("rewards-api", "$coin")


                }

            },{
                Toast.makeText(requireContext(), "Some Error Occured", Toast.LENGTH_SHORT).show()
                Log.e("rewards-api", "Error: ${it.message}")
//                binding.ProgressBar.visibility=View.INVISIBLE

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
        addtoRequestQueue(request)
    }

}