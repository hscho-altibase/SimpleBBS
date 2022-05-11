package com.study.springboot;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfiguration {
	
    @Bean(name="myDataSource")
    //@ConfigurationProperties("spring.datasource")
    public DataSource myDataSource() {
    	String vcap_services = System.getenv("VCAP_SERVICES");
    	
    	System.out.println("");
    	System.out.println("====> vcap_services start");
    	System.out.println(vcap_services);
    	System.out.println("====> vcap_services end");
    	System.out.println("");
        

		/*
			{
			 "VCAP_SERVICES": {
			  "AltibaseDB": [
			   {
			    "binding_name": null,
			    "credentials": {
			     "hostname": "192.168.204.133",
			     "jdbcurl": "jdbc:Altibase://192.168.204.133:20300/mydb?user=eb2451ccbd77839b\u0026password=4780f81c55ff952d",
			     "name": "mydb",
			     "password": "4780f81c55ff952d",
			     "port": "20300",
			     "uri": "jdbc:Altibase://192.168.204.133:20300/mydb?user=eb2451ccbd77839b\u0026password=4780f81c55ff952d",
			     "username": "eb2451ccbd77839b"
			    },
			    "instance_name": "alti-service-instance",
			    "label": "AltibaseDB",
			    "name": "alti-service-instance",
			    "plan": "Altibase-Plan-Dedicated-vm",
			    "provider": null,
			    "syslog_drain_url": null,
			    "tags": [
			     "altibase",
			     "document"
			    ],
			    "volume_mounts": []
			   }
			  ]
			 }
			}
		*/

        String username = "";
        String password = "";
        String hostname = "";
        String port = "";
        String dbname = "";
        String url = "";
        String driverClassName = "Altibase.jdbc.driver.AltibaseDriver";
    	
    	if (vcap_services != null) {
	        JSONObject jsonObject = new JSONObject(vcap_services);
	
	        //JSONObject jsonObj = JSONObject.fromObject(vcap_services);
	        JSONArray jArray = jsonObject.getJSONArray("AltibaseDB");
	
	        JSONObject obj = jArray.getJSONObject(0);
	        obj = obj.getJSONObject("credentials");
	        
	        username = obj.getString("username");
	        password = obj.getString("password");
	        hostname = obj.getString("hostname");
	        port = obj.getString("port");
	        dbname = obj.getString("name");
	        url = "jdbc:Altibase://" + hostname + ":" + port + "/" + dbname;
    	}
        

    	System.out.println("username : " + username + ", password : " + password + ", url : " + url);

        HikariDataSource ds = (HikariDataSource) DataSourceBuilder 
        		.create()
        		.driverClassName(driverClassName) 
        		.url(url) 
        		.username(username)
        		.password(password)
        		.build();
        
        ds.setConnectionTestQuery("select 1 from dual"); // for isValid error
        
        return ds;
    }
}
