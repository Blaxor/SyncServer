import { Component } from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import { GameListComponent } from "../game-list/game-list.component";

@Component({
    selector: 'app-grid',
    standalone: true,
    templateUrl: './grid.component.html',
    styleUrl: './grid.component.css',
    imports: [MatGridListModule, GameListComponent]
})
export class GridComponent {

}
