package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource.Companion.PreferenceKey
import org.swm.att.data.remote.datasource.UserDataSource
import org.swm.att.data.remote.request.OpeningHourDTO
import org.swm.att.data.remote.request.PhoneNumDTO
import org.swm.att.data.remote.request.StoreDTO
import org.swm.att.data.remote.response.MileageDTO
import org.swm.att.domain.entity.request.PhoneNumVO
import org.swm.att.domain.entity.request.StoreVO
import org.swm.att.domain.entity.response.MileageIdVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.StoreIdVO
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

class AttPosUserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val attEncryptedPrefDataSource: AttEncryptedPrefDataSource
): AttPosUserRepository {
    override suspend fun refreshToken(refreshToken: String): Flow<Result<TokenVO>> = flow {
        userDataSource.refreshToken(refreshToken).collect {
            emit(Result.success(it.toVO()))
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        attEncryptedPrefDataSource.setAccessToken(
            preferenceKey = PreferenceKey.ACCESS_TOKEN,
            value = accessToken
        )
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        attEncryptedPrefDataSource.setAccessToken(
            preferenceKey = PreferenceKey.REFRESH_TOKEN,
            value = refreshToken
        )
    }

    override suspend fun getAccessToken(): String {
        return attEncryptedPrefDataSource.getAccessToken(PreferenceKey.ACCESS_TOKEN)
    }

    override suspend fun getRefreshToken(): String {
        return attEncryptedPrefDataSource.getAccessToken(PreferenceKey.REFRESH_TOKEN)
    }

    override suspend fun getMileage(storeId: Int, phoneNumber: String): Flow<Result<MileageVO>> = flow {
        try {
            userDataSource.getMileage(storeId, phoneNumber).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun patchMileage(storeId: Int, mileage: MileageVO): Flow<Result<MileageVO>> = flow {
        try {
            userDataSource.patchMileage(
                storeId,
                MileageDTO(
                    mileageId = mileage.mileageId,
                    mileage = mileage.mileage
                )
            ).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun registerCustomer(storeId: Int, phone: PhoneNumVO): Flow<Result<MileageIdVO>> = flow {
        try {
            userDataSource.registerCustomer(
                storeId,
                PhoneNumDTO(
                    phone = phone.phone
                )
            ).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun registerStore(store: StoreVO): Flow<Result<StoreIdVO>> = flow {
        try {
            userDataSource.registerStore(
                StoreDTO(
                    store.name,
                    store.address,
                    store.openingHours?.map {
                        OpeningHourDTO(
                            it.day,
                            it.open,
                            it.close
                        )
                    }
                )
            ).collect { emit(Result.success(it.toVO())) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

}