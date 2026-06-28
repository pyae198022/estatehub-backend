package com.estatehub.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.estatehub.backend.model.BaseRepoImpl;


@Configuration
@EnableJpaRepositories(repositoryBaseClass = BaseRepoImpl.class)
public class JpaConfig {

}
