package com.github.va1m.shopping.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Shopping list item entity
 */
@Entity
@Table(name = "list_items")
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString(of = {"id", "version", "name", "checked", "quantity", "isDeleted"})
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ListItemEntity implements Cloneable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Protects record from overwriting by different users or processes
     */
    @Version
    private int version;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean checked;

    @Column
    private Integer quantity;

    @Column
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    @NotNull
    @JsonIgnore
    private ListEntity list;

    /**
     * User who is the last editor of the item
     */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    /**
     * On which device the item was modified
     */
    @ManyToOne
    @JoinColumn(name = "device_id")
    private DeviceEntity device;

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
