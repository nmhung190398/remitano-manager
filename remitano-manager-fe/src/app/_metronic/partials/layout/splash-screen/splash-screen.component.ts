import {Component, OnInit, ViewChild, ElementRef, ChangeDetectorRef} from '@angular/core';
import { SplashScreenService } from './splash-screen.service';

@Component({
  selector: 'app-splash-screen',
  templateUrl: './splash-screen.component.html',
  styleUrls: ['./splash-screen.component.scss'],
})
export class SplashScreenComponent implements OnInit {

  constructor(
    private splashScreenService: SplashScreenService,
    private changeDetectorRefs: ChangeDetectorRef,
  ) {}
  isShow = true;
  ngOnInit(): void {
    this.splashScreenService.showEvent.subscribe(data => {
      this.isShow = data
      this.changeDetectorRefs.detectChanges();
    })
  }
}
