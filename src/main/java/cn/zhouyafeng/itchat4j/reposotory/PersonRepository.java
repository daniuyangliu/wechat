package cn.zhouyafeng.itchat4j.reposotory;

import cn.zhouyafeng.itchat4j.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author 71768
 * @Title: PersonRepository
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2617:27
 */
@Repository
public interface PersonRepository extends JpaRepository<Person,Long>, JpaSpecificationExecutor<Person> {
}
