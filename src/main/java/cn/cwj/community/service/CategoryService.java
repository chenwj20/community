package cn.cwj.community.service;

import cn.cwj.community.dto.TableDTO;
import cn.cwj.community.mapper.CategoryMapper;
import cn.cwj.community.model.Category;

import cn.cwj.community.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Date 2020/2/26
 * @Version V1.0
 **/
@Service
public class CategoryService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CategoryMapper categoryMapper;
    public TableDTO findCategory() {
        List<Category> categories = categoryMapper.selectAll();
        return TableDTO.okOf(categories,Long.valueOf(categories.size()));
    }
    public List<Category> selectCategory(){
        String categories = redisUtil.get("categories");
        if (StringUtils.isEmpty(categories)){
            List<Category> categories1 = categoryMapper.selectAll();
            String category = JSON.toJSONString(categories1);
            redisUtil.set("categories",category);
            return categories1;
        }
        return JSON.parseObject(categories,List.class);
    }

    public void insertOrUpdate(Category category) {

        if (category.getId() == null){
            categoryMapper.insertSelective(category);
        }else {
            categoryMapper.updateByPrimaryKey(category);
        }
        redisUtil.delete("categories");
    }

    public Category findCategoryById(Integer id) {

        Category category = categoryMapper.selectByPrimaryKey(id);
        return category;
    }
}
