package com.example.data.mock

import android.content.Context
import android.os.SystemClock
import com.example.data.mock.core.AssetFileProvider
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import kotlin.random.Random

class MockInterceptor(context: Context) : Interceptor {

    private val mockServer = MockServer(AssetFileProvider(context))

    override fun intercept(chain: Interceptor.Chain): Response {
        SystemClock.sleep(Random.nextInt(1, 3) * 1000L)

        val path = chain.request().url.encodedPath
        Timber.d("[ESES##] 요청 경로: $path")

        val responseString = mockServer.get(chain.request())
        Timber.d("[ESES##] 응답 여부: ${responseString != null}")

        return chain.proceed(chain.request())
            .newBuilder()
            .apply {
                if (responseString == null) {
                    code(500)
                    message("에러 발생")
                } else {
                    message(responseString)
                    body(responseString.toByteArray().toResponseBody("application/json".toMediaType()))
                    protocol(Protocol.HTTP_2)
                    addHeader("content-type", "application/json")
                    code(200)
                }
            }
            .build()
    }
}