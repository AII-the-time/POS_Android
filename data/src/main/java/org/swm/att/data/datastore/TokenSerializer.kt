package org.swm.att.data.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.swm.att.data.remote.response.TokenDTO
import java.io.InputStream
import java.io.OutputStream

class TokenSerializer(
    private val cryptoManager: CryptoManager
): Serializer<TokenDTO> {
    override val defaultValue: TokenDTO
        get() = TokenDTO()

    override suspend fun readFrom(input: InputStream): TokenDTO {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = TokenDTO.serializer(),
                string = decryptedBytes.decodeToString()
            )

        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: TokenDTO, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = TokenDTO.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}