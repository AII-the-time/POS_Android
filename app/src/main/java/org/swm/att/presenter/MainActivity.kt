package org.swm.att.presenter

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.swm.att.R
import org.swm.att.`common-ui`.BaseActivity
import org.swm.att.databinding.ActivityMainBinding
import org.swm.att.presenter.home.HomeFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupJetpackNavigation()
    }

    private fun setupJetpackNavigation() {
        val host = supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment ?: return
        navController = host.navController
        //homeFragment 설정
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragment_home)
        )
        //appBar와 navController 연결
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //action bar에서 navigate up이 진행될 때 호출
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}