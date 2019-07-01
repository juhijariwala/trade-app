import {Component} from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

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
}
