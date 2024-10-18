import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { GridComponent } from './root-grid/grid.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [GridComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  title = 'Sync Game';
  title_style = 'text-align:center; color:green;'
}
