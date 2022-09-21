package fly.mall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

//@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan("fly.spring", basePackageClasses = [AdminApplication::class])
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}
