package application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class APIApplication extends ResourceConfig {
	public APIApplication() {

		packages("com.chinamobo.ue");
		//register(LoggingFilter.class);  
		register(JspMvcFeature.class);
		register(RequestContextFilter.class);
		register(MultiPartFeature.class);
//		register(JacksonFeature.class);
	}
}
