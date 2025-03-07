import { Component } from '@angular/core';
import {AnunciosComponent} from "../components/anuncios/anuncios.component";
import {FormsModule} from "@angular/forms";
import {AnuncioService} from "../services/anuncio.service";
import {HttpClientModule} from "@angular/common/http";

@Component({
  selector: 'app-contenido',
  standalone: true,
  templateUrl: './contenido.component.html',
  imports: [
    AnunciosComponent,
    FormsModule,
    HttpClientModule
  ],
  styleUrl: './contenido.component.scss',
  providers:[AnuncioService]
})
export class ContenidoComponent {
  filtroSeleccionado: string = 'todos'; // Valor inicial


  onFiltroChange(): void {
    console.log(`Filtro cambiado a: ${this.filtroSeleccionado}`);
  }
}
