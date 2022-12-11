import {Component, OnDestroy, OnInit} from '@angular/core';
import {VideoManagementHttpService} from '../manager-video/_service/videoManagement-http.service';
import {ChangeDetectorRef} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {RemitanoAccountsService} from "../service/remitano-accounts.service";
import {SubheaderService} from "../../_metronic/partials/layout";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {of} from "rxjs";
import {SplashScreenService} from "../../_metronic/partials/layout/splash-screen/splash-screen.service";

enum EMOTION_TYPE {
    LIKE = 'LIKE',
    DISLIKE = 'DISLIKE',
    LOVE = 'LOVE',
    ANGRY = 'ANGRY',
    WOW = 'WOW',
    LOL = 'LOL',
}

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

    videos = [];
    hiddenVideo = true;
    private preEndTime: null
    private currentBlock = null;

    accounts = [];

    accountDetails = {}

    listEmotion = [];
    isLoading = false;
    intervalRestartAccount = null;

    enableOfferForm: FormGroup

    constructor(
        private remitanoAccountsService: RemitanoAccountsService,
        private videoManagementHttpService: VideoManagementHttpService,
        private changeDetectorRefs: ChangeDetectorRef,
        private toastr: ToastrService,
        private subheader: SubheaderService,
        private fb: FormBuilder,
        private modalService: NgbModal,
        private splashScreenService: SplashScreenService,
    ) {
    }


    ngOnInit(): void {
        this.listEmotion = Object.entries(EMOTION_TYPE).map(([key, val]) => ({key: key, value: val}));
        this.subheader.accountRefreshEvent.subscribe(data => {
            if (data) {
                this.accounts = []
                this.getAccounts(true)
            }
        })

        this.getAccounts(true);
        this.intervalRestartAccount = setInterval(() => {
            console.log("refreshData")
            this.accounts.forEach(x => {
                this.getDetailAccount(x['id'])
            })
        }, 20000)
        this.initForm();
    }


    initForm() {
        this.enableOfferForm = this.fb.group({
            offerId: [null],
            accountId: [null],
            totalAmount: [
                0,
                Validators.compose([
                    Validators.required // https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address
                ]),
            ],
            maxAmount: [
                0,
                Validators.compose([
                    Validators.required,
                ]),
            ],
        });
    }

    disableOffer(accountId, offer) {
        this.splashScreenService.show();
        this.remitanoAccountsService.disableOffer(accountId, offer['id']).subscribe(
            data => {
                this.getDetailAccount(accountId, true);
            },
            err => {
                this.splashScreenService.hide();
                this.toastr.error(err.error.message);
            },
            () => {
                this.changeDetectorRefs.detectChanges();
            }
        )
    }

    enableOffer(accountId, offer, content) {
        if (offer['offer_type'] == 'sell') {
            this.splashScreenService.show()
            this.remitanoAccountsService.enableOffer(accountId, offer['id'], {}).subscribe(
                data => {
                    this.getDetailAccount(accountId, true);
                },
                err => {
                    this.splashScreenService.hide();
                    this.toastr.error(err.error.message);
                },
                () => {
                    this.changeDetectorRefs.detectChanges();
                }
            )

            return
        }

        console.log(offer);
        this.enableOfferForm.patchValue({
            offerId: offer['id'],
            accountId: accountId,
            totalAmount: offer['total_amount'],
            maxAmount: offer['max_amount']
        })

        this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(
            (result) => {
            },
            (reason) => {
            },
        );
    }

    submitEnableOffer(modal) {
        const data = this.enableOfferForm.value;
        this.splashScreenService.show()
        const accountId = data.accountId;
        const offerId = data.offerId;
        this.remitanoAccountsService.enableOffer(accountId, offerId, data).subscribe(
            data => {
                this.getDetailAccount(accountId, true);
            },
            err => {
                this.splashScreenService.hide();
                this.toastr.error(err.error.message);
            },
            () => {
                modal.close()
            }
        )

        return
    }

    getAccounts(isLoading = false) {
        this.splashScreenService.showEvent.next(isLoading);
        this.remitanoAccountsService.getAccounts().subscribe(
            data => {
                this.accounts = data;
                this.accountDetails = {}
                this.accounts.forEach(x => {
                    this.getDetailAccount(x['id'])
                })
            },
            err => {
                this.toastr.error(err);
            },
            () => {
                this.splashScreenService.hide();
                this.changeDetectorRefs.detectChanges();
            }
        )
    }

    public getDetailAccount(id: string, isLoading = false) {
        this.splashScreenService.showEvent.next(isLoading);
        this.remitanoAccountsService.getDetailAccount(id).subscribe(
            data => {
                this.accountDetails[id] = {
                    data: data
                };
            },
            err => {
                this.toastr.error(err);
                this.accountDetails[id] = {
                    error: err
                }
            },
            () => {
                this.splashScreenService.hide();
                this.changeDetectorRefs.detectChanges();
            }
        )
    }


    emojiVideo(video, emojiType) {
        const payload = {
            emojiType: emojiType,
            videoId: video?.id
        }
        this.videoManagementHttpService.emojiVideo(payload).subscribe(
            data => {

                if (video.emojiCount == null) {
                    video.emojiCount = {}
                }

                if (video.emojiCount && video.currentUserEmoji && video.emojiCount[video.currentUserEmoji] && video.emojiCount[video.currentUserEmoji] > 0) {
                    video.emojiCount[video.currentUserEmoji] = video.emojiCount[video.currentUserEmoji] - 1;
                }

                video.currentUserEmoji = emojiType;
                video.emojiCount[emojiType] = (video.emojiCount[emojiType] || 0) + 1;
            },
            error => {

            },
            () => {
                this.changeDetectorRefs.detectChanges();
            }
        )
    }

    ngOnDestroy(): void {
        clearInterval(this.intervalRestartAccount);
    }

}
