package com.E_commerceApp.configs;

import com.E_commerceApp.constant.PredefinedCategory;
import com.E_commerceApp.constant.PredefinedProduct;
import com.E_commerceApp.constant.PredefinedRole;
import com.E_commerceApp.models.Category;
import com.E_commerceApp.models.Product;
import com.E_commerceApp.models.Role;
import com.E_commerceApp.models.User;
import com.E_commerceApp.repositories.CategoryRepository;
import com.E_commerceApp.repositories.ProductRepository;
import com.E_commerceApp.repositories.RoleRepository;
import com.E_commerceApp.repositories.UserRepository;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AppInitConfig {
    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_USER_PASSWORD = "admin";

    private static final Logger log = LoggerFactory.getLogger(AppInitConfig.class);

    private final PasswordEncoder passwordEncoder;

    public AppInitConfig(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository,
            CategoryRepository categoryRepository, ProductRepository productRepository) {
        log.info("Application started!");
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_USER_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
            }

            if (categoryRepository.findByName(PredefinedCategory.PHONE_CATEGORY).isEmpty()) {
                categoryRepository.save(Category.builder()
                        .name(PredefinedCategory.PHONE_CATEGORY)
                        .description("Điện thoại")
                        .build());
            }

            if (categoryRepository.findByName(PredefinedCategory.LAPTOP_CATEGORY).isEmpty()) {
                categoryRepository.save(Category.builder()
                        .name(PredefinedCategory.LAPTOP_CATEGORY)
                        .description("Laptop")
                        .build());
            }

            if (categoryRepository.findByName(PredefinedCategory.TABLET_CATEGORY).isEmpty()) {
                categoryRepository.save(Category.builder()
                        .name(PredefinedCategory.TABLET_CATEGORY)
                        .description("Tablet")
                        .build());
            }

            if (!productRepository.existsByName("Sony Xperia 1")) {
                Set<Category> categorySet = new HashSet<>();
                Optional<Category> mobilePhoneCat = categoryRepository.findByName("Điện thoại");
                mobilePhoneCat.ifPresent(category -> {
                    categorySet.add(mobilePhoneCat.get());
                });

                productRepository.save(Product.builder()
                        .name(PredefinedProduct.PRODUCT1)
                        .description("Điện thoại Sony Xperia 1")
                        .price(150000)
                        .quantity(100)
                        .categories(categorySet)
                        .imageUrl("https://res.cloudinary.com/dxxxhfhkp/image/upload/v1743324980/ecommerce/products/product_Sony_Xperia_1_1743324978655.webp")
                        .build());
            }
        };
    }
}
