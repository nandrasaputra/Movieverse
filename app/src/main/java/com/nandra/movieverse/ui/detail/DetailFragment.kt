package com.nandra.movieverse.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.FilmDetail
import com.endiar.movieverse.core.utils.Constant
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.CastRecyclerViewAdapter
import com.nandra.movieverse.adapter.ImagesRecyclerViewAdapter
import com.nandra.movieverse.adapter.VideosRecyclerAdapter
import com.nandra.movieverse.util.FilmType
import com.nandra.movieverse.util.formatDate
import com.nandra.movieverse.util.setVisibilityGone
import com.nandra.movieverse.util.setVisibilityVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var filmType: FilmType

    private lateinit var imagesRecyclerViewAdapter: ImagesRecyclerViewAdapter
    private lateinit var castRecyclerViewAdapter: CastRecyclerViewAdapter
    private lateinit var videosRecyclerAdapter: VideosRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupView()
    }

    private fun observeData() {
        detailViewModel.detailRemoteMediatorLiveData.observe(viewLifecycleOwner) { resource ->
            if (resource != null) {
                when (resource) {
                    is Resource.Loading -> {
                        fragment_detail_scroll_view.setVisibilityGone()
                        detail_shimmer.setVisibilityVisible()
                    }
                    is Resource.Success -> {
                        bindRemoteData(resource.data)
                        fragment_detail_scroll_view.setVisibilityVisible()
                        detail_shimmer.setVisibilityGone()

                    }
                    is Resource.Error -> {
                        fragment_detail_scroll_view.setVisibilityGone()
                        detail_shimmer.setVisibilityVisible()
                        Toast.makeText(requireContext(), "${resource.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        detailViewModel.detailLocalDataMediatorLiveData.observe(viewLifecycleOwner) {
            handleLocalData(it)
        }
    }

    private fun setupView() {
        val id = args.id
        filmType = when(args.mediaType) {
            Constant.MOVIE_FILM_TYPE -> FilmType.FilmTypeMovie
            else -> FilmType.FilmTypeTV
        }

        fragment_detail_toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back_toolbar)
            setNavigationOnClickListener { activity?.onBackPressed() }
        }

        imagesRecyclerViewAdapter = ImagesRecyclerViewAdapter()
        castRecyclerViewAdapter = CastRecyclerViewAdapter()
        videosRecyclerAdapter = VideosRecyclerAdapter()

        detail_images_recyclerview.apply {
            adapter = imagesRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        detail_cast_recyclerview.apply {
            adapter = castRecyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        detail_videos_recyclerview.apply {
            adapter = videosRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        detailViewModel.getDetailData(id, filmType)

        detail_favorite_section.setOnClickListener {
            detailViewModel.toggleFavorite()
        }
    }

    private fun bindRemoteData(data: FilmDetail?) {
        data?.let {
            when (filmType) {
                is FilmType.FilmTypeMovie -> {
                    detail_text_movie_title.text = it.movieTitle
                    detail_text_release_date.text = formatDate(it.movieReleaseDate)
                    val runtime = "${it.runtime} Minutes"
                    detail_text_runtime.text = runtime
                }
                else -> {
                    detail_text_movie_title.text = it.tvTitle
                    detail_text_release_date.text = formatDate(it.tvAirDate)
                    val totalEpisodes = "${it.totalEpisode} Episodes"
                    detail_text_runtime.text = totalEpisodes
                }
            }

            val rating = "${it.voteAverage} / 10"
            detail_text_movie_rating.text = rating
            detail_text_movie_genre.text = it.genre.toString()
            if(it.overview == ""){
                val text = getString(R.string.overview_not_available_en)
                detail_text_movie_overview.text = text
            } else {
                detail_text_movie_overview.text = data.overview
            }

            val voteCount = "From ${data.voteCount} Votes"
            detail_text_movie_rating_count.text = voteCount

            val url = "https://image.tmdb.org/t/p/w342"
            val backdropUrl = "https://image.tmdb.org/t/p/w500"
            if(it.posterImagePath.isNotEmpty()) {
                Glide.with(this)
                    .load(url + it.posterImagePath)
                    .into(detail_image_movie_poster)
            } else {
                Glide.with(this)
                    .load(R.drawable.img_back_portrait_default)
                    .into(detail_image_movie_poster)
            }

            if(it.backdropImagePath.isNotEmpty()) {
                Glide.with(this)
                    .load(backdropUrl + it.backdropImagePath)
                    .into(detail_backdrop)
            } else {
                Glide.with(this)
                    .load(R.drawable.img_back_landscape_default)
                    .into(detail_backdrop)
            }

            if (it.imagePathList.isNotEmpty()) {
                imagesRecyclerViewAdapter.submitList(it.imagePathList)
            } else {
                detail_gallery_text.setVisibilityGone()
            }

            if (it.castList.isNotEmpty()) {
                castRecyclerViewAdapter.submitList(it.castList)
            } else {
                detail_fragment_cast_text.setVisibilityGone()
            }

            if (it.videoKeyList.isNotEmpty()) {
                videosRecyclerAdapter.submitList(it.videoKeyList)
            } else {
                detail_video_text.setVisibilityGone()
            }
        }
    }

    private fun handleLocalData(favoriteStatus: Boolean?) {
        favoriteStatus?.let { isFavorite ->
            if (isFavorite) {
                Glide.with(requireContext())
                    .load(R.drawable.ic_heart_pink)
                    .into(detail_image_heart)
                detail_favorite_text.text = getString(R.string.favorite_text_remove_to_favorite_en)
            } else {
                Glide.with(requireContext())
                    .load(R.drawable.ic_heart_hollow)
                    .into(detail_image_heart)
                detail_favorite_text.text = getString(R.string.favorite_text_add_to_favorite_en)
            }
        }
    }
}