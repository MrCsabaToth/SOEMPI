/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openhie.openempi.transformation.function.corruption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.ErrorLocation;
import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.FollowedByConstraint;
import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.PrecededByConstraint;
import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.SpecialConstraint;
import org.openhie.openempi.transformation.function.corruption.SwapoutBase.CaseEnum;

public class PhoneticError
{
	private static final Map<String, PhoneticErrorDefinition> CONST_PHONETIC_ERROR_MAP =
	    Collections.unmodifiableMap(new HashMap<String, PhoneticErrorDefinition>() {{
	    	put("SAN JOSE", new PhoneticErrorDefinition("SAN HOSE", ErrorLocation.IN_THE_MIDDLE));
	    	put("CHARIS", new PhoneticErrorDefinition("KARIS", ErrorLocation.AT_THE_BEGINNING));
	    	put("ORCHID", new PhoneticErrorDefinition("ORKID"));
	    	put("BACHER", new PhoneticErrorDefinition("BAKER", ErrorLocation.IN_THE_MIDDLE));
	    	put("CHARAC", new PhoneticErrorDefinition("KARAK", ErrorLocation.AT_THE_BEGINNING));
	    	put("ARCHIT", new PhoneticErrorDefinition("ARKIT"));
	    	put("ORCHES", new PhoneticErrorDefinition("ORKES"));
	    	put("MACHER", new PhoneticErrorDefinition("MAKER", ErrorLocation.IN_THE_MIDDLE));
	    	put("VAN CH", new PhoneticErrorDefinition("VAN K", ErrorLocation.AT_THE_BEGINNING));
	    	put("CAESAR", new PhoneticErrorDefinition("SESAR", ErrorLocation.AT_THE_BEGINNING));
	    	put("VON CH", new PhoneticErrorDefinition("VON K", ErrorLocation.AT_THE_BEGINNING));
	    	put("SHOLZ", new PhoneticErrorDefinition("SOLZ"));
	    	put("SHOEK", new PhoneticErrorDefinition("SOEK"));
	    	put("SCHEM", new PhoneticErrorDefinition("SKEM"));
	    	put("UCCES", new PhoneticErrorDefinition("UKSES"));
	    	put("SHEIM", new PhoneticErrorDefinition("SEIM"));
	    	put("SCHOO", new PhoneticErrorDefinition("SKOO"));
	    	put("SCHUY", new PhoneticErrorDefinition("SKUY"));
	    	put("SCHEN", new PhoneticErrorDefinition("XEN"));
	    	put("SCHER", new PhoneticErrorDefinition("XER"));
	    	put("UCCEE", new PhoneticErrorDefinition("UKSEE"));
	    	put("SCHED", new PhoneticErrorDefinition("SKED"));
	    	put("HROUG", new PhoneticErrorDefinition("REW"));
	    	put("SHOLM", new PhoneticErrorDefinition("SOLM"));
	    	put("LOUGH", new PhoneticErrorDefinition("LOW"));
	    	put("SUGAR", new PhoneticErrorDefinition("XUGAR", ErrorLocation.AT_THE_BEGINNING));
	    	put("OCHL", new PhoneticErrorDefinition("OKL"));
	    	put("GNES", new PhoneticErrorDefinition("NS", ErrorLocation.AT_THE_END));
	    	put("UCHB", new PhoneticErrorDefinition("UKB"));
	    	put("GNEY", new PhoneticErrorDefinition("NEY", ErrorLocation.IN_THE_MIDDLE, SpecialConstraint.NOT_SLAVIC));
	    	put("ACHL", new PhoneticErrorDefinition("AKL"));
	    	put("AUGH", new PhoneticErrorDefinition("ARF"));
	    	put("ECHW", new PhoneticErrorDefinition("EKW"));
	    	put("ACHH", new PhoneticErrorDefinition("AKH"));
	    	put("OCHV", new PhoneticErrorDefinition("OKV"));
	    	put("CHIA", new PhoneticErrorDefinition("KIA"));
	    	put("UCHF", new PhoneticErrorDefinition("UKF"));
	    	put("CHIA", new PhoneticErrorDefinition("KIA", ErrorLocation.AT_THE_BEGINNING));
	    	put("WICZ", new PhoneticErrorDefinition("TS"));
	    	put("CHYM", new PhoneticErrorDefinition("KYM", ErrorLocation.AT_THE_BEGINNING));
	    	put("ECHM", new PhoneticErrorDefinition("EKM"));
	    	put("UCHR", new PhoneticErrorDefinition("UKR"));
	    	put("ECHH", new PhoneticErrorDefinition("EKH"));
	    	put("AISZ", new PhoneticErrorDefinition("AI", ErrorLocation.AT_THE_END));
	    	put("GIER", new PhoneticErrorDefinition("JIER", ErrorLocation.IN_THE_MIDDLE));
	    	put("ACHR", new PhoneticErrorDefinition("AKR"));
	    	put("ACHV", new PhoneticErrorDefinition("AKV"));
	    	put("ECHN", new PhoneticErrorDefinition("EKN"));
	    	put("EAUX", new PhoneticErrorDefinition("EAUKS", ErrorLocation.AT_THE_END));
	    	put("ECHR", new PhoneticErrorDefinition("EKR"));
	    	put("OGGI", new PhoneticErrorDefinition("OJI", ErrorLocation.IN_THE_MIDDLE));
	    	put("OCHW", new PhoneticErrorDefinition("OKW"));
	    	put("CHAE", new PhoneticErrorDefinition("KAE", ErrorLocation.IN_THE_MIDDLE));
	    	put("ACCE", new PhoneticErrorDefinition("ASKE", ErrorLocation.ANYWHERE, FollowedByConstraint.NOT_FOLLOWED_BY_SPACE_AND_HU));
	    	put("ILLO", new PhoneticErrorDefinition("ILO", ErrorLocation.AT_THE_END));
	    	put("MPTS", new PhoneticErrorDefinition("MPS"));
	    	put("CHEM", new PhoneticErrorDefinition("KEM", ErrorLocation.AT_THE_BEGINNING));
	    	put("OCHH", new PhoneticErrorDefinition("OKH"));
	    	put("ACHM", new PhoneticErrorDefinition("AKM"));
	    	put("IAUX", new PhoneticErrorDefinition("IAUKS", ErrorLocation.AT_THE_END));
	    	put("UCHN", new PhoneticErrorDefinition("UKN"));
	    	put("ACHN", new PhoneticErrorDefinition("AKN"));
	    	put("UCHM", new PhoneticErrorDefinition("UKM"));
	    	put("CHOR", new PhoneticErrorDefinition("KOR", ErrorLocation.AT_THE_BEGINNING));
	    	put("TION", new PhoneticErrorDefinition("XION"));
	    	put("GHNE", new PhoneticErrorDefinition("NE"));
	    	put("WICZ", new PhoneticErrorDefinition("WIS"));
	    	put("OCHF", new PhoneticErrorDefinition("OKF"));
	    	put("ACHF", new PhoneticErrorDefinition("AKF"));
	    	put("ACCH", new PhoneticErrorDefinition("ASKH", ErrorLocation.ANYWHERE, FollowedByConstraint.NOT_FOLLOWED_BY_SPACE_AND_HU));
	    	put("OISS", new PhoneticErrorDefinition("OI", ErrorLocation.AT_THE_END));
	    	put("ILLA", new PhoneticErrorDefinition("ILA", ErrorLocation.AT_THE_END));
	    	put("UCHH", new PhoneticErrorDefinition("UKH"));
	    	put("OUGH", new PhoneticErrorDefinition("OF"));
	    	put("OCHN", new PhoneticErrorDefinition("OKN"));
	    	put("UCHW", new PhoneticErrorDefinition("UKW"));
	    	put("ECHL", new PhoneticErrorDefinition("EKL"));
	    	put("ECHV", new PhoneticErrorDefinition("EKV"));
	    	put("AGGI", new PhoneticErrorDefinition("AJI", ErrorLocation.IN_THE_MIDDLE));
	    	put("OCHM", new PhoneticErrorDefinition("OKM"));
	    	put("AISS", new PhoneticErrorDefinition("AI", ErrorLocation.AT_THE_END));
	    	put("EAUX", new PhoneticErrorDefinition("OH", ErrorLocation.AT_THE_END));
	    	put("AGGI", new PhoneticErrorDefinition("AKI", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("ACCI", new PhoneticErrorDefinition("ASKI", ErrorLocation.ANYWHERE, FollowedByConstraint.NOT_FOLLOWED_BY_SPACE_AND_HU));
	    	put("WITZ", new PhoneticErrorDefinition("TS"));
	    	put("EXCI", new PhoneticErrorDefinition("ECS"));
	    	put("ECHF", new PhoneticErrorDefinition("EKF"));
	    	put("ALLE", new PhoneticErrorDefinition("ALE", ErrorLocation.AT_THE_END));
	    	put("ECHB", new PhoneticErrorDefinition("EKB"));
	    	put("UCHV", new PhoneticErrorDefinition("UKV"));
	    	put("UCHL", new PhoneticErrorDefinition("UKL"));
	    	put("ACHW", new PhoneticErrorDefinition("AKW"));
	    	put("ACHB", new PhoneticErrorDefinition("AKB"));
	    	put("OCHR", new PhoneticErrorDefinition("OKR"));
	    	put("OGGI", new PhoneticErrorDefinition("OKI", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("OISZ", new PhoneticErrorDefinition("OI", ErrorLocation.AT_THE_END));
	    	put("OCHB", new PhoneticErrorDefinition("OKB"));
	    	put("CHN", new PhoneticErrorDefinition("KN", ErrorLocation.AT_THE_BEGINNING));
	    	put("OUX", new PhoneticErrorDefinition("OUKS", ErrorLocation.AT_THE_END));
	    	put("CIA", new PhoneticErrorDefinition("SIA"));
	    	put("CCE", new PhoneticErrorDefinition("XI"));
	    	put("ZZI", new PhoneticErrorDefinition("SI"));
	    	put("SCI", new PhoneticErrorDefinition("SIIF"));
	    	put("CHF", new PhoneticErrorDefinition("KF", ErrorLocation.AT_THE_BEGINNING));
	    	put("BTL", new PhoneticErrorDefinition("TL"));
	    	put("ISL", new PhoneticErrorDefinition("IL"));
	    	put("SCH", new PhoneticErrorDefinition("X", ErrorLocation.AT_THE_END));
	    	put("MPS", new PhoneticErrorDefinition("MS"));
	    	put("WSK", new PhoneticErrorDefinition("VSKIE", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("CHB", new PhoneticErrorDefinition("KB", ErrorLocation.AT_THE_BEGINNING));
	    	put("GNE", new PhoneticErrorDefinition("N", ErrorLocation.AT_THE_END));
	    	put("CHW", new PhoneticErrorDefinition("KW", ErrorLocation.AT_THE_BEGINNING));
	    	put("SCH", new PhoneticErrorDefinition("SH"));
	    	put("DGY", new PhoneticErrorDefinition("JY"));
	    	put("RIE", new PhoneticErrorDefinition("RY"));
	    	put("GER", new PhoneticErrorDefinition("KER", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_E_OR_I, SpecialConstraint.DOESNT_CONTAIN_DANGER_RANGER_MANGER));
	    	put("GES", new PhoneticErrorDefinition("KES", ErrorLocation.AT_THE_BEGINNING));
	    	put("WHO", new PhoneticErrorDefinition("WO"));
	    	put("CHV", new PhoneticErrorDefinition("KV", ErrorLocation.AT_THE_BEGINNING));
	    	put("SCE", new PhoneticErrorDefinition("SE"));
	    	put("TNT", new PhoneticErrorDefinition("ENT", ErrorLocation.AT_THE_END));
	    	put("TCH", new PhoneticErrorDefinition("X"));
	    	put("TIA", new PhoneticErrorDefinition("XIA"));
	    	put("WHE", new PhoneticErrorDefinition("WE"));
	    	put("SCH", new PhoneticErrorDefinition("X", ErrorLocation.IN_THE_MIDDLE));
	    	put("GEY", new PhoneticErrorDefinition("KEY", ErrorLocation.AT_THE_BEGINNING));
	    	put("DGE", new PhoneticErrorDefinition("JE"));
	    	put("TTH", new PhoneticErrorDefinition("T"));
	    	put("GHT", new PhoneticErrorDefinition("T"));
	    	put("GEP", new PhoneticErrorDefinition("KEP", ErrorLocation.AT_THE_BEGINNING));
	    	put("SCH", new PhoneticErrorDefinition("X", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.NOT_FOLLOWED_BY_VOWEL_OR_W));
	    	put("C G", new PhoneticErrorDefinition("K"));
	    	put("GER", new PhoneticErrorDefinition("KER", ErrorLocation.AT_THE_BEGINNING));
	    	put("GEB", new PhoneticErrorDefinition("KEB", ErrorLocation.AT_THE_BEGINNING));
	    	put("CHM", new PhoneticErrorDefinition("KM", ErrorLocation.AT_THE_BEGINNING));
	    	put("WHI", new PhoneticErrorDefinition("WI"));
	    	put("ACH", new PhoneticErrorDefinition("K", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.NOT_FOLLOWED_BY_E_OR_I));
	    	put("ZZO", new PhoneticErrorDefinition("S"));
	    	put("CHS", new PhoneticErrorDefinition("KS"));
	    	put("SCH", new PhoneticErrorDefinition("SK", ErrorLocation.AT_THE_BEGINNING));
	    	put("CIO", new PhoneticErrorDefinition("SIO"));
	    	put("GIL", new PhoneticErrorDefinition("KIL", ErrorLocation.AT_THE_BEGINNING));
	    	put("CKS", new PhoneticErrorDefinition("X", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("GIB", new PhoneticErrorDefinition("KIB", ErrorLocation.AT_THE_BEGINNING));
	    	put("STL", new PhoneticErrorDefinition("SL", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("GHI", new PhoneticErrorDefinition("J", ErrorLocation.AT_THE_BEGINNING));
	    	put("LES", new PhoneticErrorDefinition("ILES", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_CONSONANT));
	    	put("AUX", new PhoneticErrorDefinition("AUKS", ErrorLocation.AT_THE_END));
	    	put("CHH", new PhoneticErrorDefinition("KH", ErrorLocation.AT_THE_BEGINNING));
	    	put("GHN", new PhoneticErrorDefinition("N"));
	    	put("CIA", new PhoneticErrorDefinition("XIA"));
	    	put("YTH", new PhoneticErrorDefinition("ITH"));
	    	put("WHA", new PhoneticErrorDefinition("WA"));
	    	put("DGI", new PhoneticErrorDefinition("JI"));
	    	put("LLE", new PhoneticErrorDefinition("LE"));
	    	put("UMB", new PhoneticErrorDefinition("UM"));
	    	put("STL", new PhoneticErrorDefinition("SL"));
	    	put("GET", new PhoneticErrorDefinition("KET", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("TCH", new PhoneticErrorDefinition("CH"));
	    	put("GLI", new PhoneticErrorDefinition("KLI", ErrorLocation.IN_THE_MIDDLE, SpecialConstraint.NOT_SLAVIC));
	    	put("SCY", new PhoneticErrorDefinition("SY"));
	    	put("YSL", new PhoneticErrorDefinition("YL"));
	    	put("C C", new PhoneticErrorDefinition("K"));
	    	put("C Q", new PhoneticErrorDefinition("K"));
	    	put("GEI", new PhoneticErrorDefinition("KEI", ErrorLocation.AT_THE_BEGINNING));
	    	put("CCH", new PhoneticErrorDefinition("XH"));
	    	put("GIN", new PhoneticErrorDefinition("KIN", ErrorLocation.AT_THE_BEGINNING));
	    	put("CCI", new PhoneticErrorDefinition("XI"));
	    	put("ZZA", new PhoneticErrorDefinition("SA"));
	    	put("GIE", new PhoneticErrorDefinition("KIE", ErrorLocation.AT_THE_BEGINNING));
	    	put("NED", new PhoneticErrorDefinition("ND", ErrorLocation.AT_THE_END));
	    	put("CHR", new PhoneticErrorDefinition("KR", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("WHY", new PhoneticErrorDefinition("WY"));
	    	put("CHL", new PhoneticErrorDefinition("KL", ErrorLocation.AT_THE_BEGINNING));
	    	put("MPT", new PhoneticErrorDefinition("MT"));
	    	put("WSK", new PhoneticErrorDefinition("VSKIE", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("WHU", new PhoneticErrorDefinition("WU"));
	    	put("TSJ", new PhoneticErrorDefinition("CH", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("CHT", new PhoneticErrorDefinition("KT"));
	    	put("GEL", new PhoneticErrorDefinition("KEL", ErrorLocation.AT_THE_BEGINNING));
	    	put("CIE", new PhoneticErrorDefinition("SIE"));
	    	put("MCH", new PhoneticErrorDefinition("MK", ErrorLocation.IN_THE_MIDDLE));
	    	put("CG", new PhoneticErrorDefinition("K"));
	    	put("HO", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("EI", new PhoneticErrorDefinition("I"));
	    	put("LZ", new PhoneticErrorDefinition("LSH"));
	    	put("WZ", new PhoneticErrorDefinition("Z", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("TS", new PhoneticErrorDefinition("T", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("AU", new PhoneticErrorDefinition("O"));
	    	put("GY", new PhoneticErrorDefinition("KY", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_E_OR_I, FollowedByConstraint.NOT_FOLLOWED_BY_RGY_OR_OGY));
	    	put("ZH", new PhoneticErrorDefinition("JH"));
	    	put("ZZ", new PhoneticErrorDefinition("S"));
	    	put("TT", new PhoneticErrorDefinition("T"));
	    	put("LJ", new PhoneticErrorDefinition("LD", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("SL", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_BEGINNING));
	    	put("CE", new PhoneticErrorDefinition("SE"));
	    	put("IA", new PhoneticErrorDefinition("YA"));
	    	put("HR", new PhoneticErrorDefinition("AH", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_CONSONANT));
	    	put("VV", new PhoneticErrorDefinition("FF"));
	    	put("CZ", new PhoneticErrorDefinition("CH", ErrorLocation.IN_THE_MIDDLE));
	    	put("GI", new PhoneticErrorDefinition("JI", ErrorLocation.IN_THE_MIDDLE));
	    	put("KN", new PhoneticErrorDefinition("N", ErrorLocation.AT_THE_BEGINNING));
	    	put("DT", new PhoneticErrorDefinition("T", ErrorLocation.AT_THE_END));
	    	put("WH", new PhoneticErrorDefinition("H"));
	    	put("RE", new PhoneticErrorDefinition("AR", ErrorLocation.AT_THE_END));
	    	put("MN", new PhoneticErrorDefinition("N", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("OH", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("FF", new PhoneticErrorDefinition("VV"));
	    	put("GI", new PhoneticErrorDefinition("KI", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("FF", new PhoneticErrorDefinition("V"));
	    	put("WR", new PhoneticErrorDefinition("R"));
	    	put("DG", new PhoneticErrorDefinition("G"));
	    	put("TH", new PhoneticErrorDefinition("T"));
	    	put("JR", new PhoneticErrorDefinition("DR", ErrorLocation.IN_THE_MIDDLE));
	    	put("NX", new PhoneticErrorDefinition("NKS"));
	    	put("JC", new PhoneticErrorDefinition("K", ErrorLocation.AT_THE_END));
	    	put("CZ", new PhoneticErrorDefinition("C", ErrorLocation.AT_THE_BEGINNING));
	    	put("YJ", new PhoneticErrorDefinition("Y", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("CL", new PhoneticErrorDefinition("KL", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("TJ", new PhoneticErrorDefinition("CH", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("HI", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("JJ", new PhoneticErrorDefinition("J"));
	    	put("NK", new PhoneticErrorDefinition("NG"));
	    	put("DT", new PhoneticErrorDefinition("T"));
	    	put("GG", new PhoneticErrorDefinition("K"));
	    	put("LL", new PhoneticErrorDefinition("L"));
	    	put("KS", new PhoneticErrorDefinition("X", ErrorLocation.IN_THE_MIDDLE));
	    	put("GN", new PhoneticErrorDefinition("KN", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, SpecialConstraint.STARTS_WITH_A_VOWEL_AND_NOT_SLAVIC));
	    	put("LE", new PhoneticErrorDefinition("ILE", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_CONSONANT));
	    	put("DG", new PhoneticErrorDefinition("TK"));
	    	put("NN", new PhoneticErrorDefinition("N"));
	    	put("RZ", new PhoneticErrorDefinition("RSH"));
	    	put("OW", new PhoneticErrorDefinition("O", ErrorLocation.AT_THE_END));
	    	put("EW", new PhoneticErrorDefinition("E", ErrorLocation.AT_THE_END));
	    	put("CY", new PhoneticErrorDefinition("SY"));
	    	put("EE", new PhoneticErrorDefinition("I"));
	    	put("PF", new PhoneticErrorDefinition("F", ErrorLocation.AT_THE_BEGINNING));
	    	put("GE", new PhoneticErrorDefinition("KE", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("CH", new PhoneticErrorDefinition("X", ErrorLocation.IN_THE_MIDDLE));
	    	put("YH", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("GY", new PhoneticErrorDefinition("JY", ErrorLocation.IN_THE_MIDDLE));
	    	put("SZ", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_END));
	    	put("CI", new PhoneticErrorDefinition("SI"));
	    	put("SH", new PhoneticErrorDefinition("X"));
	    	put("OU", new PhoneticErrorDefinition("O"));
	    	put("UH", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("QQ", new PhoneticErrorDefinition("K"));
	    	put("PB", new PhoneticErrorDefinition("P"));
	    	put("EE", new PhoneticErrorDefinition("EA", ErrorLocation.AT_THE_END));
	    	put("AH", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("GC", new PhoneticErrorDefinition("K", ErrorLocation.AT_THE_END));
	    	put("NC", new PhoneticErrorDefinition("NK"));
	    	put("DL", new PhoneticErrorDefinition("DIL", ErrorLocation.AT_THE_END));
	    	put("OO", new PhoneticErrorDefinition("U"));
	    	put("GY", new PhoneticErrorDefinition("KY", ErrorLocation.AT_THE_BEGINNING));
	    	put("CH", new PhoneticErrorDefinition("X", ErrorLocation.AT_THE_BEGINNING));
	    	put("HA", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("GH", new PhoneticErrorDefinition("", ErrorLocation.AT_THE_BEGINNING));
	    	put("WR", new PhoneticErrorDefinition("R", ErrorLocation.AT_THE_BEGINNING));
	    	put("CO", new PhoneticErrorDefinition("KO"));
	    	put("GN", new PhoneticErrorDefinition("N", ErrorLocation.AT_THE_BEGINNING));
	    	put("GE", new PhoneticErrorDefinition("JY", ErrorLocation.IN_THE_MIDDLE));
	    	put("HR", new PhoneticErrorDefinition("AH", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("BB", new PhoneticErrorDefinition("P"));
	    	put("ES", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_END));
	    	put("SZ", new PhoneticErrorDefinition("S"));
	    	put("CA", new PhoneticErrorDefinition("KA"));
	    	put("EH", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("GH", new PhoneticErrorDefinition("K", ErrorLocation.ANYWHERE, PrecededByConstraint.NOT_PRECEEDED_BY_I));
	    	put("CQ", new PhoneticErrorDefinition("K"));
	    	put("MB", new PhoneticErrorDefinition("M", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("SM", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_BEGINNING));
	    	put("TL", new PhoneticErrorDefinition("TIL", ErrorLocation.AT_THE_END));
	    	put("EY", new PhoneticErrorDefinition("Y"));
	    	put("GH", new PhoneticErrorDefinition("F", ErrorLocation.ANYWHERE, PrecededByConstraint.PRECEEDED_BY_U_OR_3_SPACES_AND_CGLRT));
	    	put("YA", new PhoneticErrorDefinition("IA"));
	    	put("GH", new PhoneticErrorDefinition("K", ErrorLocation.AT_THE_BEGINNING));
	    	put("HE", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("RY", new PhoneticErrorDefinition("RIE"));
	    	put("PS", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_BEGINNING));
	    	put("AA", new PhoneticErrorDefinition("AR"));
	    	put("GN", new PhoneticErrorDefinition("N", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.FOLLOWED_BY_CONSONANT));
	    	put("SS", new PhoneticErrorDefinition("AS", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("HY", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("DD", new PhoneticErrorDefinition("T"));
	    	put("SC", new PhoneticErrorDefinition("SK"));
	    	put("CY", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_BEGINNING));
	    	put("SS", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_END));
	    	put("ZS", new PhoneticErrorDefinition("S"));
	    	put("TD", new PhoneticErrorDefinition("T"));
	    	put("KK", new PhoneticErrorDefinition("K"));
	    	put("CY", new PhoneticErrorDefinition("SI"));
	    	put("CC", new PhoneticErrorDefinition("K"));
	    	put("AW", new PhoneticErrorDefinition("A", ErrorLocation.AT_THE_END));
	    	put("IE", new PhoneticErrorDefinition("I"));
	    	put("XX", new PhoneticErrorDefinition(""));
	    	put("PN", new PhoneticErrorDefinition("N", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("GN", new PhoneticErrorDefinition("N", ErrorLocation.AT_THE_END));
	    	put("GH", new PhoneticErrorDefinition("", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_2_SPACES_AND_BHD));
	    	put("PP", new PhoneticErrorDefinition("P"));
	    	put("CU", new PhoneticErrorDefinition("KU"));
	    	put("PH", new PhoneticErrorDefinition("F"));
	    	put("HU", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("PT", new PhoneticErrorDefinition("T", ErrorLocation.AT_THE_BEGINNING));
	    	put("VV", new PhoneticErrorDefinition("F"));
	    	put("MM", new PhoneticErrorDefinition("M"));
	    	put("RR", new PhoneticErrorDefinition("R"));
	    	put("GH", new PhoneticErrorDefinition("E", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("CT", new PhoneticErrorDefinition("KT"));
	    	put("GH", new PhoneticErrorDefinition("K", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_SPACE_AND_A_VOWEL));
	    	put("YW", new PhoneticErrorDefinition("Y", ErrorLocation.AT_THE_END));
	    	put("IW", new PhoneticErrorDefinition("I", ErrorLocation.AT_THE_END));
	    	put("SN", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_BEGINNING));
	    	put("IH", new PhoneticErrorDefinition("H", ErrorLocation.AT_THE_BEGINNING));
	    	put("UW", new PhoneticErrorDefinition("U", ErrorLocation.AT_THE_END));
	    	put("SW", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_BEGINNING));
	    	put("CK", new PhoneticErrorDefinition("K"));
	    	put("NG", new PhoneticErrorDefinition("NK"));
	    	put("FF", new PhoneticErrorDefinition("F"));
	    	put("GH", new PhoneticErrorDefinition("G", ErrorLocation.AT_THE_BEGINNING));
	    	put("CR", new PhoneticErrorDefinition("KR", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("XC", new PhoneticErrorDefinition(""));
	    	put("ZZ", new PhoneticErrorDefinition("TS"));
	    	put("T", new PhoneticErrorDefinition("D"));
	    	put("G", new PhoneticErrorDefinition("K"));
	    	put("Q", new PhoneticErrorDefinition("KW", ErrorLocation.AT_THE_BEGINNING));
	    	put("C", new PhoneticErrorDefinition("K"));
	    	put("S", new PhoneticErrorDefinition("ST"));
	    	put("Z", new PhoneticErrorDefinition("J"));
	    	put("F", new PhoneticErrorDefinition("V"));
	    	put("R", new PhoneticErrorDefinition("AH", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("J", new PhoneticErrorDefinition("S"));
	    	put("W", new PhoneticErrorDefinition(""));
	    	put("K", new PhoneticErrorDefinition("CK"));
	    	put("Z", new PhoneticErrorDefinition("S"));
	    	put("R", new PhoneticErrorDefinition("AH", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_CONSONANT));
	    	put("J", new PhoneticErrorDefinition("Y", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("I", new PhoneticErrorDefinition("Y", ErrorLocation.AT_THE_END));
	    	put("Z", new PhoneticErrorDefinition("S", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_T, SpecialConstraint.IS_SLAVIC));
	    	put("H", new PhoneticErrorDefinition(""));
	    	put("X", new PhoneticErrorDefinition(""));
	    	put("Y", new PhoneticErrorDefinition("I", ErrorLocation.AT_THE_END));
	    	put("E", new PhoneticErrorDefinition("", ErrorLocation.AT_THE_END));
	    	put("J", new PhoneticErrorDefinition("Z"));
	    	put("Q", new PhoneticErrorDefinition("LW", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("D", new PhoneticErrorDefinition("T"));
	    	put("B", new PhoneticErrorDefinition("P"));
	    	put("Z", new PhoneticErrorDefinition("S", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("X", new PhoneticErrorDefinition("ECS"));
	    	put("X", new PhoneticErrorDefinition("S", ErrorLocation.AT_THE_BEGINNING));
	    	put("S", new PhoneticErrorDefinition("Z"));
	    	put("Z", new PhoneticErrorDefinition("TS", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_CONSONANT));
	    	put("S", new PhoneticErrorDefinition("J"));
	    	put("V", new PhoneticErrorDefinition("F"));
	    	put("Q", new PhoneticErrorDefinition("K"));
	    	put("L", new PhoneticErrorDefinition("LE", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_A_OR_I));
	    }});

	private static final List<String> CONST_PHONETIC_ERROR_KEYS = new ArrayList<String>();

	public static String phoneticError(String input, Random rnd) {
		if (CONST_PHONETIC_ERROR_KEYS.size() == 0) {
			synchronized(CONST_PHONETIC_ERROR_KEYS) {
				if (CONST_PHONETIC_ERROR_KEYS.size() == 0) {
					Set<Map.Entry<String, PhoneticErrorDefinition>> entries = CONST_PHONETIC_ERROR_MAP.entrySet();
					for (Map.Entry<String, PhoneticErrorDefinition> entry : entries)
						CONST_PHONETIC_ERROR_KEYS.add(entry.getKey());
				}
			}
		}
		CaseEnum caseType = SwapoutBase.determineCaseType(input);
		String inputUpperCase = input;
		if (caseType != CaseEnum.UpperCase)
			inputUpperCase = input.toUpperCase();
		for(int i = 0; i < CONST_PHONETIC_ERROR_KEYS.size(); i++) {
			int beforeIndex = rnd.nextInt(CONST_PHONETIC_ERROR_KEYS.size());
			String before = CONST_PHONETIC_ERROR_KEYS.get(beforeIndex);
			PhoneticErrorDefinition ped = CONST_PHONETIC_ERROR_MAP.get(before);
			String result = apply(input, inputUpperCase, caseType, before, ped);
			if (result != null && result.length() > 0)
				return result;
		}
		return input;
	}

	protected static String apply(String input, String inputUpperCase, CaseEnum caseType, String before, PhoneticErrorDefinition ped) {
		boolean found1 = true;
		switch (ped.errorLocation) {
			case AT_THE_BEGINNING: {
				found1 = inputUpperCase.startsWith(before);
			}
			break;
			case IN_THE_MIDDLE: {
				int index = inputUpperCase.indexOf(before);
				found1 = index > 0 && index < inputUpperCase.length() - before.length();
			}
			break;
			case AT_THE_END: {
				found1 = inputUpperCase.endsWith(before);
			}
			break;
			case ANYWHERE:
			default: {
				found1 = inputUpperCase.contains(before);
			}
		}
		if (!found1)
			return null;
		else if (ped.preConstr == PrecededByConstraint.NOTHING && ped.postConstr == FollowedByConstraint.NOTHING)
			return input;

		// Rule out impossible combinations: beginning and precondition: cannot be anything before the beginning
		if (ped.errorLocation == ErrorLocation.AT_THE_BEGINNING && (
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_VOWEL ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_CONSONANT ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_A_OR_I ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_U_OR_3_SPACES_AND_CGLRT ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_2_SPACES_AND_BHD ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH))
				return null;

		// Rule out impossible combinations: end and postcondition: cannot be anything after the end
		if (ped.errorLocation == ErrorLocation.AT_THE_END && (
			ped.postConstr == FollowedByConstraint.FOLLOWED_BY_VOWEL ||
			ped.postConstr == FollowedByConstraint.FOLLOWED_BY_CONSONANT))
				return null;

		boolean preConstrMet = false;
		boolean postConstrMet = false;
		int index = ped.errorLocation != ErrorLocation.AT_THE_END ? inputUpperCase.indexOf(before) : inputUpperCase.lastIndexOf(before);
		while (!preConstrMet && !postConstrMet && index != -1) {
			switch(ped.preConstr) {
				case PRECEEDED_BY_CONSONANT: {
					preConstrMet = (index > 0 && isConsonant(inputUpperCase.charAt(index - 1)));
				}
				break;
				case PRECEEDED_BY_VOWEL: {
					preConstrMet = (index > 0 && isVowel(inputUpperCase.charAt(index - 1)));
				}
				break;
				case PRECEEDED_BY_VAN_VON_OR_SCH: {
					if (index > 2) {
						String preStr = inputUpperCase.substring(index - 2 - 3, index - 2);
						preConstrMet = (preStr.equals("VAN") || preStr.equals("VON") || preStr.equals("SCH"));
					}
				}
				break;
				case PRECEEDED_BY_U_OR_3_SPACES_AND_CGLRT: {
					preConstrMet = ((index > 0 && inputUpperCase.charAt(index - 1) == 'U') ||
							(index > 2 && (inputUpperCase.charAt(index - 1) != ' ' || inputUpperCase.charAt(index - 2) != 'B' ||
							 inputUpperCase.charAt(index - 2) != 'H' || inputUpperCase.charAt(index - 2) != 'D')));
				}
				break;
				case PRECEEDED_BY_2_SPACES_AND_BHD: {
					preConstrMet = (index > 1 && (inputUpperCase.charAt(index - 1) != ' ' || inputUpperCase.charAt(index - 2) != 'B' ||
							inputUpperCase.charAt(index - 2) != 'H' || inputUpperCase.charAt(index - 2) != 'D'));
				}
				break;
				case PRECEEDED_BY_A_OR_I: {
					char preChar = inputUpperCase.charAt(index - 1);
					preConstrMet = (index > 0 && (preChar == 'A' || preChar == 'I'));
				}
				break;
				case NOT_PRECEEDED_BY_E_OR_I: {
					if (index > 0) {
						char preChar = inputUpperCase.charAt(index - 1);
						preConstrMet = (preChar != 'E' && preChar != 'I');
					}
				}
				break;
				case NOT_PRECEEDED_BY_I: {
					preConstrMet = (index > 0 && inputUpperCase.charAt(index - 1) != 'I');
				}
				break;
				case NOT_PRECEEDED_BY_T: {
					preConstrMet = (index > 0 && inputUpperCase.charAt(index - 1) != 'T');
				}
				break;
				case NOT_PRECEEDED_BY_SPACE_AND_A_VOWEL: {
					preConstrMet = (index > 1 && (inputUpperCase.charAt(index - 1) != ' ' || !isVowel(inputUpperCase.charAt(index - 2))));
				}
				break;
				case NOTHING:
				default: {
					preConstrMet = true;
				}
			}
			int index2 = index + before.length();
			switch(ped.postConstr) {
				case FOLLOWED_BY_CONSONANT: {
					postConstrMet = (index2 < inputUpperCase.length() && isConsonant(inputUpperCase.charAt(index2)));
				}
				break;
				case FOLLOWED_BY_VOWEL: {
					postConstrMet = (index2 < inputUpperCase.length() && isVowel(inputUpperCase.charAt(index2)));
				}
				break;
				case NOT_FOLLOWED_BY_SPACE_AND_HU: {
					if (index2 + 3 < inputUpperCase.length()) {
						String postStr = inputUpperCase.substring(index2 + 1, index2 + 3);
						postConstrMet = (inputUpperCase.charAt(index2) == ' ' && postStr.equals("HU"));
					}					
				}
				break;
				case NOT_FOLLOWED_BY_VOWEL_OR_W: {
					postConstrMet = (index2 < inputUpperCase.length() && (isVowel(inputUpperCase.charAt(index2)) || inputUpperCase.charAt(index2) == 'W'));
				}
				break;
				case NOT_FOLLOWED_BY_E_OR_I: {
					postConstrMet = (index2 < inputUpperCase.length() && (inputUpperCase.charAt(index2) == 'E' || inputUpperCase.charAt(index2) == 'I'));
				}
				break;
				case NOT_FOLLOWED_BY_RGY_OR_OGY: {
					if (index2 + 3 < inputUpperCase.length()) {
						String postStr = inputUpperCase.substring(index2, index2 + 3);
						postConstrMet = (postStr.equals("RGY") || postStr.equals("OGY"));
					}					
				}
				case NOTHING:
				default: {
					postConstrMet = true;
				}
			}
			if (!preConstrMet || !postConstrMet) {
				if (ped.errorLocation == ErrorLocation.AT_THE_BEGINNING || ped.errorLocation == ErrorLocation.AT_THE_END)
					index = -1;	// If any of the constraints weren't met, then it's over
				else
					index = inputUpperCase.indexOf(before, index + 1);
			}
		}
		if (index == -1)
			return null;
		
		StringBuilder sb = new StringBuilder(input);
		sb.replace(index, index + before.length(), ped.after.getCase(caseType));
		return sb.toString();
	}
	
	protected static boolean isVowel(char ch) {
		return ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U';
	}

	protected static boolean isConsonant(char ch) {
		return Character.isLetter(ch) && !isVowel(ch);
	}
}
