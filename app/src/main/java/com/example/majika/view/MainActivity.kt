package com.example.majika.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.majika.R
import com.example.majika.databinding.ActivityMainBinding

const val CLICK = "ButtonTest"
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var customBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.fragment_space) as NavHostFragment
//        navController = navHostFragment.navController

//        setupActionBarWithNavController((navController))
//        val navMenu: ImageView = findViewById(R.id.nav_menu)
//        navMenu.setOnClickListener {
//            Log.d(CLICK, "Menu")
//            navController.navigate(R.id.menuFragment)
//        }

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        switchFragment(MenuFragment())
        updateToolbar("Menu")
        binding.navbarBottom.selectedItemId = R.id.nav_menu

        binding.navbarBottom.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_twibbon -> {
                    switchFragment(TwibbonFragment())
                    updateToolbar("Twibbon")
                }
                R.id.nav_location -> {
                    switchFragment(BranchFragment())
                    updateToolbar("Location")
                }
                R.id.nav_menu -> {
                    switchFragment(MenuFragment())
                    updateToolbar("Menu")
                }
                R.id.nav_cart -> {
                    switchFragment(CartFragment())
                    updateToolbar("Cart")
                }

                else -> {

                }


            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_space, fragment)
        fragmentTransaction.commit()

    }

    private fun updateToolbar(title: String) {
        val toolbarFragment: Fragment = ToolbarFragment.newInstance(title)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.action_bar_space, toolbarFragment)
        fragmentTransaction.commit()
    }

    fun updateBottomNavbar(selectedItemId: Int) {
        binding.navbarBottom.selectedItemId = selectedItemId
    }
}