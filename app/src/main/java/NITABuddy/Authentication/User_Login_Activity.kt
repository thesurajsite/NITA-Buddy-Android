package NITABuddy.Authentication

import NITABuddy.Activities.MainActivity
import NITABuddy.Authentication.User_SignUp
import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.databinding.ActivityUserLoginBinding
import org.json.JSONObject

class User_Login_Activity : AppCompatActivity() {

    lateinit var binding: ActivityUserLoginBinding
    private lateinit var vibrator: Vibrator
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = AuthViewModelFactory(RetrofitInstance.retrofitService)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
        vibrator=getSystemService(VIBRATOR_SERVICE) as Vibrator
        sharedPreferences= SharedPreferencesManager(this)


        binding.loginBtn.setOnClickListener {
            vibrator.vibrate(50)
            binding.Progressbar.visibility= View.VISIBLE

            val email=binding.emailEt.text.toString()
            val password= binding.passwordEt.text.toString()

            if(password.isNotBlank() && email.isNotBlank()){
                binding.Progressbar.visibility = View.VISIBLE
                val loginData = LoginRequestDataClass(email, password)
                authViewModel.Login(loginData)

                authViewModel.loginResponse.observe(this) { response->
                    Toast.makeText(this, "${response.message}", Toast.LENGTH_SHORT).show()
                    binding.Progressbar.visibility= View.INVISIBLE

                    if(response.status==true){
                        val token = response.token.toString()
                        sharedPreferences.updateLoginState(true)
                        sharedPreferences.updateUserToken(token)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                }
            }
            else{
                Toast.makeText(this, "Please fill all details ", Toast.LENGTH_SHORT).show()
                binding.Progressbar.visibility= View.INVISIBLE
            }
        }

        binding.LoginToSignupTv.setOnClickListener {
            vibrator.vibrate(50)
            startActivity(Intent(this, User_SignUp::class.java))
            finish()
        }

    }
}