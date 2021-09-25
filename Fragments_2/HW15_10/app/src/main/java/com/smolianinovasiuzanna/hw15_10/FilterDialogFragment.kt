package com.smolianinovasiuzanna.hw15_10

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FilterDialogFragment: DialogFragment()  {

    val tags = arrayOf(
        ArticleType.VOLUMETRIC_GLASSWARE.tag,
        ArticleType.LABORATORY_GLASSWARE.tag,
        ArticleType.SPECIAL_GLASSWARE.tag)

    var filteredTags = tags.toHashSet()

    @Throws(IllegalStateException::class)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val dialog = AlertDialog.Builder(
                requireActivity()
            )
            dialog.setTitle("Выберите вид химической посуды")
                .setMultiChoiceItems(tags, MainActivity.checkedItems) {
                        _, which, isChecked ->
                    if (isChecked) {
                        filteredTags.add(tags[which])
                        MainActivity.checkedItems[which] = true

                    } else if (filteredTags.contains(tags[which])) {

                            filteredTags.remove(tags[which])
                            MainActivity.checkedItems[which] = false

                    }

                    Toast.makeText(activity, "Выбран тип $filteredTags", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("Фильтровать",
                    DialogInterface.OnClickListener { dialog, id -> (activity as MainActivity?)
                        ?.okClicked(filteredTags);  Log.d("setPositiveButton", filteredTags.toString()) }

                )

                .setNegativeButton("Отмена"
                ) { _, _ ->  }
            dialog.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

}

