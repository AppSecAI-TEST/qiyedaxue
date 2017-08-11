package com.chinamobo.ue.api.message.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.chinamobo.ue.ums.DBContextHolder;


@Path("/messageTest")
@Scope("request")
@Component
public class MessageResult {
	@Autowired
	private MessageApiService examApiService;

	@GET
	@Path("/get/{name}")
	public String test(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("name") String name) {
		if (name.equals("eleMyMessagesList")) {
			DBContextHolder.setDbType(DBContextHolder.SLAVE);
			return examApiService.eleMyMessagesList(request, response).getResults();
		} else if (name.equals("eleMyMessageDetails")) {
			DBContextHolder.setDbType(DBContextHolder.MASTER);
			return examApiService.eleMyMessageDetails(request, response).getResults();
		}
		return null;
	}
}
