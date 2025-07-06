package com.achrafelaffas.mcpserver.config;

import com.achrafelaffas.mcpserver.tools.GitlabTools;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public MethodToolCallbackProvider getMethodToolCallbackProvider() {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(new GitlabTools()).build();
    }
}
