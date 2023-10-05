package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.OrderDataSource
import org.swm.att.data.remote.request.OrderedMenuDTO
import org.swm.att.data.remote.request.OrderedMenusDTO
import org.swm.att.data.remote.request.PaymentDTO
import org.swm.att.data.remote.request.PreOrderedMenusDTO
import org.swm.att.domain.constant.PayMethod
import org.swm.att.domain.entity.request.OrderedMenusVO
import org.swm.att.domain.entity.request.PaymentVO
import org.swm.att.domain.entity.request.PreOrderedMenusVO
import org.swm.att.domain.entity.response.OrderBillsVO
import org.swm.att.domain.entity.response.OrderReceiptVO
import org.swm.att.domain.entity.response.OrderVO
import org.swm.att.domain.entity.response.PreOrderBillVO
import org.swm.att.domain.entity.response.PreOrdersVO
import org.swm.att.domain.repository.AttOrderRepository
import javax.inject.Inject

class AttOrderRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource
): AttOrderRepository {
    override suspend fun postOrder(
        storeId: Int,
        preOrderId: Int?,
        totalPrice: Int,
        orderedMenus: OrderedMenusVO
    ): Flow<Result<OrderVO>> = flow {
        try {
            orderDataSource.postOrder(
                storeId,
                OrderedMenusDTO(
                    totalPrice = totalPrice,
                    preOrderId = preOrderId,
                    menus = orderedMenus.menus?.map {
                        OrderedMenuDTO(
                            id = it.id,
                            count = it.count ?: 1,
                            options = it.options.map { option -> option.id },
                            detail = it.detail
                        )
                    } ?: listOf()
                )
            ).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun postPayment(storeId: Int, paymentVO: PaymentVO): Flow<Result<Nothing?>> = flow {
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

    override suspend fun getOrderBills(storeId: Int, page: Int, count: Int): Flow<Result<OrderBillsVO>> = flow {
        try {
            orderDataSource.getOrderBills(storeId, page, count).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getOrderBill(storeId: Int, orderId: Int): Flow<Result<OrderReceiptVO>> =
        flow {
            try {
                orderDataSource.getOrderBill(storeId, orderId).collect {
                    emit(Result.success(it.toVO()))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

    override suspend fun postPreOrder(
        storeId: Int,
        preOrderedMenus: PreOrderedMenusVO
    ): Flow<Result<Unit>> = flow {
        try {
            val preOrderedMenusDTO = PreOrderedMenusDTO(
                totalPrice = preOrderedMenus.totalPrice,
                menus = preOrderedMenus.menus?.map {
                    OrderedMenuDTO(
                        id = it.id,
                        count = it.count ?: 1,
                        options = it.options.map { option -> option.id },
                        detail = it.detail
                    )
                } ?: listOf(),
                phone = preOrderedMenus.phone,
                memo = preOrderedMenus.memo,
                orderedFor = preOrderedMenus.orderedFor
            )
            orderDataSource.postPreOrder(storeId, preOrderedMenusDTO).collect {
                emit(Result.success(it))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getPreOrders(
        storeId: Int,
        page: Int,
        date: String?
    ): Flow<Result<PreOrdersVO>> = flow {
        try {
            orderDataSource.getPreOrders(storeId, page, date).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getPreOrderBill(
        storeId: Int,
        preOrderId: Int
    ): Flow<Result<PreOrderBillVO>> = flow {
        try {
            orderDataSource.getPreOrderBill(storeId, preOrderId).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}