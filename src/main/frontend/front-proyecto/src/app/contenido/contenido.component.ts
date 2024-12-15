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

  soloTrabajadores: boolean = false;
  soloEmpresas: boolean = false;

  constructor() {}

  onCheckboxChange() {
    // Si ambos checkboxes están desmarcados, mostrar ambos tipos de anuncios
    if (!this.soloTrabajadores && !this.soloEmpresas) {
      console.log('Mostrando todos los anuncios.');
    }
    // Si solo se marca "Trabajadores"
    else if (this.soloTrabajadores && !this.soloEmpresas) {
      console.log('Mostrando solo anuncios de Trabajadores.');
    }
    // Si solo se marca "Empresas"
    else if (!this.soloTrabajadores && this.soloEmpresas) {
      console.log('Mostrando solo anuncios de Empresas.');
    }
    // Si ambos están marcados
    else if (this.soloTrabajadores && this.soloEmpresas) {
      console.log('Mostrando todos los anuncios.');
    }
  }

  onCreateAnuncio() {
    // Redirigir o abrir el formulario para crear un anuncio
    console.log('Crear nuevo anuncio');
  }
}
