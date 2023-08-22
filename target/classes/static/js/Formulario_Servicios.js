function enviarDatos() {
    var id = document.getElementById("id").value;
    var titulo = document.getElementById("titulo").value;
    var descripcion = document.getElementById("descripcion").value;
    var foto = document.getElementById("fotos").files[0]; // Es para obtener el archivo de la foto si es necesario

    var formData = new FormData();
    
    formData.append("titulo", titulo);
    formData.append("rubro", rubro);
    formData.append("descripcion", descripcion);
    formData.append("fotos", foto);

   // Realizar la solicitud AJAX para enviar los datos al servidor
   fetch("Vista_Formulario_Servicios.html", {           //
    method: "POST",             // Esto envia la solicitud para guardarla en la base de datos
    body: formData,
   
}).then(function(response) {
    if (response.ok) {
        // Respuesta exitosa
        document.getElementById("mensajeRespuesta").textContent = "Publicación exitosa";
    } else {
        // Error al enviar los datos
        document.getElementById("mensajeRespuesta").textContent = "Error al publicar";
    }
}).catch(function(error) {
    console.log("Error:", error);
});
}

// Esto es para mostrar arlado del boton elegir archivos las imagenes que cargaste en miniatura
function mostrarMiniaturas() {
    const contenedorMiniaturas = document.getElementById('miniaturas');
    contenedorMiniaturas.innerHTML = ''; // Limpiar el contenido previo

    const inputFotos = document.getElementById('fotos');
    const archivos = inputFotos.files;

    for (let i = 0; i < archivos.length; i++) {
        const archivo = archivos[i];
        const reader = new FileReader();

        reader.onload = function (e) {
            const miniatura = document.createElement('div');
            miniatura.classList.add('col-2', 'mb-4');

            const imagen = document.createElement('img');
            imagen.src = e.target.result;
            imagen.alt = 'Miniatura';
            imagen.classList.add('img-thumbnail'); // Agregamos la clase para la miniatura de Bootstrap

            miniatura.appendChild(imagen);
            contenedorMiniaturas.appendChild(miniatura);
        };

        reader.readAsDataURL(archivo);
    }
}

// Evento para mostrar las miniaturas al seleccionar imágenes
document.getElementById('fotos').addEventListener('change', mostrarMiniaturas);

function formatText(command, value = null) {
    document.execCommand(command, false, value);
    updateHiddenEditor();
}

function updateHiddenEditor() {
    const editor = document.getElementById('editor');
    const hiddenEditor = document.getElementById('hidden-editor');
    hiddenEditor.value = editor.innerHTML;
}

document.getElementById('editor').addEventListener('input', updateHiddenEditor);
