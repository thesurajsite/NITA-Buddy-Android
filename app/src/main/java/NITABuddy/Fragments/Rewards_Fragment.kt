package NITABuddy.Fragments

import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.Retrofit.RetrofitService
import NITABuddy.SharedPreferences.SharedPreferencesManager
import NITABuddy.ViewModels.RewardsViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gharaana.nitabuddy.databinding.FragmentRewardsBinding

class Rewards_Fragment : Fragment() {

    lateinit var binding: FragmentRewardsBinding
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var rewardsViewModel: RewardsViewModel
    private lateinit var  retrofitService: RetrofitService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRewardsBinding.inflate(inflater, container, false)
        sharedPreferences= SharedPreferencesManager(requireContext())
        rewardsViewModel = RewardsViewModel()
        retrofitService = RetrofitInstance.retrofitService

        fetchRewards()

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchRewards()
            binding.swipeRefreshLayout.isRefreshing=false
        }

        return binding.root
    }

    private fun fetchRewards() {
        binding.coins.setText("Fetching...")
        val token = sharedPreferences.getUserToken().toString()
        rewardsViewModel.fetchRewards(retrofitService, token)

        rewardsViewModel.fetchRewardsResponse.observe(viewLifecycleOwner) { response->
            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

            if(response.status==true){
                val coins = response.coins
                binding.coins.text = "$coins Coins"
            }
            else{
                binding.coins.text = "0 Coins"
            }
        }

    }

}