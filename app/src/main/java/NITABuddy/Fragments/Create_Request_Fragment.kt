package NITABuddy.Fragments

import NITABuddy.DataClass.CreateOrderRequestDataClass
import NITABuddy.Retrofit.RetrofitInstance
import NITABuddy.Retrofit.RetrofitService
import NITABuddy.SharedPreferences.SharedPreferencesManager
import NITABuddy.ViewModels.OrderViewModel
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
    private lateinit var sharedPreferences: SharedPreferencesManager
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var retrofitService: RetrofitService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCreateRequestBinding.inflate(inflater, container,false)

        orderViewModel = OrderViewModel()
        retrofitService = RetrofitInstance.retrofitService
        vibrator=requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sharedPreferences= SharedPreferencesManager(requireContext())

        val dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.create_request_dialog)
        val dialogEditText=dialog.findViewById<EditText>(R.id.dialogEditText)
        val createRequestButton=dialog.findViewById<Button>(R.id.createRequestButton)

        binding.amazonOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")
            createRequestButton.setOnClickListener {
                vibrator.vibrate(50)
                val orderDetails = dialogEditText.text.toString()
                createOrder("Amazon", orderDetails, dialog)
            }
        }

        binding.flipkartOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")
            createRequestButton.setOnClickListener {
                vibrator.vibrate(50)
                val orderDetails = dialogEditText.text.toString()
                createOrder("Flipkart", orderDetails, dialog)
            }
        }

        binding.samratOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")
            createRequestButton.setOnClickListener {
                vibrator.vibrate(50)
                val orderDetails = dialogEditText.text.toString()
                createOrder("Samrat", orderDetails, dialog)
            }
        }

        binding.johnOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")
            createRequestButton.setOnClickListener {
                vibrator.vibrate(50)
                val orderDetails = dialogEditText.text.toString()
                createOrder("John", orderDetails, dialog)
            }
        }

        binding.wowOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")
            createRequestButton.setOnClickListener {
                vibrator.vibrate(50)
                val orderDetails = dialogEditText.text.toString()
                createOrder("Wow", orderDetails, dialog)
            }
        }

        binding.shoppingComplexOrder.setOnClickListener {
            vibrator.vibrate(50)
            dialog.show()
            dialogEditText.setText("")
            createRequestButton.setOnClickListener {
                vibrator.vibrate(50)
                val orderDetails = dialogEditText.text.toString()
                createOrder("Shopping complex", orderDetails, dialog)
            }
        }

        return binding.root
    }

    private fun createOrder(store: String, orderDetails: String, dialog: Dialog){
        val token = sharedPreferences.getUserToken()
        val order = CreateOrderRequestDataClass(store, orderDetails)
        orderViewModel.placeOrder(retrofitService, token, order)

        orderViewModel.createOrderResponse.observe(viewLifecycleOwner) { response->
            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            if(response.status==true){
                dialog.dismiss()
            }
        }
    }

}