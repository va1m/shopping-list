package com.github.va1m.shopping.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User - owner, collaborator, or author of the last changes
 */
@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString(of = {"id", "login", "name"})
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(min = 5, max = 100)
    @NotNull
    private String login;

    @Column
    @Size(min = 1)
    @NotNull
    private String name;

    @Column
    @Size(min = 3)
    @NotNull
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<ListEntity> listsAsOwner;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private Collection<ListItemEntity> editedItems;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<DeviceEntity> devices;
}
