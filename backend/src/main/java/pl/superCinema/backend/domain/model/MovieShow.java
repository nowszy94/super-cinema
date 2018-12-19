package pl.superCinema.backend.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class MovieShow {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDate startMovieShow;
    private LocalDate endMovieShow;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "FILM_SHOWS_AND_CINEMA_HALLS",
            joinColumns = {@JoinColumn(name = "MOVIE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "CINEMA_HALL_ID")})
    List<CinemaHall> cinemaHalls;

    @OneToOne(mappedBy = "movieShow")
    private Movie movie;

    @OneToMany(mappedBy = "movieShow")
    List<SeatAvailability> seatsAvailability;

}
