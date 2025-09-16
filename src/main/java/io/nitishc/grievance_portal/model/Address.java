package io.nitishc.grievance_portal.model;

import io.nitishc.grievance_portal.enums.Block;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    private int houseNo;

    private String street;

    private String block;

    @Setter(AccessLevel.NONE)
    private final String city = "Kanpur City";

    @Setter(AccessLevel.NONE)
    private final String state = "Uttar Pradesh";

    @Column(nullable = false)
    private String pincode;

    @OneToOne
    private Grievance grievance;

}
