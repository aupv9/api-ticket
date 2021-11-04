package com.apps.mybatis.mysql;

import com.apps.domain.entity.Concession;
import com.apps.response.entity.ConcessionMyOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ConcessionRepository {
    List<Concession> findAll(@Param("limit") int limit, @Param("offset") int offset,
                             @Param("sort")String sort, @Param("order") String order,
                             @Param("name")String name, @Param("category") Integer categoryId);
    int findCountAll(@Param("name")String name, @Param("category") Integer categoryId);
    int update(@Param("entity") Concession concession);
    void delete(@Param("id") Integer id);
    @Select("SELECT * FROM CONCESSION WHERE ID = #{id}")
    Concession findById(@Param("id") Integer id);

    @Select("select concession.id  ,price, category_id ,\n" +
            "       concession.image , concession.thumbnail ,c.name as categoryName\n" +
            "from concession join category c on c.id = concession.category_id")
    List<Concession> findAllConcession();

    List<ConcessionMyOrder> findAllConcessionInOrder(@Param("id")Integer id);
}
