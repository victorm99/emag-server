package com.emag.model.dao;

import com.emag.model.dto.product.FilterProductsDTO;
import com.emag.model.dto.product.ResponseProductDTO;
import com.emag.model.pojo.*;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ModelMapper modelMapper;

    @SneakyThrows
    public List<ResponseProductDTO> filter(FilterProductsDTO dto) {
        StringBuilder query = new StringBuilder("SELECT p.id, p.name, p.brand, p.model, p.price, p.discounted_price, " +
                "p.description, p.quantity, p.warranty_months, p.added_at, p.deleted_at, p.product_rating, \n" +
                "sc.id AS sub_category_id, sc.subcategory_name, c.id AS category_id, c.category_name, d.id AS discount_id, d.discount_percent, d.start_date, d.expire_date,\n" +
                "pi.id AS product_image_id, pi.url\n" +
                "FROM products AS p\n" +
                "JOIN sub_categories AS sc ON (p.sub_category_id = sc.id)\n" +
                "JOIN category AS c ON (sc.category_id = c.id)\n" +
                "LEFT JOIN discounts AS d ON (p.discounts_id = d.id)\n" +
                "LEFT JOIN products_images AS pi ON (p.id = pi.product_id)\n" +
                "WHERE p.deleted_at IS NULL\n" +
                "AND ");
        StringBuilder queryParams = new StringBuilder();
        List<Integer> productsPerPageParams = new ArrayList<>();
        Long subcategoryId = dto.getSubcategoryId();
        if (subcategoryId != null){
            query.append("sub_category_id = ? AND ");
            queryParams.append(subcategoryId.toString()).append(",");
        }
        String keyword = dto.getSearchKeyword();
        if (keyword != null && !keyword.trim().equals("")){
            query.append("(name LIKE ? OR description LIKE ?) AND ");
            queryParams.append("%").append(keyword).append("%").append(",");
            queryParams.append("%").append(keyword).append("%").append(",");
        }
        String brand = dto.getBrand();
        if (brand != null && !brand.trim().equals("")){
            query.append("brand LIKE ? AND ");
            queryParams.append("%").append(brand).append("%").append(",");
        }
        String model = dto.getModel();
        if (model != null && !model.trim().equals("")){
            query.append("model LIKE ? AND ");
            queryParams.append("%").append(model).append("%").append(",");
        }
        Double maxPrice = dto.getMaxPrice();
        if (maxPrice != null && maxPrice > 0){
            query.append("IF(discounted_price = 0, price , discounted_price) <= ? AND ");
            queryParams.append(maxPrice.toString()).append(",");
        }
        Double minPrice = dto.getMinPrice();
        if (minPrice != null && minPrice >= 0){
            query.append("IF(discounted_price = 0,price, discounted_price ) >= ? AND ");
            queryParams.append(minPrice.toString()).append(",");
        }
        Boolean discountedOnly = dto.getDiscountedOnly();
        if (discountedOnly != null && discountedOnly){
            query.append("discounts_id IS NOT NULL ");
        }
        //removes the last AND if it is not necessary
        if (query.substring(query.length() - 4).equals("AND ")){
            query = new StringBuilder(query.substring(0, query.length() - 4));
        }
        Boolean orderByPrice = dto.getOrderByPrice();
        if (orderByPrice != null && orderByPrice){
            query.append("ORDER BY IF(discounted_price IS NOT NULL, discounted_price, price) ");
            Boolean sortDesc = dto.getSortDesc();
            if (sortDesc != null && sortDesc){
                query.append("DESC ");
            }
        }
        Integer productsPerPage = dto.getProductsPerPage();
        if (productsPerPage != null && productsPerPage >= 0){
            query.append("LIMIT ? ");
            productsPerPageParams.add(productsPerPage);
            Integer pageNumber = dto.getPageNumber();
            if (pageNumber != null && pageNumber > 0){
                query.append("OFFSET ?");
                int offset = (pageNumber - 1) * productsPerPage;
                productsPerPageParams.add(offset);
            }
        }
        query.append(";");

        List<String> splitParams = new ArrayList<>();
        if (queryParams.length() != 0){
            splitParams.addAll(Arrays.asList(queryParams.toString().split(",")));
        }

        try(Connection connection = jdbcTemplate.getDataSource().getConnection() ;
            PreparedStatement statement = connection.prepareStatement(query.toString())){
            int paramsCount = 0;
            if (!splitParams.isEmpty()) {
                for (int i = 0; i < splitParams.size(); i++) {
                    statement.setString(i + 1, splitParams.get(i));
                    paramsCount++;
                }
            }
            if (!productsPerPageParams.isEmpty()){
                for (Integer param : productsPerPageParams) {
                    statement.setInt(++paramsCount, param);
                }
            }
            ResultSet result = statement.executeQuery();
            List<ResponseProductDTO> foundProducts = new ArrayList<>();

            while(result.next()){
                Category category = new Category();
                category.setId(result.getLong("category_id"));
                category.setCategoryName(result.getString("category_name"));

                SubCategory subCategory = new SubCategory();
                subCategory.setId(result.getLong("sub_category_id"));
                subCategory.setCategory(category);
                subCategory.setSubcategoryName(result.getString("subcategory_name"));

                Product product = new Product();
                product.setId(result.getLong("id"));
                product.setName(result.getString("name"));
                product.setSubCategory(subCategory);
                product.setBrand(result.getString("brand"));
                product.setModel(result.getString("model"));
                product.setPrice(result.getDouble("price"));
                product.setDiscountedPrice(result.getDouble("discounted_price"));
                product.setDescription(result.getString("description"));
                product.setQuantity(result.getInt("quantity"));
                product.setWarrantyMonths(result.getInt("warranty_months"));
                product.setAddedAt(result.getTimestamp("added_at"));
                product.setDeletedAt(result.getTimestamp("deleted_at"));
                product.setProductRating(result.getDouble("product_rating"));

                if (result.getString("discount_id" ) != null) {
                    Discount discount = new Discount();
                    discount.setId(result.getLong("discount_id"));
                    discount.setDiscountPercent(result.getInt("discount_percent"));
                    discount.setStartDate(result.getTimestamp("start_date"));
                    discount.setExpireDate(result.getTimestamp("expire_date"));
                    product.setDiscount(discount);
                }

                ProductImage productImage = null;
                if (result.getString("product_image_id") != null){
                    productImage = new ProductImage();
                    productImage.setId(result.getLong("product_image_id"));
                    productImage.setUrl(result.getString("url"));
                    productImage.setProduct(product);
                }


                ResponseProductDTO responseProductDTO = modelMapper.map(product, ResponseProductDTO.class);
                if(!foundProducts.stream()
                        .map(ResponseProductDTO::getId)
                        .collect(Collectors.toList())
                        .contains(responseProductDTO.getId())){
                    if (productImage != null) {
                        responseProductDTO.setProductImages(new ArrayList<>());
                        responseProductDTO.getProductImages().add(productImage);
                    }
                    foundProducts.add(responseProductDTO);
                }else{
                    ResponseProductDTO productDTO = foundProducts.get(foundProducts.stream()
                            .map(ResponseProductDTO::getId)
                            .collect(Collectors.toList())
                            .indexOf(responseProductDTO.getId()));
                    productDTO.getProductImages().add(productImage);
                }
            }
            return foundProducts;
        }

    }
}