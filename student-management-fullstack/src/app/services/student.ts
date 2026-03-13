import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Student } from '../models/student';

@Injectable({
  providedIn: 'root',
})
export class StudentService {

  private base = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllStudents() {
    return this.http.get<Student[]>(`${this.base}/students`);
  }

  getStudentByRegNo(regNo: number) {
    return this.http.get<Student>(`${this.base}/students/${regNo}`);
  }

  insertStudent(s: Student) {
    return this.http.post(`${this.base}/students`, s, { responseType: 'text' });
  }

  updateStudent(regNo: number, s: Student) {
    return this.http.put(`${this.base}/students?regNo=${regNo}`, s, { responseType: 'text' });
  }

  deleteStudent(regNo: number) {
    return this.http.delete(`${this.base}/students?regNo=${regNo}`, { responseType: 'text' });
  }

  getStudentsBySchool(name: string) {
    return this.http.get<Student[]>(`${this.base}/students/school?name=${name}`);
  }

  getCountBySchool(name: string) {
    return this.http.get<number>(`${this.base}/students/school/count?name=${name}`);
  }

  getCountByStandard(standard: number) {
    return this.http.get<number>(`${this.base}/students/school/standard/count?class=${standard}`);
  }

  getStudentsByResult(pass: boolean) {
    return this.http.get<Student[]>(`${this.base}/students/result?pass=${pass}`);
  }

  getStrengthByGenderAndStandard(gender: string, standard: number) {
    return this.http.get<number>(`${this.base}/students/strength?gender=${gender}&standard=${standard}`);
  }
}
