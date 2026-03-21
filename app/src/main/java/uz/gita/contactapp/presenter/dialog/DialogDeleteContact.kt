package uz.gita.contactapp.presenter.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.databinding.DialogDeleteContactBinding

class DialogDeleteContact : DialogFragment(R.layout.dialog_delete_contact) {

    private val binding by viewBinding(DialogDeleteContactBinding::bind)
    private var onDeleteListener: (() -> Unit)? = null

    fun setOnDeleteListener(listener: () -> Unit) {
        onDeleteListener = listener
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            args.getString(ARG_NAME)?.let {
                binding.tvMessage.text = "Do you want to delete \"$it\" contact?"
            }
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnOk.setOnClickListener {
            onDeleteListener?.invoke()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        private const val ARG_NAME = "arg_name"

        fun newInstance(name: String): DialogDeleteContact {
            return DialogDeleteContact().apply {
                arguments = bundleOf(ARG_NAME to name)
            }
        }
    }
}