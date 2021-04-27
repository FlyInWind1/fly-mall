package fly.mall.modules.admin.service.impl

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import fly.mall.modules.admin.service.UmsAdminService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.util.FileSystemUtils
import java.nio.file.Path

@SpringBootTest
class UmsAdminServiceImplTest {
    @Autowired
    lateinit var umsAdminService: UmsAdminService

    @Test
    fun getByUsername() {
        println(umsAdminService.getByUsername("admin"))
    }

    @Test
    fun fopy() {
        FileSystemUtils.copyRecursively(Path.of("/mnt/d/linux/boot-starter"), Path.of("/tmp/boot-starter"))
    }

    @Test
    fun pdf() {
        val pdfDocument = PdfDocument(PdfWriter("a.pdf"))
        val document = Document(pdfDocument)
        document
    }

}