package com.kyc.bills.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kyc.core.config.BuildDetailConfig;
import com.kyc.core.exception.handlers.KycGenericRestExceptionHandler;
import com.kyc.core.exception.handlers.KycUnhandledExceptionHandler;
import com.kyc.core.exception.handlers.KycValidationRestExceptionHandler;
import com.kyc.core.properties.KycMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;

import static com.kyc.bills.constants.AppConstants.MESSAGE_000;
import static com.kyc.bills.constants.AppConstants.MESSAGE_001;

@Import(value = {KycMessages.class, BuildDetailConfig.class, KycGenericRestExceptionHandler.class})
@Configuration
public class AppConfig {

    @Bean
    public JavaTimeModule javaTimeModule(){
        return new JavaTimeModule();
    }

    @Bean
    public KycUnhandledExceptionHandler kycUnhandledExceptionHandler(KycMessages kycMessages){

        return new KycUnhandledExceptionHandler(kycMessages.getMessage(MESSAGE_000));
    }

    @Bean
    public KycValidationRestExceptionHandler kycValidationRestExceptionHandler(KycMessages kycMessages){

        return new KycValidationRestExceptionHandler(kycMessages.getMessage(MESSAGE_001));
    }

    @Bean
    public JdbcMappingContext jdbcMappingContext() {
        DefaultNamingStrategy namingStrategy = new DefaultNamingStrategy();
        //namingStrategy.setForeignKeyNaming(ForeignKeyNaming.IGNORE_RENAMING);
        JdbcMappingContext jdbcMappingContext =  new JdbcMappingContext(namingStrategy);
        jdbcMappingContext.setForceQuote(false);
        return jdbcMappingContext;
    }
}
