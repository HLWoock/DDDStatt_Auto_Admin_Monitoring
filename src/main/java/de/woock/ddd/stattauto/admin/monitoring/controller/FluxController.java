package de.woock.ddd.stattauto.admin.monitoring.controller;

import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

@RestController
public class FluxController {
	
	@Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;
	
	
	@GetMapping("/add/{mitgliedId}/{fahrzeugId}/{von}/{bis}/{zeit}/{km}")
	private void monitor(@PathVariable("mitgliedId")String mitgliedId, @PathVariable("fahrzeugId")String fahrzeugId,
			             @PathVariable("von")String von, @PathVariable("bis")String bis,
			             @PathVariable("zeit")String zeit, @PathVariable("km")String km) {
		influxDBTemplate.getDatabase();
//      influxDBTemplate.query(new Query("DROP SERIES FROM disk", "stattauto_ddd"));
//      influxDBTemplate.query(new Query("DROP SERIES FROM cpu" , "stattauto_ddd"));
        final Point p1 = Point.measurement("reservierung")
                              .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                              .tag("mitglied", mitgliedId)
                              .addField("von", von)
                              .addField("bis", bis)
                              .build();
        final Point p2 = Point.measurement("reservierung")
        	                  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        	                  .tag("fahrzeug", fahrzeugId)
        	                  .addField("zeit", zeit)
        	                  .addField("km", km)
        	                  .build();
        influxDBTemplate.write(Lists.newArrayList(p1, p2));
	}
	
	@GetMapping("/get")
	public String getResults() {
		QueryResult query = influxDBTemplate.query(new Query("SELECT * FROM reservierung GROUP BY mitglied", "stattauto_ddd"));
		return query.getResults().toString();
		
	}
}
