package com.example.coinapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.coinapp.databinding.CoinItemBinding
import com.example.coinapp.domain.entity.Coin
import com.squareup.picasso.Picasso

class CoinAdapter: ListAdapter<Coin, CoinViewHolder>(CoinDiffCallback) {

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = CoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = getItem(position)
        with(holder.binding){
            Picasso.get().load(coin.icon).into(image)
            textName.text = coin.name
            textSymbol.text = coin.symbol
            textPrice.text = coin.price.toString()
            textPercent.text = coin.percent.toString()
        }

        holder.itemView.setOnClickListener {
            onCoinClickListener?.onCoinClick(coin)
        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coin: Coin)
    }
}