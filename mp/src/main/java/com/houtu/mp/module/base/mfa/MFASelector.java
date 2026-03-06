package com.houtu.mp.module.base.mfa;

import io.github.lujiafa.houtu.core.exception.BusinessException;
import io.github.lujiafa.houtu.core.exception.ErrorCode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MFASelector implements InitializingBean, ApplicationContextAware {

    private static final Map<String, MFAProcessor> PROCESSOR_MAP = new HashMap<>();

    private ApplicationContext applicationContext;

    public static List<MFAProcessor.MFAType> getMfaTypes() {
        return PROCESSOR_MAP.values().stream().map(MFAProcessor::getMfaType).collect(Collectors.toList());
    }

    public static MFAProcessor getMFAProcessor(String mfaType) {
        MFAProcessor mfaProcessor = PROCESSOR_MAP.get(mfaType);
        if (mfaProcessor == null) {
            throw new BusinessException(ErrorCode.build(30, Stream.of(mfaType).toArray()));
        }
        return mfaProcessor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, MFAProcessor.class).values().forEach(v -> {
            PROCESSOR_MAP.put(v.getMfaType().getMfaType(), v);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
