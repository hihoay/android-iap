package taymay.iap.adaptors.datasources.local
import taymay.adaptors.datasources.local.EntityLocalDataSource
import taymay.adaptors.datasources.remote.EntityRemoteDataSource

import taymay.adaptors.mapper.Mappable
import taymay.iap.adaptors.mapper.SubscriptionMapper
import taymay.iap.entities.Subscription
import taymay.frameworks.database.JsonDatabase
import taymay.frameworks.utils.AppEnvironment
import java.io.File
import java.util.logging.Logger

class SubscriptionLocalDataSource(
     box: JsonDatabase<SubscriptionMapper>? = JsonDatabase(
            File(
                AppEnvironment.applicationContext?.filesDir!!.absolutePath,
                SubscriptionMapper::class.java.name
            ).absolutePath, SubscriptionMapper::class.java
        )
) : EntityLocalDataSource<SubscriptionMapper>(
    box!!,
    fromMap = { map: Map<String, Any> ->
        Mappable.fromMap(map, SubscriptionMapper::class.java)
    },
    toMap = { userMapper: SubscriptionMapper -> userMapper.toMap() },
) {
    override val logger = Logger.getLogger(SubscriptionLocalDataSource::class.java.name)
}
