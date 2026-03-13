import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Student } from '../models/student';
import { StudentService } from '../services/student';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {

  students$!: Observable<Student[]>;
  message = '';
  showForm = false;
  isEditing = false;

  formData: Student = {
    regNo: 0, rollNo: 0, name: '',
    standard: 0, school: '', gender: '', percentage: 0
  };

  constructor(private service: StudentService) {}

  ngOnInit() {
    this.loadStudents();
  }

  loadStudents() {
    this.students$ = this.service.getAllStudents().pipe(
      map(data => data.sort((a, b) => a.regNo - b.regNo))
    );
  }

  openAddForm() {
    this.isEditing = false;
    this.formData = { regNo: 0, rollNo: 0, name: '', standard: 0, school: '', gender: '', percentage: 0 };
    this.showForm = true;
    this.message = '';
  }

  openEditForm(s: Student) {
    this.isEditing = true;
    this.formData = { ...s };
    this.showForm = true;
    this.message = '';
  }

  cancelForm() {
    this.showForm = false;
    this.message = '';
  }

  submitForm() {
    if (this.isEditing) {
      this.service.updateStudent(this.formData.regNo, this.formData).subscribe(res => {
        this.message = res;
        this.showForm = false;
        this.loadStudents();
      });
    } else {
      this.service.insertStudent(this.formData).subscribe(res => {
        this.message = res;
        this.showForm = false;
        this.loadStudents();
      });
    }
  }

  deleteStudent(regNo: number) {
    if (confirm('Are you sure you want to delete this student?')) {
      this.service.deleteStudent(regNo).subscribe(res => {
        this.message = res;
        this.loadStudents();
      });
    }
  }
}
