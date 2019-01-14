package uz.mimsoft.shop_ixdeliver.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import uz.mimsoft.shop_ixdeliver.ApiFactory
import uz.mimsoft.shop_ixdeliver.ui.MainActivity
import uz.mimsoft.shop_ixdeliver.PManager
import uz.mimsoft.shop_ixdeliver.R
import uz.shopix.agentapp.SERVER_IMAGES_ADDRESS

class LoginActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()
    private lateinit var loadingAlert: AlertDialog
    private lateinit var errorAlert: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.i("SharedPreference",PManager.getKey())

        loadingAlert = AlertDialog.Builder(this)
                .setView(R.layout.dialog_loading)
                .create()

        errorAlert = AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(R.string.fill_fields)
                .setPositiveButton("OK") { dialog, g ->
                    if (tvName.text?.isNotEmpty() == true) {
                        tvPassword.requestFocus()
                    } else {
                        tvName.requestFocus()
                    }
                    dialog.dismiss()

                }
                .create()

        tvSign.setOnClickListener {
            disposable.clear()
            if (tvName.text?.isNotEmpty() == true && tvPassword.text?.isNotEmpty() == true) {
                getData()
            } else {
                errorAlert.setMessage(getString(R.string.fill_fields))
                errorAlert.show()
            }
        }

    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    private fun getData() {
        disposable.add(
            ApiFactory.getApiService()
                .auth(tvName.text.toString(), tvPassword.text.toString())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    loadingAlert.show()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadingAlert.dismiss()
                    when (it.code) {
                        0 -> {
                            it.result?.let { deliver ->
                                PManager.setName(deliver.name)
                                PManager.setCompanyId(deliver.companyId)
                                PManager.setCompanyId(deliver.regionId)
                                PManager.setKey(deliver.key)
                                PManager.setImage(SERVER_IMAGES_ADDRESS + deliver.image)
                                PManager.setPhone(deliver.phone)

                            }
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        2 -> {
                            errorAlert.setMessage(getString(R.string.login_password_not_found))
                            errorAlert.show()

                        }
                        else -> {
                            errorAlert.setMessage(getString(R.string.unknown_error))
                            errorAlert.setMessage(getString(R.string.unknown_error))
                            errorAlert.show()
                        }
                    }
                }, {
                    loadingAlert.hide()
                    errorAlert.setMessage(getString(R.string.error_no_internet_connection))
                    errorAlert.setTitle(getString(R.string.error))
                    errorAlert.show()
                }))
    }
}