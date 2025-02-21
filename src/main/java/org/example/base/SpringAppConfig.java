package org.example.base;

import io.netty.channel.ChannelOption;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class SpringAppConfig {
    @Value("${spring.ai.ollama.base-url}")
    private String ollamaUrl;
    @Bean
    public RestClient.Builder ollamaRestClientBuilder() {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(
                java.net.http.HttpClient.newHttpClient());
        requestFactory.setReadTimeout(Duration.ofMinutes(3000));
        return RestClient.builder().requestFactory(requestFactory);
    }

@Bean
    public WebClient.Builder createWebClient() {
    reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300000000) // 连接超时
            .responseTimeout(Duration.ofMinutes(3000)); // 读取超时

    return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient));
}
    @Bean
    public OllamaApi ollamaApi(){
        return new OllamaApi(ollamaUrl,ollamaRestClientBuilder(),createWebClient());
    }

}
