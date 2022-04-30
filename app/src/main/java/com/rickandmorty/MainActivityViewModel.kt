package com.rickandmorty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rickandmorty.network.CharacterData
import com.rickandmorty.network.RetroInstance
import com.rickandmorty.network.RetroService
import kotlinx.coroutines.flow.Flow

class MainActivityViewModel : ViewModel() {

    lateinit var retroService: RetroService

    init {
        retroService = RetroInstance.getRetroInstance().create(RetroService::class.java)
    }

    fun getListData() : Flow<PagingData<CharacterData>> {
        return Pager(config = PagingConfig(pageSize = 34, maxSize = 200),
        pagingSourceFactory = {CharacterPagingSource(retroService)}).flow.cachedIn(viewModelScope)
    }
}