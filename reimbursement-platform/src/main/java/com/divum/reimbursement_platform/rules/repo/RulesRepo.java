package com.divum.reimbursement_platform.rules.repo;

import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import com.divum.reimbursement_platform.rules.entity.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RulesRepo extends JpaRepository<Rules, Long>{

    public Rules findByCategory(final RuleCategory category);
}
