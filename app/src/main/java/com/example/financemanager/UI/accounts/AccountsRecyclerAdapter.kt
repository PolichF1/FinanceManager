package com.example.financemanager.UI.accounts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.R
import com.example.financemanager.data.models.Account
import com.example.financemanager.databinding.AccountItemBinding
import com.example.financemanager.toAmountFormat
import com.example.financemanager.utils.mapOfColors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountsRecyclerAdapter @Inject constructor (
    private val context: Context) :
    ListAdapter<Account, AccountsRecyclerAdapter.AccountViewHolder>(DIFF_CALLBACK){

    private var onClickListener: OnClickListener? = null

    inner class AccountViewHolder(private val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            binding.title.text = account.name
            binding.amount.text = account.amount.toAmountFormat()

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                ContextCompat.getColor(
                    context,
                    mapOfColors[account.color] ?: R.color.orange_red
                )
            )

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


    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class OnClickListener(val clickListener : (account: Account) -> Unit) {
        fun onClick(account: Account) = clickListener(account)
    }
}