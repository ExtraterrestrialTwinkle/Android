package com.smolianinovasiuzanna.testapp.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smolianinovasiuzanna.testapp.R
import com.smolianinovasiuzanna.testapp.databinding.CourseListFragmentBinding
import com.smolianinovasiuzanna.testapp.databinding.FragmentDialogBinding
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlin.properties.Delegates


class CoursesListFragment : Fragment(R.layout.course_list_fragment) {

    private val viewModel : CoursesListViewModel by viewModels()
    private var _binding: CourseListFragmentBinding? = null
    private val binding: CourseListFragmentBinding get() = _binding!!
    private var _dialogBinding: FragmentDialogBinding? = null
    private val dialogBinding: FragmentDialogBinding get() = _dialogBinding!!
    private var coursesAdapter: CoursesListAdapter? = null
    private var courseTitle: String? = null
    private var courseId: Long by Delegates.notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CourseListFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initList()
        bindViewModel()
        binding.addCourseButton.setOnClickListener {
            Log.d("Fragment", "addCourseButton")
            openDialog(ADD_COURSE)
        }
        binding.deleteAllCoursesButton.setOnClickListener {
            Log.d("Fragment", "deleteButton")
            deleteAllCourses()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initList(){
        Log.d("Fragment", "initList")
        coursesAdapter = CoursesListAdapter({ courseId ->
            deleteCourse(courseId) }
        ){ courseId ->
            updateCourseId(courseId)
        }
        with(binding.courses){
            adapter = coursesAdapter
            when(adapter?.itemCount){
                0 -> {
                    binding.deleteAllCoursesButton.isGone = true
                    binding.noCoursesTextView.isVisible = true
                }
                else -> {
                    binding.deleteAllCoursesButton.isVisible = true
                    binding.noCoursesTextView.isGone = true
                }
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindViewModel(){
        Log.d("Fragment", "bindViewModel")
        viewModel.getCourses()
        viewModel.courses.observe(viewLifecycleOwner){ courseList ->
            coursesAdapter?.setCoursesList(courseList)
        }
        viewModel.error.observe(viewLifecycleOwner){ message ->  showError(message)}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteCourse(id: Long){
        Log.d("Fragment", "deleteCourse: id = $id")
        viewModel.deleteCourse(id)
        if(coursesAdapter?.itemCount == 0) {
            binding.noCoursesTextView.isVisible = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourse(courseTitle: String?){

        Log.d("Fragment", "addCourse, title = $courseTitle")
        if(courseTitle != null){
            viewModel.addCourse(courseTitle)
        } else return

        if(binding.deleteAllCoursesButton.isGone){
            binding.deleteAllCoursesButton.isVisible = true
        }
        binding.noCoursesTextView.isGone = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCourseId(id: Long): Boolean{
        Log.d("Fragment", "updateCourse: id = $id")
        courseId = id
        openDialog(UPDATE_COURSE)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCourse(courseTitle: String?){
        Log.d("Fragment","updateCourse: courseTitle = $courseTitle, courseId = $courseId")
        if(courseTitle != null){
            viewModel.updateCourse(courseId, courseTitle)
        } else return
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDialog(type: String){
        Log.d("Fragment", "openDialog: type = $type")
        val dialog = AlertDialog.Builder(requireContext())
        _dialogBinding = FragmentDialogBinding.inflate(LayoutInflater.from(requireContext()))
            dialog.setView(dialogBinding.root)
                .setTitle("Input title")
            .setPositiveButton("OK", DialogInterface.OnClickListener{ _, _ ->

                when(type) {
                    ADD_COURSE -> addCourse(courseTitle)
                    UPDATE_COURSE -> updateCourse(courseTitle)
                }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            })
        dialogBinding.titleEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                courseTitle = dialogBinding.titleEditText.text.toString()
                Log.d("openDialog", "courseTitle inside = $courseTitle")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        Log.d("openDialog", "courseTitle1 = $courseTitle")
                dialog.show()
        Log.d("openDialog", "show = $courseTitle")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteAllCourses(){
        Log.d("Fragment", "deleteAllCourses")
        viewModel.deleteAllCourses()
        binding.deleteAllCoursesButton.isGone = true
        binding.noCoursesTextView.isVisible = true
    }

    private fun showError(message: String){
        Log.d("Fragment", "fun showError: message = $message")
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setIcon(R.drawable.ic_error)
            .setMessage(message)
            .setPositiveButton("OK"){ dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _dialogBinding = null
        coursesAdapter = null
    }

    companion object{
        private const val ADD_COURSE = "add course"
        private const val UPDATE_COURSE = "update course"
    }
}