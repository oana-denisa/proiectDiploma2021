import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConcluziiConsultatie } from '../concluzii-consultatie.model';

@Component({
  selector: 'jhi-concluzii-consultatie-detail',
  templateUrl: './concluzii-consultatie-detail.component.html',
})
export class ConcluziiConsultatieDetailComponent implements OnInit {
  concluziiConsultatie: IConcluziiConsultatie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concluziiConsultatie }) => {
      this.concluziiConsultatie = concluziiConsultatie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
