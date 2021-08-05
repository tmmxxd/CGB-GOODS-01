package com.cy.pj.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@ConfigurationProperties(prefix = "page.config")
@Configuration
public class PaginationProperties {
    private Integer pageSize = 10;

    public Integer getStartIndex(Integer pageCurrent) {
        return (pageCurrent - 1) * pageSize;
    }
}
