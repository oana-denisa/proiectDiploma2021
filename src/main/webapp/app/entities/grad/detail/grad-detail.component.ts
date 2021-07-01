import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGrad } from '../grad.model';

@Component({
  selector: 'jhi-grad-detail',
  templateUrl: './grad-detail.component.html',
})
export class GradDetailComponent implements OnInit {
  grad: IGrad | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grad }) => {
      this.grad = grad;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
