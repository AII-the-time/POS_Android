package org.swm.att.data.repository

import org.swm.att.data.remote.datasource.OrderDataSource
import org.swm.att.data.remote.request.OrderedMenuDTO
import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.request.PaymentDTO
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.entity.response.PaymentResultVO
import org.swm.att.domain.repository.AttOrderRepository
import javax.inject.Inject

class AttOrderRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource
): AttOrderRepository {
    override suspend fun postOrder(storeId: Int, mileageId: Int?, totalPrice: Int, orderedMenus: OrderedMenusVO): Result<OrderVO> {
        return try {
            val response = orderDataSource.postOrder(
                storeId,
                OrderedMenusDTO(
                    totalPrice = totalPrice,
                    mileageId = mileageId,
                    menus = orderedMenus.menus?.map { OrderedMenuDTO(
                        Id = it.id,
                        count = it.count ?: 1,
                        options = it.options.map { option -> option.id },
                        detail = it.detail
                    )} ?: listOf()
                )
            )
            Result.success(response.toVO())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Result<PaymentResultVO> {
        return try {
            val response = orderDataSource.postPayment(
                storeId,
                PaymentDTO(
                    paymentMethod = PayMethod.toString(paymentVO.paymentMethod),
                    price = paymentVO.price,
                    orderId = paymentVO.orderId
                )
            )
            Result.success(response.toVO())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}