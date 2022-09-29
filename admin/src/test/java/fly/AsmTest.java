package fly;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;


public class AsmTest implements Opcodes, Function<Object, String> {
    public static void main(String[] args) {
        Class<Function> functionClass = Function.class;
        System.out.println(functionClass.getName());
    }

    @Test
    public void test1() throws IOException, ClassNotFoundException {
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_STATIC, "fly/TestClass", "Ljava/util/function/Function<Ljava/lang/Object;Ljava/lang/String;>;", "java/lang/Object", new String[]{"java/util/function/Function"});
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "apply", "(Ljava/lang/Object;)Ljava/lang/String;", null, null);
        methodVisitor.visitParameter("o", 0);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitMaxs(1, 2);
        methodVisitor.visitEnd();
        classWriter.visitEnd();
        FileUtils.writeByteArrayToFile(new File("build/TestClass.class"), classWriter.toByteArray());
        InnerClassLoader classLoader = new InnerClassLoader();
        String className = "fly.TestClass";
        classLoader.defineClass(className, classWriter.toByteArray());
        Class<?> aClass = Class.forName(className, true, classLoader);
        System.out.println(aClass);
    }

    public static class InnerClassLoader extends ClassLoader {
        public void defineClass(String name, byte[] bytes) {
            defineClass(name, bytes, 0, bytes.length);
        }
    }

    public static class aClaa implements Function<Object, String> {
        @Override
        public String apply(Object o) {
            return o.toString();
        }
    }

    @Override
    public String apply(Object o) {
        String s = o.toString();
        return s.toString();
    }

}
