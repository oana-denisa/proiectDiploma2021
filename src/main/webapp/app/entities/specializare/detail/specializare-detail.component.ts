import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpecializare } from '../specializare.model';

@Component({
  selector: 'jhi-specializare-detail',
  templateUrl: './specializare-detail.component.html',
})
export class SpecializareDetailComponent implements OnInit {
  specializare: ISpecializare | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specializare }) => {
      this.specializare = specializare;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
