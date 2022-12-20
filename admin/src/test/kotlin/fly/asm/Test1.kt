package fly.asm

import org.springframework.asm.*
import org.springframework.asm.Opcodes.ASM4
import kotlin.test.Test

class Test1 {
    class ClassPrinter : ClassVisitor(ASM4) {
        override fun visit(
            version: Int,
            access: Int,
            name: String?,
            signature: String?,
            superName: String?,
            interfaces: Array<out String>?
        ) {
            println("$name extends $superName {")
        }

        override fun visitSource(source: String?, debug: String?) {
        }

        override fun visitOuterClass(owner: String?, name: String?, descriptor: String?) {
        }

        override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
            return null;
        }

        override fun visitAttribute(attribute: Attribute?) {
        }

        override fun visitInnerClass(name: String?, outerName: String?, innerName: String?, access: Int) {
        }

        override fun visitField(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            value: Any?
        ): FieldVisitor? {
            println(" $descriptor $name")
            return null;
        }

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor? {
            println(" $name $descriptor")
            return null;
        }

        override fun visitEnd() {
            println("}")
        }
    }

    @Test
    fun classPrintTest() {
        val classPrinter = ClassPrinter()
        val classReader = ClassReader(Runnable::class.qualifiedName)
        classReader.accept(classPrinter, 0)
    }
}
