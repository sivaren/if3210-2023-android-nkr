package com.example.majika.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majika.R
import com.example.majika.adapter.CartItemDecreaseListener
import com.example.majika.adapter.CartItemIncreaseListener
import com.example.majika.adapter.ListCartAdapter
import com.example.majika.databinding.FragmentCartBinding
import com.example.majika.model.MajikaApplication
import com.example.majika.viewmodel.CartViewModel
import com.example.majika.viewmodel.CartViewModelFactory
import com.example.majika.viewmodel.MenuViewModel
import java.text.NumberFormat
import java.util.*

class CartFragment : Fragment() {
    private val menuViewModel: MenuViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModelFactory(
            (activity?.application as MajikaApplication).repository
        )
    }

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        val recyclerView = binding.cartRecyclerView
        val adapter = ListCartAdapter(
            CartItemIncreaseListener { name, price ->
                cartViewModel.addNewFnb(name, price)
            },
            CartItemDecreaseListener { name, price ->
                cartViewModel.removeFnbQuantityByNameAndPrice(name, price)
                menuViewModel._foodItem.value?.find { menuItem -> menuItem.name == name && menuItem.price == price }
                    ?.decreaseQuantity()
                menuViewModel._drinkItem.value?.find { menuItem -> menuItem.name == name && menuItem.price == price }
                    ?.decreaseQuantity()
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        cartViewModel.allFnbs.observe(viewLifecycleOwner, Observer { fnbs -> fnbs?.let {
            adapter.submitList(it)
            // embed with local currency format
            val localelId = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localelId)
            val totalPriceText: String = formatRupiah.format(
                fnbs.sumOf { fnb -> fnb.fnbPrice * fnb.fnbQuantity }.toInt()
            ).toString()

            binding.totalPrice.text = totalPriceText.substring(0, totalPriceText.length - 3)}
            // enable payment button when the total price > 0
            binding.button2.isEnabled = fnbs.sumOf {
                    fnb -> fnb.fnbPrice * fnb.fnbQuantity
            } > 0
        })

        updateToolbar("Cart")

        // redirect to PaymentFragment using "Bayar" button
        binding.button2.setOnClickListener {
            val paymentFragment = PaymentFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_space, paymentFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()

            updateToolbar("Payment")

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            cartFragment = this@CartFragment
        }
    }

    fun goToNextScreen() {
        findNavController().navigate(R.id.action_cartFragment_to_branchFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateToolbar(title: String) {
        val toolbarFragment: Fragment = ToolbarFragment.newInstance(title)
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.action_bar_space, toolbarFragment)
        fragmentTransaction?.commit()
    }
}