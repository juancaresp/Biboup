package es.boup.appboup.Model;

import java.util.Objects;

public class DebtDTO {
    private Group group;

    private Debt debt;

    public DebtDTO(Group group, Debt debt) {
        this.group = group;
        this.debt = debt;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DebtDTO debtDTO = (DebtDTO) o;
        return Objects.equals(group, debtDTO.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group);
    }
}
