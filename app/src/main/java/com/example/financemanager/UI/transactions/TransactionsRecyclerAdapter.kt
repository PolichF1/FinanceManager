package com.example.financemanager.UI.transactions

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.data.models.Account
import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.DayInfo
import com.example.financemanager.data.models.Transaction
import com.example.financemanager.data.useCases.AccountsUseCases
import com.example.financemanager.data.useCases.CategoryUseCases
import com.example.financemanager.data.useCases.TransactionUseCases
import com.example.financemanager.databinding.DayInfoItemBinding
import com.example.financemanager.databinding.TransactionItemBinding
import com.example.financemanager.toAmountFormat
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TransactionsRecyclerAdapter @Inject constructor(
    private val context: Context,
    private val transactionUseCases: TransactionUseCases,
    private val accountUseCases: AccountsUseCases,
    private val categoryUseCases: CategoryUseCases
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactionsWithInfoList = emptyList<Any>()
    private var accountsList = emptyList<Account>()
    private var categoriesList = emptyList<Category>()

    inner class TransactionViewHolder(
        private val binding: TransactionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            val currentCategory = categoriesList.find { category ->
                category.id == transaction.categoryId
            }
            val currentAccount = accountsList.find { account ->
                account.id == transaction.accountId
            }

            if (currentCategory != null && currentAccount != null) {
                binding.transactionName.text = transaction.name
                binding.cardName.text = currentAccount.name
                binding.icon.setImageResource(currentCategory.icon)
                binding.iconBackground.setImageResource(currentCategory.iconColor)
            }
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

            binding.amount.text = amount.toAmountFormat()
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
                val transaction = transactionsWithInfoList[position] as Transaction
                viewHolder.bind(transaction)
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
        return if (transactionsWithInfoList[position] is Transaction) TRANSACTION_VIEW_TYPE else DAY_INFO_VIEW_TYPE
    }

    suspend fun updateData(){
        transactionsWithInfoList = transactionUseCases.getTransactionListForRecyclerView()
        accountsList = accountUseCases.getAccounts().first()
        categoriesList = categoryUseCases.getCategories().first()
        notifyDataSetChanged()
    }

    companion object {
        private const val TRANSACTION_VIEW_TYPE = 1
        private const val DAY_INFO_VIEW_TYPE = 0
    }


}