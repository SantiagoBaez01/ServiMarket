const fila = document.querySelector('.contenedor-carousel');
const peliculas = document.querySelectorAll('.pelicula');

const flechaIzquierda = document.getElementById('flecha-izquierda');
const flechaDerecha = document.getElementById('flecha-derecha');

// ? ----- ----- Event Listener para la flecha derecha. ----- -----
flechaDerecha.addEventListener('click', () => {
	fila.scrollLeft += fila.offsetWidth;

	const indicadorActivo = document.querySelector('.indicadores .activo');
	if(indicadorActivo.nextSibling){
		indicadorActivo.nextSibling.classList.add('activo');
		indicadorActivo.classList.remove('activo');
	}
});

// ? ----- ----- Event Listener para la flecha izquierda. ----- -----
flechaIzquierda.addEventListener('click', () => {
	fila.scrollLeft -= fila.offsetWidth;

	const indicadorActivo = document.querySelector('.indicadores .activo');
	if(indicadorActivo.previousSibling){
		indicadorActivo.previousSibling.classList.add('activo');
		indicadorActivo.classList.remove('activo');
	}
});

// ? ----- ----- Hover ----- -----
peliculas.forEach((pelicula) => {
	pelicula.addEventListener('mouseenter', (e) => {
		const elemento = e.currentTarget;
		setTimeout(() => {
			peliculas.forEach(pelicula => pelicula.classList.remove('hover'));
			elemento.classList.add('hover');
		}, 300);
	});
});

fila.addEventListener('mouseleave', () => {
	peliculas.forEach(pelicula => pelicula.classList.remove('hover'));
});

// Obtener la referencia al div del carrusel y a la etiqueta img del contenedor
const carruselDiv = document.querySelector('.peliculas-recomendadas');
const imagenSeleccionada = document.getElementById('imagenSeleccionada');

// Agregar un evento al carrusel para detectar cuando se selecciona una imagen
carruselDiv.addEventListener('click', (event) => {
    // Verificar si el elemento clickeado es una imagen dentro del carrusel
    if (event.target.tagName === 'IMG') {
        // Obtener la URL de la imagen seleccionada
        const urlImagenSeleccionada = event.target.src;
        // Asignar la URL como el atributo src de la etiqueta img del contenedor
        imagenSeleccionada.src = urlImagenSeleccionada;

        // Prevenir el comportamiento predeterminado del enlace (desplazamiento hacia arriba)
        event.preventDefault();
    }
});

// Mostrar la primera imagen del carrusel en el container desde el principio
const primeraImagen = carruselDiv.querySelector('.pelicula img');
const urlPrimeraImagen = primeraImagen.src;
imagenSeleccionada.src = urlPrimeraImagen;
