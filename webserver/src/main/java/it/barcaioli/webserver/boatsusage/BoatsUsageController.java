package it.barcaioli.webserver.boatsusage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "boatsusage")
public class BoatsUsageController {

  private final BoatsUsageService boatsUsageService;

  @Autowired
  public BoatsUsageController(BoatsUsageService boatsUsageService) {
    this.boatsUsageService = boatsUsageService;
  }

  @GetMapping
  public Iterable<BoatsUsage> getBoatsUsage() {
    return boatsUsageService.getBoatsUsage();
  }

  @GetMapping(path = "{boatUsageId}")
  public BoatsUsage getBoatUsage(@PathVariable Long boatsUsageId) {
    return boatsUsageService.getBoatsUsage(boatsUsageId);
  }

  @GetMapping(path = "/trips/{tripId}")
  public Iterable<BoatsUsage> getBoatsUsageByTripId(@PathVariable Long tripId) {
    return boatsUsageService.getBoatsUsageByTripId(tripId);
  }
}
