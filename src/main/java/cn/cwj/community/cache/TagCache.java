package cn.cwj.community.cache;

import cn.cwj.community.dto.TagDTO;
import cn.hutool.core.collection.CollectionUtil;
import org.springframework.util.CollectionUtils;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Date 2020/2/4
 * @Version V1.0
 **/
public class TagCache {

    public static List<TagDTO> tagList(List<String> tag){
        List<TagDTO> tagDTOs = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setList(Arrays.asList("javascript", "php", "css", "html", "html5", "java", "node.js", "python", "c++", "c", "golang", "objective-c", "typescript", "shell", "swift", "c#", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl"));
        tagDTOs.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setList(Arrays.asList("laravel","layui","vue","bootstrap","angular","jquery","react","spring", "springboot","springcloud","springmvc","mybatis","express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts"));
        tagDTOs.add(framework);


        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setList(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos", "缓存 tomcat", "负载均衡", "unix", "hadoop", "windows-server"));
        tagDTOs.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setList(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql memcached", "sqlserver", "postgresql", "sqlite"));
        tagDTOs.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setList(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text", "xcode intellij-idea", "eclipse", "maven", "ide", "svn", "visual-studio", "atom emacs", "textmate", "hg"));
        tagDTOs.add(tool);

        TagDTO userTag = new TagDTO();
        userTag.setCategoryName("我的标签");
        userTag.setList(tag);
        tagDTOs.add(userTag);
        System.out.println(tagDTOs);
        return tagDTOs;

    }

    public static List<String> getTagList(){
        List<String> list = new ArrayList<>();
        List<TagDTO> tagDTOS = tagList(new ArrayList<>());
        for (TagDTO tagDTO : tagDTOS) {
            list.addAll(tagDTO.getList());
        }
        return list;
    }

}
