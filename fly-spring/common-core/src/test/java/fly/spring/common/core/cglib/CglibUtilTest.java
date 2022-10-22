package fly.spring.common.core.cglib;

import fly.spring.common.pojo.R;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CglibUtilTest {

    @Test
    void test1() {
        R<Object> source = new R<>();
        source.setData("c");
        source.setMsg("c");
        R2<Object> target = new R2<>();
        CglibUtil.copy(source, target);
        assertEquals(source.getData(), target.getData());
        assertEquals(source.getMsg(), target.getMsg());
    }

    @Data
    public class R2<T> implements Serializable {
        private int code;

        private String msg;

        private T data;
    }
}
