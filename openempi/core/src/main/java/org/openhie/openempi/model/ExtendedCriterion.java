package org.openhie.openempi.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ExtendedCriterion extends Criterion
{
	private static final long serialVersionUID = 7608441012044500866L;

	private String alias;
	private String associationPath;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAssociationPath() {
		return associationPath;
	}

	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof ExtendedCriterion))
			return false;
		ExtendedCriterion castOther = (ExtendedCriterion) other;
		return new EqualsBuilder()
			.append(getName(), castOther.getName())
			.append(getOperation(), castOther.getOperation())
			.append(getValue(), castOther.getValue())
			.append(getAlias(), castOther.getAlias())
			.append(getAssociationPath(), castOther.getAssociationPath())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getName())
			.append(getOperation())
			.append(getValue())
			.append(getAlias())
			.append(getAssociationPath())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("name", getName())
			.append("operation", getOperation())
			.append("value", getValue())
			.append("alias", getAlias())
			.append("associationPath", getAssociationPath())
				.toString();
	}	
}
