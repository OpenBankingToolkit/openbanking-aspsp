import { Component, OnInit, ChangeDetectionStrategy, Output, EventEmitter, ElementRef } from '@angular/core';

@Component({
  selector: 'app-consent-steps',
  template: `
    <ng-content></ng-content>
  `,
  styles: [],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ConsentStepsComponent implements OnInit {
  currentStep = 0;
  @Output() stepChange = new EventEmitter<number>();

  constructor(private el: ElementRef) {}

  ngOnInit() {
    this.stepChange.emit(this.currentStep);
  }

  public next() {
    this.currentStep++;
    this.stepChange.emit(this.currentStep);
    this.el.nativeElement.parentNode.scrollIntoView({ behavior: 'smooth' });
  }

  public previous() {
    if (this.currentStep === 0) return;
    this.currentStep--;
    this.stepChange.emit(this.currentStep);
    this.el.nativeElement.parentNode.scrollIntoView({ behavior: 'smooth' });
  }
}
