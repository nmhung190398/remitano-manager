import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {AuthService} from "../auth";

const YOUTUBE_API = `${environment.YOUTUBE_API}`;
const YOUTUBE_API_KEY = `${environment.YOUTUBE_API_KEY}`;
const BACKEND_API = `${environment.apiUrl}`;

@Injectable({
    providedIn: "root",
})
export class RemitanoAccountsService {
    constructor(private http: HttpClient, private authService: AuthService) {
    }

    disableOffer(accountId: string, offerId: string): Observable<any> {
        const token = this.authService.getAuthFromLocalStorage().accessToken;
        const httpHeaders = new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });

        return this.http.put<any>(`${BACKEND_API}/remitano-accounts/${accountId}/disable-offer/${offerId}`, {}, {
            headers: httpHeaders,
        });
    }

    enableOffer(accountId: string, offerId: string, body: any): Observable<any> {
        const token = this.authService.getAuthFromLocalStorage().accessToken;
        const httpHeaders = new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });

        return this.http.put<any>(`${BACKEND_API}/remitano-accounts/${accountId}/enable-offer/${offerId}`, body, {
            headers: httpHeaders,
        });
    }

    addAccount(params: any): Observable<any> {
        const token = this.authService.getAuthFromLocalStorage().accessToken;
        const httpHeaders = new HttpHeaders({
            Authorization: `Bearer ${token}`,
        });
        console.log({params});

        return this.http.post<any>(`${BACKEND_API}/remitano-accounts`, params, {
            headers: httpHeaders,
        });
    }

    getAccounts(): Observable<any> {
        const token = this.authService.getAuthFromLocalStorage()?.accessToken;
        let headers = {
            Authorization: `Bearer ${token}`,
        }
        const httpHeaders = new HttpHeaders(headers);

        return this.http.get<any>(`${BACKEND_API}/remitano-accounts`, {
            headers: httpHeaders
        });
    }


    getDetailAccount(id: string): Observable<any> {
        const token = this.authService.getAuthFromLocalStorage()?.accessToken;
        let headers = {
            Authorization: `Bearer ${token}`,
        }
        const httpHeaders = new HttpHeaders(headers);

        return this.http.get<any>(`${BACKEND_API}/remitano-accounts/${id}`, {
            headers: httpHeaders
        });
    }
}
