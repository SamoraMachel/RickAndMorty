package com.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickandmorty.network.CharacterLoadingAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_state.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView() {
        rickandMortyRecycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)

            recyclerViewAdapter = RecyclerViewAdapter()

            recyclerViewAdapter.withLoadStateHeaderAndFooter(
                header = CharacterLoadingAdapter(recyclerViewAdapter),
                footer = CharacterLoadingAdapter(recyclerViewAdapter)
            )

            lifecycleScope.launchWhenCreated {
                recyclerViewAdapter.loadStateFlow.collectLatest { loadStates ->
                    progress_bar.isVisible = loadStates.refresh is LoadState.Loading
                    retry_button.isVisible = loadStates.refresh !is LoadState.Loading
                    error_msg.isVisible = loadStates.refresh is LoadState.Error
                }
            }
            adapter = recyclerViewAdapter
        }



    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        lifecycleScope.launch {
            viewModel.getListData().collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
}