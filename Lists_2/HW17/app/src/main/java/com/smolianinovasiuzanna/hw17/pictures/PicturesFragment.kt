package com.smolianinovasiuzanna.hw17
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smolianinovasiuzanna.hw17.databinding.FragmentPicturesBinding
import com.smolianinovasiuzanna.hw17.pictures.PicturesAdapter
import java.util.*
import kotlin.collections.ArrayList


class PicturesFragment: Fragment(){
    private var _binding: FragmentPicturesBinding? = null
    private val binding: FragmentPicturesBinding get() = _binding!!
    var state = PictureState(listOf())
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPicturesBinding.inflate(layoutInflater, container, false)

        Log.d("MainFragment", "onCreateView")
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
            initPictures(requireContext())

    }
    private lateinit var pictures: ArrayList<Pictures>

    private val links = arrayListOf(
        "https://fishki.net/picsw/042013/18/post/zhivotnie/tn.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0001.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0002.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0003.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0004.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0005.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0006.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0007.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0009.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0010.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0012.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0013.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0015.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0016.jpg",
        "https://fishki.net/picsw/042013/18/post/zhivotnie/zhivotnie-0017.jpg"
        )

    private fun initPictures(context: Context) {
        pictures = arrayListOf()

                for (i in links.indices) {
                    val picture = Pictures(
                        id = i+1,
                        pictureLink = links[i]
                    )
                    pictures.add(picture)
                }
    }

    private var picturesAdapter: PicturesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ListFragment", "onActivityCreated")

        if(savedInstanceState == null) {
            Log.d("savedInstanceState", "null")
            initList()
            val listOfPictures = pictures.shuffled()
            picturesAdapter?.setImages(listOfPictures)
            state = PictureState(listOfPictures)
        }
        if(savedInstanceState != null) {
            Log.d("savedInstanceState","not null")
            initList()
            state = savedInstanceState.getParcelable(KEY)?: error("error state")
            picturesAdapter?.setImages(state.picturesList)
            Log.d("isDialogOpen", "$state")
        }
        picturesAdapter?.notifyDataSetChanged()
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    private fun initList() {
        Log.d("picturesFragment", "fun initList")
        picturesAdapter = PicturesAdapter()
        with(binding.picturesList) {
            adapter = picturesAdapter
            val span = 3
            layoutManager = GridLayoutManager(requireContext(),span).apply{
                spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
                    override fun getSpanSize(position: Int): Int {
                        return if (position % 2 == 0) 1 else 2
                    }
                }
            }
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
            addItemDecoration(ItemOffsetDecoration(requireContext()))
            val items = binding.picturesList as RecyclerView

            var isLastPage: Boolean = false
            var isLoading: Boolean = false

            items.addOnScrollListener(object : PaginationScrollListener(GridLayoutManager(requireContext(), span)) {
                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    isLoading = true
                    //you have to call loadmore items to get more data
                    getMoreItems()
                }
                fun getMoreItems() {
                    //after fetching your data assuming you have fetched list in your
                    // recyclerview adapter assuming your recyclerview adapter is
                    //rvAdapter
                    val listOfPictures = {pictures + createPicturesList(10).shuffled()}as ArrayList<Pictures>
                    isLoading = false

                    (adapter as PicturesAdapter?).addData(listOfPictures)
                }

            })


//            scrollListener = object : EndlessRecyclerViewScrollListener(GridLayoutManager(requireContext(),span)) {
//                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                    // Triggered only when new data needs to be appended to the list
//                    // Add whatever code is needed to append new items to the bottom of the list
//                    loadNextDataFromApi(page)
//                }
//            }
//            // Adds the scroll listener to RecyclerView
//            items.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
        }
    }
//    fun loadNextDataFromApi(offset: Int) {
//        // Send an API request to retrieve appropriate paginated data
//        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
//        //  --> Deserialize and construct new model objects from the API response
//        //  --> Append the new data objects to the existing set of items inside the array of items
//        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
//        val listOfPictures = pictures + createPicturesList(10).shuffled()
//        picturesAdapter?.setImages(listOfPictures)
//        val count = picturesAdapter?.itemCount
//        picturesAdapter?.notifyItemRangeInserted(pictures.size -1, count?:0)
//    }
//

    fun createPicturesList(picsNum: Int): List<Pictures> {
        val newPictures: MutableList<Pictures> = ArrayList<Pictures>()
        for (i in 1..picsNum) {
            newPictures.add(
                Pictures(
                    id = Random().nextInt(),
                    links[i]
                )
            )
        }
        return newPictures
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ListFragment", "onDestroyView")
        picturesAdapter = null
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("onSaveInstanceState", "${state.picturesList}")
        outState.putParcelable(KEY, state)
    }

    companion object{
        private const val KEY = "key"

    }

}

private fun PicturesAdapter?.addData (listOfPictures: ArrayList<Pictures>) {
        val size = listOfPictures.size
        listOfPictures.addAll(listOfPictures)
        val sizeNew = listOfPictures.size
    this?.notifyItemRangeChanged(size, sizeNew)
    }

