package com.example.financemanager.UI.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.data.models.Account
import com.example.financemanager.databinding.AccountItemBinding
import com.example.financemanager.toAmountFormat

class AccountsRecyclerAdapter : RecyclerView.Adapter<AccountsRecyclerAdapter.AccountViewHolder>() {

    private var listOfAccounts = emptyList<Account>()

    class AccountViewHolder(private val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            binding.title.text = account.name
            binding.amount.text = account.amount.toAmountFormat()
            binding.currency.text = account.currency

            DrawableCompat.setTint(
                DrawableCompat.wrap(binding.iconBackground.drawable),
                account.color
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AccountItemBinding.inflate(layoutInflater, parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(listOfAccounts[position])
    }

    override fun getItemCount(): Int {
        return listOfAccounts.size
    }

    fun setData(newList: List<Account>) {
        this.listOfAccounts = newList
        notifyDataSetChanged()
    }
}