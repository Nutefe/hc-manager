package com.bluerizon.hcmanager.payload.genarate;


import com.bluerizon.hcmanager.models.Etats;
import com.bluerizon.hcmanager.models.FicheTraitements;
import com.bluerizon.hcmanager.models.Patients;
import com.bluerizon.hcmanager.models.TypeTraitements;
import com.bluerizon.hcmanager.payload.helper.FrenchNumber;
import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.bluerizon.hcmanager.payload.response.EtatFactureEntreprise;
import com.bluerizon.hcmanager.payload.response.EtatFacturePatient;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
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

import static com.bluerizon.hcmanager.payload.helper.Helpers.convertFr;

public class GenarateEtat {
    public static File etatPatient(List<EtatFacturePatient> facturePatients, Etats etat) throws IOException {

        File file = new File(Helpers.path_file+etat.getFileName());
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
            paragraph1.add("Lomé, le "+Helpers.convertDate(etat.getCreatedAt())).setFontSize(12).setTextAlignment(TextAlignment.RIGHT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);
            paragraph1.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            // paragraphe designation de la facture
            Paragraph paragraph2 = new Paragraph();
            String date = convertFr(etat.getDateFin()).toUpperCase();

            paragraph2.add("ETAT RECAPITULATIF DU MOIS DE "+date+" DES FACTURES DE "+etat.getAssurance()
                    .getLibelle()).setFontSize(13).setUnderline().setBold().setTextAlignment(TextAlignment.CENTER);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(20);
            paragraph2.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(paragraph1);
            document.add(paragraph2);

            // ajout du tableau a 4 columns
            Table table = new Table(6).setWidth(500);
            table.setMarginBottom(0);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // creation de l'entete du tableau
            Cell[] headerFooter = new Cell[]{
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("NOMS ET PRENOMS").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("N° DE FACTURE").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("DATE DE L'ACTE").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("DESIGNATION").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("MONTANT PAYER PAR L'ASSURE").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("MONTANT A PAYER PAR L'ASSURANCE").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
            };

            for (Cell hfCell : headerFooter) {
                table.addHeaderCell(hfCell);
            }

            // ajout dynamiquement de donnees des differente celuile
            Double totalAssureur = 0.0, totalAssurer = 0.0;
            for (int counter = 0; counter < facturePatients.size(); counter++) {
                EtatFacturePatient ligneRequest = facturePatients.get(counter);
                String designation = ""; Double mtnPatient =0.0, mtnAssureur = 0.0;
                for (TypeTraitements type :
                        ligneRequest.getTypes()) {
                    designation += " "+type.getLibelle();
                }
                for (FicheTraitements ft :
                        ligneRequest.getFiches()) {
                    mtnPatient += ft.getNetPayBeneficiaire();
                    mtnAssureur += ft.getNetPayAssu();
                }
                totalAssureur += mtnAssureur;
                totalAssurer += mtnPatient;

                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFacture().getFiche().getPatient().getNom()+" "+ligneRequest.getFacture().getFiche().getPatient().getPrenom())).setFontSize(11));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFacture().getNumero()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(Helpers.convertDate(ligneRequest.getFacture().getDateFacture())+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(designation+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(mtnPatient+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(mtnAssureur+"").setFontSize(11)));
            }
            table.addCell(new Cell().addStyle(new Style().setBorderRight(Border.NO_BORDER))
                    .setTextAlignment(TextAlignment.LEFT).add(new Paragraph("TOTAL").setFontSize(12).setBold()));
            table.addCell(new Cell().addStyle(new Style().setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(new Paragraph("")));
            table.addCell(new Cell().addStyle(new Style().setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(new Paragraph("")));
            table.addCell(new Cell().addStyle(new Style().setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER)).setTextAlignment(TextAlignment.LEFT).add(new Paragraph("")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalAssurer+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalAssureur+"").setFontSize(12)));

            document.add(table);

            Paragraph paragraph5 = new Paragraph();

            paragraph5.add("Arreté la présente facture à la somme de: "+ FrenchNumber.convert(Math.round(totalAssureur))+"("+totalAssureur+") Fcfa")
                    .setFontSize(12);
            paragraph5.setMarginLeft(45);
            paragraph5.setMarginRight(45);
            paragraph5.setMarginTop(30);
            paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);

            // paragraphe signature
            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("Le Directeur / opticien")
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
    public static File etatEntreprise(List<EtatFactureEntreprise> factureEntreprises, Etats etat) throws IOException {

        File file = new File(Helpers.path_file+etat.getFileName()+".pdf");
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
            paragraph1.add("Lomé, le "+Helpers.convertDate(etat.getCreatedAt())).setFontSize(12).setTextAlignment(TextAlignment.RIGHT);
            paragraph1.setMarginLeft(45);
            paragraph1.setMarginRight(45);
            paragraph1.setMarginTop(20);
            paragraph1.setHorizontalAlignment(HorizontalAlignment.RIGHT);

            String date = convertFr(etat.getDateFin()).toUpperCase();

            // paragraphe designation de la facture
            Paragraph paragraph2 = new Paragraph();
            paragraph2.add("ETAT RECAPITULATIF DU MOIS DE "+date+" DES FACTURES DE "+etat.getAssurance()
                    .getLibelle()).setFontSize(13).setUnderline().setBold().setTextAlignment(TextAlignment.CENTER);
            paragraph2.setMarginLeft(45);
            paragraph2.setMarginRight(45);
            paragraph2.setMarginBottom(20);
            paragraph2.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(paragraph1);
            document.add(paragraph2);

            // ajout du tableau a 4 columns
            Table table = new Table(6).setWidth(500);
            table.setMarginBottom(0);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // creation de l'entete du tableau
            Cell[] headerFooter = new Cell[]{
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("NOMS DES SOCIETES").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("NOMBRE DE FACTURE").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("NOMBRE D'ASSURES").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("MONTANT TOTAL DES PRESTATIONS").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("MONTANT TOTAL REGLE PAR LES ASSURES").setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
                    new Cell()
                            .setBackgroundColor(new DeviceGray(0.75f))
                            .add(new Paragraph("MONTANT TOTAL A REGLE PAR "+etat.getAssurance().getLibelle()).setFontSize(12).setBold())
                            .setTextAlignment(TextAlignment.CENTER),
            };

            for (Cell hfCell : headerFooter) {
                table.addHeaderCell(hfCell);
            }

            // ajout dynamiquement de donnees des differente celuile
            Double totalAssureur = 0.0, totalAssurer = 0.0, totalPrestation=0.0; Integer nrbFacture =0; Long nbrPatient=0L;
            for (int counter = 0; counter < factureEntreprises.size(); counter++) {
                EtatFactureEntreprise ligneRequest = factureEntreprises.get(counter);
                List<Patients> patients = new ArrayList<>();
                Double totalAssureur1 = 0.0, totalAssurer1 = 0.0;
                for (EtatFacturePatient eta :
                        ligneRequest.getFacturePatients()) {
                    Double mtnPatient =0.0, mtnAssureur = 0.0;
                    if(!patients.contains(eta.getFacture().getFiche().getPatient()))
                        patients.add(eta.getFacture().getFiche().getPatient());

                    for (FicheTraitements ft :
                            eta.getFiches()) {
                        mtnPatient += ft.getNetPayBeneficiaire();
                        mtnAssureur += ft.getNetPayAssu();
                    }
                    totalAssureur1 += mtnAssureur;
                    totalAssurer1 += mtnPatient;
                }


                totalAssureur += totalAssureur1;
                totalAssurer += totalAssurer1;

                nbrPatient = patients.stream().distinct().count();
                nrbFacture = ligneRequest.getFacturePatients().size();

                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getEntreprise().getRaisonSocial())).setFontSize(11));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(ligneRequest.getFacturePatients().size()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(patients.stream().distinct().count()+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph((totalAssureur1+totalAssurer1)+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(totalAssurer1+"").setFontSize(11)));
                table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER)
                        .add(new Paragraph(totalAssureur1+"").setFontSize(11)));

                totalPrestation = totalAssurer + totalAssureur;
            }
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph("TOTAL").setFontSize(12).setBold()));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(nrbFacture+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(nbrPatient+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalPrestation+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalAssurer+"")));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(totalAssureur+"").setFontSize(12)));

            document.add(table);

            Paragraph paragraph5 = new Paragraph();

            paragraph5.add("Arreté la présente facture à la somme de: "+ FrenchNumber.convert(Math.round(totalAssureur))+"("+totalAssureur+") Fcfa")
                    .setFontSize(12);
            paragraph5.setMarginLeft(45);
            paragraph5.setMarginRight(45);
            paragraph5.setMarginTop(30);
            paragraph5.setVerticalAlignment(VerticalAlignment.BOTTOM);

            // paragraphe signature
            Paragraph paragraph6 = new Paragraph();
            paragraph6.add("Le Directeur / opticien")
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

}
