package org.d2rq.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.d2rq.SystemLoader;


/**
 * Initialize D2R server on startup of an appserver such as Tomcat. This listener should
 * be included in the web.xml. This is compatible with Servlet 2.3 spec compliant appservers.
 *
 * @author Inigo Surguy
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class WebappInitListener implements ServletContextListener {
	private final static Log log = LogFactory.getLog(WebappInitListener.class);
	
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();

		// Is there already a loader in the servlet context, put there
		// by the JettyLauncher?
		SystemLoader loader = D2RServer.retrieveSystemLoader(context);
		if (loader == null) {
			log.info("Fresh ServletContext; initializing a new SystemLoader");
			// We are running as pure webapp, without JettyLauncher,
			// so initalize the loader from the configFile context parameter.
			loader = new SystemLoader();
			D2RServer.storeSystemLoader(loader, context);
			if (context.getInitParameter("configFile") == null) {
				throw new RuntimeException("No configFile configured in web.xml");
			}
			String configFileName = absolutize(context.getInitParameter("configFile"), context);
			loader.setMappingFile(configFileName);
			loader.setResourceStem("resource/");
		}
		if (loader.getJdbcURL()==null){
			String jdbcurl = context.getInitParameter("jdbcurl");	
			String username = context.getInitParameter("username");	
			String password = context.getInitParameter("password");	
			String baseuri = context.getInitParameter("baseuri");
			String localport = context.getInitParameter("localport");
			
			if(username !=null)
				loader.setUsername(username);
			if(password !=null)
				loader.setPassword(password);
			if(jdbcurl !=null)
				loader.setJdbcURL(jdbcurl);
			if(baseuri !=null)
				loader.setSystemBaseURI(baseuri);
			if(localport !=null)
				try{
					int i = Integer.parseInt(localport);
					if (i>=65535){
						i=-1;
						throw new NumberFormatException();
					}
					loader.setPort(i);
				}
			catch (NumberFormatException e){
				System.out.println(localport+" is not a valid port number");
			}
			
			//if it's still null 
			if (loader.getJdbcURL()==null){
				System.out.println("no JDBC URL specified!");
				System.out.println("insert <param-name>jdbcurl</param-name>\n<param-value>url</param-value> etc into web.xml");
			}			
			
		}
		
		D2RServer server = loader.getD2RServer();
		server.start();
		VelocityWrapper.initEngine(server, context);

	}

	public void contextDestroyed(ServletContextEvent event) {
		D2RServer server = D2RServer.fromServletContext(event.getServletContext());
		if (server != null)
			server.shutdown();
	}
	
	private String absolutize(String fileName, ServletContext context) {
		if (!fileName.matches("[a-zA-Z0-9]+:.*")) {
			fileName = context.getRealPath("/WEB-INF/" + fileName);
		}
		return ConfigLoader.toAbsoluteURI(fileName);
	}
}
