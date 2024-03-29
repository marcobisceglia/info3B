package it.barcaioli.webserver.boat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "boats")
public class BoatController {

	private final BoatService boatService;

	@Autowired
	public BoatController(BoatService boatService) {
		this.boatService = boatService;
	}

	private Boat dtoToEntity(BoatDto boatDto) {
		var boat = new Boat();

		boat.setModel(boatDto.getModel());
		boat.setSeats(boatDto.getSeats());

		return boat;
	}

	@GetMapping
	public Iterable<Boat> getBoats() {
		return boatService.getBoats();
	}

	@GetMapping(path = "{boatId}")
	public Boat getBoat(@PathVariable Long boatId) {
		return boatService.getBoat(boatId);
	}

	@PostMapping()
	public Boat createBoat(@RequestBody BoatDto boatDto) {
		var boat = dtoToEntity(boatDto);
		return boatService.createBoat(boat);
	}

	@PutMapping(path = "{boatId}")
	public Boat updateBoat(@PathVariable("boatId") Long boatId, @RequestBody BoatDto boatDto) {
		var boat = dtoToEntity(boatDto);
		return boatService.updateBoat(boatId, boat);
	}

	@DeleteMapping(path = "{boatId}")
	public void deleteBoat(@PathVariable("boatId") Long boatId) {
		boatService.deleteBoat(boatId);
	}
}
