package uz.mimsoft.shop_ixdeliver

import io.reactivex.Single
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import uz.shopix.agentapp.SERVER_ADDRESS
import java.util.concurrent.TimeUnit

class ApiFactory {
    companion object {
        private var okClient: OkHttpClient? = null
        private var services: ApiService? = null

        fun getApiService(): ApiService {
            var service: ApiService? = services
            if (service == null) {
                synchronized(ApiFactory::class.java) {
                    service = services
                    if (service == null) {
                        services = buildSite().create(ApiService::class.java)
                        service = services
                    }
                }
            }
            return service ?: buildSite().create(ApiService::class.java)
        }

        private fun buildSite(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }


        private fun getClient(): OkHttpClient {
            var c: OkHttpClient? = okClient
            if (c == null) {
                synchronized(ApiFactory::class.java) {
                    okClient = buildClient()
                    c = okClient
                }
            }
            return c ?: buildClient()
        }

        private fun buildClient(): OkHttpClient {
            val interceptor = Interceptor { chain ->
                val request = chain.request()
                request.newBuilder().addHeader("Cache-Control", "no-cache")
                    .cacheControl(CacheControl.FORCE_NETWORK)
                chain.proceed(request)
            }
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(interceptor)
                .hostnameVerifier { _, _ -> true }
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20 / 2 , TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .cache(null)
                .build()
        }
    }
}

interface ApiService {

    @GET("deliver/version")
    fun version(@Query("version") version: Int): Single<BaseResponse<Version>>

    @GET("deliver/auth")
    fun auth(@Query("username")username:String,@Query("password")password:String) : Single<BaseResponse<Deliver>>

    @GET("deliver/orders")
    fun getOrders(@Header("key")key: String):Single<BaseResponse<ArrayList<Order>>>

    @GET("deliver/order/peers")
    fun getOrderPeer(@Header("key")key: String,@Query("orderId")orderId:Long):Single<BaseResponse<ArrayList<Peer>>>

    @GET("deliver/edit")
    fun setProfileData(@Header("key") key: String, @Query("name")name:String, @Query("phone")phone:String): Single<BaseResponse<String>>

    @Multipart
    @POST("deliver/image")
    fun upDataDeliverImage(@Header("key")key: String,@Part()filePart:MultipartBody.Part):Single<BaseResponse<String>>

    @GET("deliver/history")
    fun getHistory(@Header("key")key: String):Single<BaseResponse<ArrayList<Order>>>

    @GET("deliver/credits")
    fun getDeliverCredits(@Header("key")key: String):Single<BaseResponse<ArrayList<Order>>>
}