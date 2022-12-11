import { Observable } from "rxjs";
import {
  VideoDetail,
  VideoManagementService,
} from "./../_service/videoManagement.service";
import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-list-video",
  templateUrl: "./list-video.component.html",
  styleUrls: ["./list-video.component.scss"],
})
export class ListVideoComponent implements OnInit {
  listVideos: VideoDetail[];
  preBlockNumber: string = "";
  nextBlockNumber: string = "";
  constructor(private videoService: VideoManagementService) {}

  ngOnInit(): void {
    this.getListVideo();
    const tag = document.createElement("script");

    tag.src = "https://www.youtube.com/iframe_api";
    document.body.appendChild(tag);
  }
  getListVideo() {
    this.videoService.listVideo(this.preBlockNumber).subscribe((data) => {
      console.log(data);
      this.listVideos = data.videos;
      this.preBlockNumber = data.preEndTime;
      this.nextBlockNumber = data.nextEndTime;
      console.log(this.listVideos, this.preBlockNumber);
    });
  }
}
