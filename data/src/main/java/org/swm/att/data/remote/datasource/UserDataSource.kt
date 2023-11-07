package org.swm.att.data.remote.datasource

import org.swm.att.data.remote.request.CertificationDTO
import org.swm.att.data.remote.request.LoginDTO
import org.swm.att.data.remote.request.PhoneNumDTO
import org.swm.att.data.remote.request.StoreDTO
import org.swm.att.data.remote.response.MileageDTO
import org.swm.att.data.remote.service.AttPosUserService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val attPosUserService: AttPosUserService
): BaseNetworkDataSource() {
    suspend fun refreshToken(refreshToken: String) =
        checkResponse(attPosUserService.refreshToken(refreshToken))

    suspend fun getMileage(storeId: Int, phoneNumber: String) =
        checkResponse(attPosUserService.getMileage(storeId, phoneNumber))

    suspend fun patchMileage(storeId: Int, mileage: MileageDTO) =
        checkResponse(attPosUserService.patchMileage(storeId, mileage))

    suspend fun registerCustomer(storeId: Int, phone: PhoneNumDTO) =
        checkResponse(attPosUserService.registerCustomer(storeId, phone))

    suspend fun registerStore(store: StoreDTO) =
        checkResponse(attPosUserService.registerStore(store))

    suspend fun registerStoreForTest(store: StoreDTO) =
        checkResponse(attPosUserService.registerStoreForTest(store))

    suspend fun postPhoneNumberForAuthentication(phone: PhoneNumDTO) =
        checkResponse(attPosUserService.postPhoneNumberForAuthentication(phone))

    suspend fun checkCertificationCode(certificationInfo: CertificationDTO) =
        checkResponse(attPosUserService.checkCertificationCode(certificationInfo))

    suspend fun login(userInfo: LoginDTO) =
        checkResponse(attPosUserService.login(userInfo))

    suspend fun getStore() = checkResponse(attPosUserService.getStore())
}