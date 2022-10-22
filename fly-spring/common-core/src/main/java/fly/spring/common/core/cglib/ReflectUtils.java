package fly.spring.common.core.cglib;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

    public static PropertyDescriptor[] getBeanSetters(Class type) {
        return getPropertiesHelper(type, false, true);
    }

    private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write) {
        PropertyDescriptor[] all = BeanUtils.getPropertyDescriptors(type);
        if (read && write) {
            return all;
        }
        List properties = new ArrayList(all.length);
        for (int i = 0; i < all.length; i++) {
            PropertyDescriptor pd = all[i];
            if ((read && pd.getReadMethod() != null) ||
                    (write && pd.getWriteMethod() != null)) {
                properties.add(pd);
            }
        }
        return (PropertyDescriptor[]) properties.toArray(new PropertyDescriptor[properties.size()]);
    }

}
