import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FaConfig, FaIconLibrary, FaIconComponent } from '@fortawesome/angular-fontawesome';
import { fontAwesomeIcons } from './shared/font-awesome-icons';

@Component({
  imports: [RouterModule, FaIconComponent],
  selector: 'ecom-root',
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App  implements OnInit{
  protected title = 'sales-application';

  private faIconLibrary = inject(FaIconLibrary);
  private faConfig = inject(FaConfig);


  ngOnInit(): void {
    this.initFontAwesome();
  }

  private initFontAwesome(){
    this.faConfig.defaultPrefix = 'far';
    this.faIconLibrary.addIcons(...fontAwesomeIcons);
  }

}
