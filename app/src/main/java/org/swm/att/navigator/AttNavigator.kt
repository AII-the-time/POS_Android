package org.swm.att.navigator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.FragmentActivity
import org.swm.att.common_ui.navigator.FakeAttNavigator
import org.swm.att.home.MainActivity
import org.swm.att.login.LoginActivity
import javax.inject.Inject

class AttNavigator @Inject constructor(): FakeAttNavigator {
    override fun navigateToMain(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        finishAffinity(context as AppCompatActivity)
        context.startActivity(intent)
    }

    override fun navigateToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        finishAffinity(context as AppCompatActivity)
        context.startActivity(intent)
    }
}