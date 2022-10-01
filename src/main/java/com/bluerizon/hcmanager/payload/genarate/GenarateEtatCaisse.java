package com.bluerizon.hcmanager.payload.genarate;


import com.bluerizon.hcmanager.models.Caisses;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.request.FactureProformaRequest;
import com.bluerizon.hcmanager.payload.request.TraitementProformaRequest;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class GenarateEtatCaisse {
    public static File etatCaisse(Caisses request) throws IOException {

        File file = new File(Helpers.path_file+"facture_proforma.pdf");
//        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ouvrir un document pour ecrire
        PdfWriter pdfWriter = new PdfWriter(file);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);


        pdfDocument.setDefaultPageSize(PageSize.A4);

        // Ajouter l'entete en image
        ImageData imageData = ImageDataFactory.create(Helpers.path_file+"entete_e2v.jpg");
        Image pdfImg = new Image(imageData)
                .setMarginTop(0)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .scaleAbsolute(595, 110);

        // Create document to add new elements
        Document document = new Document(pdfDocument);

        // la boucle pour avoir 3 pages
        for (int x = 0; x < 2; x++){

            document.setTopMargin(0);
            document.setLeftMargin(0);
            document.setRightMargin(0);
            document.add(pdfImg);
            document.setFontColor(new DeviceRgb(21, 109, 214));

            // paragraphe numero facture
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("Lomé, le "+Helpers.convertDate(new Date())).setFontSize(12).setTextAlignment(TextAlignment.RIGHT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);
            paragraph1.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            // paragraphe designation de la facture
            Paragraph paragraph2 = new Paragraph();
//            String date = convertFr(etat.getDateFin()).toUpperCase();

            paragraph2.add("ETAT DE LA CAISSE LE "+Helpers.convertDate(new Date())).setFontSize(13).setUnderline().setBold().setTextAlignment(TextAlignment.CENTER);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(15);
            paragraph2.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Paragraph paragraph3 = new Paragraph();
            paragraph3.add("Montant total encaisse : "+request.getRecette()).setFontSize(13).setBold().setTextAlignment(TextAlignment.LEFT);
            paragraph3.setMarginLeft(45);
            paragraph3.setMarginRight(45);
            paragraph3.setMarginBottom(15);
            paragraph3.setHorizontalAlignment(HorizontalAlignment.LEFT);

            Paragraph paragraph4 = new Paragraph();
            paragraph4.add("Montant total decaisser : "+request.getDecaissement()).setFontSize(13).setBold().setTextAlignment(TextAlignment.LEFT);
            paragraph4.setMarginLeft(45);
            paragraph4.setMarginRight(45);
            paragraph4.setMarginBottom(15);
            paragraph4.setHorizontalAlignment(HorizontalAlignment.LEFT);

            Paragraph paragraph5 = new Paragraph();

            paragraph5.add("Montant total dans la caisse : "+ request.getSolde())
                    .setFontSize(13).setBold();
            paragraph5.setMarginLeft(45);
            paragraph5.setMarginRight(45);
            paragraph5.setMarginBottom(15);
            paragraph5.setHorizontalAlignment(HorizontalAlignment.LEFT);

            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);


            // paragraphe signature
            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("La caisse")
                    .setFontSize(12).setBold().setTextAlignment(TextAlignment.RIGHT);
            paragraph6.setMarginLeft(45);
            paragraph6.setMarginRight(45);
            paragraph6.setMarginTop(30);
            paragraph6.setVerticalAlignment(VerticalAlignment.BOTTOM);
            paragraph6.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            document.add(paragraph5);
            document.add(paragraph6);

            // create a new page
            if (x < 1){
                pdfDocument.addNewPage();
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }

        }

        document.close();
        return file;
    }

    public static File etatReserve(Double tReserve, Double tDepense) throws IOException {

        File file = new File(Helpers.path_file+"facture_proforma.pdf");
//        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ouvrir un document pour ecrire
        PdfWriter pdfWriter = new PdfWriter(file);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);


        pdfDocument.setDefaultPageSize(PageSize.A4);

        // Ajouter l'entete en image
        ImageData imageData = ImageDataFactory.create(Helpers.path_file+"entete_e2v.jpg");
        Image pdfImg = new Image(imageData)
                .setMarginTop(0)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .scaleAbsolute(595, 110);

        // Create document to add new elements
        Document document = new Document(pdfDocument);

        // la boucle pour avoir 3 pages
        for (int x = 0; x < 2; x++){

            document.setTopMargin(0);
            document.setLeftMargin(0);
            document.setRightMargin(0);
            document.add(pdfImg);
            document.setFontColor(new DeviceRgb(21, 109, 214));

            // paragraphe numero facture
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("Lomé, le "+Helpers.convertDate(new Date())).setFontSize(12).setTextAlignment(TextAlignment.RIGHT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);
            paragraph1.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            // paragraphe designation de la facture
            Paragraph paragraph2 = new Paragraph();
//            String date = convertFr(etat.getDateFin()).toUpperCase();

            paragraph2.add("ETAT DE LA RESERVE LE "+Helpers.convertDate(new Date())).setFontSize(13).setUnderline().setBold().setTextAlignment(TextAlignment.CENTER);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(15);
            paragraph2.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Paragraph paragraph3 = new Paragraph();
            paragraph3.add("Montant total reserve : "+tReserve.intValue()).setFontSize(13).setBold().setTextAlignment(TextAlignment.LEFT);
            paragraph3.setMarginLeft(45);
            paragraph3.setMarginRight(45);
            paragraph3.setMarginBottom(15);
            paragraph3.setHorizontalAlignment(HorizontalAlignment.LEFT);

            Paragraph paragraph4 = new Paragraph();
            paragraph4.add("Montant total depense : "+tDepense.intValue()).setFontSize(13).setBold().setTextAlignment(TextAlignment.LEFT);
            paragraph4.setMarginLeft(45);
            paragraph4.setMarginRight(45);
            paragraph4.setMarginBottom(15);
            paragraph4.setHorizontalAlignment(HorizontalAlignment.LEFT);

//            Paragraph paragraph5 = new Paragraph();
//
//            paragraph5.add("Montant total dans la caisse : "+ request.getSolde())
//                    .setFontSize(12);
//            paragraph5.setMarginLeft(45);
//            paragraph5.setMarginRight(45);
//            paragraph5.setMarginTop(30);
//            paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);

            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);


            // paragraphe signature
            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("La caisse")
                    .setFontSize(12).setBold().setTextAlignment(TextAlignment.RIGHT);
            paragraph6.setMarginLeft(45);
            paragraph6.setMarginRight(45);
            paragraph6.setMarginTop(30);
            paragraph6.setVerticalAlignment(VerticalAlignment.BOTTOM);
            paragraph6.setHorizontalAlignment(HorizontalAlignment.RIGHT);

//            document.add(paragraph5);
            document.add(paragraph6);

            // create a new page
            if (x < 1){
                pdfDocument.addNewPage();
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }

        }

        document.close();
        return file;
    }
}
