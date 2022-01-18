package com.siuzannasmolianinova.hw36.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.siuzannasmolianinova.hw36.databinding.FragmentBlankBinding
import com.siuzannasmolianinova.hw36.databinding.FragmentNewsBinding
import com.siuzannasmolianinova.hw36.presentation.view_model.TechnologyNewsViewModel


class TechNewsFragment : Fragment() {
    private var _binding: FragmentBlankBinding? = null
    private val binding: FragmentBlankBinding get() = _binding!!
    private val viewModel: TechnologyNewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.text.text = "Technology News"
    }
}