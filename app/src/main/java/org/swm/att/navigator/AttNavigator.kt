package org.swm.att.navigator

import android.content.Context
import android.content.Intent
import org.swm.att.common_ui.navigator.FakeAttNavigator
import org.swm.att.home.MainActivity
import javax.inject.Inject

class AttNavigator @Inject constructor(): FakeAttNavigator {
    override fun navigateToMain(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}