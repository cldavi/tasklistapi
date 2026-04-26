package com.personalproject.tasklist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class NullValuesRequest {
    
    public static String[] getNUllProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyValues = new HashSet<>();

        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyValues.add(pd.getName());
            }
        }
        String[] result = new String[emptyValues.size()];
        return emptyValues.toArray(result);
    }

    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNUllProperties(source));
    }
}
