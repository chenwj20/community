package cn.cwj.community.model;

import lombok.Data;
import org.springframework.stereotype.Repository;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Date 2020/2/10
 * @Version V1.0
 **/
@Data
@Table(name = "ad")
public class Ad {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String url;
    private String image;
    private Long gmtCreate;
    private Long gmtModified;
    private Long gmtStart;
    private Long gmtEnd;
    private Integer status;
    private String postion;
}
