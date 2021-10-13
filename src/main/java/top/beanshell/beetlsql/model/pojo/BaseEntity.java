package top.beanshell.beetlsql.model.pojo;

import lombok.Data;
import org.beetl.sql.annotation.entity.AssignID;

import java.io.Serializable;
import java.util.Date;

/**
 * base pojo for database table mapping
 * @author binchao
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * primary key
     */
    @AssignID("myId")
    private Long id;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * updateTime
     */
    private Date updateTime;


    /**
     * init entity
     */
    public void init() {
        if (null == createTime) {
            this.createTime = new Date();
        }
    }
}
