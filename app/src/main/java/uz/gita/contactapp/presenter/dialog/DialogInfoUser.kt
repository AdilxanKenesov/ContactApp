package uz.gita.contactapp.presenter.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.androidbroadcast.vbpd.viewBinding
import uz.gita.contactapp.R
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.databinding.DialogUserInfoBinding


class DialogInfoUser: DialogFragment(R.layout.dialog_user_info) {
    private val binding by viewBinding(DialogUserInfoBinding::bind)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUsername.text = "Username: ${TokenManager.getName()}"
        binding.tvPassword.text = "Password: ${TokenManager.getPassword()}"

        binding.btnClose.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}