package cn.zhouyafeng.itchat4j.reposotory;

import cn.zhouyafeng.itchat4j.entity.Person;
import cn.zhouyafeng.itchat4j.entity.SysCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author 71768
 * @Title: PersonRepository
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2617:27
 */
@Repository
public interface SysCodeRepository extends JpaRepository<SysCode,Long>, JpaSpecificationExecutor<SysCode> {

}
