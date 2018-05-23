package io.spring.lab.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Bean;

import com.netflix.turbine.streaming.servlet.TurbineStreamServlet;

@SpringBootApplication
@EnableHystrixDashboard
@EnableTurbine
public class DashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
	}

	// org.springframework.cloud.netflix.turbine.TurbineHttpConfiguration
	@Bean
	public ServletRegistrationBean turbineStreamServlet() {
		return new ServletRegistrationBean(new TurbineStreamServlet(), "/admin/turbine.stream");
	}

}
