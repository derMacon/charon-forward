package com.dermacon.proxy.config;

import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.ForwardedRequestInterceptor;
import com.github.mkopylec.charon.core.http.ReceivedResponseInterceptor;
import com.github.mkopylec.charon.core.http.RequestData;
import com.github.mkopylec.charon.core.http.ResponseData;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class CustomForwardRequestInterceptor implements ForwardedRequestInterceptor, ReceivedResponseInterceptor {

    private static final String HREF_REPLACE_REGEX = "href=\"https://";

    @Override
    public void intercept(RequestData requestData, MappingProperties mappingProperties) {
        log.info("requestData: {}", requestData);
        log.info("mappingProperties: {}", mappingProperties);
        requestData.getHeaders().remove("Accept-Encoding");
        requestData.getHeaders().add("Content-Type", "text/html");
    }

    @Override
    public void intercept(ResponseData responseData, MappingProperties mappingProperties) {
        responseData.getHeaders().add("Accept-Encoding", "gzip");
        log.info("responseData: {}", responseData);
        log.info("mappingProperties: {}", mappingProperties);


        String html = responseData.getBodyAsString();
        responseData.setBody(html.replaceAll(HREF_REPLACE_REGEX, "href=\"http://localhost:8080/"));
    }
}

