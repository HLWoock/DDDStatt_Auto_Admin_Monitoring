package de.woock.ddd.stattauto.admin.monitoring;

import org.influxdb.dto.Point;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.influxdb.DefaultInfluxDBTemplate;
import org.springframework.data.influxdb.InfluxDBConnectionFactory;
import org.springframework.data.influxdb.InfluxDBProperties;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.data.influxdb.converter.PointConverter;

@EnableConfigurationProperties(InfluxDBProperties.class)
@EnableDiscoveryClient
@SpringBootApplication
public class DddStattAutoAdminMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(DddStattAutoAdminMonitoringApplication.class, args);
	}
	
	@Bean
	public InfluxDBConnectionFactory connectionFactory(final InfluxDBProperties properties) {
		return new InfluxDBConnectionFactory(properties);
	}

	@Bean
	public InfluxDBTemplate<Point> influxDBTemplate(final InfluxDBConnectionFactory connectionFactory) {
		/*
		 * You can use your own 'PointCollectionConverter' implementation, e.g.
		 * in case you want to use your own custom measurement object.
		 */
		return new InfluxDBTemplate<>(connectionFactory, new PointConverter());
	}

	@Bean
	public DefaultInfluxDBTemplate defaultTemplate(final InfluxDBConnectionFactory connectionFactory) {
		/*
		 * If you are just dealing with Point objects from 'influxdb-java' you
		 * could also use an instance of class DefaultInfluxDBTemplate.
		 */
		return new DefaultInfluxDBTemplate(connectionFactory);
	}
}
