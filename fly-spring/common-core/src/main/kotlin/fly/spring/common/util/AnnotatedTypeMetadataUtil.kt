package fly.spring.common.util

import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.core.type.ClassMetadata

@Suppress("unused")
object AnnotatedTypeMetadataUtil {

    @JvmStatic
    fun classNameEquals(metadata: AnnotatedTypeMetadata, className: String): Boolean {
        if (metadata is ClassMetadata && metadata.className == className) {
            return true
        }
        return false
    }

}
