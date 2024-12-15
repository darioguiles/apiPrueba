import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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
  @Output() crearAnuncio = new EventEmitter<void>();

  anuncios: any[] = [];
  totalAnuncios: number = 0;
  totalPages: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;
  constructor(private anuncioService: AnuncioService) {}

  ngOnInit(): void {
    this.loadAnuncios();
  }

  ngOnChanges(): void {
    this.loadAnuncios(); // Recargar anuncios cuando cambien los filtros
  }

  loadAnuncios() {
    this.anuncioService.getAnuncios(this.soloTrabajadores, this.soloEmpresas, this.pageSize, this.currentPage)
      .subscribe(data => {
        this.anuncios = data.anuncioTrabajadores; // Mapear la respuesta a la lista de anuncios
        this.totalAnuncios = data.totalItems;
        this.totalPages = data.totalPages;
      });
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
