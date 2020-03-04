package com.github.va1m.shopping.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents shopping list
 */
@Entity
@Table(name = "lists")
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString(of = {"id", "name", "description", "owner", "listItems", "force", "device"})
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ListEntity implements Cloneable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(min = 1)
    @NotNull
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @OneToMany(mappedBy = "list", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Collection<ListItemEntity> listItems;

    /**
     * On which device the list was modified
     */
    @Transient
    private DeviceEntity device;

    /**
     * Firstly user send list with force == false|null.
     * If there was a conflict the user resolves the one and send the updated list with force == true.
     */
    @Transient
    private Boolean force;

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
