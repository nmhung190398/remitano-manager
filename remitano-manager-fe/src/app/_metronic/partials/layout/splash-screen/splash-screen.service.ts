import {ElementRef, Injectable} from '@angular/core';
import {animate, AnimationBuilder, style} from '@angular/animations';
import {BehaviorSubject} from "rxjs";

@Injectable({
    providedIn: 'root',
})
export class SplashScreenService {
    // Private properties
    private el: ElementRef;
    private stopped: boolean;

    showEvent: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
        false
    );


    /**
     * Service constructor
     *
     * @param animationBuilder: AnimationBuilder
     */
    constructor() {
    }

    /**
     * Init
     *
     * @param element: ElementRef
     */


    show(){
        this.showEvent.next(true);
    }

    /**
     * Hide
     */
    hide() {
        this.showEvent.next(false);
    }
}
