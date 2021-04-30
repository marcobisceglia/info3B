package it.barcaioli.webserver.trip;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;

@Table(name = "Trip")
@Entity
public class Trip {

	private @Id @GeneratedValue Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Europe/Paris")
	private Date dateTime;

	Trip() {
	}

	public Trip(Date dateTime) {
		super();
		this.dateTime = dateTime;
	}

	public Long getId() {
		return id;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
}
