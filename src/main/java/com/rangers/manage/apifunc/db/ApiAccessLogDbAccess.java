package com.rangers.manage.apifunc.db;

import org.springframework.data.repository.CrudRepository;

import com.rangers.manage.apifunc.domain.ApiAccessLog;

public interface ApiAccessLogDbAccess extends CrudRepository<ApiAccessLog, Long> {

}
