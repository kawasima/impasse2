package net.unit8.impasse.dao;

import net.unit8.impasse.entity.Node;
import org.seasar.doma.*;

import java.util.List;

@Dao
public interface NodeDao {
    @Select
    List<Node> findAllUnder(Long id);

    @Insert
    int insert(Node node);

    @Update
    int update(Node node);

    @Delete
    int delete(Node node);
}
