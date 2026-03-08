package uz.gita.contactapp.presenter.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.databinding.DialogDeleteContactBinding

class DialogMessageContact : DialogFragment(R.layout.dialog_delete_contact) {

    private val binding by viewBinding(DialogDeleteContactBinding::bind)
    private var onOkListener: (() -> Unit)? = null

    fun setOnOkListener(listener: () -> Unit) {
        onOkListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            args.getString(ARG_TITLE)?.let { binding.tvTitle.text = it }
            args.getString(ARG_MESSAGE)?.let { binding.tvMessage.text = it }
        }

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnOk.setOnClickListener {
            onOkListener?.invoke()
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
        private const val ARG_TITLE = "arg_title"
        private const val ARG_MESSAGE = "arg_message"

        fun newInstance(title: String, message: String): DialogMessageContact {
            return DialogMessageContact().apply {
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_MESSAGE to message
                )
            }
        }
    }
}