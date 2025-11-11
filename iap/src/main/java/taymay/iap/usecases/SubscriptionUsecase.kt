package taymay.iap.usecases

import taymay.iap.adaptors.mapper.SubscriptionMapper
import taymay.iap.repositories.SubscriptionRepository
import taymay.usecases.EntityUsecase

class SubscriptionUsecase(private val repositorySubscription: SubscriptionRepository) :
    EntityUsecase<SubscriptionMapper>(repositorySubscription) {
}
