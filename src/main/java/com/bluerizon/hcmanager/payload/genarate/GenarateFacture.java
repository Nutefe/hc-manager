package com.bluerizon.hcmanager.payload.genarate;


import com.bluerizon.hcmanager.models.Factures;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
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

import javax.swing.text.StyleConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class GenarateFacture {

    public static File myPdfHelloWordClientMoral(Factures factures, List<FicheTraitements> request) throws IOException {

        File file = new File(Helpers.path_file+factures.getFileName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Open PDF document in write mode
        PdfWriter pdfWriter = new PdfWriter(file);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);


        pdfDocument.setDefaultPageSize(PageSize.A4);

        ImageData imageData = ImageDataFactory.create(Helpers.path_file+"entete_e2v.jpg");
        Image pdfImg = new Image(imageData)
                .setMarginTop(0)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .scaleAbsolute(595, 110);

        // Create document to add new elements
        Document document = new Document(pdfDocument);

        for (int x = 0; x < 3; x++){

//            PdfCanvas canvas = new PdfCanvas(pdfDocument.addNewPage())
//                    .stroke();

            document.setTopMargin(0);
            document.setLeftMargin(0);
            document.setRightMargin(0);
            document.add(pdfImg);

//        Rectangle rectangle = new Rectangle(120, 730, 350, 80);
//        Canvas canvas1 = new Canvas(canvas, rectangle);
////        Canvas canvas1 = new Canvas(canvas, pdfDocument, rectangle);
//
//        Paragraph pBoutique = new Paragraph()
//                .add(factures.getUtilisateur().getNom()+" "+factures.getUtilisateur().getEmail())
//                .setFontSize(12).setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMargin(0)
//                .setMarginTop(3);
//        Paragraph ptelephone = new Paragraph()
//                .add("Tel: "+factures.getFiche().getPatient().getTelephone())
//                .setFontSize(11).setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMargin(0)
//                .setMarginTop(5);
//        Paragraph pLome = new Paragraph()
//                .add("Lomé - TOGO")
//                .setFontSize(13).setBold()
//                .setTextAlignment(TextAlignment.CENTER)
//                .setMargin(0)
//                .setMarginTop(3);
//        canvas1.add(pBoutique);
//        canvas1.add(ptelephone);
//        canvas1.add(pLome);
//        canvas1.close();

            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("FACTURE N°: " +factures.getNumero()+"/E2V/"+ Helpers.year()+"                                             Lomé, le "+Helpers.convertDate(factures.getCreatedAt())).setFontSize(12).setTextAlignment(TextAlignment.LEFT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);

            Paragraph paragraph2 = new Paragraph();
            paragraph2.add("Doit: ASSURANCE ................................... Pour le paiement des frais de ...............").setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED_ALL);
            paragraph2.add("........................................................................ De M./Mme/Enft : " +
                    factures.getFiche().getPatient().getNom() +
                    " " + factures.getFiche().getPatient().getPrenom()).setFontSize(12).setTextAlignment(TextAlignment.JUSTIFIED);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(20);

//        Paragraph paragraph3 = new Paragraph();
//        paragraph3.add("FACTURE N°: 0"+factures.getNumero()).setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER);
//        paragraph3.setMarginBottom(20);

            document.add(paragraph1);
            document.add(paragraph2);
//        document.add(paragraph3);

            Table table = new Table(4).setWidth(500);
            table.setMarginBottom(0);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
//

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

            Paragraph paragraph4 = new Paragraph();
            paragraph4.add("Remise: "+factures.getRemise()+" Fcfa \t\t\t\t Acompte: "+factures.getAcompte()+" Fcfa \t\t\t\t Reste: "+factures.getReste())
                    .setFontSize(12).setBold();
            paragraph4.setMarginLeft(45);
            paragraph4.setMarginRight(45);
            paragraph4.setMarginTop(30);
            paragraph4.setVerticalAlignment(VerticalAlignment.BOTTOM);

            Paragraph paragraph5 = new Paragraph();
            paragraph5.add("Arreté la présente facture à la somme de: "+arrete+" Fcfa")
                    .setFontSize(12);
            paragraph5.setMarginLeft(45);
            paragraph5.setMarginRight(45);
            paragraph5.setMarginTop(30);
            paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);

            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("Signature Client \t \t \t \t \t \t \t \t \t \t Signature responsable")
                    .setFontSize(12).setBold();
            paragraph6.setMarginLeft(45);
            paragraph6.setMarginRight(45);
            paragraph6.setVerticalAlignment(VerticalAlignment.BOTTOM);

            document.add(paragraph4);
            document.add(paragraph5);
            document.add(paragraph6);

            if (x < 2){
                pdfDocument.addNewPage();
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }

        }

        document.close();
        return file;
    }

    public static void myPdf(){
        try {

            PdfWriter writer = new PdfWriter(new FileOutputStream("/home/users/Documents/pdf/hello_world.pdf"));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);
            doc.add(new Paragraph("Hello World"));
            pdfDoc.addNewPage();
            doc.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
