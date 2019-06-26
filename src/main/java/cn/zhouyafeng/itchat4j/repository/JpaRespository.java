package cn.zhouyafeng.itchat4j.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface JpaRespository<T,ID extends Serializable> extends PagingAndSortingRepository<T,ID> , QueryByExampleExecutor {
    List<T> findAll();

    T getOne(ID var1);

}
