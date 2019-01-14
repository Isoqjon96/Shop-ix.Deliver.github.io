package uz.mimsoft.shop_ixdeliver.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.view_error.*
import uz.mimsoft.shop_ixdeliver.ApiFactory
import uz.mimsoft.shop_ixdeliver.Order
import uz.mimsoft.shop_ixdeliver.PManager
import uz.mimsoft.shop_ixdeliver.R
import uz.mimsoft.shop_ixdeliver.adapter.PeerAdapter
import uz.shopix.agentapp.INTENT_ORDER
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity() {

    private val adapter = PeerAdapter()
    private var order: Order? = null
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        order = intent.getSerializableExtra(INTENT_ORDER) as Order

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = String.format(resources.getString(R.string.order_number), order?.id)

        rvOrderDetail.layoutManager = LinearLayoutManager(this)
        rvOrderDetail.adapter = adapter

        tvRetry.setOnClickListener {
            getData()
        }
        setData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getData() {
        var load = false
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ApiFactory
                .getApiService()
                .getOrderPeer(PManager.getKey(), order?.id!!)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    showLoading()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    load = true
                    if (it.code == 0) {
                        it.result.let { i ->
                            if (i!!.size == 0) {
                                adapter.clearData()
                            } else {
                                adapter.setData(it.result!!)
                            }
                        }
                    } else {
                        adapter.clearData()
                        AlertDialog.Builder(this)
                            .setTitle(resources.getString(R.string.error))
                            .setMessage(resources.getString(R.string.unknown_error))
                            .show()

                    }
                    showActivity()
                }, {
                    if(!load)
                    showError()
                })
        )
    }

    private fun setData() {

        tvEmporiumName.text = order?.shop?.title
        tvDeliverDate.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(order?.orderTime!!))
        tvOrderCost.text = String.format(resources.getString(R.string.order_price), order?.totalPrice)
        when (order?.paymentType) {
            0 -> tvPaymentType.text = tvPaymentType.resources.getString(R.string.cash)
            1 -> tvPaymentType.text = tvPaymentType.resources.getString(R.string.cash_card)
            2 -> tvPaymentType.text = tvPaymentType.resources.getString(R.string.transfer)
        }
        when (order?.status) {
            0 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.delevired)
            1 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.cancelled)
            2 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.process)
            3 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.accepted)
            4 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.received)
        }    }

    private fun showError() {
        vLoading.visibility = View.GONE
        vError.visibility = View.VISIBLE
    }

    private fun showLoading() {
        vLoading.visibility = View.VISIBLE
        vError.visibility = View.GONE
    }

    private fun showActivity() {
        vLoading.visibility = View.GONE
        vError.visibility = View.GONE
    }

    override fun onStart() {
        getData()
        super.onStart()
    }

    override fun onStop() {
        compositeDisposable.dispose()
        super.onStop()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
