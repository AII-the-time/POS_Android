package org.swm.att.domain.repository

import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.response.OrderVO

interface AttOrderRepository {
    suspend fun postOrder(storeId: Int, mileageId: Int, orderedMenus: OrderedMenusVO): Result<OrderVO>
}