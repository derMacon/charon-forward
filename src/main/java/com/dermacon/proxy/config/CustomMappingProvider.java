package com.dermacon.proxy.config;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.HttpClientProvider;
import com.github.mkopylec.charon.core.mappings.MappingsCorrector;
import com.github.mkopylec.charon.core.mappings.MappingsProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

@Component
@EnableConfigurationProperties({CharonProperties.class, ServerProperties.class})
@Configuration
@Slf4j
public class CustomMappingProvider extends MappingsProvider {

    private final List<MappingProperties> mappingProperties;

    public CustomMappingProvider(ServerProperties server, CharonProperties charon, MappingsCorrector mappingsCorrector, HttpClientProvider httpClientProvider) {
        super(server, charon, mappingsCorrector, httpClientProvider);

        MappingProperties mapping = new MappingProperties();
        mapping.setName("mapping");
        mapping.setPath("/test");
        mapping.setDestinations(Arrays.asList("https://www.google.com"));

        mappingProperties = Arrays.asList(mapping);
    }

    @Override
    protected boolean shouldUpdateMappings(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        mappingProperties.get(0).setPath(requestUrl);
        if (requestUrl.contains("/sw.js")) {
            return false;
        }

//        String forwardedUrl = requestUrl.substring(1);
        String forwardedUrl = "https://" + requestUrl.substring(1);
//        log.info("forwarded url: {}", forwardedUrl);
        mappingProperties.get(0).setDestinations(Arrays.asList(forwardedUrl));

        return true;
    }

    @Override
    protected List<MappingProperties> retrieveMappings() {
        return mappingProperties;
    }

}
