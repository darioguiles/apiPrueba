import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {AnuncioService} from "../../services/anuncio.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {NgForOf, NgIf} from "@angular/common";
import {ContenidoComponent} from "../../contenido/contenido.component";
import Swal from "sweetalert2";
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
export class AnunciosComponent implements OnInit, OnChanges{
  @Input() filtroSeleccionado: string = 'todos'; // Recibimos el filtro desde el padre
  @Input() usuario: any = null; // Usuario autenticado

  anuncios: any[] = [];
  totalAnuncios: number = 0;
  //totalPages: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;

  constructor(private anuncioService: AnuncioService) {}

  ngOnInit(): void {
    this.loadAnuncios();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['filtroSeleccionado']) {
      this.anuncios = []; // Limpiar lista al cambiar filtro
      this.loadAnuncios();
    }
  }

  loadAnuncios(): void {
    switch (this.filtroSeleccionado) {
      case 'empresa':
        this.loadAnunciosEmpresa();
        break;
      case 'trabajador':
        this.loadAnunciosTrabajador();
        break;
      default:
        this.loadAnunciosTodos();
        break;
    }
  }

  loadAnunciosEmpresa(): void {
    this.anuncioService.getAnunciosEmpresas(this.pageSize, this.currentPage).subscribe({
      next: (data) => {
        this.anuncios = data.anuncioEmpresa || [];
        this.totalAnuncios = data.totalItems || 0;
      },
      error: (error) => console.error('Error al cargar anuncios de empresas:', error)
    });
  }

  loadAnunciosTrabajador(): void {
    this.anuncioService.getAnunciosTrabajadores(this.pageSize, this.currentPage).subscribe({
      next: (data) => {
        this.anuncios = data.anuncioTrabajadores || [];
        this.totalAnuncios = data.totalItems || 0;
      },
      error: (error) => console.error('Error al cargar anuncios de trabajadores:', error)
    });
  }

  loadAnunciosTodos(): void {
    Promise.all([
      this.anuncioService.getAnunciosEmpresas(this.pageSize, this.currentPage).toPromise(),
      this.anuncioService.getAnunciosTrabajadores(this.pageSize, this.currentPage).toPromise()
    ])
      .then(([empresas, trabajadores]) => {
        const empresaAnuncios = empresas?.anuncioEmpresa || [];
        const trabajadorAnuncios = trabajadores?.anuncioTrabajadores || [];
        this.anuncios = [...empresaAnuncios, ...trabajadorAnuncios].sort((a, b) => b.id - a.id);
        this.totalAnuncios = this.anuncios.length;
      })
      .catch((error) => console.error('Error al cargar anuncios combinados:', error));
  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadAnuncios();
  }

  esPropietario(anuncio: any): boolean {
    if (!this.usuario) return false;
    if (this.usuario.trabajador && anuncio.trabajador) {
      return this.usuario.trabajador.id_trabajador === anuncio.trabajador.id_trabajador;
    } else if (this.usuario.empresa && anuncio.empresa) {
      return this.usuario.empresa.idEmpresa === anuncio.empresa.id_empresa;
    }
    return false;
  }

  obtenerFechaActualUTCmas1(): string {
    const fechaActual = new Date();

    // Convertir a UTC+1 (España)
    fechaActual.setHours(fechaActual.getHours() + 1);

    // Formatear en "yyyy-MM-dd"
    return fechaActual.toISOString().split("T")[0];
  }
  crearAnuncio(): void {
    const usuarioData = localStorage.getItem("usuario");

    // Verificar si el usuario está logueado
    if (!usuarioData) {
      Swal.fire({
        title: "Inicia sesión",
        text: "Debes iniciar sesión para crear un anuncio.",
        icon: "warning",
        confirmButtonText: "Ir al login"
      }).then(() => {
        window.location.href = "/login"; // Redirigir al login
      });
      return;
    }

    const usuario = JSON.parse(usuarioData);

    let formFields = [
      {
        title: 'Descripción',
        input: 'text',
        inputPlaceholder: 'Describe tu anuncio...',
        inputAttributes: {
          required: 'true'
        }
      }
    ];

    // Si el usuario es empresa, añadimos los campos extra
    if (usuario.empresa!=undefined) {
      formFields.push(
        {
          title: 'Fecha de inicio',
          input: 'date',
          inputPlaceholder: 'Fecha de Inicio',
          inputAttributes: {
            required: 'true'
          }
        },
        {
          title: 'Fecha de fin (Opcional)',
          input: 'date',
          inputPlaceholder: 'Fecha de fin (Opcional)',
          inputAttributes: {
            required: 'false'
          }
        }
      );
    }

    Swal.fire({
      title: 'Crear Anuncio',
      html: `
        <input id="descripcion" class="swal2-input" placeholder="Descripción del anuncio">
        ${usuario.empresa ? `
          <input id="fechaInicio" type="date" class="swal2-input" placeholder="Fecha de inicio">
          <input id="fechaFin" type="date" class="swal2-input" placeholder="Fecha de fin (Opcional)">
        ` : ''}
      `,
      showCancelButton: true,
      confirmButtonText: 'Crear',
      cancelButtonText: 'Cancelar',
      preConfirm: () => {
        const descripcion = (document.getElementById('descripcion') as HTMLInputElement)?.value;
        const fechaInicio = this.usuario.empresa ? (document.getElementById('fechaInicio') as HTMLInputElement)?.value : null;
        let fechaFin = this.usuario.empresa ? (document.getElementById('fechaFin') as HTMLInputElement)?.value : null;

        if (!descripcion) {
          Swal.showValidationMessage('La descripción es obligatoria.');
          return false;
        }

        if (this.usuario.empresa && !fechaInicio) {
          Swal.showValidationMessage('La fecha de inicio es obligatoria.');
          return false;
        }

        // Si no se introduce fecha de fin, se deja como `null`
        fechaFin = fechaFin || null;

        return { descripcion, fechaInicio, fechaFin };
      }
    }).then((result) => {
      if (result.isConfirmed) {

        let nuevoAnuncio: any = {
          descripcion: result.value.descripcion
        };

        if (this.usuario.trabajador) {
          nuevoAnuncio.fecha_publicacion = this.obtenerFechaActualUTCmas1();
          nuevoAnuncio.trabajador = { id_trabajador: this.usuario.trabajador.id_trabajador };
        } else if (this.usuario.empresa) {
          nuevoAnuncio.empresa = { id_empresa: this.usuario.empresa.id_empresa };
          nuevoAnuncio.fechaInicio = result.value.fechaInicio;
          nuevoAnuncio.fechaFin = result.value.fechaFin;
        }

        this.anuncioService.crearAnuncio(nuevoAnuncio).subscribe({
          next: () => {
            Swal.fire('Éxito', 'El anuncio se ha creado correctamente.', 'success');
          },
          error: () => {
            Swal.fire('Error', 'Hubo un problema al crear el anuncio.', 'error');
          }
        });
      }
    });
  }

  editarAnuncio(anuncio: any): void {
    Swal.fire({
      title: 'Editar Anuncio',
      input: 'text',
      inputLabel: 'Descripción',
      inputValue: anuncio.descripcion,
      showCancelButton: true,
      confirmButtonText: 'Actualizar',
      preConfirm: (descripcion) => {
        if (!descripcion) {
          Swal.showValidationMessage('La descripción no puede estar vacía');
          return false;
        }
        return descripcion;
      }
    }).then(result => {
      if (result.isConfirmed) {
        anuncio.descripcion = result.value;
        this.anuncioService.editarAnuncio(anuncio.idAnuncio, anuncio).subscribe(() => {
          Swal.fire('Éxito', 'Anuncio actualizado correctamente', 'success');
        }, () => {
          Swal.fire('Error', 'No se pudo actualizar el anuncio', 'error');
        });
      }
    });
  }
}
