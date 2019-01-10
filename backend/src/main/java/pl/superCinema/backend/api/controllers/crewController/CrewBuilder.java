package pl.superCinema.backend.api.controllers.crewController;

import pl.superCinema.backend.api.dto.CrewDto;
import pl.superCinema.backend.api.dto.CrewRoleDto;
import pl.superCinema.backend.domain.model.Crew;
import pl.superCinema.backend.domain.model.CrewRole;

import java.util.List;
import java.util.stream.Collectors;

public class CrewBuilder {

    public CrewDto entityToDto(Crew crew) {
        CrewDto crewDto = new CrewDto();
        crewDto.setId(crew.getId());
        crewDto.setName(crew.getName());
        crewDto.setSurname(crew.getSurname());
        if(crew.getCrewRoles() != null) {
            List<CrewRoleDto> crewRoleDtos = crew.getCrewRoles()
                    .stream()
                    .map(x -> CrewRoleDto.valueOf(x.name()))
                    .collect(Collectors.toList());
            crewDto.setCrewRoles(crewRoleDtos);
        }
        return crewDto;
    }

    public Crew dtoToEntity(CrewDto crewDto){
        Crew crew = new Crew();
        if(crewDto != null){
            crew.setId(crewDto.getId());
        }
        crew.setName(crewDto.getName());
        crew.setSurname(crewDto.getSurname());
        if(crewDto.getCrewRoles() != null) {
            List<CrewRole> crewRoles = crewDto.getCrewRoles()
                    .stream()
                    .map(x -> CrewRole.valueOf(x.name()))
                    .collect(Collectors.toList());
            crew.setCrewRoles(crewRoles);
        }
        return crew;
    }
}
