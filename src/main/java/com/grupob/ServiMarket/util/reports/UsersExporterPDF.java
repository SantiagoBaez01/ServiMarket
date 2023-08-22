package com.grupob.ServiMarket.util.reports;

import com.grupob.ServiMarket.entity.UserEntity;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UsersExporterPDF {
    private List<UserEntity> listaUsers;

    public UsersExporterPDF(List<UserEntity> listaUsers) {
        super();
        this.listaUsers = listaUsers;
    }

    private void escribirCabeceraDeLaTabla(PdfPTable tabla) {
        PdfPCell celda = new PdfPCell();

        celda.setBackgroundColor(Color.BLUE);
        celda.setPadding(5);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.WHITE);

        celda.setPhrase(new Phrase("ID", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Nombre", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Apellido", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Email", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Contacto", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Rol", fuente));
        tabla.addCell(celda);

        celda.setPhrase(new Phrase("Estado", fuente));
        tabla.addCell(celda);


    }
    private void escribirDatosDeLaTabla(PdfPTable tabla) {
        for (UserEntity user : listaUsers) {
            tabla.addCell(String.valueOf(user.getId()));
            tabla.addCell(user.getName());
            tabla.addCell(user.getLastName());
            tabla.addCell(user.getEmail());
            tabla.addCell(String.valueOf(user.getContact()));
            tabla.addCell(String.valueOf(user.getRole()));
            tabla.addCell(String.valueOf(user.isStatus()));

        }
    }

    public void exportar(HttpServletResponse response) throws DocumentException, IOException {
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuente.setColor(Color.BLUE);
        fuente.setSize(18);

        Paragraph titulo = new Paragraph("Lista de Usuarios", fuente);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);
        tabla.setWidths(new float[] { 1f, 2.3f, 2.3f, 6f, 3.5f, 2f, 2f });
        tabla.setWidthPercentage(110);

        escribirCabeceraDeLaTabla(tabla);
        escribirDatosDeLaTabla(tabla);

        documento.add(tabla);
        documento.close();
    }

}
