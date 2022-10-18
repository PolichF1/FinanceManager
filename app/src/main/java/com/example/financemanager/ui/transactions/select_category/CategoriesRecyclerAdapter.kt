package com.example.financemanager.ui.transactions.select_category

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.DateUtils.toAmountFormat
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.databinding.CategoryItemBinding
import com.example.financemanager.utils.Utils
import com.example.financemanager.utils.Utils.setIcon
import com.example.financemanager.utils.Utils.setTint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRecyclerAdapter @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ListAdapter<CategoryView, CategoriesRecyclerAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryView: CategoryView) {
            with(binding) {
                icon.setIcon(categoryView.icon)
                iconBackground.setTint(categoryView.iconColor)

                name.text = categoryView.name
                amount.text = categoryView.amount.toAmountFormat(withMinus = false)
                currency.text = sharedPreferences.getString(Utils.CURRENCY_PREFERENCE_KEY, Utils.MAIN_CURRENCY)

                name.isSelected = true
            }

            itemView.alpha = if (categoryView.amount == 0.0) 0.3f else 1f
            itemView.setOnClickListener {
                onClickListener?.onClick(categoryView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(layoutInflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class OnClickListener(val clickListener: (category: CategoryView) -> Unit) {
        fun onClick(category: CategoryView) = clickListener(category)
    }

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<CategoryView>() {
        override fun areItemsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
}