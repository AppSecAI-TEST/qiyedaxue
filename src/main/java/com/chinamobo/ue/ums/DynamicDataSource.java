package com.chinamobo.ue.ums;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{

	@Override
	public Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		 return DBContextHolder.getDbType();
	}
	
}
