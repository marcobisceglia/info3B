package it.barcaioli.webserver.trip;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TripRepositoryTest {

    @Autowired
    private TripRepository underTest;

    @Test
    void findByDateTime() {
        // given
        LocalDateTime date = LocalDateTime.of(2022, Month.JANUARY, 01, 10, 00, 00);
        Trip expected = new Trip(date);
        underTest.save(expected);

        // when
        Trip result = underTest.findByDateTime(date).get(0);

        // then
        assertThat(expected).isEqualTo(result);
    }
}
