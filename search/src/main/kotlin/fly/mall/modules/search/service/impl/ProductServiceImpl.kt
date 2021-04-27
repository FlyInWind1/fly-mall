package fly.mall.modules.search.service.impl

import fly.mall.modules.search.dao.ProductDao
import fly.mall.modules.search.pojo.EsProduct
import fly.mall.modules.search.respository.ProductRepository
import fly.mall.modules.search.service.ProductService
import mu.KotlinLogging
import org.apache.commons.collections4.CollectionUtils
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
//        op: ElasticsearchEntityInformationCreatorImpl<EsProduct, Long>,
        private val productDao: ProductDao, private val productRepository: ProductRepository
) :
//        SimpleReactiveElasticsearchRepository<EsProduct, Long>(op),
        ProductService {

    companion object {
        val log = KotlinLogging.logger { }
    }

    override fun importAll(): Int {
        var startId = 0L
        val batchSize = 1000
        var result = 0
        var batchLastEntity: EsProduct? = null

        while (true) {
            val products = productDao.getWithLimit(startId, batchSize)
            if (CollectionUtils.isEmpty(products))
                break
            val productsIterable = productRepository.saveAll(products)
            productsIterable.subscribe {
                result++
                batchLastEntity = it
            }
            startId = batchLastEntity!!.id!!
            log.info("imported: $result")
        }
        return result
    }

//    override fun deleteById(id: Long) {
//        productRepository.deleteById(id)
//    }
}