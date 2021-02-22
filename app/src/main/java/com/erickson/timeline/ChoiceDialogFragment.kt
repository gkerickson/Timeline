package com.erickson.timeline

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.erickson.timeline.model.DataViewModel

class ChoiceDialogFragment : DialogFragment() {
    private val viewModel: DataViewModel by activityViewModels()

    override fun onDismiss(dialog: DialogInterface) {
        if(viewModel.selectedIsHigher) this.activity?.onBackPressed()
        viewModel.updateChoices()
        super.onDismiss(dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)
            builder.setMessage(
                if (viewModel.selectedIsHigher) getString(R.string.message_correct)
                else getString(R.string.message_incorrect)
            )
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}