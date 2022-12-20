package fly.asm

import org.junit.jupiter.api.Test
import org.springframework.asm.ClassWriter
import org.springframework.asm.Opcodes.*

class Test2 {
    /**
     * package pkg;
     * public interface Comparable extends Mesurable {
     *  int LESS = -1;
     *  int EQUAL = 0;
     *  int GREATER = 1;
     *  int compareTo(Object o);
     * }
     */
    @Test
    fun test1() {
        val cw = ClassWriter(ASM9)
        cw.visit(
            V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "pkg/Comparable",
            null, "java/lang/Object", arrayOf("pkg/Mesurable")
        )
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "LESS", "I", null, -1).visitEnd()
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "EQUAL", "I", null, 0).visitEnd();
        cw.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "GREATER", "I", null, 0).visitEnd();
        cw.visitMethod(ACC_PUBLIC+ACC_ABSTRACT,"compareTo","(Ljava/lang/Object;)I",null,null).visitEnd()
        cw.visitEnd()
        val b = cw.toByteArray()
    }
}
