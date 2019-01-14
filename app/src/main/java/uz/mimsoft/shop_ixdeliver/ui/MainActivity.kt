package uz.mimsoft.shop_ixdeliver.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import uz.mimsoft.shop_ixdeliver.PManager
import uz.mimsoft.shop_ixdeliver.R
import uz.mimsoft.shop_ixdeliver.fragment.*
import uz.mimsoft.shop_ixdeliver.splash.LoginActivity

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, dl, toolbar,
            R.string.app_name,
            R.string.app_name
        )
        dl.addDrawerListener(toggle)
        toggle.syncState()

        nv.setNavigationItemSelectedListener(this)
        nv.getHeaderView(0).findViewById<AppCompatTextView>(R.id.tvName).text =
                PManager.getName()
        nv.getHeaderView(0).findViewById<AppCompatTextView>(R.id.tvPhone).text =
                PManager.getPhone()

        nv.getHeaderView(0).setOnClickListener {
            replaceFragment(ProfileFragment.getInstance())
            dl.closeDrawer(GravityCompat.START)
        }

        Picasso.get()
            .load(PManager.getImage())
            .placeholder(R.drawable.ic_account)
            .centerCrop()
            .fit()
            .into(nv.getHeaderView(0).findViewById<AppCompatImageView>(R.id.ivNavigationView))

        replaceFragment(MainFragment.getInstance())

    }


    override fun onBackPressed() {
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_main -> {
                replaceFragment(MainFragment.getInstance())
            }

            R.id.nav_orders -> {
                replaceFragment(OrdersFragment.getInstance())
            }

            R.id.nav_debt->{
                replaceFragment(DebtFragment.getInstance())
            }
            R.id.nav_history ->{
                replaceFragment(HistoryFragment.getInstance())
            }
            R.id.nav_log_out -> {
                AlertDialog.Builder(this)
                    .setMessage(R.string.log_out_warning)
                    .setPositiveButton(R.string.yes) { a, _ ->
                        a.dismiss()
                        PManager.clearData()

                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                    .setNegativeButton(R.string.cancel) { a, _ ->
                        a.dismiss()
                    }
                    .show()
            }
        }

        dl.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
//        Log.i("OnActivityStart",PManager.getImage())

        super.onStart()
    }

    override fun onResume() {
//        Log.i("OnActivityResume",PManager.getImage())
        super.onResume()
    }

    fun onChangeImage(){
        Picasso.get()
            .load(PManager.getImage())
            .into(nv.getHeaderView(0).findViewById<AppCompatImageView>(R.id.ivNavigationView))    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

}
