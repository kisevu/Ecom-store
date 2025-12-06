import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaConfig, FaIconLibrary, FaIconComponent } from '@fortawesome/angular-fontawesome';
import { fontAwesomeIcons } from './shared/font-awesome-icons';
import { Navbar } from "./layout/navbar/navbar";
import { Footer } from "./layout/footer/footer";
import { Oauth2Service } from './auth/auth/service/oauth/oauth2-service';
import { isPlatformBrowser, NgClass } from '@angular/common';
import { ToastService } from './shared/toast/service/toast-service';

@Component({
  imports: [RouterModule, Navbar, Footer, NgClass, FaIconComponent],
  selector: 'ecom-root',
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App  implements OnInit{
  protected title = 'sales-application';

  private faIconLibrary = inject(FaIconLibrary);
  private faConfig = inject(FaConfig);

  private oauth2Service = inject(Oauth2Service);

  private platformId = inject(PLATFORM_ID);
  toastService = inject(ToastService);

  constructor(){
    // checks whether we are on the browser side
    if(isPlatformBrowser(this.platformId)){
      this.oauth2Service.initAuthentication();
    }
    this.oauth2Service.connectedUserQuery = this.oauth2Service.fetch();
  }

  ngOnInit(): void {
    this.initFontAwesome();
    this.toastService.show("hello toast",'SUCCESS');
  }

  private initFontAwesome(){
    this.faConfig.defaultPrefix = 'far';
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }

}
