package com.rickandmorty.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rickandmorty.R
import com.rickandmorty.RecyclerViewAdapter
import kotlinx.android.synthetic.main.loading_state.view.*


class CharacterLoadingAdapter(private val adapter : RecyclerViewAdapter) : LoadStateAdapter<CharacterLoadingAdapter.NetworkStateItemViewHolder>() {
    class NetworkStateItemViewHolder(val view : View, retry : () -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            view.retry_button.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            with(view) {
                progress_bar.isVisible = loadState is LoadState.Loading
                retry_button.isVisible = loadState is LoadState.Error
                error_msg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                error_msg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) = holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder =
        NetworkStateItemViewHolder(
            view = LayoutInflater.from(parent.context).inflate(R.layout.loading_state, parent, false),
        ) { adapter.retry() }
}