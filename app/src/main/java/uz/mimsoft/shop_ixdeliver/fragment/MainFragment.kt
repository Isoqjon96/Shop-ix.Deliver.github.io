package uz.mimsoft.shop_ixdeliver.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.view_error.*
import uz.mimsoft.shop_ixdeliver.R

class MainFragment : Fragment() {

    companion object {
        private val fragment = MainFragment()
        fun getInstance() = fragment
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = resources.getString(R.string.main)

        tvRetry.setOnClickListener {
//            getData()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }



    private fun getAnimator( end: Int): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(0, end)
        valueAnimator.duration = 500
        return valueAnimator
    }

    private fun showError() {
        vLoading.visibility = View.GONE
        vError.visibility = View.VISIBLE
    }

    private fun showLoading() {
        vLoading.visibility = View.VISIBLE
        vError.visibility = View.GONE
    }

    private fun showFragment() {
        vLoading.visibility = View.GONE
        vError.visibility = View.GONE
    }

    override fun onResume() {
//        getData()
        super.onResume()
    }
}