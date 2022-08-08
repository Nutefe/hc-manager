package com.bluerizon.hcmanager.payload.genarate;


import com.bluerizon.hcmanager.models.Encaissements;
import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.payload.helper.FrenchNumber;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

//import javax.swing.text.StyleConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GenarateFacture {

    public static File inamPdf(Factures factures, List<FicheTraitements> request) throws IOException {

        File file = new File(Helpers.path_file+factures.getFileName());
//        File file = new File(Helpers.path_file+factures.getFileName()+".pdf");
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
        for (int x = 0; x < 3; x++){

            document.setTopMargin(0);
            document.setLeftMargin(0);
            document.setRightMargin(0);
            document.add(pdfImg);

            // paragraphe numero facture
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("FACTURE N°: " +factures.getNumero()+"/E2V/"+ Helpers.year()+"                                             Lomé, le "+Helpers.convertDate(factures.getCreatedAt())).setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);

            // paragraphe designation de la facture
            Paragraph paragraph2 = new Paragraph();
            paragraph2.add("Doit: ASSURANCE ................................... Pour le paiement des frais de ...............").setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED_ALL);
            paragraph2.add("........................................................................ De M./Mme/Enft : " +
                    factures.getFiche().getPatient().getNom() +
                    " " + factures.getFiche().getPatient().getPrenom()).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(20);

            document.add(paragraph1);
            document.add(paragraph2);

            // ajout du tableau a 4 columns
            Table table = new Table(4).setWidth(500);
            table.setMarginBottom(0);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // creation de l'entete du tableau
            Cell[] headerFooter = new Cell[]{
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("DESIGNATION").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Prix Unitaire").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Montant payé \n par l'assureur").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Montant payé \n par l'assuré").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
            };

            for (Cell hfCell : headerFooter) {
                table.addHeaderCell(hfCell);
            }

            // ajout dynamiquement de donnees des differente celuile
            Double totalAssureur = 0.0;
            for (int counter = 0; counter < request.size(); counter++) {
                FicheTraitements ligneRequest = request.get(counter);
                totalAssureur += ligneRequest.getNetPayAssu();
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFicheTraitementPK().getTraitement().getLibelle())).setFontSize(11));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFicheTraitementPK().getTraitement().getPrice()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getNetPayAssu()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getNetPayBeneficiaire()+"").setFontSize(11)));
            }
            table.addCell(new Cell().addStyle(new Style().setBorderRight(Border.NO_BORDER))
                    .setTextAlignment(TextAlignment.LEFT).add(new Paragraph("TOTAL").setFontSize(12).setBold().setMarginLeft(50)));
            table.addCell(new Cell().addStyle(new Style().setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(new Paragraph("")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalAssureur+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(factures.getTotal()+"").setFontSize(12)));

            document.add(table);

            Double arrete = factures.getTotal() - factures.getRemise();

            // paragraphe remise et arrete
            Paragraph paragraph4 = new Paragraph();
            Paragraph paragraph5 = new Paragraph();
            if ( x == 0){
                if (factures.getRemise() != null && factures.getRemise() != 0.0){
                    paragraph4.add("Remise: "+factures.getRemise()+" Fcfa \t\t\t\t ").setFontSize(12).setBold();
                }
                if (factures.getAcompte() != null && factures.getAcompte() != 0.0){
                    paragraph4.add(" Acompte: "+factures.getAcompte()+" Fcfa \t\t\t\t ").setFontSize(12).setBold();
                }
                if (factures.getReste() != null && factures.getReste() != 0.0){
                    paragraph4.add("  Reste: "+factures.getReste()+" Fcfa").setFontSize(12).setBold();
                }
                paragraph4.setMarginLeft(45);
                paragraph4.setMarginRight(45);
                paragraph4.setMarginTop(30);
                paragraph4.setVerticalAlignment(VerticalAlignment.BOTTOM);

                paragraph5.add("Arreté la présente facture à la somme de: "+ FrenchNumber.convert(Math.round(arrete))+"("+arrete+") Fcfa")
                        .setFontSize(12);
                paragraph5.setMarginLeft(45);
                paragraph5.setMarginRight(45);
                paragraph5.setMarginTop(30);
                paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);
            } else {

                paragraph5.add("Arreté la présente facture à la somme de: "+ FrenchNumber.convert(Math.round(totalAssureur))+"("+totalAssureur+") Fcfa")
                        .setFontSize(12);
                paragraph5.setMarginLeft(45);
                paragraph5.setMarginRight(45);
                paragraph5.setMarginTop(30);
                paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);
            }

            // paragraphe signature
            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("Signature Client "+" \t "+" "+" \t "+" \t "+" \t \t \t \t \t \t \t \t \t \t \t \t \t              Secrétaire")
                    .setFontSize(12);
            paragraph6.setMarginLeft(45);
            paragraph6.setMarginRight(45);
            paragraph6.setMarginTop(30);
            paragraph6.setVerticalAlignment(VerticalAlignment.BOTTOM);

            document.add(paragraph4);
            document.add(paragraph5);
            document.add(paragraph6);

            // create a new page
            if (x < 2){
                pdfDocument.addNewPage();
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }

        }

        document.close();
        return file;
    }
    public static File autrePdf(Factures factures, List<FicheTraitements> request) throws IOException {

        File file = new File(Helpers.path_file+factures.getFileName());
//        File file = new File(Helpers.path_file+factures.getFileName()+".pdf");
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
        for (int x = 0; x < 3; x++){

            document.setTopMargin(0);
            document.setLeftMargin(0);
            document.setRightMargin(0);
            document.add(pdfImg);

            // paragraphe numero facture
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("FACTURE N°: " +factures.getNumero()+"/E2V/"+ Helpers.year()+"                                             Lomé, le "+Helpers.convertDate(factures.getCreatedAt())).setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);

            // paragraphe designation de la facture
            Paragraph paragraph2 = new Paragraph();
            paragraph2.add("Doit: ASSURANCE ................................... Pour le paiement des frais de ...............").setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED_ALL);
            paragraph2.add("........................................................................ De M./Mme/Enft : " +
                    factures.getFiche().getPatient().getNom() +
                    " " + factures.getFiche().getPatient().getPrenom()).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(20);

            document.add(paragraph1);
            document.add(paragraph2);

            // ajout du tableau a 4 columns
            Table table = new Table(4).setWidth(500);
            table.setMarginBottom(0);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // creation de l'entete du tableau
            Cell[] headerFooter = new Cell[]{
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("DESIGNATION").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Prix Unitaire").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Montant payé \n par l'assureur").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Montant payé \n par l'assuré").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
            };

            for (Cell hfCell : headerFooter) {
                table.addHeaderCell(hfCell);
            }

            // ajout dynamiquement de donnees des differente celuile
            Double totalAssureur = 0.0;
            for (int counter = 0; counter < request.size(); counter++) {
                FicheTraitements ligneRequest = request.get(counter);
                totalAssureur += ligneRequest.getNetPayAssu();
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFicheTraitementPK().getTraitement().getLibelle())).setFontSize(11));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFicheTraitementPK().getTraitement().getPrice()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getNetPayAssu()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getNetPayBeneficiaire()+"").setFontSize(11)));
            }
            table.addCell(new Cell().addStyle(new Style().setBorderRight(Border.NO_BORDER))
                    .setTextAlignment(TextAlignment.LEFT).add(new Paragraph("TOTAL").setFontSize(12).setBold().setMarginLeft(50)));
            table.addCell(new Cell().addStyle(new Style().setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(new Paragraph("")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalAssureur+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(factures.getTotal()+"").setFontSize(12)));

            document.add(table);

            Double arrete = factures.getTotal() - factures.getRemise();

            // paragraphe remise et arrete
            Paragraph paragraph4 = new Paragraph();
            Paragraph paragraph5 = new Paragraph();
            if ( x == 0){
                if (factures.getRemise() != null && factures.getRemise() != 0.0){
                    paragraph4.add("Remise: "+factures.getRemise()+" Fcfa \t\t\t\t ").setFontSize(12).setBold();
                }
                if (factures.getAcompte() != null && factures.getAcompte() != 0.0){
                    paragraph4.add(" Acompte: "+factures.getAcompte()+" Fcfa \t\t\t\t ").setFontSize(12).setBold();
                }
                if (factures.getReste() != null && factures.getReste() != 0.0){
                    paragraph4.add("  Reste: "+factures.getReste()+" Fcfa").setFontSize(12).setBold();
                }
                paragraph4.setMarginLeft(45);
                paragraph4.setMarginRight(45);
                paragraph4.setMarginTop(30);
                paragraph4.setVerticalAlignment(VerticalAlignment.BOTTOM);

                paragraph5.add("Arreté la présente facture à la somme de: "+ FrenchNumber.convert(Math.round(arrete))+"("+arrete+") Fcfa")
                        .setFontSize(12);
                paragraph5.setMarginLeft(45);
                paragraph5.setMarginRight(45);
                paragraph5.setMarginTop(30);
                paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);
            } else {

                paragraph5.add("Arreté la présente facture à la somme de: "+ FrenchNumber.convert(Math.round(totalAssureur))+"("+totalAssureur+") Fcfa")
                        .setFontSize(12);
                paragraph5.setMarginLeft(45);
                paragraph5.setMarginRight(45);
                paragraph5.setMarginTop(30);
                paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);
            }

            // paragraphe signature
            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("Signature Client "+" \t "+" "+" \t "+" \t "+" \t \t \t \t \t \t \t \t \t \t \t \t \t Le Directeur / opticien")
                    .setFontSize(12);
            paragraph6.setMarginLeft(45);
            paragraph6.setMarginRight(45);
            paragraph6.setMarginTop(30);
            paragraph6.setVerticalAlignment(VerticalAlignment.BOTTOM);

            document.add(paragraph4);
            document.add(paragraph5);
            document.add(paragraph6);

            // create a new page
            if (x < 2){
                pdfDocument.addNewPage();
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }

        }

        document.close();
        return file;
    }
    public static File nonPdf(Factures factures, List<FicheTraitements> request) throws IOException {

        File file = new File(Helpers.path_file+factures.getFileName());
//        File file = new File(Helpers.path_file+factures.getFileName()+".pdf");
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

            // paragraphe numero facture
            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("FACTURE N°: " +factures.getNumero()+"/E2V/"+ Helpers.year()+"                                             Lomé, le "+Helpers.convertDate(factures.getCreatedAt())).setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);

            // paragraphe designation de la facture
            Paragraph paragraph2 = new Paragraph();
            paragraph2.add("M./Mme/Enft : " +
                    factures.getFiche().getPatient().getNom() +
                    " " + factures.getFiche().getPatient().getPrenom()).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(20);

            document.add(paragraph1);
            document.add(paragraph2);

            // ajout du tableau a 4 columns
            Table table = new Table(3).setWidth(500);
            table.setMarginBottom(0);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // creation de l'entete du tableau
            Cell[] headerFooter = new Cell[]{
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("DESIGNATION").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Prix Unitaire").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
//                    new Cell()
//                            .setBackgroundColor(new DeviceGray(0.75f))
//                            .add(new Paragraph("Montant payé \n par l'assureur").setFontSize(12).setBold())
//                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("Montant net a payé").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
            };

            for (Cell hfCell : headerFooter) {
                table.addHeaderCell(hfCell);
            }

            // ajout dynamiquement de donnees des differente celuile
