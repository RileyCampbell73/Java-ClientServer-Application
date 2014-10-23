

package dtos;

/**
 *
 * @author Riley Campbell
 * @date 10/17/2014
 * @file PurchaseOrderDTO.java
 * @purpose to serve as a data object between layers
 */
import java.io.Serializable;
import java.util.ArrayList;


public class PurchaseOrderDTO implements Serializable{
    
     public PurchaseOrderDTO() {}
         double total;
         int vendorno;
         ArrayList<ProductDTO> items;

    public double getTotal() {
        
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getVendorno() {
        return vendorno;
    }

    public void setVendorno(int vendorno) {
        this.vendorno = vendorno;
    }

    public ArrayList<ProductDTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<ProductDTO> items) {
        this.items = items;
    }
         
         
    
     
}
