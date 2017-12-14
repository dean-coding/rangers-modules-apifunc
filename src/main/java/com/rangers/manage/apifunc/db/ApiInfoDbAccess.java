package com.rangers.manage.apifunc.db;

import org.springframework.data.repository.CrudRepository;

import com.rangers.manage.apifunc.domain.ApiInfo;

public interface ApiInfoDbAccess extends CrudRepository<ApiInfo, String> {

}
