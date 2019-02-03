package pl.superCinema.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.superCinema.backend.domain.model.Crew;
import pl.superCinema.backend.domain.model.MovieShow;
import pl.superCinema.backend.domain.model.Type;

import java.util.List;

@Getter
@Setter
public class MovieDto {
    private Long id;
    private String title = "";
    private Integer duration;
    private String productionCountry = "";
    private Integer productionYear;

    @JsonProperty("types")
    private List<TypeDto> types;

    private List<CrewDto> directors;
    private List<CrewDto> cast;
    private MovieShow movieShow;


}
