import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'specializare',
        data: { pageTitle: 'consultatiiApp.specializare.home.title' },
        loadChildren: () => import('./specializare/specializare.module').then(m => m.SpecializareModule),
      },
      {
        path: 'grad',
        data: { pageTitle: 'consultatiiApp.grad.home.title' },
        loadChildren: () => import('./grad/grad.module').then(m => m.GradModule),
      },
      {
        path: 'medic',
        data: { pageTitle: 'consultatiiApp.medic.home.title' },
        loadChildren: () => import('./medic/medic.module').then(m => m.MedicModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'consultatiiApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'consultatie',
        data: { pageTitle: 'consultatiiApp.consultatie.home.title' },
        loadChildren: () => import('./consultatie/consultatie.module').then(m => m.ConsultatieModule),
      },
      {
        path: 'concluzii-consultatie',
        data: { pageTitle: 'consultatiiApp.concluziiConsultatie.home.title' },
        loadChildren: () => import('./concluzii-consultatie/concluzii-consultatie.module').then(m => m.ConcluziiConsultatieModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
