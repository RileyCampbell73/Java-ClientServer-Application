package dtos;
/**
 * ProductDTO - Container class that serializes vendor information traveling 
 * between ViewModel and Model classes
 */
import java.io.Serializable;
/**
 *
 * @author Riley4
 */

public class ProductDTO implements Serializable{
    public ProductDTO() {
    }
    String productcode;
    int vendorno;
    String vendorsku;
    String productname;
    double costprice;
    double msrp;
    int rop;
    int eoq;
    int qoh;
    int qoo;
    String tempproductcode; 
    int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTempproductcode() {
        return tempproductcode;
    }

    public void setTempproductcode(String tempproductcode) {
        this.tempproductcode = tempproductcode;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public int getVendorno() {
        return vendorno;
    }

    public void setVendorno(int vendorno) {
        this.vendorno = vendorno;
    }

    public String getVendorsku() {
        return vendorsku;
    }

    public void setVendorsku(String vendorsku) {
        this.vendorsku = vendorsku;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public double getCostprice() {
        return costprice;
    }

    public void setCostprice(double costprice) {
        this.costprice = costprice;
    }

    public double getMsrp() {
        return msrp;
    }

    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }

    public int getRop() {
        return rop;
    }

    public void setRop(int rop) {
        this.rop = rop;
    }

    public int getEoq() {
        return eoq;
    }

    public void setEoq(int eoq) {
        this.eoq = eoq;
    }

    public int getQoh() {
        return qoh;
    }

    public void setQoh(int qoh) {
        this.qoh = qoh;
    }

    public int getQoo() {
        return qoo;
    }

    public void setQoo(int qoo) {
        this.qoo = qoo;
    }

   
}
