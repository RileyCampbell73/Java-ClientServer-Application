/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package resources;
import java.net.URI;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.ProductModel;
import dtos.ProductDTO;
import java.util.ArrayList;

/**
 *
 * @author Riley4
 */
@Path("product")
public class ProductResource {
     @Context
    private UriInfo context;
    
    //resources needs to be already defined in Glassfish
    @Resource(lookup = "jdbc/Info5059db")
    DataSource ds;

    /**
     * Creates a new instance of ProductResource
     */
    public ProductResource() {
    }
    
@POST
@Consumes("application/json")
public Response createProductFromJson(ProductDTO product){
    ProductModel model = new ProductModel();
    int msg = model.addProduct(product, ds);
    URI uri = context.getAbsolutePath();
    return Response.created(uri).entity(msg).build();
}
    
@GET
@Produces("application/json")
public ArrayList<ProductDTO> getProductJson(){
    ProductModel model = new ProductModel();
return model.getProducts(ds);
}

@GET
@Path("/{vendorno}")
@Produces("application/json")
public ArrayList<ProductDTO> getVendorProductsJson(@PathParam("vendorno") int vendorno){
    ProductModel model = new ProductModel();
    return model.getAllProductsForVendor(vendorno, ds);
}

@PUT
@Consumes("application/json")
public Response updateVendorFromJson(ProductDTO product){
        ProductModel model = new ProductModel();
        int numOfRowsUpdated = model.updateProduct(product, ds);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numOfRowsUpdated).build();
}

@DELETE
@Path("/{productcode}")
@Consumes("application/json")
public Response deleteVendorFromJson(@PathParam("productcode")String productcode){
    ProductModel model = new ProductModel();
    int numOfRowsDeleted = model.deleteProduct(productcode, ds);
    URI uri = context.getAbsolutePath();
    System.out.println("number of rows deleted " + numOfRowsDeleted);
    return Response.created(uri).entity(numOfRowsDeleted).build();
}
}
