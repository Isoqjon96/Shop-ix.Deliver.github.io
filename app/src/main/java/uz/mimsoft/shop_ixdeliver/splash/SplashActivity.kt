package uz.mimsoft.shop_ixdeliver.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_version.*
import uz.mimsoft.shop_ixdeliver.*
import uz.mimsoft.shop_ixdeliver.ui.MainActivity
import uz.shopix.agentapp.APP_VERSION
import uz.shopix.agentapp.VERSION_CHECK_INTERVAL_HOURS

class SplashActivity : AppCompatActivity() {
    private val disposable =CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        when{
            PManager.getKey().isEmpty() -> Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 777)
            PManager.getLastVersionChecked() < System.currentTimeMillis() - VERSION_CHECK_INTERVAL_HOURS * 60 * 60 * 1000 -> getData()
            else -> Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 777)
        }
        tvRetry.setOnClickListener {
            getData()
        }
        b.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")))
            }
        }
    }
    private fun getData(){
        disposable.add(ApiFactory.getApiService()
            .version(APP_VERSION)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                showLoading()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.code == 0) {
                    if (it.result?.isLast == true) {
                        PManager.setLastVersionChecked(System.currentTimeMillis())
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        showVersion()
                    }
                } else {
                    showError()
                }
            }, {
                showError()
            })
        )
    }
    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    private fun showError() {
        vLoading.visibility = View.GONE
        vVersion.visibility = View.GONE
        vError.visibility = View.VISIBLE
    }

    private fun showLoading() {
        vLoading.visibility = View.VISIBLE
        vVersion.visibility = View.GONE
        vError.visibility = View.GONE
    }

    private fun showVersion() {
        vLoading.visibility = View.GONE
        vVersion.visibility = View.VISIBLE
        vError.visibility = View.GONE
    }

}
