package com.example.library.dto;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String authorName;
    private String publisherName;
    private Set<String> categories;

    public BookDTO(Long id, String title, String authorName, String publisherName, Set<String> categories) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.categories = categories;
    }
}
