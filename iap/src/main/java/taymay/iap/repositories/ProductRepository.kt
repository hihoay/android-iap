package taymay.iap.repositories

import taymay.adaptors.repositories.EntityRepositoryImpl

import android.content.Context
import taymay.iap.adaptors.datasources.local.ProductLocalDataSource
import taymay.iap.adaptors.datasources.remote.ProductRemoteDataSource
import taymay.iap.adaptors.mapper.ProductMapper

abstract class ProductRepository (
    context: Context,
    localDataSource: ProductLocalDataSource,
    remoteDataSource: ProductRemoteDataSource
) : EntityRepositoryImpl<ProductMapper>(context,localDataSource,remoteDataSource) {

}


