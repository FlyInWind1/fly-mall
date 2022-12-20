package fly.asm

import org.springframework.asm.ClassReader
import org.springframework.asm.ClassVisitor
import org.springframework.asm.ClassWriter
import org.springframework.asm.Opcodes.ASM9
import org.springframework.asm.Opcodes.V1_8
import java.util.function.Function
import kotlin.test.Test

class Test3 {
    @Test
    fun test1() {
        val cw = ClassWriter(0)
        // cv将所有时间转发给cw
        val cv = object : ClassVisitor(ASM9, cw) {}
        val cr = ClassReader(Function::class.qualifiedName)
        cr.accept(cv, 0)
        val b = cw.toByteArray()
        // b 就等于 Function::class.qualifiedName 的字节码
    }

    class ChangeVersionAdapter(cv: ClassVisitor) : ClassVisitor(ASM9, cv) {
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            cv.visit(V1_8, access, name, signature, superName, interfaces)
        }
    }

    @Test
    fun test2() {

    }
}
