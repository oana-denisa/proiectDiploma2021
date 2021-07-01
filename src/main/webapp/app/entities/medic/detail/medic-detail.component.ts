import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedic } from '../medic.model';

@Component({
  selector: 'jhi-medic-detail',
  templateUrl: './medic-detail.component.html',
})
export class MedicDetailComponent implements OnInit {
  medic: IMedic | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medic }) => {
      this.medic = medic;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
