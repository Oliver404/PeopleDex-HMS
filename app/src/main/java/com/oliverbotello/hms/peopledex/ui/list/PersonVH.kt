package com.oliverbotello.hms.peopledex.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oliverbotello.hms.peopledex.R

class PersonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): PersonVH =
            PersonVH(LayoutInflater.from(parent.context).inflate(R.layout.person_vh_layout, parent, false))
    }

    fun bind() {

    }
}