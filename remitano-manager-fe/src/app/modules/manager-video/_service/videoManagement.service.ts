import { catchError, finalize } from "rxjs/operators";
import { Router } from "@angular/router";
import { VideoManagementHttpService } from "./videoManagement-http.service";
import { Injectable, OnDestroy } from "@angular/core";
import { Observable, BehaviorSubject } from "rxjs";
export interface VideoDetail {
  description: string;
  sourceId: string;
  sourceType: string;
  title: string;
}
@Injectable({
  providedIn: "root",
})
export class VideoManagementService {
  isUploading: boolean = false;
  isSubmitting: boolean = false;
  constructor(
    private videoHttpService: VideoManagementHttpService,
    private router: Router
  ) {}

  checkVideoUrl(url: string): Observable<any> {
    this.isUploading = true;
    return this.videoHttpService
      .checkVideoUrl(url)
      .pipe(finalize(() => (this.isUploading = false)));
  }
  saveVideo(params: VideoDetail): Observable<any> {
    return this.videoHttpService.saveVideo(params);
  }
  listVideo(index?: string): Observable<any> {
    return this.videoHttpService.getBlockTime(index);
  }
}
