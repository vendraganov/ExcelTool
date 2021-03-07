import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AdminPanelComponent} from './admin-panel.component';
import {AddStudentComponent} from './add-student/add-student.component';
import {ListStudentsComponent} from './list-students/list-students.component';

const routes: Routes = [
  {path: '', component: AdminPanelComponent},
  {path: 'add-student', component: AddStudentComponent},
  {path: 'list-students', component: ListStudentsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminPanelRoutingModule { }
