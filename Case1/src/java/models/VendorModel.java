package models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named; 
import javax.sql.DataSource;  
import dtos.VendorDTO;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

/*
 * VendorModel.java
 *
 * Created on Aug 31, 2013, 3:03 PM
 *  Purpose:    Contains methods for supporting db access for vendor information
 *              Usually consumed by the ViewModel Class via DTO
 *  Revisions: 
 */
@Named(value = "vendorModel")  
@RequestScoped
public class VendorModel implements Serializable {

    public VendorModel() {
    }
    public int addVendor(VendorDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
         int vendorno = -1;

        String sql = "INSERT INTO Vendors (Address1,City,Province,PostalCode,"
                + "Phone,VendorType,Name,Email) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getAddress1());
            pstmt.setString(2, details.getCity());
            pstmt.setString(3, details.getProvince());
            pstmt.setString(4, details.getPostalCode());
            pstmt.setString(5, details.getPhone());
            pstmt.setString(6, details.getType());
            pstmt.setString(7, details.getName());
            pstmt.setString(8, details.getEmail());
            pstmt.execute();
           

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                vendorno = rs.getInt(1);
                
            }
            con.close();

//            if (vendorno > 0) {
//                msg = "Vendor " + vendorno + " Added!";
//            } else {
//                msg = "Vendor Not Added!";
//            }
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
        return vendorno;
    }
    
    public ArrayList getVendors(DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        ArrayList result = new ArrayList();

        String sql = "SELECT * FROM Vendors";
       
        try{
            //open connection
            System.out.println("before get connection11");
            con = ds.getConnection();
            System.out.println("after get connection");
            
            pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            //close connection
            con.close();
            //add the data from the result set into an VendorDTO object then into an arraylist
            while (rs.next()){
                VendorDTO row = new VendorDTO();
                row.setVendorno(rs.getInt("Vendorno"));
                row.setAddress1(rs.getString("ADDRESS1"));
                row.setCity(rs.getString("CITY"));
                row.setProvince(rs.getString("PROVINCE"));
                row.setPostalCode(rs.getString("POSTALCODE"));
                row.setPhone(rs.getString("PHONE"));
                row.setType(rs.getString("VENDORTYPE"));
                row.setName(rs.getString("NAME"));
                row.setEmail(rs.getString("EMAIL"));
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
        
    public int updateVendor(VendorDTO vendor,DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int updatedRow = 0;
        //prepare the update
        String sql = "UPDATE Vendors SET ADDRESS1=?, CITY=?, PROVINCE=?, POSTALCODE=?, PHONE=?, VENDORTYPE=?,NAME=?,EMAIL=?"
                + "WHERE VENDORNO=?";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            //build the statment
            pstmt.setString(1, vendor.getAddress1());
            pstmt.setString(2, vendor.getCity());
            pstmt.setString(3, vendor.getProvince());
            pstmt.setString(4, vendor.getPostalCode());
            pstmt.setString(5, vendor.getPhone());
            pstmt.setString(6, vendor.getType());
            pstmt.setString(7, vendor.getName());
            pstmt.setString(8, vendor.getEmail());
            pstmt.setString(9, Integer.toString(vendor.getVendorno())); 
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
     public int deleteVendor(int vendorno,DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int deletedRow = 0;
        //prepare the update
        String sql = "DELETE FROM Vendors "
                + "WHERE VENDORNO=?";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            //build the statment
            pstmt.setString(1, Integer.toString(vendorno)); 
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