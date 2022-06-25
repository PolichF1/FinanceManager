package com.example.financemanager.UI.transactions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.R
import com.example.financemanager.data.models.*
import com.example.financemanager.data.useCases.TransactionUseCases
import com.example.financemanager.databinding.DayInfoItemBinding
import com.example.financemanager.databinding.TransactionItemBinding
import com.example.financemanager.toAmountFormat
import com.example.financemanager.utils.mapOfColors
import com.example.financemanager.utils.mapOfDrawables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionsRecyclerAdapter @Inject constructor(
    private val context: Context,
    private val transactionUseCases: TransactionUseCases,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactionsWithInfoList = emptyList<Any>()

    inner class TransactionViewHolder(
        private val binding: TransactionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transactionView: TransactionView) {
            binding.categoryName.text = transactionView.categoryName
            binding.cardName.text = transactionView.accountName
            binding.textAmount.text = transactionView.amount.toAmountFormat(withMinus = true)

            binding.icon.setImageResource(
                mapOfDrawables[transactionView.icon] ?: R.drawable.ic_bank
            )

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                ContextCompat.getColor(
                    context,
                    mapOfColors[transactionView.icon_color] ?: R.color.orange_red
                )
            )
        }
    }

    inner class DayInfoViewHolder(
        private val binding: DayInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(dayInfo: DayInfo) {
            val date = dayInfo.transactionDate
            val amount = dayInfo.amountPerDay
            val monthAndYear = "${date.month} ${date.year}"

            binding.amount.text = amount.toAmountFormat(withMinus = false)
            binding.day.text = date.dayOfMonth.toString()
            binding.monthAndYear.text = monthAndYear
            binding.dayOfWeek.text = date.dayOfWeek.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TRANSACTION_VIEW_TYPE -> {
                val binding = TransactionItemBinding.inflate(layoutInflater, parent, false)
                TransactionViewHolder(binding)
            }
            else -> {
                val binding = DayInfoItemBinding.inflate(layoutInflater, parent, false)
                DayInfoViewHolder(binding)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            TRANSACTION_VIEW_TYPE -> {
                val viewHolder = holder as TransactionViewHolder
                val transactionView = transactionsWithInfoList[position] as TransactionView
                viewHolder.bind(transactionView)
            }
            else -> {
                val viewHolder = holder as DayInfoViewHolder
                val dayInfo = transactionsWithInfoList[position] as DayInfo
                viewHolder.bind(dayInfo)
            }
        }
    }

    override fun getItemCount(): Int {
        return transactionsWithInfoList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (transactionsWithInfoList[position] is TransactionView) TRANSACTION_VIEW_TYPE else DAY_INFO_VIEW_TYPE
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun updateData(transactionView: List<TransactionView>){
        transactionsWithInfoList = transactionUseCases.getTransactionListForRecyclerView(transactionView)
        notifyDataSetChanged()
    }

    companion object {
        private const val TRANSACTION_VIEW_TYPE = 1
        private const val DAY_INFO_VIEW_TYPE = 0
    }


}