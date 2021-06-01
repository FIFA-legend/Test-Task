package by.bsuir.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "items")
public class Item extends BaseEntity {

    @Column(name = "name")
    @ApiModelProperty(notes = "Product Name", name = "name", required = true, value = "Soccer Ball")
    private String name;

    @Column(name = "description")
    @ApiModelProperty(notes = "Product Description", name = "description", required = true,
            value = "A soccer ball is the ball used in the sport of association football.")
    private String description;

    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "item_id"))
    @ElementCollection(targetClass = Tag.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "tags")
    @ApiModelProperty(notes = "Product Tags", name = "tags", required = true, value = "SOME_TAG")
    private Set<Tag> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(tags, item.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, tags);
    }
}
