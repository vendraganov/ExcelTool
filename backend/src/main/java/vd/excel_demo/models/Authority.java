package vd.excel_demo.models;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static vd.excel_demo.utils.Constants.AUTHORITY_ID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    private static final String ROLE = "role";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AUTHORITY_ID)
    private Long authorityId;

    @NotNull
    @Column(name = ROLE)
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
