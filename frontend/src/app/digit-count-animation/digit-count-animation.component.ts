import {AfterViewInit, Component, ElementRef, Input, OnChanges, SimpleChanges, ViewChild} from '@angular/core';

@Component({
  selector: 'app-digit-count-animation',
  templateUrl: './digit-count-animation.component.html',
  styleUrls: ['./digit-count-animation.component.scss']
})
export class DigitCountAnimationComponent implements AfterViewInit, OnChanges {


  private readonly duration: number = 1000;
  private readonly steps: number = 10;
  private readonly DIGIT_TYPE = 'number';

  @Input() digit: number;
  @ViewChild('animatedDigit') animatedDigit: ElementRef;

  animateCount(): void {
    if (typeof this.digit === this.DIGIT_TYPE) {
      this.counterFunc(this.digit, this.duration, this.animatedDigit);
    }
  }

  counterFunc(endValue, durationMs, element): void {
    const stepCount = Math.abs(durationMs / this.steps);
    const valueIncrement = (endValue - 0) / stepCount;
    const sinValueIncrement = Math.PI / stepCount;

    let currentValue = 0;
    let currentSinValue = 0;

    function step(): void {
      currentSinValue += sinValueIncrement;
      currentValue += valueIncrement * Math.sin(currentSinValue) ** 2 * 2;
      if (element) {
        element.nativeElement.textContent = Math.abs(Math.floor(currentValue));
      }
      if (currentSinValue < Math.PI) {
        window.requestAnimationFrame(step);
      }
    }
    step();
  }

  ngAfterViewInit(): void {
    if (this.digit) {
      this.animateCount();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.digit) {
      this.animateCount();
    }
  }
}

