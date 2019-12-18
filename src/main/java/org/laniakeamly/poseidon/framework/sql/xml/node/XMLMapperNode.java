package org.laniakeamly.poseidon.framework.sql.xml.node;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * Create by 2BKeyboard on 2019/12/17 15:06
 */
@Getter
@Setter
public class XMLMapperNode {

    private String name;
    private List<XMLNode> nodes = new LinkedList<>();

    public void addNode(XMLNode node){
        nodes.add(node);
    }

}