package pl.training.bank.entity.operation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.training.bank.entity.account.Account;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "operations")
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Operation implements Serializable {

    @GeneratedValue
    @Id
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @OneToOne
    @NonNull
    private Account account;
    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    @NonNull
    private OperationType type;
    @NonNull
    private Long funds;

}
