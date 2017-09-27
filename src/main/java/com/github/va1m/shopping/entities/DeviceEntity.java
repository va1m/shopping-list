package com.github.va1m.shopping.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Represents users device on which editing has made.
 */
@Entity
@Table(name = "devices")
@Getter
@Setter
@ToString(of = {"id", "name"})
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(min = 1)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonIgnore
    private UserEntity user;

    @OneToMany(mappedBy = "device")
    @JsonIgnore
    private Collection<ListItemEntity> items;
}
