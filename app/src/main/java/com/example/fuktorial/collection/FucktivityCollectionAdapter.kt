package com.example.fuktorial.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.databinding.FucktivityElementBinding

class FucktivityCollectionAdapter(private var dataSet: List<Fucktivity>) :
RecyclerView.Adapter<FucktivityCollectionAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FucktivityElementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fucktivity: Fucktivity) {
            binding.fucktivity = fucktivity
        }
    }

    fun updateDataSet(list: List<Fucktivity>) {
        dataSet = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = FucktivityElementBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(dataSet[position])

    override fun getItemCount() = dataSet.size

}