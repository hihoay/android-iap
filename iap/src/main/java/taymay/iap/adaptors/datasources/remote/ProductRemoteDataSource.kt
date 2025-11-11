package taymay.iap.adaptors.datasources.remote
import taymay.adaptors.datasources.local.EntityLocalDataSource
import taymay.adaptors.datasources.remote.EntityRemoteDataSource

import taymay.adaptors.mapper.Mappable
import taymay.iap.adaptors.mapper.ProductMapper
import java.util.logging.Logger
import taymay.frameworks.utils.AppEnvironment.SESSION_SERVICE_URL

class ProductRemoteDataSource(
) : EntityRemoteDataSource<ProductMapper>(
    "${SESSION_SERVICE_URL}/api/v1/products",
    fromMap = { map: Map<String, Any> ->
        Mappable.fromMap(map, ProductMapper::class.java)
    },
    toMap = { userMapper: ProductMapper -> userMapper.toMap() },
    toJson = { userMapper: ProductMapper -> userMapper.toJson() },
    fromJson = { s: String -> Mappable.fromJson(s) },
    fromJsons = { s: String -> Mappable.fromJsons(s) }) {
    override var logger = Logger.getLogger(ProductRemoteDataSource::class.java.name)
}
