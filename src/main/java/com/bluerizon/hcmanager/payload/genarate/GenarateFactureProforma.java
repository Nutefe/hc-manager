package com.bluerizon.hcmanager.payload.genarate;


import com.bluerizon.hcmanager.models.Etats;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.TypeTraitements;
import com.bluerizon.hcmanager.payload.helper.FrenchNumber;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.request.FactureProformaRequest;
import com.bluerizon.hcmanager.payload.request.TraitementProformaRequest;
import com.bluerizon.hcmanager.payload.response.EtatFactureEntreprise;
import com.bluerizon.hcmanager.payload.response.EtatFacturePatient;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.bluerizon.hcmanager.payload.helper.Helpers.convertFr;

public class GenarateFactureProforma {
    public static File etatPatient(FactureProformaRequest request) throws IOException {

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

            paragraph2.add("FACTURE PROFORMA").setFontSize(13).setUnderline().setBold().setTextAlignment(TextAlignment.CENTER);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(20);
            paragraph2.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Paragraph paragraph3 = new Paragraph();
            paragraph3.add("Nom : "+request.getNom()).setFontSize(13).setBold().setTextAlignment(TextAlignment.LEFT);
            paragraph3.setMarginLeft(45);
            paragraph3.setMarginRight(45);
            paragraph3.setMarginBottom(20);
            paragraph3.setHorizontalAlignment(HorizontalAlignment.LEFT);

            Paragraph paragraph4 = new Paragraph();
            paragraph4.add("Prénom : "+request.getPrenom()).setFontSize(13).setBold().setTextAlignment(TextAlignment.LEFT);
            paragraph4.setMarginLeft(45);
            paragraph4.setMarginRight(45);
            paragraph4.setMarginBottom(20);
            paragraph4.setHorizontalAlignment(HorizontalAlignment.LEFT);

            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);

            if (request.getDateNaiss() != null && !request.getDateNaiss().equals("")){
                Paragraph paragraph5 = new Paragraph();
                paragraph5.add("Age : "+request.getDateNaiss()).setFontSize(13).setBold().setTextAlignment(TextAlignment.LEFT);
                paragraph5.setMarginLeft(45);
                paragraph5.setMarginRight(45);
                paragraph5.setMarginBottom(20);
                paragraph5.setHorizontalAlignment(HorizontalAlignment.LEFT);
                document.add(paragraph5);
            }

            // ajout du tableau a 4 columns
            Table table = new Table(2).setWidth(500);
            table.setMarginBottom(0);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // creation de l'entete du tableau
            Cell[] headerFooter = new Cell[]{
                    new Cell()
                            .setBorderTop(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .setBorderBottom(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .setBorderLeft(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .setBorderRight(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .add(new Paragraph("Désignation").setFontSize(13).setBold())
                            .setTextAlignment(TextAlignment.CENTER).setFontColor(new DeviceRgb(21, 109, 214)),
                    new Cell()
                            .setBorderTop(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .setBorderBottom(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .setBorderLeft(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .setBorderRight(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            .add(new Paragraph("Coûts").setFontSize(13).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
            };

            for (Cell hfCell : headerFooter) {
                table.addHeaderCell(hfCell);
            }

            // ajout dynamiquement de donnees des differente celuile
            Double somme =0.0;
            for (int counter = 0; counter < request.getTraitements().size(); counter++) {
                TraitementProformaRequest   proformaRequest = request.getTraitements().get(counter);
                somme += proformaRequest.getPrice();
                table.addCell(new Cell()
                        .addStyle(new Style()
                                .setBorderTop(Border.NO_BORDER)
                                .setBorderBottom(Border.NO_BORDER)
                                .setBorderLeft(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                                .setBorderRight(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                        )
                        .setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(proformaRequest.getTraitement()+"").setFontSize(12)));
                if (proformaRequest.getPrice() > 0){
                    table.addCell(new Cell()
                            .addStyle(new Style()
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderBottom(Border.NO_BORDER)
                                    .setBorderLeft(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                                    .setBorderRight(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            )
                            .setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph(proformaRequest.getPrice()+"").setFontSize(12)));
                } else {
                    table.addCell(new Cell()
                            .addStyle(new Style()
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderBottom(Border.NO_BORDER)
                                    .setBorderLeft(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                                    .setBorderRight(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                            )
                            .setTextAlignment(TextAlignment.CENTER)
                            .add(new Paragraph("").setFontSize(12)));
                }

            }
            table.addCell(new Cell()
                    .addStyle(new Style()
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderBottom(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                                .setBorderRight(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                    )
                    .setTextAlignment(TextAlignment.LEFT).add(new Paragraph().setFontSize(12).setBold()));
            table.addCell(new Cell()
                    .addStyle(new Style()
                                    .setBorderTop(Border.NO_BORDER)
                                    .setBorderBottom(new SolidBorder(new DeviceRgb(21, 109, 214), 1))
                                .setBorderLeft(Border.NO_BORDER)
                    )
                    .setTextAlignment(TextAlignment.LEFT).add(new Paragraph()));
//
            document.add(table);

            if (somme >0){
                Paragraph paragraph5 = new Paragraph();
                paragraph5.add("Arreté la présente facture à la somme de: "+somme.intValue())
                        .setFontSize(12);
                paragraph5.setMarginLeft(45);
                paragraph5.setMarginRight(45);
                paragraph5.setMarginTop(30);
                paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);

                document.add(paragraph5);
            } else {
                Paragraph paragraph5 = new Paragraph();
                paragraph5.add("Arreté la présente facture à la somme de: ...................................................................................")
                        .setFontSize(12);
                paragraph5.setMarginLeft(45);
                paragraph5.setMarginRight(45);
                paragraph5.setMarginTop(30);
                paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);
                Paragraph paragraph6 = new Paragraph();
                paragraph6.add("......................................................................................................................................................")
                        .setFontSize(12);
                paragraph6.setMarginLeft(45);
                paragraph6.setMarginRight(45);
                paragraph6.setMarginTop(15);
                paragraph6.setVerticalAlignment(VerticalAlignment.BOTTOM);

                document.add(paragraph5);
                document.add(paragraph6);
            }

            // paragraphe signature
            Paragraph paragraph7 = new Paragraph();
            paragraph7.add("Le Directeur")
                    .setFontSize(12).setBold().setTextAlignment(TextAlignment.RIGHT);
            paragraph7.setMarginLeft(45);
            paragraph7.setMarginRight(45);
            paragraph7.setMarginTop(30);
            paragraph7.setVerticalAlignment(VerticalAlignment.BOTTOM);
            paragraph7.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            document.add(paragraph7);

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
