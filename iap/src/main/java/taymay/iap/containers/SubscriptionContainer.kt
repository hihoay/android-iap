



package taymay.iap.containers

import taymay.iap.usecases.SubscriptionUsecase
import taymay.containers.RepositoryContainer
import taymay.iap.adaptors.datasources.local.SubscriptionLocalDataSource
import taymay.iap.adaptors.datasources.remote.SubscriptionRemoteDataSource
import taymay.iap.adaptors.repositories.SubscriptionRepositoryImpl

fun RepositoryContainer.getSubscriptionRepository(): SubscriptionRepositoryImpl {
    return SubscriptionRepositoryImpl(
        context, SubscriptionLocalDataSource(), SubscriptionRemoteDataSource()
    )
}

class SubscriptionContainer(repositoryContainer: RepositoryContainer) {
    var usecase: SubscriptionUsecase
        init {

//            var SubscriptionRepository: SubscriptionRepositoryImpl

//            SubscriptionRepository = SubscriptionRepositoryImpl(
//                context = context,
//                localDataSource = SubscriptionLocalDataSource(),
//                remoteDataSource = SubscriptionRemoteDataSource()
//            )

            usecase = SubscriptionUsecase(repositoryContainer.getSubscriptionRepository())
        }



}







