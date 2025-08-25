package com.fattah.repository.site;

import com.fattah.entity.site.NavBar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NavBarRepository extends JpaRepository<NavBar,Long> {
    List<NavBar> findAllByEnabledIsTrueOrderByOrderNumberAsc();



    @Query("""
select orderNumber from NavBar order by orderNumber desc limit 1
""")
    Integer findLastOrderNumber();

Optional<NavBar> findFirstByOrderNumberLessThanOrderByOrderNumberDesc(Integer orderNumber);

    Optional<NavBar> findFirstByOrderNumberGreaterThanOrderByOrderNumberAsc(Integer orderNumber);
}


