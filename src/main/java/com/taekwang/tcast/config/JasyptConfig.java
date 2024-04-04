package com.taekwang.tcast.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {
    @Bean(name = "encryptorBean")
    public StringEncryptor stringEncryptor(SimpleStringPBEConfig simpleStringPBEConfig) {
        PooledPBEStringEncryptor stringEncryptor = new PooledPBEStringEncryptor();
        stringEncryptor.setConfig(simpleStringPBEConfig);

        return stringEncryptor;
    }

    @Bean
    @ConfigurationProperties("jasypt.encryptor")
    public SimpleStringPBEConfig simpleStringPBEConfig() {
        return new SimpleStringPBEConfig();
    }
}
