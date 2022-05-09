package com.syl.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Liu XiangLiang
 * @date 2022/4/30 下午7:47
 */
public class ConvertClassUtil {
    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return Collections.emptyList();
    }
}
