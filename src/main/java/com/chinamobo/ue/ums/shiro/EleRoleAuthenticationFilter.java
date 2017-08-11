package com.chinamobo.ue.ums.shiro;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

public class EleRoleAuthenticationFilter extends RolesAuthorizationFilter{

    public boolean isAccessAllowed(ServletRequest request,  
            ServletResponse response, Object mappedValue) throws IOException {  
         return super.isAccessAllowed(request, response, buildRoles(request)); 
    }  
    protected String[] buildRoles(ServletRequest request) {  
        String[] perms = new String[1];  
        HttpServletRequest req = (HttpServletRequest) request;  
        String path = req.getServletPath();  
        //perms[0] = path;//path直接作为权限字符串  
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String url=httpRequest.getRequestURI();
        perms[0]=url;
        //SecurityUtils.getSubject().getPrincipals();
        return perms;  
    }  
    
    
}
