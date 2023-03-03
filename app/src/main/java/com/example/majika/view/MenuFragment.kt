package com.example.majika.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.example.majika.adapter.*
import com.example.majika.databinding.FragmentMenuBinding
import com.example.majika.model.Datasource
import com.example.majika.model.Fnb
import com.example.majika.model.MajikaApplication
import com.example.majika.model.MenuItem
import com.example.majika.viewmodel.CartViewModel
import com.example.majika.viewmodel.CartViewModelFactory
import com.example.majika.viewmodel.MenuViewModel

private const val TAG = "MenuFragment"

class MenuFragment: Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val menuViewModel: MenuViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels {
        CartViewModelFactory(
            (activity?.application as MajikaApplication).repository
        )
    }

    lateinit var adapter: ConcatAdapter
    lateinit var foodSectionHeaderAdapter: SectionHeaderAdapter
    lateinit var foodItemAdapter: ListMenuAdapter
    lateinit var drinkSectionHeaderAdapter: SectionHeaderAdapter
    lateinit var drinkItemAdapter: ListMenuAdapter

    /*
     * inflate layout with data binding, sets its lifecycle owner to MenuFragment
     * to enable data binding to observe liveData
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater)

        // allows data binding to observe liveData with this fragment lifecycle
        binding.lifecycleOwner = this

        // give binding access to menuViewModel
        binding.menuViewModel = menuViewModel

        // listener for search filter
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                menuViewModel.filterData(query)
                return true
            }
        })

        // reset the filter whenever fragment is recreated
        menuViewModel.resetFilter()

        // setting up the adapter for recycler view
        foodSectionHeaderAdapter = SectionHeaderAdapter(Datasource .getFoodTitle())
        foodItemAdapter = ListMenuAdapter(MenuItemIncreaseListener { name, price ->
            cartViewModel.addNewFnb(name, price)
        }, MenuItemDecreaseListener { name, price ->

            menuViewModel._foodItem.value?.find { menuItem -> menuItem.name == name && menuItem.price == price }
                ?.decreaseQuantity()
            cartViewModel.removeFnbQuantityByNameAndPrice(name, price)
        })

        drinkSectionHeaderAdapter = SectionHeaderAdapter(Datasource.getDrinkTitle())
        drinkItemAdapter = ListMenuAdapter(MenuItemIncreaseListener { name, price ->
            cartViewModel.addNewFnb(name, price)
        }, MenuItemDecreaseListener { name, price ->

            menuViewModel._drinkItem.value?.find { menuItem -> menuItem.name == name && menuItem.price == price }
                ?.decreaseQuantity()
            cartViewModel.removeFnbQuantityByNameAndPrice(name, price)
        })

        adapter = ConcatAdapter(foodSectionHeaderAdapter, foodItemAdapter,drinkSectionHeaderAdapter, drinkItemAdapter)

        binding.recyclerView.adapter = adapter

        // set up the quantity for menu and sync it with database quantity
        Log.d(TAG, "init quantity")
        menuViewModel.foodItem.observe(viewLifecycleOwner) {
            // update menu view model for initial render
                items ->
            items.forEach{ initMenuQuantity(it) }
            adapter.notifyDataSetChanged()
        }
        cartViewModel.allFnbs.observe(viewLifecycleOwner) {
            // update menu view model for any changes in cart view model
            items ->
            items.forEach{ updateMenuQuantity(it) }
            adapter.notifyDataSetChanged()
        }
        Log.d(TAG, "finish update quantity")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.apply {
            menuFragment = this@MenuFragment
        }
    }

    private fun updateMenuQuantity(it: Fnb) {
        Log.d(TAG, "iterating: ${it.fnbName}")
        if (menuViewModel._foodItem.value == null) {
            Log.d(TAG, "food item is null")
        }
        val tempFood = menuViewModel._foodItem.value?.find { menuItem -> menuItem.name == it.fnbName && menuItem.price == it.fnbPrice.toString() }
        if (tempFood != null) {
            tempFood.quantity = it.fnbQuantity
            Log.d(TAG, "quantity for ${tempFood.name} updated")
        }
        val tempDrink = menuViewModel._drinkItem.value?.find { menuItem -> menuItem.name == it.fnbName && menuItem.price == it.fnbPrice.toString() }
        if (tempDrink != null) {
            tempDrink.quantity = it.fnbQuantity
            Log.d(TAG, "quantity for ${tempDrink.name} updated")
        }
    }

    private fun initMenuQuantity(it: MenuItem) {
        Log.d(TAG, "init iterating: ${it.name}")
        val tempMenu = cartViewModel.allFnbs.value?.find { fnb -> fnb.fnbName == it.name && fnb.fnbPrice == it.price.toInt() }
        if (tempMenu != null) {
            it.quantity = tempMenu.fnbQuantity
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}