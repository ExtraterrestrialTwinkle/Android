package com.smolianinovasiuzanna.testapp.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smolianinovasiuzanna.testapp.data.Course
import com.smolianinovasiuzanna.testapp.databinding.ItemCourseBinding
import kotlin.properties.Delegates

class CoursesListAdapter(
    private val onClick: (Long) -> Unit,
    private val onLongClick: (Long) -> Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, DiffUtilCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoursesViewHolder(
            binding,
            onClick,
            onLongClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with (holder as CoursesViewHolder) {
            val i = differ.currentList[position]
            Log.d("onBindViewHolder","Course = $i")
            holder.bind(differ.currentList[position])
        }
    }

    fun setCoursesList(courses: List<Course>) {
        differ.submitList(courses)
    }

    override fun getItemCount(): Int = differ.currentList.size

    class CoursesViewHolder(
        private val binding: ItemCourseBinding,
        onClick: (Long) -> Unit,
        onLongClick: (Long) -> Boolean
    ): RecyclerView.ViewHolder(binding.root){

        private var courseId by Delegates.notNull<Long>()

        init{
            binding.itemCourse.setOnClickListener{
                Log.d("CoursesViewHolder", "onClick, id = $courseId")
                onClick(courseId)
            }

            binding.itemCourse.setOnLongClickListener {
                Log.d("CoursesViewHolder", "onLongClick, id = $courseId")
                 onLongClick(courseId)
            }
        }

        fun bind(course: Course) {
            with(binding){
                courseId = course.id
                itemCourse.text = course.title
                Log.d("CoursesViewHolder", "bind, id = $courseId, title = ${course.title}" )
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}