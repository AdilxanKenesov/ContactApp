package uz.gita.contactapp.presenter.dialog


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.data.model.contact.response.ContactResponse
import uz.gita.contactapp.databinding.DialogUpdateContactBinding

class DialogUpdateContact : DialogFragment(R.layout.dialog_update_contact) {

    private val binding by viewBinding(DialogUpdateContactBinding::bind)
    private var onUpdateListener: ((ContactResponse) -> Unit)? = null

    fun setOnUpdateListener(listener: (ContactResponse) -> Unit) {
        onUpdateListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            binding.etName.setText(args.getString(ARG_NAME, ""))
            binding.etPhone.setText(args.getString(ARG_PHONE, ""))
        }

        binding.btnUpdate.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                binding.containerName.error = "Name is required"
                isValid = false
            } else {
                binding.containerName.error = null
            }

            val cleanedPhone = phone.removePrefix("+998").replace(" ", "")
            val validOperatorCodes = setOf("33", "70", "77", "88", "90", "91", "93", "94", "95", "97", "99")

            if (phone.isEmpty()) {
                binding.containerPhone.error = "Phone number is required"
                isValid = false
            } else if (!phone.startsWith("+998")) {
                binding.containerPhone.error = "Phone number must start with +998"
                isValid = false
            } else if (cleanedPhone.length != 9 || !cleanedPhone.all { it.isDigit() }) {
                binding.containerPhone.error = "The phone number must contain 9 digits after +998"
                isValid = false
            } else if (cleanedPhone.substring(0, 2) !in validOperatorCodes) {
                binding.containerPhone.error = "Operator code is invalid (33, 70, 77, 88, 90-91, 93-95, 97, 99)"
                isValid = false
            } else {
                binding.containerPhone.error = null
            }

            if (!isValid) return@setOnClickListener

            val id = arguments?.getLong(ARG_ID, 0L) ?: 0L
            onUpdateListener?.invoke(ContactResponse(id = id, name = name, phone = phone))
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
        private const val ARG_ID = "arg_id"
        private const val ARG_NAME = "arg_name"
        private const val ARG_PHONE = "arg_phone"

        fun newInstance(contact: ContactResponse): DialogUpdateContact {
            return DialogUpdateContact().apply {
                arguments = bundleOf(
                    ARG_ID to contact.id,
                    ARG_NAME to contact.name,
                    ARG_PHONE to contact.phone
                )
            }
        }
    }
}