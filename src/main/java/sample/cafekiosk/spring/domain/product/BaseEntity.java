package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdDateTime;
    //생성되거나 수정되었을때 자동으로 시간이 찍힘

    @LastModifiedDate
    private LocalDateTime modifiedDateTime;

}
