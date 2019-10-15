package com.nandra.movieverse.data

import androidx.paging.PageKeyedDataSource
import com.nandra.movieverse.network.Film

class DiscoverMovieDataSource : PageKeyedDataSource<Int, Film>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Film>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Film>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}