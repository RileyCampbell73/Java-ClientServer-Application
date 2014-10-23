/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;
import dtos.ProductDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException; 
import java.text.DateFormat;  
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.Date;
import java.sql.Timestamp;
/**
 *
 * @author Riley4
 */
@Named(value = "purchaseOrderModel")  
@RequestScoped
public class PurchaseOrderModel {
    public PurchaseOrderModel() {
    }
    public String purchaseOrderAdd(double total, int vendorno, ArrayList<ProductDTO> items,DataSource ds){
        
         PreparedStatement pstmt;
        Connection con = null;
        ArrayList result = new ArrayList();
        java.util.Date utilDate = new Date();
        // Convert it to java.sql.Date
        java.sql.Date date = new java.sql.Date(utilDate.getTime());
        String msg = "";
        int poNum= 0;
        //dateFormat.format(date)); //2014/08/06 15:59:48

        String sql = "INSERT INTO purchaseOrders (VENDORNO,AMOUNT,PODATE)" + 
                "VALUES (?,?,?)";
       
        try{
            con = ds.getConnection();
            con.setAutoCommit(false);//needed for trans rollback
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, vendorno);
            pstmt.setDouble(2, total);
            pstmt.setDate(3, date);
            pstmt.execute();
            
             try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                poNum = rs.getInt(1);  
            }
             
             for (ProductDTO item: items){
                 if(item.getQuantity() > 0){
                    sql = "INSERT INTO PurchaseOrderLineItems (PONumber,ProdCD, QTY, Price) VALUES (?,?,?,?)";
                    pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstmt.setInt(1, poNum);
                    pstmt.setString(2, item.getProductcode());
                    pstmt.setInt(3, item.getQuantity());
                    pstmt.setDouble(4, item.getCostprice());
                    pstmt.execute();
                 }
             }
             
             
             con.commit();
             msg = "PO " + poNum + " Added!";
             con.close();
        }
         catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
            msg = "PO not added! - " + se.getMessage();
            try{
                con.rollback();
            } catch(SQLException sqx){
                System.out.println("Rollback failed - " + sqx.getMessage());
            }
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        //return the result set
        return msg;
    }
}
