import { isPlatformBrowser } from '@angular/common';
import { inject, Injectable,PLATFORM_ID } from '@angular/core';
import { AbstractSecurityStorage } from 'angular-auth-oidc-client';

@Injectable({
  providedIn: 'root',
})
export class SsrStorage implements AbstractSecurityStorage {

    private platformId = inject(PLATFORM_ID);

    /*
    As we know the application will be rendered twice given the two app configs, and main.ts ,
    main.server.ts
    When  it renders on the front end then we have the isPlatformBrowser
    */

  read(key: string): string | null {
     if(isPlatformBrowser(this.platformId)){
    return sessionStorage.getItem(key);
    } else {
      return null;
    }
  }

  write(key: string, value: string): void {
    if(isPlatformBrowser(this.platformId)){
      sessionStorage.setItem(key,value);
    }
  }

  remove(key: string): void {
     if(isPlatformBrowser(this.platformId)){
      sessionStorage.removeItem(key);
    }
  }

   clear(): void {
    if(isPlatformBrowser(this.platformId)){
      sessionStorage.clear();
    }
  }


}
