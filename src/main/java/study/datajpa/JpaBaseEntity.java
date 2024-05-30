package study.datajpa;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@MappedSuperclass
@Getter
public class JpaBaseEntity {


@Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        //최초 업데이트는 생성일과 같은 값을 등록함
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
