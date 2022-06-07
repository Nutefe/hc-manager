package com.bluerizon.hcmanager.payload.genarate;

import com.bluerizon.hcmanager.payload.helper.Helpers;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.SneakyThrows;

public class HeaderPdf implements IEventHandler {

    @SneakyThrows
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        Rectangle pageSize = page.getPageSize();

        ImageData imageData = ImageDataFactory.create(Helpers.path_file+"entete_e2v.jpg");
        Image pdfImg = new Image(imageData);
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.getLastContentStream(), page.getResources(), pdf);
        Canvas canvas = new Canvas(pdfCanvas, pageSize);

        canvas.setFontSize(18f);
        //Write text at position
        canvas.showTextAligned("header",
                pageSize.getWidth() / 2,
                pageSize.getTop() - 30, TextAlignment.CENTER);
    }
}
