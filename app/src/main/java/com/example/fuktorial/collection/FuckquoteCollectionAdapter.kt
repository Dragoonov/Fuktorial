package com.example.fuktorial.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fuktorial.database.models.Fuquote
import com.example.fuktorial.databinding.FuquoteElementBinding

class FuquoteCollectionAdapter(private var dataSet: List<Fuquote>) :
    RecyclerView.Adapter<FuquoteCollectionAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
     inner class ViewHolder(private val binding: FuquoteElementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fuquote: Fuquote) {
            binding.text.text = fuquote.text
        }
    }

    fun updateDataSet(list: List<Fuquote>) {
        dataSet = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = FuquoteElementBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(dataSet[position])
    override fun getItemCount() = dataSet.size

}
