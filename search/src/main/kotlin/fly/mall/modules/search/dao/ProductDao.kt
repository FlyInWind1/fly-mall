package fly.mall.modules.search.dao

import fly.mall.modules.search.pojo.EsProduct
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ProductDao {

    fun getWithLimit(@Param("startId") startId: Long?, @Param("batchSize") batchSize: Int?): List<EsProduct>

}