package taymay.iap.usecases

import taymay.iap.adaptors.mapper.ProductMapper
import taymay.iap.repositories.ProductRepository
import taymay.usecases.EntityUsecase

class ProductUsecase(private val repositoryProduct: ProductRepository) :
    EntityUsecase<ProductMapper>(repositoryProduct) {
}
