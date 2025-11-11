package taymay.iap.adaptors.datasources.remote
import taymay.adaptors.datasources.local.EntityLocalDataSource
import taymay.adaptors.datasources.remote.EntityRemoteDataSource

import taymay.adaptors.mapper.Mappable
import taymay.iap.adaptors.mapper.SubscriptionMapper
import java.util.logging.Logger
import taymay.frameworks.utils.AppEnvironment.SESSION_SERVICE_URL

class SubscriptionRemoteDataSource(
) : EntityRemoteDataSource<SubscriptionMapper>(
    "${SESSION_SERVICE_URL}/api/v1/subscriptions",
    fromMap = { map: Map<String, Any> ->
        Mappable.fromMap(map, SubscriptionMapper::class.java)
    },
    toMap = { userMapper: SubscriptionMapper -> userMapper.toMap() },
    toJson = { userMapper: SubscriptionMapper -> userMapper.toJson() },
    fromJson = { s: String -> Mappable.fromJson(s) },
    fromJsons = { s: String -> Mappable.fromJsons(s) }) {
    override var logger = Logger.getLogger(SubscriptionRemoteDataSource::class.java.name)
}
