package taymay.iap.adaptors.datasources.local
import taymay.adaptors.datasources.local.EntityLocalDataSource
import taymay.adaptors.datasources.remote.EntityRemoteDataSource

import taymay.adaptors.mapper.Mappable
import taymay.iap.adaptors.mapper.ProductMapper
import taymay.iap.entities.Product
import taymay.frameworks.database.JsonDatabase
import taymay.frameworks.utils.AppEnvironment
import java.io.File
import java.util.logging.Logger

class ProductLocalDataSource(
     box: JsonDatabase<ProductMapper>? = JsonDatabase(
            File(
                AppEnvironment.applicationContext?.filesDir!!.absolutePath,
                ProductMapper::class.java.name
            ).absolutePath, ProductMapper::class.java
        )
) : EntityLocalDataSource<ProductMapper>(
    box!!,
    fromMap = { map: Map<String, Any> ->
        Mappable.fromMap(map, ProductMapper::class.java)
    },
    toMap = { userMapper: ProductMapper -> userMapper.toMap() },
) {
    override val logger = Logger.getLogger(ProductLocalDataSource::class.java.name)
}
