package uz.mimsoft.shop_ixdeliver

import java.io.Serializable

data class BaseResponse<T>(val code: Int, val result: T?, val error: String)

data class Version(
    val isLast: Boolean,
    val link: String
)

data class Deliver(
    val name: String,
    val regionId: Long,
    val companyId: Long,
    val image: String,
    val phone: String,
    val key: String
) : Serializable

data class Order(
    val id: Long? = null,
    val shopId: Long? = null,
    val agentId: Long? = null,
    val deliverId: Long? = null,
    val time: Long? = null,
    val orderTime: Long? = 0,
    val deliverTime: Long? = 0,
    val peers: List<Peer>? = null,
    val agentLocation: Location? = null,
    val isOverDraft: Boolean? = false,
    val overDraftDate: Long? = 0,
    val status: Int? = 0,
    val taskId: Long? = null,
    val paymentType: Int? = 0,
    val cancelReason: String? = null,
    val totalPrice: Double? = 0.0,
    val name: String? = null,
    val shop: Shop? = null,
    var discountPrice: Double? = null
):Serializable

data class Shop(
    val id: Long,
    val title: String,
    val image: String,
    val owner: String,
    val phone: String,
    val address: String,
    val location: Location
) : Serializable

data class Location(
    val lat: Double,
    val lng: Double
) : Serializable

data class Peer(
    val id: Long? = null,
    val productId: Long? = null,
    val product: Product? = null,
    val count: Int? = 0,
    val totalPrice: Double? = 0.0,
    val discountPrice: Double? = 0.0
) : Serializable

data class Product(
    val id: Long? = 0,
    val groupId: Long? = 0,
    val categoryId: Long? = 0,
    val companyId: Long? = 0,
    val groupName: String? = "",
    val name: String? = "",
    var image: String? = "",
    val price: Double? = 0.0,
    val hasDiscount: Boolean? = false,
    val discount: Int? = 0,
    val bonusCount: Int? = 0,
    val bonusProductId: Long? = 0,
    val bonusProductCount: Int? = 0,
    val inBox: Int? = 0,
    val type: String? = ""
)