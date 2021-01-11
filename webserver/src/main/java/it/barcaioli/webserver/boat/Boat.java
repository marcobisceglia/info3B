package it.barcaioli.webserver.boat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Boat {
	private @Id @GeneratedValue Long id;
    private String model;
    private Integer numSeats;
    
    Boat(){}
    
    public Boat(String model, Integer numSeats) {
		super();
		this.model = model;
		this.numSeats = numSeats;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}
    
}