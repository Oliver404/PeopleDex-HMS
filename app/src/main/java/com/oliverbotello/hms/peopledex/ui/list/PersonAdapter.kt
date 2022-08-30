package com.oliverbotello.hms.peopledex.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oliverbotello.hms.peopledex.model.PersonEnt

class PersonAdapter(
    private val lstItems: List<PersonEnt>,
    private val onItemSelect: OnItemSelect<PersonEnt>?) : RecyclerView.Adapter<PersonVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonVH =
        PersonVH.newInstance(parent, onItemSelect)

    override fun onBindViewHolder(holder: PersonVH, position: Int) {
        holder.bind(lstItems.get(position))
    }

    override fun getItemCount(): Int =  lstItems.size
}