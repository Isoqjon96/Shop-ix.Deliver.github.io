package uz.mimsoft.shop_ixdeliver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import uz.mimsoft.shop_ixdeliver.Order
import uz.mimsoft.shop_ixdeliver.R
import java.text.SimpleDateFormat
import java.util.*

class OrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data = ArrayList<Order>()
    private var callBack: CallBack? = null
    private var isOrder:Boolean = true

    fun setData(data: ArrayList<Order>) {
        this.data.clear()
        this.data = data
        notifyDataSetChanged()
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }
    fun setCallBack(callBack: CallBack){
        this.callBack = callBack
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderHolder(v)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderHolder).bindItem(data[position])
    }

    inner class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvShopName = itemView.findViewById<AppCompatTextView>(R.id.tvShopName)
        private val tvShopAddress = itemView.findViewById<AppCompatTextView>(R.id.tvShopAddress)
        private val tvDeliveryDate = itemView.findViewById<AppCompatTextView>(R.id.tvDeliveryDate)
        private val tvOrderPrice = itemView.findViewById<AppCompatTextView>(R.id.tvOrderPrice)
        private val tvPaymentType = itemView.findViewById<AppCompatTextView>(R.id.tvPaymentType)
        private val tvOrderStatus = itemView.findViewById<AppCompatTextView>(R.id.tvOrderStatus)

        fun bindItem(order: Order) {
            tvShopName.text = order.shop?.title
            tvShopAddress.text = order.shop?.address
            tvDeliveryDate.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(order.orderTime?:0))
            tvOrderPrice.text = String.format(tvOrderPrice.resources.getString(R.string.order_price), order.totalPrice)

            when (order.paymentType) {
                0 -> tvPaymentType.text = tvPaymentType.resources.getString(R.string.cash)
                1 -> tvPaymentType.text = tvPaymentType.resources.getString(R.string.cash_card)
                2 -> tvPaymentType.text = tvPaymentType.resources.getString(R.string.transfer)
            }
            when (order.status) {
                1 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.order_made)
                2 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.order_accepted)
                3 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.order_cancelled_by_admin)
                4 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.order_delivered)
                5 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.order_cancelled_by_store)
                6 -> tvOrderStatus.text = tvOrderStatus.resources.getString(R.string.order_paid)

            }
            itemView.setOnClickListener {
                callBack?.onItemClick(order)
            }
        }
    }

    interface CallBack {
        fun onItemClick(order: Order)
    }
}