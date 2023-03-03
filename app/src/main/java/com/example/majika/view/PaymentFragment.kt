package com.example.majika.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.*
import com.example.majika.R
import com.example.majika.databinding.FragmentPaymentBinding
import com.example.majika.model.MajikaApplication
import com.example.majika.viewmodel.CartViewModel
import com.example.majika.viewmodel.CartViewModelFactory
import com.example.majika.viewmodel.MenuViewModel
import com.example.majika.viewmodel.PaymentViewModel
import java.text.NumberFormat
import java.util.*

private const val CAMERA_REQUEST_CODE = 101

class PaymentFragment : Fragment() {
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner
    private val viewModel: PaymentViewModel by viewModels()
    private val menuViewModel: MenuViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModelFactory(
            (activity?.application as MajikaApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPaymentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.paymentViewModel = viewModel

        setupPermission()

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        var tempTotalPrice = ""
        // observe changes to set the total price
        cartViewModel.allFnbs.observe(viewLifecycleOwner, Observer {
                fnbs -> fnbs?.let {
            val totalPrice = fnbs.sumOf { fnb -> fnb.fnbPrice * fnb.fnbQuantity }
            if (totalPrice != 0) {
                val priceText: String = formatRupiah.format(totalPrice).toString()
                binding.totalPrice.text = "Total price: " + priceText.substring(0, priceText.length - 3)
                tempTotalPrice = priceText
            } else {
                binding.totalPrice.text = "Total price: " + tempTotalPrice.substring(0, tempTotalPrice.length - 3)
            }

        }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        val activity = requireActivity()
        val imageStatus: ImageView = binding.imageStatus
        val qrTextView: TextView = binding.qrTextView
        val retryBtn: Button = binding.retryBtn
        val totalPrice: TextView = binding.totalPrice
        retryBtn.setOnClickListener {
            qrTextView.text = "Scanning..."
            codeScanner.startPreview()
            imageStatus.setImageResource(0)
        }

        qrTextView.text = "Scanning..."

        // observe payment status
        val priceObserver = Observer<String> { status ->
            if (status == "SUCCESS") {
                qrTextView.visibility = GONE
                imageStatus.setImageResource(R.drawable.payment_success)
                retryBtn.visibility = GONE
                menuViewModel.resetQuantity()
                cartViewModel.resetFnb()

//                switch to menu fragment
                val handler = android.os.Handler()
                handler.postDelayed({
                    val menuFragment = MenuFragment()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragment_space, menuFragment)
                    transaction?.addToBackStack(null)
                    transaction?.commit()

                    val toolbarFragment: Fragment = ToolbarFragment.newInstance("Menu")
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    if (fragmentTransaction != null) {
                        fragmentTransaction.add(R.id.action_bar_space, toolbarFragment)
                        fragmentTransaction.commit()
                    }

                    (activity as MainActivity).updateBottomNavbar(R.id.nav_menu)
                }, 5000)
            } else {
                qrTextView.text = ""
                imageStatus.setImageResource(R.drawable.payment_failed)
            }
        }

        // Observe the payment status, passing in this activity as the LifecycleOwner and the observer.
        viewModel.status.observe(viewLifecycleOwner, priceObserver)

        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
//                call payment API to verify payment status
                viewModel.getStatus(it.text)
                Log.d("PaymentFragment", "status: ${viewModel.status} or ${viewModel.status.value}")

            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(requireContext(),  android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "need camera permission to continue", Toast.LENGTH_SHORT)
                }
            }
        }
    }
}