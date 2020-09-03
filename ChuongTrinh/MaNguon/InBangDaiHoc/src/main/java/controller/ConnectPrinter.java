package controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

public class ConnectPrinter {
    public boolean printPaper(String filePath) throws PrinterException, IOException {
        PDDocument document = PDDocument.load(new File(filePath));

        java.awt.print.PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        if (job.printDialog())
        {
            job.print();
            System.out.println("Printed to paper");
            document.close();
            return true;
        }else{
            System.out.println("Not printed");
            document.close();
            return false;
        }

    }
}
