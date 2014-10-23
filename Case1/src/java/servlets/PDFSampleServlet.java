package servlets;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Evan
 */
@WebServlet(name = "PDFSampleServlet", urlPatterns = {"/PDFSample"})
public class PDFSampleServlet extends HttpServlet {
          @Resource(lookup = "jdbc/Info5059db")
          DataSource ds;
          int poNumb;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");


        try {
            //poNumb = Integer.parseInt(request.getParameter("po"));
            poNumb = Integer.parseInt(request.getParameter("po"));
            buildpdf(response);
            
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }    
    }
    private void buildpdf(HttpServletResponse response) {
        Font catFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font small = new Font(Font.FontFamily.HELVETICA, 12);
        String IMG = getServletContext().getRealPath("/img/logo.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        
        
  
        PreparedStatement pstmt;
        Connection con = null;
        
        DecimalFormat df = new DecimalFormat("#.00");
        double total = 0;
        
        String sql = "SELECT * " +
"FROM PurchaseOrders " +
"INNER JOIN PurchaseOrderLineItems\n" +
"ON PurchaseOrders.PONUMBER=PurchaseOrderLineItems.PONUMBER " +
"INNER JOIN vendors " +
"on PurchaseOrders.vendorno=PurchaseOrders.vendorno " +
"INNER JOIN products " +
"on products.PRODUCTCODE=PurchaseOrderLineItems.PRODCD " +
"WHERE PurchaseOrders.PONUMBER = ? AND vendors.vendorno = PurchaseOrders.vendorno AND products.PRODUCTCODE = PurchaseOrderLineItems.PRODCD";
        
        try {
            //open connection
            System.out.println("before get connection11");
            con = ds.getConnection();
            System.out.println("after get connection");
            
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, poNumb);
            ResultSet rs = pstmt.executeQuery();
            
            //close connection
            con.close();
            
            rs.next();
            double dTotal = rs.getDouble("AMOUNT");
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph preface = new Paragraph();
            // We add one empty line
            Image image1 = Image.getInstance(IMG);
            image1.setAbsolutePosition(55f, 760f);
            preface.add(image1);
            preface.setAlignment(Element.ALIGN_RIGHT);
            // Lets write a big header
            Paragraph mainHead = new Paragraph(String.format("%55s", "PURCHASE ORDER"), catFont);
            preface.add(mainHead);
            preface.add(new Paragraph(String.format("%87s", "PO#: " + poNumb), subFont));
            addEmptyLine(preface, 2);
            preface.add(new Paragraph(String.format("%1s", "Vendor: "), smallBold));

            preface.add(new Paragraph(String.format("%1s", rs.getString("NAME") + "\n" + rs.getString("ADDRESS1")+ "\n" + rs.getString("CITY") + ", " + rs.getString("PROVINCE") + "\n" + rs.getString("POSTALCODE")), small));
//            preface.add(new Paragraph(String.format("%20s", rs.getString("ADDRESS1"))));
//            preface.add(new Paragraph(String.format("%20s", rs.getString("CITY") + ", " + rs.getString("PROVINCE"))));
//            preface.add(new Paragraph(String.format("%20s", rs.getString("POSTALCODE"))));
            
            addEmptyLine(preface, 1);
            // 4 column table
            PdfPTable table = new PdfPTable(5);
            PdfPCell cell = new PdfPCell(new Paragraph("Product Code", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Product Description", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Quantity Sold", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Ext Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            
            do{
            cell = new PdfPCell(new Phrase(rs.getString("PRODCD")));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(rs.getString("PRODUCTNAME")));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(rs.getString("QTY")));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("$" + rs.getString("PRICE")));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("$" + String.valueOf(df.format(rs.getDouble("CostPrice") * rs.getInt("QTY")))));
            total += rs.getDouble("MSRP") * rs.getInt("QTY");
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            }while(rs.next());
            
            cell = new PdfPCell(new Phrase("Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("$" + String.valueOf(df.format(total))));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);            
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase(new Phrase("Tax:")));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("$" + String.valueOf(df.format(total * 0.13))));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);           
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Order Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
             
            cell = new PdfPCell(new Phrase("$" + String.valueOf(df.format(dTotal))));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBackgroundColor(BaseColor.YELLOW);            
            table.addCell(cell);
            
            preface.add(table);
            addEmptyLine(preface, 3);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.add(new Paragraph(String.format("%60s", new Date()), subFont));
            document.add(preface);
            document.close();

            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Disposition", "attachment; filename=\"PurchaseOrder" + poNumb + ".pdf\"");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());

            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
            
        }

   private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}