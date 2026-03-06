package com.houtu.mp.util;

import io.github.lujiafa.houtu.util.common.JsonUtils;
import io.github.lujiafa.houtu.web.model.ResponseData;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.Writer;

public class ResponseUtils {

    final static Logger logger  = LoggerFactory.getLogger(ResponseUtils.class);

    public static void responseJson(HttpServletResponse response, ResponseData<?> responseData) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            Writer writer = response.getWriter();
            writer.write(JsonUtils.toStringIgnoreNull(responseData));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("responseJson error", e);
        }
    }
}
