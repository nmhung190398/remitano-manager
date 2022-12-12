import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {BreadcrumbItemModel} from '../_models/breadcrumb-item.model';
import {LayoutService} from '../../../../core';
import {SubheaderService} from '../_services/subheader.service';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {RemitanoAccountsService} from "../../../../../modules/service/remitano-accounts.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-subheader7',
    templateUrl: './subheader7.component.html',
})
export class Subheader7Component implements OnInit {
    subheaderCSSClasses = '';
    subheaderContainerCSSClasses = '';
    subheaderMobileToggle = false;
    subheaderDisplayDesc = false;
    subheaderDisplayDaterangepicker = false;
    title$: Observable<string>;
    breadcrumbs$: Observable<BreadcrumbItemModel[]>;
    description$: Observable<string>;
    isAutoRefresh = true;

    addAccountForm: FormGroup;

    constructor(
        private layout: LayoutService,
        private subheader: SubheaderService,
        private fb: FormBuilder,
        private accountService: RemitanoAccountsService,
        private modalService: NgbModal,
        private toastr: ToastrService
    ) {
        this.title$ = this.subheader.titleSubject.asObservable();
        this.breadcrumbs$ = this.subheader.breadCrumbsSubject.asObservable();
        this.description$ = this.subheader.descriptionSubject.asObservable();
        this.isAutoRefresh = localStorage.getItem("isAutoRefresh") == "true"
        this.subheader.isAutoRefresh.next(this.isAutoRefresh);
    }

    ngOnInit() {
        this.subheaderCSSClasses = this.layout.getStringCSSClasses('subheader');
        this.subheaderContainerCSSClasses = this.layout.getStringCSSClasses(
            'subheader_container'
        );
        this.subheaderMobileToggle = this.layout.getProp('subheader.mobileToggle');
        this.subheaderDisplayDesc = this.layout.getProp('subheader.displayDesc');
        this.subheaderDisplayDaterangepicker = this.layout.getProp(
            'subheader.displayDaterangepicker'
        );

        this.initForm();
    }

    onChangeIsAutoRefresh() {
        this.isAutoRefresh = !this.isAutoRefresh;
        localStorage.setItem("isAutoRefresh", String(this.isAutoRefresh));
        this.subheader.isAutoRefresh.next(this.isAutoRefresh);
    }

    submitAddAccount(model) {
        if (this.addAccountForm.invalid) {
            return
        }
        const data = this.addAccountForm.value;
        console.log(data);
        this.accountService.addAccount(data).subscribe(
            data => {
            },
            err => {
                console.log(err);
                this.toastr.error(err.error.message);
            },
            () => {
                this.subheader.accountRefreshEvent.next(true);
                model.close()
            }
        )
    }

    closeResult = ""

    open(content) {
        this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(
            (result) => {
                this.closeResult = `Closed with: ${result}`;
            },
            (reason) => {
                this.closeResult = `Dismissed ${reason}`;
            },
        );
    }


    refreshAll() {
        this.subheader.accountRefreshEvent.next(true);
    }


    initForm() {
        this.addAccountForm = this.fb.group({
            accessKey: [
                '',
                Validators.compose([
                    Validators.required,
                    Validators.minLength(3), // https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address
                ]),
            ],
            secretKey: [
                '',
                Validators.compose([
                    Validators.required,
                    Validators.minLength(3),
                ]),
            ],
        });
    }
}
