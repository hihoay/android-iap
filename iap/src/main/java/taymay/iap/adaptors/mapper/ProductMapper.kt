package taymay.iap.adaptors.mapper

import com.google.gson.reflect.TypeToken
import taymay.adaptors.mapper.Mappable.Companion.gson
import taymay.iap.entities.Product
import taymay.adaptors.mapper.Mapper

class ProductMapper() : Mapper<Product>() {
    var _id: Long = 0
    override fun toEntity(): Product {
        return Product(
          _id,
        )
    }

    override fun toJson(): String {
        return gson.toJson(toEntity())
    }

    override fun toMap(): Map<String, Any> {
        return mapOf(
          "_id" to _id,
        )
    }
}
