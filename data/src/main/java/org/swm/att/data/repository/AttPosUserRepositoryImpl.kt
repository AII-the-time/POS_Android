package org.swm.att.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource.Companion.PreferenceKey
import org.swm.att.data.remote.datasource.UserDataSource
import org.swm.att.data.remote.request.CertificationDTO
import org.swm.att.data.remote.request.LoginDTO
import org.swm.att.data.remote.request.OpeningHourDTO
import org.swm.att.data.remote.request.PhoneNumDTO
import org.swm.att.data.remote.request.StoreDTO
import org.swm.att.data.remote.response.MileageDTO
import org.swm.att.domain.entity.request.CertificationVO
import org.swm.att.domain.entity.request.LoginVO
import org.swm.att.domain.entity.request.PhoneNumVO
import org.swm.att.domain.entity.request.StoreVO
import org.swm.att.domain.entity.response.CertificatedPhoneTokenVO
import org.swm.att.domain.entity.response.MileageIdVO
import org.swm.att.domain.entity.response.MileageVO
import org.swm.att.domain.entity.response.StoreIdVO
import org.swm.att.domain.entity.response.StoreListVO
import org.swm.att.domain.entity.response.TokenForCertificationPhoneVO
import org.swm.att.domain.entity.response.TokenVO
import org.swm.att.domain.repository.AttPosUserRepository
import javax.inject.Inject

class AttPosUserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val attEncryptedPrefDataSource: AttEncryptedPrefDataSource
): AttPosUserRepository {
    override fun refreshToken(refreshToken: String): Flow<Result<TokenVO>> = flow {
        userDataSource.refreshToken(refreshToken).collect {
            emit(Result.success(it.toVO()))
        }
    }

    override fun saveAccessToken(accessToken: String) {
        attEncryptedPrefDataSource.setToken(
            preferenceKey = PreferenceKey.ACCESS_TOKEN,
            value = accessToken
        )
    }

    override fun saveRefreshToken(refreshToken: String) {
        attEncryptedPrefDataSource.setToken(
            preferenceKey = PreferenceKey.REFRESH_TOKEN,
            value = refreshToken
        )
    }

    override fun getAccessToken(): String? {
        return attEncryptedPrefDataSource.getToken(PreferenceKey.ACCESS_TOKEN)
    }

    override fun getRefreshToken(): String? {
        return attEncryptedPrefDataSource.getToken(PreferenceKey.REFRESH_TOKEN)
    }

    override fun saveStoreId(storeId: Int) {
        attEncryptedPrefDataSource.setStoreId(
            preferenceKey = PreferenceKey.STORE_ID,
            value = storeId
        )
    }

    override fun getStoreId(): Int {
        return attEncryptedPrefDataSource.getStoreId(PreferenceKey.STORE_ID)
    }

    override fun logout() {
        attEncryptedPrefDataSource.logout()
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

    override suspend fun registerStoreForTest(store: StoreVO): Flow<Result<StoreIdVO>> = flow {
        try {
            userDataSource.registerStoreForTest(
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

    override suspend fun postPhoneNumberForAuthentication(phone: String): Flow<Result<TokenForCertificationPhoneVO>> = flow {
        try {
            userDataSource.postPhoneNumberForAuthentication(
                PhoneNumDTO(
                    phone = phone
                )
            ).collect { emit(Result.success(it.toVO())) }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun checkCertificationCode(certificationInfo: CertificationVO): Flow<Result<CertificatedPhoneTokenVO>> = flow {
        try {
            val certificationInfo = CertificationDTO(
                certificationInfo.phone,
                certificationInfo.certificationCode,
                certificationInfo.phoneCertificationToken
            )
            userDataSource.checkCertificationCode(certificationInfo).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun login(userInfo: LoginVO): Flow<Result<TokenVO>> = flow {
        try {
            val user = LoginDTO(
                userInfo.businessRegistrationNumber,
                userInfo.certificatedPhoneToken
            )
            userDataSource.login(user).collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun getStore(): Flow<Result<StoreListVO>> = flow {
        try {
            userDataSource.getStore().collect {
                emit(Result.success(it.toVO()))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }

    }
}