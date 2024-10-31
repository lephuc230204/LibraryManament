package com.example.librarymanagement.init;

import com.example.librarymanagement.model.entity.*;
import com.example.librarymanagement.model.entity.Book.Status;
import com.example.librarymanagement.repository.*;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitialization {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorRepository authorRepository;
    private final CrackRepository crackRepository;

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

        // Tạo Author
        Author author1 = new Author();
        author1.setName("Frank Herbert");
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setName("J.R.R. Tolkien");
        authorRepository.save(author2);

        // Tạo Crack và lưu vào database
        List<Crack> cracks = new ArrayList<>();

        for (int shelfNumber = 1; shelfNumber <= 2; shelfNumber++) {           // 2 kệ
            for (int rowNumber = 1; rowNumber <= 3; rowNumber++) {              // Mỗi kệ có 3 hàng
                for (int slotNumber = 1; slotNumber <= 4; slotNumber++) {       // Mỗi hàng có 4 ô
                    Crack crack = new Crack();
                    crack.setShelf("Shelf " + shelfNumber);
                    crack.setRow(rowNumber);
                    crack.setSlot(slotNumber);
                    cracks.add(crack);
                }
            }
        }

        // Lưu tất cả các đối tượng Crack vào database
        crackRepository.saveAll(cracks);

        // Giả sử sử dụng Crack đầu tiên cho book1 và Crack thứ hai cho book2
        Crack crack1 = cracks.get(0);
        Crack crack2 = cracks.get(1);

        // Tạo Book và gán tác giả cùng với Crack
        Book book1 = new Book();
        book1.setBookName("Dune");
        book1.setQuantity(10L);
        book1.setCategory(category1);
        book1.setPublisher("Penguin Random House");
        book1.setPostingDate(LocalDate.of(2021, 1, 1));
        book1.setStatus(Book.Status.AVAILABLE);
        book1.setAuthor(author1);
        book1.setCrack(crack1);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setBookName("The Hobbit");
        book2.setQuantity(5L);
        book2.setCategory(category2);
        book2.setPublisher("HarperCollins");
        book2.setPostingDate(LocalDate.of(2022, 6, 10));
        book2.setStatus(Book.Status.BORROWED);
        book2.setAuthor(author2);  // Gán tác giả J.R.R. Tolkien cho sách The Hobbit
        book2.setCrack(crack2);    // Gán vị trí crack2 cho sách The Hobbit
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
