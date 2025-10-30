package com.houtu.mp.aspect;

import com.houtu.core.web.annotation.CachingParam;
import com.houtu.mp.support.type.ModuleType;
import com.houtu.mp.support.type.OperateType;

import java.lang.annotation.*;

@Documented
@Target(value={ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@CachingParam
public @interface OperateLog {

	ModuleType moduleType();

	OperateType operateType();

	boolean ignoreResponseData() default false;

}