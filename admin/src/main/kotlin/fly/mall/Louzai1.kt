package fly.mall

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class Louzai1 {
    val louzai1: Louzai1
    constructor(@Lazy louzai1: Louzai1){
        this.louzai1 = louzai1
    }

//    @Resource
//    private lateinit var louzai: Louzai2
//    @Resource
//    private lateinit var louzai1: Louzai1
}
