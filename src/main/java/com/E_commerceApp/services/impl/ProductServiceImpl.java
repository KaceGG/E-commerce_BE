package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.request.ProductCreationRequest;
import com.E_commerceApp.DTOs.request.ProductUpdateRequest;
import com.E_commerceApp.DTOs.response.ProductResponse;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.mappers.ProductMapper;
import com.E_commerceApp.models.Category;
import com.E_commerceApp.models.Product;
import com.E_commerceApp.repositories.CategoryRepository;
import com.E_commerceApp.repositories.ProductRepository;
import com.E_commerceApp.services.ProductService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final Cloudinary cloudinary;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductMapper productMapper, Cloudinary cloudinary, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.cloudinary = cloudinary;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse getProduct(int productId) {
        return productMapper.toProductResponse(productRepository.findById(productId).orElseThrow(()
                -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse).toList();
    }

    @Override
    public List<ProductResponse> getProductByCategory(int categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(productMapper::toProductResponse).toList();
    }

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        Product product = productMapper.toProduct(request);

        // Upload ảnh lên Cloudinary và gán imageUrl
        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                var uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "ecommerce/products",
                                "public_id", "product_" + request.getName().replace(" ", "_") + "_" + System.currentTimeMillis(),
                                "overwrite", true));
                String imageUrl = (String) uploadResult.get("secure_url");
                product.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        // Lấy danh sách Category từ categoryIds
        Set<Category> categories = request.getCategoryIds().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)))
                .collect(Collectors.toSet());
        product.setCategories(categories);

        // Lưu vào DB
        Product savedProduct = productRepository.save(product);

        // Chuyển đổi sang DTO trả về
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(int productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId).orElseThrow(()
                -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        boolean existed = productRepository.existsByNameAndIdNot(request.getName(), productId);
        if (existed) throw new AppException(ErrorCode.PRODUCT_EXISTED);

        productMapper.updateProduct(request, product);

        // Xử lý upload ảnh nếu có
        MultipartFile image = request.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                log.info("Uploading new image for product: {}", product.getName());
                var uploadResult = cloudinary.uploader().upload(image.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "ecommerce/products",
                                "public_id", "product_" + product.getName().replace(" ", "_") + "_" + System.currentTimeMillis(),
                                "overwrite", true));
                String imageUrl = (String) uploadResult.get("secure_url");
                product.setImageUrl(imageUrl);
                log.info("Image uploaded successfully: {}", imageUrl);
            } catch (IOException e) {
                log.error("Failed to upload image", e);
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        // Cập nhật danh sách Category nếu có
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            log.info("Updating categories for product: {}", request.getCategoryIds());
            Set<Category> categories = request.getCategoryIds().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
            log.info("Updated categories: {}", categories);
        }

        // Lưu sản phẩm đã cập nhật
        Product updatedProduct = productRepository.save(product);
        log.info("Updated product with ID: {}", updatedProduct.getId());
        return productMapper.toProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(int productId) {
        Product product = productRepository.findById(productId).orElseThrow(()
                -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }
}
