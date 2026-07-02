package com.estatehub.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.estatehub.backend.model.BaseRepoImpl;


@Configuration
@EnableJpaRepositories(
	    basePackages = "com.estatehub.backend.model.repo",
	    repositoryBaseClass = BaseRepoImpl.class
	)
public class JpaConfig {

}
