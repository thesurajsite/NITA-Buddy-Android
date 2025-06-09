package NITABuddy.Authentication

import NITABuddy.Activities.MainActivity
import NITABuddy.Authentication.User_SignUp
import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager

    fun <T> addtoRequestQueue(request: Request<T>, timeoutMillis: Int) {
        request.retryPolicy = DefaultRetryPolicy(
            timeoutMillis,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(request)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrator=getSystemService(VIBRATOR_SERVICE) as Vibrator
        SharedPreferencesManager= SharedPreferencesManager(this)


        binding.loginBtn.setOnClickListener {
            vibrator.vibrate(50)
            binding.Progressbar.visibility= View.VISIBLE

            val phoneNo=binding.phoneEt.text.toString()
            val password= binding.passwordEt.text.toString()

            if(password!="" && phoneNo!=""){
                jsonObject= JSONObject()
                jsonObject.put("phoneNo", phoneNo)
                jsonObject.put("password", password)

                val url = "https://gharaanah.onrender.com/engineering/login"
                val request= JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    { jsonData ->
                        binding.Progressbar.visibility = View.INVISIBLE
                        val action = jsonData.getBoolean("action")
                        val response = jsonData.getString("response")

                        if (action == true) {
                            val token = jsonData.getString("token")
                            SharedPreferencesManager.updateLoginState(true)
                            SharedPreferencesManager.updateUserToken(token)

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                            Log.w("login-response", "jsonData= $jsonData jsonObject= $jsonObject")
                            Log.d(
                                "login-response",
                                "action= $action ; response= $response ; token= $token"
                            )
                        }

                        Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

                    }, {
                        binding.Progressbar.visibility = View.INVISIBLE
                        if (it.message != null) {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()  //line 77
                            Log.w("login-response", "${it.message}")
                        }
                    }
                )

                addtoRequestQueue(request, 120000)

            }
            else{
                Toast.makeText(this, "Please fill all fields ", Toast.LENGTH_SHORT).show()
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