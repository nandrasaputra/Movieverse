package com.nandra.movieverse.widget

/*
class StackWidgetRemoteViewFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private var cursor: Cursor? = null
    private var listData: List<Film>? = null
    private val authority = "com.nandra.movieverse.provider"
    private val scheme = "content"
    private var fetchDataJob: Job = Job()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val MOVIE_CONTENT_URI = Uri.Builder()
        .scheme(scheme)
        .authority(authority)
        .appendPath("movie")
        .build()

    override fun onCreate() {
        fetchDataJob = scope.launch {
            cursor = mContext.contentResolver.query(MOVIE_CONTENT_URI, null, null, null, null)
            listData = cursor!!.toArrayList()
            MovieverseWidgetProvider.refreshAppWidget(mContext)
            fetchDataJob.join()
        }
    }

    override fun onDataSetChanged() {
        if (cursor != null) {
            cursor!!.close()
        }

        val identityToken = Binder.clearCallingIdentity()

        // querying ke database
        cursor = mContext.contentResolver.query(MOVIE_CONTENT_URI, null, null, null, null)

        Binder.restoreCallingIdentity(identityToken)
    }

    override fun getViewAt(position: Int): RemoteViews {
        if (fetchDataJob.isActive) {
            scope.launch {
                fetchDataJob.join()
            }
        }
        val currentMovie = listData!![position]
        val url = "https://image.tmdb.org/t/p/w185"
        val remoteView = RemoteViews(mContext.packageName, R.layout.item_stack_widget)
        var bitmap: Bitmap? = null
        try {
            bitmap = Glide.with(mContext)
                .asBitmap()
                .load(url + currentMovie.posterPath)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get()
        } catch (exception: Exception) { }

        remoteView.setImageViewBitmap(R.id.item_stack_poster, bitmap)
        val extra = bundleOf(
            MovieverseWidgetProvider.EXTRA_ITEM to position
        )
        val intent = Intent().apply {
            putExtras(extra)
        }

        remoteView.setOnClickFillInIntent(R.id.item_stack_poster, intent)
        return remoteView
    }

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}

    override fun getCount(): Int {
        if (fetchDataJob.isActive) {
            scope.launch {
                fetchDataJob.join()
            }
        }
        return listData?.size ?: 0
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    private fun Cursor.toArrayList() : ArrayList<Film> {

        val filmList = ArrayList<Film>()

        while (this.moveToNext()) {
            val id = this.getString(this.getColumnIndexOrThrow("id"))
            val title = this.getString(this.getColumnIndexOrThrow("title"))
            val posterPath = this.getStringOrNull(this.getColumnIndexOrThrow("posterPath"))
            filmList.add(Film(0.0, 0, false, posterPath, id.toInt(), false,
                "", "", "", listOf(), title, 0.0, "",
                "", "", "", "", ""))
        }
        return filmList
    }
}*/
