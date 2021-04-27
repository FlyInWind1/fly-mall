package fly.mall.modules.search.respository

import fly.mall.modules.search.pojo.EsProduct
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository

interface ProductRepository : ReactiveElasticsearchRepository<EsProduct, Long>