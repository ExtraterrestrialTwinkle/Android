package com.smolianinovasiuzanna.hw28.ui

import android.Manifest
import android.app.Activity
import android.app.RemoteAction
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smolianinovasiuzanna.hw28.R
import com.smolianinovasiuzanna.hw28.VideoViewModel
import com.smolianinovasiuzanna.hw28.data.Video
import com.smolianinovasiuzanna.hw28.databinding.FragmentVideoBinding
import com.smolianinovasiuzanna.hw28.utils.haveQ
import com.smolianinovasiuzanna.hw28.utils.hideKeyboardFrom
import com.smolianinovasiuzanna.hw28.utils.showError


class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding: FragmentVideoBinding get() = _binding!!
    private val viewModel: VideoViewModel by viewModels()
    private lateinit var videoListAdapter: VideoListAdapter
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var loader: LinearLayout
    private lateinit var fileUri: Uri
    private lateinit var videoList: List<Video>
    private var title: String? = null
    private var url: String? = null
    private var flag = false
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var recoverableActionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var createDocumentLauncher: ActivityResultLauncher<String>


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionResultListener()
        initRecoverableDeleteActionListener()
        initRecoverableFavoriteActionListener()
        initCreateDocumentLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheet()
        initToolbar()
        initPermissionCallback()
        if (hasPermission().not()) {
            requestPermissions()
        }
        initList()
        bindViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePermissionState(hasPermission())
    }

    private fun initBottomSheet(){
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.bottomSheet.downoladdButton.setOnClickListener {
                            title = binding.bottomSheet.titleTextField.editText?.text.toString()
                            url = binding.bottomSheet.urlTextField.editText?.text.toString()
                            if(title.isNullOrEmpty() || url.isNullOrEmpty()) {
                                showError(Throwable("Заполните все поля"))
                            } else {
                                viewModel.downloadVideo(
                                    title = title!!,
                                    url = url!!
                                )
                            }
                        }
                        binding.bottomSheet.selectDirectoryButton.setOnClickListener {
                            title = binding.bottomSheet.titleTextField.editText?.text.toString()
                            url = binding.bottomSheet.urlTextField.editText?.text.toString()
                            if(title.isNullOrEmpty() || url.isNullOrEmpty()) {
                                showError(Throwable("Заполните все поля"))
                            } else {
                                createFile()
                            }
                        }
                    }
                    else -> {
                        binding.bottomSheet.titleTextField.editText?.text?.clear()
                        binding.bottomSheet.urlTextField.editText?.text?.clear()
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun initToolbar(){
        val toolbar = binding.appBar.toolbar

        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.download_from_network -> {
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    true
                }
                R.id.favorites -> {
                    val favorites = binding.appBar.toolbar.menu.findItem(R.id.favorites)
                    flag = !flag
                    when(flag) {
                        true -> favorites.setIcon(R.drawable.ic_favorite_24)
                        false -> favorites.setIcon(R.drawable.ic_favorite_border_24)
                    }
                    loadFavorites(flag)
                    true
                }
                else -> false
            }
        }
    }

    private fun initPermissionCallback(){
        binding.videoList.permissionIsNotGrantedTextView.setOnClickListener {
            requestPermissions()
        }
    }

    private fun hasPermission(): Boolean {
        return PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun initPermissionResultListener() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionToGrantedMap: Map<String, Boolean> ->
            if (permissionToGrantedMap.values.all { it }) {
                viewModel.permissionsGranted()
            } else {
                viewModel.permissionsDenied()
            }
        }
    }

    private fun initRecoverableDeleteActionListener() {
        recoverableActionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {  activityResult ->
            val isConfirmed = activityResult.resultCode == Activity.RESULT_OK
            if(isConfirmed) {
                viewModel.confirmDelete()
            } else {
                viewModel.declineDelete()
            }
        }
    }

    private fun initRecoverableFavoriteActionListener() {
        recoverableActionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {  activityResult ->
            val isConfirmed = activityResult.resultCode == Activity.RESULT_OK
            if(isConfirmed) {
                viewModel.confirmFavorite()
            } else {
                viewModel.declineFavorite()
            }
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(*PERMISSIONS.toTypedArray())
    }

    private fun initList(){
        videoListAdapter = VideoListAdapter( ::favorite, ::trash )
        with(binding.videoList.videoRecyclerView) {
            adapter = videoListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    Log.d("ContractorsFragment", "onSwiped")
                    val videoId = videoListAdapter.getItemId(viewHolder.bindingAdapterPosition)
                    viewModel.deleteVideoById(videoId)
                    Toast.makeText(requireContext(), "Видеофайл удален", Toast.LENGTH_LONG).show()
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.videoList.videoRecyclerView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindViewModel(){
        viewModel.videoList.observe(viewLifecycleOwner){
            videoList = it.reversed()
            videoListAdapter.setVideoList(videoList)
        }
        viewModel.error.observe(viewLifecycleOwner) { showError(it) }
        viewModel.permissionsGranted.observe(viewLifecycleOwner){ updatePermissionUi(it) }
        viewModel.recoverableDeleteAction.observe(viewLifecycleOwner){ handleRecoverableAction(it) }
        viewModel.recoverableFavoriteAction.observe(viewLifecycleOwner){ handleRecoverableAction(it)}
        viewModel.saveSuccess.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "Видеофайл загружен", Toast.LENGTH_LONG).show()
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        viewModel.loading.observe(viewLifecycleOwner){  isLoading ->
            if(!isLoading) hideLoader() else showLoader()
        }
        viewModel.favorite.observe(viewLifecycleOwner){ isFavorite ->
            videoListAdapter.notifyDataSetChanged()
        }
    }

    private fun favorite(id: Long): Boolean{
        Log.d("VideoFragment", "favorite id=$id")
        if (!haveQ()) {
            Toast.makeText(
                requireContext(),
                R.string.not_available_feature,
                Toast.LENGTH_SHORT) .show()
            return false
        }
        val media = videoList.filter{ it.id == id }
        val state = !(media.isNotEmpty() && media[0].favorite)
        viewModel.requestFavoriteMedia (media, state)
        return true
    }

    private fun trash(id: Long){
       // TODO добавить удаление в корзину
    }

    private fun loadFavorites(flag: Boolean){
        if (!haveQ()) {
            Toast.makeText(
                requireContext(),
                R.string.not_available_feature,
                Toast.LENGTH_SHORT) .show()
            return
        }
        viewModel.loadFavorites(flag)
    }
    private fun updatePermissionUi(isGranted: Boolean){
        binding.videoList.permissionIsNotGrantedTextView.isVisible = isGranted.not()
        binding.videoList.videoRecyclerView.isVisible = isGranted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleRecoverableAction(action: RemoteAction){
        val request = IntentSenderRequest.Builder(action.actionIntent.intentSender)
            .build()
        recoverableActionLauncher.launch(request)
    }

    private fun showLoader(){
        val inflater = requireActivity().layoutInflater
        val container: View = inflater.inflate(R.layout.loader, null)
        container.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
            ). apply{
                gravity = Gravity.CENTER
        }
        loader = container.findViewById<LinearLayout>(R.id.loaderContainer)
        requireContext().hideKeyboardFrom(binding.root)
        binding.root.addView(loader)
    }

    private fun hideLoader(){
        binding.root.removeView(loader)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initCreateDocumentLauncher() {
        createDocumentLauncher = registerForActivityResult(
            ActivityResultContracts.CreateDocument()
        ) { uri ->
            handleCreateFile(uri)
        }
    }

    private fun createFile() {
        createDocumentLauncher.launch(title)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun handleCreateFile(uri: Uri?) {
        if (uri == null) {
            Toast.makeText(requireContext(), "Не удалось создать файл", Toast.LENGTH_SHORT).show()
            return
        }
        fileUri = uri
        viewModel.downloadVideoToCustomDirectory(fileUri, url!!)
        Toast.makeText(requireContext(), "Создан файл: $fileUri", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
                .takeIf { haveQ().not() }
        )
    }
}