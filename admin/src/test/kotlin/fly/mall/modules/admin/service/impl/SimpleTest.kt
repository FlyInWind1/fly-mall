package fly.mall.modules.admin.service.impl

import org.junit.jupiter.api.Test
import org.springframework.util.FileSystemUtils
import java.nio.file.Path

class SimpleTest {
    @Test
    fun copy()   {
        FileSystemUtils.copyRecursively(Path.of("/mnt/d/Pictures/微信图片_20201004132722.jpg"), Path.of("/tmp/a/a.jpg"))
    }
}