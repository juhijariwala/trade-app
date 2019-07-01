import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AgmCoreModule} from "@agm/core";

const routes: Routes = [];

@NgModule({
  imports: [RouterModule.forRoot(routes),
    AgmCoreModule.forRoot({
      apiKey: '***REMOVED***'
    })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
