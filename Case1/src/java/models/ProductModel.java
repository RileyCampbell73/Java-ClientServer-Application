/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;
import dtos.ProductDTO;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException; 
import java.util.ArrayList;  
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 *
 * @author Riley4
 */
@Named(value = "productModel")  
@RequestScoped
public class ProductModel implements Serializable {
    public ProductModel() {
    }
    
       public ArrayList getAllProductsForVendor(int vendorno,DataSource ds){
        PreparedStatement pstmt;
        Connection con = null;
        ArrayList result = new ArrayList();

        String sql = "SELECT * FROM PRODUCTS WHERE VENDORNO = ?";
       
        try{
            //open connection
            System.out.println("before get connection11");
            con = ds.getConnection();
            System.out.println("after get connection");
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Integer.toString(vendorno));
            ResultSet rs = pstmt.executeQuery();
            
            //close connection
            con.close();
            //add the data from the result set into an ProductDTO object then into an arraylist
            while (rs.next()){
                ProductDTO row = new ProductDTO();
                row.setProductcode(rs.getString("productcode"));
                row.setVendorno(rs.getInt("vendorno"));
                row.setVendorsku(rs.getString("vendorsku"));
                row.setProductname(rs.getString("productname"));
                row.setCostprice(rs.getDouble("costprice"));
                row.setMsrp(rs.getDouble("msrp"));
                row.setRop(rs.getInt("rop"));
                row.setEoq(rs.getInt("eoq"));
                row.setQoh(rs.getInt("qoh"));
                row.setQoo(rs.getInt("qoo"));
                result.add(row);
            }
        }
         catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
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
        return result;
       }
       
       public ArrayList getProducts(DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        ArrayList result = new ArrayList();

        String sql = "SELECT * FROM Products";
       
        try{
            //open connection
            System.out.println("before get connection11");
            con = ds.getConnection();
            System.out.println("after get connection");
            
            pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            //close connection
            con.close();
            //add the data from the result set into an ProductDTO object then into an arraylist
            while (rs.next()){
                ProductDTO row = new ProductDTO();
                row.setProductcode(rs.getString("productcode"));
                row.setVendorno(rs.getInt("vendorno"));
                row.setVendorsku(rs.getString("vendorsku"));
                row.setProductname(rs.getString("productname"));
                row.setCostprice(rs.getDouble("costprice"));
                row.setMsrp(rs.getDouble("msrp"));
                row.setRop(rs.getInt("rop"));
                row.setEoq(rs.getInt("eoq"));
                row.setQoh(rs.getInt("qoh"));
                row.setQoo(rs.getInt("qoo"));
                result.add(row);
            }
        }
         catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
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
        return result;
    }
       public int addProduct(ProductDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int productno = -1;

        String sql = "INSERT INTO Products (Productcode,Vendorno,Vendorsku,Productname,"
                + "CostPrice,MSRP,ROP,EOQ,QOH,QOO) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getProductcode());
            pstmt.setString(2, Integer.toString(details.getVendorno()));
            pstmt.setString(3, details.getVendorsku());
            pstmt.setString(4, details.getProductname());
            pstmt.setString(5, Double.toString(details.getCostprice()));
            pstmt.setString(6, Double.toString(details.getMsrp()));
            pstmt.setString(7, Integer.toString(details.getRop()));
            pstmt.setString(8, Integer.toString(details.getEoq()));
            pstmt.setString(9, Integer.toString(details.getQoh()));
            pstmt.setString(10, Integer.toString(details.getQoo()));
            productno = pstmt.executeUpdate();
            con.close();
            
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return productno;
    }
       
       public int updateProduct(ProductDTO product,DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int updatedRow = 0;
        //prepare the update

        String sql = "UPDATE Products SET Vendorno=?, Vendorsku=?, Productname=?, CostPrice=?, MSRP=?, ROP=?,EOQ=?,QOH=?,QOO=?"
                + "WHERE PRODUCTCODE=?";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            //build the statment
            pstmt.setString(1, Integer.toString(product.getVendorno()));
            pstmt.setString(2, product.getVendorsku());
            pstmt.setString(3, product.getProductname());
            pstmt.setString(4, Double.toString(product.getCostprice()));
            pstmt.setString(5, Double.toString(product.getMsrp()));
            pstmt.setString(6, Integer.toString(product.getRop()));
            pstmt.setString(7, Integer.toString(product.getEoq()));
            pstmt.setString(8, Integer.toString(product.getQoh()));
            pstmt.setString(9, Integer.toString(product.getQoo()));
            pstmt.setString(10, product.getProductcode()); 
            updatedRow = pstmt.executeUpdate();

        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
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
        //return the updated row
        return updatedRow ; 
    }
    public int deleteProduct(String productcode,DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int deletedRow = 0;
        //prepare the update
        String sql = "DELETE FROM Products "
                + "WHERE PRODUCTCODE=?";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            //build the statment
            pstmt.setString(1, productcode); 
            deletedRow = pstmt.executeUpdate();

        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
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
        //return the updated row
        return deletedRow ; 
     }
}
