package uz.gita.contactapp.presenter.screen.splash

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.contactapp.R
import uz.gita.contactapp.data.local.TokenManager
import uz.gita.contactapp.databinding.SplashScreenBinding

class SplashScreen : Fragment(R.layout.splash_screen) {

    private val binding by viewBinding(SplashScreenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startAnimation()
        openNextScreen()

        Log.d("TTT", "${TokenManager.getToken()}")
    }

    private fun startAnimation() {
        binding.imgLogo.apply {
            alpha = 0f
            scaleX = 0.8f
            scaleY = 0.8f

            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
        }

        binding.textTitle.apply {
            alpha = 0f
            translationY = 50f

            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(800)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
        }
    }

    private fun openNextScreen() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)

            val destination =
                if (!TokenManager.getToken().isNullOrEmpty()) {
                    R.id.action_splashScreen_to_homeScreen
                } else {
                    R.id.action_splashScreen_to_loginScreen
                }

            findNavController().navigate(
                destination,
                null,
                navOptions {
                    popUpTo(R.id.splashScreen) {
                        inclusive = true
                    }
                }
            )
        }
    }
}