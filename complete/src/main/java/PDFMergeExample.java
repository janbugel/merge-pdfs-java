import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFMergeExample {
    static void mergePdfFiles(List<InputStream> inputPdfList, OutputStream outputStream) throws Exception {
        Document document = new Document();
        List<PdfReader> readers = new ArrayList<PdfReader>();
        Iterator<InputStream> pdfIterator = inputPdfList.iterator();

        while (pdfIterator.hasNext()) {
            InputStream pdf = pdfIterator.next();
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
        }

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();
        PdfContentByte pageContentByte = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }

        outputStream.flush();
        document.close();
        outputStream.close();

        System.out.println("Pdf files merged successfully.");
    }

    public static void main(String args[]) {
        try {
            List<InputStream> inputPdfList = new ArrayList<InputStream>();
            inputPdfList.add(new FileInputStream("/Users/asdf/Documents/pdfs/Bevis_Diploma.PDF"));
            inputPdfList.add(new FileInputStream("/Users/asdf/Documents/pdfs/Jan-Bugel-Zivotopis.pdf"));

            OutputStream outputStream = new FileOutputStream("/Users/asdf/Documents/pdfs/MergeFile.pdf");

            mergePdfFiles(inputPdfList, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
