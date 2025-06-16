package NITABuddy.Activities

import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.Retrofit.RetrofitService
import NITABuddy.ViewModels.ProfileViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gharaana.nitabuddy.databinding.ActivityStudentDetailsBinding

class student_details : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDetailsBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var retrofitService: RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        profileViewModel = ProfileViewModel()
        retrofitService = RetrofitInstance.retrofitService


        val userID= intent.getStringExtra("userID")
        profileViewModel.getUserProfileFromID(retrofitService, userID.toString())

        var phoneNo = ""
        profileViewModel.userProfileResponse.observe(this) { response->
            if(response.status==true){
                val userDetails = response.user

                binding.nameTv.text = userDetails.name.toString()
                binding.enrollmentTv.text = userDetails.enrollment.toString()
                binding.branchTv.text = userDetails.branch.toString()
                binding.yearTv.text = userDetails.year.toString()
                binding.hostelTv.text = userDetails.hostel.toString()
                binding.phoneTv.text = userDetails.phone.toString()
                phoneNo = userDetails.phone.toString()
            }
        }

        binding.callCardView.setOnClickListener {
            vibrator.vibrate(50)
            Toast.makeText(this, "Calling $phoneNo", Toast.LENGTH_SHORT).show()
            val callintent = Intent(Intent.ACTION_DIAL)
            callintent.setData(Uri.parse("tel:" + phoneNo))
            startActivity(callintent)
        }

       // Toast.makeText(this, "$name, $branch, $year, $hostel, $phoneNo", Toast.LENGTH_SHORT).show()



    }
}