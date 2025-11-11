package taymay.iap.repositories

import taymay.adaptors.repositories.EntityRepositoryImpl

import android.content.Context
import taymay.iap.adaptors.datasources.local.SubscriptionLocalDataSource
import taymay.iap.adaptors.datasources.remote.SubscriptionRemoteDataSource
import taymay.iap.adaptors.mapper.SubscriptionMapper

abstract class SubscriptionRepository (
    context: Context,
    localDataSource: SubscriptionLocalDataSource,
    remoteDataSource: SubscriptionRemoteDataSource
) : EntityRepositoryImpl<SubscriptionMapper>(context,localDataSource,remoteDataSource) {

}


