package cn.cwj.community.controller;

import cn.cwj.community.dto.ResultDTO;
import cn.cwj.community.dto.TableDTO;
import cn.cwj.community.enums.CommonEnum;
import cn.cwj.community.model.Category;
import cn.cwj.community.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Date 2020/2/26
 * @Version V1.0
 **/
@Controller
@RequestMapping("/system/category")
public class SystemCategoryController {
    @Autowired
    private CategoryService categoryService;
    @RequestMapping("/all")
    @ResponseBody
    public TableDTO categoryAll(){
        TableDTO allCategory = categoryService.findCategory();
        return allCategory;
    }
    @PostMapping("/option")
    @ResponseBody
    public ResultDTO optionCategory(Category category){
        categoryService.insertOrUpdate(category);
        return ResultDTO.okOf(CommonEnum.OPTION__SUCCESS);
    }

    @RequestMapping("/{opType}/{id}")
    public String optionCategory(@PathVariable String opType,
                                 @PathVariable Integer id,
                                 Model model){
        Category category = null;
        if (opType.equals("edit")){
            category = categoryService.findCategoryById(id);
        }
        model.addAttribute("category",category);
        return "system/edit_category";
    }
}
