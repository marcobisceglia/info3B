package it.barcaioli.webserver.boat;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.barcaioli.webserver.boatsusage.BoatsUsage;

@Table(name = "Boat")
@Entity
public class Boat implements Comparable<Boat> {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String model;
	private Integer seats;

	// 1 Boat can be used many time (BoatsUsage)
	// mappedBy a boat private field in BoatsUsage
	// Each operation on the boat will be propagated to the boatsUsage
	// BoatsUsage are removed if boat is removed
	@JsonIgnore
	@OneToMany(mappedBy = "boat", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BoatsUsage> boatsUsage;

	Boat() {
	}

	public Boat(String model, Integer seats) {
		super();
		this.model = model;
		this.seats = seats;
	}

	public Long getId() {
		return id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public List<BoatsUsage> getBoatsUsage() {
		return boatsUsage;
	}

	public void setBoatsUsage(List<BoatsUsage> boatsUsage) {
		this.boatsUsage = boatsUsage;
	}

	@Override
	public int compareTo(Boat o) {
		return this.seats - o.seats;
	}
}
