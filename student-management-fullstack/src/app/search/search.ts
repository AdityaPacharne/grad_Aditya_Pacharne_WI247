import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Student } from '../models/student';
import { StudentService } from '../services/student';

@Component({
  selector: 'app-search',
  standalone: false,
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search {

  regNoInput = 0;
  foundStudent$!: Observable<Student>;

  schoolInput = '';
  schoolStudents$!: Observable<Student[]>;

  schoolCountInput = '';
  schoolCount$!: Observable<number>;

  standardInput = 0;
  standardCount$!: Observable<number>;

  resultStudents$!: Observable<Student[]>;
  resultLabel = '';

  genderInput = '';
  strengthStandardInput = 0;
  strength$!: Observable<number>;

  constructor(private service: StudentService) {}

  findByRegNo() {
    this.foundStudent$ = this.service.getStudentByRegNo(this.regNoInput);
  }

  findBySchool() {
    this.schoolStudents$ = this.service.getStudentsBySchool(this.schoolInput).pipe(
      map(data => data.sort((a, b) => a.regNo - b.regNo))
    );
  }

  findCountBySchool() {
    this.schoolCount$ = this.service.getCountBySchool(this.schoolCountInput);
  }

  findCountByStandard() {
    this.standardCount$ = this.service.getCountByStandard(this.standardInput);
  }

  findByResult(pass: boolean) {
    this.resultLabel = pass ? 'Pass' : 'Fail';
    this.resultStudents$ = this.service.getStudentsByResult(pass).pipe(
      map(data => data.sort((a, b) => a.regNo - b.regNo))
    );
  }

  findStrength() {
    this.strength$ = this.service.getStrengthByGenderAndStandard(this.genderInput, this.strengthStandardInput);
  }
}
