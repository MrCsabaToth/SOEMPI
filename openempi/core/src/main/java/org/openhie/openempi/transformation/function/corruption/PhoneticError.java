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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.ErrorLocation;
import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.FollowedByConstraint;
import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.PrecededByConstraint;
import org.openhie.openempi.transformation.function.corruption.PhoneticErrorDefinition.SpecialConstraint;

public class PhoneticError
{
	private static final Map<String, PhoneticErrorDefinition> CONST_PHONETIC_ERROR_MAP =
	    Collections.unmodifiableMap(new HashMap<String, PhoneticErrorDefinition>() {{
	    	put("san jose", new PhoneticErrorDefinition("san hose", ErrorLocation.IN_THE_MIDDLE));
	    	put("charis", new PhoneticErrorDefinition("karis", ErrorLocation.AT_THE_BEGINNING));
	    	put("orchid", new PhoneticErrorDefinition("orkid"));
	    	put("bacher", new PhoneticErrorDefinition("baker", ErrorLocation.IN_THE_MIDDLE));
	    	put("charac", new PhoneticErrorDefinition("karak", ErrorLocation.AT_THE_BEGINNING));
	    	put("archit", new PhoneticErrorDefinition("arkit"));
	    	put("orches", new PhoneticErrorDefinition("orkes"));
	    	put("macher", new PhoneticErrorDefinition("maker", ErrorLocation.IN_THE_MIDDLE));
	    	put("van ch", new PhoneticErrorDefinition("van k", ErrorLocation.AT_THE_BEGINNING));
	    	put("caesar", new PhoneticErrorDefinition("sesar", ErrorLocation.AT_THE_BEGINNING));
	    	put("von ch", new PhoneticErrorDefinition("von k", ErrorLocation.AT_THE_BEGINNING));
	    	put("sholz", new PhoneticErrorDefinition("solz"));
	    	put("shoek", new PhoneticErrorDefinition("soek"));
	    	put("schem", new PhoneticErrorDefinition("skem"));
	    	put("ucces", new PhoneticErrorDefinition("ukses"));
	    	put("sheim", new PhoneticErrorDefinition("seim"));
	    	put("schoo", new PhoneticErrorDefinition("skoo"));
	    	put("schuy", new PhoneticErrorDefinition("skuy"));
	    	put("schen", new PhoneticErrorDefinition("xen"));
	    	put("scher", new PhoneticErrorDefinition("xer"));
	    	put("uccee", new PhoneticErrorDefinition("uksee"));
	    	put("sched", new PhoneticErrorDefinition("sked"));
	    	put("hroug", new PhoneticErrorDefinition("rew"));
	    	put("sholm", new PhoneticErrorDefinition("solm"));
	    	put("lough", new PhoneticErrorDefinition("low"));
	    	put("sugar", new PhoneticErrorDefinition("xugar", ErrorLocation.AT_THE_BEGINNING));
	    	put("ochl", new PhoneticErrorDefinition("okl"));
	    	put("gnes", new PhoneticErrorDefinition("ns", ErrorLocation.AT_THE_END));
	    	put("uchb", new PhoneticErrorDefinition("ukb"));
	    	put("gney", new PhoneticErrorDefinition("ney", ErrorLocation.IN_THE_MIDDLE, SpecialConstraint.NOT_SLAVIC));
	    	put("achl", new PhoneticErrorDefinition("akl"));
	    	put("augh", new PhoneticErrorDefinition("arf"));
	    	put("echw", new PhoneticErrorDefinition("ekw"));
	    	put("achh", new PhoneticErrorDefinition("akh"));
	    	put("ochv", new PhoneticErrorDefinition("okv"));
	    	put("chia", new PhoneticErrorDefinition("kia"));
	    	put("uchf", new PhoneticErrorDefinition("ukf"));
	    	put("chia", new PhoneticErrorDefinition("kia", ErrorLocation.AT_THE_BEGINNING));
	    	put("wicz", new PhoneticErrorDefinition("ts"));
	    	put("chym", new PhoneticErrorDefinition("kym", ErrorLocation.AT_THE_BEGINNING));
	    	put("echm", new PhoneticErrorDefinition("ekm"));
	    	put("uchr", new PhoneticErrorDefinition("ukr"));
	    	put("echh", new PhoneticErrorDefinition("ekh"));
	    	put("aisz", new PhoneticErrorDefinition("ai", ErrorLocation.AT_THE_END));
	    	put("gier", new PhoneticErrorDefinition("jier", ErrorLocation.IN_THE_MIDDLE));
	    	put("achr", new PhoneticErrorDefinition("akr"));
	    	put("achv", new PhoneticErrorDefinition("akv"));
	    	put("echn", new PhoneticErrorDefinition("ekn"));
	    	put("eaux", new PhoneticErrorDefinition("eauks", ErrorLocation.AT_THE_END));
	    	put("echr", new PhoneticErrorDefinition("ekr"));
	    	put("oggi", new PhoneticErrorDefinition("oji", ErrorLocation.IN_THE_MIDDLE));
	    	put("ochw", new PhoneticErrorDefinition("okw"));
	    	put("chae", new PhoneticErrorDefinition("kae", ErrorLocation.IN_THE_MIDDLE));
	    	put("acce", new PhoneticErrorDefinition("aske", ErrorLocation.ANYWHERE, FollowedByConstraint.NOT_FOLLOWED_BY_SPACE_AND_HU));
	    	put("illo", new PhoneticErrorDefinition("ilo", ErrorLocation.AT_THE_END));
	    	put("mpts", new PhoneticErrorDefinition("mps"));
	    	put("chem", new PhoneticErrorDefinition("kem", ErrorLocation.AT_THE_BEGINNING));
	    	put("ochh", new PhoneticErrorDefinition("okh"));
	    	put("achm", new PhoneticErrorDefinition("akm"));
	    	put("iaux", new PhoneticErrorDefinition("iauks", ErrorLocation.AT_THE_END));
	    	put("uchn", new PhoneticErrorDefinition("ukn"));
	    	put("achn", new PhoneticErrorDefinition("akn"));
	    	put("uchm", new PhoneticErrorDefinition("ukm"));
	    	put("chor", new PhoneticErrorDefinition("kor", ErrorLocation.AT_THE_BEGINNING));
	    	put("tion", new PhoneticErrorDefinition("xion"));
	    	put("ghne", new PhoneticErrorDefinition("ne"));
	    	put("wicz", new PhoneticErrorDefinition("wis"));
	    	put("ochf", new PhoneticErrorDefinition("okf"));
	    	put("achf", new PhoneticErrorDefinition("akf"));
	    	put("acch", new PhoneticErrorDefinition("askh", ErrorLocation.ANYWHERE, FollowedByConstraint.NOT_FOLLOWED_BY_SPACE_AND_HU));
	    	put("oiss", new PhoneticErrorDefinition("oi", ErrorLocation.AT_THE_END));
	    	put("illa", new PhoneticErrorDefinition("ila", ErrorLocation.AT_THE_END));
	    	put("uchh", new PhoneticErrorDefinition("ukh"));
	    	put("ough", new PhoneticErrorDefinition("of"));
	    	put("ochn", new PhoneticErrorDefinition("okn"));
	    	put("uchw", new PhoneticErrorDefinition("ukw"));
	    	put("echl", new PhoneticErrorDefinition("ekl"));
	    	put("echv", new PhoneticErrorDefinition("ekv"));
	    	put("aggi", new PhoneticErrorDefinition("aji", ErrorLocation.IN_THE_MIDDLE));
	    	put("ochm", new PhoneticErrorDefinition("okm"));
	    	put("aiss", new PhoneticErrorDefinition("ai", ErrorLocation.AT_THE_END));
	    	put("eaux", new PhoneticErrorDefinition("oh", ErrorLocation.AT_THE_END));
	    	put("aggi", new PhoneticErrorDefinition("aki", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("acci", new PhoneticErrorDefinition("aski", ErrorLocation.ANYWHERE, FollowedByConstraint.NOT_FOLLOWED_BY_SPACE_AND_HU));
	    	put("witz", new PhoneticErrorDefinition("ts"));
	    	put("exci", new PhoneticErrorDefinition("ecs"));
	    	put("echf", new PhoneticErrorDefinition("ekf"));
	    	put("alle", new PhoneticErrorDefinition("ale", ErrorLocation.AT_THE_END));
	    	put("echb", new PhoneticErrorDefinition("ekb"));
	    	put("uchv", new PhoneticErrorDefinition("ukv"));
	    	put("uchl", new PhoneticErrorDefinition("ukl"));
	    	put("achw", new PhoneticErrorDefinition("akw"));
	    	put("achb", new PhoneticErrorDefinition("akb"));
	    	put("ochr", new PhoneticErrorDefinition("okr"));
	    	put("oggi", new PhoneticErrorDefinition("oki", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("oisz", new PhoneticErrorDefinition("oi", ErrorLocation.AT_THE_END));
	    	put("ochb", new PhoneticErrorDefinition("okb"));
	    	put("chn", new PhoneticErrorDefinition("kn", ErrorLocation.AT_THE_BEGINNING));
	    	put("oux", new PhoneticErrorDefinition("ouks", ErrorLocation.AT_THE_END));
	    	put("cia", new PhoneticErrorDefinition("sia"));
	    	put("cce", new PhoneticErrorDefinition("xi"));
	    	put("zzi", new PhoneticErrorDefinition("si"));
	    	put("sci", new PhoneticErrorDefinition("siif"));
	    	put("chf", new PhoneticErrorDefinition("kf", ErrorLocation.AT_THE_BEGINNING));
	    	put("btl", new PhoneticErrorDefinition("tl"));
	    	put("isl", new PhoneticErrorDefinition("il"));
	    	put("sch", new PhoneticErrorDefinition("x", ErrorLocation.AT_THE_END));
	    	put("mps", new PhoneticErrorDefinition("ms"));
	    	put("wsk", new PhoneticErrorDefinition("vskie", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("chb", new PhoneticErrorDefinition("kb", ErrorLocation.AT_THE_BEGINNING));
	    	put("gne", new PhoneticErrorDefinition("n", ErrorLocation.AT_THE_END));
	    	put("chw", new PhoneticErrorDefinition("kw", ErrorLocation.AT_THE_BEGINNING));
	    	put("sch", new PhoneticErrorDefinition("sh"));
	    	put("dgy", new PhoneticErrorDefinition("jy"));
	    	put("rie", new PhoneticErrorDefinition("ry"));
	    	put("ger", new PhoneticErrorDefinition("ker", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_E_OR_I, SpecialConstraint.DOESNT_CONTAIN_DANGER_RANGER_MANGER));
	    	put("ges", new PhoneticErrorDefinition("kes", ErrorLocation.AT_THE_BEGINNING));
	    	put("who", new PhoneticErrorDefinition("wo"));
	    	put("chv", new PhoneticErrorDefinition("kv", ErrorLocation.AT_THE_BEGINNING));
	    	put("sce", new PhoneticErrorDefinition("se"));
	    	put("tnt", new PhoneticErrorDefinition("ent", ErrorLocation.AT_THE_END));
	    	put("tch", new PhoneticErrorDefinition("x"));
	    	put("tia", new PhoneticErrorDefinition("xia"));
	    	put("whe", new PhoneticErrorDefinition("we"));
	    	put("sch", new PhoneticErrorDefinition("x", ErrorLocation.IN_THE_MIDDLE));
	    	put("gey", new PhoneticErrorDefinition("key", ErrorLocation.AT_THE_BEGINNING));
	    	put("dge", new PhoneticErrorDefinition("je"));
	    	put("tth", new PhoneticErrorDefinition("t"));
	    	put("ght", new PhoneticErrorDefinition("t"));
	    	put("gep", new PhoneticErrorDefinition("kep", ErrorLocation.AT_THE_BEGINNING));
	    	put("sch", new PhoneticErrorDefinition("x", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.NOT_FOLLOWED_BY_VOWEL_OR_W));
	    	put("c g", new PhoneticErrorDefinition("k"));
	    	put("ger", new PhoneticErrorDefinition("ker", ErrorLocation.AT_THE_BEGINNING));
	    	put("geb", new PhoneticErrorDefinition("keb", ErrorLocation.AT_THE_BEGINNING));
	    	put("chm", new PhoneticErrorDefinition("km", ErrorLocation.AT_THE_BEGINNING));
	    	put("whi", new PhoneticErrorDefinition("wi"));
	    	put("ach", new PhoneticErrorDefinition("k", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.NOT_FOLLOWED_BY_E_OR_I));
	    	put("zzo", new PhoneticErrorDefinition("s"));
	    	put("chs", new PhoneticErrorDefinition("ks"));
	    	put("sch", new PhoneticErrorDefinition("sk", ErrorLocation.AT_THE_BEGINNING));
	    	put("cio", new PhoneticErrorDefinition("sio"));
	    	put("gil", new PhoneticErrorDefinition("kil", ErrorLocation.AT_THE_BEGINNING));
	    	put("cks", new PhoneticErrorDefinition("x", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("gib", new PhoneticErrorDefinition("kib", ErrorLocation.AT_THE_BEGINNING));
	    	put("stl", new PhoneticErrorDefinition("sl", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("ghi", new PhoneticErrorDefinition("j", ErrorLocation.AT_THE_BEGINNING));
	    	put("les", new PhoneticErrorDefinition("iles", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_CONSONANT));
	    	put("aux", new PhoneticErrorDefinition("auks", ErrorLocation.AT_THE_END));
	    	put("chh", new PhoneticErrorDefinition("kh", ErrorLocation.AT_THE_BEGINNING));
	    	put("ghn", new PhoneticErrorDefinition("n"));
	    	put("cia", new PhoneticErrorDefinition("xia"));
	    	put("yth", new PhoneticErrorDefinition("ith"));
	    	put("wha", new PhoneticErrorDefinition("wa"));
	    	put("dgi", new PhoneticErrorDefinition("ji"));
	    	put("lle", new PhoneticErrorDefinition("le"));
	    	put("umb", new PhoneticErrorDefinition("um"));
	    	put("stl", new PhoneticErrorDefinition("sl"));
	    	put("get", new PhoneticErrorDefinition("ket", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("tch", new PhoneticErrorDefinition("ch"));
	    	put("gli", new PhoneticErrorDefinition("kli", ErrorLocation.IN_THE_MIDDLE, SpecialConstraint.NOT_SLAVIC));
	    	put("scy", new PhoneticErrorDefinition("sy"));
	    	put("ysl", new PhoneticErrorDefinition("yl"));
	    	put("c c", new PhoneticErrorDefinition("k"));
	    	put("c q", new PhoneticErrorDefinition("k"));
	    	put("gei", new PhoneticErrorDefinition("kei", ErrorLocation.AT_THE_BEGINNING));
	    	put("cch", new PhoneticErrorDefinition("xh"));
	    	put("gin", new PhoneticErrorDefinition("kin", ErrorLocation.AT_THE_BEGINNING));
	    	put("cci", new PhoneticErrorDefinition("xi"));
	    	put("zza", new PhoneticErrorDefinition("sa"));
	    	put("gie", new PhoneticErrorDefinition("kie", ErrorLocation.AT_THE_BEGINNING));
	    	put("ned", new PhoneticErrorDefinition("nd", ErrorLocation.AT_THE_END));
	    	put("chr", new PhoneticErrorDefinition("kr", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("why", new PhoneticErrorDefinition("wy"));
	    	put("chl", new PhoneticErrorDefinition("kl", ErrorLocation.AT_THE_BEGINNING));
	    	put("mpt", new PhoneticErrorDefinition("mt"));
	    	put("wsk", new PhoneticErrorDefinition("vskie", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("whu", new PhoneticErrorDefinition("wu"));
	    	put("tsj", new PhoneticErrorDefinition("ch", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("cht", new PhoneticErrorDefinition("kt"));
	    	put("gel", new PhoneticErrorDefinition("kel", ErrorLocation.AT_THE_BEGINNING));
	    	put("cie", new PhoneticErrorDefinition("sie"));
	    	put("mch", new PhoneticErrorDefinition("mk", ErrorLocation.IN_THE_MIDDLE));
	    	put("cg", new PhoneticErrorDefinition("k"));
	    	put("ho", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("ei", new PhoneticErrorDefinition("i"));
	    	put("lz", new PhoneticErrorDefinition("lsh"));
	    	put("wz", new PhoneticErrorDefinition("z", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("ts", new PhoneticErrorDefinition("t", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("au", new PhoneticErrorDefinition("o"));
	    	put("gy", new PhoneticErrorDefinition("ky", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_E_OR_I, FollowedByConstraint.NOT_FOLLOWED_BY_RGY_OR_OGY));
	    	put("zh", new PhoneticErrorDefinition("jh"));
	    	put("zz", new PhoneticErrorDefinition("s"));
	    	put("tt", new PhoneticErrorDefinition("t"));
	    	put("lj", new PhoneticErrorDefinition("ld", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("sl", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_BEGINNING));
	    	put("ce", new PhoneticErrorDefinition("se"));
	    	put("ia", new PhoneticErrorDefinition("ya"));
	    	put("hr", new PhoneticErrorDefinition("ah", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_CONSONANT));
	    	put("vv", new PhoneticErrorDefinition("ff"));
	    	put("cz", new PhoneticErrorDefinition("ch", ErrorLocation.IN_THE_MIDDLE));
	    	put("gi", new PhoneticErrorDefinition("ji", ErrorLocation.IN_THE_MIDDLE));
	    	put("kn", new PhoneticErrorDefinition("n", ErrorLocation.AT_THE_BEGINNING));
	    	put("dt", new PhoneticErrorDefinition("t", ErrorLocation.AT_THE_END));
	    	put("wh", new PhoneticErrorDefinition("h"));
	    	put("re", new PhoneticErrorDefinition("ar", ErrorLocation.AT_THE_END));
	    	put("mn", new PhoneticErrorDefinition("n", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("oh", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("ff", new PhoneticErrorDefinition("vv"));
	    	put("gi", new PhoneticErrorDefinition("ki", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("ff", new PhoneticErrorDefinition("v"));
	    	put("wr", new PhoneticErrorDefinition("r"));
	    	put("dg", new PhoneticErrorDefinition("g"));
	    	put("th", new PhoneticErrorDefinition("t"));
	    	put("jr", new PhoneticErrorDefinition("dr", ErrorLocation.IN_THE_MIDDLE));
	    	put("nx", new PhoneticErrorDefinition("nks"));
	    	put("jc", new PhoneticErrorDefinition("k", ErrorLocation.AT_THE_END));
	    	put("cz", new PhoneticErrorDefinition("c", ErrorLocation.AT_THE_BEGINNING));
	    	put("yj", new PhoneticErrorDefinition("y", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("cl", new PhoneticErrorDefinition("kl", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("tj", new PhoneticErrorDefinition("ch", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("hi", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("jj", new PhoneticErrorDefinition("j"));
	    	put("nk", new PhoneticErrorDefinition("ng"));
	    	put("dt", new PhoneticErrorDefinition("t"));
	    	put("gg", new PhoneticErrorDefinition("k"));
	    	put("ll", new PhoneticErrorDefinition("l"));
	    	put("ks", new PhoneticErrorDefinition("x", ErrorLocation.IN_THE_MIDDLE));
	    	put("gn", new PhoneticErrorDefinition("kn", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, SpecialConstraint.STARTS_WITH_A_VOWEL_AND_NOT_SLAVIC));
	    	put("le", new PhoneticErrorDefinition("ile", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_CONSONANT));
	    	put("dg", new PhoneticErrorDefinition("tk"));
	    	put("nn", new PhoneticErrorDefinition("n"));
	    	put("rz", new PhoneticErrorDefinition("rsh"));
	    	put("ow", new PhoneticErrorDefinition("o", ErrorLocation.AT_THE_END));
	    	put("ew", new PhoneticErrorDefinition("e", ErrorLocation.AT_THE_END));
	    	put("cy", new PhoneticErrorDefinition("sy"));
	    	put("ee", new PhoneticErrorDefinition("i"));
	    	put("pf", new PhoneticErrorDefinition("f", ErrorLocation.AT_THE_BEGINNING));
	    	put("ge", new PhoneticErrorDefinition("ke", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH));
	    	put("ch", new PhoneticErrorDefinition("x", ErrorLocation.IN_THE_MIDDLE));
	    	put("yh", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("gy", new PhoneticErrorDefinition("jy", ErrorLocation.IN_THE_MIDDLE));
	    	put("sz", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_END));
	    	put("ci", new PhoneticErrorDefinition("si"));
	    	put("sh", new PhoneticErrorDefinition("x"));
	    	put("ou", new PhoneticErrorDefinition("o"));
	    	put("uh", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("qq", new PhoneticErrorDefinition("k"));
	    	put("pb", new PhoneticErrorDefinition("p"));
	    	put("ee", new PhoneticErrorDefinition("ea", ErrorLocation.AT_THE_END));
	    	put("ah", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("gc", new PhoneticErrorDefinition("k", ErrorLocation.AT_THE_END));
	    	put("nc", new PhoneticErrorDefinition("nk"));
	    	put("dl", new PhoneticErrorDefinition("dil", ErrorLocation.AT_THE_END));
	    	put("oo", new PhoneticErrorDefinition("u"));
	    	put("gy", new PhoneticErrorDefinition("ky", ErrorLocation.AT_THE_BEGINNING));
	    	put("ch", new PhoneticErrorDefinition("x", ErrorLocation.AT_THE_BEGINNING));
	    	put("ha", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("gh", new PhoneticErrorDefinition("", ErrorLocation.AT_THE_BEGINNING));
	    	put("wr", new PhoneticErrorDefinition("r", ErrorLocation.AT_THE_BEGINNING));
	    	put("co", new PhoneticErrorDefinition("ko"));
	    	put("gn", new PhoneticErrorDefinition("n", ErrorLocation.AT_THE_BEGINNING));
	    	put("ge", new PhoneticErrorDefinition("jy", ErrorLocation.IN_THE_MIDDLE));
	    	put("hr", new PhoneticErrorDefinition("ah", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("bb", new PhoneticErrorDefinition("p"));
	    	put("es", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_END));
	    	put("sz", new PhoneticErrorDefinition("s"));
	    	put("ca", new PhoneticErrorDefinition("ka"));
	    	put("eh", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("gh", new PhoneticErrorDefinition("k", ErrorLocation.ANYWHERE, PrecededByConstraint.NOT_PRECEEDED_BY_I));
	    	put("cq", new PhoneticErrorDefinition("k"));
	    	put("mb", new PhoneticErrorDefinition("m", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("sm", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_BEGINNING));
	    	put("tl", new PhoneticErrorDefinition("til", ErrorLocation.AT_THE_END));
	    	put("ey", new PhoneticErrorDefinition("y"));
	    	put("gh", new PhoneticErrorDefinition("f", ErrorLocation.ANYWHERE, PrecededByConstraint.PRECEEDED_BY_U_OR_3_SPACES_AND_CGLRT));
	    	put("ya", new PhoneticErrorDefinition("ia"));
	    	put("gh", new PhoneticErrorDefinition("k", ErrorLocation.AT_THE_BEGINNING));
	    	put("he", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("ry", new PhoneticErrorDefinition("rie"));
	    	put("ps", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_BEGINNING));
	    	put("aa", new PhoneticErrorDefinition("ar"));
	    	put("gn", new PhoneticErrorDefinition("n", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.FOLLOWED_BY_CONSONANT));
	    	put("ss", new PhoneticErrorDefinition("as", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("hy", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("dd", new PhoneticErrorDefinition("t"));
	    	put("sc", new PhoneticErrorDefinition("sk"));
	    	put("cy", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_BEGINNING));
	    	put("ss", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_END));
	    	put("zs", new PhoneticErrorDefinition("s"));
	    	put("td", new PhoneticErrorDefinition("t"));
	    	put("kk", new PhoneticErrorDefinition("k"));
	    	put("cy", new PhoneticErrorDefinition("si"));
	    	put("cc", new PhoneticErrorDefinition("k"));
	    	put("aw", new PhoneticErrorDefinition("a", ErrorLocation.AT_THE_END));
	    	put("ie", new PhoneticErrorDefinition("i"));
	    	put("xx", new PhoneticErrorDefinition(""));
	    	put("pn", new PhoneticErrorDefinition("n", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("gn", new PhoneticErrorDefinition("n", ErrorLocation.AT_THE_END));
	    	put("gh", new PhoneticErrorDefinition("", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_2_SPACES_AND_BHD));
	    	put("pp", new PhoneticErrorDefinition("p"));
	    	put("cu", new PhoneticErrorDefinition("ku"));
	    	put("ph", new PhoneticErrorDefinition("f"));
	    	put("hu", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("pt", new PhoneticErrorDefinition("t", ErrorLocation.AT_THE_BEGINNING));
	    	put("vv", new PhoneticErrorDefinition("f"));
	    	put("mm", new PhoneticErrorDefinition("m"));
	    	put("rr", new PhoneticErrorDefinition("r"));
	    	put("gh", new PhoneticErrorDefinition("e", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("ct", new PhoneticErrorDefinition("kt"));
	    	put("gh", new PhoneticErrorDefinition("k", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_SPACE_AND_A_VOWEL));
	    	put("yw", new PhoneticErrorDefinition("y", ErrorLocation.AT_THE_END));
	    	put("iw", new PhoneticErrorDefinition("i", ErrorLocation.AT_THE_END));
	    	put("sn", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_BEGINNING));
	    	put("ih", new PhoneticErrorDefinition("h", ErrorLocation.AT_THE_BEGINNING));
	    	put("uw", new PhoneticErrorDefinition("u", ErrorLocation.AT_THE_END));
	    	put("sw", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_BEGINNING));
	    	put("ck", new PhoneticErrorDefinition("k"));
	    	put("ng", new PhoneticErrorDefinition("nk"));
	    	put("ff", new PhoneticErrorDefinition("f"));
	    	put("gh", new PhoneticErrorDefinition("g", ErrorLocation.AT_THE_BEGINNING));
	    	put("cr", new PhoneticErrorDefinition("kr", ErrorLocation.AT_THE_BEGINNING, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("xc", new PhoneticErrorDefinition(""));
	    	put("zz", new PhoneticErrorDefinition("ts"));
	    	put("t", new PhoneticErrorDefinition("d"));
	    	put("g", new PhoneticErrorDefinition("k"));
	    	put("q", new PhoneticErrorDefinition("kw", ErrorLocation.AT_THE_BEGINNING));
	    	put("c", new PhoneticErrorDefinition("k"));
	    	put("s", new PhoneticErrorDefinition("st"));
	    	put("z", new PhoneticErrorDefinition("j"));
	    	put("f", new PhoneticErrorDefinition("v"));
	    	put("r", new PhoneticErrorDefinition("ah", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_VOWEL));
	    	put("j", new PhoneticErrorDefinition("s"));
	    	put("w", new PhoneticErrorDefinition(""));
	    	put("k", new PhoneticErrorDefinition("ck"));
	    	put("z", new PhoneticErrorDefinition("s"));
	    	put("r", new PhoneticErrorDefinition("ah", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_CONSONANT));
	    	put("j", new PhoneticErrorDefinition("y", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("i", new PhoneticErrorDefinition("y", ErrorLocation.AT_THE_END));
	    	put("z", new PhoneticErrorDefinition("s", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.NOT_PRECEEDED_BY_T, SpecialConstraint.IS_SLAVIC));
	    	put("h", new PhoneticErrorDefinition(""));
	    	put("x", new PhoneticErrorDefinition(""));
	    	put("y", new PhoneticErrorDefinition("i", ErrorLocation.AT_THE_END));
	    	put("e", new PhoneticErrorDefinition("", ErrorLocation.AT_THE_END));
	    	put("j", new PhoneticErrorDefinition("z"));
	    	put("q", new PhoneticErrorDefinition("kw", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_VOWEL, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("d", new PhoneticErrorDefinition("t"));
	    	put("b", new PhoneticErrorDefinition("p"));
	    	put("z", new PhoneticErrorDefinition("s", ErrorLocation.IN_THE_MIDDLE, FollowedByConstraint.FOLLOWED_BY_VOWEL));
	    	put("x", new PhoneticErrorDefinition("ecs"));
	    	put("x", new PhoneticErrorDefinition("s", ErrorLocation.AT_THE_BEGINNING));
	    	put("s", new PhoneticErrorDefinition("z"));
	    	put("z", new PhoneticErrorDefinition("ts", ErrorLocation.IN_THE_MIDDLE, PrecededByConstraint.PRECEEDED_BY_CONSONANT));
	    	put("s", new PhoneticErrorDefinition("j"));
	    	put("v", new PhoneticErrorDefinition("f"));
	    	put("q", new PhoneticErrorDefinition("k"));
	    	put("l", new PhoneticErrorDefinition("le", ErrorLocation.AT_THE_END, PrecededByConstraint.PRECEEDED_BY_A_OR_I));
	    }});


	public static String phoneticError(String input, Random rnd) {
		StringBuilder sb = new StringBuilder(input);
		Set<String> beforeKeys = CONST_PHONETIC_ERROR_MAP.keySet();
		String[] befores = (String[])beforeKeys.toArray();
		for(int i = 0; i < befores.length; i++) {
			int beforeIndex = rnd.nextInt(befores.length);
			String before = befores[beforeIndex];
			PhoneticErrorDefinition ped = CONST_PHONETIC_ERROR_MAP.get(before);
			if (apply(sb, input, before, ped))
				break;
		}
		return sb.toString();
	}

	protected static boolean apply(StringBuilder sb, String input, String before, PhoneticErrorDefinition ped) {
		boolean found1 = true;
		switch (ped.errorLocation) {
			case AT_THE_BEGINNING: {
				found1 = input.startsWith(before);
			}
			break;
			case IN_THE_MIDDLE: {
				int index = input.indexOf(before);
				found1 = index > 0 && index < input.length() - before.length();
			}
			break;
			case AT_THE_END: {
				found1 = input.endsWith(before);
			}
			break;
			case ANYWHERE:
			default: {
				found1 = input.contains(before);
			}
		}
		if (!found1)
			return false;
		else if (ped.preConstr == PrecededByConstraint.NOTHING && ped.postConstr == FollowedByConstraint.NOTHING)
			return true;

		// Rule out impossible combinations: beginning and precondition: cannot be anything before the beginning
		if (ped.errorLocation == ErrorLocation.AT_THE_BEGINNING && (
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_VOWEL ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_CONSONANT ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_A_OR_I ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_U_OR_3_SPACES_AND_CGLRT ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_2_SPACES_AND_BHD ||
			ped.preConstr == PrecededByConstraint.PRECEEDED_BY_VAN_VON_OR_SCH))
				return false;

		// Rule out impossible combinations: end and postcondition: cannot be anything after the end
		if (ped.errorLocation == ErrorLocation.AT_THE_END && (
			ped.postConstr == FollowedByConstraint.FOLLOWED_BY_VOWEL ||
			ped.postConstr == FollowedByConstraint.FOLLOWED_BY_CONSONANT))
				return false;

		boolean preConstrMet = false;
		boolean postConstrMet = false;
		int index = ped.errorLocation != ErrorLocation.AT_THE_END ? input.indexOf(before) : input.lastIndexOf(before);
		while (!preConstrMet && !postConstrMet && index != -1) {
			switch(ped.preConstr) {
				case PRECEEDED_BY_CONSONANT: {
					preConstrMet = (index > 0 && isConsonant(input.charAt(index - 1)));
				}
				break;
				case PRECEEDED_BY_VOWEL: {
					preConstrMet = (index > 0 && isVowel(input.charAt(index - 1)));
				}
				break;
				case PRECEEDED_BY_VAN_VON_OR_SCH: {
					if (index > 2) {
						String preStr = input.substring(index - 2 - 3, index - 2);
						preConstrMet = (preStr.equals("van") || preStr.equals("von") || preStr.equals("sch"));
					}
				}
				break;
				case PRECEEDED_BY_U_OR_3_SPACES_AND_CGLRT: {
					preConstrMet = ((index > 0 && input.charAt(index - 1) == 'u') ||
							(index > 2 && (input.charAt(index - 1) != ' ' || input.charAt(index - 2) != 'b' ||
							input.charAt(index - 2) != 'h' || input.charAt(index - 2) != 'd')));
				}
				break;
				case PRECEEDED_BY_2_SPACES_AND_BHD: {
					preConstrMet = (index > 1 && (input.charAt(index - 1) != ' ' || input.charAt(index - 2) != 'b' ||
							input.charAt(index - 2) != 'h' || input.charAt(index - 2) != 'd'));
				}
				break;
				case PRECEEDED_BY_A_OR_I: {
					char preChar = input.charAt(index - 1);
					preConstrMet = (index > 0 && (preChar == 'a' || preChar == 'i'));
				}
				break;
				case NOT_PRECEEDED_BY_E_OR_I: {
					if (index > 0) {
						char preChar = input.charAt(index - 1);
						preConstrMet = (preChar != 'e' && preChar != 'i');
					}
				}
				break;
				case NOT_PRECEEDED_BY_I: {
					preConstrMet = (index > 0 && input.charAt(index - 1) != 'i');
				}
				break;
				case NOT_PRECEEDED_BY_T: {
					preConstrMet = (index > 0 && input.charAt(index - 1) != 't');
				}
				break;
				case NOT_PRECEEDED_BY_SPACE_AND_A_VOWEL: {
					preConstrMet = (index > 1 && (input.charAt(index - 1) != ' ' || !isVowel(input.charAt(index - 2))));
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
					postConstrMet = (index2 < input.length() && isConsonant(input.charAt(index2)));
				}
				break;
				case FOLLOWED_BY_VOWEL: {
					postConstrMet = (index2 < input.length() && isVowel(input.charAt(index2)));
				}
				break;
				case NOT_FOLLOWED_BY_SPACE_AND_HU: {
					if (index2 + 3 < input.length()) {
						String postStr = input.substring(index2 + 1, index2 + 3);
						postConstrMet = (input.charAt(index2) == ' ' && postStr.equals("hu"));
					}					
				}
				break;
				case NOT_FOLLOWED_BY_VOWEL_OR_W: {
					postConstrMet = (index2 < input.length() && (isVowel(input.charAt(index2)) || input.charAt(index2) == 'w'));
				}
				break;
				case NOT_FOLLOWED_BY_E_OR_I: {
					postConstrMet = (index2 < input.length() && (input.charAt(index2) == 'e' || input.charAt(index2) == 'i'));
				}
				break;
				case NOT_FOLLOWED_BY_RGY_OR_OGY: {
					if (index2 + 3 < input.length()) {
						String postStr = input.substring(index2, index2 + 3);
						postConstrMet = (postStr.equals("rgy") || postStr.equals("ogy"));
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
					index = input.indexOf(before, index + 1);
			}
		}
		if (index == -1)
			return false;
		
		sb.replace(index, index + before.length(), ped.after);
		return true;
	}
	
	protected static boolean isVowel(char ch) {
		return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u';
	}

	protected static boolean isConsonant(char ch) {
		return Character.isLetter(ch) && !isVowel(ch);
	}
}
