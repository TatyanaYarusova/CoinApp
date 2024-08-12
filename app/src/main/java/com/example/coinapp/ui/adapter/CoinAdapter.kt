package com.example.coinapp.ui.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.ListAdapter
import com.example.coinapp.R
import com.example.coinapp.databinding.CoinItemBinding
import com.example.coinapp.domain.entity.Coin
import com.example.coinapp.utils.formatNumber
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CoinAdapter @Inject constructor(
    private val app: Application,
    private var currency: String = CURRENCY_USD
) : ListAdapter<Coin, CoinViewHolder>(CoinDiffCallback) {

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = CoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = getItem(position)
        with(holder.binding) {
            Picasso.get().load(coin.icon).into(image)
            textName.text = coin.name
            textSymbol.text = coin.symbol.uppercase()
            var symbol = getString(app.applicationContext, R.string.symbol_usd)
            if(currency == CURRENCY_RUB) {
                symbol = getString(app.applicationContext, R.string.symbol_rub)
            }
            textPrice.text = String.format(
                app.resources.getString(R.string.format_price),
                symbol,
                coin.price.formatNumber()
            )
            if (coin.percent > 0) {
                textPercent.text = String.format(
                    app.resources.getString(R.string.plus_percent_coin),
                    coin.percent.formatNumber()
                )
                textPercent.setTextColor(getColor(app.applicationContext, R.color.plus_percent_text))
            } else {
                textPercent.text = String.format(
                    app.resources.getString(R.string.minus_percent_coin),
                    coin.percent.formatNumber()
                )
                textPercent.setTextColor(getColor(app.applicationContext, R.color.minus_percent_text))
            }
        }

        holder.itemView.setOnClickListener {
            onCoinClickListener?.onCoinClick(coin)
        }
    }

    fun changeCurrency(newCurrency: String) {
        currency = newCurrency
    }

    interface OnCoinClickListener {
        fun onCoinClick(coin: Coin)
    }

    companion object {
        private const val CURRENCY_USD = "USD"
        private const val CURRENCY_RUB = "RUB"
    }
}