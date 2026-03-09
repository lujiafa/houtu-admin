package com.xx.mp.aspect;

import io.github.lujiafa.houtu.core.web.annotation.CachingParam;
import com.xx.mp.support.type.ModuleType;
import com.xx.mp.support.type.OperateType;

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