package uz.mimsoft.shop_ixdeliver.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.view_error.*
import uz.mimsoft.shop_ixdeliver.ApiFactory
import uz.mimsoft.shop_ixdeliver.Order
import uz.mimsoft.shop_ixdeliver.PManager
import uz.mimsoft.shop_ixdeliver.R
import uz.mimsoft.shop_ixdeliver.adapter.OrderAdapter
import uz.mimsoft.shop_ixdeliver.ui.OrderDetailActivity
import uz.shopix.agentapp.INTENT_ORDER

class OrdersFragment : Fragment(), OrderAdapter.CallBack {

    companion object {
        private val fragment = OrdersFragment()
        fun getInstance() = fragment
    }

    private val adapter = OrderAdapter()
    private var compositeDisposable = CompositeDisposable()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        adapter.setCallBack(this)

        tvRetry.setOnClickListener {
            getData()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onItemClick(order: Order) {
        startActivity(Intent(requireContext(), OrderDetailActivity::class.java).putExtra(INTENT_ORDER, order))
    }

    private fun getData() {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            ApiFactory
                .getApiService()
                .getOrders(PManager.getKey())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    showLoading()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == 0) {
                        it.result.let { i ->
                            if (i!!.size == 0) {
                                adapter.clearData()
                                tvEmptyData.visibility = View.VISIBLE
                            } else {
                                tvEmptyData.visibility = View.GONE
                                adapter.setData(it.result!!)
                            }
                        }
                    } else {
                        adapter.clearData()
                        AlertDialog.Builder(requireContext())
                            .setTitle(resources.getString(R.string.error))
                            .setMessage(resources.getString(R.string.unknown_error))
                            .show()

                    }
                    showFragment()
                }, {
                    showError()
                })
        )
    }

    private fun showLoading() {
        vLoading.visibility = View.VISIBLE
        vError.visibility = View.GONE
    }

    private fun showError() {
        vLoading.visibility = View.GONE
        vError.visibility = View.VISIBLE
    }

    private fun showFragment() {
        vLoading.visibility = View.GONE
        vError.visibility = View.GONE
    }

    override fun onStop() {
        compositeDisposable.dispose()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        getData()
        super.onStart()
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}