package uz.gita.contactapp.presenter.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.data.model.contact.response.ContactResponse
import uz.gita.contactapp.databinding.DialogAddContactBinding

class DialogAddContact: DialogFragment(R.layout.dialog_add_contact) {

    private val binding by viewBinding(DialogAddContactBinding::bind)
    private var listener: ((ContactResponse) -> Unit)? = null
    fun setListener(f: (ContactResponse) -> Unit) {
        listener = f
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddContact.setOnClickListener {
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

            val contact = ContactResponse(
                id = System.currentTimeMillis(),
                name = name,
                phone = phone
            )

            listener?.invoke(contact)
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
}