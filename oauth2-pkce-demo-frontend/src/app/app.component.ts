import { Component, OnDestroy } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Subscription } from 'rxjs';
import { AppService } from './app.service';
import { authConfig } from './auth.config';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
  title = 'frontend';
  text = '';
  subscription: Subscription;

  constructor( private authService: OAuthService, private appService: AppService) {
    this.configure();
    this.subscription = this.appService.hello().subscribe(text => {
      this.text = text;
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private configure() {
    this.authService.configure(authConfig);
    this.authService.loadDiscoveryDocumentAndTryLogin();
  }


  public login() {
    this.authService.initCodeFlow();
  }

  public logout() {
    this.authService.logOut();
  }
}
