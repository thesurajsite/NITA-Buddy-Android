package NITABuddy.Authentication

import NITABuddy.Activities.MainActivity
import NITABuddy.Activities.SignUp_Data
import NITABuddy.Activities.SignUp_Interface
import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.databinding.ActivityUserSignUpBinding
import org.json.JSONObject

class User_SignUp : AppCompatActivity() {
    lateinit var binding: ActivityUserSignUpBinding
    private lateinit var vibrator: Vibrator
    private lateinit var signupViewModel: AuthViewModel
    private lateinit var sharedPreferences: SharedPreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = AuthViewModelFactory(RetrofitInstance.retrofitService)
        signupViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        sharedPreferences= SharedPreferencesManager(this)


        //Spinner TextView
        spinner()

        binding.getOTPBtn.setOnClickListener {
            vibrator.vibrate(50)
            val name = binding.nameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()
            val phoneNo = binding.phoneEt.text.toString()
            val enrollmentNo = binding.enrollmentEt.text.toString()
            val branch = binding.branchSpinner.selectedItem.toString()
            val year = binding.yearSpinner.selectedItem.toString()
            val hostel = binding.hostelSpinner.selectedItem.toString()

            if(!password.equals(confirmPassword)) {
                Toast.makeText(this, "Please Enter Same Password", Toast.LENGTH_SHORT).show()
            }
            else if(name.isNotBlank() && email.isNotBlank() && year!="Select Year" && phoneNo.isNotBlank() && password.isNotBlank() && branch!="Select Branch" && hostel!="Select Hostel" && enrollmentNo.isNotBlank()) {
                val signupData = SignupRequestDataClass(email, password, name, enrollmentNo, phoneNo, hostel, branch, year)
                signupViewModel.Signup(signupData)

                signupViewModel.signupResponse.observe(this) { response->
                    Toast.makeText(this, "${response.message}", Toast.LENGTH_SHORT).show()

                    if(response.status==true){
                        val token = response.token
                        sharedPreferences.updateLoginState(true)
                        sharedPreferences.updateUserToken(token)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            else{
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }

        }

        binding.signupToLoginTv.setOnClickListener {
            vibrator.vibrate(50)
            val intent= Intent(this, User_Login_Activity::class.java)
            startActivity(intent)
            finish()

        }

    }


    private fun spinner() {

        // Hostel Spinner
        val list_of_hostels= arrayListOf<String>("Select Hostel","Aryabhatta", "RNT", "Gargi")
        val hostelSpinnerAdapter= ArrayAdapter(this, R.layout.simple_spinner_item, list_of_hostels)
        hostelSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.hostelSpinner.adapter=hostelSpinnerAdapter

        //Branch Spinner
        val list_of_branch= arrayListOf<String>("Select Branch", "Computer Science", "Electronics", "Mechanical", "Civil", "Electrical", "Chemical", "Production", "Instrumentation", "BioTech", "IIIT")
        val branchSpinnerAdapter= ArrayAdapter(this, R.layout.simple_spinner_item, list_of_branch)
        branchSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.branchSpinner.adapter=branchSpinnerAdapter

        //Year Spinner
        val list_of_year= arrayListOf<String>("Select Year", "1st Year", "2nd Year", "3rd Year", "4th Year")
        val yearSpinnerAdapter= ArrayAdapter(this, R.layout.simple_spinner_item, list_of_year)
        yearSpinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.yearSpinner.adapter=yearSpinnerAdapter

    }


}