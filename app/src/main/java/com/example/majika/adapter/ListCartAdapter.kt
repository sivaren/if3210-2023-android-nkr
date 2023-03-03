package com.example.majika.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Fnb
import com.example.majika.view.PaymentFragment
import kotlinx.coroutines.Job
import java.text.NumberFormat
import java.util.*

class ListCartAdapter(
    val increaseClickListener: CartItemIncreaseListener,
    val decreaseClickListener: CartItemDecreaseListener
) : ListAdapter<Fnb, ListCartAdapter.CartViewHolder>(CartComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val fnb = getItem(position)
        holder.increaseCountBtn.setOnClickListener{
            increaseClickListener.onIncreaseClick(fnb.fnbName, fnb.fnbPrice.toString())
        }
        holder.decreaseCountBtn.setOnClickListener{
            decreaseClickListener.onDecreaseClick(fnb.fnbName, fnb.fnbPrice.toString())
        }
        holder.bind(fnb.fnbName, fnb.fnbPrice, fnb.fnbQuantity)
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cartName: TextView = itemView.findViewById(R.id.cartName)
        private val cartPrice: TextView = itemView.findViewById(R.id.cartPrice)
        private val cartQuantity: TextView = itemView.findViewById(R.id.cartQuantity)
        val increaseCountBtn: ImageView = itemView.findViewById(R.id.increaseCountBtn)
        val decreaseCountBtn: ImageView = itemView.findViewById(R.id.decreaseCountBtn)

        fun bind(
            name: String?, price: Int?, quantity: Int?
        ) {
            // embed with local currency format
            val localelId = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localelId)
            val priceText: String = formatRupiah.format(price).toString()

            cartName.text = name
            cartPrice.text = priceText.substring(0, priceText.length - 3)
            cartQuantity.text = quantity.toString()
        }

        companion object {
            fun create(parent: ViewGroup): CartViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_cart_item, parent, false)
                return CartViewHolder(view)
            }
        }
    }

    class CartComparator : DiffUtil.ItemCallback<Fnb>() {
        override fun areItemsTheSame(oldItem: Fnb, newItem: Fnb): Boolean {
            return oldItem.fnbName == newItem.fnbName
        }

        override fun areContentsTheSame(oldItem: Fnb, newItem: Fnb): Boolean {
            return oldItem.fnbQuantity == newItem.fnbQuantity
        }
    }
}

class CartItemIncreaseListener(
    val clickListener: (name: String, price: String) -> Job
) {
    fun onIncreaseClick(cartName: String, cartPrice: String) = clickListener.invoke(cartName, cartPrice)
}
class CartItemDecreaseListener(
    val clickListener: (name: String, price: String) -> Unit?
) {
    fun onDecreaseClick(cartName: String, cartPrice: String) = clickListener.invoke(cartName, cartPrice)
}
