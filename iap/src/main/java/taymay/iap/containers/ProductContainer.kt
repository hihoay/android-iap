



package taymay.iap.containers

import taymay.iap.usecases.ProductUsecase
import taymay.containers.RepositoryContainer
import taymay.iap.adaptors.datasources.local.ProductLocalDataSource
import taymay.iap.adaptors.datasources.remote.ProductRemoteDataSource
import taymay.iap.adaptors.repositories.ProductRepositoryImpl

fun RepositoryContainer.getProductRepository(): ProductRepositoryImpl {
    return ProductRepositoryImpl(
        context, ProductLocalDataSource(), ProductRemoteDataSource()
    )
}

class ProductContainer(repositoryContainer: RepositoryContainer) {
    var usecase: ProductUsecase
        init {

//            var ProductRepository: ProductRepositoryImpl

//            ProductRepository = ProductRepositoryImpl(
//                context = context,
//                localDataSource = ProductLocalDataSource(),
//                remoteDataSource = ProductRemoteDataSource()
//            )

            usecase = ProductUsecase(repositoryContainer.getProductRepository())
        }



}







