package com.example.financemanager.UI.accounts

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.R
import com.example.financemanager.data.models.Account
import com.example.financemanager.databinding.AccountItemBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountsRecyclerAdapter @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : ListAdapter<Account, AccountsRecyclerAdapter.AccountViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class AccountViewHolder(private val binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            binding.title.text = account.name
            binding.amount.text = account.amount.toAmountFormat(withMinus = false)

            binding.currency.text = sharedPreferences.getString(
                "currency",
                context.resources.getStringArray(R.array.currency_values)[0]
            )

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                Color.parseColor(account.color)
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
            return oldItem.id == newItem.id
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