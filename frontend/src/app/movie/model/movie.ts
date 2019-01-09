import {Crew} from '../../crew/models/model-crew/crew';

export class Movie {
  id;
  title: string;
  duration;
  productionCountry: string;
  productionYear: string;
  directors: Crew;
  cast: string;
  movieShow: string;
  types: string[] = [];
}
