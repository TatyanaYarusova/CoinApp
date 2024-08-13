package com.example.coinapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.coinapp.domain.entity.Coin

object CoinDiffCallback : DiffUtil.ItemCallback<Coin>() {
    override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
        return oldItem.id == newItem.id
    }
}