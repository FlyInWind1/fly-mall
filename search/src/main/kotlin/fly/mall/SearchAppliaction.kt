package fly.mall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("fly.spring", basePackageClasses = [SearchApplication::class])
class SearchApplication

fun main(args: Array<String>) {
    runApplication<SearchApplication>(*args)
}