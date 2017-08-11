package com.chinamobo.ue.system.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.system.service.AuthoritiesTypeServise;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/authoritiesTypeRest")
@Scope("request")
@Component
public class AuthoritiesTypeRest {
	@Autowired
	private AuthoritiesTypeServise authoritiesTypeServise;
	ObjectMapper mapper = new ObjectMapper();

	@GET
	@Path("/findAll")
	@Produces({ MediaType.APPLICATION_JSON })
	public String findAll() {
		try {
			
			return mapper.writeValueAsString(authoritiesTypeServise.findAll());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"result\":\"error\"}";
	}
}
