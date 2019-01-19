import { Component, OnInit } from '@angular/core';
import { ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {NotificationService} from '../../share/notification.service';
import {NgForm} from "@angular/forms";
import {MovieService} from "../../services/service-movie/movie-service";
import {Crew} from "../../crew/model/crew";
import {CrewService} from "../../services/crew-service/crew-service";
import {CrewId} from "../../crew/model/crewId";
import {CrewInMovieComponent} from "../../crew/crew-in-movie/crew-in-movie.component";
import {MatDialog} from "@angular/material";
import {CrewInMovieService} from "../../services/crew-in-movie-service/crew-in-movie.service";
import {Movie} from "../model/movie";

@Component({
  selector: 'app-edit-movie',
  templateUrl: './edit-movie.component.html',
  styleUrls: ['./edit-movie.component.scss']
})
export class EditMovieComponent implements OnInit {

  //TODO data form database
  movieTypesArray = [
    {value: 'COMEDY', name: 'comedy', checked: false},
    {value: 'HORROR', name: 'horror', checked: false},
    {value: 'SF', name: 'science - fiction', checked: false},
    {value: 'ACTION', name: 'action', checked: false},
    {value: 'THRILLER', name: 'thriller', checked: false},
    {value: 'DRAMA', name: 'drama', checked: false},
    {value: 'CRIME', name: 'crime', checked: false},
    {value: 'FANTASY', name: 'fantasy', checked: false},
    {value: 'MUSICAL', name: 'musical', checked: false},
    {value: 'ANIMATION', name: 'animation', checked: false},
    {value: 'WESTERNS', name: 'western', checked: false}
  ];

  private movie: Movie;
  private actorsList;
  private actorsListToAdd;
  private actorsIdsList;
  private existingDirectorsList;
  private directorsListToAdd;
  private directorsListToUpdateMovie;
  private directorsIdsList: CrewId[] = [];
  private isPopupOpened: boolean = false;

  constructor(private httpClient: HttpClient,
              private crewService: CrewService,
              private route: ActivatedRoute,
              private movieService: MovieService,
              private crewInMovieService: CrewInMovieService,
              private matDialog: MatDialog,
              private notification: NotificationService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.movie = new Movie;
      this.movie.id = params['id'];
    });
    this.getMovieData();
  }

  wasTypeSelected(movieType: { value: string; name: string; checked: boolean }) {

    if (this.movie.types.includes(movieType.value)) {
      movieType.checked = true;
      return true;
    }
    movieType.checked = false;
    return false;
  }

  checkMovieType(i, event) {
    this.movieTypesArray[i].checked = !this.movieTypesArray[i].checked;
    if (this.movieTypesArray[i].checked) {
      this.movie.types.push(this.movieTypesArray[i].value);
    } else {
      const indexOf = this.movie.types.indexOf(this.movieTypesArray[i].value);
      this.movie.types.splice(indexOf, 1);
    }
  }

  saveChanges(editMovieForm: NgForm) {
    const checkedMovieTypes = this.movieTypesArray.filter(type => type.checked === true).map(type => type.value);
    this.formDataValidation(editMovieForm);
    this.movieService.updateMovie(this.movie.id, this.movie)
      .subscribe(
        (data: any) => {
          this.notification.success('Edited ' + this.movie.title + ' movie succesfully' );
        },
        (error) => {
          this.notification.warn(error);
        }
        );
  }

  private getMovieData() {
    this.movieService.getMovie(this.movie.id)
      .subscribe(
        (data: any) => {
          this.movie.title = data.title;
          this.movie.duration = data.duration;
          this.movie.productionCountry = data.productionCountry;
          this.movie.productionYear = data.productionYear;
          this.movie.cast = data.cast;
          this.movie.directors = data.directors;
          this.movie.movieShow = data.movieShow;
          this.movie.types = data.types;
          this.existingDirectorsList = this.movie.directors;
          this.actorsList = this.movie.cast;
        });


  }

  private formDataValidation(form: NgForm){
    if (this.movie.title === '' || this.movie.title == null) {
      this.notification.warn('Please give title.');
      return;
    }
    if (this.movie.duration === '' || this.movie.duration == null ) {
      this.notification.warn('Please give movie duration.');
      return;
    }
    if (Number.isNaN(Number(this.movie.duration))) {
      this.notification.warn('Duration must be given as a number');
      return;
    }
  }

  addCrewListToEditMovieComponent(crewRole: string) {
    this.isPopupOpened = true;
    const dialogRef = this.matDialog.open(CrewInMovieComponent, {
      width: '700px',
      height: '500px',
      data: {crewRole}
    });
    dialogRef.afterClosed()
      .subscribe(data => {
        this.isPopupOpened = false;
        this.directorsListToAdd = this.crewInMovieService.getAllDirectors();
        let existingDirectorsIdsList = this.existingDirectorsList.map(directors => directors.id);
        this.directorsListToAdd.map(directorToAdd => {
          if(existingDirectorsIdsList.includes(directorToAdd.id) === false){
            this.existingDirectorsList.push(directorToAdd);
          }

        })

        })

      }


}


