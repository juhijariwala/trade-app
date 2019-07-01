import {Component} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { MouseEvent } from '@agm/core';
// import {google} from "@agm/core/services/google-maps-types";
declare var google: any;
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular8-springboot-websocket';
  message: String;
  name: string;
  webSocketEndPoint: string = 'http://localhost:8090/live-stream';
  topic: string = "/topic/trades";
  stompClient: any;
  geocoder: any;

  // google maps zoom level
  zoom: number = 3;

  // initial center position for the map
  lat: number = 51.678418;
  lng: number = 7.815982;

  clickedMarker(label: string, index: number) {
    console.log(`clicked the marker: ${label || index}`)
  }

  mapClicked($event: MouseEvent) {
    this.markers.push({
      lat: $event.coords.lat,
      lng: $event.coords.lng,
      draggable: true
    });
  }

  markerDragEnd(m: marker, $event: MouseEvent) {
    console.log('dragEnd', m, $event);
  }

  markers: marker[] = [
    {
      lat: 51.673858,
      lng: 7.815982,
      label: 'A',
      draggable: true
    },
    {
      lat: 51.373858,
      lng: 7.215982,
      label: 'B',
      draggable: false
    },
    {
      lat: 51.723858,
      lng: 7.895982,
      label: 'C',
      draggable: true
    }
  ]

// just an interface for type safety.


  ngOnInit() {
    this._connect();
    this.geocoder = new google.maps.Geocoder();
    this.getCountry("India");
  }

  ngOnDestroy() {
    this.disconnect();
  }

  _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function (frame) {
      console.log("Here ------");
      _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
        console.log("Connected")
        _this.onMessageReceived(sdkEvent);
      });
    }, this.errorCallBack);
  };

  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  onMessageReceived(message) {
    console.log("Message Recieved from Server :: " + message);
    this.message = JSON.stringify(message.body);
  }

  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect();
    }, 5000);
  }



  getCountry(country) {
    this.geocoder.geocode( { 'address': country }, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        console.log(results);
        // results[0].geometry.location
        // map.setCenter(results[0].geometry.location);
        // var marker = new google.maps.Marker({
        //   map: map,
        //   position: results[0].geometry.location
        // });
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });
  }

}

interface marker {
  lat: number;
  lng: number;
  label?: string;
  draggable: boolean;
}
