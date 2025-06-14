import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import Swal from 'sweetalert2';
import {NgIf, NgFor, CommonModule} from "@angular/common";
import {AnuncioService} from "../../services/anuncio.service";


@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    NgFor,
    CommonModule
  ],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss'
})
export class UsuariosComponent implements OnInit{

  usuarioForm!: FormGroup;
  usuario: any = null;
  nombreUsuarioExiste = false;
  correoExiste = false;
  telfInvalido = false;
  anunciosEmpresaUsuario: any[]=[] ;
  anunciosTrabajadorUsuario: any[]=[] ;
  tipoAnuncios: 'empresa' | 'trabajador' | null = null;
  private anuncios: any;
  constructor(private fb: FormBuilder, private authService: AuthService, private anuncioService: AnuncioService) {
  }
  ngOnInit(): void {
    this.cargarUsuario();
  }



  // Cargar usuarios desde el backend
  cargarUsuario() {
    const usuarioData = this.authService.obtenerUsuario();
    if (usuarioData) {
      this.usuario = usuarioData;
      this.usuarioForm = this.fb.group({
        nomUsuario: [this.usuario.nomUsuario],
        correo: [this.usuario.correo],
        telefono: [
          this.usuario.trabajador ? this.usuario.trabajador.telefono : this.usuario.empresa?.telefono,
          [Validators.required, Validators.pattern(/^[0-9]{9}$/)]
        ],
        // Campos adicionales de Trabajador
        nombreTrabajador: [this.usuario.trabajador?.nombre || '', [Validators.pattern(/^[a-zA-Z\s]+$/)]],
        apellidosTrabajador: [this.usuario.trabajador?.apellidos || '', [Validators.pattern(/^[a-zA-Z\s]+$/)]],
        informacionTrabajador: [this.usuario.trabajador?.informacion || ''],
        // Campos adicionales de Empresa
        nombreEmpresa: [this.usuario.empresa?.nombre || '', [Validators.pattern(/^[a-zA-Z\s]+$/)]],
        descripcionEmpresa: [this.usuario.empresa?.descripcion || ''],
      });

      // Escucha cambios en el campo teléfono
      this.usuarioForm.get('telefono')?.valueChanges.subscribe(value => {
        this.telfInvalido = !/^[0-9]{9}$/.test(value);
      });
      this.usuarioForm.get('nomUsuario')?.valueChanges.subscribe(value => this.verificarNombreUsuario(value));
      this.usuarioForm.get('correo')?.valueChanges.subscribe(value => this.verificarCorreo(value));

      if (this.usuario) {
        const id = this.usuario.id;
        console.log("Id que pasamos: "+ id);

        this.authService.getTodosAnunciosUser(id).subscribe({
          next: (res) => {

            this.anunciosEmpresaUsuario = res.anuncioEmpresa;
            this.anunciosTrabajadorUsuario = res.anuncioTrabajador;

            // Lógica para mostrar solo una sección
            if (this.anunciosTrabajadorUsuario?.length > 0) {
              this.tipoAnuncios = 'trabajador';
            } else if (this.anunciosEmpresaUsuario?.length > 0) {
              this.tipoAnuncios = 'empresa';
            } else {
              this.tipoAnuncios = null;
            }
          },
          error: (err) => {
            console.error('Error al cargar anuncios del usuario', err);
          }
        });
      }
    }
  }



  get esTrabajador(): boolean {
    return !!this.usuario?.trabajador;
  }

  get esEmpresa(): boolean {
    return !!this.usuario?.empresa;
  }

  verificarNombreUsuario(nuevoNombre: string) {
    this.authService.verificarNombreUsuario(nuevoNombre).subscribe(existe => {
      this.nombreUsuarioExiste = existe;
    });
  }

  verificarCorreo(nuevoCorreo: string) {
    this.authService.verificarCorreo(nuevoCorreo).subscribe(existe => {
      this.correoExiste = existe;
    });
  }
  guardarCambios() {
    if (this.usuarioForm.invalid || this.nombreUsuarioExiste || this.correoExiste) return;

    const datosActualizados = { ...this.usuario, ...this.usuarioForm.value };

    // Si es trabajador, actualizar solo datos de trabajador
    if (this.esTrabajador) {
      datosActualizados.trabajador = {
        ...this.usuario.trabajador,
        nombre: this.usuarioForm.value.nombreTrabajador,
        apellidos: this.usuarioForm.value.apellidosTrabajador,
        informacion: this.usuarioForm.value.informacionTrabajador,
        telefono: this.usuarioForm.value.telefono,
      };
    }

    // Si es empresa, actualizar solo datos de empresa
    if (this.esEmpresa) {
      datosActualizados.empresa = {
        ...this.usuario.empresa,
        nombre: this.usuarioForm.value.nombreEmpresa,
        descripcion: this.usuarioForm.value.descripcionEmpresa,
        telefono: this.usuarioForm.value.telefono,
      };
    }
    //JSON STRING para OBJ -> STRING
    //JSON PARSE para STRING -> OBJ
    this.authService.updateUsuario(this.usuario.idUsuario, datosActualizados).subscribe({
      next: (usuarioActualizado) => {
        Swal.fire('Éxito', 'Tus datos han sido actualizados.', 'success');
        //console.log("VALORES CAMBIADOS: "+JSON.stringify(datosActualizados));
        //console.log("NUEVO USUARIO: "+JSON.stringify(usuarioActualizado));
        this.authService.setUsuario(usuarioActualizado); // Actualiza localStorage y BehaviorSubject
      },
      error: () => Swal.fire('Error', 'Hubo un problema al actualizar los datos.', 'error')
    });
  }

  confirmarEliminarUsuario() {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción eliminará tu cuenta permanentemente.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eliminarUsuario();
      }
    });
  }
  eliminarUsuario() {
    const usuarioData = this.authService.obtenerUsuario();
    console.log("Usuario antes de eliminar:", usuarioData);
    if (!usuarioData || !usuarioData.idUsuario) {
      Swal.fire('Error', 'No se puede eliminar el usuario. ID no encontrado.', 'error');
      return;
    }

    this.authService.deleteUsuario(usuarioData.idUsuario).subscribe({
      next: () => {
        Swal.fire('Cuenta eliminada', 'Tu cuenta ha sido eliminada correctamente.', 'success');
        localStorage.removeItem('usuario');
        window.location.href = '/landing';
      },
      error: () => Swal.fire('Error', 'Hubo un problema al eliminar la cuenta.', 'error')
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
        // Actualizar el array correspondiente
        if (tipoAnuncio === "empresa") {
          this.anunciosEmpresaUsuario = this.anunciosEmpresaUsuario.filter(a => a.idAnuncioEmpresa !== idAnuncio);
        } else {
          this.anunciosTrabajadorUsuario = this.anunciosTrabajadorUsuario.filter(a => a.idAnuncioTrabajador !== idAnuncio);
        }
        Swal.fire('Anuncio Eliminado', 'Tu anuncio ha sido eliminada correctamente.', 'success');
      },
      error: () => Swal.fire('Error', 'Hubo un problema al eliminar el anuncio.', 'error')
    });
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
            this.ngOnInit();
          },
          error: () => {
            Swal.fire('Error', 'Hubo un problema al crear el anuncio.', 'error');
          }
        });
      }
    });
  }
  obtenerFechaActualUTCmas1(): string {
    const fechaActual = new Date();

    // Convertir a UTC+1 (España)
    fechaActual.setHours(fechaActual.getHours() + 1);

    // Formatear en "yyyy-MM-dd"
    return fechaActual.toISOString().split("T")[0];
  }
}
