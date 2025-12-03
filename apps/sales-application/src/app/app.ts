import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaConfig, FaIconLibrary} from '@fortawesome/angular-fontawesome';
import { fontAwesomeIcons } from './shared/font-awesome-icons';
import { Navbar } from "./layout/navbar/navbar";
import { Footer } from "./layout/footer/footer";
import { Oauth2Service } from './auth/auth/service/oauth/oauth2-service';
import { isPlatformBrowser } from '@angular/common';

@Component({
  imports: [RouterModule, Navbar, Footer],
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

  constructor(){
    // checks whether we are on the browser side
    if(isPlatformBrowser(this.platformId)){
      this.oauth2Service.initAuthentication();
    }
    this.oauth2Service.connectedUserQuery = this.oauth2Service.fetch();
  }

  ngOnInit(): void {
    this.initFontAwesome();
    
  }

  private initFontAwesome(){
    this.faConfig.defaultPrefix = 'far';
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }

}
