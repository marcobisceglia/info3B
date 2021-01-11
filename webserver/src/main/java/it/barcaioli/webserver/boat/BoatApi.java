package it.barcaioli.webserver.boat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoatApi {

    private final BoatService boatService;
    
    @Autowired
    public BoatApi(BoatService boatService){
    	this.boatService = boatService;
    }

    @GetMapping("/boats")
    public Iterable<Boat> getBoats() {
        return boatService.getBoats();
    }
    
    @GetMapping("/boats/{id}")
    public Boat getBoat(@PathVariable Long id) {
        return boatService.getBoat(id);
    }

    @PostMapping("/boats")
    public Boat createBoat(@RequestBody Boat newBoat) {
        return boatService.createBoat(newBoat);
    }
    
    @PutMapping("/boats/{id}")
    public Boat updateBoat(@PathVariable("id") Long id, @RequestBody Boat boat) {
        return boatService.updateBoat(id, boat);
    }

    @DeleteMapping("/boats/{id}")
    public void deleteBoat(@PathVariable("id") Long id) {
        boatService.deleteBoat(id);
    }

}