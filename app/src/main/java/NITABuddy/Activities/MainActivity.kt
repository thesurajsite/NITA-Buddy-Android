package NITABuddy.Activities

import NITABuddy.Authentication.User_Login_Activity
import NITABuddy.Fragments.Accepted_Requests_Fragment
import NITABuddy.Fragments.Create_Request_Fragment
import NITABuddy.Fragments.Home_fragment
import NITABuddy.Fragments.Profile_Fragment
import NITABuddy.Fragments.Rewards_Fragment
import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.R
import com.gharaana.nitabuddy.databinding.ActivityMainBinding
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    val UPDATE_CODE=1233
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType= AppUpdateType.FLEXIBLE
    lateinit var binding: ActivityMainBinding
    private lateinit var SharedPreferencesManager: SharedPreferencesManager
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appUpdateManager= AppUpdateManagerFactory.create(applicationContext)
        if(updateType==AppUpdateType.FLEXIBLE){
            appUpdateManager.registerListener(installStateUpdateListener)
        }



        SharedPreferencesManager= SharedPreferencesManager(this)
        vibrator=getSystemService(VIBRATOR_SERVICE) as Vibrator

        //If User Not Logged In, Send it to Login Activity
        if(SharedPreferencesManager.getLoginState()==false){
            startActivity(Intent(this, User_Login_Activity::class.java))
            finish()
        }

        //checkTokenStatus()
        checkForAppUpdate()


        //Default Fragment on StartUp
        replaceFragment(Home_fragment())
        binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(Home_fragment())
                R.id.accepted-> replaceFragment(Accepted_Requests_Fragment())
                R.id.createRequest -> replaceFragment(Create_Request_Fragment())
                R.id.rewards -> replaceFragment(Rewards_Fragment())
                R.id.profile -> replaceFragment(Profile_Fragment())

            }

            return@setOnItemSelectedListener true
        }
    }

    fun replaceFragment(fragment: Fragment){
       val fragmentManager=supportFragmentManager
       val fragmentTransaction=fragmentManager.beginTransaction()
       fragmentTransaction.replace(R.id.frameLayout, fragment)
       fragmentTransaction.commit()
       vibrator.vibrate(50)
    }

    private fun logout() {
        SharedPreferencesManager.updateLoginState(false)
        SharedPreferencesManager.updateUserToken("")
        startActivity(Intent(this, User_Login_Activity::class.java))
        finish()
    }

    private val installStateUpdateListener= InstallStateUpdatedListener{ state->
        if(state.installStatus()== InstallStatus.DOWNLOADED){
            Toast.makeText(applicationContext, "Update Successful, Restarting in 5 Seconds", Toast.LENGTH_LONG).show()
        }
        lifecycleScope.launch {
            delay(5.seconds)
            appUpdateManager.completeUpdate()
        }
    }

    private fun checkForAppUpdate(){
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info->
            val isUpdateAvailable=info.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed=when(updateType){
                AppUpdateType.FLEXIBLE->info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE->info.isImmediateUpdateAllowed
                else-> false
            }

            if(isUpdateAvailable && isUpdateAllowed){
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    UPDATE_CODE
                )
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if(updateType==AppUpdateType.IMMEDIATE){
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info->
                if(info.updateAvailability()== UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateType,
                        this,
                        UPDATE_CODE
                    )
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==UPDATE_CODE){
            if(resultCode!= RESULT_OK){
                println("Somerthing went wrong while updating...")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(updateType==AppUpdateType.FLEXIBLE){
            appUpdateManager.unregisterListener(installStateUpdateListener)
        }

    }
}
