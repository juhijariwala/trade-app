import {Component} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MouseEvent} from '@agm/core';
import {countries} from "./countries";
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Trade App';
  message: String;
  name: string;
  webSocketEndPoint: string = environment.tradeProcessorUrl;
  topic: string = "/topic/trades";
  stompClient: any;

  // google maps zoom level
  zoom: number = 2;

  // initial center position for the map
  lat: number = 51.678418;
  lng: number = 7.815982;

  clickedMarker(label: string, index: number) {
    console.log(`clicked the marker: ${label || index}`)

  }

  markerDragEnd(m: marker, $event: MouseEvent) {
    console.log('dragEnd', m, $event);
  }

  markers: marker[] = [];
  coutnrywiseStats: Map<string, countryWiseStat> = new Map();

  ngOnInit() {
    this._connect();
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
    const body = JSON.parse(message.body);
    console.log();
    Object.keys(body).forEach(key => {
      const val = body[key];
      const code = val.originatingCountry.replace("en_", "");
      let country = countries.find(country => country.countryCode === code);
      let marker = <marker>{
        lat: country.latitude,
        lng: country.longitude,
        label: country.name,
        draggable: false,
        info: val.totalTransactions
      };
      let countryWiseStat = <countryWiseStat>{
        currencySymbol: val.currencyPair,
        country: country.name,
        totalAmountSent: val.totalAmountSent,
        totalAmountReceived: val.totalAmountReceived,
        totalTransactions: val.totalTransactions,
        minRate: val.minExchangeRate,
        maxRate: val.maxExchangeRate,
      };
      this.markers.push(marker)
      this.coutnrywiseStats.set(country.name, countryWiseStat)
    });

    this.message = JSON.stringify(body);
  }

  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect();
    }, 5000);
  }

}

interface marker {
  lat: number;
  lng: number;
  label?: string;
  draggable: boolean;
  info: string
}

interface countryWiseStat {
  country: string;
  currencySymbol: string;
  totalAmountSent: string;
  totalAmountReceived: string;
  totalTransactions: string;
  minRate: number;
  maxRate: number;
}
