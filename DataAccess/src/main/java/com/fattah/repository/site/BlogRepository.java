package com.fattah.repository.site;

import com.fattah.entity.site.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    @Query("""
from Blog where status=com.fattah.entity.enums.BlogStatus.Published and publishDate<=current_date order by publishDate desc
""")
    List<Blog> findAllPublished(Pageable pageable);
}
