package org.swm.att.home.constant

import androidx.annotation.IdRes
import org.swm.att.home.R

enum class NavDestinationType(
    @IdRes val action: Int,
    @IdRes val viewId: Int
) {
    Home(R.id.action_global_fragment_home, R.id.btn_home),
    Bills(R.id.action_global_fragment_bill, R.id.btn_bills),
    Preorder(R.id.action_global_fragment_preorder, R.id.btn_preorder),
    Stock(R.id.action_global_fragment_stock, R.id.btn_stock),
    Recipe(R.id.action_global_fragment_recipe, R.id.btn_recipe),
    Worker(R.id.action_global_fragment_worker, R.id.btn_worker),
    Sales(R.id.action_global_fragment_sales, R.id.btn_sales),
    Setting(R.id.action_global_fragment_setting, R.id.btn_setting);
}