import {Component, OnInit} from '@angular/core';
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
export class ContenidoComponent implements OnInit{
  filtroSeleccionado: string = 'todos'; // Valor inicial
  filtros = {
    descripcion: '',
    nombre: ''
  };
  filtroTrigger = 0;  // Este será nuestro "disparador" manual

// Emitir filtros al componente hijo <app-anuncios>
  aplicarFiltros(): void {
    this.filtroTrigger++; // Esto ya reactiva el hijo
  }
  resetFiltros(): void {
    this.filtros = {
      descripcion: '',
      nombre: ''
    };
    this.aplicarFiltros();
  }
  onFiltroChange(): void {
    console.log(`Filtro cambiado a: ${this.filtroSeleccionado}`);
  }

  ngOnInit(): void {
    this.aplicarFiltros();
  }

}
