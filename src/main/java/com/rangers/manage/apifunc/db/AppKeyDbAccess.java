package com.rangers.manage.apifunc.db;

import org.springframework.data.repository.CrudRepository;

import com.rangers.manage.apifunc.domain.AppKey;

public interface AppKeyDbAccess extends CrudRepository<AppKey, String> {

}
