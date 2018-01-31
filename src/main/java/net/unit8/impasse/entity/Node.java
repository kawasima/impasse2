package net.unit8.impasse.entity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;

import java.io.Serializable;

@Entity
@Table(name = "nodes")
@Data
public class Node implements Serializable {
    private Long id;
    private String name;
    private NodeType type;
    private Long parentId;
}
