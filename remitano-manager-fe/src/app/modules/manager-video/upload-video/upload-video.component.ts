import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VideoDetail, VideoManagementService,} from './../_service/videoManagement.service';
import {ChangeDetectorRef, Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {VideoManagementHttpService} from '../_service/videoManagement-http.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Component({
  selector: "app-upload-video",
  templateUrl: "./upload-video.component.html",
  styleUrls: ["./upload-video.component.scss"],
})
export class UploadVideoComponent implements OnInit, OnChanges {
  checkVideoUrlForm: FormGroup;
  createVideoForm: FormGroup;
  constructor(
    private videoService: VideoManagementService,
    private formBuilder: FormBuilder,
    private videoManagementHttpService : VideoManagementHttpService,
    private changeDetectorRefs: ChangeDetectorRef,
    private toastr: ToastrService,
    private router: Router
  ) {}


  ngOnInit(): void {
    console.log("ngOnInit");
    this.initForm();
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log("onChanges");

  }
  initForm() {
    this.checkVideoUrlForm = this.formBuilder.group({
      url: ["", Validators.compose([Validators.required])],
    });

    this.createVideoForm = this.formBuilder.group({
      sourceId: ["", Validators.compose([Validators.required])],
      sourceType: ["", Validators.compose([Validators.required])],
      title: ["", Validators.compose([Validators.required])],
      description: ["", Validators.compose([Validators.required])],
    });
  }
  resetValue() {
  }
  getParameterByName(name, url) {
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return "";
    return decodeURIComponent(results[2].replace(/\+/g, " "));
  }

  checkVideoUrl() {
    console.log('here');
    const url = this.checkVideoUrlForm.get("url").value;
    const videoId = this.getParameterByName("v",url);
    if(!videoId){
      this.toastr.error('url video error');
      return;
    }
    this.videoManagementHttpService
      .checkVideoUrl(videoId)
      .subscribe(
        (data) => {
          this.createVideoForm.patchValue({
            sourceId: data.sourceId,
            sourceType:  data.sourceType,
            title:  data.title,
            description: data.description
          });
          this.toastr.success('Tạo mới thành công');
        },
        error => {
          this.toastr.error('Có lỗi xảy ra'); // TODO => convert error sang str
        },
        () => {
          this.changeDetectorRefs.detectChanges();
        }
      );
  }
  onSubmit() {
    const data: VideoDetail = this.createVideoForm.value;

    this.videoService.saveVideo(data).subscribe(data => {

    });
  }
}
