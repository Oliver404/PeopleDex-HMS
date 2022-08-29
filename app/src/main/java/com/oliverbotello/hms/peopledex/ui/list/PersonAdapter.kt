package com.oliverbotello.hms.peopledex.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(private val lstItems: List<Any>) : RecyclerView.Adapter<PersonVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH =
        PersonVH.newInstance(parent)

    override fun onBindViewHolder(holder: PersonVH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int =  lstItems.size
}