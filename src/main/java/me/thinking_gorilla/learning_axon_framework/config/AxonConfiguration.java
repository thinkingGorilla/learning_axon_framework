package me.thinking_gorilla.learning_axon_framework.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {

    private static final String[] ALLOWED_TYPES = {"me.thinking_gorilla.learning_axon_framework.**"};

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        // 이벤트 스토어에 이벤트를 읽거나 읽는데 사용할 클래스를 패키지 수준에서 제한한다.
        xStream.allowTypesByWildcard(ALLOWED_TYPES);
        return xStream;
    }
}
