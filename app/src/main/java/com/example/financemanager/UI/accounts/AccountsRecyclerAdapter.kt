package com.example.financemanager.UI.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.data.models.Account
import com.example.financemanager.databinding.AccountItemBinding
import com.example.financemanager.toAmountFormat

class AccountsRecyclerAdapter (val onClickListener: OnClickListener) :
    ListAdapter<Account, AccountsRecyclerAdapter.AccountViewHolder>(DIFF_CALLBACK){

    inner class AccountViewHolder(private val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            binding.title.text = account.name
            binding.amount.text = account.amount.toAmountFormat()
            binding.currency.text = account.currency

            DrawableCompat.setTint(
                DrawableCompat.wrap(binding.iconBackground.drawable),
                account.color
            )

            itemView.setOnClickListener {
                onClickListener.onClick(account)
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


    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    class OnClickListener(val clickListener : (account: Account) -> Unit) {
        fun onClick(account: Account) = clickListener(account)
    }
}