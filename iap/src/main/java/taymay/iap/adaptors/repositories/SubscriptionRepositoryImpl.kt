package taymay.iap.adaptors.repositories

import android.content.Context
import taymay.iap.adaptors.datasources.local.SubscriptionLocalDataSource
import taymay.iap.adaptors.datasources.remote.SubscriptionRemoteDataSource
import taymay.iap.adaptors.mapper.SubscriptionMapper
import kotlinx.coroutines.runBlocking
import taymay.iap.repositories.SubscriptionRepository

class SubscriptionRepositoryImpl(
    override var context: Context,
    override var localDataSource: SubscriptionLocalDataSource,
    override var remoteDataSource: SubscriptionRemoteDataSource,
) : SubscriptionRepository(context,localDataSource,remoteDataSource) {

    override fun create(data: SubscriptionMapper): Long = runBlocking {
       localDataSource.create(data)
    }

    override fun update(_id: Long, data: SubscriptionMapper): Boolean = runBlocking {
       localDataSource.update(_id, data)
    }


    override fun delete(_id: Long): Boolean = runBlocking {
       localDataSource.delete(_id)
    }

    override fun read(_id: Long): SubscriptionMapper? = runBlocking {
       localDataSource.read(_id)
    }

    override fun reads(): List<SubscriptionMapper> = runBlocking {
       localDataSource.reads()
    }

    override fun count(): Long = runBlocking {
       localDataSource.count()
    }

    override fun exists(_id: Long): Boolean = runBlocking {
       localDataSource.exists(_id)
    }

    override fun sync() = runBlocking {

    }



    override fun addIfNotExist(
        item:SubscriptionMapper, predicate: (SubscriptionMapper) -> Boolean
    ): Boolean =runBlocking {
        localDataSource.addIfNotExist(item,predicate)
    }

    override fun addOrUpdate(item:SubscriptionMapper, predicate: (SubscriptionMapper) -> Boolean) =
        runBlocking {
            localDataSource.addOrUpdate(item, predicate)

        }

    override fun find(predicate: (SubscriptionMapper) -> Boolean): List<SubscriptionMapper> =
        runBlocking {
            localDataSource.find(predicate)
        }

    override fun forEach(action: (SubscriptionMapper) -> Unit) = runBlocking {
        localDataSource.forEach(action)
    }


}


