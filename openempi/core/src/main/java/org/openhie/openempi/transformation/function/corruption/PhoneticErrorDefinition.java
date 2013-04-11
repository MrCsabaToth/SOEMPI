package org.openhie.openempi.transformation.function.corruption;

public class PhoneticErrorDefinition
{
	public enum ErrorLocation {
		ANYWHERE,
		AT_THE_BEGINNING,
		IN_THE_MIDDLE,
		AT_THE_END
	}
	public enum PrecededByConstraint {
		NOTHING,
		PRECEEDED_BY_CONSONANT,
		PRECEEDED_BY_VOWEL,
		PRECEEDED_BY_VAN_VON_OR_SCH,
		PRECEEDED_BY_U_OR_3_SPACES_AND_CGLRT,
		PRECEEDED_BY_2_SPACES_AND_BHD,
		PRECEEDED_BY_A_OR_I,
		NOT_PRECEEDED_BY_E_OR_I,
		NOT_PRECEEDED_BY_I,
		NOT_PRECEEDED_BY_T,
		NOT_PRECEEDED_BY_SPACE_AND_A_VOWEL
	}
	
	public enum FollowedByConstraint {
		NOTHING,
		FOLLOWED_BY_CONSONANT,
		FOLLOWED_BY_VOWEL,
		NOT_FOLLOWED_BY_SPACE_AND_HU,
		NOT_FOLLOWED_BY_VOWEL_OR_W,
		NOT_FOLLOWED_BY_E_OR_I,
		NOT_FOLLOWED_BY_RGY_OR_OGY
	}
	
	public enum SpecialConstraint {
		NOTHING,
		IS_SLAVIC,
		NOT_SLAVIC,
		STARTS_WITH_A_VOWEL_AND_NOT_SLAVIC,
		DOESNT_CONTAIN_DANGER_RANGER_MANGER
	}
	
	public String after;
	ErrorLocation errorLocation = ErrorLocation.ANYWHERE;
	PrecededByConstraint preConstr = PrecededByConstraint.NOTHING;
	FollowedByConstraint postConstr = FollowedByConstraint.NOTHING;
	SpecialConstraint specConstr = SpecialConstraint.NOTHING;
	
	public PhoneticErrorDefinition(String after) {
		this.after = after;
	}

	public PhoneticErrorDefinition(String after, ErrorLocation errorLocation) {
		this.after = after;
		this.errorLocation = errorLocation;
	}

	public PhoneticErrorDefinition(String after, ErrorLocation errorLocation, SpecialConstraint specConstr) {
		this.after = after;
		this.errorLocation = errorLocation;
		this.specConstr = specConstr;
	}

	public PhoneticErrorDefinition(String after, ErrorLocation errorLocation, PrecededByConstraint preConstr) {
		this.after = after;
		this.errorLocation = errorLocation;
		this.preConstr = preConstr;
	}

	public PhoneticErrorDefinition(String after, ErrorLocation errorLocation, PrecededByConstraint preConstr, SpecialConstraint specConstr) {
		this.after = after;
		this.errorLocation = errorLocation;
		this.preConstr = preConstr;
		this.specConstr = specConstr;
	}

	public PhoneticErrorDefinition(String after, ErrorLocation errorLocation, FollowedByConstraint postConstr) {
		this.after = after;
		this.errorLocation = errorLocation;
		this.preConstr = PrecededByConstraint.NOTHING;
		this.postConstr = postConstr;
	}

	public PhoneticErrorDefinition(String after, ErrorLocation errorLocation, PrecededByConstraint preConstr, FollowedByConstraint postConstr) {
		this.after = after;
		this.errorLocation = errorLocation;
		this.preConstr = preConstr;
		this.postConstr = postConstr;
	}
}
