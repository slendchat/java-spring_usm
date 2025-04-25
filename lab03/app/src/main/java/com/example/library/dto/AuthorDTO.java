package com.example.library.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class AuthorDTO {
    private Long id;
    @NotBlank
    private String name;
    private List<BookDTO> books;
    public AuthorDTO(Long id, String name, List<BookDTO> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }
}
