import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitCountAnimationComponent } from './digit-count-animation.component';

describe('DigitCountAnimationComponent', () => {
  let component: DigitCountAnimationComponent;
  let fixture: ComponentFixture<DigitCountAnimationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DigitCountAnimationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DigitCountAnimationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
