package com.packapps.videoview

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, ViewListFragment.newInstance())
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                testOpenBlankFragment()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                testOpenBlankFragment()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun testOpenBlankFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, BlankFragment.newInstance())
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

}
