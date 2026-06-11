package donut.rush

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RushApplication

fun main(args: Array<String>) {
    runApplication<RushApplication>(*args)
}
