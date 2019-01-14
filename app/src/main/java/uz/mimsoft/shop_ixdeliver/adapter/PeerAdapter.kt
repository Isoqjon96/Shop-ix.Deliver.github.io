package uz.mimsoft.shop_ixdeliver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.mimsoft.shop_ixdeliver.Peer
import uz.mimsoft.shop_ixdeliver.R
import uz.shopix.agentapp.SERVER_IMAGES_ADDRESS

class PeerAdapter:RecyclerView.Adapter<PeerAdapter.PeerHolder>(){
    private var data = ArrayList<Peer>()
    fun setData(data: ArrayList<Peer>) {
        this.data = data
        notifyDataSetChanged()
    }
    fun clearData(){
        this.data.clear()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_peer, parent, false)
        return PeerHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PeerHolder, position: Int) {
        holder.bindItem(data[position])
    }

    inner class PeerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPeerName = itemView.findViewById<AppCompatTextView>(R.id.tvPeerName)
        private val ivPeer = itemView.findViewById<AppCompatImageView>(R.id.ivPeer)
        private val tvUnitMeasure = itemView.findViewById<AppCompatTextView>(R.id.tvUnitMeasure)
        private val tvOnePeerPrice = itemView.findViewById<AppCompatTextView>(R.id.tvOnePeerPrice)
        private val tvPeerCount = itemView.findViewById<AppCompatTextView>(R.id.tvPeerCount)
        private val tvBonus = itemView.findViewById<AppCompatTextView>(R.id.tvBonus)
        private val tvOrderPrice = itemView.findViewById<AppCompatTextView>(R.id.tvOrderPrice)

        fun bindItem(peer: Peer) {
            tvPeerName.text = peer.product!!.name
            Picasso.get()
                .load(SERVER_IMAGES_ADDRESS + peer.product.image)
                .placeholder(R.drawable.ic_alert)
                .into(ivPeer)

            tvUnitMeasure.text =  peer.product.type

            tvOnePeerPrice.text = String.format(tvOnePeerPrice.resources.getString(R.string.order_price), peer.product.price)
            tvPeerCount.text = peer.count.toString()
            if (peer.product.hasDiscount!!) {
                tvBonus.text = peer.discountPrice.toString()
            } else {
                tvBonus.text = "Нет бонус"
            }
            tvOrderPrice.text = String.format(tvOnePeerPrice.resources.getString(R.string.order_price), peer.totalPrice)

        }
    }

}