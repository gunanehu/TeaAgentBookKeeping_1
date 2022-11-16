package com.teaagent.ui.listEntries

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teaagent.R
import com.teaagent.domain.firemasedbEntities.TradeAnalysis
import util.GeneralUtils.Companion.convertDisplayDate

class TradeListNewAdapter(
    private val data: List<TradeAnalysis>,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TradeListNewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trade_list_row, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tvStockName.text = data[position].stockName
        holder.tvIncomeTrade.text = data[position].tradeIncomeType
        val isBuy = data[position].isBuy
        val entryPrice = data[position].EntryPrice
        val exitPrice = data[position].ExitPrice
        val qntty = data[position].quantity
        if (isBuy) {
            holder.tvbuySold.text = "BUY"
        } else {
            holder.tvbuySold.text = "SELL"
        }
        if (!entryPrice!!.isEmpty() && !exitPrice!!.isEmpty() && qntty!! > 0) {
            val entry = java.lang.Long.valueOf(entryPrice)
            val exit = java.lang.Long.valueOf(exitPrice)
            if (isBuy) { //BUY
                val profit = (exit - entry) * qntty
                if (exit - entry > 0) {
                    holder.tvProfitOrLoss.text = "PROFIT :$profit"
                    holder.tvProfitOrLoss.setTextColor(Color.GREEN)
                } else {
                    holder.tvProfitOrLoss.text = "LOSS :$profit"
                    holder.tvProfitOrLoss.setTextColor(Color.RED)
                }
            } else { //sell
                val profit = (entry - exit) * qntty
                if (entry - exit > 0) {
                    holder.tvProfitOrLoss.text = "PROFIT :$profit"
                    holder.tvProfitOrLoss.setTextColor(Color.GREEN)
                } else {
                    holder.tvProfitOrLoss.text = "LOSS :$profit"
                    holder.tvProfitOrLoss.setTextColor(Color.RED)
                }
            }
        }
        holder.tvbuyPrice.text = data[position].EntryPrice
        holder.tvbuyQntty.text = "" + data[position].quantity
        holder.tvsellPrice.text = data[position].ExitPrice
        if (data[position].timestampTradePlanned != null) {
            val milli = java.lang.Long.valueOf(data[position].timestampTradePlanned)
            val date = convertDisplayDate(milli)
            holder.tvdate.text = date
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    class ViewHolder(view: View, itemClickListener: ItemClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var itemClickListener: ItemClickListener
        val tvStockName: TextView
        val tvIncomeTrade: TextView
        val tvbuySold: TextView
        val tvbuyPrice: TextView
        val tvbuyQntty: TextView
        val tvsellPrice: TextView
        val tvdate: TextView
        val tvProfitOrLoss: TextView
        override fun onClick(v: View) {
            itemClickListener.onClick(absoluteAdapterPosition)
        }

        init {
            tvStockName = view.findViewById<View>(R.id.tvStockName) as TextView
            tvIncomeTrade = view.findViewById<View>(R.id.tvIncomeTrade) as TextView
            tvbuySold = view.findViewById<View>(R.id.tvbuySold) as TextView
            tvbuyPrice = view.findViewById<View>(R.id.tvbuyPrice) as TextView
            tvbuyQntty = view.findViewById<View>(R.id.tvbuyQntty) as TextView
            tvsellPrice = view.findViewById<View>(R.id.tvsellPrice) as TextView
            tvdate = view.findViewById<View>(R.id.tvdate) as TextView
            tvProfitOrLoss = view.findViewById<View>(R.id.tvProfitOrLoss) as TextView
            view.setOnClickListener(this)
            this.itemClickListener = itemClickListener
        }
    }
}