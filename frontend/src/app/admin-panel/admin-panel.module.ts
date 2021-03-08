import { NgModule } from '@angular/core';

import { AdminPanelRoutingModule } from './admin-panel-routing.module';
import { AdminPanelComponent } from './admin-panel.component';
import { ListStudentsComponent } from './list-students/list-students.component';
import { AddStudentComponent } from './add-student/add-student.component';
import { SharedModule } from '../shared.module';


@NgModule({
  declarations: [
    AdminPanelComponent,
    ListStudentsComponent,
    AddStudentComponent,
    AddStudentComponent,
    ListStudentsComponent
  ],
  imports: [
    AdminPanelRoutingModule,
    SharedModule
  ]
})
export class AdminPanelModule { }
