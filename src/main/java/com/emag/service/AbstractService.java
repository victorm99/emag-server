package com.emag.service;

import com.emag.exception.BadRequestException;
import com.emag.model.dao.ProductDAO;
import com.emag.model.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public abstract class AbstractService {

    protected static final String[] SORTED_BY = {"price_asc" , "price_desc" ,  "reviews" , "added_desc" };

    protected static final String[] GENDER = {"male", "female", "gay", "shemale", "transgender","m","f","g","t"};

    @Autowired
    protected ProductDAO productDAO;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected DiscountRepository discountRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderedProductRepository orderedProductRepository;

    @Autowired
    protected ProductImageRepository productImageRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected SubCategoryRepository subCategoryRepository;

    @Autowired
    protected CartRepository cartRepository;


    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

}
