package com.exam.engineer.ai.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.exam.engineer.ai.api.UserApi
import com.exam.engineer.ai.util.NetworkState
import com.exam.engineer.ai.R

/**
 * A simple adapter implementation that shows User posts.
 */
class UserAdapter(
    private val retryCallback: () -> Unit
) : PagedListAdapter<UserApi.ListingResponse.Data.Users, RecyclerView.ViewHolder>(POST_COMPARATOR) {
    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_user_list -> (holder as UserViewHolder).bind(
                getItem(position)
            )
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user_list -> UserViewHolder.create(parent)
            R.layout.network_state_item -> NetworkStateItemViewHolder.create(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.item_user_list
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<UserApi.ListingResponse.Data.Users>() {
            override fun areContentsTheSame(
                oldItem: UserApi.ListingResponse.Data.Users,
                newItem: UserApi.ListingResponse.Data.Users
            ): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(
                oldItem: UserApi.ListingResponse.Data.Users,
                newItem: UserApi.ListingResponse.Data.Users
            ): Boolean =
                oldItem.name == newItem.name
        }
    }
}
