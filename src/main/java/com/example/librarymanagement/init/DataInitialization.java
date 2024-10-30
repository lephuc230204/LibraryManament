package com.example.librarymanagement.init;

import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Category;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.model.entity.Book.Status;
import com.example.librarymanagement.model.entity.Role;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.CategoryRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.repository.RoleRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitialization {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void initData() {
        // Tạo Role
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        // Tạo Category
        Category category1 = new Category();
        category1.setCategoryName("Science Fiction");
        Category category2 = new Category();
        category2.setCategoryName("Fantasy");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // Tạo Book
        Book book1 = new Book();
        book1.setBookName("Dune");
        book1.setQuantity(10L);
        book1.setCategory(category1);
        book1.setPublisher("Penguin Random House");
        book1.setPostingDate(LocalDate.of(2021, 1, 1));
        book1.setStatus(Status.AVAILABLE);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setBookName("The Hobbit");
        book2.setQuantity(5L);
        book2.setCategory(category2);
        book2.setPublisher("HarperCollins");
        book2.setPostingDate(LocalDate.of(2022, 6, 10));
        book2.setStatus(Status.BORROWED);
        bookRepository.save(book2);

        // Tạo User
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("123456"); // mã hóa mật khẩu khi lưu vào thực tế
        adminUser.setRole(adminRole);
        adminUser.setCreatedDate(LocalDate.now());
        adminUser.setStatus("ACTIVE");
        userRepository.save(adminUser);

        User regularUser = new User();
        regularUser.setUsername("user");
        regularUser.setEmail("user@example.com");
        regularUser.setPassword("12"); // mã hóa mật khẩu khi lưu vào thực tế
        regularUser.setRole(userRole);
        regularUser.setCreatedDate(LocalDate.now());
        regularUser.setStatus("ACTIVE");
        userRepository.save(regularUser);
    }
}
