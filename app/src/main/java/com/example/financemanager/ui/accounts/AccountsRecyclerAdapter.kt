package com.example.financemanager.ui.accounts

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.data.models.Account
import com.example.financemanager.databinding.AccountItemBinding
import javax.inject.Inject
import javax.inject.Singleton
import com.example.financemanager.utils.Utils.CURRENCY_PREFERENCE_KEY
import com.example.financemanager.utils.Utils.MAIN_CURRENCY
import com.example.financemanager.utils.Utils.setTint

@Singleton
class AccountsRecyclerAdapter @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ListAdapter<Account, AccountsRecyclerAdapter.AccountViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class AccountViewHolder(private val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            with(binding) {
                name.text = account.name
                amount.text = account.amount.toAmountFormat(withMinus = false)
                currency.text = sharedPreferences.getString(CURRENCY_PREFERENCE_KEY, MAIN_CURRENCY)
                iconBackground.setTint(account.color)
            }

            itemView.setOnClickListener {
                onClickListener?.onClick(account)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AccountItemBinding.inflate(layoutInflater, parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class OnClickListener(val clickListener: (account: Account) -> Unit) {
        fun onClick(account: Account) = clickListener(account)
    }

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}