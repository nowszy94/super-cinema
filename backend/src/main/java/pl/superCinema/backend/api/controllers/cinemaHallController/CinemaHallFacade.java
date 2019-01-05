package pl.superCinema.backend.api.controllers.cinemaHallController;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.superCinema.backend.api.controllers.seatController.SeatBuilderService;
import pl.superCinema.backend.api.dto.CinemaHallDto;
import pl.superCinema.backend.api.dto.SeatDto;
import pl.superCinema.backend.domain.exceptions.EntityCouldNotBeFoundException;
import pl.superCinema.backend.domain.model.CinemaHall;
import pl.superCinema.backend.domain.model.Seat;
import pl.superCinema.backend.domain.repository.CinemaHallRepository;
import pl.superCinema.backend.domain.repository.SeatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CinemaHallFacade {

    private CinemaHallRepository cinemaHallRepository;
    private CinemaHallBuilderService cinemaHallBuilderService;
    private SeatBuilderService seatBuilderService;
    private SeatRepository seatRepository;

    protected CinemaHallDto saveCinemaHall(CinemaHallDto cinemaHallDto){
        List<SeatDto> seatsDtos = cinemaHallDto.getSeats();
        List<Seat> seats = new ArrayList<>();
        if(seatsDtos != null){
            seats = seatsDtos.stream()
                    .map(seatDto -> {
                        Seat seat = seatBuilderService.entityFromDto(seatDto);
                            return seatRepository.save(seat);
                    })
                    .collect(Collectors.toList());
        }

        CinemaHall cinemaHall = cinemaHallBuilderService.entityFromDto(cinemaHallDto);
        cinemaHall.setSeats(seats);
        CinemaHall cinemaHallSaved = cinemaHallRepository.save(cinemaHall);
        seats.stream()
                .map(seat -> {
                    Optional<Seat> seatOptional = seatRepository.findById(seat.getId());
                    if(seatOptional.isPresent()){
                        seat.setCinemaHall(cinemaHallSaved);
                        seatRepository.save(seat);
                    }
                    return seat; })
                .collect(Collectors.toList());
        cinemaHallSaved.setSeats(seats);
        CinemaHall save = cinemaHallRepository.save(cinemaHallSaved);
        return cinemaHallBuilderService.dtoFromEntity(save);
    }

    protected CinemaHallDto getCinemaHallById(Long id){
        CinemaHall cinemaHallEntity = findCinemaHallEntity(id);
        return cinemaHallBuilderService.dtoFromEntity(cinemaHallEntity);
    }

    protected CinemaHallDto editCinemaHallById(Long id, CinemaHallDto cinemaHallDto){
        CinemaHall cinemaHallEntity = findCinemaHallEntity(id);
        List<Seat> seats = cinemaHallDto.getSeats()
                .stream()
                .map(seatDto -> seatBuilderService.entityFromDto(seatDto))
                .collect(Collectors.toList());
        if(seats != null){
            cinemaHallEntity.setSeats(seats);
        }
        if(cinemaHallDto.getMovieShows() != null) {
            cinemaHallEntity.setMovieShows(cinemaHallDto.getMovieShows());
        }

        CinemaHall cinemaHallSaved = cinemaHallRepository.save(cinemaHallEntity);
        return cinemaHallBuilderService.dtoFromEntity(cinemaHallSaved);
    }

    protected void deleteCInemaHallById(Long id){
        cinemaHallRepository.deleteById(id);
    }

    private CinemaHall findCinemaHallEntity(Long id) {
        return  cinemaHallRepository.findById(id).orElseThrow(
                () -> new EntityCouldNotBeFoundException("cinemaHall by id: " + id + " not found")
        );
    }
}
