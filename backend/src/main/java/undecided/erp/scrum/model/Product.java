package undecided.erp.scrum.model;

import java.util.List;
import undecided.erp.relMgmt.model.personRole.productOwner.ProductOwner;

/**
 * Represents a product.
 */
public class Product {

  private String name;
  private String description;
  private List<Feature> features;  // New field
  private ProductOwner owner;

}
