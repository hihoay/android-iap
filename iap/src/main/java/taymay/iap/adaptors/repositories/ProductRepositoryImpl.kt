package taymay.iap.adaptors.repositories

import android.content.Context
import taymay.iap.adaptors.datasources.local.ProductLocalDataSource
import taymay.iap.adaptors.datasources.remote.ProductRemoteDataSource
import taymay.iap.adaptors.mapper.ProductMapper
import kotlinx.coroutines.runBlocking
import taymay.iap.repositories.ProductRepository

class ProductRepositoryImpl(
    override var context: Context,
    override var localDataSource: ProductLocalDataSource,
    override var remoteDataSource: ProductRemoteDataSource,
) : ProductRepository(context,localDataSource,remoteDataSource) {

    override fun create(data: ProductMapper): Long = runBlocking {
       localDataSource.create(data)
    }

    override fun update(_id: Long, data: ProductMapper): Boolean = runBlocking {
       localDataSource.update(_id, data)
    }


    override fun delete(_id: Long): Boolean = runBlocking {
       localDataSource.delete(_id)
    }

    override fun read(_id: Long): ProductMapper? = runBlocking {
       localDataSource.read(_id)
    }

    override fun reads(): List<ProductMapper> = runBlocking {
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
        item:ProductMapper, predicate: (ProductMapper) -> Boolean
    ): Boolean =runBlocking {
        localDataSource.addIfNotExist(item,predicate)
    }

    override fun addOrUpdate(item:ProductMapper, predicate: (ProductMapper) -> Boolean) =
        runBlocking {
            localDataSource.addOrUpdate(item, predicate)

        }

    override fun find(predicate: (ProductMapper) -> Boolean): List<ProductMapper> =
        runBlocking {
            localDataSource.find(predicate)
        }

    override fun forEach(action: (ProductMapper) -> Unit) = runBlocking {
        localDataSource.forEach(action)
    }


}


