import { AuthService } from "./../../auth/_services/auth.service";
import { environment } from "./../../../../environments/environment";
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { VideoDetail } from "./videoManagement.service";
const YOUTUBE_API = `${environment.YOUTUBE_API}`;
const YOUTUBE_API_KEY = `${environment.YOUTUBE_API_KEY}`;
const BACKEND_API = `${environment.apiUrl}`;
@Injectable({
  providedIn: "root",
})
export class VideoManagementHttpService {
  constructor(private http: HttpClient, private authService: AuthService) {}
  //upload video
  checkVideoUrl(url: string): Observable<any> {
    const token = this.authService.getAuthFromLocalStorage().accessToken;
    const httpHeaders = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    return this.http.get<any>(`${BACKEND_API}/videos/get-info-youtube`, {
      params: {
        id: url,
      },
      headers: httpHeaders,
    });
  }
  saveVideo(params: VideoDetail): Observable<any> {
    const token = this.authService.getAuthFromLocalStorage().accessToken;
    const httpHeaders = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    console.log({ params });

    return this.http.post<any>(`${BACKEND_API}/videos`, params, {
      headers: httpHeaders,
    });
  }

  getBlockTime(et?: string): Observable<any> {
    const token = this.authService.getAuthFromLocalStorage()?.accessToken;
    let headers = {}
    if(token){
      headers = {
        Authorization: `Bearer ${token}`,
      }
    }
    const httpHeaders = new HttpHeaders(headers);

    return this.http.get<any>(`${BACKEND_API}/videos/public/getByBlockTime`, {
      headers: httpHeaders,
      params: {
        et: et,
      },
    });
  }

  emojiVideo(payload: any): Observable<any> {
    const token = this.authService.getAuthFromLocalStorage().accessToken;
    const httpHeaders = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.post<any>(
      `${BACKEND_API}/videos/emoji-video`,
      payload,
      {
        headers: httpHeaders,
      });
  }
}
