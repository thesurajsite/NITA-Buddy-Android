package NITABuddy.Fragments

import NITABuddy.SharedPreferences.SharedPreferencesManager
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gharaana.nitabuddy.R
import com.gharaana.nitabuddy.databinding.FragmentCreateRequestBinding
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Create_Request_Fragment : Fragment() {
    lateinit var binding: FragmentCreateRequestBinding
    private lateinit var vibrator: Vibrator
    private lateinit var jsonObject: JSONObject
    private lateinit var SharedPreferencesManager: SharedPreferencesManager

    fun <T> addtoRequestQueue(request: Request<T>){
        requestQueue.add(request)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCreateRequestBinding.inflate(inflater, container,false)

        vibrator=requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        SharedPreferencesManager= SharedPreferencesManager(requireContext())
        val url = "https://gharaanah.onrender.com/engineering/placeorder"
        val dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.create_request_dialog)
        val dialogEditText=dialog.findViewById<EditText>(R.id.dialogEditText)
        val dialogButton=dialog.findViewById<Button>(R.id.createRequestButton)


        binding.amazonOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")

            dialogButton.setOnClickListener {

                vibrator.vibrate(50)
                Toast.makeText(requireContext(), "Creating Request...", Toast.LENGTH_SHORT).show()
                val orderDetails=dialogEditText.text.toString()

                jsonObject= JSONObject()
                jsonObject.put("orderPoint", "Gate 2")
                jsonObject.put("type", "Parcel")
                jsonObject.put("storeName", "amazon")
                jsonObject.put("orderDetails", orderDetails)
                jsonObject.put("orderTime", currentTime().toString())

                val request=object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { jsonData->
                        val action=jsonData.getBoolean("action")
                        val response=jsonData.getString("response")
                        Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
                        Log.d("Request-Call", "jsonData: $jsonData || action: $action || response: $response")
                        dialog.dismiss()


                    },{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        Log.w("Request-Call", "${it.message}")
                        dialog.dismiss()

                    }
                ){
                    override fun getHeaders(): MutableMap<String, String>{
                        val headers=HashMap<String, String>()
                        val token=SharedPreferencesManager.getUserToken()
                        headers["Authorization"]="Bearer $token"
                        return headers
                    }
                }

                addtoRequestQueue(request)

            }



        }

        binding.flipkartOrder.setOnClickListener {
            vibrator.vibrate(50)

            dialog.show()
            dialogEditText.setText("")

            dialogButton.setOnClickListener {

                vibrator.vibrate(50)
                Toast.makeText(requireContext(), "Creating Request...", Toast.LENGTH_SHORT).show()
                val orderDetails=dialogEditText.text.toString()

                jsonObject= JSONObject()
                jsonObject.put("orderPoint", "Gate 2")
                jsonObject.put("type", "Parcel")
                jsonObject.put("storeName", "flipkart")
                jsonObject.put("orderDetails", orderDetails)
                jsonObject.put("orderTime", currentTime().toString())

                val request=object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { jsonData->
                        val action=jsonData.getBoolean("action")
                        val response=jsonData.getString("response")
                        Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
                        Log.d("Request-Call", "jsonData: $jsonData || action: $action || response: $response")
                        dialog.dismiss()


                    },{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        Log.w("Request-Call", "${it.message}")
                        dialog.dismiss()

                    }
                ){
                    override fun getHeaders(): MutableMap<String, String>{
                        val headers=HashMap<String, String>()
                        val token=SharedPreferencesManager.getUserToken()
                        headers["Authorization"]="Bearer $token"
                        return headers
                    }
                }

                addtoRequestQueue(request)

            }

        }

        binding.samratOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")

            dialogButton.setOnClickListener {

                vibrator.vibrate(50)
                Toast.makeText(requireContext(), "Creating Request...", Toast.LENGTH_SHORT).show()
                val orderDetails=dialogEditText.text.toString()

                jsonObject= JSONObject()
                jsonObject.put("orderPoint", "Gate 2")
                jsonObject.put("type", "Shop")
                jsonObject.put("storeName", "Samrat")
                jsonObject.put("orderDetails", orderDetails)
                jsonObject.put("orderTime", currentTime().toString())

                val request=object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { jsonData->
                        val action=jsonData.getBoolean("action")
                        val response=jsonData.getString("response")
                        Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
                        Log.d("Request-Call", "jsonData: $jsonData || action: $action || response: $response")
                        dialog.dismiss()


                    },{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        Log.w("Request-Call", "${it.message}")
                        dialog.dismiss()

                    }
                ){
                    override fun getHeaders(): MutableMap<String, String>{
                        val headers=HashMap<String, String>()
                        val token=SharedPreferencesManager.getUserToken()
                        headers["Authorization"]="Bearer $token"
                        return headers
                    }
                }

                addtoRequestQueue(request)

            }

        }

        binding.johnOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")

            dialogButton.setOnClickListener {

                vibrator.vibrate(50)
                Toast.makeText(requireContext(), "Creating Request...", Toast.LENGTH_SHORT).show()
                val orderDetails=dialogEditText.text.toString()

                jsonObject= JSONObject()
                jsonObject.put("orderPoint", "Gate 2")
                jsonObject.put("type", "food")
                jsonObject.put("storeName", "John")
                jsonObject.put("orderDetails", orderDetails)
                jsonObject.put("orderTime", currentTime().toString())

                val request=object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { jsonData->
                        val action=jsonData.getBoolean("action")
                        val response=jsonData.getString("response")
                        Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
                        Log.d("Request-Call", "jsonData: $jsonData || action: $action || response: $response")
                        dialog.dismiss()


                    },{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        Log.w("Request-Call", "${it.message}")
                        dialog.dismiss()

                    }
                ){
                    override fun getHeaders(): MutableMap<String, String>{
                        val headers=HashMap<String, String>()
                        val token=SharedPreferencesManager.getUserToken()
                        headers["Authorization"]="Bearer $token"
                        return headers
                    }
                }

                addtoRequestQueue(request)

            }
        }

        binding.wowOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")

            dialogButton.setOnClickListener {

                vibrator.vibrate(50)
                Toast.makeText(requireContext(), "Creating Request...", Toast.LENGTH_SHORT).show()
                val orderDetails=dialogEditText.text.toString()

                jsonObject= JSONObject()
                jsonObject.put("orderPoint", "Gate 2")
                jsonObject.put("type", "food")
                jsonObject.put("storeName", "Joydip")
                jsonObject.put("orderDetails", orderDetails)
                jsonObject.put("orderTime", currentTime().toString())

                val request=object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { jsonData->
                        val action=jsonData.getBoolean("action")
                        val response=jsonData.getString("response")
                        Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
                        Log.d("Request-Call", "jsonData: $jsonData || action: $action || response: $response")
                        dialog.dismiss()


                    },{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        Log.w("Request-Call", "${it.message}")
                        dialog.dismiss()

                    }
                ){
                    override fun getHeaders(): MutableMap<String, String>{
                        val headers=HashMap<String, String>()
                        val token=SharedPreferencesManager.getUserToken()
                        headers["Authorization"]="Bearer $token"
                        return headers
                    }
                }

                addtoRequestQueue(request)

            }

        }


        binding.shoppingComplexOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")

            dialogButton.setOnClickListener {

                vibrator.vibrate(50)
                Toast.makeText(requireContext(), "Creating Request...", Toast.LENGTH_SHORT).show()
                val orderDetails=dialogEditText.text.toString()

                jsonObject= JSONObject()
                jsonObject.put("orderPoint", "Shopping Complex")
                jsonObject.put("type", "shopping")
                jsonObject.put("storeName", "Shopping Complex")
                jsonObject.put("orderDetails", orderDetails)
                jsonObject.put("orderTime", currentTime().toString())

                val request=object : JsonObjectRequest(
                    Method.POST, url, jsonObject,
                    { jsonData->
                        val action=jsonData.getBoolean("action")
                        val response=jsonData.getString("response")
                        Toast.makeText(requireContext(), "$response", Toast.LENGTH_SHORT).show()
                        Log.d("Request-Call", "jsonData: $jsonData || action: $action || response: $response")
                        dialog.dismiss()


                    },{
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                        Log.w("Request-Call", "${it.message}")
                        dialog.dismiss()

                    }
                ){
                    override fun getHeaders(): MutableMap<String, String>{
                        val headers=HashMap<String, String>()
                        val token=SharedPreferencesManager.getUserToken()
                        headers["Authorization"]="Bearer $token"
                        return headers
                    }
                }

                addtoRequestQueue(request)

            }

        }





        return binding.root
    }

    private fun currentTime(): String? {
        val currentDateTime = LocalDateTime.now()

        // Format the date and time with the desired pattern (dd-MM HH:mm)
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
        val DateTime = currentDateTime.format(formatter).toString()

        return DateTime
    }

}