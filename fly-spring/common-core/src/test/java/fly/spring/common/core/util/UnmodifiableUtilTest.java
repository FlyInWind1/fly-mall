package fly.spring.common.core.util;

import fly.spring.common.pojo.R;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class UnmodifiableUtilTest {
    @Test
    void test1() {
        R<Object> r1 = new R<>();
        r1.setData("test");
        R<R<Object>> r2 = new R<>();
        r2.setData(r1);
        R<R<Object>> unmodifiable = UnmodifiableUtil.unmodifiable(r2);
        UnmodifiableUtil.unModifiableTmp();
        assertThrowsExactly(UnsupportedOperationException.class, () -> unmodifiable.getData().setData("a"));
        assertEquals(r1.getData(), unmodifiable.getData().getData());
    }
}
