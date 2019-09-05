package com.exam.engineer.ai.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UsersPostAdapter(var items: List<String>?) :
        RecyclerView.Adapter<UsersPostHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersPostHolder {
        return UsersPostHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UsersPostHolder, position: Int) {
        holder.bind(items?.get(position))
    }

    override fun getItemCount(): Int {
        return items?.count() ?: 0
    }
}