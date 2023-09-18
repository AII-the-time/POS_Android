package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.OrderDataSource
import org.swm.att.data.remote.request.OrderedMenuDTO
import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.request.PaymentDTO
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.repository.AttOrderRepository
import javax.inject.Inject

class AttOrderRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource
): AttOrderRepository {
    override suspend fun postOrder(storeId: Int, totalPrice: Int, orderedMenus: OrderedMenusVO): Flow<Result<OrderVO>> = flow {
        try {
            orderDataSource.postOrder(
                storeId,
                OrderedMenusDTO(
                    totalPrice = totalPrice,
                    menus = orderedMenus.menus?.map { OrderedMenuDTO(
                        id = it.id,
                        count = it.count ?: 1,
                        options = it.options.map { option -> option.id },
                        detail = it.detail
                    )} ?: listOf()
                )
            ).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Flow<Result<PaymentResultVO>> = flow {
        try {
            orderDataSource.postPayment(
                storeId,
                PaymentDTO(
                    orderId = paymentVO.orderId,
                    paymentMethod = PayMethod.toString(paymentVO.paymentMethod),
                    mileageId = paymentVO.mileageId,
                    useMileage = paymentVO.useMileage,
                    saveMileage = paymentVO.saveMileage
                )
            ).collect {
                emit(Result.success(null))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}