//            Double totalAssureur = 0.0;
            for (int counter = 0; counter < request.size(); counter++) {
                FicheTraitements ligneRequest = request.get(counter);
//                totalAssureur += ligneRequest.getNetPayAssu();
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFicheTraitementPK().getTraitement().getLibelle())).setFontSize(11));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFicheTraitementPK().getTraitement().getPrice()+"").setFontSize(11)));
//                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
//                        .add(new Paragraph(ligneRequest.getNetPayAssu()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getNetPayBeneficiaire()+"").setFontSize(11)));
            }
            table.addCell(new Cell().addStyle(new Style().setBorderRight(Border.NO_BORDER))
                    .setTextAlignment(TextAlignment.LEFT).add(new Paragraph("TOTAL").setFontSize(12).setBold().setMarginLeft(50)));
            table.addCell(new Cell().addStyle(new Style().setBorderLeft(Border.NO_BORDER).setBorderRight(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(new Paragraph("")));
//            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalAssureur+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(factures.getTotal()+"").setFontSize(12)));

            document.add(table);

            Double arrete = factures.getTotal() - factures.getRemise();

            // paragraphe remise et arrete
            Paragraph paragraph4 = new Paragraph();
            Paragraph paragraph5 = new Paragraph();
            if (factures.getRemise() != null && factures.getRemise() != 0.0){
                paragraph4.add("Remise: "+factures.getRemise()+" Fcfa \t\t\t\t ").setFontSize(12).setBold();
            }
            if (factures.getAcompte() != null && factures.getAcompte() != 0.0){
                paragraph4.add(" Acompte: "+factures.getAcompte()+" Fcfa \t\t\t\t ").setFontSize(12).setBold();
            }
            if (factures.getReste() != null && factures.getReste() != 0.0){
                paragraph4.add("  Reste: "+factures.getReste()+" Fcfa").setFontSize(12).setBold();
            }
            paragraph4.setMarginLeft(45);
            paragraph4.setMarginRight(45);
            paragraph4.setMarginTop(30);
            paragraph4.setVerticalAlignment(VerticalAlignment.BOTTOM);

            paragraph5.add("Arreté la présente facture à la somme de: "+ FrenchNumber.convert(Math.round(arrete))+"("+arrete+") Fcfa")
                    .setFontSize(12);
            paragraph5.setMarginLeft(45);
            paragraph5.setMarginRight(45);
            paragraph5.setMarginTop(30);
            paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);

            // paragraphe signature
            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("Signature Client "+" \t "+" "+" \t "+" \t "+" \t \t \t \t \t \t \t \t \t \t \t \t \t Le Directeur / opticien")
                    .setFontSize(12);
            paragraph6.setMarginLeft(45);
            paragraph6.setMarginRight(45);
            paragraph6.setMarginTop(30);
            paragraph6.setVerticalAlignment(VerticalAlignment.BOTTOM);

            document.add(paragraph4);
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
    public static File caisse(Encaissements encaissement) throws IOException {

        File file = new File(Helpers.path_file+encaissement.getFileName());
//        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // ouvrir un document pour ecrire
        PdfWriter pdfWriter = new PdfWriter(file);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        PageSize pageSize = new PageSize(204 ,567);

        pdfDocument.setDefaultPageSize(pageSize);


        // Ajouter l'entete en image
        ImageData imageData = ImageDataFactory.create(Helpers.path_file+"e2v-logo.png");
        Image pdfImg = new Image(imageData)
                .setMarginTop(0)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .scaleAbsolute(30, 30);

        // Create document to add new elements
        Document document = new Document(pdfDocument);


        document.setTopMargin(0);
        document.setLeftMargin(2);
        document.setRightMargin(2);
//        document.add(pdfImg);


        // paragraphe numero facture
        Table table = new Table(2);

        Cell[] headerFooter = new Cell[]{
                new Cell()
                        .add(pdfImg)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setWidth(35),
                new Cell()
                        .setBackgroundColor(new DeviceGray(0.75f))
                        .add(new Paragraph("ESPOIR VIE VUE\n").setFontSize(9).setBold())
                        .add(new Paragraph("Centre Medico - Social Ophtalmologique\n").setFontSize(8).setBold())
                        .add(new Paragraph("Agoe-Nyve, rue AYASSOR non loin de la CEET cacaveli\n").setFontSize(6).setBold())
                        .add(new Paragraph("Tel: (+228) 70351929 / 22513519 / 90045905 / 99501254").setFontSize(6).setBold())
                        .setTextAlignment(TextAlignment.CENTER)
                        .setWidth(165),
        };

        for (Cell hfCell : headerFooter) {
            table.addHeaderCell(hfCell);
        }

        document.add(table);

        Paragraph paragraph1 = new Paragraph();

        paragraph1.add("FACTURE N°: " +encaissement.getFacture().getNumero()+"/E2V/"+ Helpers.year()).setFontSize(9).setTextAlignment(TextAlignment.CENTER);
        Paragraph pDate = new Paragraph();
        pDate.add("Date: "+ Helpers.convertDate(new Date())).setFontSize(9).setTextAlignment(TextAlignment.CENTER);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.add("Patient: " +
                encaissement.getFacture().getFiche().getPatient().getNom() +
                " " + encaissement.getFacture().getFiche().getPatient().getPrenom()).setFontSize(9).setTextAlignment(TextAlignment.CENTER);
//        Paragraph pMontantFacture = new Paragraph();
//        pMontantFacture.add("Total facture: \t\t"+(encaissement.getFacture().getTotal() - encaissement.getFacture().getRemise()));
//        Paragraph pNetPaye = new Paragraph();
//        pNetPaye.add("Net Payer: \t\t"+encaissement.getMontant());
//        Paragraph pReste = new Paragraph();
//        pReste.add("Reste a payer: \t\t"+((encaissement.getFacture().getTotal() - encaissement.getFacture().getRemise()) - encaissement.getMontant()));


//        Table tableDate = new Table(2).setWidth(180);
//        tableDate.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER))
//                .setTextAlignment(TextAlignment.LEFT).add(new Paragraph("Date Paiement: "+ Helpers.convertDate(new Date())).setFontSize(9)));
//        tableDate.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER))
//                .setTextAlignment(TextAlignment.LEFT).add(new Paragraph("Date Paiement: "+ Helpers.convertDate(new Date())).setFontSize(9)));
//        document.add(tableDate);
        Table tablePrice = new Table(2).setWidth(180);
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(
                new Paragraph("Total facture: ").setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.RIGHT).add(
                new Paragraph(""+(encaissement.getFacture().getTotal() - encaissement.getFacture().getRemise())).setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(
                new Paragraph("Net Payer:").setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.RIGHT).add(
                new Paragraph(""+encaissement.getMontant()).setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(
                new Paragraph("Reliquat:").setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.RIGHT).add(
                new Paragraph(""+encaissement.getReliquat()).setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(
                new Paragraph("Acompte:").setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.RIGHT).add(
                new Paragraph(""+encaissement.getFacture().getAcompte()).setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(
                new Paragraph("Reste a payer:").setFontSize(9)));
        tablePrice.addCell(new Cell().addStyle(new Style().setBorder(Border.NO_BORDER)).setTextAlignment(TextAlignment.RIGHT).add(
                new Paragraph(""+(encaissement.getReste())).setFontSize(9)));
        tablePrice.setHorizontalAlignment(HorizontalAlignment.CENTER);



        document.add(paragraph1);
        document.add(pDate);
        document.add(paragraph2);
        document.add(tablePrice);
//        document.add(pMontantFacture);
//        document.add(pNetPaye);
//        document.add(pReste);

        document.close();
        return file;
    }


}
