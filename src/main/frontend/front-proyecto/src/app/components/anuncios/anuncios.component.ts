import {Component, EventEmitter, HostListener, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {AnuncioService} from "../../services/anuncio.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {ContenidoComponent} from "../../contenido/contenido.component";
import Swal from "sweetalert2";
@Component({
  selector: 'app-anuncios',
  standalone: true,
  imports: [
    MatPaginator,
    NgForOf,
    NgIf,
    NgClass
  ],
  templateUrl: './anuncios.component.html',
  styleUrl: './anuncios.component.scss',
  providers:[AnuncioService]
})
export class AnunciosComponent implements OnInit, OnChanges{
  @Input() filtroSeleccionado: string = 'todos'; // Recibimos el filtro desde el padre
  @Input() usuario: any = null; // Usuario autenticado

  anuncios: any[] = [];
  anunciosMostrados: any[] = [];
  totalAnuncios: number = 0;
  //totalPages: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;
  esNuevo : boolean = false;
  cantidadInicial = 5;      // Cuántos anuncios mostramos primero
  incremento = 5;           // Cuántos se añaden al hacer "Ver más"
  constructor(private anuncioService: AnuncioService) {}

  ngOnInit(): void {
    this.loadAnuncios();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['filtroSeleccionado']) {
      this.currentPage = 0;
      this.anuncios = []; // Limpiar lista al cambiar filtro
      this.anunciosMostrados = []; // Limpiar lista al cambiar filtro
      this.loadAnuncios();
    }
  }

  mostrarBotonArriba: boolean = false;

  @HostListener('window:scroll', [])
  onScroll(): void {
    this.mostrarBotonArriba = window.scrollY > 300; // Muestra el botón si bajamos 300px
  }

  scrollArriba(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
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
        this.anunciosMostrados = data.anuncioEmpresa || [];
        this.totalAnuncios = data.totalItems || 0;
      },
      error: (error) => console.error('Error al cargar anuncios de empresas:', error)
    });
  }

  loadAnunciosTrabajador(): void {
    this.anuncioService.getAnunciosTrabajadores(this.pageSize, this.currentPage).subscribe({
      next: (data) => {
        this.anunciosMostrados = data.anuncioTrabajadores || [];
        this.totalAnuncios = data.totalItems || 0;
      },
      error: (error) => console.error('Error al cargar anuncios de trabajadores:', error)
    });
  }

  loadAnunciosTodos(): void {
    this.anuncioService.getTodosLosAnuncios().subscribe({
      next: (data) => {
        const empresaAnuncios = data.anuncioEmpresa || [];
        const trabajadorAnuncios = data.anuncioTrabajador || [];

        // Mezclamos todos los anuncios y los ordenamos por ID
        this.anuncios = [...empresaAnuncios, ...trabajadorAnuncios]
          .sort((a, b) => b.id - a.id);

        this.anunciosMostrados = this.anuncios.slice(0, this.cantidadInicial);
        //console.log("Anuncios: "+ JSON.stringify(this.anunciosMostrados));
      },
      error: (error) => console.error('Error al cargar anuncios:', error)
    });
  }

  verMas(): void {
    console.log("Se ha pulsado Ver más")
    const siguienteCantidad = this.anunciosMostrados.length + this.incremento;
    this.anunciosMostrados = this.anuncios.slice(0, siguienteCantidad);
    console.log("Estos son los anuncios Mostrados:"+JSON.stringify(this.anunciosMostrados))
  }

  onPageChange(event: PageEvent) {
    console.log(` Página seleccionada: ${event.pageIndex}, Tamaño: ${event.pageSize}`);
    // Actualizamos la página actual y el tamaño de página
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    // Volver a cargar los anuncios con la nueva paginación
    this.loadAnuncios();
  }

  esPropietario(anuncio: any): boolean {
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');

    if (!usuario.idUsuario) return false; // Si no hay usuario logueado, no permitir editar

    if (anuncio.trabajador && usuario.trabajador) {
      return anuncio.trabajador.id_trabajador === usuario.trabajador.id_trabajador;
    }

    if (anuncio.empresa && usuario.empresa) {
      return anuncio.empresa.idEmpresa === usuario.empresa.idEmpresa;
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
    if (usuario.empresa) {
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
        if (usuario.empresa) {
          const fechaInicio = usuario.empresa ? (document.getElementById('fechaInicio') as HTMLInputElement)?.value : null;
          let fechaFin = this.usuario.empresa ? (document.getElementById('fechaFin') as HTMLInputElement)?.value : null;

          if (usuario.empresa && !fechaInicio) {
            Swal.showValidationMessage('La fecha de inicio es obligatoria.');
            return false;
          }
          // Si no se introduce fecha de fin, se deja como `null`
          fechaFin = fechaFin || null;

          if (!descripcion) {
            Swal.showValidationMessage('La descripción es obligatoria.');
            return false;
          }
          return { descripcion, fechaInicio, fechaFin };
        }
        if (!descripcion) {
          Swal.showValidationMessage('La descripción es obligatoria.');
          return false;
        }

        return {descripcion};


      }
    }).then((result) => {
      if (result.isConfirmed) {

        let nuevoAnuncio: any = {
          descripcion: result.value.descripcion
        };

        if (usuario.trabajador) {
          nuevoAnuncio.fecha_publicacion = this.obtenerFechaActualUTCmas1();
          nuevoAnuncio.trabajador = { id_trabajador: usuario.trabajador.id_trabajador };
        } else if (usuario.empresa) {
          nuevoAnuncio.empresa = { id_empresa: usuario.empresa.id_empresa };
          nuevoAnuncio.fechaInicio = result.value.fechaInicio;
          nuevoAnuncio.fechaFin = result.value.fechaFin;
        }

        this.anuncioService.crearAnuncio(nuevoAnuncio).subscribe({
          next: () => {

            Swal.fire('Éxito', 'El anuncio se ha creado correctamente.', 'success');
            this.esNuevo= true;
            this.anunciosMostrados.unshift(nuevoAnuncio)
            setTimeout(() => {
              nuevoAnuncio.esNuevo = false;
            }, 5000);
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

        let id : number = 0;
        if (anuncio.idAnuncioEmpresa)
        {
          id = anuncio.idAnuncioEmpresa
        }
        if (anuncio.idAnuncioTrabajador)
        {
          id = anuncio.idAnuncioTrabajador
        }

        this.anuncioService.editarAnuncio(id, anuncio).subscribe(() => {
          Swal.fire('Éxito', 'Anuncio actualizado correctamente', 'success');
        }, () => {
          Swal.fire('Error', 'No se pudo actualizar el anuncio', 'error');
        });
      }
    });
  }

  confirmarBorrarAnuncio(anuncio: any){
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción borrara este anuncio.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eliminarAnuncio(anuncio);
      }
    });
  }
  eliminarAnuncio(anuncio:any){
    let idAnuncio: number;
    let tipoAnuncio: string;

    if (anuncio.empresa)
    {
      idAnuncio = anuncio.idAnuncioEmpresa;
      tipoAnuncio = "empresa";
    }
    else
    {
      idAnuncio = anuncio.idAnuncioTrabajador;
      tipoAnuncio = "trabajador";
    }

    this.anuncioService.borrarAnuncio(idAnuncio,tipoAnuncio).subscribe({
      next: () => {
        this.anuncios = this.anuncios.filter(a => a.idAnuncioEmpresa !== idAnuncio && a.idAnuncioTrabajador !== idAnuncio);
        this.anunciosMostrados = this.anuncios.filter(a => a.idAnuncioEmpresa !== idAnuncio && a.idAnuncioTrabajador !== idAnuncio);
        Swal.fire('Anuncio Eliminado', 'Tu anuncio ha sido eliminada correctamente.', 'success');
     },
      error: () => Swal.fire('Error', 'Hubo un problema al eliminar el anuncio.', 'error')
    });
  }
}
