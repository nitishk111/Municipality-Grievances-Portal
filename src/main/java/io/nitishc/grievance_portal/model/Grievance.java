package io.nitishc.grievance_portal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.enums.Priority;
import io.nitishc.grievance_portal.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "grievance_register")
public class Grievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long grievanceId;

    @Column(nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department grievanceType;

    @Column(nullable = false)
    private String complaintTitle;

    @Column(nullable = false)
    private String complaintDescription;

    @JsonIgnore
    @OneToOne(mappedBy = "grievance",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false ,columnDefinition = "ENUM('NEW', 'IN_PROGRESS', 'RESOLVED'," +
            " 'REJECTED', 'ESCALATED')")
    private Status status = Status.NEW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false ,columnDefinition = "ENUM('LOW', 'MEDIUM', 'HIGH')")
    private Priority priority = Priority.LOW;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private final LocalDate createdAt = LocalDate.now();

    private LocalDate lastUpdate = LocalDate.now();

    @JsonIgnore
    @OneToMany(mappedBy = "grievance", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "grievance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GrievanceFile> grievanceFile  =new ArrayList<>();

    public void addFile(GrievanceFile file) {
        grievanceFile.add(file);
        file.setGrievance(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setGrievance(this);
    }

}
