import {Component, EventEmitter, Input, OnInit, Output, SimpleChanges} from '@angular/core';
import {AnuncioService} from "../../services/anuncio.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {NgForOf, NgIf} from "@angular/common";
import {ContenidoComponent} from "../../contenido/contenido.component";
@Component({
  selector: 'app-anuncios',
  standalone: true,
  imports: [
    MatPaginator,
    NgForOf,
    NgIf
  ],
  templateUrl: './anuncios.component.html',
  styleUrl: './anuncios.component.scss',
  providers:[AnuncioService]
})
export class AnunciosComponent implements OnInit{
  @Input() soloTrabajadores: boolean = false;
  @Input() soloEmpresas: boolean = false;

  anuncios: any[] = [];
  totalAnuncios: number = 0;
  totalPages: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;
  constructor(private anuncioService: AnuncioService) {}

  ngOnInit(): void {
    this.loadAnuncios();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['soloTrabajadores'] || changes['soloEmpresas']) {
      this.loadAnuncios();
    }
  }

  loadAnuncios() {
    // Si solo se seleccionan anuncios de trabajadores
    if (this.soloTrabajadores) {
      this.anuncioService.getAnunciosTrabajadores(this.pageSize, this.currentPage).subscribe(data => {
        this.anuncios = data.anuncioTrabajadores || []; // Cambié de "anuncioTrabajador" a "anuncioTrabajadores"
        this.totalAnuncios = data.totalItems || 0;
        this.totalPages = data.totalPages || 0;
      }, error => {
        console.error('Error al cargar anuncios de trabajadores:', error);
        this.anuncios = [];
      });
    }
    // Si solo se seleccionan anuncios de empresas
    else if (this.soloEmpresas) {
      this.anuncioService.getAnunciosEmpresas(this.pageSize, this.currentPage).subscribe(data => {
        this.anuncios = data.anuncioEmpresa || [];
        this.totalAnuncios = data.totalItems || 0;
        this.totalPages = data.totalPages || 0;
      }, error => {
        console.error('Error al cargar anuncios de empresas:', error);
        this.anuncios = [];
      });
    }
    // Si ninguno está marcado, cargar ambos tipos de anuncios
    else {
      this.anuncioService.getAnunciosTrabajadores(this.pageSize, this.currentPage).subscribe(data => {
        this.anuncios = data.anuncioTrabajadores || []; // Cambié de "anuncioTrabajador" a "anuncioTrabajadores"
        this.totalAnuncios = data.totalItems || 0;
        this.totalPages = data.totalPages || 0;
      }, error => {
        console.error('Error al cargar anuncios de trabajadores:', error);
        this.anuncios = [];
      });

      this.anuncioService.getAnunciosEmpresas(this.pageSize, this.currentPage).subscribe(data => {
        this.anuncios = [...this.anuncios, ...(data.anuncioEmpresa || [])]; // Concatenar anuncios de empresas
        this.totalAnuncios = this.anuncios.length;
        this.totalPages = Math.ceil(this.totalAnuncios / this.pageSize); // Calcular el total de páginas
      }, error => {
        console.error('Error al cargar anuncios de empresas:', error);
      });
    }
  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex;  // Obtén la página actual
    this.pageSize = event.pageSize;      // Obtén el tamaño de la página
    this.loadAnuncios();                 // Recargar los anuncios con la nueva página
  }
  onCreateAnuncio() {
    console.log("Se ha intentado crear un anuncio");
  }
}
