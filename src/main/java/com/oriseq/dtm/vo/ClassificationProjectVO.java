package com.oriseq.dtm.vo;

import com.oriseq.dtm.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Description: new java files header..
 *
 * @author hacah
 * @version 1.0
 * @date 2024/5/23 10:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationProjectVO {
    private Long id;
    private String name;
    private Set<ClassificationProjectVO> children = new LinkedHashSet<>();
    ;
    private List<Project> projects = new ArrayList<>();

    public ClassificationProjectVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addChildren(ClassificationProjectVO classificationProjectVO) {
        this.children.add(classificationProjectVO);
    }


}
