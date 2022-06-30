package com.example.financemanager.UI.transactions.add.select_category

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.financemanager.R
import com.example.financemanager.data.models.Category
import com.example.financemanager.data.models.CategoryView
import com.example.financemanager.databinding.CategoryItemBinding
import com.example.financemanager.utils.mapOfDrawables
import javax.inject.Inject

class CategoriesRecyclerAdapter :
    ListAdapter<CategoryView, CategoriesRecyclerAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryView: CategoryView) {
            binding.name.text = categoryView.name
            binding.icon.setImageResource(mapOfDrawables[categoryView.icon] ?: R.drawable.ic_other)
            binding.amount.visibility = View.GONE

            binding.name.isSelected = true

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                Color.parseColor(categoryView.iconColor)
            )

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

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<CategoryView>() {
        override fun areItemsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class OnClickListener(val clickListener: (category: CategoryView) -> Unit) {
        fun onClick(category: CategoryView) = clickListener(category)
    }
}