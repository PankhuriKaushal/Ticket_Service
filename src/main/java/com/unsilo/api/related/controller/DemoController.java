package com.unsilo.api.related.controller;

//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@Validated

public class DemoController {

	public static Map<String, StationDetail> stations = new HashMap<>();
    public static Map<String, Ticket> tickets = new HashMap<>();

    {
        stations.put("A", new StationDetail(true, false, 0));
        stations.put("B", new StationDetail(false, false, 10));
        stations.put("C", new StationDetail(false, false, 20));
        stations.put("D", new StationDetail(false, false, 30));
        stations.put("E", new StationDetail(false, false, 40));
        stations.put("F", new StationDetail(false, false, 50));
        stations.put("G", new StationDetail(false, false, 60));
        stations.put("H", new StationDetail(false, false, 70));
        stations.put("I", new StationDetail(false, false, 80));
        stations.put("J", new StationDetail(false, false, 90));

        stations.put("K", new StationDetail(false, false, 100));
        stations.put("L", new StationDetail(false, false, 110));
        stations.put("M", new StationDetail(false, false, 120));
        stations.put("N", new StationDetail(false, false, 130));
        stations.put("O", new StationDetail(false, false, 140));
        stations.put("P", new StationDetail(false, false, 150));
        stations.put("Q", new StationDetail(false, false, 160));
        stations.put("R", new StationDetail(false, false, 170));
        stations.put("S", new StationDetail(false, false, 180));
        stations.put("T", new StationDetail(false, true, 190));
    }

    @RequestMapping(value = "/getTicket", method = RequestMethod.GET, produces = "application/json")
    public Ticket getTicket(@RequestParam String source, @RequestParam String destination) {
        Ticket ticket = new Ticket();
        ticket.source = source;
        ticket.destination = destination;
        ticket.id = String.valueOf(UUID.randomUUID());
        ticket.expirationTimestamp = Instant.now().plus(18, ChronoUnit.HOURS);
        long deltaPrice = stations.get(source).price - stations.get(destination).price;
        if (deltaPrice < 0) {
            deltaPrice = deltaPrice * -1;
        }
        ticket.price = deltaPrice;
        tickets.put(ticket.id, ticket);
        System.out.println(ticket);
        return ticket;
    }

    @GetMapping(value = "/entry")
    public String entry(@PathVariable String id) {
        if (tickets.containsKey(id)) {
            return "Entry Succesfully done";
        }
        throw new RuntimeException("Invalid Ticket");
    }

    @GetMapping(value = "/exit")
    public String exit(@PathVariable String id) {
        if (tickets.containsKey(id)) {
            Ticket ticket = tickets.get(id);
            boolean deltaTime = ticket.expirationTimestamp.isAfter(Instant.now());
            if (deltaTime) {
                tickets.remove(id);
                return "Exit Succesfully done";
            } else {
                throw new RuntimeException("Ticket got TimeOut ");
            }
        } else {
            throw new RuntimeException("Invalid Ticket");
        }
    }
}

@AllArgsConstructor
class StationDetail {
    public StationDetail(boolean b, boolean c, int i) {
		// TODO Auto-generated constructor stub
    	this.startStation=startStation;
    	this.lastStation=lastStation;
    	this.price=price;
	}
	boolean startStation;
    boolean lastStation;
    long price;
}

class Ticket {
    String id;
    Instant expirationTimestamp;

    String source;
    String destination;
    long price;

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", expirationTimestamp=" + expirationTimestamp +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", price=" + price +
                '}';
    }
}
