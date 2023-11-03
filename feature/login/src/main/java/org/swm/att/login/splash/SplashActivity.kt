package org.swm.att.login.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.swm.att.common_ui.presenter.base.BaseActivity
import org.swm.att.login.LoginActivity
import org.swm.att.login.R
import org.swm.att.login.databinding.ActivitySplashBinding
class SplashActivity: BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startRealActivityAfterDelay()
    }

    private fun startRealActivityAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            startLoginActivity()
        }, SPLASH_DELAY)
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
        this.finish()
    }

    companion object {
        private const val SPLASH_DELAY: Long = 1000
    }
}