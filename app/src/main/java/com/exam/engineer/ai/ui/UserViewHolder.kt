package com.exam.engineer.ai.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exam.engineer.ai.R
import com.exam.engineer.ai.api.UserApi
import com.exam.engineer.ai.util.GlideApp

/**
 * A RecyclerView ViewHolder that displays a User post.
 */
class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.tvTitle)
    private val ivUser: ImageView = view.findViewById(R.id.ivUser)
    private val rvSubList: RecyclerView = view.findViewById(R.id.rvSubList)

    fun bind(users: UserApi.ListingResponse.Data.Users?) {

        title.text = "${users?.name}"
        GlideApp.with(itemView)
                .load(users?.image)
                .placeholder(android.R.color.darker_gray)
                .circleCrop()
                .into(ivUser)

        rvSubList.layoutManager = GridLayoutManager(itemView.context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when {
                        users?.items.orEmpty().count() % 2 == 0 -> 1
                        else -> when (position) {
                            0 -> 2
                            else -> 1
                        }
                    }
                }
            }
        }
        rvSubList.isNestedScrollingEnabled = false
        rvSubList.adapter = UsersPostAdapter(users?.items)
    }

    companion object {
        fun create(parent: ViewGroup): UserViewHolder {
            return UserViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_user_list, parent, false)
            )
        }
    }
}