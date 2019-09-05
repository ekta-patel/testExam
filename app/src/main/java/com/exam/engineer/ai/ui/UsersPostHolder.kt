package com.exam.engineer.ai.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.exam.engineer.ai.R
import com.exam.engineer.ai.util.GlideApp

class UsersPostHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val ivImageItem: ImageView = view.findViewById(R.id.ivImageItem)

    fun bind(imageUrl: String?) {
        GlideApp.with(itemView)
                .load(imageUrl)
                .placeholder(android.R.color.darker_gray)
                .into(ivImageItem)
    }

    companion object {
        fun create(parent: ViewGroup): UsersPostHolder {
            return UsersPostHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_user_sub_list, parent, false)
            )
        }
    }
}