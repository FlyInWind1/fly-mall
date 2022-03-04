package fly.mall.modules.search.pojo

import fly.mall.modules.search.constant.Constant
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

@Document(indexName = "product", replicas = Constant.ES_REPLICAS, createIndex = false)
class EsProduct {

    @Id
    var id: Long? = null

    @Field(type = FieldType.Text, analyzer = Constant.IK_MAX_WORD)
    var name: String? = null

    var description: String? = null

    @Field(type = FieldType.Keyword)
    var productSn: String? = null

    var brandId: Long? = null

    @Field(type = FieldType.Keyword)
    var brandName: String? = null

    @Field(type = FieldType.Text, analyzer = Constant.IK_MAX_WORD)
    var subTitle: String? = null

    @Field(type = FieldType.Text, analyzer = Constant.IK_MAX_WORD)
    var keywords: String? = null

    var deleted: Boolean? = null
}